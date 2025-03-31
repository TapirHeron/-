package org.example.backend.controller;

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
    @PostMapping
    public ResponseMessage<User> addUser(@Validated @RequestBody UserDto userDto) {
        User newUser = userService.add(userDto);
        return ResponseMessage.success(newUser);
    }
    @DeleteMapping("{userId}")
    public ResponseMessage<User> deleteUser(@PathVariable int userId) {
        userService.delete(userId);
        return ResponseMessage.success(null);
    }
    @GetMapping("{userId}")
    public ResponseMessage<User> user(@PathVariable int userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            return ResponseMessage.error(404, "用户不存在");
        }
        return ResponseMessage.success(user);
    }
    @PutMapping("{userId}")
    public ResponseMessage<User> updateUser(@PathVariable int userId, @RequestBody UserDto userDto) {
        User user = userService.update(userId, userDto);
        if (user == null) {
            return ResponseMessage.error(404, "用户不存在");
        }
        return ResponseMessage.success(user);
    }
    @PostMapping("/login")
    public ResponseMessage<User> login(@RequestBody UserDto userDto) {
        if (userService.Login(userDto)) {
            User user = null;
            BeanUtils.copyProperties(userDto, user);
            return ResponseMessage.success(user);
        }
        return ResponseMessage.error(404, "用户不存在");
    }
}
