package com.diageo.mras.webservices.modals;

public class TextUrl {

	private String URL;
	private boolean SMSPhone;
	private boolean PhoneEncryption;
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public boolean isSMSPhone() {
		return SMSPhone;
	}
	public void setSMSPhone(boolean sMSPhone) {
		SMSPhone = sMSPhone;
	}
	public boolean isPhoneEncryption() {
		return PhoneEncryption;
	}
	public void setPhoneEncryption(boolean phoneEncryption) {
		PhoneEncryption = phoneEncryption;
	}
	
	
	
}
