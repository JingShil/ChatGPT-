package com.chat.back.entity;

import lombok.Data;

@Data
public class AzureTtsRequest {
    private String text;
    private String voiceName;
    private float speed;
    private boolean moderation=false;
    private boolean moderation_stop=false;
}
