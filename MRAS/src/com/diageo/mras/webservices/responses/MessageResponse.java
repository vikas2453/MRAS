package com.diageo.mras.webservices.responses;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.diageo.mras.webservices.modals.Message;

@XmlRootElement(name = "MessageList")
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageResponse {
	
	
	List<Message> messagelist;

	public List<Message> getMessagelist() {
		return messagelist;
	}

	/*public void setMessagelist(List<Message> messagelist) {
		this.messagelist = messagelist;
	}*/

	public void setMessagelist(List<Message> consumerMessageList) {
		this.messagelist = consumerMessageList;
		
	}
}
