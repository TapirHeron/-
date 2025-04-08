package org.example.backend.service;
import org.example.backend.pojo.Dto.UserDto;
import org.example.backend.pojo.User;

public interface IUserService {
    User Login(UserDto userDto);
    String generateToken(User user);
    boolean save(User user);
}
