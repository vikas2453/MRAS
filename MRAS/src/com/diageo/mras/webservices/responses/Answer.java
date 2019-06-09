package com.diageo.mras.webservices.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.omg.Security.Public;

@XmlRootElement(name = "Answer")
@XmlType(propOrder = { "optionID", "modifyFlag", "brandID",
		"communicationChannel", "answerText" })
public class Answer {
	private String optionID;
	private String modifyFlag;
	private String answerText;
	private String brandID;
	private String CommunicationChannel;

	public Answer(){
		
	}
	
	
	public Answer(String optionID, String modifyFlag, String answerText,
			String brandID, String CommunicationChannel) {
		this.optionID = optionID;
		this.modifyFlag = modifyFlag;
		this.answerText = answerText;
		this.brandID = brandID;
		this.CommunicationChannel = CommunicationChannel;

	}

	@XmlElement(name = "CommunicationChannel")
	public String getCommunicationChannel() {
		return CommunicationChannel;
	}

	public void setCommunicationChannel(String communicationChannel) {
		CommunicationChannel = communicationChannel;
	}

	@XmlElement(name = "OptionID")
	public String getOptionID() {
		return optionID;
	}

	@XmlElement(name = "BrandID")
	public String getBrandID() {
		return brandID;
	}

	public void setBrandID(String brandID) {
		this.brandID = brandID;
	}

	public void setOptionID(String optionID) {
		this.optionID = optionID;
	}

	@XmlElement(name = "ModifyFlag")
	public String getModifyFlag() {
		return modifyFlag;
	}

	public void setModifyFlag(String modifyFlag) {
		this.modifyFlag = modifyFlag;
	}

	@XmlElement(name = "AnswerText")
	public String getAnswerText() {
		return answerText;
	}

	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

}
