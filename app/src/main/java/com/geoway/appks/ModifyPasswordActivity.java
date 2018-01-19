package com.geoway.appks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geoway.appks.Model.BaseResponse;
import com.geoway.appks.util.NetWorkUtil;
import com.geoway.appks.util.ToastUtil;
import com.google.gson.Gson;

public class ModifyPasswordActivity extends AppCompatActivity {

    private TextView tv_title;
    private RelativeLayout title_back;
    private TextView tv_username;
    private EditText new_password;
    private EditText confirm_password;
    private TextView modify_complete;
    private View title_divider;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    String username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        preferences = getSharedPreferences("login",MODE_PRIVATE);
        initUI();
        initData();
        //从登录中获取信息
        username = preferences.getString("username","");
        tv_username.setText(username);
    }

    private void initUI(){
        tv_title = (TextView) findViewById(R.id.tv_title);
        title_back = (RelativeLayout) findViewById(R.id.title_back);
        tv_username = (TextView) findViewById(R.id.tv_username);
        new_password = (EditText) findViewById(R.id.new_password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        modify_complete = (TextView) findViewById(R.id.tv_modify_complete);
        title_divider = (View) findViewById(R.id.title_divider);

        //头部导航栏多条线
        title_divider.setVisibility(View.GONE);

        //返回按钮
        tv_title.setText("设置密码");
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //完成按钮点击
        modify_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyPassword();
            }
        });

    }
    private void initData(){


    }

    private void modifyPassword() {

        new_password.setError(null);
        confirm_password.setError(null);

        // Store values at the time of the login attempt.
        final String newpass = new_password.getText().toString();
        String confirmpass = confirm_password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(newpass)) {
            new_password.setError("密码不能为空！");
            focusView = new_password;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(confirmpass)) {
            confirm_password.setError("确认密码不能为空！");
            focusView = confirm_password;
            cancel = true;
        }
        if (!confirmpass.equals(newpass)) {
            confirm_password.setError("密码输入不一致！");
            focusView = confirm_password;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //提交修改的密码，跳转到登录界面
            try {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        String data = NetWorkUtil.modifyPassword(username,newpass);
                        Message msg = new Message();
                        msg.what = 1;// 输出识别标识，可以用做显示判断
                        msg.obj = data;
                        handler.sendMessage(msg);
                    }
                }.start();
            }catch (Exception e){
                Toast.makeText(this,"修改失败！",Toast.LENGTH_SHORT).show();
            }

        }
    }


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                Gson gson = new Gson();
                BaseResponse baseResponse = gson.fromJson(msg.obj.toString(), BaseResponse.class);
                ToastUtil.CustomTimeToast(ModifyPasswordActivity.this, baseResponse.getMessage(), 1000);
                if (baseResponse.getStatus().equals("OK")) {
                    editor = preferences.edit();
                    editor.putBoolean("remember_password", false);
                    editor.apply();
                    Intent intent = new Intent();
                    intent.setClass(ModifyPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                }



                // DataCondition dataCondition = (DataCondition)JSONObject.toBean(json, DataCondition.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
