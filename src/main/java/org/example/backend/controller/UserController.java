package org.example.backend.controller;

import lombok.Setter;
import org.example.backend.pojo.Dto.UserDto;
import org.example.backend.pojo.Response.ResponseMessage;
import org.example.backend.pojo.User;
import org.example.backend.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    // 添加用户
    @PostMapping
    public ResponseMessage<User> addUser(@Validated @RequestBody UserDto userDto) {
        User newUser = userService.add(userDto);
        return ResponseMessage.success(newUser);
    }

    // 删除用户
    @DeleteMapping("{userId}")
    public ResponseMessage<User> deleteUser(@PathVariable int userId) {
        userService.delete(userId);
        return ResponseMessage.success(null);
    }

    // 查询用户
    @GetMapping("{userId}")
    public ResponseMessage<User> user(@PathVariable int userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            return ResponseMessage.error(404, "用户不存在");
        }
        return ResponseMessage.success(user);
    }

    // 更新用户
    @PutMapping("{userId}")
    public ResponseMessage<User> updateUser(@PathVariable int userId, @RequestBody UserDto userDto) {
        User user = userService.update(userId, userDto);
        if (user == null) {
            return ResponseMessage.error(404, "用户不存在");
        }
        return ResponseMessage.success(user);
    }

    // 登录
    @PostMapping("/login")
    public ResponseMessage<User> login(@RequestBody UserDto userDto) {
        // 验证用户名和密码是否匹配
        User user = userService.Login(userDto);
        if (user != null) {
            // 如果登陆成功，返回用户信息及JWT令牌
            String token = userService.generateToken(user);
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
