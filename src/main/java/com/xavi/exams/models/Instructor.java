package com.xavi.exams.models;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
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

//    @Column(nullable = true, length = 13)
//    private String phoneNumber;

    @Column(nullable = false, length = 20)
    private String password;

    @Column(name = "created_on")
    private Date createdOn;

    //Relationships
    @ManyToOne
    @JoinColumn(name = "examId", referencedColumnName = "examId")
    private Exams exams;






}
