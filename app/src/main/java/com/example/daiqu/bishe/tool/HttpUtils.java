package com.example.daiqu.bishe.tool;

import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.mob.MobSDK.getContext;

public class HttpUtils {
    private static String PATH = "http://10.16.66.2:8080/";
    private static URL url;

    public HttpUtils() {
    }
    /**
     * @param params 填写的url的参数
     * @param encode 字节编码
     * @return
     */
    public static String sendPostMessage(Map<String, String> params, String encode, String TAG)  {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        try {//把请求的主体写入正文！！
            if (params != null && !params.isEmpty()) {
                //迭代器
                //Map.Entry 是Map中的一个接口，他的用途是表示一个映射项（里面有Key和Value）
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    buffer.append("\"");
                    buffer.append(entry.getKey()).append("\":\"").
                            append(entry.getValue()).
                            append("\",");
                }
            }
            //删除最后一个字符&，多了一个;主体设置完毕

            buffer.deleteCharAt(buffer.length() - 1);
            buffer.append("}");
            System.out.println(new String(buffer).toString());
            byte[] myData = buffer.toString().getBytes();
            url = new URL(PATH + TAG);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setDoInput(true);//表示从服务器获取数据
            connection.setDoOutput(true);//表示向服务器写数据
            connection.setRequestMethod("POST");
            //是否使用缓存
            connection.setUseCaches(false);
            //表示设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/json");

            connection.setRequestProperty("Content-Length", String.valueOf(myData.length));
            connection.connect();   //连接，不写也可以。。？？有待了解

            //获得输出流，向服务器输出数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(myData, 0, myData.length);
            //获得服务器响应的结果和状态码
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return changeInputeStream(connection.getInputStream(), encode);
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "-999";
    }

    /**
     * 将一个输入流转换成字符串
     *
     * @param inputStream
     * @param encode
     * @return
     */
    private static String changeInputeStream(InputStream inputStream, String encode) {
        //通常叫做内存流，写在内存中的
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        String result = "";
        if (inputStream != null) {
            try {
                while ((len = inputStream.read(data)) != -1) {
                    data.toString();

                    outputStream.write(data, 0, len);
                }
                //result是在服务器端设置的doPost函数中的
                result = new String(outputStream.toByteArray(), encode);
                outputStream.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

    //上传文件用(采用了okHttp3)
    public static void uploadPicture(File file, String TAG) {
        OkHttpClient httpClient = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/octet-stream");//设置类型，类型为八位字节流
        RequestBody requestBody = RequestBody.create(mediaType, file);//把文件与类型放入请求体
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), requestBody)
                .build();
        Request request = new Request.Builder()
                .url(PATH + TAG)
                .post(multipartBody)
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getContext(), "上传出错啦", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String state = response.body().string();
                System.out.println(state);
                if ("11".equals(state)) {
                    Looper.prepare();
                    Toast.makeText(getContext(), "传输成功啦", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else if ("00".equals(state)) {
                    Looper.prepare();
                    Toast.makeText(getContext(), "服务器出错啦", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    Looper.prepare();
                    Toast.makeText(getContext(), "图片上传出错啦", Toast.LENGTH_SHORT).show();
                    Log.d("state", state);
                    Looper.loop();
                }
            }
        });
    }

    //上传文件并且携带参数
    public static void postPicWithParam(Map<String, String> param, File file, Callback callback, String TAG) {
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //判断参数是否为空
        if (param != null) {
            for (Map.Entry<String, String> entry : param.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        //判断文件是否存在
        if (file != null && file.exists()) {
            Log.d(TAG, "post3: 文件存在");
            MediaType mediaType = MediaType.parse("application/octet-stream");//设置类型，类型为八位字节流
            RequestBody body = RequestBody.create(mediaType, file);//把文件与类型放入请求体
            builder.addFormDataPart("file", file.getName(), body);
            Request request = new Request.Builder()
                    .url(PATH + TAG)
                    .post(builder.build())
                    .build();
            client.newCall(request).enqueue(callback);
        } else {
            Log.d(TAG, "post3: 文件不存在");
            RequestBody requestBody = builder.build();
            Request request = new Request.Builder()
                    .url(PATH + TAG)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(callback);
        }
    }
}
