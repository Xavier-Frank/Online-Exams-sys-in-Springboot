package com.xavi.exams.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Exams {

    @Id
    @Column(length = 10)
    private String assessmentId;

//    @Column(nullable = false, length = 70)
//    private String institution;

    @Column(nullable = false, length = 255)
    private String assessmentName;

    @Column(nullable = false, length = 70)
    private String department;


    @NotNull
    @Column(name = "classOfExamination")
    private Integer classOfExamination;

//    @NotNull
//    @Column(name = "dateOfExamination")
//    @DateTimeFormat(pattern = "MM.dd.yyyy")
//    private Date dateOfExamination;

    @Column(name = "dateOfExamination")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dateOfExamination;

    @NotNull
    @Column(name = "duration")
    private Time duration;

//    @Column(name = "score")
//    private Double score;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private Timestamp createdOn;

    //Relationships
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "staffId", referencedColumnName = "staffId")
    private Instructor instructor;


}
