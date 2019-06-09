package com.diageo.mras.webservices.modals;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



@XmlRootElement(name = "PreferencesNew")
@XmlType(propOrder = { "questionCategory" })
public class PreferencesNew {

	
private ArrayList<QuestionCategoryNew> questionCategory;
	
	
	public PreferencesNew(){
		
	}
	
	public PreferencesNew(ArrayList<QuestionCategoryNew> questionCategory){
		this.questionCategory=questionCategory;
		
	}
	
	
	@XmlElement(name = "QuestionCategory")
	public ArrayList<QuestionCategoryNew> getQuestionCategory() {
		return questionCategory;
	}
	public void setQuestionCategory(ArrayList<QuestionCategoryNew> questionCategory) {
		this.questionCategory = questionCategory;
	}

	
}
