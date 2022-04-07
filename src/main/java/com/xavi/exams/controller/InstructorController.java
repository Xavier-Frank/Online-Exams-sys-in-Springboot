package com.xavi.exams.controller;

import com.xavi.exams.models.*;
import com.xavi.exams.services.*;
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

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ExamService examService;


    /* ======================= Dashboard Navigation =================================== */
    /* ========================= Navigation controllers ====================================*/

    //  Return Lecturers Dashboard
    @GetMapping("/lec-dashboard")
    public String instructorDashboard() {
        return "/instructor/lec-dashboard";
    }

    // Return Lecturers Profile
    @GetMapping("/lec-profile")
    public String instructorProfile() {

        return "/instructor/lec-profile";
    }

    //return Announcements section
    @GetMapping("/announcements")
    public String lecAnnouncements(Model model) {

        List<Notifications> notifications = notificationService.notificationsList();
        model.addAttribute("notifications", notifications);
        return "/instructor/lec-notification";
    }

    //Return Calendar
    @GetMapping("/calendar")
    public String lecCalendar() {
        return "/instructor/lec-calendar";
    }

    //Return assessment page
    @GetMapping("/assessment")
    public String lecAssessments() {
        return "/instructor/lec-assessment";
    }

    //return results page
    @GetMapping("/results")
    public String lecResults() {
        return "/instructor/lec-results";
    }

    //return report page
    @GetMapping("/lec-report")
    public String lecReports() {
        return "/instructor/lec-report";
    }

    @GetMapping("/lec-OtpValidation")
    public String otpValidation() {
        return "/instructor/OTPValidation";
    }

    /* ========================= End of Navigation controllers ====================================*/

    /* ======================= End of Dashboard Navigation =================================== */


    /* ################################## Instructor Login, Logout, and Registration ###############################*/

    //return the login page
    @GetMapping("/lec-loginform")
    public String instructorLoginForm() {
        return "/instructor/lec-loginform";
    }

    //return registration form
    @GetMapping("/lec-registrationform")
    public String intructorRegistrationForm(Model model) {
        model.addAttribute("instructor", new Instructor());
        model.addAttribute("pageTitle", "User Registration Page");
        model.addAttribute("formHeader", "Create An Account");
        return "/instructor/lec-registration";
    }

    // handle logouts
    @GetMapping("/instructor-logout")
    public String instructorLogout() {
        return "/api/main/user-type?logOutSuccess";
    }

    //register an instructor
    @PostMapping("/lec-regitrationProcess")
    public String lecRegistrationProcess(Instructor instructor, String staffId) {

        try{
            instructorService.registerInstructor(instructor, staffId);
            return "redirect:/api/instructor/lec-loginform?successMe";

        }catch (Exception | UserNotFoundException e){
            return "redirect:/api/instructor/lec-registrationform?error";
        }

    }

    //login an instructor
    @PostMapping("/generateOTP")
    public ModelAndView lecLoginProcess(HttpServletRequest httpServletRequest, ModelAndView modelAndView, Instructor instructor) {
        String staffId = httpServletRequest.getParameter("staffId");


        //generate random integer
        String otp = RandomString.make(4);


        // check if staff id and save the otp in database
        try {
            instructorService.validateStaffId(otp, staffId);
            String link = Utility.getSiteURL(httpServletRequest) + "/api/instructor/lec-OtpValidationPage?otp=" + otp;
            String email = instructorService.getUserByEmailAddress(staffId);
            instructorService.sendEmail(email, link, otp);
            modelAndView.setViewName("redirect:/api/instructor/lec-loginform?success");
        } catch (ClassNotFoundException | MessagingException | UnsupportedEncodingException e) {
            modelAndView.setViewName("redirect:/api/instructor/lec-loginform?error");

        }
        return modelAndView;
    }

    //Get the login form
    @GetMapping("/lec-OtpValidationPage")
    public ModelAndView showResetPasswordForm(@Param(value = "otp") String otp, ModelAndView modelAndView) {
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
    public ModelAndView verifyOTP(ModelAndView modelAndView, HttpServletRequest httpServletRequest) {
        String pass = httpServletRequest.getParameter("pass");


        Instructor instructor = instructorService.getByOneTimePassword(pass);

        if (instructor != null) {
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
    public ModelAndView handleMissingTokens(MissingServletRequestParameterException ex) {
        return new ModelAndView("redirect:/api/instructor/lec-loginform?successM");
    }

    /* ################################## End of Instructor Login, Logout, and Registration ###############################*/



    /* ######################################### Start Instructor Operations #############################*/

    @Autowired
    private LearnerService learnerService;

    // Return  list of learners
    @GetMapping("/list-of-learners")
    public String showLearners(Model model) {
        List<Learner> list_of_learners = learnerService.learnerList();
        model.addAttribute("learner", list_of_learners);

        return "/instructor/list-learners";
    }

    //Search a learner
    @GetMapping("/searchlearner")
    public String searchLearner(Model model, @Param("keyword") String keyword){

        try{
            List<Learner> searchedLearner = learnerService.search(keyword);

            model.addAttribute("keyword", keyword);
            model.addAttribute("searchedLearner", searchedLearner);
            model.addAttribute("pageTitle", "Search Results for: '" + keyword + "'");


        }catch (UserNotFoundException userNotFoundException){
            model.addAttribute("keyword", keyword);
            model.addAttribute("pageTitle", "User not Found");
            model.addAttribute("message", "User does not exist");
            return "/instructor/search/search-results";

        }
        return "/instructor/search/search-results";

    }
    //Search a Notification
    @GetMapping("/searchNotification")
    public String searchNotification(@Param("keyword") String keyword, Model model){
        List<Notifications> searchedNotification = notificationService.searchNotification(keyword);


        model.addAttribute("searchedNotification", searchedNotification);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", "Search Results for: '" + keyword + "'");


        return "/instructor/search/search-notifications";
    }
    //Search exams
    @GetMapping("/searchAssessment")
    public String searchExams(@Param("keyword") String keyword, Model model){
        List<Exams> searchedExam = examService.searchExams(keyword);

        model.addAttribute("searchedExam", searchedExam);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", "Search Results for: '" + keyword + "'");

        return "/instructor/search/search-exams";
    }

    //Edit user profile
    @GetMapping("/edit-profile/{staffId}")
    public String EditUserProfile(@PathVariable("staffId") String staffId, Model model){

        try{
            Instructor instructor = instructorService.get(staffId);
            model.addAttribute("instructor", instructor);
            model.addAttribute("pageTitle", "Edit Instructor Page");
            model.addAttribute("formHeader", "Edit Your Details:" + " " + "User id:" + " " + staffId);
        } catch (UserNotFoundException e){

            return "redirect:/api/instructor/lec-profile?erroredit";

        }
        return "/instructor/lec-profile-edit";
    }

    //edit lec profile
    @PostMapping("/lec-profileEdit")
    public String editLecProfile(Instructor instructor) {
            instructorService.updateInstructor(instructor);
            return "redirect:/api/instructor/lec-profile?successedit";

    }


}


    /* ######################################### End of Instructor Operations #############################*/





















