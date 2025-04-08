package org.example.backend.service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.persistence.EntityNotFoundException;
import org.example.backend.pojo.Dto.UserDto;
import org.example.backend.pojo.User;
import org.example.backend.repository.IUserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;


@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;

    //JWT密钥
    @Value("${jwt.secret}")
    private String jwtSecret;

    //Jwt有效期
    private long jwtExpirationMs = 3600000; // 1 hour


    @Override
    public User Login(UserDto userDto) {
      return userRepository.findByUserName(userDto.getUserName())
              .filter(user -> BCrypt.checkpw(userDto.getUserPassword(), user.getUserPassword()))
              .orElse(null);
    }

    @Override
    public String generateToken(User user) {
        // 设置JWT的过期时间
        Date expirationDate = new Date(System.currentTimeMillis() + jwtExpirationMs);

        // 生成JWT令牌
        return Jwts.builder()
                .setSubject(user.getUserName()) // 设置用户名作为主题
                .claim("userId", user.getUserId()) // 设置自定义的Claims，存储用户ID
                .setIssuedAt(new Date()) // 设置签发时间
                .setExpiration(expirationDate) // 设置过期时间
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // 使用HS512算法和密钥生成签名
                .compact(); // 生成JWT
    }

    @Override
    public boolean save(User user) {
        user.setUserPassword(BCrypt.hashpw(user.getUserPassword(), BCrypt.gensalt()));
        if (userRepository.findByUserName(user.getUserName()).isPresent()) {
            return false;
        }
        userRepository.save(user);
        return true;
    }
}
