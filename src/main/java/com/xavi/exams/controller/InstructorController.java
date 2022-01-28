package com.xavi.exams.controller;

import com.xavi.exams.models.Instructor;
import com.xavi.exams.models.Learner;
import com.xavi.exams.models.Notifications;
import com.xavi.exams.models.Utility;
import com.xavi.exams.services.InstructorService;
import com.xavi.exams.services.LearnerService;
import com.xavi.exams.services.NotificationService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequestMapping("/api/instructor")
public class InstructorController {
    @Autowired
    private InstructorService instructorService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired private NotificationService notificationService;


    /* ======================= Dashboard Navigation =================================== */
    /* ========================= Navigation controllers ====================================*/

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

    //return Announcements section
    @GetMapping("/announcements")
    public String lecAnnouncements(Model model){

        List<Notifications> notifications = notificationService.notificationsList();
        model.addAttribute("notifications", notifications);
        return "/instructor/lec-notification";
    }
    //Return Calendar
    @GetMapping("/calendar")
    public String lecCalendar(){
        return "/instructor/lec-calendar";
    }
    //Return assessment page
    @GetMapping("/assessment")
    public String lecAssessments(){
        return "/instructor/lec-assessment";
    }
    //return results page
    @GetMapping("/results")
    public String lecResults(){
        return "/instructor/lec-results";
    }
    //return report page
    @GetMapping("/lec-report")
    public String lecReports(){
        return "/instructor/lec-report";
    }
    @GetMapping("/lec-OtpValidation")
    public String otpValidation(){
        return "/instructor/OTPValidation";
    }

    /* ========================= End of Navigation controllers ====================================*/

    /* ======================= End of Dashboard Navigation =================================== */


    /* ################################## Instructor Login, Logout, and Registration ###############################*/

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

    // handle logouts
    @GetMapping("/instructor-logout")
    public String instructorLogout(){
        return "/api/main/user-type?logOutSuccess";
    }

    //register an instructor
    @PostMapping("/lec-regitrationProcess")
    public String lecRegistrationProcess(Instructor instructor){
        instructorService.registerInstructor(instructor);
        return "redirect:/api/instructor/lec-loginform?successMe";
    }

    //login an instructor
    @PostMapping("/generateOTP")
    public  ModelAndView lecLoginProcess(HttpServletRequest httpServletRequest, ModelAndView modelAndView, Instructor instructor){
        String staffId = httpServletRequest.getParameter("staffId");


        //generate random integer
        String otp = RandomString.make(4);


       // check if staff id and save the otp in database
        try {
            instructorService.validateStaffId(otp,staffId);
            String link = Utility.getSiteURL(httpServletRequest) + "/api/instructor/lec-OtpValidationPage?otp=" + otp;
            String email = instructorService.getUserByEmailAddress(staffId);
            instructorService.sendEmail(email,link,otp);
            modelAndView.setViewName("redirect:/api/instructor/lec-loginform?success");


        } catch (ClassNotFoundException | MessagingException | UnsupportedEncodingException e){
            modelAndView.setViewName("redirect:/api/instructor/lec-loginform?error");

        }
        return modelAndView;
    }

    //Get the login form
        @GetMapping("/lec-OtpValidationPage")
        public ModelAndView showResetPasswordForm(@Param(value = "otp") String otp, ModelAndView modelAndView ) {
            Instructor instructor = instructorService.getByOneTimePassword(otp);

            modelAndView.addObject("otp", otp);

            if (instructor == null) {
                modelAndView.addObject("errorMessage", "Invalid OTP");
                modelAndView.setViewName("redirect:/api/instructor/lec-OtpValidation?invalid");
            } else {
                modelAndView.setViewName("redirect:/api/instructor/lec-OtpValidation?success");
                return modelAndView;
            }
            return modelAndView;
        }

        //Login an instructor
    @PostMapping("/lec-otpVerification")
    public ModelAndView verifyOTP(ModelAndView modelAndView, HttpServletRequest httpServletRequest){
        String pass = httpServletRequest.getParameter("pass");

        System.out.println("The passed otp is" + ":" + pass);

        Instructor instructor = instructorService.getByOneTimePassword(pass);

        if (instructor != null){
            instructorService.loginInstructor(instructor);
            modelAndView.setViewName("redirect:/api/instructor/lec-dashboard?success");
            return modelAndView;
        } else {
            modelAndView.setViewName("redirect:/api/instructor/lec-OtpValidation?error");
        }
        return modelAndView;
    }
    //handling the exception of accessing the resetPassword page without token redirects to login page
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingTokens(MissingServletRequestParameterException ex){
        return new ModelAndView("redirect:/api/instructor/lec-loginform?successMe");
    }

    /* ################################## End of Instructor Login, Logout, and Registration ###############################*/

    /* ######################################### Start Instructor Operations #############################*/

    @Autowired private LearnerService learnerService;
    // Return  list of learners
    @GetMapping("/list-of-learners")
    public String showLearners(Model model){
        List<Learner> list_of_learners = learnerService.learnerList();
        model.addAttribute("learner", list_of_learners);

        return "/instructor/list-learners";
    }


    /* ######################################### End of Instructor Operations #############################*/




















    /* ############### Login and registration controllers with forgot password fxns ################################## */



//    @PostMapping("/forgotPasswordProcess")
//    public ModelAndView forgotPasswordProcess(HttpServletRequest request, ModelAndView modelAndView) {
//        String email = request.getParameter("email");
//        String token = RandomString.make(25);
//
//        try {
//            instructorService.updateResetPasswordToken(token, email);
//            String resetPasswordLink = Utility.getSiteURL(request) + "/api/instructor/resetPassword?token=" + token;
//            sendEmail(email, resetPasswordLink);
//            modelAndView.setViewName("redirect:/api/instructor/forgotPassword?success");
//
//        } catch (ClassNotFoundException | MessagingException | UnsupportedEncodingException e) {
//            modelAndView.setViewName("redirect:/api/instructor/forgotPassword?error");
//        }
//        return modelAndView;
//    }

//
//    //Reset the password
//    @GetMapping("/resetPassword")
//    public ModelAndView showResetPasswordForm(@Param(value = "token") String token, ModelAndView modelAndView ){
//        Instructor instructor = instructorService.getByResetPasswordToken(token);
//
//        modelAndView.addObject("token", token);
//
//        if (instructor == null){
//            modelAndView.addObject("errorMessage", "Invalid token");
//        }
//        modelAndView.setViewName("/instructor/resetPassword");
//        return modelAndView;
//    }
//
//    @PostMapping("/resetPasswordProcess")
//    public ModelAndView resetPasswordProcess(ModelAndView modelAndView, HttpServletRequest request){
//        String token = request.getParameter("token");
//        String password = request.getParameter("password");
//
//        Instructor instructor = instructorService.getByResetPasswordToken(token);
//
//        if (instructor == null) {
//            modelAndView.addObject("errorMessage", "Invalid token");
//            modelAndView.setViewName("redirect:/api/instructor/resetPassword?errorMessage");
//        } else {
//            instructorService.updatePassword(instructor, password);
//            modelAndView.addObject("successMessage", "You have succesfully changed your password");
//            modelAndView.setViewName("redirect:/api/instructor/lec-loginform?successMessage");
//            return modelAndView;
//        }
//        return modelAndView;
//    }

    /* ######################################### End of Login and registration controllers ################################## */






}
