package com.diageo.mras.webservices.modals;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CampaignsModal {
	@XmlElement(name = "campaignList")
	private List<CampaignObject> campaignList;

	public List<CampaignObject> getCampaignList() {
		return campaignList;
	}

	public void setCampaignList(List<CampaignObject> campaignList) {
		this.campaignList = campaignList;
	}

}
