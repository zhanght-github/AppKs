package com.geoway.appks.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.geoway.appks.Model.BaseResponse;
import com.geoway.appks.Model.FoodModel;
import com.geoway.appks.Model.OrderModel;
import com.geoway.appks.Model.RoomModel;
import com.geoway.appks.R;
import com.geoway.appks.adapter.FoodAdapter;
import com.geoway.appks.util.AmountView;
import com.geoway.appks.util.NetWorkUtil;
import com.geoway.appks.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * 原生首页
 */
public class HomeFragment extends Fragment {

    private View view;
    private List<FoodModel> foodModels;
    private List<RoomModel> roomModels;
    private List<Integer> mDatas = new ArrayList<>();
    //private AmountView amountView;

    private ListView lv_foods;

    private TextView tv_jiesuan;
    private TextView tv_xiadan;
    private TextView total_price;
    private EditText roomid;
    private Spinner spinner;

    private FoodAdapter foodAdapter;

    private OrderModel orderModel = new OrderModel();

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private List<Integer> data_list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_home, container, false);
        preferences = getActivity().getSharedPreferences("login",MODE_PRIVATE);


        initData();
        initUI();

        return view;
    }

    private void initUI(){
        lv_foods = (ListView) view.findViewById(R.id.lv_foods);
        tv_jiesuan = (TextView) view.findViewById(R.id.tv_jiesuan);
        tv_xiadan = (TextView) view.findViewById(R.id.tv_xiadan);
        total_price = (TextView) view.findViewById(R.id.total_price);
        roomid = (EditText) view.findViewById(R.id.roomid);


        tv_jiesuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_jiesuan.setVisibility(View.GONE);
                tv_xiadan.setVisibility(View.VISIBLE);
                total_price.setText(foodAdapter.getTotalPrice()+"元");
            }
        });

        tv_xiadan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_xiadan.setVisibility(View.GONE);
                tv_jiesuan.setVisibility(View.VISIBLE);

                commitOrder();

                total_price.setText("");
                roomid.setText("0");
                foodAdapter.setTotalprice(0);
                roomid.clearFocus();
                lv_foods.setAdapter(foodAdapter);
            }
        });
    }

    //网络请求不能跑在主线程，所以new 线程
    private void initData() {
        try {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    BaseResponse fodddata = NetWorkUtil.getFoods();
                    if (fodddata.getStatus().equals("OK")) {
                        Message msg = new Message();
                        msg.what = 1;// 输出识别标识，可以用做显示判断
                        msg.obj = fodddata.getData();
                        handler.sendMessage(msg);
                    }

                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {

                if (msg.what == 1){

                    //把json字符串转成json对象，填充到adapter对应的list
                    Gson gson = new Gson();
                    foodModels = gson.fromJson(msg.obj.toString(), new TypeToken<List<FoodModel>>() {}.getType());
                    foodAdapter = new FoodAdapter(foodModels,view.getContext());
                    lv_foods.setAdapter(foodAdapter);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };





    //提交订单
    private void commitOrder() {
        try {

            Gson gson = new Gson();
            String username = preferences.getString("username","");
            orderModel.setOrderid(0);
            orderModel.setRoomid(roomid.getText().toString());
            orderModel.setStaffid(username);
            String a = total_price.getText().toString();
            orderModel.setOrdermoney(Integer.parseInt(a.substring(0,a.length()-1)));

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date date=new java.util.Date();
            String str=sdf.format(date);
            orderModel.setCreatedate(str);
            final String order = gson.toJson(orderModel);

            new Thread(){
                @Override
                public void run() {
                    super.run();
                    String data = NetWorkUtil.commitOrder(order);
                    if (data != null) {
                        Message msg = new Message();
                        msg.what = 1;// 输出识别标识，可以用做显示判断
                        msg.obj = data;
                        handler2.sendMessage(msg);
                    }
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    Handler handler2 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                //把json字符串转成json对象，填充到adapter对应的list
                Gson gson = new Gson();
                BaseResponse baseResponse = gson.fromJson(msg.obj.toString(), BaseResponse.class);
                    ToastUtil.CustomTimeToast(view.getContext(),baseResponse.getMessage(),500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };



}
