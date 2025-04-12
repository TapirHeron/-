package org.example.backend.service;

import org.example.backend.pojo.Response.AnalysisResponse;
import org.example.backend.pojo.UserTransData;
import org.springframework.web.multipart.MultipartFile;

public interface IDataService {
    AnalysisResponse saveAndAnalysis(UserTransData userTransData, MultipartFile image) throws Exception;
}
