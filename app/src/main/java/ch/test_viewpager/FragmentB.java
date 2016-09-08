package ch.test_viewpager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentB extends Fragment implements View.OnClickListener {


    Button btn_pm10, btn_pm25, btn_o3;

    FragmentManager childFragmentManager;
    FragmentTransaction childFragTrans;

    FragmentB_A FragB_A;
    FragmentB_B FragB_B;
    FragmentB_C FragB_C;

    public FragmentB() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_b, container, false);


        btn_pm10 = (Button) v.findViewById(R.id.btn_pm10);
        btn_pm25 = (Button) v.findViewById(R.id.btn_pm25);
        btn_o3 = (Button) v.findViewById(R.id.btn_o3);

        btn_pm10.setOnClickListener(this);
        btn_pm25.setOnClickListener(this);
        btn_o3.setOnClickListener(this);

        //childFragment
        childFragmentManager = getFragmentManager();
        childFragTrans = childFragmentManager.beginTransaction();

        FragB_A = new FragmentB_A();
        FragB_B = new FragmentB_B();
        FragB_C = new FragmentB_C();

        childFragTrans.add(R.id.fragment_placeholder, FragB_A);
//      childFragTrans.add(R.id.fragment_placeholder, FragB_B);
//      childFragTrans.add(R.id.fragment_placeholder, FragB_C);

        childFragTrans.commit();

        return v;
    }


    @Override
    public void onClick(View view) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction childFragTrans = fm.beginTransaction();

        switch(view.getId()){
            case R.id.btn_pm10:
                Toast.makeText(getContext(), "미세먼지 예보", Toast.LENGTH_SHORT).show();

                childFragTrans.replace(R.id.fragment_placeholder, FragB_A);
                childFragTrans.commit();

                break;
            case R.id.btn_pm25:
                Toast.makeText(getActivity(), "초미세먼지 예보", Toast.LENGTH_SHORT).show();

                childFragTrans.replace(R.id.fragment_placeholder, FragB_B);
                childFragTrans.commit();
                break;
            case R.id.btn_o3:
                Toast.makeText(getActivity(), "오존상태 예보", Toast.LENGTH_SHORT).show();

                childFragTrans.replace(R.id.fragment_placeholder, FragB_C);
                childFragTrans.commit();
                break;

        }

    }
}
