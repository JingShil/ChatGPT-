package com.chat.back.controller;

import com.chat.back.api.IfasrApi;
import com.chat.back.common.R;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@CrossOrigin
@RequestMapping("/voice/text")
public class IfasrController {

    @PostMapping("/upload")
    public R<String> uploadAudio(@RequestParam("audio") MultipartFile audioFile) {
        try {

            File file = File.createTempFile(UUID.randomUUID().toString(), ".wav");
            audioFile.transferTo(file);

            IfasrApi ifasrApi = new IfasrApi();

            String text = ifasrApi.getText(file);

            String pattern = "\"w\\\\\":\\\\\".*?\\\\\"";

            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(text);

            StringBuilder sb = new StringBuilder();
            while (m.find()) {
                String match = m.group();
                sb.append(match.substring(7, match.length() - 2));
            }

            String result = sb.toString();
            System.out.println(result);

            return R.success(result);
        } catch (Exception e) {
            System.out.println(e);
            return R.error("错误");
        }
    }
}
