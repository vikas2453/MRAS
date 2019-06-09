package com.diageo.mras.webservices.modals;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BrandListModal {

	
	private String brandId;
	private String commChannel;
	private String optionId;
	
	
	
	public String getOptionId() {
		return optionId;
	}
	public void setOptionId(String optionId) {
		this.optionId = optionId;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getCommChannel() {
		return commChannel;
	}
	public void setCommChannel(String commChannel) {
		this.commChannel = commChannel;
	}
	
	public String toString() {
		return "brandId: " + brandId + " commChannel :"
				+ commChannel;
	}
	
}
