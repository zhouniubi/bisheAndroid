package com.example.daiqu.bishe.TencentUtils;

import android.content.Context;
import android.util.Log;

import com.example.daiqu.bishe.data.userData;
import com.example.daiqu.bishe.tool.AES;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.imsdk.v2.V2TIMSDKListener;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;

public class TencentIM {
    //对应的APPID
    public static final int APPID = 1400506545;
    //初始化腾讯即时通讯IM
    public static void initIm(Context context){
        V2TIMSDKConfig config = new V2TIMSDKConfig();
        // 3. 指定 log 输出级别，详情请参考 SDKConfig。
        config.setLogLevel(V2TIMSDKConfig.V2TIM_LOG_INFO);
        // 4. 初始化 SDK 并设置 V2TIMSDKListener 的监听对象。
        // initSDK 后 SDK 会自动连接网络，网络连接状态可以在 V2TIMSDKListener 回调里面监听。
        V2TIMManager.getInstance().initSDK(context, 1400506545, config, new V2TIMSDKListener() {
            // 5. 监听 V2TIMSDKListener 回调
            @Override
            public void onConnecting() {
                // 正在连接到腾讯云服务器
                Log.d("initing", "初始化中。。。。。");
            }
            @Override
            public void onConnectSuccess() {
                // 已经成功连接到腾讯云服务器
                Log.d("initing", "初始化成功！");
            }
            @Override
            public void onConnectFailed(int code, String error) {
                // 连接腾讯云服务器失败
                Log.d("initing", "初始化失败！");
            }
        });
    }
    //用户登录
    public static void load(String phone,userData uData){
        V2TIMCallback callback = new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                Log.d("loading", "登录失败！");
                Log.d("loadError", s);
            }
            @Override
            public void onSuccess() {
                Log.d("loading", "登录成功！");
                setUserMsg(uData);
            }
        };
        V2TIMManager.getInstance().login(AES.decrypt(phone), GenerateTestUserSig.genTestUserSig(AES.decrypt(phone)),callback);
    }
    //用户登出
    public static void logout(){
        V2TIMCallback callback = new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                Log.d("loading", "登出失败！");
            }
            @Override
            public void onSuccess() {
                Log.d("loading", "登出成功！");
            }
        };
        V2TIMManager.getInstance().logout(callback);
    }
    //发送消息(仅限文本)
    public static void sendMsg(String toUserId,String msg){
        V2TIMValueCallback<V2TIMMessage> callback = new V2TIMValueCallback<V2TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                Log.d("sendState", "发送失败！");
            }

            @Override
            public void onSuccess(V2TIMMessage v2TIMMessage) {
                Log.d("sendState", "发送成功！msg是："+msg);

            }
        };
        V2TIMManager.getInstance().sendC2CTextMessage(msg, toUserId, callback);
    }
    //设置个人信息
    public static void setUserMsg(userData uData){
        //load(AES.decrypt(uData.getPhone()));
        V2TIMCallback callback = new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                Log.d("setUserMsg", "设置个人信息失败"+"错误码："+i+";"+"错误信息："+s);

            }

            @Override
            public void onSuccess() {
                Log.d("setUserMsg", "设置个人信息成功");
            }
        };
        V2TIMUserFullInfo userFullInfo = new V2TIMUserFullInfo();

        userFullInfo.setNickname(AES.decrypt(uData.getName()));
        //userFullInfo.setFaceUrl(uData.getPic());
        V2TIMManager.getInstance().setSelfInfo(userFullInfo,callback);
    }
}
