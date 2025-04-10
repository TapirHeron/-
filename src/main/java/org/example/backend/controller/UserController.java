package org.example.backend.controller;

import lombok.Setter;
import org.example.backend.pojo.Dto.UserDto;
import org.example.backend.pojo.Response.ResponseMessage;
import org.example.backend.pojo.User;
import org.example.backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;
    private static User curentUser;

    public static User getCurrentUser() {
        return curentUser;
    }

    // 登录
    @PostMapping("/login")
    public ResponseMessage<User> login(@RequestBody UserDto userDto) {
        // 验证用户名和密码是否匹配
        User user = userService.Login(userDto);
        if (user != null) {
            // 如果登陆成功，返回用户信息及JWT令牌
            String token = userService.generateToken(user);
            curentUser = user;
            return ResponseMessage.success(new LoginResponse(user, token).getUser());
        }
        return ResponseMessage.error(404, "用户不存在");
//        if (userService.Login(userDto)) {
//            User user = null;
//            BeanUtils.copyProperties(userDto, user);
//            return ResponseMessage.success(user);
//        }
//        return ResponseMessage.error(404, "用户不存在");
    }
    @PostMapping("/register")
    public ResponseMessage<User> Register(@RequestBody UserDto userDto) {
        User user = new User(userDto.getUserName(), userDto.getUserEmail(), userDto.getUserPassword());
        if (userService.save(user)) {
            curentUser = user;
            return ResponseMessage.success(user);
        }
        return ResponseMessage.error(404, "注册失败");
    }
    // 登陆成功时返回的用户信息及JWT令牌
    public static class LoginResponse {
        @Setter
        private User user;
        private String token;

        public LoginResponse(User user, String token) {
            this.user = user;
            this.token = token;
        }

        public User getUser() {
            return user;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
        }
    }
}
