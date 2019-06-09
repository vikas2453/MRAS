package com.diageo.mras.webservices.responses;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ViewConsumerInGroupResponse {
	String phoneNumber;
	int statusFlag;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(int statusFlag) {
		this.statusFlag = statusFlag;
	}

	public ViewConsumerInGroupResponse(Map.Entry<String, Integer> e) {

		this.statusFlag = e.getValue();
		this.phoneNumber = e.getKey();
	}

	public ViewConsumerInGroupResponse() {

	}

}
