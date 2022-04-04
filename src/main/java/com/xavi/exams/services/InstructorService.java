package com.xavi.exams.services;

import com.xavi.exams.doa.InstructorRepository;
//import com.xavi.exams.doa.ResultsRepository;
import com.xavi.exams.models.Instructor;
//import com.xavi.exams.models.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@Service
public class InstructorService {
//    @Autowired
//    private ResultsRepository resultsRepository;

    @Autowired
    private InstructorRepository instructorRepository;



    @Autowired
    private JavaMailSender mailSender;

    //register an instructor
    public Instructor registerInstructor(Instructor instructor, String staffId) throws UserNotFoundException {
        String email = instructorRepository.findByEmail(staffId);

        if (email == null) {
            return instructorRepository.save(instructor);
        } else {
            throw new UserNotFoundException("Email already exists");
        }


    }

    // validate staffId during login

    public void validateStaffId(String otp,String staffId) throws ClassNotFoundException {
        //get the staff id
        Instructor instructor = instructorRepository.getById(staffId);
        if (instructor != null){
            //get the string email
            String email = instructorRepository.findByEmail(staffId);
            instructor.setOneTimePassword(otp);

            instructorRepository.save(instructor);

        } else {
            throw new ClassNotFoundException("No user with such id exist" + staffId);
        }

    }
    //    //send the email
    public void sendEmail(String email,String link, String otp) throws MessagingException, UnsupportedEncodingException {
//        String email = instructorRepository.findByEmail(staffId);
        MimeMessage message = mailSender.createMimeMessage();
          MimeMessageHelper helper = new MimeMessageHelper(message);

          helper.setFrom("supportcenter@gmail.com", "OTP Password Support");
          helper.setTo(String.valueOf(email));

          String subject = "OTP Password Generated";
          String content = "<p>Dear user,</p>"
                  + "<p>Your request to generate an OTP (one time password) is successful.</p>"
                  + "<p> Copy this code to complete login</p>" + otp
                  + "<p><a href=\"" + link + "\">Complete Login</a></p>"
                  + "<br>"
                  + "<p>Ignore this email if you do not make this request";
          helper.setSubject(subject);
          helper.setText(content, true);
          mailSender.send(message);

    }

    // get the saved otp in the instructor db

        public Instructor getByOneTimePassword(String otp){
        return instructorRepository.findByOneTimePassword(otp);
    }

    //Process OTP for login
    public void loginInstructor(Instructor instructor){
        //set OTP to null
        instructor.setOneTimePassword(null);
        instructorRepository.save(instructor);

    }

    //get user by email address
    public String getUserByEmailAddress(String staffId){
        return instructorRepository.findByEmail(staffId);
    }

    //get user by id for edit
    public Instructor get(String staffId) throws UserNotFoundException {
        Optional<Instructor> getInstructor = instructorRepository.findById(staffId);
        if (getInstructor.isPresent()){
            getInstructor.get();

        } throw new UserNotFoundException("Could not load user");

    }





//
//    //View results
//    public List<Results> getAllResults(){
//        return resultsRepository.findAll();
//    }

    //update instructor
    public Instructor updateInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    //Create exams





}
