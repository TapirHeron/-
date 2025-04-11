package org.example.backend.controller;

import jakarta.validation.constraints.NotNull;
import org.example.backend.pojo.Dto.UserTransDataDto;
import org.example.backend.pojo.Response.AnalysisResponse;
import org.example.backend.pojo.UserTransData;
import org.example.backend.service.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/data")
public class DataController {
    @Autowired
    private IDataService dataService;
    @RequestMapping("/uploadData")
    public AnalysisResponse uploadData(@RequestBody UserTransDataDto userTransDataDto) throws Exception {
        String imagePath = storeFile(userTransDataDto.getImage());
        UserTransData userTransData = new UserTransData(UserController.getCurrentUser().getUserId(), imagePath, userTransDataDto.getText());
        return dataService.saveAndAnalysis(userTransData, userTransDataDto);
    }

    private String storeFile(@NotNull(message = "图像不能为空") MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        // 指定存储目录
        try {
            String uploadDir = "../../../../../resources/images";
            System.out.println("uploadDir: " + uploadDir);
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 生成唯一文件名
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = uploadDir + fileName;

            // 存储文件
            file.transferTo(new File(filePath));

            return filePath;
        } catch (IOException e) {
            throw new RuntimeException("文件存储失败", e);
        }
    }
}
