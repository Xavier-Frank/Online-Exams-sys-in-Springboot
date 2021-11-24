package com.xavi.exams.controller;

import com.xavi.exams.models.Learner;
import com.xavi.exams.models.Utility;
import com.xavi.exams.services.LearnerService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/api/student")
public class LearnerController {
    @Autowired
    private LearnerService learnerService;

    @Autowired
    private JavaMailSender mailSender;

    // Return Student Dashboard
    @GetMapping("/stud-dashboard")
    public String studentDashboard(){
        return "/student/stud-dashboard";
    }

    //return the login page
    @GetMapping("/stud-loginform")
    public String studentLoginForm(){
        return "/student/stud-loginform";
    }

    //return registration form
    @GetMapping("/stud-registrationform")
    public String studentRegistrationForm(Model model){
        model.addAttribute("learner", new Learner());
        return "/student/stud-registration";
    }

    /* ############### Login and registration controllers with forgot password fxns ################################## */
    //register a learner
    @PostMapping("/stud-registrationProcess")
    public String studentRegistrationProcess(Learner learner){
        //encrypt password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(learner.getPassword());
        learner.setPassword(encodedPassword);

        learnerService.registerLeaner(learner);

        return "redirect:/api/student/stud-loginform?success";

    }
    //reset Password
    @GetMapping("/forgotPassword")
    public String showStudentForgotPasswordform(){
        return "/student/forgotPasswordStud";
    }

    @PostMapping("/forgotPasswordProcess")
    public ModelAndView forgotPasswordProcess(HttpServletRequest request, ModelAndView modelAndView) {
        String email = request.getParameter("email");
        String token = RandomString.make(25);

        try {
            learnerService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/api/student/resetPassword?token=" + token;
            sendEmail(email, resetPasswordLink);
            modelAndView.setViewName("redirect:/api/student/forgotPassword?success");

        } catch (ClassNotFoundException | MessagingException | UnsupportedEncodingException e) {
            modelAndView.setViewName("redirect:/api/student/forgotPassword?error");
        }
        return modelAndView;
    }

    public void sendEmail(String recipientEmail, String link) throws MessagingException,UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("supportcenter@gmail.com", "Password Support");
        helper.setTo(recipientEmail);

        String subject = "Password reset link";
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);

    }
    //Reset the password
    @GetMapping("/resetPassword")
    public ModelAndView showResetPasswordForm(@Param(value = "token") String token, ModelAndView modelAndView ){
        Learner learner  = learnerService.getByResetPasswordToken(token);

        modelAndView.addObject("token", token);

        if (learner == null){
            modelAndView.addObject("errorMessage", "Invalid token");
        }
        modelAndView.setViewName("/student/resetPasswordStud");
        return modelAndView;
    }
    @PostMapping("/resetPasswordProcess")
    public ModelAndView resetPasswordProcess(ModelAndView modelAndView, HttpServletRequest request){
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        Learner learner  = learnerService.getByResetPasswordToken(token);

        if (learner == null) {
            modelAndView.addObject("errorMessage", "Invalid token");
            modelAndView.setViewName("redirect:/api/student/resetPassword?errorMessage");
        } else {
            learnerService.updatePassword(learner, password);
            modelAndView.addObject("successMessage", "You have succesfully changed your password");
            modelAndView.setViewName("redirect:/api/student/stud-loginform?successMessage");
            return modelAndView;
        }
        return modelAndView;
    }
    //handling the exception of accessing the resetPassword page without token redirects to login page
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingTokens(MissingServletRequestParameterException ex){
        return new ModelAndView("redirect:/api/main/user-type");
    }
    /* ############### End of Login and registration controllers with forgot password fxns ################################## */








}
