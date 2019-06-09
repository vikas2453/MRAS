package com.diageo.mras.webservices.services;

public interface WebConstant {

	public static final String MISSING_PARAMETER = "Missing mandatory parameter";
	public static final String PRODUCT_NAME_NOTFOUND = "No Product name found for given RewardCode and PhoneNumber";
	public static final String MISSING_PARAMETER_STATUS_FLAG = "Missing mandatory parameters consumerId, groupId, statusFlag is not equal to 0,1,2";
	public static final String INVALID_CONSUMERID = "Invalid consumerId";
	public static final String INVALID_PHONENUMBER_441 = "Phone number already exits";
	public static final String WRONG_PHONENUMBER_425 = "Wrong phone number format ";
	public static final String CONSUMERID_EXIST = "ConsumerId or phone number already exists";
	public static final String UNHANDLED_ERROR = "Some unhandled error has occurred";
	public static final String CONSUMER_NOT_PART = "This consumerId is not a part of any group";
	public static final String INVALID_OFFERID = "Invalid offerId";
	public static final String INVALID_APPID = "Invalid appId";
	public static final String SUCCESSFUL = "Successful";
	public static final String INVALID_GROUPID = "Invalid groupId";
	public static final String SUCCESFUL_CAPTURED = "Consumer details captured successfully";
	public static final String INVALID_DEVICEID = "Invalid devicedId";
	public static final String DATE_FORMAT = "Date format is not correct, please provide the date in the format yyyy-dd-mm hh:mm:ss";
	public static final String DATE_FORMAT_HOURS = "Date format is not correct, please provide the date in the format yyyy-dd-mm";
	public static final String INVALID_REWARD_CODE = "Reward code is not valid";
	public static final String INVALID_CRDENTIALS = "Consumer credentials does not match";
	public static final String INVALID_TEXT = "Invalid text";
	public static final String NO_MATCH_FOUND = "No match found";
	public static final String INVALID_OUTLET_CODE = "Invalid outletcode";
	public static final String ISSUE_LIMIT_TODAY = "Issue Limit for today exceeded";
	public static final String ISSUE_LIMIT_WEEK = "Issue Limit for week exceeded";
	public static final String NO_MATCH_FOUND_CAMPAIGN = "No campaign running under this AppId";
	public static final String NO_OFFER_FOUND_APPID = "No offer running under this AppID & Countrycode";
	public static final String No_Installationoffer_for_this_AppID = "No Installationoffer for this AppID";
	public static final String CHECK_IN_REQUIRED = "Check In Required";
	public static final String DEVICE_ID_ALREADY_EXISTS = "Device Id already exists";
	public static final String SMS_SUCESS_Voucher = "You will soon recieve SMS.";
	public static final String OFFER_REDEMPTION_LIMIT_COMPLETE = "Offer's redemption limit complete ";
	public static final String OUTLET_REDEMPTION_LIMIT_COMPLETE = "Outlet's redemption limit complete ";
	public static final String OFFER_PRODUCTNAME_NOT_VALID = "Offer not valid on the given product name ";
	public static final String DEVICEID_ALREADY_EXISTS = "DeviceId already exists";
	public static final String THIRD_PARTY_VOUCHERCODE_NOT_PRESENT = "Offer can't be issued further as all the third party vouchers for this offer are completed";
	public static final String OFFER_NON_REDEEMABLE = "Offer is not redeemable";
	public static final String OFFER_IMAGE_NOT_AVAILABLE = "Image not available for this OfferId";
	public static final String OFFER_MAXIMUM_LIMIT_COMPLETE = "Maximum limit complete";
	public static final String PHONE_NUMBER_MISMATCH = "Phone Number Mismatch";
	
}