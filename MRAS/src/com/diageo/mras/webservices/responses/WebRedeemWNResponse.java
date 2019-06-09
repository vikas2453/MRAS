package com.diageo.mras.webservices.responses;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WebRedeemWNResponse {
	
	
	
	
	public WebRedeemWNResponse(){
		
	}
	
	public WebRedeemWNResponse(String offerName,String outletName,String outletAddress,String outletZipCode,String outletTown,String productName ){
	
		this.offerName=offerName;
		
		this.outletName=outletName;
		this.outletAddress=outletAddress;
		this.outletZipCode=outletZipCode;
		this.outletTown=outletTown;
		this.productName=productName;
	}
	
	private String offerName;
	public String getOfferName() {
		return offerName;
	}
	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}
	public String getOutletName() {
		return outletName;
	}
	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}
	public String getOutletAddress() {
		return outletAddress;
	}
	public void setOutletAddress(String outletAddress) {
		this.outletAddress = outletAddress;
	}
	public String getOutletZipCode() {
		return outletZipCode;
	}
	public void setOutletZipCode(String outletZipCode) {
		this.outletZipCode = outletZipCode;
	}
	public String getOutletTown() {
		return outletTown;
	}
	public void setOutletTown(String outletTown) {
		this.outletTown = outletTown;
	}
	private String outletName;
	
	private String productName;
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	private String outletAddress;
	private String outletZipCode;	
	private String outletTown;
}
