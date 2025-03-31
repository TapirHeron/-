package org.example.backend.repository;

import jakarta.validation.constraints.NotBlank;
import org.example.backend.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends CrudRepository<User, Integer>, JpaRepository<User, Integer> {
    @Query("select u from User u where u.userName = ?1")
    User findByUserName(@NotBlank(message = "姓名不能为空") String userName);
}
