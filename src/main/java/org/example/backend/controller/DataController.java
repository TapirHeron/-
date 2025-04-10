package org.example.backend.controller;

import org.example.backend.pojo.Dto.UserTransDataDto;
import org.example.backend.pojo.Response.ResponseMessage;
import org.example.backend.pojo.UserTransData;
import org.example.backend.service.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class DataController {
    @Autowired
    private IDataService dataService;
    @RequestMapping("/uploadData")
    public ResponseMessage<UserTransData> uploadData(@RequestBody UserTransDataDto userTransDataDto) {
        UserTransData userTransData = new UserTransData(UserController.getCurrentUser().getUserId(), userTransDataDto.getImage(), userTransDataDto.getText());
        if (dataService.save(userTransData)) {
            return ResponseMessage.success(userTransData);
        }
        return ResponseMessage.error(404, "上传失败");
    }
}
