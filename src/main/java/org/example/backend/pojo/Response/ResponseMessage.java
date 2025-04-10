package org.example.backend.pojo.Response;

import org.example.backend.pojo.User;
import org.springframework.http.HttpStatus;

public class ResponseMessage<T>{

    private Integer status;
    private String message;
    private T data;

    public ResponseMessage(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return super.toString();
    }
    public static <T> ResponseMessage<T> success(T data) {
        return new ResponseMessage<>(HttpStatus.OK.value(), "success", data);
    }
    public static <T> ResponseMessage<T> error(Integer status, String message) {
        return new ResponseMessage<>(status, message, null);
    }

}
