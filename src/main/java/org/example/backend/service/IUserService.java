package org.example.backend.service;
import org.example.backend.pojo.Dto.UserDto;
import org.example.backend.pojo.User;

public interface IUserService {
    /*
    * @pram userDto
    * */
    User add(UserDto userDto);
    /*
    *
    * */
    User update(Integer userId, UserDto userDto);
    /*
    *
    * */
    User delete(Integer userId);
    /*
    *
    * */
    User getUser(Integer userId);
    /*
    *
    * */
    boolean Login(UserDto userDto);
}
