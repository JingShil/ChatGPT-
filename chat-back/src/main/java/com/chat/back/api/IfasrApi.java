package com.chat.back.api;

import cn.hutool.json.JSONUtil;

import com.chat.back.sign.LfasrSignature;
import com.chat.back.utils.HttpUtil;
import com.google.gson.Gson;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.*;
import java.security.SignatureException;
import java.util.HashMap;

public class IfasrApi {
    private static final String HOST = "https://raasr.xfyun.cn";
    private static String AUDIO_FILE_PATH;
    private static final String appid = "20ce2a7a";
    private static final String keySecret = "066da07d6f6788099c748b6e1138014b";

    private static final Gson gson = new Gson();

//    static {
//        try {
//            AUDIO_FILE_PATH = IfasrApi.class.getResource("/").toURI().getPath() + "/audio/合成音频.wav";
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }


    public String getText(File file) throws Exception {
        AUDIO_FILE_PATH = file.getAbsolutePath();
        String result = upload();
        String jsonStr = StringEscapeUtils.unescapeJavaScript(result);
        String orderId = String.valueOf(JSONUtil.getByPath(JSONUtil.parse(jsonStr), "content.orderId"));
        return getResult(orderId);
    }

    private static String upload() throws SignatureException, FileNotFoundException {
        HashMap<String, Object> map = new HashMap<>(16);
        File audio = new File(AUDIO_FILE_PATH);
        String fileName = audio.getName();
        long fileSize = audio.length();
        map.put("appId", appid);
        map.put("fileSize", fileSize);
        map.put("fileName", fileName);
        map.put("duration", "200");
        LfasrSignature lfasrSignature = new LfasrSignature(appid, keySecret);
        map.put("signa", lfasrSignature.getSigna());
        map.put("ts", lfasrSignature.getTs());

        String paramString = HttpUtil.parseMapToPathParam(map);
        System.out.println("upload paramString:" + paramString);

        String url = HOST + "/v2/api/upload" + "?" + paramString;
        System.out.println("upload_url:" + url);
        String response = HttpUtil.iflyrecUpload(url, new FileInputStream(audio));

        System.out.println("upload response:" + response);
        return response;
    }

    private static String getResult(String orderId) throws SignatureException, InterruptedException, IOException {
        HashMap<String, Object> map = new HashMap<>(16);
        map.put("orderId", orderId);
        LfasrSignature lfasrSignature = new LfasrSignature(appid, keySecret);
        map.put("signa", lfasrSignature.getSigna());
        map.put("ts", lfasrSignature.getTs());
        map.put("appId", appid);
        map.put("resultType", "transfer,predict");
        String paramString = HttpUtil.parseMapToPathParam(map);
        String url = HOST + "/v2/api/getResult" + "?" + paramString;
        System.out.println("\nget_result_url:" + url);
        while (true) {
            String response = HttpUtil.iflyrecGet(url);
            JsonParse jsonParse = gson.fromJson(response, JsonParse.class);
            if (jsonParse.content.orderInfo.status == 4 || jsonParse.content.orderInfo.status == -1) {
                System.out.println("订单完成:" + response);
                write(response);
                return response;
            } else {
                System.out.println("进行中...，状态为:" + jsonParse.content.orderInfo.status);
                //建议使用回调的方式查询结果，查询接口有请求频率限制
                Thread.sleep(7000);
            }
        }
    }

    public static void write(String resp) throws IOException {
        //将写入转化为流的形式
        BufferedWriter bw = new BufferedWriter(new FileWriter("src\\main\\resources\\output\\test.txt"));
        String ss = resp;
        bw.write(ss);
        //关闭流
        bw.close();
        System.out.println("写入txt成功");
    }

    class JsonParse {
        Content content;
    }

    class Content {
        OrderInfo orderInfo;
    }

    class OrderInfo {
        Integer status;
    }
}
