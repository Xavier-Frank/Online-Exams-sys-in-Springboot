package com.xavi.exams.models;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Admin {
    @Id
    @Column(length = 20)
    private String staffId;

    @NotNull
    @Column(nullable = false, columnDefinition = "varchar(750)", length = 20)
    private String password;

    @Column(name = "created_on")
    private Date createdOn;

    //Relationships


}
