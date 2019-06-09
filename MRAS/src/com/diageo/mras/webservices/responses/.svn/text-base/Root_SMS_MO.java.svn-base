package com.diageo.mras.webservices.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



@XmlRootElement(name = "SMS_MO")
@XmlType(propOrder = { "msIsdn", "orgAddress","message" ,"parameters"})

public class Root_SMS_MO {

	private String msIsdn,message;
	private int orgAddress;
	private SMSMO_Parameters parameters;
	
	

	@XmlElement(name = "MSISDN")
	public String getMsIsdn() {
		return msIsdn;
	}
	public void setMsIsdn(String msIsdn) {
		this.msIsdn = msIsdn;
	}
	

	@XmlElement(name = "MESSAGE")
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	

	@XmlElement(name = "ORIGINATING_ADDRESS")
	public int getOrgAddress() {
		return orgAddress;
	}
	public void setOrgAddress(int orgAddress) {
		this.orgAddress = orgAddress;
	}
	

	@XmlElement(name = "PARAMETERS")
	public SMSMO_Parameters getParameters() {
		return parameters;
	}
	public void setParameters(SMSMO_Parameters parameters) {
		this.parameters = parameters;
	}

	
	
	
}
