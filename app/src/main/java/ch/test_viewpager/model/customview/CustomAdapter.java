package ch.test_viewpager.model.customview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ch.test_viewpager.R;
import ch.test_viewpager.model.data.SearchListOfStationNames;

/**
 * Created by CH on 2016-09-14.
 */
public class CustomAdapter extends BaseAdapter {

    private ArrayList<SearchListOfStationNames> m_Obj_List = new ArrayList<SearchListOfStationNames>();

    public CustomAdapter() {
        super();
        this.m_Obj_List = new ArrayList<SearchListOfStationNames>();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return m_Obj_List.size();
    }

    @Override
    public Object getItem(int i) {
        return m_Obj_List.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        ViewHolder holder;

        if(view == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_item, null);

            holder.mText = (TextView) view.findViewById(R.id.text);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        SearchListOfStationNames data = m_Obj_List.get(i);
        holder.mText.setText(data.getAddress());


        return view;
    }

    //외부에서 아이템 추가 요청시
    public void add(SearchListOfStationNames data) {
        m_Obj_List.add(data);
    }

    //외부에서 아이템 삭제 요청시
    public void remove(int position) {
        m_Obj_List.remove(position);
        notifyDataSetChanged();
    }
}
