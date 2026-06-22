package com.gcsj2.common;

import lombok.Data;

/**
 * 统一返回实体
 */
@Data
public class Result<T> {

    /** 状态码：200-成功 401-未登录 500-服务异常 */
    private int code;

    /** 提示消息 */
    private String msg;

    /** 返回数据 */
    private T data;

    private Result() {}

    private Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // ==================== 成功 ====================

    public static <T> Result<T> ok() {
        return new Result<>(200, "操作成功", null);
    }

    /**
     * 只带消息的成功返回（data=null，适配 Result<Void> 场景）
     */
    public static <T> Result<T> ok(String msg) {
        return new Result<>(200, msg, null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "操作成功", data);
    }

    public static <T> Result<T> ok(String msg, T data) {
        return new Result<>(200, msg, data);
    }

    // ==================== 失败 ====================

    public static <T> Result<T> error(String msg) {
        return new Result<>(500, msg, null);
    }

    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg, null);
    }
}
