package com.xavi.exams.services;


import com.xavi.exams.doa.QuestionRepo;
import com.xavi.exams.doa.ResultRepo;
import com.xavi.exams.models.Question;
import com.xavi.exams.models.QuestionForm;
import com.xavi.exams.models.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {
	
	@Autowired
	Question question;
	@Autowired
	QuestionForm qForm;
	@Autowired
	QuestionRepo qRepo;
	@Autowired
	Result result;
	@Autowired
	ResultRepo rRepo;
	
	public QuestionForm getQuestions() {
		List<Question> allQues = qRepo.findAll();
//		List<Question> qList = new ArrayList<Question>();

//		Random random = new Random();

//		for(int i=0; i<5; i++) {
//			int rand = random.nextInt(allQues.size());
//			allQues.remove(rand);
//		}

//		qList.add((Question) allQues);
		qForm.setQuestions(allQues);

		return qForm;
	}
	
	public int getResult(QuestionForm qForm) {
		int correct = 0;
		
		for(Question q: qForm.getQuestions())
			if(q.getAns() == q.getChose())
				correct++;
		
		return correct;
	}

	public void saveScore(Result result) {
		Result saveResult = new Result();
//		saveResult.setUsername(result.getUsername());
		saveResult.setTotalCorrect(result.getTotalCorrect());
		rRepo.save(saveResult);
	}
	
	public List<Result> getTopScore() {
		List<Result> sList = rRepo.findAll(Sort.by(Sort.Direction.DESC, "totalCorrect"));
		
		return sList;
	}

	//Add a question
	public Question saveQuestion(Question question) throws Exception {

//		Optional<Question> questionId = qRepo.findById(quesId);
//
//		if (questionId.isPresent()){
//
//			throw new Exception("Question Id already exists");
//		}
//		else{

			return qRepo.save(question);
//		}
	}

	public List<Question> questionList() {
		return (List<Question>) qRepo.findAll();
	}

	//delete assessment
    public void deleteById(Integer quesId) {

		rRepo.deleteById(quesId);
    }
}
