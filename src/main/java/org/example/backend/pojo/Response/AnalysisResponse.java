package org.example.backend.pojo.Response;

public class AnalysisResponse{
    private String result;
    private boolean status;
    public AnalysisResponse(String result, boolean status) {
        this.result = result;
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public boolean getStatus() {
        return status;
    }
}
