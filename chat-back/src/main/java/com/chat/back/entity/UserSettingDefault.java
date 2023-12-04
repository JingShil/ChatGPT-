package com.chat.back.entity;

import lombok.Data;

@Data
public class UserSettingDefault {
    private String userId;
    private String settingId;
    private String defaultValue;
}
