package org.example.backend.controller;

import com.alibaba.fastjson.JSONObject;
import jakarta.validation.constraints.NotNull;
import org.example.backend.pojo.Response.AnalysisResponse;
import org.example.backend.pojo.UserTransData;
import org.example.backend.service.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/data")
public class DataController {
    @Autowired
    private IDataService dataService;
    @RequestMapping("/uploadData")
    public AnalysisResponse uploadData(@RequestPart("image") MultipartFile image, @RequestParam("text") String text) throws Exception {
        System.out.println("userTransData: " + image + "text: " + text);
        String imagePath = storeFile(image);
        UserTransData userTransData = new UserTransData(UserController.getCurrentUser().getUserId(), imagePath, text);
        return dataService.saveAndAnalysis(userTransData);

    }

    private String storeFile(@NotNull(message = "图像不能为空") MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        // 指定存储目录
        try {
            String uploadDir = String.valueOf(Paths.get("src/main/resources/images/").toAbsolutePath());
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 生成唯一文件名
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = uploadDir + "\\" +fileName;
            System.out.println("fileName:" + fileName + "\nfilePath:" + filePath);
            // 存储文件
            file.transferTo(new File(filePath));

            return filePath;
        } catch (IOException e) {
            throw new RuntimeException("文件存储失败", e);
        }
    }
}
