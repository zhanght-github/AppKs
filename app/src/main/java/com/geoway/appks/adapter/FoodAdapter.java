package com.geoway.appks.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.geoway.appks.Model.FoodModel;
import com.geoway.appks.R;
import com.geoway.appks.util.AmountView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanghongtao on 2017/12/25.
 */

public class FoodAdapter extends BaseAdapter {

    private List<FoodModel> mDatas;
    private Context context;

    private Map<Integer,Integer> amountValue = new HashMap<>();

    private int totalprice = 0;

    public FoodAdapter(List<FoodModel> mDatas, Context context){
        this.mDatas = mDatas;
        this.context = context;
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //得到订单总价
    public int getTotalPrice(){
        for (int i=0;i<amountValue.size();i++){
            totalprice += mDatas.get(i).getFoodprice()*amountValue.get(i);
        }
        return totalprice;
    }

    public void setTotalprice(int value){
        for (int i=0;i<amountValue.size();i++){
            amountValue.put(i,value);
        }
        totalprice = value;
    }

    public void setAmount(int value){

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){

            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_home_item,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        amountValue.put(position,0);
        FoodModel foodModel = mDatas.get(position);
        holder.foodname.setText(""+foodModel.getFoodname());
        holder.foodprice.setText(foodModel.getFoodprice()+"元");
        holder.amountView.setGoods_storage(50);
        //holder.amountView.setAmount(0);
        holder.amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
//                Toast.makeText(view.getContext(), "Amount=>  " + amount+"positon"+amountValue.get(0), Toast.LENGTH_SHORT).show();
                amountValue.put(position,amount);
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


            }
        });

        return convertView;
    }


    //自定义的viewholder，持有每个Item的所有组件
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        private AmountView amountView;
        private TextView foodname;
        private TextView foodprice;




        public ViewHolder(View view) {
            super(view);
            mView = view;
            foodname = (TextView)view.findViewById(R.id.tv_foodname);
            foodprice = (TextView)view.findViewById(R.id.tv_foodprice);
            amountView = (AmountView)view.findViewById(R.id.amount_view);


        }
    }
}
