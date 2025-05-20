package com.olga.aics.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendRegistrationEmail(String to, String username) throws MessagingException {
        String emailContent = String.format("""
            <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <h2 style="color: #2c3e50;">歡迎加入我們！</h2>
                <p>親愛的 %s，</p>
                <p>感謝您註冊我們的服務。您的帳號已經成功建立。</p>
                <p>如果您有任何問題，請隨時與我們聯繫。</p>
                <div style="margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee;">
                    <p style="color: #7f8c8d; font-size: 12px;">
                        此郵件由系統自動發送，請勿回覆。
                    </p>
                </div>
            </div>
            """, username);
        
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject("歡迎註冊我們的服務");
        helper.setText(emailContent, true);
        
        mailSender.send(message);
    }
} 