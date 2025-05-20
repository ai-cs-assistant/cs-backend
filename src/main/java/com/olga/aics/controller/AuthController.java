package com.olga.aics.controller;

import com.olga.aics.JwtUtil;
import com.olga.aics.dto.LoginRequest;
import com.olga.aics.dto.RegisterRequest;
import com.olga.aics.dto.RegistrationEmailMessage;
import com.olga.aics.entity.User;
import com.olga.aics.service.RegistrationMessageProducer;
import com.olga.aics.service.TokenBlacklistService;
import com.olga.aics.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    private RegistrationMessageProducer messageProducer;

    @GetMapping("/encode")
    public String encodePassword(@RequestParam String pwd) {
        return passwordEncoder.encode(pwd);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // 檢查使用者名稱是否已存在
        if (userService.findByUsername(request.getUsername()) != null) {
            return ResponseEntity.badRequest().body("使用者名稱已存在");
        }

        // 檢查電子郵件是否已存在
        if (userService.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.badRequest().body("電子郵件已被註冊");
        }

        try {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword())); // 加密密碼
            user.setEmail(request.getEmail());
            user.setRole("USER");

            userService.createUser(user);

            // 發送註冊確認郵件
            RegistrationEmailMessage emailMessage = new RegistrationEmailMessage(
                request.getEmail(),
                request.getUsername()
            );
            messageProducer.sendRegistrationEmail(emailMessage);

            return ResponseEntity.ok("註冊成功，確認郵件已發送");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("註冊過程中發生錯誤：" + e.getMessage());
        }
    }

    //  @PostMapping("/login")
    //  public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
    //      // 這裡先簡單驗證，實際應用中應該要檢查資料庫
    //      if ("admin".equals(loginRequest.getUsername()) &&
    //          "admin123".equals(loginRequest.getPassword())) {
    //          String token = jwtUtil.generateToken(loginRequest.getUsername());
    //          return ResponseEntity.ok(token);
    //      }
    //      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    //  }

     @PostMapping("/login")
     public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
         try {
             User user = userService.findByUsername(loginRequest.getUsername());

             if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                 String token = jwtUtil.generateToken(user.getUsername());
                 
                 Map<String, Object> response = new HashMap<>();
                 response.put("token", token);
                 response.put("username", user.getUsername());
                 response.put("role", user.getRole());
                 response.put("email", user.getEmail());
                 
                 return ResponseEntity.ok(response);
             }
 
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                     .body("使用者名稱或密碼錯誤");
         } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                     .body("登入過程中發生錯誤：" + e.getMessage());
         }
     }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            // 從請求頭中獲取 token
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                
                // 獲取 token 的過期時間
                long expirationTime = jwtUtil.extractExpiration(token).getTime();
                
                // 將 token 加入黑名單
                tokenBlacklistService.blacklistToken(token, expirationTime);
            }

            // 清除 SecurityContext 中的認證資訊
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                SecurityContextHolder.clearContext();
            }

            Map<String, String> response = new HashMap<>();
            response.put("message", "登出成功");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("登出過程中發生錯誤：" + e.getMessage());
        }
    }
}
