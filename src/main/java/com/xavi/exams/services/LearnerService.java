package com.xavi.exams.services;

import com.xavi.exams.doa.LearnerRepository;
import com.xavi.exams.models.Learner;
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
public class LearnerService {
    @Autowired
    private LearnerRepository learnerRepository;
    @Autowired
    private JavaMailSender mailSender;

    //register a leaner
    public Learner registerLeaner(Learner learner, String learnerId) throws UserNotFoundException {
        String email = learnerRepository.findByEmail(learnerId);
        if (email == null) {
            return learnerRepository.save(learner);
        }
        else {
            throw new UserNotFoundException("Email already exists");
        }

    }

    // validate learnerId during login

    public void validateLearnerId(String otp,String learnerId) throws ClassNotFoundException {
        //get the learner id
        Learner learner  = learnerRepository.getById(learnerId);
        if (learner != null){
            //get the string email
            String email = learnerRepository.findByEmail(learnerId);

            String firstName = learnerRepository.findByFirstName(learnerId);
            String lastName = learnerRepository.findByLastName(learnerId);

            learner.setOneTimePassword(otp);

            learnerRepository.save(learner);

        } else {
            throw new ClassNotFoundException("No user with such id exist: " + " " + learnerId);
        }

    }
    //    //send the email
    public void sendEmail(String email,String link, String otp, String firstName, String lastName) throws MessagingException, UnsupportedEncodingException {
//        String email = instructorRepository.findByEmail(staffId);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("supportcenter@gmail.com", "OTP Password Support");
        helper.setTo(String.valueOf(email));

        String subject = "OTP Password Generated";
        String content = "<p>Dear user:" + firstName + " " + lastName + " </p>"
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

    public Learner getByOneTimePassword(String otp){
        return learnerRepository.findByOneTimePassword(otp);
    }

    public String getFirstName(String pass){
        return learnerRepository.findFirstName(pass);
    }
    public String getLastName(String pass){
        return learnerRepository.findLastName(pass);
    }

    //Process OTP for login
    public void loginLearner(Learner learner){
        //set OTP to null
        learner.setOneTimePassword(null);
        learnerRepository.save(learner);

    }

    //get user by email address
    public String getUserByEmailAddress(String learnerId){
        return learnerRepository.findByEmail(learnerId);
    }

    //get user by first name
    public String getUserByFirstName(String learnerId) {
        return learnerRepository.findByFirstName(learnerId);
    }

    //get user by last name
    public String getUserByLastName(String learnerId) {
        return learnerRepository.findByLastName(learnerId);
    }

    public List<Learner> learnerList() {
        return learnerRepository.findAll();
    }
    //View results

    //Take Assessment

    //contact lecturer

    //Search for a learner
    public List<Learner> search(String keyword) throws UserNotFoundException {
        List<Learner> serchedLearner = learnerRepository.searchLearner(keyword);

        if (serchedLearner.isEmpty()){
            throw new UserNotFoundException("No Learner with the key word exists");
        }
        return learnerRepository.searchLearner(keyword);
    }

    //edit a leaner
    public Learner get(String learnerId) throws UserNotFoundException {
        Optional<Learner> getLearner = learnerRepository.findById(learnerId);
        if (getLearner.isPresent()){
            getLearner.get();
        } throw new UserNotFoundException("Learner not Found");
    }

    public Learner updateLeaner(Learner learner) {
        return learnerRepository.save(learner);
    }
}
