package com.geoway.appks.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wangchaojie on 2017/11/9.
 */

public class ToastUtil {
    private static Toast toast;
    private static Toast longToast;
    private static ProgressDialog dialog;

    private static final int duration = Toast.LENGTH_LONG;

    //短时toast显示时间
    public static void CustomTimeToast(Context context,String content,int time) {
        final Toast toast = Toast.makeText(context, content, Toast.LENGTH_LONG);
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, time);// 3000表示点击按钮之后，Toast延迟3000ms后显示
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, time);// 1000表示Toast显示时间为1秒

    }

    public static void showToast(Context c, int resId) {
        Toast.makeText(c, resId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context c, String desc) {
        Toast.makeText(c, desc, Toast.LENGTH_SHORT).show();
    }

    public static void showProgressDialog(Context c, String msg) {
        dialog = ProgressDialog.show(c, "", msg);
    }

    public static void showProgressDialog(Context c, int resId) {
        dialog = ProgressDialog.show(c, "", c.getString(resId));
    }

    public static void cancelProgressDialog() {
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }
    private static Toast getToast(Context context, String text){
        if(toast == null){
            toast = Toast.makeText(context.getApplicationContext(), text, duration);
        }else{
            toast.setText(text);
            toast.setDuration(duration);
        }
        return toast;
    }

    public static void showMsg(Context context, String text){
        Toast toast = getToast(context, text);

        toast.show();
    }

    private static Toast getToastLongTime(Context context, String text){
        if(longToast == null){
            longToast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_LONG);//
        }else{
            longToast.setText(text);
            longToast.setDuration(Toast.LENGTH_LONG);
        }
        return longToast;
    }

    public static void showMsgLongTime(Context context, String text){
        Toast toast = getToastLongTime(context, text);
        toast.show();
    }

    public static void cancelMsg(){
        if(toast != null){
            toast.cancel();
        }
    }

    public static void showMsgInCenterLong(Context context, String text){
        Toast toast = getToast(context, text);

        //toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showMsgInCenterShort(Context context, String text){
        Toast toast = getToast(context, text);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
