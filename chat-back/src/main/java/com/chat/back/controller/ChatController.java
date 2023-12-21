package com.chat.back.controller;


import com.chat.back.api.AzureTtsApi;
import com.chat.back.api.ChatGptApi;
import com.chat.back.common.R;
import com.chat.back.entity.*;
import com.chat.back.mapper.ChatHistoryMapper;
import com.chat.back.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import java.time.LocalDateTime;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/chat")
public class ChatController {

    private Map<String, SseEmitter> sseEmitterMap = new HashMap<>();




    @Autowired
    private ChatHistoryMapper chatHistoryMapper;


    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody MessageDto messageDto) {

        List<Message> messageList = new ArrayList<>();
        List<Message> messages = messageDto.getMessages();
        for(int i=0;i < messages.size()-1;i++) {
            messageList.add(messages.get(i));
        }
        ChatGptApi chatGptApi = new ChatGptApi();
        ChatRequestData chatRequestData = new ChatRequestData();
        chatRequestData.setStream(true);
        chatRequestData.setMessages(messageList);
        chatRequestData.setModel(messageDto.getModel());
//        chatGptApi.send("你好");
        chatGptApi.sendChatGpt(chatRequestData);
        return R.success("发送成功");
    }

    @PostMapping("/sendMsg/voice")
    public R<Map<String, String>> sendMsgVoice(@RequestBody MessageDto messageDto) {

//        List<Message> messageList = new ArrayList<>();
//        for(int i=0;i < messages.size()-1;i++) {
//            messageList.add(messages.get(i));
//        }
        List<Message> messages = messageDto.getMessages();
        ChatGptApi chatGptApi = new ChatGptApi();
        ChatRequestData chatRequestData = new ChatRequestData();
        chatRequestData.setStream(false);
        chatRequestData.setMessages(messages);
        chatRequestData.setModel(messageDto.getModel());
//        chatGptApi.send("你好");
//        ChatResponseData chatResponseData = chatGptApi.sendChatGpt(chatRequestData);
        String content = chatGptApi.sendChatGpt(chatRequestData);
        //文字转语言
        AzureTtsApi azureTtsApi = new AzureTtsApi();
        AzureTtsRequest azureTtsRequest = new AzureTtsRequest();
//        List<ChatResponseData.Choice> choices = chatResponseData.getChoices();
//        Message message = choices.get(0).getMessage();
//        String content = message.getContent();
        azureTtsRequest.setText(content);

        azureTtsRequest.setSpeed(Float.parseFloat(messageDto.getSpeedVoice()));
        azureTtsRequest.setVoiceName(messageDto.getLanguageVoice());
        azureTtsRequest.setModeration_stop(true);
        byte[] tts = azureTtsApi.getTts(azureTtsRequest);

        String base64Audio = Base64.getEncoder().encodeToString(tts);
        Map<String, String> map = new HashMap<>();
        map.put("audioBase64", base64Audio);
        map.put("content", content);
        return R.success(map);
    }



    @PostMapping("/history/add")
    public R<String> historyAdd(@RequestBody Map<String, String> object){
        String userId = object.get("userId");
        String text = object.get("text");
        LocalDateTime now = LocalDateTime.now();
        String id = new MD5Utils().encryptMD5(userId + text + now);
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setId(id);
        chatHistory.setText(text);
        chatHistory.setUpdateTime(now);
        chatHistory.setUserId(userId);
        chatHistoryMapper.insert(chatHistory);
        return R.success("保存成功");
    }

    @PostMapping("/history/delete")
    public R<String> historyDelete(@RequestBody Map<String, String> object){
        String id = object.get("id");
        chatHistoryMapper.deleteById(id);
        return R.success("删除成功");
    }

    @PostMapping("/history/list")
    public R<List<ChatHistory>> historyList(@RequestBody Map<String, String> object){
        Integer pageNumber = Integer.parseInt(object.get("pageNumber"));
        Integer pageSize = Integer.parseInt(object.get("pageSize"));
        String userId = object.get("userId");
        int offset = (pageNumber - 1) * pageSize;
        List<ChatHistory> chatHistories = chatHistoryMapper.selectPageByUserId(userId, offset, pageSize);
        return R.success(chatHistories);
    }
}
