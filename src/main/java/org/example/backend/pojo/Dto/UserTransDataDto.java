package org.example.backend.pojo.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserTransDataDto {
    @NotNull(message = "图像不能为空")
    private MultipartFile image; // 图像文件
    @NotNull(message = "文本不能为空")
    private String text;
}
