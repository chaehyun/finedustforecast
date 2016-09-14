package ch.test_viewpager.view;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ch.test_viewpager.R;
import ch.test_viewpager.model.pref.AppSharedPreferences;
import ch.test_viewpager.view.custom.CustomAdapterSavedAddress;
import ch.test_viewpager.view.intent.SearchAddress;


/*
 * A simple {@link Fragment} subclass.
 */
public class FragmentD extends Fragment implements View.OnClickListener {

    AppSharedPreferences pref;
    ViewPager viewPager = null;
    ImageView add;

    EditText[] edit;
    ImageView[] img_delete;

    String[] pref_name;
    String[] pref_name_umd;
    String[] pref_name_tmX;
    String[] pref_name_tmY;

    public FragmentD() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_d, container, false);

        pref = new AppSharedPreferences(getActivity());

        edit = new EditText[3];
        img_delete = new ImageView[3];

        edit[0] = (EditText) v.findViewById(R.id.editText1);
        edit[1] = (EditText) v.findViewById(R.id.editText2);
        edit[2] = (EditText) v.findViewById(R.id.editText3);

        img_delete[0] = (ImageView) v.findViewById(R.id.img_button_minus1);
        img_delete[1] = (ImageView) v.findViewById(R.id.img_button_minus2);
        img_delete[2] = (ImageView) v.findViewById(R.id.img_button_minus3);

        img_delete[0].setEnabled(false);
        img_delete[1].setEnabled(false);
        img_delete[2].setEnabled(false);
        img_delete[0].setVisibility(v.INVISIBLE);
        img_delete[1].setVisibility(v.INVISIBLE);
        img_delete[2].setVisibility(v.INVISIBLE);



        pref_name = new String[3];
        pref_name[0] = "LocationSave_One_Addr";
        pref_name[1] = "LocationSave_Two_Addr";
        pref_name[2] = "LocationSave_Three_Addr";

        pref_name_umd = new String[3];
        pref_name_umd[0] = "LocationSave_One_Umd";
        pref_name_umd[1] = "LocationSave_Two_Umd";
        pref_name_umd[2] = "LocationSave_Three_Umd";

        pref_name_tmX = new String[3];
        pref_name_tmX[0] = "LocationSave_One_TmX";
        pref_name_tmX[1] = "LocationSave_Two_TmX";
        pref_name_tmX[2] = "LocationSave_Three_TmX";

        pref_name_tmY = new String[3];
        pref_name_tmY[0] = "LocationSave_One_TmY";
        pref_name_tmY[1] = "LocationSave_Two_TmY";
        pref_name_tmY[2] = "LocationSave_Three_TmY";


        for(int i=0; i<3; i++)
        {
            String temp = pref.getValue(pref_name[i],"");
            String TmX = pref.getValue(pref_name_tmX[i],"");
            String TmY = pref.getValue(pref_name_tmY[i],"");

            System.out.println("CHECK : " + temp + " / " + TmX + " , " + TmY);

            if(!temp.equals("")) {
                edit[i].setText(temp);
                img_delete[i].setEnabled(true);
                img_delete[i].setVisibility(v.VISIBLE);
            }

        }


        //button onClickListener 등록
        for(int i=0; i<3; i++)
        {
            img_delete[i].setOnClickListener(this);
            edit[i].setOnClickListener(this);
        }


        add = (ImageView) v.findViewById(R.id.img_button_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , SearchAddress.class);

                //Intent에서 작업한 결과를 리턴 받기
                //startActivity(intent);
                startActivityForResult(intent, 0);
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK && requestCode ==0)
        {
            String addr = data.getStringExtra("ADDR");
            String TmX = data.getStringExtra("TmX");
            String TmY = data.getStringExtra("TmY");
            String Umd = data.getStringExtra("Umd");

            if(edit[0].getText().toString().equals(""))
            {
                edit[0].setText(addr);
                pref.put(pref_name[0], addr);
                pref.put(pref_name_umd[0], Umd);
                pref.put(pref_name_tmX[0] , TmX);
                pref.put(pref_name_tmY[0] , TmY);
                img_delete[0].setEnabled(true);
                img_delete[0].setVisibility(getView().VISIBLE);

            }
            else if(edit[1].getText().toString().equals(""))
            {
                edit[1].setText(addr);
                pref.put(pref_name[1], addr);
                pref.put(pref_name_umd[1], Umd);
                pref.put(pref_name_tmX[1] , TmX);
                pref.put(pref_name_tmY[1] , TmY);
                img_delete[1].setEnabled(true);
                img_delete[1].setVisibility(getView().VISIBLE);
            }
            else if(edit[2].getText().toString().equals(""))
            {
                edit[2].setText(addr);
                pref.put(pref_name[2], addr);
                pref.put(pref_name_umd[2], Umd);
                pref.put(pref_name_tmX[2] , TmX);
                pref.put(pref_name_tmY[2] , TmY);
                img_delete[2].setEnabled(true);
                img_delete[2].setVisibility(getView().VISIBLE);
            }
            else
            {
                Toast.makeText(getContext(), "더 이상 저장 할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.img_button_minus1:
                pref.removeValue(pref_name[0]);
                pref.removeValue(pref_name_umd[0]);
                pref.removeValue(pref_name_tmX[0]);
                pref.removeValue(pref_name_tmY[0]);
                pref.put("CurrentMode", "CURR");

                edit[0].setText("");
                img_delete[0].setEnabled(false);
                img_delete[0].setVisibility(getView().INVISIBLE);

                break;
            case R.id.img_button_minus2:
                pref.removeValue(pref_name[1]);
                pref.removeValue(pref_name_umd[1]);
                pref.removeValue(pref_name_tmX[1]);
                pref.removeValue(pref_name_tmY[1]);
                pref.put("CurrentMode", "CURR");

                edit[1].setText("");
                img_delete[1].setEnabled(false);
                img_delete[1].setVisibility(getView().INVISIBLE);
                break;
            case R.id.img_button_minus3:
                pref.removeValue(pref_name[2]);
                pref.removeValue(pref_name_umd[2]);
                pref.removeValue(pref_name_tmX[2]);
                pref.removeValue(pref_name_tmY[2]);
                pref.put("CurrentMode", "CURR");

                edit[2].setText("");
                img_delete[2].setEnabled(false);
                img_delete[2].setVisibility(getView().INVISIBLE);
                break;

            case R.id.editText1:
                pref.put("CurrentMode", "ONE");
                //Toast.makeText(getActivity(), "1번 선택위치",Toast.LENGTH_SHORT).show();

                break;
            case R.id.editText2:
                pref.put("CurrentMode", "TWO");
                //Toast.makeText(getActivity(), "2번 선택위치",Toast.LENGTH_SHORT).show();

                break;
            case R.id.editText3:
                pref.put("CurrentMode", "THREE");
                //Toast.makeText(getActivity(), "3번 선택위치",Toast.LENGTH_SHORT).show();

                break;

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
