package com.gcsj2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 注册请求 DTO
 */
@Data
public class RegisterDTO {

    @NotBlank(message = "账号不能为空")
    @Size(min = 6, max = 20, message = "账号长度需在6-20位之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 16, message = "密码长度需在6-16位之间")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    /** 昵称（可选） */
    private String nickName;
}
