package com.diageo.mras.webservices.responses;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResponseMRAS {
	private String responseMessage;
	private int responseCode;

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String toString() {
		return "{\"responseMessage\":\"" + responseMessage + "\",\"responseCode\":\""
				+ responseCode+"\"}";
	}

}
