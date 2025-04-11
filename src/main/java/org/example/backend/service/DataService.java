package org.example.backend.service;
import org.example.backend.pojo.Dto.UserTransDataDto;
import org.example.backend.pojo.Response.AnalysisResponse;
import org.example.backend.pojo.UserTransData;
import org.example.backend.repository.IDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Optional;

@Service
public class DataService implements IDataService {
    @Autowired
    private IDataRepository dataRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public AnalysisResponse saveAndAnalysis(UserTransData userTransData, UserTransDataDto UserTransDataDto) throws Exception {
        dataRepository.save(userTransData);
        return analysis(UserTransDataDto);
    }
    private AnalysisResponse analysis(UserTransDataDto userTransDataDdto) throws Exception{
        String url = "http://127.0.0.1:8080/upload";
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, userTransDataDdto, String.class);
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
