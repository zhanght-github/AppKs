package com.geoway.appks.util;

import com.geoway.appks.Model.BaseResponse;
import com.google.gson.Gson;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 */

public class NetWorkUtil {



    //获取审批数据
    public static BaseResponse  getFoods(){
        BaseResponse baseResponse = new BaseResponse();
        Gson gson = new Gson();
        String msg = null;

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://169.254.165.44:8080/apis/food/getfoods")
                    .build();
            Response response = client.newCall(request).execute();
            msg = response.body().string();// 网络访问不能放在主线程，需要另辟线程
            baseResponse= gson.fromJson(msg, BaseResponse.class);

        }catch (Exception e){
            e.printStackTrace();
        }
        return baseResponse;
    }


    //获取审批数据
    public static BaseResponse  getRooms(){
        BaseResponse baseResponse = new BaseResponse();
        Gson gson = new Gson();
        String msg = null;

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://169.254.165.44:8080/apis/room/getroom")
                    .build();
            Response response = client.newCall(request).execute();
            msg = response.body().string();// 网络访问不能放在主线程，需要另辟线程
            baseResponse= gson.fromJson(msg, BaseResponse.class);

        }catch (Exception e){
            e.printStackTrace();
        }
        return baseResponse;
    }

    //提交菜单
    public static String commitOrder(String order){
        String msg = null;
        try {
            OkHttpClient client = new OkHttpClient();
            //使用post方法时的传参方式
                    RequestBody requestBody = new FormBody.Builder()
                            .add("strRequest",order)
                            .build();
            Request request = new Request.Builder()
                    .url("http://169.254.165.44:8080/apis/order/commitorder")
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            msg = response.body().string();// 网络访问不能放在主线程，需要另辟线程

        }catch (Exception e){
            e.printStackTrace();
        }
        return msg;
    }

    //提交审批结果
    public static String setRoomState(int roomid,int roomstate){
        String msg = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://169.254.165.44:8080/apis/room/setroomstate?roomid="+roomid+"&roomstate="+roomstate)
                    .build();
            Response response = client.newCall(request).execute();
            msg = response.body().string();// 网络访问不能放在主线程，需要另辟线程

        }catch (Exception e){
            e.printStackTrace();
        }
        return msg;
    }

    //登录
    public static String login(String username,String password){
        String msg = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://169.254.165.44:8080/apis/user/login?username="+username+"&password="+password)
                    .build();
            Response response = client.newCall(request).execute();
            msg = response.body().string();// 网络访问不能放在主线程，需要另辟线程

        }catch (Exception e){
            e.printStackTrace();
        }
        return msg;
    }

    //修改密码
    public static String modifyPassword(String username,String password){
        String msg = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://169.254.165.44:8080/apis/user/modifypassword?username="+username+"&password="+password)
                    .build();
            Response response = client.newCall(request).execute();
            msg = response.body().string();// 网络访问不能放在主线程，需要另辟线程

        }catch (Exception e){

        }
        return msg;
    }

    //用户详细信息
    public static String getUserinfo(String username){
        String msg = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://169.254.165.44:8080/apis/User/GetUserInfo?UserName="+username)
                    .build();
            Response response = client.newCall(request).execute();
            msg = response.body().string();// 网络访问不能放在主线程，需要另辟线程

        }catch (Exception e){
            e.printStackTrace();
        }
        return msg;
    }




}
