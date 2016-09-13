package ch.test_viewpager.view;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import ch.test_viewpager.R;
import ch.test_viewpager.model.data.GetMinuDustFrcstDspth;
import ch.test_viewpager.model.data.URLName;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentB_C extends Fragment {

    private TextView tv_today, tv_tomorroww, tv_dayafter;
    private Vector<GetMinuDustFrcstDspth> getMinuDustFrcstDspthVector;


    String[] imgURL;
    Bitmap[] weatherImage = new Bitmap[6];
    ImageView mImageView;
    private final int IMAGE_CNT = 10;
    int img_idx = 0;
    int img_cnt = IMAGE_CNT;



    private boolean imageStop = false;

    public FragmentB_C() {
        // Required empty public constructor
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("STATE_C", "DESTROY VIEW!!!!!!!");
        imageStop = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("STATE_C", "RESUME!!!!!!!");
        imageStop = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("STATE_C", "PAUSE!!!!!!!");
        imageStop = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_b_c, container, false);

        tv_today = (TextView) v.findViewById(R.id.tv_today);
        tv_tomorroww = (TextView) v.findViewById(R.id.tv_tomorrow);
        tv_dayafter = (TextView) v.findViewById(R.id.tv_dayafter);

        //Run : to get info about 예보정보(오늘/내일/모레)
        new GetMinuDustFrcstDspthTask().execute();

        return v;
    }

    //Input : URL
    //Purpose : 오늘 / 내일 / 모레 3개 날짜에 대한 예보 정보를 요청한다.
    public class GetMinuDustFrcstDspthTask extends AsyncTask<String, Void, String> {
        String urlName = makeURL();

        @Override
        protected String doInBackground(String... strings) {


            try {
                URL url = new URL(urlName);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
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
            getMinuDustFrcstDspthVector = new Vector<>();
            int cnt = 3; //상위 3개 정보만 읽는다. 오늘 내일 모레
            imgURL = new String[6];

            //읽을 json code값 : dataTime , informCause, informCode, informData, inforrmGrade, informOverall
            try {
                JSONObject json = new JSONObject(s);

                JSONArray resultJson = json.getJSONArray("list");

                for(int i = 0; i < cnt; i++)
                {
                    JSONObject itemJson = resultJson.getJSONObject(i);

                    GetMinuDustFrcstDspth data = new GetMinuDustFrcstDspth();
                    data.setDataTime(itemJson.getString("dataTime"));
                    data.setInformCause(itemJson.getString("informCause"));
                    data.setInformData(itemJson.getString("informData"));
                    data.setInformGrade(itemJson.getString("informGrade"));
                    data.setInformOverall(itemJson.getString("informOverall"));
                    if(i==1)        //내일 예보일 경우만
                    {
                        for(int j = 1; j<=6; j++)
                        {
                            if(!itemJson.isNull("imageUrl"+j))
                                imgURL[j-1] = itemJson.getString("imageUrl"+j);

                            System.out.println("ImageURL["+ j +"] : " + imgURL[j-1]);
                        }

                        //Bitmap 받아오기
                        Thread th_getBitmap = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                URL url = null;
                                InputStream is = null;
                                for(int idx=0; idx < 6; idx++) {
                                    try {
                                        url = new URL(imgURL[idx]);
                                        is = url.openStream();
                                        weatherImage[idx] = BitmapFactory.decodeStream(is);

                                        is.close();
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        th_getBitmap.start();
                        th_getBitmap.join();
                        mImageView = (ImageView) getView().findViewById(R.id.img_c);

                        //Bitmap 번갈아가면서 Load해주는 쓰레드
                        Thread th_setImageView = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(!imageStop) {
                                    try {
                                        //System.out.println("* C **Thread _ image Load , " + imageStop);
                                        imageView_handler.sendMessage(imageView_handler.obtainMessage());
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        th_setImageView.start();
                    }



                    Log.i("CHECK_JSON",data.getDataTime());

                    getMinuDustFrcstDspthVector.add(data);
                }
                if(!getMinuDustFrcstDspthVector.isEmpty()) {
                    Vector<GetMinuDustFrcstDspth> it = getMinuDustFrcstDspthVector;

                    TextView[] tv = new TextView[3];
                    tv[0] = tv_today;
                    tv[1] = tv_tomorroww;
                    tv[2] = tv_dayafter;

                    for(int i = 0; i<cnt; i++)
                    {
                        tv[i].setText("갱신시간 : " + it.get(i).getDataTime());
                        tv[i].append("\n예보날짜 : " + it.get(i).getInformData());
                        if(!it.get(i).getInformOverall().isEmpty())
                            tv[i].append("\n예보 : " + it.get(i).getInformOverall());
                        if(!it.get(i).getInformCause().isEmpty())
                            tv[i].append("\n예보근거 : " + it.get(i).getInformCause());
                        if(!it.get(i).getInformGrade().isEmpty() && i != 2)
                            tv[i].append("\n광역별 예보 : " + it.get(i).getInformGrade());
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        Handler imageView_handler = new Handler() {
            public void handleMessage(Message msg) {
                //System.out.println("index : " + img_idx);
                mImageView.setImageBitmap(weatherImage[img_idx]);
                img_idx++;
                //img_cnt--;
                if(img_idx == 6)    img_idx=0;
                if(img_cnt <= 0) {
                    imageStop = true;
                    img_cnt = IMAGE_CNT;
                }
            }
        };

        //URL : 서비스키
        //http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMinuDustFrcstDspth?searchDate=2016-07-20&searchCondition=DAILY
        String makeURL() {

            //현재 날짜구하기
            SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar mCalendar = Calendar.getInstance();
            String date;

            int hour = Integer.parseInt(String.valueOf(mCalendar.get(Calendar.HOUR_OF_DAY)));   //현재시간

            if(hour >= 0 && hour < 5){
                //아직 그 날의 첫번째 예보정보가 업데이트 되기 전이므로 어제 정보를 읽어오도록 한다.
                mCalendar.add(Calendar.DATE, -1);
                date = DateFormat.format(mCalendar.getTime());

                System.out.println("Hour : " + mCalendar.get(Calendar.HOUR_OF_DAY) +" , " + hour);
                System.out.println("Date(Yesterday) : " + mCalendar.get(Calendar.DATE));
                System.out.println("DateFormat(전날) : " + date);
            }
            else {
                date = DateFormat.format(mCalendar.getTime());

                System.out.println("Hour : " + mCalendar.get(Calendar.HOUR_OF_DAY) +" , " + hour);
                System.out.println("DateFormat(오늘) : " + date);
            }



            URLName url = new URLName();



            url.setMainURL("http://openapi.airkorea.or.kr/openapi/services/rest");
            url.setServiceName("/ArpltnInforInqireSvc");
            url.setOperationName("/getMinuDustFrcstDspth");
            url.setSearchDate("?searchDate=");
            url.setSearchCondition("&searchCondition=DAILY");
            url.setInformCode("&informCode=o3");
            url.setApiKey("&ServiceKey=eaMmar5hKNMuIksQ9vh69CxELySno05xLk1bRfx5nuDiMlHdwSm75%2B22iUruTfzfCDBz%2BMaKLlyzxESYFBLCRw%3D%3D");
            url.setReturnType("&_returnType=json");

            String urlName = url.getMainURL() + url.getServiceName() + url.getOperationName() + url.getSearchDate() + date
                    + url.getSearchCondition() + url.getInformCode() + url.getApiKey() + url.getReturnType();

            Log.i("CHEKC_URL", urlName);

            return urlName;
        }

    }

}
