package com.diageo.mras.webservices.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "phoneNumber", "phoneType", "modifyFlag" })
public class NeoPhoneBean {

	
	public NeoPhoneBean(){
		
	}
	public NeoPhoneBean(String PhoneNumber,String PhoneType,String ModifyFlag){
		
		this.PhoneNumber=PhoneNumber;
		this.PhoneType=PhoneType;
	
		this.ModifyFlag=ModifyFlag;
		
		
		
	}
	private String PhoneNumber, PhoneType, ModifyFlag;

	@XmlElement(name = "PhoneNumber")
	public String getPhoneNumber() {
		return PhoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber;
	}

	@XmlElement(name = "PhoneType")
	public String getPhoneType() {
		return PhoneType;
	}

	public void setPhoneType(String phoneType) {
		PhoneType = phoneType;
	}

	@XmlElement(name = "ModifyFlag")
	public String getModifyFlag() {
		return ModifyFlag;
	}

	public void setModifyFlag(String modifyFlag) {
		ModifyFlag = modifyFlag;
	}
}
