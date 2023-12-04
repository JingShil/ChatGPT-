package com.chat.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chat.back.entity.UserSettingDefault;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserSettingDefaultMapper extends BaseMapper<UserSettingDefault> {

    @Update("UPDATE user_setting_default SET default_value = #{defaultValue} WHERE user_id = #{userId} and setting_id=#{settingId}")
    void updateById(@Param("userId") String userId,
                    @Param("settingId") String settingId,
                    @Param("defaultValue") String defaultValue
                    );
}
