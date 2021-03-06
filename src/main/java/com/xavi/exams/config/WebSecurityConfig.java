package com.xavi.exams.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{


    //define the bean for sending emails
    @Bean
    public JavaMailSender getJavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("oduorfrancis134@gmail.com");
        mailSender.setPassword("xjdqcenacsqmuiwt");

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");



//        proxy
//        properties.setProperty("http.proxyHost", "172.16.63.3");
//        properties.setProperty("http.proxyPort", "3128");
//        properties.setProperty("http.nonProxyHosts", "localhost|127.0.0.1|10.*.*.*");
//        properties.setProperty("https.proxyHost", "172.16.63.3");
//        properties.setProperty("https.proxyPort", "3128");
//        properties.setProperty("https.nonProxyHosts", "localhost|127.0.0.1|10.*.*.*");
//        properties.setProperty("java.net.useSystemProxies", "true");
//        System.setProperty("http.proxyHost", "172.16.63.3");
//        System.setProperty("http.proxyPort", "3128");
//        System.setProperty("http.nonProxyHosts", "localhost|127.0.0.1|10.*.*.*");
//        System.setProperty("https.proxyHost", "172.16.63.3");
//        System.setProperty("https.proxyPort", "3128");
//        System.setProperty("https.nonProxyHosts", "localhost|127.0.0.1|10.*.*.*");
//        System.setProperty("java.net.useSystemProxies", "true");

        return mailSender;

    }

//    @Override
//    protected void configure(HttpSecurity security) throws Exception
//    {
//
//
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().rememberMe().rememberMeParameter("remember-me").tokenValiditySeconds(7200)
                .and().logout().invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .and().logout().logoutSuccessUrl("/api/main/user-type");
        http.httpBasic().disable();

        http.headers()
                .frameOptions().sameOrigin()
                .httpStrictTransportSecurity().disable();
        http.
                csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().
                authorizeRequests().
                anyRequest().permitAll().
                and().
                exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint());

    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }


}
