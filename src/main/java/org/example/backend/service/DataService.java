package org.example.backend.service;
import ch.qos.logback.core.joran.spi.HttpUtil;
import org.example.backend.pojo.UserTransData;
import org.example.backend.repository.IDataRepository;
import org.springframework.stereotype.Service;

@Service
public class DataService implements IDataService {
    private IDataRepository dataRepository;
    public boolean save(UserTransData userTransData) throws Exception {
        dataRepository.save(userTransData);
        return analysis(userTransData);
    }
    private boolean analysis(UserTransData userTransData) throws Exception{
        String url = "http://127.0.0.1:5000/analysis";
        HttpUtil HttpClientUtil = new HttpUtil();
        HttpClientUtil.post(url,userTransData);
        return true;
    }
}
