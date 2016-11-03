package com.spring.sky.menuproject.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.spring.sky.menuproject.R;

import java.util.List;

/**
 * Created by springsky on 16/10/20.
 */

public class MenuAdapter extends BaseAdapter {
    private Context mContext;
    private List<MenuModel> list;
    private int selectPosition =  -1;
    private int column;

    public MenuAdapter(Context context) {
        mContext = context;
    }

    public void setList(List<MenuModel> list) {
        this.list = list;
        selectPosition = -1;
        notifyDataSetChanged();
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.menu_item_layout, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        MenuModel model = list.get(position);
        holder.titleTv.setText(model.value);
        int icon = 0;
        if(position == selectPosition){
            holder.titleTv.setTextColor(mContext.getResources().getColor(R.color.orange));
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.while_color));
            if(model.hasChind()){
                icon = R.mipmap.icon_chose_arrow_sel;
            }
        }else {
            holder.titleTv.setTextColor(mContext.getResources().getColor(R.color.gray_80));
            if(column == 1){
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.while_color));
            }else {
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.main_bg_in));
            }
            if(model.hasChind()){
                icon = R.mipmap.icon_chose_arrow_nor;
            }
        }

        holder.titleTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, icon, 0);
        return convertView;
    }

    private class Holder {
        TextView titleTv;

        public Holder(View view) {
            titleTv = (TextView) view.findViewById(R.id.titleTv);
        }
    }
}
