package com.diageo.mras.webservices.modals;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.diageo.mras.webservices.responses.Answer;



@XmlRootElement(name = "QuestionMultipleAnswers")
@XmlType(propOrder = { "questionID", "answer" })
public class QuestionMultipleAnswers {

	
	private int questionID;
	private List<Answer> answer;

	
	public QuestionMultipleAnswers(){
		
	}
	
	public QuestionMultipleAnswers(int questionID, List<Answer> answer) {
		this.questionID = questionID;
		this.answer = answer;

	}

	@XmlElement(name = "QuestionID")
	public int getQuestionID() {
		return questionID;
	}

	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}

	@XmlElement(name = "Answer")
	public List<Answer> getAnswer() {
		return answer;
	}

	public void setAnswer(List<Answer> answer) {
		this.answer = answer;
	}

	
}
