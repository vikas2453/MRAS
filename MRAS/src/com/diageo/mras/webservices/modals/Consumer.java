package com.diageo.mras.webservices.modals;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Consumer {

	int consumerId;
	String appId;
	String deviceId;
	int deviceType;
	String URL;
	String firstName;
	String promoCode;
	String EncryptPh;
	private String smsText;
	//List<String> brandList;
	//List<HashMap<String, Integer>> brandList;
	

	public String getSmsText() {
		return smsText;
	}

	public void setSmsText(String smsText) {
		this.smsText = smsText;
	}
	
	public String getEncryptPh() {
		return EncryptPh;
	}


	public void setEncryptPh(String encryptPh) {
		EncryptPh = encryptPh;
	}

	List<BrandListModal> commuChannelList;
	
	

	public List<BrandListModal> getCommuChannelList() {
		return commuChannelList;
	}

	public void setCommuChannelList(List<BrandListModal> commuChannelList) {
		this.commuChannelList = commuChannelList;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	String lastName;
	
	String optionId;
	String modifyFlag;
	String communicationChannel;
	String answerText;
	String brandId;

	String loginName;
	String password;
	
	
	
	
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}



	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOptionId() {
		return optionId;
	}

	public void setOptionId(String optionId) {
		this.optionId = optionId;
	}

	public String getModifyFlag() {
		return modifyFlag;
	}

	public void setModifyFlag(String modifyFlag) {
		this.modifyFlag = modifyFlag;
	}

	public String getCommunicationChannel() {
		return communicationChannel;
	}

	public void setCommunicationChannel(String communicationChannel) {
		this.communicationChannel = communicationChannel;
	}

	public String getAnswerText() {
		return answerText;
	}

	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	String productName;
public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	int campaignId;
	int notificationId;
	String phoneNumber;
	String dateOfBirth;
	String gender;
	List<String> phoneNumbers;
	int countryId;
	int offerId;
	int groupId;
	double latitude;
	double longitude;
	long outletCode;
	int countryCode;
	
	
	String countrycode;
	public String getCountrycode() {
		return countrycode;
	}

	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}

	String rewardCode;
String gatewayid;
	

	public String getGatewayid() {
	return gatewayid;
}

public void setGatewayid(String gatewayid) {
	this.gatewayid = gatewayid;
}

	String emailId;
	int statusFlag;
	int isGroupCheckIn;
	private long outlet_Ship_To;

	// phase 2
	String message;

	public List<String> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public String getGiftMessage() {
		return message;
	}

	public void setGiftMessage(String giftMessage) {
		this.message = giftMessage;
	}

	public long getOutlet_Ship_To() {
		return outlet_Ship_To;
	}

	public void setOutlet_Ship_To(long outlet_Ship_To) {
		this.outlet_Ship_To = outlet_Ship_To;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public int getIsGroupCheckIn() {
		return isGroupCheckIn;
	}

	public void setIsGroupCheckIn(int isGroupCheckIn) {
		this.isGroupCheckIn = isGroupCheckIn;
	}

	public int getOfferId() {
		return offerId;
	}

	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public long getOutletCode() {
		return outletCode;
	}

	public void setOutletCode(long outletCode) {
		this.outletCode = outletCode;
	}

	public int getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(int countryCode) {
		this.countryCode = countryCode;
	}

	public String getRewardCode() {
		return rewardCode;
	}

	public void setRewardCode(String rewardCode) {
		this.rewardCode = rewardCode;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public int getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(int consumerId) {
		this.consumerId = consumerId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public int getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}

	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}

	public int getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender1) {
		gender = gender1;
	}

	public int getContryId() {
		return countryId;
	}

	public void setContryId(int contryId) {
		this.countryId = contryId;
	}

	public int getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(int statusFlag) {
		this.statusFlag = statusFlag;
	}

	public String toString() {
		return phoneNumbers + " ";

	}  public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}


}
