package com.example.samuel.expensemanager.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.bean.Country;

import java.util.ArrayList;


/**
 * Created by Allen_C on 2015/12/3.
 */
public class ChooseListAdapter extends BaseAdapter {

    Activity mActivity;//所在的Activity
    ArrayList<Country> mCountryList;//要显示的item

    public ChooseListAdapter(Activity activity, ArrayList<Country> countryList) {
        super();
        mActivity = activity;

        mCountryList = countryList;
    }

    @Override
    public int getCount() {
        return mCountryList.size();
    }

    @Override
    public Country getItem(int position) {
        return mCountryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mActivity, R.layout.item_choosecur_list, null);
            holder = new ViewHolder();
            //holder.iv_choose_selected = (ImageView)convertView.findViewById(R.id.iv_choose_selected);
            holder.iv_choose_country = (ImageView) convertView.findViewById(R.id.iv_choose_country);
            holder.tv_choose_unit = (TextView) convertView.findViewById(R.id.tv_choose_unit);
            holder.tv_choose_cur = (TextView) convertView.findViewById(R.id.tv_choose_cur);
            holder.iv_choose_shown = (ImageView) convertView.findViewById(R.id.iv_choose_shown);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Country item = getItem(position);
        holder.tv_choose_unit.setText(item.getUnit());
        holder.tv_choose_cur.setText(item.getCurrency());
        holder.iv_choose_country.setImageResource(item.getImageId());

        if (item.isShown()) {//在前一页有显示的

            /*if(item.isSelected()) {//如果是初始货币
                holder.iv_choose_selected.setImageResource(R.drawable.star);//左边的星
                holder.iv_choose_selected.setVisibility(View.VISIBLE);
            }else{
                holder.iv_choose_selected.setVisibility(View.INVISIBLE);
            }*/

            if (item.isReplaced()) {//如果是要被替换的货币
                /*holder.iv_choose_selected.setImageResource(R.drawable.star);//左边的星
                holder.iv_choose_selected.setVisibility(View.VISIBLE);*/
                //   holder.iv_choose_shown.setImageResource(R.drawable.btn_check_selected);//蓝色勾
                //   holder.iv_choose_shown.setVisibility(View.VISIBLE);
                // holder.tv_choose_unit.setTextColor(Color.CYAN);//青色字
                //holder.tv_choose_cur.setTextColor(Color.CYAN);


                holder.iv_choose_shown.setVisibility(View.INVISIBLE);
            } else {
                //holder.iv_choose_selected.setVisibility(View.INVISIBLE);
                holder.iv_choose_shown.setImageResource(R.drawable.btn_checked);//右边灰色勾
                holder.iv_choose_shown.setVisibility(View.VISIBLE);
            }
            holder.tv_choose_unit.setTextColor(Color.GRAY);//字体颜色变灰
            holder.tv_choose_cur.setTextColor(Color.GRAY);
        } else {//没有显示的
            //holder.iv_choose_selected.setVisibility(View.INVISIBLE);
            holder.iv_choose_shown.setVisibility(View.INVISIBLE);
            holder.tv_choose_unit.setTextColor(Color.BLACK);
            holder.tv_choose_cur.setTextColor(Color.BLACK);
        }

        return convertView;
    }

    static class ViewHolder {
        //ImageView iv_choose_selected;
        ImageView iv_choose_country;
        TextView tv_choose_unit;
        TextView tv_choose_cur;
        ImageView iv_choose_shown;
    }
}
