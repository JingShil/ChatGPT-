package com.chat.back.entity;


import lombok.Data;

import java.util.List;

@Data
public class ChatResponseData {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<Choice> choices;
    private String system_fingerprint;
    private Usage usage;
    // getters and setters
    @Data
    public static class Choice {
        private int index;
        private Message message;
        private String finish_reason;
        private Delta delta;
        // getters and setters
    }
    @Data
    public static class Delta {
        private String content;
//        private String role;
        // getters and setters
    }

    @Data
    public static class Usage {
        private Integer prompt_tokens;
        private Integer completion_tokens;
        private Integer total_tokens;
        private Integer pre_token_count;
        private Integer pre_total;
        private Integer adjust_total;
        private Integer final_total;
    }
}



