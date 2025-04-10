package org.example.backend.pojo.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserTransDataDto {
    @NotNull(message = "图像不能为空")
    private String image;
    @NotNull(message = "文本不能为空")
    private String text;
}
