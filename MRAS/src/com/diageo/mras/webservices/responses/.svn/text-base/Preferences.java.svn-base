package com.diageo.mras.webservices.responses;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@XmlRootElement(name = "Preferences")
@XmlType(propOrder = { "questionCategory" })
public class Preferences {
	private ArrayList<QuestionCategory> questionCategory;
	
	
	public Preferences(){
		
	}
	
	public Preferences(ArrayList<QuestionCategory> questionCategory){
		this.questionCategory=questionCategory;
		
	}
	
	
	@XmlElement(name = "QuestionCategory")
	public ArrayList<QuestionCategory> getQuestionCategory() {
		return questionCategory;
	}
	public void setQuestionCategory(ArrayList<QuestionCategory> questionCategory) {
		this.questionCategory = questionCategory;
	}


}
