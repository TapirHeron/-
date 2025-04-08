package org.example.backend.repository;

import jakarta.validation.constraints.NotBlank;
import org.apache.el.stream.Stream;
import org.example.backend.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends CrudRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.userName = :userName")
    Optional<User> findByUserName(@NotBlank(message = "姓名不能为空") String userName);
}
