package com.mobai.service;

import com.mobaijun.common.constant.DateConstant;
import com.mobaijun.common.util.date.LocalDateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * Software：IntelliJ IDEA 2021.2 x64
 * Author: <a href="https://www.mobaijun.com">...</a>
 * Date: 2021/12/7 16:13
 * InterfaceName:MailService
 * 接口描述： 邮件接口
 *
 * @author mobai
 */
@Service
public class MailService {

    /**
     * 注入用户名
     */
    @Value("${spring.mail.username}")
    private String from;

    /**
     * 注入邮件发送对象
     */
    @Resource
    private JavaMailSender mailSender;

    /**
     * 邮件对象
     */
    public void sendSimpleMail() {
        // 1.创建邮件发送对象
        SimpleMailMessage message = new SimpleMailMessage();
        // 2.邮件接收者
        message.setTo("mobaijun8@163.com");
        // 3.邮件主题
        message.setSubject("备份成功通知邮件！");
        // 4.邮件内容
        message.setText("备份成功啦:" + LocalDateUtil.toFormat(LocalDateTime.now(), DateConstant.YYYY_MM_DD_HH_MM_SS));
        // 5.邮件发送者
        message.setFrom(from);
        mailSender.send(message);
    }
}