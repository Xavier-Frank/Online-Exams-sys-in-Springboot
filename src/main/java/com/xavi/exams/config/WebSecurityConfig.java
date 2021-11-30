package com.xavi.exams.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Properties;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    //define the bean for sending emails
    @Bean
    public JavaMailSender getJavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("oduorfrancis134@gmail.com");
        mailSender.setPassword("vrzfeugozfjdjgaa");

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");

        return mailSender;

    }

    // customizing login page for an instructor
    @Configuration
    @Order(1)
    public static class App1ConfigurationAdapter extends WebSecurityConfigurerAdapter{
//        public App1ConfigurationAdapter{
//            super();
//        }
        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception{
            httpSecurity.authorizeRequests()
                    .anyRequest().permitAll()
                    .antMatchers("/static/**", "/index").permitAll()
                    .antMatchers("/instructor/**").authenticated().
                    and().formLogin().loginProcessingUrl("/api/main/user-type")
                    .usernameParameter("staffId")
                    .defaultSuccessUrl("/api/instructor/lec-dashboard?success").
                    failureUrl("/api/instructor/lec-loginform?error").permitAll().and().
                    logout().deleteCookies().logoutUrl("/api/instructor/instructor-logout").permitAll()
                    .and()
                    .csrf()
                    .disable();
        }
    }

    @Configuration
    @Order(2)
    public static class App2ConfigurationAdapter extends WebSecurityConfigurerAdapter{

        public void configure(HttpSecurity httpSecurity) throws Exception{

            httpSecurity.antMatcher("/student/**").authorizeRequests().anyRequest().authenticated()
                    .antMatchers("/static/**", "/index").permitAll().and()
                    .formLogin().loginProcessingUrl("/api/main/user-type")
                    .usernameParameter("learnerId")
                    .failureUrl("/api/student/stud-loginform?error")
                    .defaultSuccessUrl("/api/student/stud-dashboard?success").and()
                    .logout().deleteCookies().logoutUrl("/api/student/student-logout").permitAll().and()
                    .exceptionHandling()
                    .and()
                    .csrf().disable();
        }
    }
}
