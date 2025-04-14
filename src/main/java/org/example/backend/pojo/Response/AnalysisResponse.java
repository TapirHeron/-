package org.example.backend.pojo.Response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;

@Getter
public class AnalysisResponse{
    private JSONObject result;
    private boolean status;

    public AnalysisResponse(JSONObject result, boolean status) {
        this.result = result;
        this.status = status;
    }

}
