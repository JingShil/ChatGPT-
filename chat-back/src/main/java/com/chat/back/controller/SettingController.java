package com.chat.back.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chat.back.common.R;
import com.chat.back.entity.Setting;
import com.chat.back.entity.SettingChild;
import com.chat.back.entity.SettingDto;
import com.chat.back.entity.UserSettingDefault;
import com.chat.back.mapper.SettingChildMapper;
import com.chat.back.mapper.SettingMapper;
import com.chat.back.mapper.UserSettingDefaultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/setting")
public class SettingController {

    @Autowired
    private SettingMapper settingMapper;

    @Autowired
    private SettingChildMapper settingChildMapper;

    @Autowired
    private UserSettingDefaultMapper userSettingDefaultMapper;

    @PostMapping("/add")
    public R<String> settingAdd(@RequestBody List<SettingDto> settingDtos){

        for(SettingDto settingDto:settingDtos) {
            UserSettingDefault userSettingDefault = new UserSettingDefault();

            userSettingDefault.setSettingId(settingDto.getSettingId());
            userSettingDefault.setUserId(settingDto.getUserId());
            userSettingDefault.setDefaultValue(settingDto.getUserDefaultValue());

            LambdaQueryWrapper<UserSettingDefault> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(UserSettingDefault::getUserId, userSettingDefault.getUserId())
                    .eq(UserSettingDefault::getSettingId, userSettingDefault.getSettingId());
            boolean exists = userSettingDefaultMapper.exists(lambdaQueryWrapper);
            if (exists) {
                userSettingDefaultMapper.updateById(userSettingDefault.getUserId(),
                        userSettingDefault.getSettingId(),
                        userSettingDefault.getDefaultValue());
            } else {
                userSettingDefaultMapper.insert(userSettingDefault);
            }
        }
        return R.success("保存成功");

    }

    @PostMapping("/list")
    public R<List<SettingDto>> settingList(@RequestBody Map<String, String> object){
        return R.success(settingDtos(object));
    }

    public List<SettingDto> settingDtos(Map<String, String> object){
        String userId=object.get("userId");
        String model=object.get("model");
        List<SettingDto> settingDtoList = new ArrayList<>();
        LambdaQueryWrapper<Setting> queryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(model))
        queryWrapper.eq(Setting::getModel, model);
        List<Setting> settings = settingMapper.selectList(queryWrapper);
        for(Setting setting:settings){
            SettingDto settingDto = new SettingDto();
            LambdaQueryWrapper<SettingChild> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(SettingChild::getSettingId, setting.getId());
            List<SettingChild> settingChildren = settingChildMapper.selectList(queryWrapper1);
            List<String> strings = new ArrayList<>();
            for(SettingChild settingChild:settingChildren){
                strings.add(settingChild.getValue());
            }
            settingDto.setModel(setting.getModel());
            settingDto.setSettingChildren(strings);
            settingDto.setType(setting.getType());
            settingDto.setTitle(setting.getTitle());
            settingDto.setUserId(userId);
            settingDto.setSettingId(setting.getId());
            settingDto.setDefaultValue(setting.getDefaultValue());
            settingDto.setUserDefaultValue(setting.getDefaultValue());
            LambdaQueryWrapper<UserSettingDefault> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(UserSettingDefault::getUserId, userId).eq(UserSettingDefault::getSettingId, setting.getId());
            boolean exists = userSettingDefaultMapper.exists(lambdaQueryWrapper);
            if(exists){
                UserSettingDefault userSettingDefault = userSettingDefaultMapper.selectOne(lambdaQueryWrapper);
                settingDto.setUserDefaultValue(userSettingDefault.getDefaultValue());
            }
            settingDtoList.add(settingDto);
        }
        return settingDtoList;
    }




}
