package ch.test_viewpager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/*
 * A simple {@link Fragment} subclass.
 */
public class FragmentD extends Fragment implements View.OnClickListener {


    ViewPager viewPager = null;

    public FragmentD() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_d, container, false);

        Button btn_home = (Button) v.findViewById(R.id.btn_home);
        btn_home.setOnClickListener(this);

        // Inflate the layout for this fragment
        return v;
    }

    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.btn_home:
                MainActivity temp = (MainActivity)getActivity();
                //temp.setCurrentPagerItem(0);
                temp.getViewPager().setCurrentItem(0);
                Toast.makeText(getActivity(), "Button in Fragment", Toast.LENGTH_SHORT).show();
                break;
        }

    }

}
