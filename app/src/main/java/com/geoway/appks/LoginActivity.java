package com.geoway.appks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geoway.appks.Model.BaseResponse;
import com.geoway.appks.util.NetWorkUtil;
import com.geoway.appks.util.ToastUtil;
import com.google.gson.Gson;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
//    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView usernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences =getSharedPreferences("login",MODE_PRIVATE);
        rememberPass = (CheckBox) findViewById(R.id.remember_password);
        // Set up the login form.
        usernameView = (AutoCompleteTextView) findViewById(R.id.username);

        mPasswordView = (EditText) findViewById(R.id.password);
        boolean isRemember = preferences.getBoolean("remember_password",false);
        if (isRemember){

            //如果保存了密码就免登录
            Intent intent = new Intent();
            intent.setClass(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            //将账号密码设置到文本框中
            String username = preferences.getString("username","");
            String password = preferences.getString("password","");
            usernameView.setText(username);
            mPasswordView.setText(password);
        }
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button signinButton = (Button) findViewById(R.id.sign_in_button);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors.
        usernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String username = usernameView.getText().toString();
        final String password = mPasswordView.getText().toString();

        editor = preferences.edit();
        if (rememberPass.isChecked()){
            editor.putBoolean("remember_password",true);
            editor.putString("username",username);
            editor.putString("password",password);
        }else {
            editor.putBoolean("remember_password",false);
            editor.putString("username",username);
            editor.putString("password",password);
        }
        editor.apply();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            usernameView.setError(getString(R.string.error_field_required));
            focusView = usernameView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            try {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        String data = NetWorkUtil.login(username,password);
                        Message msg = new Message();
                        msg.what = 1;// 输出识别标识，可以用做显示判断
                        msg.obj = data;
                        handler.sendMessage(msg);
//
                    }
                }.start();
            }catch (Exception e){
                Toast.makeText(this,"登录失败！",Toast.LENGTH_SHORT).show();
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
                BaseResponse baseResponse = gson.fromJson(msg.obj.toString(),BaseResponse.class);
               if (baseResponse.getStatus().equals("OK")){
                   Intent intent = new Intent();
                   intent.setClass(LoginActivity.this, MainActivity.class);
                   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   startActivity(intent);
//                            Message msg = new Message();
//                            msg.what = 1;// 输出识别标识，可以用做显示判断
//                            msg.obj = data;
//                            handler.sendMessage(msg);

                   ToastUtil.CustomTimeToast(LoginActivity.this,baseResponse.getMessage(),500);
               }else {
                   ToastUtil.CustomTimeToast(LoginActivity.this,baseResponse.getMessage(),500);
               }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //如果isExit为false，则置为true，同时会弹出提示，并在2000毫秒（2秒）后发出一个消息，在 Handler中将此值还原成false。
    // 如果在发送消息间隔的2秒内，再次按了BACK键，则再次执行exit方法，此时isExit的值已为 true，则会执行退出的方法。
    private void exit() {
        if (!isExit) {
            isExit = true;
//            Toast.makeText(getApplicationContext(), "再按一次 退出程序", Toast.LENGTH_SHORT).show();
            //自定义显示时间，1000为1秒
            ToastUtil.CustomTimeToast(this,"再按一次退出程序",500);
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {

            Log.e(TAG, "exit application");
            //this.finish();
            // 退出程序
            Intent intent = new Intent(Intent.ACTION_MAIN);//标识一个程序的开始
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //System.exit(0);
        }
    }



    private static Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
