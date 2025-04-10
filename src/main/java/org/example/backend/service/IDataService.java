package org.example.backend.service;

import org.example.backend.pojo.UserTransData;

public interface IDataService {
    boolean save(UserTransData userTransData) throws Exception;
}
