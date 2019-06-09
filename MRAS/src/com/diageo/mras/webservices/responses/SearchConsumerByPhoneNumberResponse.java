package com.diageo.mras.webservices.responses;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchConsumerByPhoneNumberResponse {

	@XmlElement
	private Collection<PhoneNumberConsumerResponse> phoneNumberConsumerList;

	public Collection<PhoneNumberConsumerResponse> getPhoneNumberConsumerList() {
		return phoneNumberConsumerList;
	}

	public void setPhoneNumberConsumerList(
			Collection<PhoneNumberConsumerResponse> phoneNumberConsumerList) {
		this.phoneNumberConsumerList = phoneNumberConsumerList;
	}

}
