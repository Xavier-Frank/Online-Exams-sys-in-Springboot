package com.xavi.exams.doa;

import com.xavi.exams.models.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepository extends JpaRepository <Instructor, String> {
    @Query("SELECT i FROM Instructor i WHERE i.email = ?1")
    public Instructor findByEmail(String email);

    public Instructor findByResetPasswordToken(String token);

}
