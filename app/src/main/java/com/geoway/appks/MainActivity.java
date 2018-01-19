package com.geoway.appks;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.FrameLayout;


import com.geoway.appks.fragment.HomeFragment;
import com.geoway.appks.fragment.HomepageFragment;
import com.geoway.appks.fragment.PersonFragment;
import com.geoway.appks.fragment.SettingFragment;
import com.geoway.appks.util.ToastUtil;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private int iswebpage = 0;


    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;
    //Fragment
    private Fragment homeFragment;
    private Fragment settingFragment;
    private Fragment personFragment;


    //webview的首页
    private Fragment homepageFragment;

    //获取fragmentManager对象
    FragmentManager fragmentManager = getFragmentManager();
    //开启一个事务
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {
                case R.id.bottom_home:
                    initFragment(0);
                    return true;
                case R.id.bottom_setting:
                    initFragment(1);
                    return true;
                case R.id.bottom_person:
                    initFragment(2);
                    return true;
            }
            return true;

        }

    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        pagecontainer = (ViewPager) findViewById(R.id.page_container);
//        frameLayout = (FrameLayout) findViewById(R.id.frame_content);


        if (iswebpage == 0){
            //          原生首页
            if (homeFragment == null) {
                homeFragment = new HomeFragment();
            }
            fragmentTransaction.add(R.id.frame_content,homeFragment);
            fragmentTransaction.commit();
        }else {
            //webview首页

            if (homepageFragment == null) {
                homepageFragment = new HomepageFragment();
            }
            fragmentTransaction.add(R.id.frame_content,homepageFragment);
            fragmentTransaction.commit();
        }

        initView();


    }

    //初始化控件
    private void initView(){

    }
    //初始化Fragment
    private void initFragment(int index){
        //获取fragmentManager对象
        FragmentManager fragmentManager = getFragmentManager();
        //开启一个事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        switch (index){
            case 0:
                hideFragment(fragmentTransaction);
                //webview首页
                if (iswebpage == 0){
                    if (homeFragment == null) {
                        homeFragment = new HomeFragment();
                        fragmentTransaction.add(R.id.frame_content,homeFragment);
                    }else {
                        fragmentTransaction.show(homeFragment);
                    }
                }else {
                    if (homepageFragment == null){
                        homepageFragment = new HomeFragment();
                        fragmentTransaction.add(R.id.frame_content,homepageFragment);
                    }else {
                        fragmentTransaction.show(homepageFragment);
                    }
                }
                break;
            case 1:
                hideFragment(fragmentTransaction);
                if (settingFragment == null){
                    settingFragment = new SettingFragment();
                    fragmentTransaction.add(R.id.frame_content,settingFragment);
                }else {
                    fragmentTransaction.show(settingFragment);
                }
                break;
            case 2:
                hideFragment(fragmentTransaction);
                if (personFragment == null){
                    personFragment = new PersonFragment();
                    fragmentTransaction.add(R.id.frame_content,personFragment);
                }else {
                    fragmentTransaction.show(personFragment);
                }
                break;
            default:
                break;
        }

        fragmentTransaction.commit();
    }

    //隐藏Fragment
    private void hideFragment(FragmentTransaction transaction) {

        if (iswebpage == 0){
            if (homeFragment != null) {
                transaction.hide(homeFragment);
            }
        }else {
            if (homepageFragment != null) {
                transaction.hide(homepageFragment);
            }
        }
        if (settingFragment != null) {
            transaction.hide(settingFragment);
        }
        if ( personFragment!= null) {
            transaction.hide(personFragment);
        }
    }

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
            ToastUtil.CustomTimeToast(getApplicationContext(),"再按一次 退出程序",500);
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {

            Log.e(TAG, "exit application");
            // 退出程序
            Intent intent = new Intent(Intent.ACTION_MAIN);//标识一个程序的开始
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

    //如果底部导航栏是四个按钮，需要此方法取消谷歌BottomNavigationMenuView浮夸的位移效果
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        homepageFragment = null;
        homeFragment = null;
        settingFragment = null;
        personFragment = null;
    }

}
