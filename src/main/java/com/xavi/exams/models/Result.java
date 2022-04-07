package com.xavi.exams.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Component
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String learnerId;
//	@Column(length = 20)
//	private String firstName;
//	@Column(length = 20)
//	private String lastName;
	private int totalCorrect = 0;


}
