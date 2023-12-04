package com.chat.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.chat.back.entity.SettingChild;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface SettingChildMapper extends BaseMapper<SettingChild> {
}
