package org.example.backend.service;
import org.example.backend.pojo.Response.AnalysisResponse;
import org.example.backend.pojo.UserTransData;
import org.example.backend.repository.IDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
    // 调用大模型服务
    private AnalysisResponse analysis(UserTransData userTransData, MultipartFile image) throws Exception{
        String text = userTransData.getText();
        String url = "http://localhost:8081/upload";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("text", text);
        body.add("image", new org.springframework.core.io.InputStreamResource(image.getInputStream()) {
            @Override
            public String getFilename() {
                return image.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            int status = response.getStatusCode().value();
            if (200 == status) {
                Optional<String> result = Optional.ofNullable(response.getBody());
                System.out.println("返回的结果是：");
                result.ifPresentOrElse(System.out::println, () -> System.out.println("返回结果为空"));
                if (result.isPresent()) {
                    return new AnalysisResponse(result.get(), true);
                }
            }
        } catch (Exception e) {
            throw new Exception("上传失败", e);
        }
        return new AnalysisResponse("上传失败", false);
    }
}
