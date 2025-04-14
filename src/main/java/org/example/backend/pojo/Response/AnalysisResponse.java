package org.example.backend.pojo.Response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

@Getter
public class AnalysisResponse {
    private AnalysisResult analyResult;
    private boolean status;

    public AnalysisResponse(JSONObject result, boolean status) {
        this.analyResult = JSONObject.toJavaObject(result, AnalysisResult.class);
        this.status = status;
    }

    @Override
    public String toString() {
        return "analyResult" + analyResult + "status:" + status;
    }
}
@Getter
@Setter
class AnalysisResult {
    private float[][] logits_multicls;
    private String file_path;
    private float[][] output_coord;
    private float[][] probs_real_fake;
    private float[][][] logits_tok;
    private String text;
    private String message;
    private float[][] logits_real_fake;

    public AnalysisResult() {

    }
    public String toString() {
        return "AnalysisResult{" +
                "logits_multicls=" + JSON.toJSONString(logits_multicls) +
                ", file_path='" + file_path + '\'' +
                ", output_coord=" + JSON.toJSONString(output_coord) +
                ", probs_real_fake=" + JSON.toJSONString(probs_real_fake) +
                ", logits_tok=" + JSON.toJSONString(logits_tok) +
                ", text='" + text + '\'' +
                ", message='" + message + '\'' +
                ", logits_real_fake=" + JSON.toJSONString(logits_real_fake) +
                '}';
    }
}
