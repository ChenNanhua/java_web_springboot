package com.example.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Async
@Service
public class MailService{
    @Autowired
    JavaMailSenderImpl mailSender;

    public void sendMail(String mail, String mailText) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject("手机商城的购物清单");
            helper.setText(mailText, true);
            helper.setTo(mail);
            helper.setFrom("2630966248@qq.com");
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            System.out.println("发送邮件失败。");
            e.printStackTrace();
        }
    }
}
