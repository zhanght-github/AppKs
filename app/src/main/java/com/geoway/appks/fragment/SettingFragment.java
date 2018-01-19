package com.geoway.appks.fragment;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.geoway.appks.Model.BaseResponse;
import com.geoway.appks.R;
import com.geoway.appks.util.AmountView;
import com.geoway.appks.util.NetWorkUtil;
import com.geoway.appks.util.ToastUtil;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;


public class SettingFragment extends Fragment {


    private View view;

    private Spinner spinner;

    private EditText roomid;
    private TextView tv_setstate;

    private int roomstate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_setting, container, false);
        tv_setstate = (TextView) view.findViewById(R.id.tv_setstate);
        roomid = (EditText) view.findViewById(R.id.set_roomid);

        spinner = (Spinner) view.findViewById(R.id.spinner);
// 建立数据源
        String[] mItems = {"1、可以使用", "0、不可使用"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                // TODO
                String temp = spinner.getSelectedItem().toString();

                roomstate = Integer.parseInt(temp.substring(0,1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO
            }
        });

        tv_setstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomid.clearFocus();
                setRoomState();
            }
        });

        return view;
    }

    private void setRoomState() {
        try {

            final int id = Integer.parseInt(roomid.getText().toString());
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    String data = NetWorkUtil.setRoomState(id,roomstate);
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
