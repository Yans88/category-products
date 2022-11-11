package com.yansen.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResponse<T> {
    private int status;
    private String message;
    private T data;
}
