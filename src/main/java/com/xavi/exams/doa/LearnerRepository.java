package com.xavi.exams.doa;

import com.xavi.exams.models.Learner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearnerRepository extends JpaRepository <Learner, String> {
//    @Query("SELECT l FROM Learner l WHERE l.email = ?1")
//    public Learner findByEmail(String email);
    @Query("SELECT email FROM Learner l WHERE l.learnerId = :learnerId")
    public String findByEmail(@Param("learnerId") String learnerId);

    public Learner findByOneTimePassword(String otp);


    @Query(value = "SELECT * FROM learner WHERE" + " " +
            "MATCH (learner_id, first_name, last_name, email, department, faculty)" + " "
            + "AGAINST (?1)", nativeQuery = true)
    public List<Learner> searchLearner(String keyword);

    @Query("SELECT firstName FROM Learner i WHERE i.learnerId = :learnerId")
    public String findByFirstName(@Param("learnerId") String learnerId);

    @Query("SELECT lastName FROM Learner i WHERE i.learnerId = :learnerId")
    public String findByLastName(@Param("learnerId") String learnerId);

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Query("SELECT firstName FROM Learner i WHERE i.oneTimePassword = :pass")
    public String findFirstName(String pass);

    @Query("SELECT lastName FROM Learner i WHERE i.oneTimePassword = :pass")
    public String findLastName(String pass);

    @Query("SELECT learnerId FROM Learner i WHERE i.oneTimePassword = :pass")
    String findLearnerId(String pass);

    @Query("SELECT email FROM Learner i WHERE i.oneTimePassword = :pass")
    String findEmail(String pass);

    @Query("SELECT yearOfStudy FROM Learner i WHERE i.oneTimePassword = :pass")
    Integer findYearOfStudy(String pass);

    @Query("SELECT faculty FROM Learner i WHERE i.oneTimePassword = :pass")
    String findFaculty(String pass);

    @Query("SELECT department FROM Learner i WHERE i.oneTimePassword = :pass")
    String findDepartment(String pass);

    @Query("SELECT institution FROM Learner i WHERE i.oneTimePassword = :pass")
    String findInstitution(String pass);

    @Query("SELECT campus FROM Learner i WHERE i.oneTimePassword = :pass")
    String getCampus(String pass);

//    @Query("UPDATE learner l set results = :r WHERE l.learnerId = A15/09420/17")
//    void saveScore(Integer r);
}
