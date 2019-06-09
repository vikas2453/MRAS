package com.diageo.mras.webservices.responses;



import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.omg.Security.Public;

@XmlRootElement(name = "QuestionCategory")
@XmlType(propOrder = { "categoryID", "questionAnswers"})
public class QuestionCategory {
	

	
	private int categoryID;

private ArrayList<QuestionAnswers> questionAnswers;
	
	
public QuestionCategory(){
	
}


	public QuestionCategory(int categoryID,ArrayList<QuestionAnswers> questionAnswers){
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
	public ArrayList<QuestionAnswers> getQuestionAnswers() {
		return questionAnswers;
	}
	public void setQuestionAnswers(ArrayList<QuestionAnswers> questionAnswers) {
		this.questionAnswers = questionAnswers;

	
	

	

	}}
