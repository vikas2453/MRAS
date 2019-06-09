package com.diageo.mras.webservices.modals;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CampaignObject {

	private int campaignId;
	private String campaignName;
	private int maxRewardsAllotted;
	private String promoCode;
	private Date startDate;
	private Date endDate;
	private String specialCampaign;
	private String specialCampaignText;

	public String getSpecialCampaign() {
		return specialCampaign;
	}

	public void setSpecialCampaign(String specialCampaign) {
		this.specialCampaign = specialCampaign;
	}

	public String getSpecialCampaignText() {
		return specialCampaignText;
	}

	public void setSpecialCampaignText(String specialCampaignText) {
		this.specialCampaignText = specialCampaignText;
	}

	public int getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public int getMaxRewardsAllotted() {
		return maxRewardsAllotted;
	}

	public void setMaxRewardsAllotted(int maxRewardsAllotted) {
		this.maxRewardsAllotted = maxRewardsAllotted;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
