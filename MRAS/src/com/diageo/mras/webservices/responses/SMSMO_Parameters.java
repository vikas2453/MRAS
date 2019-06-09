package com.diageo.mras.webservices.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;




@XmlRootElement(name = "PARAMETERS")
@XmlType(propOrder = { "opId", "accountId","msgId" ,"opInfo","dCS","clas","rcvdServNo","keyWord","receivedTime"})
public class SMSMO_Parameters {

	
	private int opId,accountId,msgId,clas,rcvdServNo;
	private String dCS,keyWord;
	private Parameters_OpInfo opInfo;
	private Parameters_RcvdTime receivedTime;
	
	
	@XmlElement(name = "OPERATORID")
	public int getOpId() {
		return opId;
	}
	public void setOpId(int opId) {
		this.opId = opId;
	}
	
	@XmlElement(name = "ACCOUNTID")
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	
	@XmlElement(name = "MESSAGEID")
	public int getMsgId() {
		return msgId;
	}
	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}
	
	@XmlElement(name = "CLASS")
	public int getClas() {
		return clas;
	}
	public void setClas(int clas) {
		this.clas = clas;
	}
	
	
	@XmlElement(name = "RECEIVED_SERVICENUMBER")
	public int getRcvdServNo() {
		return rcvdServNo;
	}
	public void setRcvdServNo(int rcvdServNo) {
		this.rcvdServNo = rcvdServNo;
	}
	
	@XmlElement(name = "DCS")
	public String getdCS() {
		return dCS;
	}
	public void setdCS(String dCS) {
		this.dCS = dCS;
	}
	
	@XmlElement(name = "KEYWORD")
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	
	@XmlElement(name = "OPERATOR_INFORMATION")
	public Parameters_OpInfo getOpInfo() {
		return opInfo;
	}
	public void setOpInfo(Parameters_OpInfo opInfo) {
		this.opInfo = opInfo;
	}
	
	
	@XmlElement(name = "RECEIVEDTIME")
	public Parameters_RcvdTime getReceivedTime() {
		return receivedTime;
	}
	public void setReceivedTime(Parameters_RcvdTime receivedTime) {
		this.receivedTime = receivedTime;
	}
	
	
}
