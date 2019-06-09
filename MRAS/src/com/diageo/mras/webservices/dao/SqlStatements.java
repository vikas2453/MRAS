package com.diageo.mras.webservices.dao;

public interface SqlStatements {
	
	
	
	public static final String GET_IMAGE="select o.imageurl from offer o,distribution d where o.offerid=d.offerid and d.consumerid=? and d.offerid=?";	
	public static final String OFFERID_FOR_INSTALLATION="select offerid from offer o,campaign c where c.campaignid=o.campaignid and c.statusid=8 and c.appid=? and o.TriggerId=1 and o.StatusId=8 limit 1";	
	public static final String GET_COUNTRYID_FROM_COUNTRYCODE="SELECT CountryCode from country where CountryId=? limit 1";
	public static final String GET_PRODUCTNAME="SELECT productname from webissuevoucher where rewardcode=? and webphonenumber=? and appid=? limit 1";
	
	
	/*SELECT outletid,outletname,Outlet_ShipTo,Address,town,CountryId,County,Lattitude,Longitude,Channel,
	(select count(r.offerid) from redemption r where r.outletid=o.outletid) as redemptionTillNow,
	(select MaxRewards from offeroutletmax off where off.outletid=o.Outlet_ShipTo and offerid=?) as maxredemption,
	(select TargetChekin from offer where offerid=?) as checkins
	 from outlet o, offer f
	where
	lattitude<=? and lattitude>=? and longitude<=? and longitude>=? and
	Outlet_ShipTo
	in(SELECT outletid from offeroutletmax where offerid=?);*/
	
	public static final String SEARCH_FOR_NEARBY_OUTLETS_OFFERID = " SELECT outletid,outletname,Outlet_ShipTo,Address,town,CountryId,County,Lattitude,Longitude,Channel,Phone,ZipCode, (select count(r.offerid) from redemption r where r.outletid=o.outletid and r.offerid=?) as redemptionTillNow,(select MaxRewards from offeroutletmax off where off.outletid=o.Outlet_ShipTo and offerid=?) as maxredemption,(select count(checkinId) from checkin c where c.Outlet_ShipTo=o.Outlet_ShipTo and date(date_add(timestamps,interval 5 hour))=date(date_add(current_TIMESTAMP, INTERVAL 5 HOUR))) as checkins from outlet o where lattitude<=? and lattitude>=? and longitude<=? and longitude>=? and Outlet_ShipTo	in(SELECT outletid from offeroutletmax where offerid=?)";
	
	public static final String GET_OFFER_SAPRATEABLE="select offerId from offer where IsSeperateOutlet =true and offerid=?";
	
	
	
	
	
	
	//public static final String CAMPAIGNID_CAMPAIGNPATH_GET = "SELECT campaignid,campaignoutletpath from campaign";
	//public static final String OUTLETID_LIST_INSERT = " INSERT into outletcampaignmapping values(?,?)";

	//public static final String DELETE_OUTLETID_OUTLETCAMPAIGNMAPPING = " delete from outletcampaignmapping where campaignid=?";

	public static final String SEARCH_FOR_NEARBY_OUTLETS_WITH_APPID = " SELECT outletid,outletname,Outlet_ShipTo,Address,town,CountryId,County,Lattitude,Longitude,Channel,Phone,ZipCode,(select count(checkinId) from checkin c where date(date_add(timestamps,interval 5 hour))=date(date_add(current_TIMESTAMP, INTERVAL 5 HOUR)) and c.Outlet_ShipTo=o.Outlet_ShipTo) as checkins  from outlet o where lattitude<=? and lattitude>=? and longitude<=? and longitude>=? and Outlet_ShipTo in(SELECT outletid from outletcampaignmapping where campaignid in (select campaignid from campaign where appid=? and StatusId=8))";
	
	
	public static final String SEARCH_FOR_NEARBY_OUTLETS = " SELECT outletid,outletname,Outlet_ShipTo,Address,town,CountryId,County,Lattitude,Longitude,Channel,Phone,ZipCode,(select count(checkinId) from checkin c where date(date_add(timestamps,interval 5 hour))=date(date_add(current_TIMESTAMP, INTERVAL 5 HOUR)) and c.Outlet_ShipTo=o.Outlet_ShipTo) as checkins  from outlet o where lattitude<=? and lattitude>=? and longitude<=? and longitude>=? and Outlet_ShipTo in(SELECT outletid from outletcampaignmapping o,campaign c where o.campaignid=c.campaignid and o.campaignid=? and c.statusid=8)";
	public static final String SEARCH_FOR_NEARBY_OUTLETS_OFFER = " SELECT outletid,outletname,Outlet_ShipTo,Address,town,CountryId,County,Lattitude,Longitude,Channel,Phone,ZipCode,(select count(r.offerid) from redemption r where r.outletid=o.outletid and r.offerid=?) as redemptionTillNow,(select MaxRewards from offeroutletmax off where off.outletid=o.Outlet_ShipTo and offerid=?) as maxredemption,(select count(checkinId) from checkin c where c.Outlet_ShipTo=o.Outlet_ShipTo and date(date_add(timestamps,interval 5 hour))=date(date_add(current_TIMESTAMP, INTERVAL 5 HOUR))) as checkins from outlet o where lattitude<=? and lattitude>=? and longitude<=? and longitude>=? and Outlet_ShipTo in(SELECT outletid from outletcampaignmapping where campaignid=(select campaignId from offer where offerId=? limit 1))";
	
	
	public static final String GROUPNAME_FROM_OUTLETID = " select groupname from groupoutletmapping where outletid=?";
	public static final String SELECT_CONSUMERID = " select consumerId from consumer where consumerId=?";

	public static final String GET_OFFER_OUTLET_INFORMATION = "select o.CampaignId,currencyname,imageurl,GhostImage,PromoCode, o.StartDate,o.EndDate,OfferId,OfferName, English,Regional,offerCheckin, ConsumerOfferValue,RedemptionLimit,RedemptionTrakable ,offerRegistrationrequired from offer o,currency c,campaign cam where o.CurrencyId=c.CurrencyId and o.StatusId=8 and o.enddate >= current_timestamp and cam.campaignId=o.campaignId and o.campaignid in (select campaignid from outletcampaignmapping where outletid=?) and o.campaignid in (select campaignid from campaign where appid=? )";
	public static final String GET_OFFER_OUTLET_INFORMATION_CAMPAIGN = "select o.CampaignId,currencyname,imageurl,GhostImage,PromoCode, o.StartDate,o.EndDate,OfferId,OfferName, English,Regional,offerCheckin, ConsumerOfferValue,RedemptionLimit,RedemptionTrakable ,offerRegistrationrequired from offer o,currency c,campaign cam where o.CurrencyId=c.CurrencyId and o.statusId=8 and o.enddate >= current_timestamp and cam.campaignId=o.campaignId and o.campaignid in (select campaignid from outletcampaignmapping where outletid=?) and o.campaignid=(select campaignid from campaign where campaignid=? and appid=? limit 1)";
	public static final String GET_OFFER_OUTLET_OFFERID = "select g.offerid from offer g where g.campaignid in (select campaignid from outletcampaignmapping where outletid=?) and g.campaignid in (select campaignid from campaign where appid=?) and IsSeperateOutlet=false and StatusId=8 UNION select o.offerid from offeroutletmax om,offer o where o.offerid in (select offerid from offeroutletmax where outletid=?) and o.campaignid in (select campaignid from campaign where appid=?) and IsSeperateOutlet=true and o.StatusId=8";
	public static final String GET_OFFER_OUTLET_OFFERID_CAMPAIGN = "select g.offerid from offer g where g.campaignid in (select campaignid from outletcampaignmapping where outletid=?) and g.campaignid =? and g.campaignid in (select campaignid from campaign where appid=?) and IsSeperateOutlet=false and StatusId=8 UNION select o.offerid from offeroutletmax om,offer o where o.offerid in (select offerid from offeroutletmax where outletid=?) and o.campaignid =? and o.campaignid in (select campaignid from campaign where appid=?) and IsSeperateOutlet=true and o.StatusId=8";
	
	
	public static final String GET_ALL_GROUPS_FOR_A_CONSUMER =" SELECT a.groupid, a.statusFlag,b.phonenumber from mrasgroup a,mrasgroup b where a.ConsumerId=? and a.StatusFlag !=3 and a.groupid=b.groupid and b.isowner=1 group by groupid" ;
	public static final String GET_NOTIFICATION_DEVICES ="select deviceId,devicetype,NotificationContentEnglish,NotificationContentRegional from consumer c, consumernotificationmapping cnm  ,consumernotification cn where  cnm.createddate>DATE_SUB(now(), INTERVAL ? MINUTE) and c.consumerid=cnm.consumerid  and cnm.notificationid=cn.notificationid and cn.DeliveryMechanismId=1";
	public static final String GET_NOTIFICATION_PUB_BUSSY ="select deviceId,devicetype,NotificationContentEnglish,NotificationContentRegional from consumer c, consumernotificationmapping cnm  ,consumernotification cn where  cnm.createddate>DATE_SUB(now(), INTERVAL ? MINUTE) and c.consumerid=cnm.consumerid  and cnm.notificationid=cn.notificationid and cn.DeliveryMechanismId=1";
	
	
	//For independent notifications start
	public static final String GET_INDEPENDENT_NOTIFICATION ="select NotificationId,NotificationName,ContentEnglish,ContentRegional,UserGroupFilePath,targetCheckIn,targetDays from independentnotification where date(NotificationSendTime)=date(current_timestamp) and hour(NotificationSendTime)=hour(current_timestamp) and  minute(NotificationSendTime)=minute(current_timestamp)"; // 
	public static final String GET_INDEPENDENT_NOTIFICATION_ALL ="select DeviceId,DeviceType from consumer where appid=?";
	public static final String GET_INDEPENDENT_NOTIFICATION_CONSUMER ="select DeviceId,DeviceType, ContentEnglish,ContentRegional from independentnotification,consumer where NotificationId=? and ConsumerId=?";		
	//public static final String INDEPENDENT_NOTIFICATION_CHECKIN_FILTER = "SELECT consumerId from checkin where current_date < date_add(date(Timestamps), INTERVAL ? DAY) group by consumerid having count(consumerid)>=?";
	
	public static final String INDEPENDENT_NOTIFICATION_CHECKIN_FILTER = "SELECT ch.consumerId from checkin ch, consumer c where current_date < date_add(date(Timestamps), INTERVAL ? DAY) and c.AppId=? and ch.consumerid =c.consumerid group by ch.consumerid having count(ch.consumerid)>=? ";
	//For independent notifications end	
		
	
	public static final String CONFIRM_STATUS = "UPDATE mrasgroup SET StatusFlag=? , lastupdate=current_timestamp where ConsumerId=? and GroupId=? limit 1";
	public static final String CONSUMER_ID_VERIFICATION = "Select ConsumerId from mrasgroup where ConsumerId=? and  GroupId=? limit 1";
	//public static final String GROUP_ID_VERIFICATION = "Select GroupId from mrasgroup where limit 1";
	//public static final String GET_ACTIVE_OFFERS_FOR_TIMEREWARD = "SELECT * FROM occurrencepattern where OccurrencePatternId in (SELECT OccurrencePatternId FROM groupconfig where offerId in (SELECT offerId FROM offer where statusId=8 and TriggerId =5))";

	// for getting all complete groups
	//public static final String GET_COMPLETE_GROUPID = "select groupId from  mrasgroup where iscomplete = '1'";
	//public static final String GET_COMPLETE_OFFER = "SELECT offerId FROM offer where statusId=8 and TriggerId =5";

	//public static final String INSERT_GROUP_FOR_MEMBER = "INSERT into db_mras.mrasgroup(isOwner,appid,groupId,consumerId,createddate,lastupdate,createdby,updatedby,statusflag,phonenumber)values (?,?,?,?,now(),now(),MRAS,MRAS,?,null)";
	public static final String INSERT_GROUP_FOR_MEMBER_PHONE = "insert into db_mras.mrasgroup(isOwner,appid,groupId,phonenumber,createddate,lastupdate,createdby,updatedby,statusflag)values (?,?,?,?,now(),now(),'MRAS','MRAS',?)";
	public static final String UPDATE_GROUP_FOR_MEMBER_PHONE = "update mrasgroup set consumerid=(select consumerid from consumer where phonenumber=? limit 1) where phonenumber=? and groupid=?";
	// statement for group random win ratio
	//public static String GET_GROUPID_FOR_COMPLETE_GROUP = "select  distinct groupid,offerid from mrasgroup where  isComplete=1;";
	//public static String GET_GROUPID_FOR_CONSUMERID = "select  distinct groupid, offerId from mrasgroup where consumerid=? and isComplete=1;";
	//public static String GET_TRIGGEID_FOR_OFFER = "select triggerid from offer where offerid=?";
	
	//public static String GET_TRIGGEID_FOR_CAMPAIGN = "select triggerid from offer where campaignid=?";
	//public static String GET_GROUPS_FOR_OFFER = "select distinct groupid from mrasgroup where offerid=? and TIMEDIFF(CURRENT_TIMESTAMP, LastUpdate) < '02:00:00';";
	//public static String GET_WINRATIO_OCCUR_FOR_OFFER = "select winratio,remainingoccurrence from groupconfig where offerid=?";
	//public static String GET_COMPLETED_GROUPID = "select count(distinct groupid) from mrasgroup where offerid=? and iscomplete=1";
	//public static String UPDATE_OCCURRENCE = "update groupconfig SET RemainingOccurrence=RemainingOccurrence-1 where offerid=?";
	
	// 
	public static final String SEARCH_WITHOUT_GEOLOCATION=" SELECT outletid,outletname,Outlet_ShipTo,Address,town,CountryId,County,Lattitude,Longitude,Channel,Phone,ZipCode FROM outletmyisam o,campaign c WHERE MATCH(outletname,address,town) AGAINST (?) and Outlet_ShipTo in(SELECT outletid from outletcampaignmapping where campaignid=? and c.appid=?)";
	public static final String SEARCH_WITHOUT_GEOLOCATION_WITHOUT_CAMPAIGNID=" SELECT OutletId,OutletName,Outlet_ShipTo,Address,town,CountryId,County,Lattitude,Longitude,Channel,Phone,ZipCode FROM outletmyisam WHERE MATCH(outletname) AGAINST (?) and Outlet_ShipTo in(SELECT outletid from outletcampaignmapping where campaignid in(select campaignid from campaign where appid=?))";
	public static final String SEARCH_WITHOUT_GEOLOCATION_WITH_Outlet_ship_to="SELECT OutletId,OutletName,Outlet_ShipTo,Address,town,CountryId,County,Lattitude,Longitude,Channel,Phone,ZipCode,(select count(checkinId) from checkin c where date(date_add(timestamps,interval 5 hour))=date(date_add(current_TIMESTAMP, INTERVAL 5 HOUR)) and c.Outlet_ShipTo=o.Outlet_ShipTo) as checkins FROM outlet o WHERE Outlet_ShipTo=? limit 1";
	public static final String SEARCH_WITHOUT_GEOLOCATION_WITH_AppId="SELECT distinct outletid,outletname,Outlet_ShipTo,Address,town,CountryId,County,Lattitude,Longitude,Channel from outlet where Outlet_ShipTo in(SELECT outletid from outletcampaignmapping where campaignid in(select campaignid from campaign where appid=?))";
	public static final String SEARCH_WITHOUT_GEOLOCATION_WITH_COUNTRYCODE_ZIPCODE="SELECT outletid,outletname,Outlet_ShipTo,Address,town,CountryId,County,Lattitude,Longitude,Channel,Phone,ZipCode from outletmyisam where (countryid=? or zipcode=?) and Outlet_ShipTo in(SELECT outletid from outletcampaignmapping where campaignid=?)";
	public static final String SEARCH_WITHOUT_GEOLOCATION_WITH_COUNTRYCODE_ZIPCODE_WITHOUT_CAMPAIGNID="SELECT outletname,Outlet_ShipTo from outlet where (countryid=? or zipcode=?)";
	public static final String SEARCH_WITHOUT_GEOLOCATION_WITH_AppId_Campaign_ID="SELECT distinct outletid,outletname,Outlet_ShipTo,Address,town,CountryId,County,Lattitude,Longitude,Channel from outlet where Outlet_ShipTo in(SELECT outletid from outletcampaignmapping where campaignid=?)";
	//public static final String getOfferFromOfferId = "SELECT StartDate,EndDate,StartTime,EndTime,OccurrencePatternId from offer where offerid=?";
	public static final String getOfferFromOfferId = "Select distinct p.productname,b.brandname,offer.VoucherCodeGeneration, offer.StartDate,offer.EndDate,offer.RedemptionWindow,offer.RedemptionValidity,offer.StartTime,offer.EndTime,offer.OfferTrade,offer.OccurrencePatternId,offer.IsRedeemable,offer.offername,offer.regional,offer.imageurl,offer.english,offer.consumeroffervalue,offer.IsSeperateOutlet,currency.currencycode,reward.rewardtype,reward.alcoholic,offer.campaignid,offer.triggerid ,IsGiftable,GhostImage,RedemptionTrakable,RedemptionLimit,VoucherCodeGeneration ,issueTrackable,op.ReoccurencPatternType,OccurrencePatternValue1,OccurrencePatternValue2,Days from offerproductmapping o,offer offer, product p,brand b,currency currency,rewardtype reward,occurrencepattern op where o.offerid=? and op.OccurrencePatternId=offer.OccurrencePatternId and statusId=8 and p.productId=o.productId and p.brandid=b.brandid and offer.offerid = o.offerid and offer.currencyid=currency.currencyid and reward.rewardtypeid=offer.rewardtypeid";

	
	
	public static final String GET_OUTLET_FROM_OFFER_CAMPAIGN="SELECT  outletid,outletname,Outlet_ShipTo,Address,town,CountryId,County,Lattitude,Longitude,Channel,ZipCode,Phone,Email,(select count(checkinId) from checkin c where date(timestamps)=current_date and c.Outlet_ShipTo=o.Outlet_ShipTo) as checkins from outlet o where Outlet_ShipTo in(SELECT outletid from outletcampaignmapping where campaignid=(select campaignid from offer where offerid=?)) ";
	public static final String GET_OUTLET_FROM_OFFER_OFFER="SELECT  outletid,outletname,Outlet_ShipTo,Address,town,CountryId,County,Lattitude,Longitude,Channel,ZipCode,Phone,Email,(select count(checkinId) from checkin c where date(timestamps)=current_date and c.Outlet_ShipTo=o.Outlet_ShipTo) as checkins from outlet o where Outlet_ShipTo in(SELECT outletid from offeroutletmax where offerid=?) ";
	
	public static final String GET_OFFER_ONTRADE_ON="SELECT outletid,outletname,Outlet_ShipTo,Address,town,CountryId,County,Lattitude,Longitude,Channel,ZipCode,Phone,Email,SQRT(POW(69.1 * (Lattitude - ?), 2) + POW(69.1 * (? - Longitude) * COS(Lattitude / 57.3), 2)) AS distance ,o.offerid from outlet,offer o,campaign c where c.campaignId=o.campaignId and c.appid=? and o.campaignId=? and OfferTrade=? and c.statusid=8 and o.statusid=8 and Outlet_ShipTo in(SELECT outletid from offeroutletmax om where om.offerid=o.offerid) HAVING distance<? ORDER BY distance";
	
	public static final String GET_ALLOUTLET_ONTRADE=" SELECT  outletid,outletname,Outlet_ShipTo,Address,town,CountryId,County,Lattitude,Longitude,Channel,ZipCode,Phone, Email from outlet where Outlet_ShipTo in(SELECT outletid from outletcampaignmapping where campaignid=(select campaignid from offer where offerid=?)) ";
	
	public static final String GET_OUTLET_ONTRADE=" SELECT  outletid,outletname,Outlet_ShipTo,Address,town,CountryId,County,Lattitude,Longitude,Channel,ZipCode,Phone, Email,SQRT(POW(69.1 * (Lattitude - ?), 2) + POW(69.1 * (? - Longitude) * COS(Lattitude / 57.3), 2)) AS distance from outlet where Outlet_ShipTo in(SELECT outletid from outletcampaignmapping where campaignid=(select campaignid from offer where offerid=?)) HAVING distance < ? ORDER BY distance limit ? ";
	
	public static final String GET_OUTLET_ONTRADE_OFFER=" SELECT  outletid,outletname,Outlet_ShipTo,Address,town,CountryId,County,Lattitude,Longitude,Channel,ZipCode,Phone,Email ,SQRT(POW(69.1 * (Lattitude - ?), 2) + POW(69.1 * (? - Longitude) * COS(Lattitude / 57.3), 2)) AS distance from outlet where Outlet_ShipTo in(SELECT outletid from offeroutletmax where offerid=?) HAVING distance < ? ORDER BY distance limit ? ";
	
	public static final String GET_ALLOUTLET_ONTRADE_OFFER=" SELECT  outletid,outletname,Outlet_ShipTo,Address,town,CountryId,County,Lattitude,Longitude,Channel,ZipCode,Phone,Email  from outlet where Outlet_ShipTo in(SELECT outletid from offeroutletmax where offerid=?) ";
	
	
	public static final String GET_INFORMATION_ABOUT_OUTLET="SELECT outletname,Address,town,CountryId,County,Lattitude,Longitude,Channel,Phone,Email,Website,FaceBookURL,TwitterURL,PhotoURL,ZipCode,DoingMusic,PubRegion,Sales_Org,Plant,Channel from outlet where Outlet_ShipTo =?";
	
	public static final String GET_INFORMATION_ABOUT_OUTLET_PUB="select band_number,pub_band_name,pub_band_time,pub_band_about, pub_band_facebook,pub_band_website,pub_band_photo from outletbandmapping where  shiptoid=?";
	
	public static final String GET__COUNTRY_SINGLEDAY="select countryId from countrysetting where ruleid=5 and valuedatetime=current_date group by countryId";
	public static final String VALIDATE_EMAIL_DATEOFBIRTH="select consumerId from consumer where emailid=? and dateofbirth=? limit 1";
	
	
	public static final String GET__CONSUMERIDSTATUS_INGROUP="select  statusflag,phonenumber from mrasgroup where  groupid  =(select groupid from mrasgroup where consumerID=? and isowner=1 limit 1)";
	
	public static final String GET__CONSUMER_DETAILS="select phonenumber,groupid from mrasgroup where  consumerid=? and isowner=1 limit 1";
	
	public static final String GET__RESULT_GETCONSUMERUPDATES="Select d.Rewardcode,d.statusof,d.LastUpdate from  distribution d where d.offerid=? and d.consumerId =? limit 1";
	
	public static final String GET__RESULT_GETDEVICEUPDATES="Select d.Rewardcode,d.statusof,d.LastUpdate from  distribution d where d.offerid=? and d.deviceid =? limit 1";
	
	
	public static final String GET__CAMPAIGN_DETAILS="SELECT CampaignId,CampaignName,isSpecialDescription,descriptionText,date(StartDate) as StartDate,date(EndDate) as EndDate,PromoCode,MaxRewardsAllotted FROM campaign WHERE appid=? and statusid=8";
	public static final String GET__GIFT_OFFERID="select offerid from giftmessage where PhoneNumber=? and IsRegistered =false";
	//public static final String GET__RESULT_GETCONSUMERUPDATES="Select distinct p.productname,b.brandname, offer.StartDate,offer.EndDate,offer.StartTime,offer.EndTime,offer.OccurrencePatternId from  distribution d, offerproductmapping o, offer offer, product p,brand b where o.offerid=? and p.productId=o.productId and p.brandid=b.brandid";

	public static final String GET_OFFERID_FOR_CONSUMERNOTIFICATION="Select offerId from offer where lastupdate >? and statusid=8 and DATEDIFF(CURRENT_DATE,StartDate)<60  group by offerId";
		
	public static final String GET_ACTIVE_OFFER_LIST="select offerid,c.promocode,o.startdate,o.enddate,o.IsRedeemable,o.RedemptionValidity,o.RedemptionWindow,o.campaignid,o.offercheckin,o.offerRegistrationrequired,o.offername,o.english,o.regional,o.consumeroffervalue,o.triggerid,currency.currencycode ,c.isSpecialDescription, c.descriptionText,o.StartTime,o.EndTime from offer o,campaign c,currency currency where o.statusid=8 and o.campaignid=c.campaignid  and o.currencyid=currency.currencyid  and c.appid=?";

	public static final String GET_MESSAGES_FOR_OFFERID="select cn.NotificationId,cn.NotificationContentEnglish,cn.NotificationContentRegional,cn.offerid,cn.messagetype from  consumernotificationmapping cnm,  consumernotification cn,offer of, campaign cam   where consumerid=? and   cnm.notificationid = cn.notificationid and   cn.offerid=of.offerid and  of.campaignId=cam.campaignid and of.statusId=8 and of.campaignid IN (select campaignId from campaign where Appid=?) ";
	public static final String GET_OFERID_REDEEMED_="select offerid from redemption where consumerid=? and appid=?";
	
	public static final String VALIDATE_APPID_CACHE=" select count(appid) from clientapps where appid=?";

// SearchConsumerByPhoneNumber
	public static final String SEARCH_CONSUMERBY_PHONEO = "select PhoneNumber from consumer where PhoneNumber in (";
	
	
	
	
	public static final String GET__COUNTRY_SINGLEDAY_NonAlcoholic="select countryId from countrysetting where ruleid=14 and valuedatetime=current_date group by countryId";

    public static final String OCCRENCEPATTERN_STRING="select OccurrencePatternId, StartDate, EndDate, ReoccurencPatternType, OccurrencePatternValue1, OccurrencePatternValue2, Days from db_mras.occurrencepattern where OccurrencePatternId=?";

    
    //For Issue Voucher
    public static final String GETALL_OFFERID_FOR_CAMPAIGNID = "Select offerId FROM db_mras.offer where campaignId=?";
    public static final String CHECK_FOR_UNIQUE_VOUCHER = "select rewardcode from db_mras.distribution where consumerId=?";
    public static final String CHECK_FOR_UNIQUE_VOUCHER_DEVICE_ID = "select rewardcode from db_mras.distribution where deviceId=?";
    
    public static final String GET_IMAGE_FROM_OFFERID_MAIN = "select imageurl from offer where offerid=?";
    public static final String GET_IMAGE_FROM_OFFERID_GHOST = "select GhostImage from offer where offerid=?";
    
    public static final String CHECK_FOR_UNIQUE_VOUCHER_WEB_VOUCHER = "select rewardCode from db_mras.webissuevoucher where webphonenumber=?";
    public static final String TEXT_URL = "select URL,SMSPhone,PhoneEncryption from texturl where Textenter=?";
    
    public static final String GETALL_CONSUMERID_FOR_GROUP = "Select consumerid FROM db_mras.mrasgroup where groupId=?";
    
    public static final String GET_CONSUMER_FROM_PHONENUMBER = "Select consumerid FROM db_mras.consumer where PhoneNumber=?";
    
    public static final String GET_OFFERID_FOR_THIRDPARTYVOUCHER = "SELECT offerId FROM offer  where offerid=? and VoucherCodeGeneration='MRAS'";
    public static final String GET_VOUCHER_FOR_THIRDPARTYVOUCHER = "select count(voucherCode) as counter,voucherCode from  thirdpartyvouchercode where offerid=inOfferId and (status=false or status is null)";
   
    
    public static final String SET_ISCOMPLETE_ZERO = "update db_mras.mrasgroup set iscomplete=0, lastupdate=current_timestamp where groupId=? and offerid=?";
    //public static final String COUNTRY_LIST_LEGALCHECK = "select countryid from country where legalcheck=1";
    //public static final String REPORT_LIST = "SELECT reportid,OccurrencePatternId FROM report r";
    //public static final String REPORT_LIST_EMAIL = "SELECT  DISTINCT r.campaignId,r.reportid,reportname,ReportTypeId,emailid FROM report r ,reportusermapping m,user u where r.reportid = m.reportid and  m.userid=u.userid and r.reportid in (";
    
    //For Reporting
    //public static final String REPORT_CAMPAIGN_MATRIX="select ot.triggername,ofr.maxrewardsallotted,(select count(transactionid) from redemption red where red.offerID=ofr.offerid and red.statusid=2) as rewardsredeemed,rt.Alcoholic,(SELECT max(abc) as populartime FROM( SELECT DATE_FORMAT(lastupdate,'%l') as abc , count(transactionID) as counter FROM db_mras.redemption where StatusId=2 GROUP BY abc)aa) as populartime  from rewardtype rt,offer Ofr,offertrigger ot where campaignid =? and ofr.triggerid=ot.triggerid and ofr.rewardtypeid=rt.rewardtypeid";
    //public static final String REPORT_CONSUMER_MATRIX="SELECT r.consumerId,Floor(DATEDIFF(current_date(),c.dateofbirth)/365.25),c.gender,DATE_FORMAT(r.createddate,'%T'),DATE_FORMAT(r.createddate,'%d %M %Y'),o.outletname,o.Address,r.offerid,rt.rewardtype,ofr.consumeroffervalue from rewardtype rt, consumer c,redemption r, outlet o ,offer ofr where c.consumerID=r.consumerID and r.outletId=o.outletID and r.offerId=ofr.offerID and ofr.rewardtypeId=rt.rewardtypeid";
    //public static final String REPORT_OUTLET_MATRIX="";
    public static final String VARIFICATION_DATEOFBIRTH_EMAILID="select date(DateOfBirth),emailid from consumer where ConsumerId=?";
    
    //For SAP extract
    //public static final String SAP_EXTRACT="";
    
    //For starting and closing campaigns and offers
    
    //public static final String START_OFFER="update offer SET statusid=8 , lastupdate=current_timestamp  where current_date()=StartDate and statusId=6";
    //public static final String CLOSE_OFFER="update  offer SET statusid=10 , lastupdate=current_timestamp where subdate(current_date, 1) =Enddate and statusid=8;";
    //public static final String START_CAMPAIGN="update campaign SET statusid=8 , lastupdate=current_timestamp where current_date()=StartDate and statusId=6;";
    //public static final String CLOSE_CAMPAIGN="update campaign SET statusid=10 , lastupdate=current_timestamp  where subdate(current_date, 1)=Enddate and statusid=8;";
    
    //public static final String OFFERID_BIRTHDAY="select  c.appid,o.offerId, o.consumerlistpath from offer o, campaign c  where o.triggerId=7 and o.StatusId=8 and c.campaignid=o.campaignid";
    //public static final String OFFERID_APPINSTALL = "select  c.appid,o.offerId, o.consumerlistpath from offer o, campaign c  where o.triggerId=8 and o.StatusId=8 and c.campaignid=o.campaignid";
    
  
	// Notifications
	//public static final String REWARD_NOTIFICATIONS_ANDROID = "select registrationId from consumer c, consumernotificationmapping cnm  ,consumernotification cn where  cnm.createddate>DATE_SUB(now(), INTERVAL 100 MINUTE) and c.consumerid=cnm.consumerid and c.devicetype=1 and cnm.notificationid=cn.notificationid and cn.DeliveryMechanismId=1;";
	//public static final String REWARD_NOTIFICATIONS_APPLE = "select registrationId from consumer c, consumernotificationmapping cnm  ,consumernotification cn where  cnm.createddate>DATE_SUB(now(), INTERVAL 100 MINUTE) and c.consumerid=cnm.consumerid and c.devicetype=2 and cnm.notificationid=cn.notificationid and cn.DeliveryMechanismId=1;";
	//public static final String MESSAGE_NOTIFICATIONS_ANDROID = "select registrationId from consumer c, consumernotificationmapping cnm where  cnm.createddate>DATE_SUB(now(), INTERVAL 100 MINUTE) and c.consumerid=cnm.consumerid and c.devicetype=1";
	//public static final String MESSAGE_NOTIFICATIONS_APPLE = "select registrationId from consumer c, consumernotificationmapping cnm where  cnm.createddate>DATE_SUB(now(), INTERVAL 100 MINUTE) and c.consumerid=cnm.consumerid and c.devicetype=2";
	//public static final String MESSAGE_NOTIFICATIONS_EMAIL ="select c.EmailId,cn.NotificationContentEnglish from consumer c, consumernotificationmapping cnm  ,consumernotification cn where  cnm.createddate>DATE_SUB(now(), INTERVAL 100 MINUTE) and c.consumerid=cnm.consumerid and cnm.notificationid=cn.notificationid and cn.DeliveryMechanismId=2";
	
	//Updating Rewards which have expired.
	//public static final String UPDATE_EXPIRED_REWARDS="UPDATE distribution dis,(select DistributionId from distribution dis,offer of where dis.offerid=of.offerid and  DATE_ADD(dis.createddate , INTERVAL of.RedemptionValidity DAY) <now())  aa SET dis.StatusOf=1, dis.LastUpdate=now() WHERE aa.DistributionId=dis.distributionId ";
	
	// For consumer notification
	//public static final String CONSUMER_NOTI = "SELECT notificationId,offerId,UserGroupFilePath,filterId,filterP1,filterP2 FROM db_mras.consumernotification where dateofdelivery < (NOW() + INTERVAL 60 MINUTE) and dateofdelivery > NOW() ";
	//public static final String INSERT_NOTI = "insert into consumernotificationmapping values (?,?,current_timestamp,current_timestamp,'MRAS','MRAS')";
	//public static final String ALl_NOTII = "select consumerid from consumer co where co.appid=(select c.appid from campaign c,offer o,consumernotification n where o.offerid=n.offerid and o.campaignid=c.campaignid and n.notificationid=?)";
	// notificationid notificationid offerid
	//public static final String AGE_FILTER = "select consumerId from consumer c where DATEDIFF(current_timestamp, dateofbirth)/365.25>(select filterp1 from consumernotification where notificationid=?) and DATEDIFF(current_timestamp, dateofbirth)/365.25<(select filterp2 from consumernotification where notificationid=?) and c.appid=(select appid from campaign a,offer o where a.campaignid=o.campaignid and o.offerId=?)"; 
	// notificationid,offerid
	//public static final String GENDER_FILTER = "select consumerId from consumer c where gender=(select filterP3 from consumernotification where notificationid=?) and c.appid=(select appid from campaign a,offer o where a.campaignid=o.campaignid and o.offerId=?)";
	// notificationid =2,offerid= 5
	//public static final String CHECKIN_FILTER = "select distinct consumerId from consumer c where c.consumerid in (select consumerid from checkin where datediff(current_timestamp,timestamps)<(select filterp1 from consumernotification where notificationid=?))and c.appid=(select appid from campaign a,offer o  where a.campaignid=o.campaignid and o.offerId=?)";
	//offerid,offerid 2
	//public static final String ISSUE_FILTER = "select consumerId from consumer c where c.consumerid in (select distinct consumerid from distribution where offerid=?) and c.appid=(select appid from campaign a,offer o where a.campaignid=o.campaignid and o.offerId=?)";
	//offerid,offerid 1
	//public static final String REDEEM_FILTER = "select consumerId from consumer c where c.consumerid in (select distinct consumerid from redemption where offerid in( select offerid from offer where campaignid= (select campaignid from offer where offerid=?) ) ) and c.appid=(select appid from campaign a,offer o where a.campaignid=o.campaignid and o.offerId=?)";
	//offerid=1 offerid offerid=1
	//public static final String REWARD_VALIDITY_FILTER = "select distinct consumerId from distribution d,offer o where d.offerid=? and date_add(current_timestamp, INTERVAL (select redemptionvalidity from offer where offerid=?) DAY) < date_add(d.createddate, INTERVAL (select filterp1 from consumernotification where offerId=? and filterId=15 and notificationid=?) DAY) "; 
	
	//public static final String CHECKIN_SPONTANEOUS ="select distinct consumerId from consumer c where c.consumerid in (select consumerid from checkin where datediff(current_timestamp,timestamps)< (select filtervalue from offer where offerid=?))and c.appid=(select appid from campaign a,offer o  where a.campaignid=o.campaignid and o.offerId=?) ";
    public static final String GET_NOTIFICATION_GIFTING ="select (select concat(firstname,' ',lastname) from consumer where consumerid=g.consumerid) as name, g.offerid , c.deviceid,c.devicetype ,o.English as NotificationContentEnglish, o.Regional as NotificationContentRegional from giftmessage g,consumer c,offer o where g.phonenumber=c.phonenumber and o.offerid=g.offerid  and g.createddate>DATE_SUB(now(), INTERVAL ? MINUTE) group by g.phonenumber";
    
    //Old query-- with noti content
    //public static final String GET_NOTIFICATION_DEVICES1 ="select c.deviceId,c.devicetype,d.offerid,CONCAT( ? ,o.English) as NotificationContentEnglish,CONCAT( ? ,o.Regional) as NotificationContentRegional from distribution d,consumer c,offer o where d.consumerid=c.consumerid and o.offerid=d.offerid and d.createddate>=DATE_SUB(now(), INTERVAL ? MINUTE) and d.createddate <now() ";
    
    public static final String GET_NOTIFICATION_DEVICES1 ="select c.deviceId,c.devicetype,offerid from distribution d,consumer c where d.consumerid=c.consumerid  and d.createddate>=DATE_SUB(now(), INTERVAL ? MINUTE) and d.createddate <now()";
    public static final String GET_NOTIFICATION_DEVICES2= "select c.deviceid,c.devicetype,CONCAT( ? ,( select  b.phonenumber from mrasgroup a,mrasgroup b where a.groupid=b.groupid and b.isowner=1 and a.phonenumber=c.PhoneNumber and g.groupid=a.groupid limit 1 ) ) as NotificationContentEnglish ,'1 new Group' as NotificationContentRegional from mrasgroup g ,consumer c where c.PhoneNumber=g.phoneNumber and isOwner=0 and g.createddate>DATE_SUB(now(), INTERVAL ? MINUTE)";
	}
