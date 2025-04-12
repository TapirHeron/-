package org.example.backend.service;
import org.example.backend.pojo.Response.AnalysisResponse;
import org.example.backend.pojo.UserTransData;
import org.example.backend.repository.IDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class DataService implements IDataService {
    @Autowired
    private IDataRepository dataRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public AnalysisResponse saveAndAnalysis(UserTransData userTransData, MultipartFile image) throws Exception {
        dataRepository.save(userTransData);
        return analysis(userTransData, image);
    }
    private AnalysisResponse analysis(UserTransData userTransData, MultipartFile image) throws Exception{
        String text = userTransData.getText();
        String url = "http://127.0.0.1:8080/upload";
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, new AnalysisPostObject(text, image), String.class);
            int status = response.getStatusCode().value();
            if (200 == status) {
                Optional<String> result = Optional.ofNullable(response.getBody());
                System.out.println("返回的结果是：");
                result.ifPresentOrElse(System.out::println, () -> System.out.println("返回结果为空"));
                if (result.isPresent()) {
                    return new AnalysisResponse(result.get(), true);
                }
            }
        }catch (Exception e){
            throw new Exception("上传失败");
        }
        return new AnalysisResponse("上传失败", false);
    }
}
class AnalysisPostObject {
    private String text;
    private MultipartFile image;
    public AnalysisPostObject(String text, MultipartFile image) {
        this.text = text;
        this.image = image;
    }
}
