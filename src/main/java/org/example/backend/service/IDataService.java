package org.example.backend.service;

import org.example.backend.pojo.Dto.UserTransDataDto;
import org.example.backend.pojo.Response.AnalysisResponse;
import org.example.backend.pojo.UserTransData;

public interface IDataService {
    AnalysisResponse saveAndAnalysis(UserTransData userTransData, UserTransDataDto userTransDataDto) throws Exception;
}
