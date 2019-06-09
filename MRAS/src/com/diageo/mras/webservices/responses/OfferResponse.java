package com.diageo.mras.webservices.responses;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.diageo.mras.webservices.modals.Brand;

@XmlRootElement
public class OfferResponse {

	private Date StartDate;
	private Date EndDate;
	private String StartTime;	
	private String EndTime;
	private String english;
	private String regional;
	private String offerValue;
	private String currency;
	private String redemptionWindow;
	private boolean isRedeemable;
	public boolean isRedeemable() {
		return isRedeemable;
	}

	public void setRedeemable(boolean isRedeemable) {
		this.isRedeemable = isRedeemable;
	}

	private int offerId;
	private int offercheckIn;
	private int campaignId;
	private String promocode;
	private String imageurl;
	private String offerType;
	private String offerName;
	private boolean registrationRequired;
	// Added in phase 2
	private String redemptionTrackable;
	private int redemptionLimit;
	private String ghostImage;
	private String mainImage;
	private String giftable;
	private String voucherCodeGeneration;
	private String specialCampaign;
	private String specialCampaignText;
	private String offerTrade;
	private List<Brand> validBrands;

	public String getOfferTrade() {
		return offerTrade;
	}

	public void setOfferTrade(String offerTrade) {
		this.offerTrade = offerTrade;
	}

	
	public List<Brand> getValidBrands() {
		return validBrands;
	}

	public void setValidBrands(List<Brand> validBrands) {
		this.validBrands = validBrands;
	}
	public String getSpecialCampaignText() {
		return specialCampaignText;
	}

	public void setSpecialCampaignText(String specialCampaignText) {
		this.specialCampaignText = specialCampaignText;
	}
	public String getSpecialCampaign() {
		return specialCampaign;
	}

	public void setSpecialCampaign(String specialCampaign) {
		this.specialCampaign = specialCampaign;
	}


	public String getRedemptionWindow() {
		return redemptionWindow;
	}

	public void setRedemptionWindow(String redemptionWindow) {
		this.redemptionWindow = redemptionWindow;
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

	public int getRedemptionLimit() {
		return redemptionLimit;
	}

	public void setRedemptionLimit(int redemptionLimit) {
		this.redemptionLimit = redemptionLimit;
	}

	public String getRedemptionTrackable() {
		return redemptionTrackable;
	}

	public void setRedemptionTrackable(String redemptionTrackable) {
		this.redemptionTrackable = redemptionTrackable;
	}

	public String getOfferType() {
		return offerType;
	}

	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getOfferValue() {
		return offerValue;
	}

	public void setOfferValue(String consumerOfferValue) {
		this.offerValue = consumerOfferValue;
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

	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}

	public Date getStartDate() {
		return StartDate;
	}

	public void setStartDate(Date startDate) {
		StartDate = startDate;
	}

	public Date getEndDate() {
		return EndDate;
	}

	public void setEndDate(Date endDate) {
		EndDate = endDate;
	}

	public boolean isRegistrationRequired() {
		return registrationRequired;
	}

	public void setRegistrationRequired(boolean registrationRequired) {
		this.registrationRequired = registrationRequired;
	}

	public int getOfferId() {
		return offerId;
	}

	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}

	public int getOffercheckIn() {
		return offercheckIn;
	}

	public void setOffercheckIn(int offercheckIn) {
		this.offercheckIn = offercheckIn;
	}

	public int getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}

	public String getPromocode() {
		return promocode;
	}

	public void setPromocode(String promocode) {
		this.promocode = promocode;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
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
	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getEndTime() {
		return EndTime;
	}

	public void setEndTime(String endTime) {
		EndTime = endTime;
	}

	public String getString() {
		
		String resultstring="{registrationRequired: " + registrationRequired + ", offerId:"
		+ offerId + ", StartDate:" + StartDate+ ", EndDate:" + EndDate + ", offercheckIn:" + offercheckIn
		+ ", campaignId:" + campaignId+ ", redemptionLimit:" + redemptionLimit;
		
		StringBuffer resultStringBuffer=new StringBuffer(resultstring);
		
		if(english!=null && !english.isEmpty()){
			resultStringBuffer.append(", english:" + english);
		}
		if(regional!=null && !regional.isEmpty()){
			resultStringBuffer.append(", regional:" + regional);
		}
		if(offerValue!=null && !offerValue.isEmpty()){
			resultStringBuffer.append(", offerValue:" + offerValue);
		}
		if(currency!=null && !currency.isEmpty()){
			resultStringBuffer.append(", currency:" + currency);
		}
		if(redemptionWindow!=null && !redemptionWindow.isEmpty()){
			resultStringBuffer.append(", redemptionWindow:" + redemptionWindow);
		}
		if(promocode!=null && !promocode.isEmpty()){
			resultStringBuffer.append(", promocode:" + promocode);
		}
		if(imageurl!=null && !imageurl.isEmpty()){
			resultStringBuffer.append(", imageurl:" + imageurl);
		}
		if(specialCampaign!=null && !specialCampaign.isEmpty()){
			resultStringBuffer.append(", specialCampaign:" + specialCampaign);
		}
		if(specialCampaignText!=null && !specialCampaignText.isEmpty()){
			resultStringBuffer.append(", specialCampaignText:" + specialCampaignText);
		}
		if(offerTrade!=null && !offerTrade.isEmpty()){
			resultStringBuffer.append(", offerTrade:" + offerTrade);
		}
		if(validBrands!=null && !validBrands.isEmpty()){
			
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			for (Brand s : validBrands) {
				sb.append(s.getString());
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("]");
			resultStringBuffer.append(", validBrands:" + sb);
		}		
		if(offerType!=null && !offerType.isEmpty()){
			resultStringBuffer.append(", offerType:" + offerType);
		}
		if(offerName!=null && !offerName.isEmpty()){
			resultStringBuffer.append(", offerName:" + offerName);
		}
		if(redemptionTrackable!=null && !redemptionTrackable.isEmpty()){
			resultStringBuffer.append(", redemptionTrackable:" + redemptionTrackable);
		}
		if(ghostImage!=null && !ghostImage.isEmpty()){
			resultStringBuffer.append(", ghostImage:" + ghostImage);
		}
		if(mainImage!=null && !mainImage.isEmpty()){
			resultStringBuffer.append(", mainImage:" + mainImage);
		}
		if(giftable!=null && !giftable.isEmpty()){
			resultStringBuffer.append(", giftable:" + giftable);
		}
		if(voucherCodeGeneration!=null && !voucherCodeGeneration.isEmpty()){
			resultStringBuffer.append(", voucherCodeGeneration:" + voucherCodeGeneration);
		}	
		
		
		resultStringBuffer.append("}");
		return resultStringBuffer.toString();
	}
	
	
	

}
