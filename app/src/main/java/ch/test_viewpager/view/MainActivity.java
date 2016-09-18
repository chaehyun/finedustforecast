package ch.test_viewpager.view;



import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;

import ch.test_viewpager.R;
import ch.test_viewpager.view.custom.MyViewPagerAdapter;

public class MainActivity extends FragmentActivity implements OnClickListener {

    Button btn[] = new Button[4];
    LinearLayout layout;
    ViewPager viewPager = null;
    Thread thread = null;
    Handler handler = null;
    int p = 0; // page
    int v = 1; // direction of changing activity



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Font_Setting
        Typekit.getInstance().
                addNormal(Typekit.createFromAsset(this, "BMJUA_ttf.ttf"))
                .addBold(Typekit.createFromAsset(this, "BMDOHYEON_ttf.ttf"));


        //Check if GPS is ON mode or not
        //GPS should be ON to get Coordinates data

        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            createGpsDisabledAlert();
        }

        layout = (LinearLayout) findViewById(R.id.layout_menubtns);
        btn[0] = (Button)findViewById(R.id.btn_a);
        btn[1] = (Button)findViewById(R.id.btn_b);
        btn[2] = (Button)findViewById(R.id.btn_c);
        btn[3] = (Button)findViewById(R.id.btn_d);

        Typeface font = Typeface.createFromAsset(getAssets(), "BMJUA_ttf.ttf");

        for(int i = 0; i < btn.length; i++)
        {
            btn[i].setOnClickListener(this);
            btn[i].setTypeface(font);
        }

        //viewPager
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i< btn.length; i++)
                    btn[i].setSelected(false);

                btn[position].setSelected(true);

                switch(position) {
                    case 0:
                        layout.setBackgroundColor(Color.argb(200,100,180,246));
                        break;
                    case 1:
                        layout.setBackgroundColor(Color.argb(200,255,229,127));
                        break;
                    case 2:
                        layout.setBackgroundColor(Color.argb(200,240,98,146));
                        break;
                    case 3:
                        layout.setBackgroundColor(Color.argb(200,129,199,132));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        btn[0].setSelected(true);
        layout.setBackgroundColor(Color.argb(200,100,180,246));

        viewPager.setAdapter(adapter);
/*
        handler = new Handler() {

            public void handleMessage(android.os.Message msg) {
                if(p == 0) {
                    viewPager.setCurrentItem(1);
                    p++;
                    v = 1;
                }
                if(p == 1 && v == 0) {
                    viewPager.setCurrentItem(1);
                    p--;
                }
                if(p == 1 && v == 1) {
                    viewPager.setCurrentItem(2);
                    p++;
                }
                if(p == 2) {
                    viewPager.setCurrentItem(1);
                    p--;
                    v = 0;
                }
            }
        };

        thread = new Thread() {
            public void run() {
                super.run();
                while(true) {
                    try {
                        Thread.sleep(2000);
                        handler.sendEmptyMessage(0);
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
*/
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_a:
                viewPager.setCurrentItem(0);
                Toast.makeText(this, "대기상태 화면입니다.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_b:
                viewPager.setCurrentItem(1);
                Toast.makeText(this, "통계그래프 화면입니다.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_c:
                viewPager.setCurrentItem(2);
                Toast.makeText(this, "알람설정 화면입니다.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_d:
                viewPager.setCurrentItem(3);
                Toast.makeText(this, "전체설정 화면입니다.", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }

    }

    public ViewPager getViewPager() {
        if (null == viewPager) {
            viewPager = (ViewPager) findViewById(R.id.viewPager);
        }
        return viewPager;
    }

    public void setCurrentPagerItem(int item)
    {
        viewPager.setCurrentItem(item);
    }

    //시작 시 GPS 상태를 확인하는 메소드
    private void createGpsDisabledAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GPS가 꺼져있습니다.")
                .setCancelable(false)
                .setPositiveButton("GPS ON",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                showGpsOptions();
                            }
                        })
                .setNegativeButton("GPS OFF",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //GPS를 켜짐 상태로 바꿀 수 있도록 Intent를 실행시켜주는 메소드
    private void showGpsOptions() {
        Intent gpsOptionsIntent = new  Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(gpsOptionsIntent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
