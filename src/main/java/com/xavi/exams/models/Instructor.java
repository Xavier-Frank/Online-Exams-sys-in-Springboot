package com.xavi.exams.models;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Instructor {
    @Id
    @Column(length = 20)
    private String staffId;

    @Column(nullable = false, length = 20)
    private String firstName;

    @Column(nullable = false, length = 20)
    private String lastName;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(length = 50, insertable = true)
    private String oneTimePassword;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdOn;


//    @Column(nullable = true, length = 13)
//    private String phoneNumber;

//    @Column(nullable = false, columnDefinition = "Varchar (900)")
//    private String password;



//    // for forgotten password
//    private String resetPasswordToken;


    //Relationships
    @ManyToOne
    @JoinColumn(name = "examId", referencedColumnName = "examId")
    private Exams exams;






}
