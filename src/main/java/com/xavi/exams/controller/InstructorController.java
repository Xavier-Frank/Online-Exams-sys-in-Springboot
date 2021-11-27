package com.xavi.exams.controller;

import com.xavi.exams.models.Instructor;
import com.xavi.exams.models.Utility;
import com.xavi.exams.services.InstructorService;
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
@RequestMapping("/api/instructor")
public class InstructorController {
    @Autowired
    private InstructorService instructorService;

    @Autowired
    private JavaMailSender mailSender;

    //  Return Lecturers Dashboard
    @GetMapping("/lec-dashboard")
    public String instructorDashboard(){
        return "/instructor/lec-dashboard";
    }
    // Return Lecturers Profile
    @GetMapping("/lec-profile")
    public String instructorProfile(){
        return "/instructor/lec-profile";
    }

    //return the login page
    @GetMapping("/lec-loginform")
    public String instructorLoginForm(){
        return "/instructor/lec-loginform";
    }

    //return registration form
    @GetMapping("/lec-registrationform")
    public String intructorRegistrationForm(Model model){
        model.addAttribute("instructor", new Instructor());
        return "/instructor/lec-registration";
    }

    /* ======================= Dashboard Navigation =================================== */
//    Return overview
    @GetMapping("/lec-overview")
    public String instructorOverview(){
        return "/instructor/overviewAdmin";
    }


    /* ======================= End of Dashboard Navigation =================================== */


    /* ############### Login and registration controllers with forgot password fxns ################################## */
    //register an instructor
    @PostMapping("/lec-regitrationProcess")
    public String lecRegistrationProcess(Instructor instructor){
        //encrypt instructor's password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(instructor.getPassword());
        instructor.setPassword(encodedPassword);

        instructorService.registerInstructor(instructor);
        return "redirect:/api/instructor/lec-loginform?success";
    }

    //forgot password handler
    @GetMapping("/forgotPassword")
    public String showForgotPasswordForm(){
        return "/instructor/forgotPassword";
    }

    @PostMapping("/forgotPasswordProcess")
    public ModelAndView forgotPasswordProcess(HttpServletRequest request, ModelAndView modelAndView) {
        String email = request.getParameter("email");
        String token = RandomString.make(25);

        try {
            instructorService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/api/instructor/resetPassword?token=" + token;
            sendEmail(email, resetPasswordLink);
            modelAndView.setViewName("redirect:/api/instructor/forgotPassword?success");

        } catch (ClassNotFoundException | MessagingException | UnsupportedEncodingException e) {
            modelAndView.setViewName("redirect:/api/instructor/forgotPassword?error");
        }
        return modelAndView;
    }
    //send the email
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
        Instructor instructor = instructorService.getByResetPasswordToken(token);

        modelAndView.addObject("token", token);

        if (instructor == null){
            modelAndView.addObject("errorMessage", "Invalid token");
        }
        modelAndView.setViewName("/instructor/resetPassword");
        return modelAndView;
    }

    @PostMapping("/resetPasswordProcess")
    public ModelAndView resetPasswordProcess(ModelAndView modelAndView, HttpServletRequest request){
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        Instructor instructor = instructorService.getByResetPasswordToken(token);

        if (instructor == null) {
            modelAndView.addObject("errorMessage", "Invalid token");
            modelAndView.setViewName("redirect:/api/instructor/resetPassword?errorMessage");
        } else {
            instructorService.updatePassword(instructor, password);
            modelAndView.addObject("successMessage", "You have succesfully changed your password");
            modelAndView.setViewName("redirect:/api/instructor/lec-loginform?successMessage");
            return modelAndView;
        }
        return modelAndView;
    }
    //handling the exception of accessing the resetPassword page without token redirects to login page
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingTokens(MissingServletRequestParameterException ex){
        return new ModelAndView("redirect:/api/main/user-type");
    }
    /* ######################################### End of Login and registration controllers ################################## */



}
