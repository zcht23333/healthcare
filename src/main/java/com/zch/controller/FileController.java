package com.zch.controller;

import cn.hutool.core.util.IdUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FileController {

    // 图片在电脑上的实际保存路径
    private static final String UPLOAD_DIR = "D:/healthcare_upload/";

    @PostMapping("/upload")
    public Map<String, Object> upload(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();

        if (file == null || file.isEmpty()) {
            result.put("code", 400);
            result.put("message", "上传文件不能为空");
            return result;
        }

        try {
            // 1. 如果 D:/healthcare_upload/ 目录不存在，则创建它
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 2. 获取文件的原始后缀名 (比如 .jpg, .png)
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

            // 3. 使用 UUID 生成一个不重复的文件名，防止文件被覆盖
            String newFileName = IdUtil.fastSimpleUUID() + suffix;

            // 4. 将文件保存到物理磁盘
            File dest = new File(UPLOAD_DIR + newFileName);
            file.transferTo(dest);

            // 5. 拼接给前端的网络访问 URL
            // 前端拿到这个地址后，直接赋值给 img 标签的 src 即可显示图片
            String fileUrl = "http://localhost:8080/upload/" + newFileName;

            result.put("code", 200);
            result.put("message", "上传成功");
            result.put("data", fileUrl);

        } catch (IOException e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "文件上传失败");
        }

        return result;
    }
}