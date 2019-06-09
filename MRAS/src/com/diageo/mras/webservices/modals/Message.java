package com.diageo.mras.webservices.modals;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Message {

	private String ContentEnglish;
	private String ContentRegional;
	private String MessageType;
	private int offerId;
	private int NotificationId;

	public int getNotificationId() {
		return NotificationId;
	}

	public void setNotificationId(int notificationId) {
		NotificationId = notificationId;
	}

	public String getMessageType() {
		return MessageType;
	}

	public void setMessageType(String messageType) {
		MessageType = messageType;
	}

	public String getContentEnglish() {
		return ContentEnglish;
	}

	public void setContentEnglish(String contentEnglish) {
		ContentEnglish = contentEnglish;
	}

	public String getContentRegional() {
		return ContentRegional;
	}

	public void setContentRegional(String contentRegional) {
		ContentRegional = contentRegional;
	}

	public int getOfferId() {
		return offerId;
	}

	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}



}
