package com.olga.aics.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    
    @NotBlank(message = "使用者名稱不能為空")
    @Size(min = 3, max = 50, message = "使用者名稱長度必須在3到50個字元之間")
    private String username;

    @NotBlank(message = "電子郵件不能為空")
    @Email(message = "請提供有效的電子郵件地址")
    private String email;

    @NotBlank(message = "密碼不能為空")
    @Size(min = 6, max = 100, message = "密碼長度必須在6到100個字元之間")
    private String password;

    @NotBlank(message = "確認密碼不能為空")
    private String confirmPassword;

    private String fullName;
    private String phoneNumber;
} 