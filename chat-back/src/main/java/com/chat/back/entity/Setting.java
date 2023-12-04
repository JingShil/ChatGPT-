package com.chat.back.entity;

import lombok.Data;

@Data
public class Setting {

    private String id;
    private String type;
    private String title;
    private String defaultValue;
    private String model;
}
