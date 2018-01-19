package com.geoway.appks.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geoway.appks.LoginActivity;
import com.geoway.appks.ModifyPasswordActivity;
import com.geoway.appks.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * 个人中心
 */
public class PersonFragment extends Fragment {

    private View view;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private LinearLayout ll_userinfo;
    private LinearLayout ll_modifypassword;
    private TextView tv_username;

    private TextView tv_quite;

    private String password;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_person, container, false);
        preferences = getActivity().getSharedPreferences("login",MODE_PRIVATE);
        initUI();
        initData();
        //从登录中获取信息
        boolean isRemember = preferences.getBoolean("remember_password",false);
//        if (isRemember){
            //将账号设置到文本框中
            String username = preferences.getString("username","");
            password = preferences.getString("password","");
            tv_username.setText(username);
//        }

        return view;
    }

    private void initUI(){
        ll_userinfo = (LinearLayout) view.findViewById(R.id.ll_userinfo);
        ll_modifypassword = (LinearLayout) view.findViewById(R.id.ll_modify_password);
        tv_username = (TextView) view.findViewById(R.id.personinfo_username);
        tv_quite = (TextView) view.findViewById(R.id.tv_quite);

//        //个人信息
//        ll_userinfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("username",String.valueOf(tv_username.getText().toString()));
//                intent.setClass(view.getContext(), UserInfoActivity.class);
//                startActivity(intent);
//            }
//        });

        //修改密码
        ll_modifypassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inputServer = new EditText(v.getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("请输入原密码").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String confirmPassword = inputServer.getText().toString();
                        //Toast.makeText(view.getContext(),password,Toast.LENGTH_SHORT).show();
                        if (confirmPassword.equals(password)){
                            Intent intent = new Intent();
                            intent.setClass(view.getContext(), ModifyPasswordActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(view.getContext(),"密码输入错误！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
            }
        });

        //退出
        tv_quite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = preferences.edit();
                editor.putBoolean("remember_password",false);
                editor.putString("username","");
                editor.putString("password","");
                editor.apply();
                Intent intent = new Intent();
                intent.setClass(view.getContext(), LoginActivity.class);
                getActivity().startActivity(intent);
            }
        });

    }
    private void initData(){


    }

}