package com.chat.back.entity;

import lombok.Data;

import java.util.List;

@Data
public class MessageDto {
    private String model;
    private List<Message> messages;
    private String languageVoice;
    private String speedVoice;
}
