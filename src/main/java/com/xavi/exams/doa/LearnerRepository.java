package com.xavi.exams.doa;

import com.xavi.exams.models.Instructor;
import com.xavi.exams.models.Learner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LearnerRepository extends JpaRepository <Learner, String> {
    @Query("SELECT l FROM Learner l WHERE l.email = ?1")
    public Learner findByEmail(String email);

    public Learner findByResetPasswordToken(String token);
}
