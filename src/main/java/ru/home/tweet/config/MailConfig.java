package ru.home.tweet.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${spring.mail.host}")
    private String mailHost;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Value("${spring.mail.port}")
    private int mailPort;

    @Value("${spring.mail.protocol}")
    private String mailProtocol;

    @Value("${mail.debug}")
    private String mailDebug;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String enable;

    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setPassword(mailPassword);
        javaMailSender.setUsername(mailUsername);
        javaMailSender.setPort(mailPort);
        javaMailSender.setHost(mailHost);

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.setProperty("mail.transport.protocol", mailProtocol);
        properties.setProperty("mail.debug", mailDebug);

        properties.setProperty("mail.smtp.auth", auth);
        properties.setProperty("mail.smtp.starttls.enable", enable);





        return javaMailSender;

    }
}
