package org.example.backend.service;


import jakarta.persistence.EntityNotFoundException;
import org.example.backend.pojo.Dto.UserDto;
import org.example.backend.pojo.User;
import org.example.backend.repository.IUserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;
    @Override
    public User add(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto,user);
        user.setUserPassword(BCrypt.hashpw(userDto.getUserPassword(), BCrypt.gensalt()));
        System.out.println("存储的用户：" + user);
        userRepository.save(user);
        return user;
    }

    @Override
    @Transactional
    public User update(Integer userId, UserDto userDto) {
        if (userRepository.existsById(userId)) {
            // 从数据库中获取最新的实体对象
            User existingUser = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

            // 手动复制可更新的字段，避免覆盖 id 和 version
            if (userDto.getUserName() != null) {
                existingUser.setUserName(userDto.getUserName());
            }
            if (userDto.getUserEmail() != null) {
                existingUser.setUserEmail(userDto.getUserEmail());
            }

            // 保存更新后的实体
            return userRepository.save(existingUser);
        } else {
            throw new EntityNotFoundException("User not found with ID: " + userId);
        }
    }

    @Override
    public User delete(Integer userId) {
        if (userRepository.existsById(userId)) {
            User user = userRepository.findById(userId).get();
            userRepository.deleteById(userId);
            return user;
        }
        return null;
    }

    @Override
    public User getUser(Integer userId) {
        if (userRepository.existsById(userId)) {
            User user = userRepository.findById(userId).get();
            return user;
        }
        return null;
    }
    @Override
    public boolean Login(UserDto userDto) {
        User user = userRepository.findByUserName(userDto.getUserName());
        if (user != null) {
            if (BCrypt.checkpw(userDto.getUserPassword(), user.getUserPassword())) {
                return true;
            }
        }
        return false;
    }
}
