package com.xavi.exams.controller;

import com.xavi.exams.doa.InstructorRepository;
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
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

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
    private InstructorRepository instructorRepository;

    @Autowired
    private ExamService examService;
    private Object HttpServletRequest;


    /* ======================= Dashboard Navigation =================================== */
    /* ========================= Navigation controllers ====================================*/

    //  Return Lecturers Dashboard
    @GetMapping("/lec-dashboard")
    public String instructorDashboard(){


        return "/instructor/lec-dashboard";
    }

    // Return Lecturers Profile
    @GetMapping("/lec-profile")
    public String instructorProfile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Model model,
                                    String pass) throws ServletException, UserNotFoundException, IOException {


//        String url = "/api/instructor/lec-otpVerification";
//        ServletContext context = httpServletRequest.getServletContext();
//        RequestDispatcher requestDispatcher = context.getRequestDispatcher(url);
//        requestDispatcher.include(httpServletRequest, httpServletResponse);
        //include content of another servlet

        Instructor instructor = (Instructor) httpServletRequest.getAttribute("instructor");



        String firstName = instructor.getFirstName();
        String lastName= instructor.getLastName();
        String staffId = instructor.getStaffId();
        String email = instructor.getEmail();
        Timestamp createdOn = instructor.getCreatedOn();

        instructor.setFirstName(firstName);
        instructor.setLastName(lastName);
        instructor.setStaffId(staffId);
        instructor.setEmail(email);
        instructor.setCreatedOn(createdOn);
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);

        instructorService.loginInstructor(instructor);

        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);

        System.out.println("The name of instructor received is" + instructor.getFirstName());


//        verifyOTP(model, httpServletRequest, httpServletResponse,instructor, pass);
//        Optional<Instructor> instructor = instructorService.findById(pass);
         model.addAttribute("instructor", instructor);
         instructorRepository.save(instructor);

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
    public String lecResults(Model model) {
        List<Learner> list_of_learners = learnerService.learnerList();
        model.addAttribute("learner", list_of_learners);
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
            String email = instructorService.getUserByEmailAddress(staffId);
            String firstName = instructorService.getUserByFirstName(staffId);
            String lastName = instructorService.getUserByLastName(staffId);
            String link = Utility.getSiteURL(httpServletRequest) + "/api/instructor/lec-OtpValidationPage?otp=" + otp;
            instructorService.sendEmail(email, link, otp, firstName, lastName);
            modelAndView.setViewName("redirect:/api/instructor/lec-loginform?success");
        } catch (ClassNotFoundException | MessagingException | UnsupportedEncodingException e) {
            modelAndView.setViewName("redirect:/api/instructor/lec-loginform?error");

        }
        return modelAndView;
    }

    //Get the login form
    @GetMapping("/lec-OtpValidationPage")
    public ModelAndView showResetPasswordForm(@Param(value = "otp") String otp,
                                              ModelAndView modelAndView) {
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
    @GetMapping("/lec-otpVerification")
    public String verifyOTP(Model model, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                            Instructor instructor1, String pass) throws ServletException, IOException, UserNotFoundException {
        pass = httpServletRequest.getParameter("pass");


        Instructor instructor = instructorService.getByOneTimePassword(pass);
//        String otp = instructor.getOneTimePassword();

        if (instructor != null) {

            String firstname = instructorService.getFirstName(pass);
            String lastname = instructorService.getLastName(pass);
            String staffId = instructorService.getStaffId(pass);
            String email = instructorService.getEmail(pass);

            //List<Instructor> instructor1 = instructorService.getDetails(pass);
            instructor1 = new Instructor();
            instructor1.setStaffId(staffId);
            instructor1.setFirstName(firstname);
            instructor1.setLastName(lastname);
            instructor1.setEmail(email);
            
            /* User Profile section */
            //set instructor object and send it to profile handler
            httpServletRequest.setAttribute("instructor", instructor1);

//
//            //forward the request to the profile page
//
            String url = "/api/instructor/lec-profile";
            ServletContext context = httpServletRequest.getServletContext();
            RequestDispatcher requestDispatcher = context.getRequestDispatcher(url);
            requestDispatcher.forward(httpServletRequest, httpServletResponse);
        } else {
            return "redirect:/api/instructor/lec-loginform?successM";
        }
        return "/instructor/lec-dashboard";
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

    //edit lec profile
    @GetMapping(value = {"/{staffId}/edit"})
    public String showEditLecForm(Model model, @PathVariable String staffId) {
        Optional<Instructor> instructor = null;
        try {
            examService.findById(staffId).ifPresent(o -> model.addAttribute("instructor", o) );
//            model.addAttribute("pageTitle", "Edit instructor profile");
            model.addAttribute("formHeader", "Update details");

        } catch (UserNotFoundException ex) {
            model.addAttribute("errorMessage", "Instructor not found");
            return "/instructor/lec-profile";
        }
        return "/instructor/lec-profile-edit";
    }

    //update notification
    @PostMapping(value = {"/{staffId}/edit"})
    public String updateContact(Model model,
                                @PathVariable String staffId,
                                @ModelAttribute("instructor") Instructor instructor) {
        try {
            instructor.setStaffId(staffId);
            instructorService.updateInstructor(instructor);
            return "redirect:/api/instructor/lec-profile?successedit";
        } catch (Exception ex) {
            // log exception first,
            // then show error
//            String errorMessage = ex.getMessage();
//            logger.error(errorMessage);
            model.addAttribute("errorMessage", "Error editing instructor");

            return "/instructor/lec-profile-edit";
        }
    }


}


    /* ######################################### End of Instructor Operations #############################*/





















