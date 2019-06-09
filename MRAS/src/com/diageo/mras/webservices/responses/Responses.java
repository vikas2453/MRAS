package com.diageo.mras.webservices.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Responses")
@XmlType(propOrder = { "response"})

public class Responses {

	private Response response;

	@XmlElement(name = "Response")
	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}
	
	
}
