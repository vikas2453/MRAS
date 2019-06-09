package com.diageo.mras.webservices.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
@XmlType(propOrder = { "emailId", "emailCategory","isDefaultFlag","modifyFlag"})
public class NeoEmailBean {
	
	
public NeoEmailBean(){
	
}

private String EmailId,EmailCategory,IsDefaultFlag,ModifyFlag;

public NeoEmailBean(String EmailId,String EmailCategory,String IsDefaultFlag,String ModifyFlag){
	
	this.EmailId=EmailId;
	this.EmailCategory=EmailCategory;
	this.IsDefaultFlag=IsDefaultFlag;
	this.ModifyFlag=ModifyFlag;
	
	
	
}
@XmlElement(name = "IsDefaultFlag")
public String getIsDefaultFlag() {
	return IsDefaultFlag;
}

public void setIsDefaultFlag(String isDefaultFlag) {
	IsDefaultFlag = isDefaultFlag;
}
@XmlElement(name = "ModifyFlag")
public String getModifyFlag() {
	return ModifyFlag;
}

public void setModifyFlag(String modifyFlag) {
	ModifyFlag = modifyFlag;
}
@XmlElement(name = "EmailCategory")
public String getEmailCategory() {
	return EmailCategory;
}

public void setEmailCategory(String emailCategory) {
	EmailCategory = emailCategory;
}

@XmlElement(name = "EmailId")

public String getEmailId() {
	return EmailId;
}

public void setEmailId(String emailId) {
	EmailId = emailId;
}
}
