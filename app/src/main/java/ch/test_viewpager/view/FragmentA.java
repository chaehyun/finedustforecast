package ch.test_viewpager.view;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import ch.test_viewpager.R;
import ch.test_viewpager.model.data.GPSData;
import ch.test_viewpager.model.data.GetMsrstnAcctoRltmMesureDnsty;
import ch.test_viewpager.model.data.GetNearByMsrstnList;
import ch.test_viewpager.model.data.URLName;
import ch.test_viewpager.model.pref.AppSharedPreferences;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentA extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    AppSharedPreferences pref;
    String currentMode = null;

    private Button btn[] = new Button[2];
    private ProgressBar pb_location;
    private TextView tv_current_location, tv_stn_addr;
    private TextView mTextView;

    private TextView[] tv_data_hour = new TextView[3];
    private TextView[] tv_data_24hour = new TextView[3];
    private TextView[] tv_date = new TextView[3];
    private TextView[] tv_state_msg = new TextView[3];
    private TextView[] tv_degree = new TextView[4];
    private ImageView[] imageView_state = new ImageView[7];

    private Button[] saved_location = new Button[3];
    private boolean[] empty = new boolean[3];

    public GPSData CoordData = null;

    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;

    //String gps_x, gps_y;
    private Vector<GetNearByMsrstnList> getNearByMsrstnListVector;
    private GetMsrstnAcctoRltmMesureDnsty getMsrstnAcctoRltmMesureDnsty;


    public FragmentA() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_a, container, false);

        pref = new AppSharedPreferences(getActivity());

        empty[0] = false; empty[1] = false; empty[2] = false;

        //SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        //boolean hasVisited  = sp.getBoolean("hasVisited", true);
        boolean hasVisited = pref.getValue("hasVisited", false);
        if(!hasVisited)
        {
            //SharedPreferences.Editor editor = sp.edit();
            //editor.putBoolean("hasVisited", false).apply();
            pref.put("hasVisited", true);
            pref.put("CurrentMode", "CURR");
            Toast.makeText(getActivity(), "첫 번째 방문입니다!", Toast.LENGTH_SHORT).show();
        }

        saved_location[0] = (Button) v.findViewById(R.id.btn_saved_location1);
        saved_location[1] = (Button) v.findViewById(R.id.btn_saved_location2);
        saved_location[2] = (Button) v.findViewById(R.id.btn_saved_location3);

        for(int i=0; i<3; i++)
            saved_location[i].setText("-");

        if(!pref.getValue("LocationSave_One_Umd","").equals("")) {
            saved_location[0].setText(pref.getValue("LocationSave_One_Umd",""));
        }
        if(!pref.getValue("LocationSave_Two_Umd","").equals("")) {
            saved_location[1].setText(pref.getValue("LocationSave_Two_Umd",""));
        }
        if(!pref.getValue("LocationSave_Three_Umd","").equals("")) {
            saved_location[2].setText(pref.getValue("LocationSave_Three_Umd",""));
        }

        //Find View
        pb_location = (ProgressBar) v.findViewById(R.id.progress_location);
        tv_current_location = (TextView) v.findViewById(R.id.tv_current_location);
        tv_stn_addr = (TextView)  v.findViewById(R.id.tv_stn_addr);
        mTextView = (TextView) v.findViewById(R.id.text_current_location);

        btn[0] = (Button) v.findViewById(R.id.btn_get);
        btn[0].setOnClickListener(this);

        btn[1] = (Button) v.findViewById(R.id.btn_curr);
        btn[1].setOnClickListener(this);

        for(int i=0; i < 3; i++)
        {
            saved_location[i].setOnClickListener(this);
        }
        

        pb_location.setVisibility(View.INVISIBLE);
        getCoordDatafromGPS();

        // Inflate the layout for this fragment
        return v;
    }

    void getCoordDatafromGPS(){

        //pref에서 CurrentMode 확인
        //현재위치일때 "CURR" / 아닐때 "ONE" / "TWO" / "THREE" 반환
        currentMode = pref.getValue("CurrentMode", "");
        Log.i("CurrentMode" , currentMode);


        if(currentMode.equals("ONE"))
        {
            CoordData = new GPSData();

            String Addr = pref.getValue("LocationSave_One_Addr", "");
            String tmX = pref.getValue("LocationSave_One_TmX", "");
            String tmY = pref.getValue("LocationSave_One_TmY", "");

            CoordData.setTm_x(Double.parseDouble(tmX));
            CoordData.setTm_y(Double.parseDouble(tmY));

            mTextView.setText("선택위치 : ");
            tv_current_location.setText(Addr);

            new GetNearByMsrstnListTask().execute();
        }
        else if(currentMode.equals("TWO"))
        {
            CoordData = new GPSData();

            String Addr = pref.getValue("LocationSave_Two_Addr", "");
            String tmX = pref.getValue("LocationSave_Two_TmX", "");
            String tmY = pref.getValue("LocationSave_Two_TmY", "");

            CoordData.setTm_x(Double.parseDouble(tmX));
            CoordData.setTm_y(Double.parseDouble(tmY));

            mTextView.setText("선택위치 : ");
            tv_current_location.setText(Addr);

            new GetNearByMsrstnListTask().execute();
        }
        else if(currentMode.equals("THREE")) {
            CoordData = new GPSData();

            String Addr = pref.getValue("LocationSave_Three_Addr", "");
            String tmX = pref.getValue("LocationSave_Three_TmX", "");
            String tmY = pref.getValue("LocationSave_Three_TmY", "");

            CoordData.setTm_x(Double.parseDouble(tmX));
            CoordData.setTm_y(Double.parseDouble(tmY));

            mTextView.setText("선택위치 : ");
            tv_current_location.setText(Addr);

            new GetNearByMsrstnListTask().execute();
        }
        else    // currentMode가 CURR인 경우 (default)
        {
            CoordData = new GPSData();
            mTextView.setText("현재위치 : ");

            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            //Lauch
            mGoogleApiClient.connect();
        }


    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.btn_get:
                tv_current_location.setText("Loading..");
                tv_stn_addr.setText("Loading..");

                getCoordDatafromGPS();
                break;
            case R.id.btn_curr:
                pref.put("CurrentMode", "CURR");

                tv_current_location.setText("Loading..");
                tv_stn_addr.setText("Loading..");
                getCoordDatafromGPS();

                break;

            case R.id.btn_saved_location1:
                Button btn = (Button) getActivity().findViewById(R.id.btn_saved_location1);
                String get = btn.getText().toString();
                if(!get.equals("-"))
                {
                    pref.put("CurrentMode", "ONE");
                    tv_current_location.setText("Loading..");
                    tv_stn_addr.setText("Loading..");
                    getCoordDatafromGPS();
                }
                else {
                    Toast.makeText(getActivity(), "등록된 주소지가 없습니다.", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btn_saved_location2:
                btn = (Button) getActivity().findViewById(R.id.btn_saved_location2);
                get = btn.getText().toString();
                if(!get.equals("-"))
                {
                    pref.put("CurrentMode", "TWO");
                    tv_current_location.setText("Loading..");
                    tv_stn_addr.setText("Loading..");
                    getCoordDatafromGPS();
                }
                else {
                    Toast.makeText(getActivity(), "등록된 주소지가 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_saved_location3:
                btn = (Button) getActivity().findViewById(R.id.btn_saved_location3);
                get = btn.getText().toString();
                if(!get.equals("-"))
                {
                    pref.put("CurrentMode", "THREE");
                    tv_current_location.setText("Loading..");
                    tv_stn_addr.setText("Loading..");
                    getCoordDatafromGPS();
                }
                else {
                    Toast.makeText(getActivity(), "등록된 주소지가 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //implementation for GoogleApiClient
    @Override
    public void onConnected(Bundle bundle) {

        pb_location.setVisibility(View.VISIBLE);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null) {
            Geocoder gcd = new Geocoder(getActivity().getBaseContext(), Locale.KOREA);
            List<Address> addr;
            String getAddr = null;

            //Getting GPS Coordinates
            CoordData.setWgs_x(mLastLocation.getLongitude());
            CoordData.setWgs_y(mLastLocation.getLatitude());

            //gps_x = String.valueOf(CoordData.getWgs_x());
            //gps_y = String.valueOf(CoordData.getWgs_y());

            Log.i("GPS", "x : "+ CoordData.getWgs_x() + " , y : "+ CoordData.getWgs_y());

            try {
                addr = gcd.getFromLocation(CoordData.getWgs_y(), CoordData.getWgs_x(), 1);

                if(addr != null && addr.size() > 0) {
                    getAddr = addr.get(0).getAdminArea() + " " + addr.get(0).getLocality() + " " + addr.get(0).getThoroughfare();

                    tv_current_location.setText(getAddr);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //좌표변환 호출 - Daum API
            Log.d("PROCEDURE", "DaumCoordTransApiTask() run.");
            new DaumCoordTransApiTask().execute();
        }
        else {
            tv_current_location.setText("Fail");
        }

        pb_location.setVisibility(View.INVISIBLE);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    //Input      : WGS84 based 좌표
    //Purpose    : WGS84좌표를 TM좌표로 변환한다. 다음 좌표변환 API를 사용한다. JSON
    public class DaumCoordTransApiTask extends AsyncTask<String, Void, String> {

        //URL Generate
        final static String mainURL = "https://apis.daum.net/local/geo/transcoord?";
        String apikey = "apikey=f9946de16d015bc728f1d55ceafe0c2e";
        String fromCoord = "&fromCoord=wgs84&";
        String x_val = "&x=";
        String y_val = "&y=";
        String toCoord = "&toCoord=TM";
        String type = "&output=json";

        @Override
        protected String doInBackground(String... strings) {
            String urlString = mainURL + apikey + fromCoord + x_val + CoordData.getWgs_x() + y_val + CoordData.getWgs_y() + toCoord + type;

            System.out.println("DaumAPiURL : " + urlString);
            try {
                URL url = new URL(urlString);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                Log.i("Response code","(URL CONNECTION) " + urlConnection.getResponseCode());

                //Get InputStream from URL Connection
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                //InputStream을 String Builder 사용하여 String에 저장하는 작업
                BufferedReader br = null;
                StringBuilder sb = new StringBuilder();
                String line;

                if(urlConnection.getResponseCode() >= 200 && urlConnection.getResponseCode() <= 300) {
                    br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                }
                else {
                    br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                }

                while( (line = br.readLine()) != null) {
                    sb.append(line);
                }

                br.close();
                urlConnection.disconnect();

                System.out.println("SB : " +sb);

                return sb.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);

            Log.i("TAG",s);

            try {
                JSONObject json = new JSONObject(s);

                //When API is not working, The app is finished.
                //좌표 변환 실패할 경우 앱이 강제종료 되지 않도록 수정이 필요함.
                //parseJSON
                CoordData.setTm_x(json.getDouble("x"));
                CoordData.setTm_y(json.getDouble("y"));

                Log.i("GPS(TM)","x :" + CoordData.getTm_x() + " , y : " + CoordData.getTm_y());

                //Run : 좌표기준 가까운 대기정보 측정소 정보 불러오기.
                new GetNearByMsrstnListTask().execute();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    //Input      : TM 좌표
    //Purpose    : 좌표 근처의 대기측정정보소의 목록을 받아온다.
    public class GetNearByMsrstnListTask extends AsyncTask<String, Void, String> {

        String urlName = makeURL();

        @Override
        protected String doInBackground(String... strings) {

            try {

                URL url = new URL(urlName);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();




                BufferedReader br;
                if(urlConnection.getResponseCode() >= 200 && urlConnection.getResponseCode() <=300) {
                    br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                }
                else {
                    br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                }

                StringBuilder sb = new StringBuilder();
                String line;

                while( (line = br.readLine()) != null ) {
                    sb.append(line);
                }

                br.close();
                urlConnection.disconnect();

                return sb.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            getNearByMsrstnListVector = new Vector<>();

            try {
                JSONObject json = new JSONObject(s);

                JSONArray resultJson = json.getJSONArray("list");

                String totalCount = json.getString("totalCount");
                int size = Integer.parseInt(totalCount);
                for(int i = 0; i < size; i++) {

                    //JSON List Array size만큼 item get
                    JSONObject itemJson = resultJson.getJSONObject(i);

                    GetNearByMsrstnList data = new GetNearByMsrstnList();
                    data.setStationName(itemJson.getString("stationName"));
                    data.setAddr(itemJson.getString("addr"));
                    data.setTm(itemJson.getString("tm"));

                    System.out.println("Check_JSONObject : " + data.getStationName() + " , " + data.getAddr() + " , " + data.getTm());

                    getNearByMsrstnListVector.add(data);
                }

                if(!getNearByMsrstnListVector.isEmpty()) {

                    Vector<GetNearByMsrstnList> nearStn = getNearByMsrstnListVector;

                    tv_stn_addr.setText(nearStn.get(0).getStationName() + " , "+ nearStn.get(0).getTm() +"km" +"\n"
                    + nearStn.get(0).getAddr());

                    //Run : To get info about 대기상태 from a specific station
                    new GetMsrstnAcctoRltmMesureDnstyTask().execute();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String makeURL()
        {
            URLName urlName = new URLName();

            urlName.setMainURL("http://openapi.airkorea.or.kr/openapi/services/rest");
            urlName.setApiKey("&ServiceKey=eaMmar5hKNMuIksQ9vh69CxELySno05xLk1bRfx5nuDiMlHdwSm75%2B22iUruTfzfCDBz%2BMaKLlyzxESYFBLCRw%3D%3D");
            urlName.setServiceName("/MsrstnInfoInqireSvc");
            urlName.setOperationName("/getNearbyMsrstnList");
            urlName.setX_val("?tmX=");
            urlName.setY_val("&tmY=");
            urlName.setPageNo("&pageNo=1");
            urlName.setNumOfRows("&numOfRows=10");
            urlName.setReturnType("&_returnType=json");

            String url = urlName.getMainURL() + urlName.getServiceName() + urlName.getOperationName() + urlName.getX_val() + CoordData.getTm_x() + urlName.getY_val() + CoordData.getTm_y() + urlName.getPageNo() + urlName.getNumOfRows() + urlName.getApiKey() + urlName.getReturnType();

            System.out.println("Check_URL(A) : " + url);
            System.out.println("MakeURL->Coord : (" + CoordData.getTm_x() + " , " + CoordData.getTm_y() + ")");

            return url;
        }
    }

    //Input : 측정소 이름 , version = 1.1
    //Purpose : 해당 측정소의 대기상태 측정정보를 얻어오는 메소드
    public class GetMsrstnAcctoRltmMesureDnstyTask extends AsyncTask <String, Void, String> {


        //URL generate
        String urlName = makeUrl(getNearByMsrstnListVector.get(0).getStationName());

        @Override
        protected String doInBackground(String... strings) {

            try {
                //StringBuilder urlBuilder = new StringBuilder(urlName);

                URL url = new URL(urlName);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //urlConnection.setRequestMethod("GET");
                //urlConnection.setRequestProperty("Content-type", "application/json");
                Log.i("Response code","(URL CONNECTION) " + urlConnection.getResponseCode());


                BufferedReader br;
                if(urlConnection.getResponseCode() >= 200 && urlConnection.getResponseCode() <=300) {
                    br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                }
                else {
                    br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                }

                StringBuilder sb = new StringBuilder();
                String line;

                while( (line = br.readLine()) != null ) {
                    sb.append(line);
                }

                br.close();
                urlConnection.disconnect();

                return sb.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);

            GetMsrstnAcctoRltmMesureDnsty data = new GetMsrstnAcctoRltmMesureDnsty();


            try {
                JSONObject json = new JSONObject(s);

                JSONArray resultJson = json.getJSONArray("list");

                JSONObject itemJson = resultJson.getJSONObject(0);

                //JSON Value 읽었을때 - 값을 리턴할 경우 에러나는 케이스 수정필요함.
                data.setDataTime(itemJson.getString("dataTime"));
                data.setSo2Value(itemJson.getString("so2Value"));
                data.setCoValue(itemJson.getString("coValue"));
                data.setO3Value(itemJson.getString("o3Value"));
                data.setNo2Value(itemJson.getString("no2Value"));
                data.setPm10Value(itemJson.getString("pm10Value"));
                data.setPm10Value24(itemJson.getString("pm10Value24"));
                data.setPm25Value(itemJson.getString("pm25Value"));
                data.setPm25Value24(itemJson.getString("pm25Value24"));
                data.setKhaiValue(itemJson.getString("khaiValue"));
                data.setKhaiGrade(itemJson.getString("khaiGrade"));

                //except Grade values

                getMsrstnAcctoRltmMesureDnsty = data;

                tv_date[0] = (TextView) getView().findViewById(R.id.tv_date1);
                tv_date[1] = (TextView) getView().findViewById(R.id.tv_date2);
                tv_date[2] = (TextView) getView().findViewById(R.id.tv_date3);

                tv_data_hour[0] = (TextView) getView().findViewById(R.id.tv_khai_value);
                tv_data_hour[1] = (TextView) getView().findViewById(R.id.tv_pm10_value);
                tv_data_hour[2] = (TextView) getView().findViewById(R.id.tv_pm25_value);

                tv_data_24hour[1] = (TextView) getView().findViewById(R.id.tv_pm10_value24);
                tv_data_24hour[2] = (TextView) getView().findViewById(R.id.tv_pm25_value24);

                ////


                tv_date[0].setText(data.getDataTime());
                tv_date[1].setText(data.getDataTime());
                tv_date[2].setText(data.getDataTime());

                tv_data_hour[0].setText(data.getKhaiValue());

                tv_data_hour[1].setText(data.getPm10Value());
                tv_data_24hour[1].setText(data.getPm10Value24());

                tv_data_hour[2].setText(data.getPm25Value());
                tv_data_24hour[2].setText(data.getPm25Value24());

                imageSetting(imageView_state[0], tv_state_msg[0], 0 , data.getKhaiValue());
                imageSetting(imageView_state[1], tv_state_msg[1], 1 , data.getPm10Value());
                imageSetting(imageView_state[2], tv_state_msg[2], 2 , data.getPm25Value());

                imageSetting(imageView_state[3], tv_degree[0], 3 , data.getO3Value());
                imageSetting(imageView_state[4], tv_degree[1], 4 , data.getNo2Value());
                imageSetting(imageView_state[5], tv_degree[2], 5 , data.getCoValue());
                imageSetting(imageView_state[6], tv_degree[3], 6 , data.getSo2Value());
/*
                //출력확인부분
                if(data != null) {
                    Log.i("DATA","dataTime : " + data.getDataTime());
                    Log.i("DATA","KhaiVal : " + data.getKhaiValue());
                    Log.i("DATA","So2Val : " + data.getSo2Value());
                    Log.i("DATA","CoVal : " + data.getCoValue());
                    Log.i("DATA","O3Val : " + data.getO3Value());
                    Log.i("DATA","No2Val : " + data.getNo2Value());
                    Log.i("DATA","Pm10Val : " + data.getPm10Value());
                    Log.i("DATA","Pm10Val24 : " + data.getPm10Value24());
                    Log.i("DATA","Pm25Val : " + data.getPm25Value());
                    Log.i("DATA","Pm25Val24 : " + data.getPm25Value24());


                }
*/
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        //URL 생성
        public String makeUrl(String station) {
            URLName urlName = new URLName();
            String str = null;

            ///ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?stationName=호림동&dataTerm=daily&pageNo=1&numOfRows=10&ServiceKey=eaMmar5hKNMuIksQ9vh69CxELySno05xLk1bRfx5nuDiMlHdwSm75%2B22iUruTfzfCDBz%2BMaKLlyzxESYFBLCRw%3D%3D&ver=1.1
            urlName.setMainURL("http://openapi.airkorea.or.kr/openapi/services/rest");
            urlName.setApiKey("&ServiceKey=eaMmar5hKNMuIksQ9vh69CxELySno05xLk1bRfx5nuDiMlHdwSm75%2B22iUruTfzfCDBz%2BMaKLlyzxESYFBLCRw%3D%3D");
            urlName.setServiceName("/ArpltnInforInqireSvc");
            urlName.setOperationName("/getMsrstnAcctoRltmMesureDnsty");
            urlName.setNumOfRows("&pageNo=1");
            urlName.setPageNo("&numOfRows=10");
            urlName.setVersion("&ver=1.1");
            urlName.setReturnType("&_returnType=json");

            urlName.setStationName("?stationName=");
            urlName.setDataTerm("&dataTerm=daily");

            str = urlName.getMainURL() + urlName.getServiceName() + urlName.getOperationName()
                    + urlName.getStationName() + station + urlName.getDataTerm() + urlName.getPageNo() +urlName.getNumOfRows()
                    + urlName.getApiKey() + urlName.getVersion() + urlName.getReturnType();

            System.out.println("Check_URL : " + str);

            return str;
        }

        //category : (0 , khai) , (1 , pm10) , (2 , pm25) , (3 , O3) , (4,NO2) , (5 , CO), (6 , SO2)
        public void imageSetting(ImageView img ,TextView tv, int category, String StringValue) {
            //Case : Khai(통합대기) -> 0~50 좋음 , 51~100 보통 , 101~250 나쁨 , 251 ~ 심각
            //Case : pm10(미세먼지) -> 0~30 좋음 , 31~80 보통 , 81~150 나쁨 , 151 ~ 심각
            //Case : pm25(초미세먼지) -> 0~15 좋음 , 16~50 보통 , 51~100 나쁨 , 101 ~ 심각
            //Case : O3(오존) -> 0~0.03 좋음 , 0.031~0.09 보통 , 0.091~0.15 나쁨 , 0.151 ~ 심각
            //Case : NO2(이산화질소) -> 0~03 좋음 , 0.031~0.06 보통 , 0.061~0.20 나쁨 , 0.201 ~ 심각
            //Case : CO(일산화탄소) -> 0~2.0 좋음 , 2.01~9.0 보통 , 9.01~15.0 나쁨 , 15.01 ~ 심각
            //Case : SO2(아황산가스) -> 0~0.02 좋음 , 0.021~0.05 보통 , 0.051~0.15 나쁨 , 0.151 ~ 심각

            //value를 double이 아니라 string으로 받은 후에 함수안에서 Double로 파싱하기
            //Why? 측정값이 없을 경우 double이 아닌 '-'를 리턴해서 에러가 발생함.
            double value;

            if(!StringValue.equals("-"))    //측정값을 리턴한 경우
            {
                value = Double.parseDouble(StringValue);

                Drawable resource = null;
                switch(category){
                    case 0:     //khai
                        img = (ImageView) getView().findViewById(R.id.image_state_khai);
                        tv = (TextView) getView().findViewById(R.id.tv_state_1);
                        if(value >= 0 && value <= 50) {     //좋음으로 세팅
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_best, null);
                            tv_data_hour[0].setTextColor(Color.rgb(0, 150, 250));
                            tv.setText("오늘 통합대기환경지수는 좋음입니다.");
                        }
                        else if(value <= 100) {     //보통으로 세팅
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_good, null);
                            tv_data_hour[0].setTextColor(Color.rgb(120,190, 80));
                            tv.setText("오늘 통합대기환경지수는 보통입니다.");
                        }
                        else if(value <= 250) {
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_bad, null);
                            tv_data_hour[0].setTextColor(Color.rgb(255,220,80));
                            tv.setText("오늘 통합대기환경지수는 나쁨입니다.");
                        }
                        else {
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_verybad, null);
                            tv_data_hour[0].setTextColor(Color.rgb(255,80,80));
                            tv.setText("오늘 통합대기환경지수는 심각입니다.");
                        }
                        img.setImageDrawable(resource);
                        break;

                    case 1: //pm10
                        img = (ImageView) getView().findViewById(R.id.image_state_pm10);
                        tv = (TextView) getView().findViewById(R.id.tv_state_2);
                        if(value >= 0 && value <= 30) {     //좋음으로 세팅
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_best, null);
                            tv_data_hour[1].setTextColor(Color.rgb(0, 150, 250));
                            tv_data_24hour[1].setTextColor(Color.rgb(0, 150, 250));
                            tv.setText("오늘 미세먼지농도는 좋음입니다.");
                        }
                        else if(value <= 80) {     //보통으로 세팅
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_good, null);
                            tv_data_hour[1].setTextColor(Color.rgb(120,190, 80));
                            tv_data_24hour[1].setTextColor(Color.rgb(120,190, 80));
                            tv.setText("오늘 미세먼지농도는 보통입니다.");
                        }
                        else if(value <= 150) {
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_bad, null);
                            tv_data_hour[1].setTextColor(Color.rgb(255,220,80));
                            tv_data_24hour[1].setTextColor(Color.rgb(255,220,80));
                            tv.setText("오늘 미세먼지농도는 나쁨입니다.");
                        }
                        else {
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_verybad, null);
                            tv_data_hour[1].setTextColor(Color.rgb(255,80,80));
                            tv_data_24hour[1].setTextColor(Color.rgb(255,80,80));
                            tv.setText("오늘 미세먼지농도는 심각입니다.");
                        }
                        img.setImageDrawable(resource);
                        break;

                    case 2: //pm25
                        img = (ImageView) getView().findViewById(R.id.image_state_pm25);
                        tv = (TextView) getView().findViewById(R.id.tv_state_3);
                        if(value >= 0 && value <= 15) {     //좋음으로 세팅
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_best, null);
                            tv_data_hour[2].setTextColor(Color.rgb(0, 150, 250));
                            tv_data_24hour[2].setTextColor(Color.rgb(0, 150, 250));
                            tv.setText("초미세먼지농도는 좋음입니다.");
                        }
                        else if(value <= 50) {     //보통으로 세팅
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_good, null);
                            tv_data_hour[2].setTextColor(Color.rgb(120,190, 80));
                            tv_data_24hour[2].setTextColor(Color.rgb(120,190, 80));
                            tv.setText("초미세먼지농도는 보통입니다.");
                        }
                        else if(value <= 100) {
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_bad, null);
                            tv_data_hour[2].setTextColor(Color.rgb(255,220,80));
                            tv_data_24hour[2].setTextColor(Color.rgb(255,220,80));
                            tv.setText("초미세먼지농도는 나쁨입니다.");
                        }
                        else {
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_verybad, null);
                            tv_data_hour[2].setTextColor(Color.rgb(255,80,80));
                            tv_data_24hour[2].setTextColor(Color.rgb(255,80,80));
                            tv.setText("초미세먼지농도는 심각입니다.");
                        }
                        img.setImageDrawable(resource);
                        break;

                    case 3:     //o3
                        img = (ImageView) getView().findViewById(R.id.image_state_o3);
                        tv = (TextView) getView().findViewById(R.id.tv_degree_o3);
                        if(value >= 0 && value <= 0.03) {     //좋음으로 세팅
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_best_square, null);
                            tv.setTextColor(Color.rgb(0, 150, 250));
                        }
                        else if(value <= 0.09) {     //보통으로 세팅
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_good_square, null);
                            tv.setTextColor(Color.rgb(120,190, 80));
                        }
                        else if(value <= 0.15) {
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_bad_square, null);
                            tv.setTextColor(Color.rgb(255,220,80));
                        }
                        else {
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_verybad_square, null);
                            tv.setTextColor(Color.rgb(255,80,80));
                        }

                        tv.setText(String.valueOf(value));

                        img.setImageDrawable(resource);
                        break;

                    case 4:     //NO2
                        img = (ImageView) getView().findViewById(R.id.image_state_no2);
                        tv = (TextView) getView().findViewById(R.id.tv_degree_no2);
                        if(value >= 0 && value <= 0.03) {     //좋음으로 세팅
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_best_square, null);
                            tv.setTextColor(Color.rgb(0, 150, 250));
                        }
                        else if(value <= 0.06) {     //보통으로 세팅
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_good_square, null);
                            tv.setTextColor(Color.rgb(120,190, 80));
                        }
                        else if(value <= 0.2) {
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_bad_square, null);
                            tv.setTextColor(Color.rgb(255,220,80));
                        }
                        else {
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_verybad_square, null);
                            tv.setTextColor(Color.rgb(255,80,80));
                        }

                        tv.setText(String.valueOf(value));
                        img.setImageDrawable(resource);
                        break;
                    case 5:     //CO
                        img = (ImageView) getView().findViewById(R.id.image_state_co);
                        tv = (TextView) getView().findViewById(R.id.tv_degree_co);
                        if(value >= 0 && value <= 2.0) {     //좋음으로 세팅
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_best_square, null);
                            tv.setTextColor(Color.rgb(0, 150, 250));
                        }
                        else if(value <= 9.0) {     //보통으로 세팅
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_good_square, null);
                            tv.setTextColor(Color.rgb(120,190, 80));
                        }
                        else if(value <= 15.0) {
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_bad_square, null);
                            tv.setTextColor(Color.rgb(255,220,80));
                        }
                        else {
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_verybad_square, null);
                            tv.setTextColor(Color.rgb(255,80,80));
                        }

                        tv.setText(String.valueOf(value));
                        img.setImageDrawable(resource);
                        break;
                    case 6:     //SO2
                        img = (ImageView) getView().findViewById(R.id.image_state_so2);
                        tv = (TextView) getView().findViewById(R.id.tv_degree_so2);
                        if(value >= 0 && value <= 0.02) {     //좋음으로 세팅
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_best_square, null);
                            tv.setTextColor(Color.rgb(0, 150, 250));
                        }
                        else if(value <= 0.05) {     //보통으로 세팅
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_good_square, null);
                            tv.setTextColor(Color.rgb(120,190, 80));
                        }
                        else if(value <= 0.15) {
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_bad_square, null);
                            tv.setTextColor(Color.rgb(255,220,80));
                        }
                        else {
                            resource = ResourcesCompat.getDrawable(getResources(), R.drawable.statebutton_verybad_square, null);
                            tv.setTextColor(Color.rgb(255,80,80));
                        }

                        tv.setText(String.valueOf(value));
                        img.setImageDrawable(resource);
                        break;
                }
            }
            else    //'-'를 리턴한 경우
            {

            }


        }


    }


}
