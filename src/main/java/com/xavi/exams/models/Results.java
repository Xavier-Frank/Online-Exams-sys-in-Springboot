//package com.xavi.exams.models;
//
//import com.sun.istack.NotNull;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.sql.Date;
//
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@Builder
//public class Results {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    @NotNull
//    private Integer score;
//
//    @NotNull
//    private Character grade;
//
//    @Column(name = "created_on")
//    private Date createdOn;
//
//    //Relationships
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "assessmentId", referencedColumnName = "assessmentId")
//    private Exams exams;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "Registration_number", referencedColumnName = "learnerId")
//    private Learner learner;
//
//
//
//
//}
