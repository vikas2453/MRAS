package com.diageo.mras.webservices.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ConsumerProfile")
@XmlType(propOrder = { "title", "firstName","lastName" ,"DOB","phone","promoCode","email"})
public class ConsumerProfile {

	private String promoCode,FirstName,LastName,DOB,Title  ;
	private NeoEmailBean email;
private NeoPhoneBean phone;
	
	public ConsumerProfile(){
		
	}
	
public ConsumerProfile(String FirstName,String LastName,String DOB,NeoPhoneBean phone,String promoCode,NeoEmailBean email){
		
	
		this.FirstName=FirstName;
		this.LastName=LastName;
		this.DOB=DOB;
		this.phone=phone;
		this.promoCode=promoCode;
		this.email=email;
		}
	
	
	
	
	
@XmlElement(name = "Phone")
	public NeoPhoneBean getPhone() {
	return phone;
}

public void setPhone(NeoPhoneBean phone) {
	this.phone = phone;
}

	@XmlElement(name = "Email")
	public NeoEmailBean getEmail() {
		return email;
	}

	public void setEmail(NeoEmailBean email) {
		this.email = email;
	}

	@XmlElement(name = "Title")
	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	@XmlElement(name = "FirstName")
	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	@XmlElement(name = "LastName")
	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}
	@XmlElement(name = "DOB")
	public String getDOB() {
		return DOB;
	}

	public void setDOB(String dOB) {
		DOB = dOB;
	}

	@XmlElement(name = "PromoCode")
	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}
}
