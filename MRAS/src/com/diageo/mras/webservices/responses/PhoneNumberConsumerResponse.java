package com.diageo.mras.webservices.responses;

import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
// @XmlAccessorType(XmlAccessType.FIELD)
public class PhoneNumberConsumerResponse {
	private String phoneNumber;
	private Integer consumerId;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(Integer consumerId) {
		this.consumerId = consumerId;
	}

	public PhoneNumberConsumerResponse(Map.Entry<String, Integer> e) {

		this.consumerId = e.getValue();
		this.phoneNumber = e.getKey();
	}

	public PhoneNumberConsumerResponse() {

	}
}
