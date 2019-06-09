package com.diageo.mras.webservices.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.omg.Security.Public;

@XmlRootElement(name = "QuestionAnswers")
@XmlType(propOrder = { "questionID", "answer" })
public class QuestionAnswers {
	private int questionID;
	private Answer answer;

	
	public QuestionAnswers(){
		
	}
	
	public QuestionAnswers(int questionID, Answer answer) {
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
	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

}
