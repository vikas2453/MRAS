package com.diageo.mras.webservices.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "UserAccount")
@XmlType(propOrder = { "logincredentials","secretquestions" })

public class UserAccount {
	public UserAccount(){

	}
	
	
	
	private LoginCredentials logincredentials;
	private SecretQuestions secretquestions;
	
public UserAccount(LoginCredentials logincredentials,SecretQuestions secretquestions){
		
	this.logincredentials=logincredentials;
	this.secretquestions=secretquestions;
	
	}
	
	@XmlElement(name = "SecretQuestions")
	public SecretQuestions getSecretquestions() {
		return secretquestions;
	}

	public void setSecretquestions(SecretQuestions secretquestions) {
		this.secretquestions = secretquestions;
	}

	@XmlElement(name = "LoginCredentials")
	public LoginCredentials getLogincredentials() {
		return logincredentials;
	}

	public void setLogincredentials(LoginCredentials logincredentials) {
		this.logincredentials = logincredentials;
	}
}
