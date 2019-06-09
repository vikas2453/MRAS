package com.diageo.mras.webservices.modals;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is a bean class for the Reward entity.
 * 
 * @author Infosys Limited
 * @version 1.0
 */

@XmlRootElement
public class Reward {
	
	
	List<Integer> offerid;
	
	
	
	private boolean isRedeemable;
	public boolean isRedeemable() {
		return isRedeemable;
	}

	public void setRedeemable(boolean isRedeemable) {
		this.isRedeemable = isRedeemable;
	}

	private String rewardtype;
	private String alcoholic;
	private String offerType;
	private int campaignid;
	private String english;
	private String regional;
	private String offerValue;
	private String currency;
	//private String image;
	private String offerName;
	private int offerId;
	// private String rewardType;
	private String rewardCode;
	private int status;
	private List<Brand> validBrands;
	
	//Phase 2
	private String redemptionTrackable;
	private int redemptionLimit;
	private String ghostImage;
	private String mainImage;
	private String giftable;
	private String voucherCodeGeneration;
	private String issueTrackable;
	private String redemptionValidityDate;
	

	public String getRedemptionValidityDate() {
		return redemptionValidityDate;
	}

	public void setRedemptionValidityDate(String redemptionValidityDate) {
		this.redemptionValidityDate = redemptionValidityDate;
	}

	public String getIssueTrackable() {
		return issueTrackable;
	}

	public void setIssueTrackable(String issueTrackable) {
		this.issueTrackable = issueTrackable;
	}

	public String getRedemptionTrackable() {
		return redemptionTrackable;
	}

	public void setRedemptionTrackable(String redemptionTrackable) {
		this.redemptionTrackable = redemptionTrackable;
	}

	public int getRedemptionLimit() {
		return redemptionLimit;
	}

	public void setRedemptionLimit(int redemptionLimit) {
		this.redemptionLimit = redemptionLimit;
	}

	public String getGhostImage() {
		return ghostImage;
	}

	public void setGhostImage(String ghostImage) {
		this.ghostImage = ghostImage;
	}

	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	public String getGiftable() {
		return giftable;
	}

	public void setGiftable(String giftable) {
		this.giftable = giftable;
	}

	public String getVoucherCodeGeneration() {
		return voucherCodeGeneration;
	}

	public void setVoucherCodeGeneration(String voucherCodeGeneration) {
		this.voucherCodeGeneration = voucherCodeGeneration;
	}

	
	

	public int getCampaignid() {
		return campaignid;
	}

	public void setCampaignid(int campaignid) {
		this.campaignid = campaignid;
	}

	public String getOfferType() {
		return offerType;
	}

	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}

	public String getAlcoholic() {
		return alcoholic;
	}

	public void setAlcoholic(String alcoholic) {
		this.alcoholic = alcoholic;
	}

	/*public String getImage() {
		return image;
	}*/

	public List<Integer> getOfferid() {
		return offerid;
	}

	public void setOfferid(List<Integer> offerid) {
		this.offerid = offerid;
	}

	/*public void setImage(String image) {
		this.image = image;
	}*/

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getEnglish() {
		return english;
	}

	public void setEnglish(String english) {
		this.english = english;
	}

	public String getRegional() {
		return regional;
	}

	public void setRegional(String regional) {
		this.regional = regional;
	}

	public String getOfferValue() {
		return offerValue;
	}

	public void setOfferValue(String offerValue) {
		this.offerValue = offerValue;
	}

	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}

	public String getRewardtype() {
		return rewardtype;
	}

	public void setRewardtype(String rewardtype) {
		this.rewardtype = rewardtype;
	}

	/*
	 * public String getRewardType() { return rewardType; } public void
	 * setRewardType(String rewardType) { this.rewardType = rewardType; }
	 */

	public int getOfferId() {
		return offerId;
	}

	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}

	public String getRewardCode() {
		return rewardCode;
	}

	public void setRewardCode(String rewardCode) {
		this.rewardCode = rewardCode;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<Brand> getValidBrands() {
		return validBrands;
	}

	public void setValidBrands(List<Brand> validBrands) {
		this.validBrands = validBrands;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (validBrands != null) {
			for (Brand b : validBrands) {
				sb.append(b.toString());
			}
		}
		return sb.toString();
	}
	
	private RedeemAbleDay redeemableDay;

	 public RedeemAbleDay getRedeemableDay() {
	  return redeemableDay;
	 }

	 public void setRedeemableDay(RedeemAbleDay redeemableDay) {
	  this.redeemableDay = redeemableDay;
	 }

}
