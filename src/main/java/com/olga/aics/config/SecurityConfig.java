package com.olga.aics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //Use JWT
        http
                // 1. 禁用 CSRF（跨站請求偽造保護），常用於 REST API 無需保持 session 狀態
                .csrf(csrf -> csrf.disable())

                // 2. 設定授權規則
                .authorizeHttpRequests(auth -> auth
                        // 登出需要認證
                        .requestMatchers("/auth/logout").authenticated()
                        // 公開端點
                        .requestMatchers("/auth/**").permitAll()
                        // 允許 WebSocket 端點訪問
                        .requestMatchers("/ws/**").permitAll()

                        // 其他 API 需要認證
                        .requestMatchers("/api/**").authenticated()
                        // 其他所有請求都必須認證過（登入後才能訪問）
                        .anyRequest().authenticated()
                )

                // 在 Spring Security 的 UsernamePasswordAuthenticationFilter 之前加入自定義的 jwtFilter
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        // 回傳設定好的 SecurityFilterChain
        return http.build();
    }

    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //     //Use Basic Auth
    //     http
    //             .csrf(csrf -> csrf.disable()) // 關閉 CSRF
    //             .authorizeHttpRequests(auth -> auth
    //                     .anyRequest().permitAll() // 放行所有請求
    //             );
    //     return http.build();
    // }

}
