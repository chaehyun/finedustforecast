package ch.test_viewpager.view.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.test_viewpager.R;

/**
 * Created by CH on 2016-09-14.
 */
public class CustomAdapterSavedAddress extends BaseAdapter {

    private ArrayList<String> saved_address_list = new ArrayList<String>();

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return saved_address_list.size();
    }

    @Override
    public Object getItem(int i) {
        return saved_address_list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        ViewHolderSavedAddress holder;
        final int pos = i;

        if(view == null) {
            holder = new ViewHolderSavedAddress();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.saved_address_item, null);

            holder.saved_address = (TextView) view.findViewById(R.id.saved_address);
            holder.delete = (ImageView) view.findViewById(R.id.img_button_delete);

            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolderSavedAddress) view.getTag();
        }

        holder.saved_address.setText(saved_address_list.get(i));

        holder.delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //리스트뷰에서 삭제
                int position = pos;
                remove(pos);
            }
        });

        return view;
    }

    public void add(String data) {
        saved_address_list.add(data);
    }

    public void remove(int position) {
        saved_address_list.remove(position);
        notifyDataSetChanged();
    }
}
