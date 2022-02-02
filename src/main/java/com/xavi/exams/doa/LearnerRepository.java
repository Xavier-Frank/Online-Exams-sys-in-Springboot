package com.xavi.exams.doa;

import com.xavi.exams.models.Instructor;
import com.xavi.exams.models.Learner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LearnerRepository extends JpaRepository <Learner, String> {
//    @Query("SELECT l FROM Learner l WHERE l.email = ?1")
//    public Learner findByEmail(String email);
    @Query("SELECT email FROM Learner l WHERE l.learnerId = :learnerId")
    public String findByEmail(@Param("learnerId") String learnerId);

    public Learner findByOneTimePassword(String otp);


//    @Query("SELECT u FROM Learner u WHERE u.learnerId = ?1")
//    public Learner findByLearnerId(String leanerId);

//    public Learner findByResetPasswordToken(String token);
}
