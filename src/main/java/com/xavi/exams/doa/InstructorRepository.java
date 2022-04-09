package com.xavi.exams.doa;

import com.xavi.exams.models.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.lang.instrument.Instrumentation;
import java.util.List;

@Repository
public interface InstructorRepository extends JpaRepository <Instructor, String> {
//    @Query("SELECT i FROM Instructor i WHERE i.email = ?1")
//    public Instructor findByEmail(String email);

    @Query("SELECT email FROM Instructor i WHERE i.staffId = :staffId")
    public String findByEmail(@Param("staffId") String staffId);

    @Query("SELECT firstName FROM Instructor i WHERE i.staffId = :staffId")
    public String findByFirstName(@Param("staffId") String staff);

    @Query("SELECT lastName FROM Instructor i WHERE i.staffId = :staffId")
    public String findByLastName(@Param("staffId") String staff);

    public Instructor findByOneTimePassword(String otp);

    @Query("SELECT firstName FROM Instructor i WHERE i.oneTimePassword = :pass")
    public String findFirstName(String pass);

    @Query("SELECT lastName FROM Instructor i WHERE i.oneTimePassword = :pass")
    public String findLastName(String pass);

    @Query("SELECT email FROM Instructor i WHERE i.oneTimePassword = :pass")
    public String findEmail(String pass);

    @Query("SELECT createdOn FROM Instructor i WHERE i.oneTimePassword = :pass")
    public String findCreatedOn(String pass);

    @Query("SELECT staffId FROM Instructor i WHERE i.oneTimePassword = :pass")
    String findStaffId(String pass);


//    public Instructor findByResetPasswordToken(String token);

    //get user details
//    @Query(value = "SELECT staffId, firstName, lastName, email FROM Instructor i WHERE i.oneTimePassword = :pass", nativeQuery = true)
//    List<Instructor> getInstructorDetails(@Param("pass") String pass);

}
