package org.example.backend.repository;

import org.example.backend.pojo.UserTransData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDataRepository extends CrudRepository<UserTransData, Integer> {
}
