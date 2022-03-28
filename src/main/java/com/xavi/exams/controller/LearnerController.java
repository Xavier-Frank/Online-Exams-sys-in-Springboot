package com.xavi.exams.controller;

import com.xavi.exams.models.*;
import com.xavi.exams.services.ExamService;
import com.xavi.exams.services.LearnerService;
import com.xavi.exams.services.NotificationService;
import com.xavi.exams.services.UserNotFoundException;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequestMapping("/api/student")
public class LearnerController {
    @Autowired
    private LearnerService learnerService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ExamService examService;

    /* ====================== Navigation controllers for student dashboard ===========================*/
    // Return Student Dashboard
    @GetMapping("/stud-dashboard")
    public String studentDashboard(){
        return "/student/stud-dashboard";
    }
    //return profile
    @GetMapping("/profile")
    public String studentProfile(){
        return "/student/stud-profile";
    }

    //Return notifications
    @GetMapping("/notification")
    public String studentNotification(Model model){
        List<Notifications> notifications = notificationService.notificationsList();
        model.addAttribute("notifications", notifications);
        return "/student/stud-notification";
    }

    //Return calendar
    @GetMapping("/calendar")
    public String studentCalendar(){
        return "/student/stud-calendar";
    }

    // return Assessment page
    @GetMapping("/assessments")
    public String studentAssessment(Model model){

        List<Exams> examsList = examService.assessmentList();
        model.addAttribute("examsList", examsList);

        return "/student/stud-assessments";
    }

    //return results
    @GetMapping("/results")
    public String studentResults(){
        return "/student/stud-results";
    }
    @GetMapping("/stud-OtpValidation")
    public String otpValidation() {
        return "/student/OTPValidation";
    }

    /* ====================== // Navigation controllers for student dashboard ===========================*/


    //return the login page
    @RequestMapping(value = "/stud-loginform", method = { RequestMethod.GET})
    public ModelAndView loginPage() {
        return new ModelAndView("/student/stud-loginform");
    }

    // handle logouts
    @GetMapping("/student-logout")
    public String studentLogout(){
        return "/api/main/user-type?logOutSuccess";
    }


    //return registration form
    @GetMapping("/stud-registrationform")
    public String studentRegistrationForm(Model model){
        model.addAttribute("learner", new Learner());
        model.addAttribute("pageTitle", "Learner Registration Form");
        model.addAttribute("formHeader", "Create An Account");
        return "/student/stud-registration";
    }

    /* ############### Login and registration controllers ################################## */
    //register a learner
    @PostMapping("/stud-registrationProcess")
    public String studentRegistrationProcess(Learner learner){
        learnerService.registerLeaner(learner);
        return "redirect:/api/student/stud-loginform?successM";
    }

    //login a student
    @PostMapping("/generateOTP")
    public ModelAndView studentLoginProcess(HttpServletRequest httpServletRequest, ModelAndView modelAndView, Learner learner){
        String learnerId = httpServletRequest.getParameter("learnerId");

        //generate random integer
        String otp = RandomString.make(4);

        // check if staff id and save the otp in database
        try {
            learnerService.validateLearnerId(otp, learnerId);
            String link = Utility.getSiteURL(httpServletRequest) + "/api/student/stud-OtpValidationPage?otp=" + otp;
            String email = learnerService.getUserByEmailAddress(learnerId);
            learnerService.sendEmail(email, link, otp);
            modelAndView.setViewName("redirect:/api/student/stud-loginform?success");


        } catch (ClassNotFoundException | MessagingException | UnsupportedEncodingException e) {
            modelAndView.setViewName("redirect:/api/student/stud-loginform?error");

        }
        return modelAndView;
    }

    //Get the login form
    @GetMapping("/stud-OtpValidationPage")
    public ModelAndView showResetPasswordForm(@Param(value = "otp") String otp, ModelAndView modelAndView) {
        Learner learner = learnerService.getByOneTimePassword(otp);

        modelAndView.addObject("otp", otp);

        if (learner == null) {
            modelAndView.addObject("errorMessage", "Invalid OTP");
            modelAndView.setViewName("redirect:/api/student/stud-OtpValidation?invalid");
        } else {
            modelAndView.setViewName("redirect:/api/student/stud-OtpValidation?success");
            return modelAndView;
        }
        return modelAndView;
    }

    //Login an instructor
    @PostMapping("/stud-otpVerification")
    public ModelAndView verifyOTP(ModelAndView modelAndView, HttpServletRequest httpServletRequest) {
        String pass = httpServletRequest.getParameter("pass");


        Learner learner  = learnerService.getByOneTimePassword(pass);

        if (learner != null) {
            learnerService.loginLearner(learner);
            modelAndView.setViewName("redirect:/api/student/stud-dashboard?success");
            return modelAndView;
        } else {
            modelAndView.setViewName("redirect:/api/student/stud-OtpValidation?error");
        }
        return modelAndView;
    }

   //handling the exception of accessing the resetPassword page without token redirects to login page
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingTokens(MissingServletRequestParameterException ex) {
        return new ModelAndView("redirect:/api/student/stud-loginform?successM");
    }

    //Search a Notification
    @GetMapping("/searchNotification")
    public String searchNotification(@Param("keyword") String keyword, Model model){
        List<Notifications> searchedNotification = notificationService.searchNotification(keyword);


        model.addAttribute("searchedNotification", searchedNotification);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", "Search Results for: '" + keyword + "'");


        return "/student/search-results/search-notifications";
    }

    //Search exams
    @GetMapping("/searchAssessment")
    public String searchExams(@Param("keyword") String keyword, Model model){
        List<Exams> searchedExam = examService.searchExams(keyword);

        model.addAttribute("searchedExam", searchedExam);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", "Search Results for: '" + keyword + "'");

        return "/student/search-results/search-exams";
    }

    //Edit user profile
    @GetMapping("/edit-profile/{learnerId}")
    public String EditUserProfile(@PathVariable("learnerId") String learnerId, Model model) {

        try{
            Learner learner  = learnerService.get(learnerId);
            model.addAttribute("learner", learner);
            model.addAttribute("pageTitle", "Edit Learner Page");
            model.addAttribute("formHeader", "Edit Your Details: " + "User id:" + "(" + learnerId + ")");
        } catch (UserNotFoundException e){
            return "redirect:/api/student/profile?erroredit";
        }


        return "/student/stud-profile-edit";
    }

    //edit learner details
    @PostMapping("/stud-profileEdit")
    public String editLearnerProfile(Learner learner){
        learnerService.registerLeaner(learner);
        return "redirect:/api/student/profile?successedit";
    }



    /* ############### End of Login and registration controllers with forgot password fxns ################################## */


}
