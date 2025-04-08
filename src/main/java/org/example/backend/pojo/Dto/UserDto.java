package org.example.backend.pojo.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserDto {
    @NotBlank(message = "姓名不能为空")
    private String userName;
    @NotBlank(message = "密码不能为空")
    @Length(min = 4, max = 20, message = "密码长度必须在4到20之间")
    private String userPassword;
    @Email(message = "邮箱格式不正确")
    private String userEmail;

}
