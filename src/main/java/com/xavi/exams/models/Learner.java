package com.xavi.exams.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Learner {
    @Id
    @Column(length = 20)
    private String learnerId;

    @Column(nullable = false, length = 20)
    private String firstName;

    @Column(nullable = false, length = 20)
    private String lastName;

    @Column(nullable = false,length = 1)
    private Integer yearOfStudy;

    @Column(nullable = false, length = 70)
    private String institution;
    
     @Column(nullable = false, length = 50)
    private String campus;
    
    
    @Column(nullable = true, length = 50)
    private String email;

//    @Column(nullable = true, length = 13)
//    private String phoneNumber;

    @Column(nullable = false, length = 20)
    private String password;

    //Relationships
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "examId", referencedColumnName = "examId")
    private Exams exams;



}
