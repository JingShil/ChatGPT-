package com.chat.back.entity;

import lombok.Data;

import java.util.List;

@Data
public class SettingDto {
    private String settingId;
    private String userId;
    private String type;
    private String title;
    private String model;
    private List<String> settingChildren;
    private String defaultValue;
    private String userDefaultValue;
}
