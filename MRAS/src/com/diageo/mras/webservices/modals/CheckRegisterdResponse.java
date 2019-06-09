package com.diageo.mras.webservices.modals;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CheckRegisterdResponse {
	private String phoneno;
	private String Registered;

	public String getPhoneno() {
		return phoneno;
	}

	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}

	public String getRegistered() {
		return Registered;
	}

	public void setRegistered(String registered) {
		Registered = registered;
	}

}
