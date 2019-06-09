package com.diageo.mras.webservices.modals;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.diageo.mras.webservices.responses.QuestionAnswers;


@XmlRootElement(name = "QuestionCategoryNew")
@XmlType(propOrder = { "categoryID", "questionAnswers"})
public class QuestionCategoryNew {

	
	private int categoryID;

	private ArrayList<QuestionMultipleAnswers> questionAnswers;
		
		
	public QuestionCategoryNew(){
		
	}


	public QuestionCategoryNew(int categoryID,ArrayList<QuestionMultipleAnswers> questionAnswers){
		this.categoryID=categoryID;
		this.questionAnswers=questionAnswers;
		
	}

	@XmlElement(name = "CategoryID")
	public int getCategoryID() {
		return categoryID;
	}

	
	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	@XmlElement(name = "QuestionAnswers")
	public ArrayList<QuestionMultipleAnswers> getQuestionAnswers() {
		return questionAnswers;
	}


	public void setQuestionAnswers(
			ArrayList<QuestionMultipleAnswers> questionAnswers) {
		this.questionAnswers = questionAnswers;
	}
	
	
	
}
