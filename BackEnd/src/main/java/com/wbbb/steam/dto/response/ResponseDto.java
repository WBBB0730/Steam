package com.wbbb.steam.dto.response;

import com.wbbb.steam.constant.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseDto<T> {
    private Integer code;
    private String message;
    private T data;

    /**
     * 200，操作成功
     */
    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<T>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
    }

    /**
     * 200，操作成功
     */
    public static <T> ResponseDto<T> success(T data, String message) {
        return new ResponseDto<T>(ResponseCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 400，请求错误
     */
    public static <T> ResponseDto<T> badRequest(T data) {
        return new ResponseDto<T>(ResponseCode.BAD_REQUEST.getCode(), ResponseCode.BAD_REQUEST.getMessage(), data);
    }

    /**
     * 400，请求错误
     */
    public static <T> ResponseDto<T> badRequest(T data, String message) {
        return new ResponseDto<T>(ResponseCode.BAD_REQUEST.getCode(), message, data);
    }

    /**
     * 401，请先登录
     */
    public static <T> ResponseDto<T> unauthorized(T data) {
        return new ResponseDto<T>(ResponseCode.UNAUTHORIZED.getCode(), ResponseCode.UNAUTHORIZED.getMessage(), data);
    }

    /**
     * 401，请先登录
     */
    public static <T> ResponseDto<T> unauthorized(T data, String message) {
        return new ResponseDto<T>(ResponseCode.UNAUTHORIZED.getCode(), message, data);
    }

    /**
     * 403，拒绝访问
     */
    public static <T> ResponseDto<T> forbidden(T data) {
        return new ResponseDto<T>(ResponseCode.FORBIDDEN.getCode(), ResponseCode.FORBIDDEN.getMessage(), data);
    }

    /**
     * 403，拒绝访问
     */
    public static <T> ResponseDto<T> forbidden(T data, String message) {
        return new ResponseDto<T>(ResponseCode.FORBIDDEN.getCode(), message, data);
    }

    /**
     * 404，资源不存在
     */
    public static <T> ResponseDto<T> notFound(T data) {
        return new ResponseDto<T>(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage(), data);
    }

    /**
     * 404，资源不存在
     */
    public static <T> ResponseDto<T> notFound(T data, String message) {
        return new ResponseDto<T>(ResponseCode.NOT_FOUND.getCode(), message, data);
    }

    /**
     * 409，资源冲突
     */
    public static <T> ResponseDto<T> conflict(T data) {
        return new ResponseDto<T>(ResponseCode.CONFLICT.getCode(), ResponseCode.CONFLICT.getMessage(), data);
    }

    /**
     * 409，资源冲突
     */
    public static <T> ResponseDto<T> conflict(T data, String message) {
        return new ResponseDto<T>(ResponseCode.CONFLICT.getCode(), message, data);
    }

    /**
     * 500，服务器错误
     */
    public static <T> ResponseDto<T> serverError(T data) {
        return new ResponseDto<T>(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMessage(), data);
    }

    /**
     * 500，服务器错误
     */
    public static <T> ResponseDto<T> serverError(T data, String message) {
        return new ResponseDto<T>(ResponseCode.SERVER_ERROR.getCode(), message, data);
    }
}
