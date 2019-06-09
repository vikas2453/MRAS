package com.diageo.mras.webservices.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "OPERATOR_INFORMATION")
@XmlType(propOrder = { "opStandard", "opCode"})

public class Parameters_OpInfo {

	private String opStandard;
	private OpInfo_OpCode opCode;
	
	
	@XmlElement(name = "OPERATOR_STANDARD")
	public String getOpStandard() {
		return opStandard;
	}
	public void setOpStandard(String opStandard) {
		this.opStandard = opStandard;
	}
	
	@XmlElement(name = "OPERATOR_CODE")
	public OpInfo_OpCode getOpCode() {
		return opCode;
	}
	public void setOpCode(OpInfo_OpCode opCode) {
		this.opCode = opCode;
	}
	
	
	
	
	
}
