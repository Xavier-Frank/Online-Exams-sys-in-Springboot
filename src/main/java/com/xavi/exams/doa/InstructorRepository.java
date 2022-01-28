package com.xavi.exams.doa;

import com.xavi.exams.models.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepository extends JpaRepository <Instructor, String> {
//    @Query("SELECT i FROM Instructor i WHERE i.email = ?1")
//    public Instructor findByEmail(String email);

    @Query("SELECT email FROM Instructor i WHERE i.staffId = :staffId")
    public String findByEmail(@Param("staffId") String staffId);

    public Instructor findByOneTimePassword(String otp);
//
//    public Instructor findByResetPasswordToken(String token);

}
