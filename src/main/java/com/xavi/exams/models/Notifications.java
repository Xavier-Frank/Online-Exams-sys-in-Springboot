package com.xavi.exams.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

    @Column
    private String content;

//    @Column(columnDefinition = "DEFAULT = CURRENT_TIMESTAMP")
//    @Temporal(TemporalType.TIMESTAMP)
//    @CreatedDate
//    private Date createdOn;

    @CreationTimestamp
    @Column(name = "createdOn", updatable = false)
    private Timestamp createdOn;


}
