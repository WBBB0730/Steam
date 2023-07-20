package com.wbbb.steam.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 自定义状态码
 */
@Getter
@AllArgsConstructor
public enum ResponseCode {
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求错误"),
    UNAUTHORIZED(401, "请先登录"),
    FORBIDDEN(403, "拒绝访问"),
    NOT_FOUND(404, "资源不存在"),
    CONFLICT(409, "资源冲突"),
    SERVER_ERROR(500, "服务器错误");

    private final Integer code;
    private final String message;
}
