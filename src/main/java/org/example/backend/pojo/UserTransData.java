package org.example.backend.pojo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.backend.repository.IUserRepository;

@Data
@Table(name = "user_trans_data")
@Entity
public class UserTransData {
    @Id
    @Column(name = "data_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dataId;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "image")
    private String image; // 保存为传的图片的路径
    @Column(name = "text")
    private String text;

    public UserTransData(Integer userId, @NotNull(message = "图像不能为空") String image, @NotNull(message = "文本不能为空") String text) {
        this.userId = userId;
        this.image = image;
        this.text = text;
    }

    public UserTransData() {

    }
}
