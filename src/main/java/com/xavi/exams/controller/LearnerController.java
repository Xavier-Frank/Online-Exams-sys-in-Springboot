package com.xavi.exams.controller;

import com.xavi.exams.doa.LearnerRepository;
import com.xavi.exams.models.Exams;
import com.xavi.exams.models.Learner;
import com.xavi.exams.models.Notifications;
import com.xavi.exams.models.Utility;
import com.xavi.exams.services.ExamService;
import com.xavi.exams.services.LearnerService;
import com.xavi.exams.services.NotificationService;
import com.xavi.exams.services.UserNotFoundException;
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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    private LearnerRepository learnerRepository;

    /* ====================== Navigation controllers for student dashboard ===========================*/
    // Return Student Dashboard
    @GetMapping("/stud-dashboard")
    public String studentDashboard(){
        return "/student/stud-dashboard";
    }
    //return profile
    @GetMapping("/profile")
    public String studentProfile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                 Model model) throws ServletException, IOException {

//        String url = "/api/student/stud-otpVerification";
//        ServletContext context = httpServletRequest.getServletContext();
//        RequestDispatcher requestDispatcher = context.getRequestDispatcher(url);
//        requestDispatcher.include(httpServletRequest, httpServletResponse);
        //include content of another servlet

        Learner learner  = (Learner) httpServletRequest.getAttribute("learner");

        String firstName = learner.getFirstName();
        String lastName= learner.getLastName();

        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);

        System.out.println("The name of instructor received is" + learner.getFirstName());

        learnerService.loginLearner(learner);
        model.addAttribute("learner", learner);
        learnerRepository.save(learner);
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
    public String studentRegistrationProcess(Learner learner, String learnerId){
        try{
            learnerService.registerLeaner(learner, learnerId);
            return "redirect:/api/student/stud-loginform?successM";
        }catch (Exception | UserNotFoundException e){
            return "redirect:/api/student/stud-registrationform?error";
        }

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
            String firstName = learnerService.getUserByFirstName(learnerId);
            String lastName = learnerService.getUserByLastName(learnerId);
            String link = Utility.getSiteURL(httpServletRequest) + "/api/student/stud-OtpValidationPage?otp=" + otp;
            String email = learnerService.getUserByEmailAddress(learnerId);
            learnerService.sendEmail(email, link, otp, firstName, lastName);
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
    @GetMapping("/stud-otpVerification")
    public String verifyOTP(Model model, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                            Learner learner1) throws ServletException, IOException {
        String pass = httpServletRequest.getParameter("pass");


        Learner learner  = learnerService.getByOneTimePassword(pass);

        if (learner != null) {
            String firstname = learnerService.getFirstName(pass);
            String lastname = learnerService.getLastName(pass);
            String leanerId = learnerService.getlearnerId(pass);
            String email = learnerService.getLearnerEmail(pass);
            String faculty = learnerService.getFaculty(pass);
            String department = learnerService.getDepartment(pass);
            Integer yearOfStudy = learnerService.getYearOfStudy(pass);
            String institution = learnerService.getInstitution(pass);
            String campus =learnerService.getCampus(pass);

            //List<Instructor> instructor1 = instructorService.getDetails(pass);
            learner1 = new Learner();
            learner1.setFirstName(firstname);
            learner1.setLastName(lastname);
            learner1.setLearnerId(leanerId);
            learner1.setEmail(email);
            learner1.setFaculty(faculty);
            learner1.setDepartment(department);
            learner1.setYearOfStudy(yearOfStudy);
            learner1.setInstitution(institution);
            learner1.setCampus(campus);
            /* User Profile section */
            //set instructor object and send it to profile handler
            httpServletRequest.setAttribute("learner", learner1);

//
//            //forward the request to the profile page
//
            String url = "/api/student/profile";
            ServletContext context = httpServletRequest.getServletContext();
            RequestDispatcher requestDispatcher = context.getRequestDispatcher(url);
            requestDispatcher.forward(httpServletRequest, httpServletResponse);


            String firstName = learnerService.getFirstName(pass);
            String lastName= learnerService.getLastName(pass);
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
            learnerService.loginLearner(learner);
        } else {
            return "redirect:/api/student/stud-OtpValidation?error";
        }
        return "/student/stud-dashboard";
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

    // edit stud profile
    @GetMapping(value = {"/{learnerId}/edit"})
    public String showEditLecForm(Model model, @PathVariable String learnerId) throws UserNotFoundException {
        Optional<Learner> learner = null;
        //            learnerService.findById(learnerId).ifPresent(o -> model.addAttribute("learner", o) );

        learnerService.findById(learnerId);
        if (learner.isPresent()){
            model.addAttribute("learner", learner);
            model.addAttribute("formHeader", "Update details");
        }
        else
        {
            throw new UserNotFoundException("Learner with the given id does not exist");
        }

        return "/student/stud-profile-edit";
    }

    //update notification
    @PostMapping(value = {"/{learnerId}/edit"})
    public String updateContact(Model model,
                                @PathVariable String learnerId,
                                @ModelAttribute("learner") Learner learner) {
        try {
            learner.setLearnerId(learnerId);
            learnerService.updateLeaner(learner);
            return "redirect:/api/student/stud-profile?successedit";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Error editing instructor");

            return "/student/stud-profile-edit";
        }
    }



    /* ############### End of Login and registration controllers with forgot password fxns ################################## */


}
