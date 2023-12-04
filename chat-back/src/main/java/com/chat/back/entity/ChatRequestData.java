package com.chat.back.entity;



import lombok.Data;

import java.util.List;

@Data
public class ChatRequestData {
    private String model = "gpt-3.5-turbo";;
    private List<Message> messages;
    private boolean stream = false;






}
