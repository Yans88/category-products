package com.yansen.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponse<T> {
    private int status;
    private String message;
    private T data;
}
