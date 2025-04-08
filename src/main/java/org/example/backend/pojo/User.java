package org.example.backend.pojo;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "user_tb")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Integer userId;
    @Column(name = "user_name", unique = true, nullable = false)
    private String userName;
    @Column(name = "user_password")
    private String userPassword;
    @Column(name = "user_email")
    private String userEmail;

    public User(String userName, String userEmail, String userPassword) {
        this.userPassword = userPassword;
        this.userName = userName;
        this.userEmail = userEmail;
    }
    public User() {
    }
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }
}
