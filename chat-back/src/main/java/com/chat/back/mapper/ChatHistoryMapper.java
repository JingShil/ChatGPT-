package com.chat.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.chat.back.entity.ChatHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface ChatHistoryMapper extends BaseMapper<ChatHistory> {

    @Select("<script>" +
            "SELECT * FROM chat_history" +
            "<if test='userId != null'> WHERE user_id = #{userId}</if>" +
            " LIMIT #{offset}, #{pageSize}" +
            "</script>")
    List<ChatHistory> selectPageByUserId(@Param("userId") String userId,
                                         @Param("offset") int offset,
                                         @Param("pageSize") int pageSize);
}
