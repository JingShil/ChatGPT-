package com.chat.back.api;


import com.chat.back.entity.AzureTtsRequest;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AzureTtsApi {

    public byte[] getTts(AzureTtsRequest azureTtsRequest) {
        try {
            //创建URL对象，使用HTTP POST方法请求JSON格式的数据

            HttpURLConnection cn = getHttpURLConnection();
            //设置要发送的JSON数据
//            String postDada = getPostDada(chatRequestData);
            String postDada = new JSONObject(azureTtsRequest).toString();
            //将请求发给网站
            OutputStream outputStream = cn.getOutputStream();
            outputStream.write(postDada.getBytes());
            outputStream.flush();
            outputStream.close();


            // 从网页接收音频文件并存储在字节数组中
            InputStream inputStream = cn.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            byteArrayOutputStream.close();
            inputStream.close();

            cn.disconnect();

            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }



    /**
     * 设置请求路径，格式和方式
     * @return
     * @throws IOException
     */
    private static HttpURLConnection getHttpURLConnection() throws IOException {
        URL url = new URL("https://openai.api2d.net/azure/tts");
        HttpURLConnection cn = (HttpURLConnection) url.openConnection();
        cn.setRequestMethod("POST");
        cn.setRequestProperty("Content-Type", "application/json");
        cn.setRequestProperty("Authorization", "Bearer fk194545-QXswLRl4g8ejPXSfxxpnlve3Tn6b78pp");//设置Forward Key, Bearer 要保留
        cn.setDoOutput(true);
        return cn;
    }
}
