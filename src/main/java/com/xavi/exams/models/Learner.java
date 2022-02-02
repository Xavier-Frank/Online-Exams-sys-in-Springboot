package com.xavi.exams.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
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
    private String faculty;

    @Column(nullable = false, length = 70)
    private String department;

    @Column(nullable = false, length = 70)
    private String institution;
    
     @Column(nullable = false, length = 50)
    private String campus;
    
    
    @Column(nullable = true, length = 50)
    private String email;

    @Column(length = 50, insertable = true)
    private String oneTimePassword;


    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdOn;


    //Relationships
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "examId", referencedColumnName = "examId")
    private Exams exams;



}
