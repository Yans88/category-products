package com.yansen.dtos.response;

import org.springframework.stereotype.Component;

@Component
public class ResponseGenerator<T> {

    public <T> CommonResponse<T> responseData(int status, String message, T data) {
        CommonResponse commonResponse = new CommonResponse<>();
        commonResponse.setStatus(status);
        commonResponse.setMessage(message);
        commonResponse.setData(data);
        return commonResponse;
    }
}
