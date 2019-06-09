package com.diageo.mras.webservices.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class Response {
String ResponseCode;
String ResponseMessage;

@XmlElement(name = "ResponseCode")
public String getResponseCode() {
	return ResponseCode;
}
public void setResponseCode(String responseCode) {
	ResponseCode = responseCode;
}
@XmlElement(name = "ResponseMessage")
public String getResponseMessage() {
	return ResponseMessage;
}
public void setResponseMessage(String responseMessage) {
	ResponseMessage = responseMessage;
}

}
