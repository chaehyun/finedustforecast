package ch.test_viewpager.view.intent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.test_viewpager.R;
import ch.test_viewpager.control.MyVolley;
import ch.test_viewpager.model.customview.CustomAdapter;
import ch.test_viewpager.model.data.SearchListOfStationNames;

public class SearchAddress extends AppCompatActivity {

    //Context mContext = getApplicationContext();


    EditText edit_search;
    Button button_search;
    ListView list_address;
    TextView text;

    Intent intent;
    private CustomAdapter m_custom_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_address);

        final RequestQueue queue = MyVolley.getInstance(getApplication()).getRequestQueue();

        intent = new Intent();

        list_address = (ListView) findViewById(R.id.listView_address);

        //ListView에 CustomAdpater 연결
        m_custom_adapter = new CustomAdapter();
        list_address.setAdapter(m_custom_adapter);

        //ListView의 아이템 터치 시 이벤트 추가
        list_address.setOnItemClickListener(onClickListViewItem);

        edit_search = (EditText) findViewById(R.id.edit_search);
        button_search = (Button) findViewById(R.id.button_search);
        text = (TextView) findViewById(R.id.text_visible);

        //EditText Listener
        edit_search.setOnKeyListener(onKeySearchEdit);

        //검색 버튼을 눌렀을 때
        button_search.setOnClickListener(new View.OnClickListener() {

            final String TAG = "button_search_OnClick";
            @Override
            public void onClick(View view) {
                //키보드 숨김
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(button_search.getWindowToken(), 0);

                //중첩된 안내메세지 숨김
                text.setVisibility(View.INVISIBLE);

                //재검색시 ListView의 아이템을 초기화 하기 위해서 어댑터 재설정
                m_custom_adapter = new CustomAdapter();
                list_address.setAdapter(m_custom_adapter);

                //eidtText가 empty일 경우 예외처리
                //editText에 data가 입력되었을 경우만 검색
                if(!edit_search.getText().toString().equals(""))
                {

                    String search_name = edit_search.getText().toString();

                    Log.i(TAG,"검색타겟 : " + search_name);

                    String server_url = "http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getTMStdrCrdnt" +
                            "?pageNo=1&numOfRows=30&ServiceKey=eaMmar5hKNMuIksQ9vh69CxELySno05xLk1bRfx5nuDiMlHdwSm75%2B22iUruTfzfCDBz%2BMaKLlyzxESYFBLCRw%3D%3D" +
                            "&_returnType=json&umdName=" + search_name;
                    Log.v("CHECK_URL", server_url);

                    //주소 목록 JSON data를 받기 위해 HTTP 연결요청
                    //Request를 만들어서 queue에 추가한다.
                    JsonObjectRequest search_address_req = new JsonObjectRequest(Request.Method.POST, server_url, new JSONObject(),
                            new Response.Listener<JSONObject>() {

                                //queue에 추가한 작업을 실행하면 JSON을 받아 처리하는 곳.
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONArray list = response.getJSONArray("list");

                                        for(int i=0; i< list.length(); i++)
                                        {
                                            JSONObject jsonObject = list.getJSONObject(i);

                                            SearchListOfStationNames data = new SearchListOfStationNames();

                                            data.setSidoName(jsonObject.getString("sidoName"));
                                            data.setSggName(jsonObject.getString("sggName"));
                                            data.setUmdname(jsonObject.getString("umdName"));

                                            data.setAddress(data.getSidoName() + " " + data.getSggName() + " " + data.getUmdname());

                                            data.setTmX(jsonObject.getString("tmX"));
                                            data.setTmY(jsonObject.getString("tmY"));

                                            Log.v(TAG, "Address from JSON :" + data.getAddress());
                                            m_custom_adapter.add(data);
                                        }
                                        //ListView 갱신
                                        m_custom_adapter.notifyDataSetChanged();

                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.d("Error.Response", volleyError.toString());
                            Toast.makeText(getApplicationContext(), "Network Error1", Toast.LENGTH_SHORT).show();
                        }
                    });
                    queue.add(search_address_req);

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "검색어를 입력해주세요", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    //아이템 터치 시 -> 해당 ListView의 Address와 Coord 정보를 ActivityResult로 돌려준다.
    private AdapterView.OnItemClickListener onClickListViewItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            SearchListOfStationNames data = (SearchListOfStationNames) adapterView.getAdapter().getItem(i);
            //String temp = (String) adapterView.getAdapter().getItem(i);
            String addr = data.getAddress();
            String tmX = data.getTmX();
            String tmY = data.getTmY();

            //String msg = addr + "\n(" + tmX + " , " + tmY + ")";
            //Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();

            //intent에서 이전 Activity로 데이터 전달
            intent.putExtra("ADDR", addr);
            intent.putExtra("TmX", tmX);
            intent.putExtra("TmY", tmY);
            intent.putExtra("Umd", data.getUmdname());
            setResult(RESULT_OK, intent);

            finish();
        }
    };


    //EditText(Search)에서 Enter키 인식하여 키보드 숨김
    private View.OnKeyListener onKeySearchEdit = new View.OnKeyListener() {
        final String TAG = "OnKeyListner";
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            //버튼 눌렀을 때
            if(keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                Log.i(TAG, "KeyEvent.ACTION_DOWN");
            }
            //버튼 땔 때
            else if(keyEvent.getAction() == KeyEvent.ACTION_UP)
            {
                Log.i(TAG, "KeyEvent.ACTION_UP");
            }

            //Enter키 인식
            if(i == KeyEvent.KEYCODE_ENTER)
            {
                //키보드 숨김
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edit_search.getWindowToken(), 0);
                Log.d(TAG, "KeyEvent.KEYCODE_ENTER");

                return true;
            }
            return false;
        }
    };
}
