package com.wbbb.steam.controller;

import com.wbbb.steam.dto.response.ResponseDto;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/** 全局异常处理器 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    public ResponseDto<?> handleBadRequest(Exception e) {
        System.out.println(e.getMessage());
        return ResponseDto.badRequest(null, "请求参数错误");
    }

    @ExceptionHandler({MissingRequestHeaderException.class})
    public ResponseDto<?> handleUnauthorized(Exception e) {
        System.out.println(e.getMessage());
        return ResponseDto.unauthorized(null);
    }

    @ExceptionHandler({Exception.class})
    public ResponseDto<?> handleAllException(Exception e) {
        System.out.println(e.getMessage());
        return ResponseDto.serverError(null, "未知错误");
    }
}
