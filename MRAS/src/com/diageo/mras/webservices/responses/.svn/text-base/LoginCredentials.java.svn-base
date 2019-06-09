package com.diageo.mras.webservices.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UserAccount")
public class LoginCredentials {
	private String LoginName, Password;

	public LoginCredentials(){
		
	}
	
	
	public LoginCredentials(String LoginName, String Password) {
		this.LoginName = LoginName;
		this.Password = Password;

	}

	@XmlElement(name = "LoginName")
	public String getLoginName() {
		return LoginName;
	}

	public void setLoginName(String loginName) {
		LoginName = loginName;
	}

	@XmlElement(name = "Password")
	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}
}
