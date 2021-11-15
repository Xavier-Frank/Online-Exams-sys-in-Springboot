package com.xavi.exams.models;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Exams {

    @Id
    @Column(length = 10)
    private String examid;

    @NotNull
    private String examName;

    @Column(nullable = false, length = 70)
    private String institution;

    @NotNull
    @Column(name = "yearOfExamination")
    private Integer yearOfExamination;

    @NotNull
    @Column(name = "dateOfExamination")
    private Date dateOfExamination;

    @NotNull
    @Column(name = "duration")
    private Time duration;

    @Column(name = "created_on")
    private Date createdOn;

    //Relationships
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "staffId", referencedColumnName = "staffId")
    private Instructor instructor;


}
