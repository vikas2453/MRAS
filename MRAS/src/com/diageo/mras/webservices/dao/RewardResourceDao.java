package com.diageo.mras.webservices.dao;

import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.diageo.mras.cache.MrasCache;
import com.diageo.mras.scheduler.dao.SchedulerDAO;
import com.diageo.mras.webservices.init.ConnectionPool;
import com.diageo.mras.webservices.modals.Brand;
import com.diageo.mras.webservices.modals.ConsumerResult;
import com.diageo.mras.webservices.modals.CampaignObject;
import com.diageo.mras.webservices.modals.CampaignsModal;
import com.diageo.mras.webservices.modals.Message;
import com.diageo.mras.webservices.modals.Offer;
import com.diageo.mras.webservices.modals.RedeemAbleDay;
import com.diageo.mras.webservices.modals.Reward;
import com.diageo.mras.webservices.responses.ConsumerUpdateResponse;
import com.diageo.mras.webservices.responses.Imageresponse;
import com.diageo.mras.webservices.responses.OfferResponse;
import com.diageo.mras.webservices.services.RewardResource;

/**
 * This is a DAO class for the RewardResource.
 * 
 * @author Infosys Limited
 * @version 1.0
 */

public class RewardResourceDao {
	static Logger logger = Logger.getLogger(RewardResourceDao.class.getName());
	static Format formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	static SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
	static final Format formatterTime = new SimpleDateFormat("hh:mm:ss");
	private static final String REWARD_LIST="RewardList";
	private static final String REDEEM_REWARD_LIST="RedeemList";

	private static final RewardResource rewardResource = new RewardResource();

	/**
	 * Description of ValidateRewardVoucher()
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @param consumerid
	 *            This is the Id of consumer
	 * @param offerid
	 *            This is the OfferId.
	 * @param outletid
	 *            This is the OutletId.
	 * @param rewardCode
	 *            This is the RewardCode.
	 * @return Various Responses to redeemReward web service
	 */
	public int ValidateRewardVoucher(int consumerId, int offerId, int groupId,
			int countryCode, long outletCode, String rewardCode,
			String productName, String appId) throws SQLException {
		Connection con = null;
		int result = 0;
		CallableStatement stmt = null;

		try {
			con = ConnectionPool.getConnection();
			// int outletId = (int) (outletCode % 8000000000l);

			stmt = con.prepareCall("{ call RedeemReward(?,?,?,?,?,?,?,?,?) }");
			if (logger.isDebugEnabled()) {
				logger.debug("calling procedure for RedeemReward with parameter parameter consumerId "
						+ consumerId
						+ "offerId  "
						+ offerId
						+ "groupId "
						+ groupId
						+ "countryCode "
						+ countryCode
						+ "outletCode "
						+ outletCode
						+ " rewardCode "
						+ rewardCode + " productName" + productName);
			}

			stmt.setInt(1, consumerId);
			stmt.setInt(2, offerId);
			stmt.setInt(3, groupId);
			stmt.setInt(4, countryCode);
			stmt.setLong(5, outletCode);
			stmt.setString(6, rewardCode);
			stmt.setString(7, productName);
			stmt.setString(8, appId);

			stmt.registerOutParameter(9, java.sql.Types.INTEGER);
			stmt.executeUpdate();
			result = stmt.getInt(9);
			stmt.close();
		} catch (SQLException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("SQLException from ValidateRewardVoucher "
						+ e.getMessage());
			}
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return result;
	}

	public int ValidateWebRewardVoucher(String phoneNumber, int offerId,
			String productName, int countryCode, long outletCode,
			String rewardCode, String appId) throws SQLException {
		Connection con = null;
		int result = 0;
		CallableStatement stmt = null;

		try {
			con = ConnectionPool.getConnection();
			// int outletId = (int) (outletCode % 8000000000l);

			stmt = con.prepareCall("{ call webRedeemReward(?,?,?,?,?,?,?,?) }");
			if (logger.isDebugEnabled()) {
				logger.debug("calling procedure for RedeemReward with parameter parameter phoneNumber "
						+ phoneNumber
						+ "offerId  "
						+ offerId
						+ "productName "
						+ productName
						+ "countryCode "
						+ countryCode
						+ "outletCode "
						+ outletCode
						+ " rewardCode "
						+ rewardCode + " appId " + appId);
			}

			stmt.setString(1, phoneNumber);
			stmt.setInt(2, offerId);
			stmt.setString(3, productName);
			stmt.setInt(4, countryCode);
			stmt.setLong(5, outletCode);
			stmt.setString(6, rewardCode);
			stmt.setString(7, appId);

			stmt.registerOutParameter(8, java.sql.Types.INTEGER);
			stmt.executeUpdate();
			result = stmt.getInt(8);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return result;
	}

	public int ValidateRewardWN(int consumerId, int offerId,
			String productName, int countryCode, long outletCode,
			String rewardCode, String appId) throws SQLException {
		Connection con = null;
		int result = 0;
		CallableStatement stmt = null;

		try {
			con = ConnectionPool.getConnection();
			// int outletId = (int) (outletCode % 8000000000l);

			stmt = con.prepareCall("{ call RedeemRewardWN(?,?,?,?,?,?,?,?) }");
			if (logger.isDebugEnabled()) {
				logger.debug("calling procedure for RedeemReward with parameter parameter consumerid "
						+ consumerId
						+ "offerId  "
						+ offerId
						+ "productName "
						+ productName
						+ "countryCode "
						+ countryCode
						+ "outletCode "
						+ outletCode
						+ " rewardCode "
						+ rewardCode + " appId " + appId);
			}

			stmt.setInt(1, consumerId);
			stmt.setInt(2, offerId);
			stmt.setString(3, productName);
			stmt.setInt(4, countryCode);
			stmt.setLong(5, outletCode);
			stmt.setString(6, rewardCode);
			stmt.setString(7, appId);

			stmt.registerOutParameter(8, java.sql.Types.INTEGER);
			stmt.executeUpdate();
			result = stmt.getInt(8);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return result;
	}

	/**
	 * Description of capturedeviceDetails()
	 * 
	 * @param consumerid
	 *            This is the Id of consumer
	 * @param deviceID
	 *            This is the device Id of Consumer.
	 * @param deviceType
	 *            This is the devicetype.
	 * @param AppID
	 *            This is the AppID.
	 * @param notificationid
	 *            This is the notificationid.
	 * @param phoneNumber
	 *            This is the phonenumber of the consumer.
	 * @return Various Responses to capturedeviceDetails Web service
	 */
	public String getCountrycode(int countryCode) {

		Connection con = null;
		ResultSet rs;
		String countrycode = null;

		try {
			con = ConnectionPool.getConnection();

			PreparedStatement preparedStatement = con
					.prepareStatement(SqlStatements.GET_COUNTRYID_FROM_COUNTRYCODE);
			preparedStatement.setInt(1, countryCode);
			rs = preparedStatement.executeQuery();

			if (!rs.next()) {
				logger.info("No countryid found for given countrycode ");
				return countrycode;
			}

			else {
				countrycode = rs.getString("CountryCode");
				rs.close();
				preparedStatement.close();
				return countrycode;
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return countrycode;

	}

	public int capturedeviceDetails(int consumerId, String deviceId,
			int deviceType, String appId, int notificationId,
			String phoneNumber, java.util.Date dateOfBirth, String gender,
			String emailId, String firstname, String lastName, int countryId) {
		ResultSet rs;
		Connection con = null;
		int result = 0;
		CallableStatement stmt = null;
		if (logger.isDebugEnabled()) {
			logger.debug("Capturing all details for the consumerId : "
					+ consumerId);
		}
		try {
			con = ConnectionPool.getConnection();

			stmt = con
					.prepareCall("{ call CaptureDeviceDetails(?,?,?,?,?,?,?,?,?,?,?,?,?) }");

			stmt.setString(1, appId);
			stmt.setInt(2, consumerId);
			stmt.setString(3, deviceId);
			stmt.setInt(4, deviceType);
			stmt.setInt(5, notificationId);
			stmt.setString(6, phoneNumber);

			Timestamp timeStamp = null;
			if (dateOfBirth != null) {
				timeStamp = new Timestamp(dateOfBirth.getTime());
			}
			stmt.setTimestamp(7, timeStamp);

			stmt.setString(8, gender);
			stmt.setString(9, emailId);

			stmt.setString(10, firstname);
			stmt.setString(11, lastName);
			stmt.setInt(12, countryId);
			stmt.registerOutParameter(13, java.sql.Types.INTEGER);
			rs = stmt.executeQuery();

			result = stmt.getInt(13);
			stmt.close();
			// logger.debug("result is" + result);

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return result;
	}

	/**
	 * Description of InstallationOfferId() *
	 * 
	 * @param appId
	 *            This is the app Id
	 * 
	 * @return Issues a voucher to the consumer and makes an entry in the
	 *         distribution table
	 */

	public int InstallationOfferId(String appId) {
		Connection con = null;
		ResultSet rs;
		int offerid = 0;

		try {
			con = ConnectionPool.getConnection();

			PreparedStatement preparedStatement = con
					.prepareStatement(SqlStatements.OFFERID_FOR_INSTALLATION);
			preparedStatement.setString(1, appId);
			rs = preparedStatement.executeQuery();

			if (!rs.next()) {
				logger.info("No installation offerid ");
				return offerid;
			}

			else {
				offerid = rs.getInt("offerid");
				rs.close();
				preparedStatement.close();
				return offerid;
			}

		}

		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return offerid;

	}

	
	public int issueVoucherForThirdPartyVoucher(int consumerID, int offerId,
			String deviceId) {
		Connection con = null;
		CallableStatement stmt = null;
		int response = 0;
		try {
			con = ConnectionPool.getConnection();
			stmt = con
					.prepareCall("{ call IssueVoucherForThirdPartyOffer(?,?,?,?) }");
			stmt.setInt(1, consumerID);
			stmt.setInt(2, offerId);
			stmt.setString(3, deviceId);
			stmt.registerOutParameter(4, java.sql.Types.INTEGER);
			stmt.executeQuery();
			response = stmt.getInt(4);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return response;
	}

	/**
	 * Description of getOffer()
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @param offerId
	 *            This is the offer Id
	 * @return This returns the offer details
	 */

	public Offer getOffer(int offerId) {
		// logger.debug("getting offer " + offerId);
		Connection con = null;
		ResultSet rs;
		Offer offer = null;
		try {

			con = ConnectionPool.getConnection();
			PreparedStatement preparedStatement = con
					.prepareStatement(SqlStatements.getOfferFromOfferId);
			preparedStatement.setInt(1, offerId);
			// logger.debug("setint offerId " + offerId);
			rs = preparedStatement.executeQuery();
			Date now = Calendar.getInstance().getTime();

			if (rs.next()) {
				offer = new Offer();
				if (logger.isDebugEnabled()) {
					logger.debug("perameter set in offer object are StartDate "
							+ rs.getDate("StartDate") + " EndDate "
							+ rs.getDate("EndDate") + " OccurrencePatternId "
							+ rs.getInt("OccurrencePatternId") + " StartTime "
							+ rs.getTime("StartTime") + " EndTime "
							+ rs.getTime("EndTime") + " rewardTypeId "
							+ rs.getInt("rewardTypeId") + " current time is "
							+ now);
				}
				offer.setStartDate(rs.getDate("StartDate"));
				offer.setEndDate(rs.getDate("EndDate"));
				offer.setRedemptionDaysOccurancePatternId(rs
						.getInt("OccurrencePatternId"));
				Calendar endCalender = Calendar.getInstance();
				Calendar startCalender = Calendar.getInstance();
				endCalender
						.set(Calendar.HOUR, rs.getTime("EndTime").getHours());
				endCalender.set(Calendar.MINUTE, rs.getTime("EndTime")
						.getMinutes());

				startCalender.set(Calendar.HOUR, rs.getTime("StartTime")
						.getHours());
				startCalender.set(Calendar.MINUTE, rs.getTime("StartTime")
						.getMinutes());
				offer.setRedemptionStartTime(startCalender);
				offer.setRedemptionEndTime(endCalender);
				offer.setRewardtype(rs.getString("rewardTypeId"));
				rs.close();
				preparedStatement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return offer;
	}

	/**
	 * Description of getReward()
	 * 
	 * @param consumerId
	 *            This is the consumerId
	 * @param lastRefresh
	 *            This is the last refresh time when getConsumerUpdates was
	 *            called
	 * @return This returns the list.
	 */

	public List<Integer> getReward(int consumerId, java.util.Date lastRefresh) {
		List<Integer> rewards = new ArrayList<Integer>();
		Connection con = null;
		CallableStatement stmt = null;
		ResultSet rs = null;
		try {

			con = ConnectionPool.getConnection();
			stmt = con.prepareCall("{ call GetConsumerUpdateOffer(?,?) }");
			stmt.setInt(1, consumerId);
			Timestamp timeStamp = null;
			if (lastRefresh != null) {
				timeStamp = new Timestamp(lastRefresh.getTime());
			}
			stmt.setTimestamp(2, timeStamp);
			rs = stmt.executeQuery();
			if (!rs.next()) {
				rewards = null;
				if (logger.isDebugEnabled()) {
					logger.debug("No reward with  lastrefresh" + lastRefresh
							+ " for consumerId" + consumerId);
				}

				return rewards;

			} else {
				int value = rs.getInt(1);
				if (value == 0) {
					rewards.add(value);
					return rewards;
				}

				rs.previous();

				while (rs.next()) {
					value = rs.getInt(1);
					rewards.add(value);
				}

			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return rewards;

	}

	/**
	 * Description of getOfferobject()
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @return This is used in getConsumerUpdates to get the offer object.
	 */
	public List<OfferResponse> getActiveoffer(String appid) {
		List<OfferResponse> Offerobjectlist = new ArrayList<OfferResponse>();
		Connection con = null;
		ResultSet rs;

		try {
			con = ConnectionPool.getConnection();
			PreparedStatement preparedStatement = con
					.prepareStatement(SqlStatements.GET_ACTIVE_OFFER_LIST);
			Timestamp timeStamp = null;
			preparedStatement.setString(1, appid);

			rs = preparedStatement.executeQuery();
			// select
			// offerid,c.promocode,o.endtime,o.campaignid,o.offercheckin,o.offercheckin,o.registrationrequired
			// from offer o,campaign c where o.statusid=8 and createddate>? and
			// o.campaignid=c.campaignid;
			while (rs.next()) {

				OfferResponse offerobj = new OfferResponse();
				offerobj.setOfferId(rs.getInt("offerid"));
				offerobj.setPromocode(rs.getString("promocode"));
				offerobj.setOfferName(rs.getString("offername"));
				offerobj.setCurrency(rs.getString("currencycode"));
				offerobj.setEnglish(rs.getString("english"));
				offerobj.setRegional(rs.getString("regional"));
				offerobj.setOfferValue(rs.getString("consumeroffervalue"));
				offerobj.setRedemptionWindow(rs.getString("RedemptionWindow"));
				offerobj.setRedeemable(rs.getBoolean("IsRedeemable"));

				/*
				 * Boolean isSpecialCampaign = rs
				 * .getBoolean("isSpecialDescription");
				 * 
				 * 
				 * 
				 * System.out.println("isSpecialDescription"+isSpecialCampaign);
				 */

				Integer isSpecialCampaign = rs.getInt("isSpecialDescription");

				if (isSpecialCampaign == null || isSpecialCampaign == 0) {

					offerobj.setSpecialCampaign("No");
				} else {

					offerobj.setSpecialCampaign("Yes");
					offerobj.setSpecialCampaignText(rs
							.getString("descriptionText"));
				}

				int triggerid = rs.getInt("triggerid");
				switch (triggerid) {
				case 1:
					offerobj.setOfferType("Installation");
					break;
				case 2:
					offerobj.setOfferType("Group Random Win Ratio");
					break;
				case 3:
					offerobj.setOfferType("Group Guaranteed Reward");
					break;
				case 4:
					offerobj.setOfferType("Group Manual Push");
					break;
				case 5:
					offerobj.setOfferType("Group Timed Reward");
					break;
				case 6:
					offerobj.setOfferType("Spontaneous");
					break;
				case 7:
					offerobj.setOfferType("Birthday");
					break;
				case 8:
					offerobj.setOfferType("Application Insatallation Anniversary");
					break;
				case 9:
					offerobj.setOfferType("External Trigger");
					break;
				case 10:
					offerobj.setOfferType("Limited");
					break;
				case 11:
					offerobj.setOfferType("Spontaneous Manual");
					break;
				case 12:
					offerobj.setOfferType("Limited Manual Push");
					break;
				case 13:
					offerobj.setOfferType("Random Win");
					break;

				}

				String startdate = rs.getString("startdate");

				String secondDate = rs.getString("enddate");
				Date date = null;
				Date date2 = null;

				if (startdate != "") {
					try {
						date = (Date) formatter.parseObject(startdate);

					} catch (Exception e) {
						logger.info("exception in parsing startDate"
								+ e.getMessage());
						e.printStackTrace();
					}
					offerobj.setStartDate(date);

				}

				if (secondDate != "") {
					try {
						date2 = (Date) formatter.parseObject(secondDate);
					} catch (Exception e) {
						logger.info("exception in parsing endDate"
								+ e.getMessage());
						e.printStackTrace();
					}
					offerobj.setEndDate(date2);
				}

				Date startTime = rs.getTime("StartTime");
				Date endTime = rs.getTime("EndTime");
				offerobj.setStartTime(startTime.toString());
				offerobj.setEndTime(endTime.toString());

				offerobj.setCampaignId(rs.getInt("campaignid"));
				offerobj.setOffercheckIn(rs.getInt("offercheckin"));
				offerobj.setRegistrationRequired(rs
						.getBoolean("offerRegistrationrequired"));

				Offerobjectlist.add(offerobj);
			}

			rs.close();
			preparedStatement.close();
		} catch (Exception e) {
			logger.info("exception in parsing endTime" + e.getMessage());
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return Offerobjectlist;

	}

	public ConsumerUpdateResponse getConsumerUpdatesDao(int consumerID,
			java.util.Date lastRefresh, String deviceId, String appId)
			throws Exception {
		ConsumerUpdateResponse consumerUpdateResponse = new ConsumerUpdateResponse();
		List<OfferResponse> activeofferlistfiltered = new ArrayList<OfferResponse>();
		List<Reward> rewardList = null;

		// logger.debug("Getting all rewards for the consumerID = " +
		// consumerID);

		// setting message list first
		/*List<Message> messagelist = null;
		if (consumerID != 0) {

			messagelist = getActiveMessages(consumerID, lastRefresh, appId);
			
			 * if(messagelist.size()>0){
			 * System.out.println("consumerID in message11"+consumerID); }
			 
		}
		consumerUpdateResponse.setMessageList(messagelist);*/

		// setting OfferList now
		

		// Getting reward for the particular consumer
		Map<String, List<Integer>> rewardsAndRedeem=getReward(consumerID, lastRefresh, deviceId);
		List<Integer> rewards1=null;
		if(rewardsAndRedeem!=null && rewardsAndRedeem.size()>0){
			rewards1= rewardsAndRedeem.get(REWARD_LIST);
			
		}

		/*if (rewards1 == null) {
			if (logger.isDebugEnabled()) {
				logger.info("No reward for given lastrefresh: " + lastRefresh
						+ "for given Consumer Id :" + consumerID);
			}
			consumerUpdateResponse.setRewardList(rewardList);
			return consumerUpdateResponse;
		}*/

		if ( rewards1==null || rewards1.size() == 0) {

			logger.info("rewardslist is null");
			//return consumerUpdateResponse = null;
		} else {
			rewardList = new ArrayList<Reward>();

			for (int offerId : rewards1) {
					Reward reward = new Reward();
				Offer offer = (Offer) MrasCache.MCache.recover("Offer"
						+ offerId);
				if (offer == null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Offer Id :" + offerId
								+ " not found in Cache");
					}
					offer = getOfferWithOfferid(offerId);

					if (offer != null) {
						// key for offerId starting from 30000 to get reward
						// object, for getting consumer updates.
						MrasCache.MCache.admit("Offer" + offerId, offer);
					}

				}
				if (offer != null) {
					reward.setRedeemableDay(offer.getRedeemableDay());
					reward.setValidBrands(offer.getValidBrands());
					reward.setRewardtype(offer.getRewardtype());
					reward.setOfferId(offerId);
                    
					reward.setOfferName(offer.getOfferName());
					reward.setCurrency(offer.getCurrency());
					reward.setEnglish(offer.getEnglish());
					reward.setRegional(offer.getRegional());
					reward.setOfferValue(offer.getOfferValue());
					reward.setMainImage(offer.getImage());
					reward.setAlcoholic(offer.getAlcoholic());
					reward.setOfferType(offer.getOfferType());
					reward.setCampaignid(offer.getCampaignId());
					reward.setGhostImage(offer.getGhostImage());
					reward.setRedemptionTrackable(offer
							.getRedemptionTrackable());
					reward.setRedemptionLimit(offer.getRedemptionLimit());
					reward.setGiftable(offer.getGiftable());
					reward.setVoucherCodeGeneration(offer
							.getVoucherCodeGeneration());
					reward.setIssueTrackable(offer.getIssueTrackable());
					reward.setRedeemable(offer.isRedeemable());
					int redemptionValidity = offer.getRedemptionValidity();
					// let's get the reward's attribute linked to this
					// particular consumer.

					if (consumerID == 0) {
						getRewardswithConsumerId(deviceId, reward, offerId,
								redemptionValidity);
						rewardList.add(reward);
					}

					else {
						getRewardswithConsumerId(consumerID, reward, offerId,
								redemptionValidity);
						rewardList.add(reward);
					}

				}
			}

			consumerUpdateResponse.setRewardList(rewardList);
			// logger.debug("RewardList for the consumerId: " + consumerID
			// + " is " + rewardList);
		}
		
		
		
		
		List<OfferResponse> activeofferlist = (List<OfferResponse>) MrasCache.MCache
		.recover("activeOfferList" + appId);

       if (activeofferlist == null) {
	     // logger.info("Activeofferlist not found in the cache, and hence getting from the database");
	       activeofferlist = getActiveoffer(appId);
	       MrasCache.MCache.admit("activeOfferList" + appId, activeofferlist);
       }
       List<Integer> redeemOfferId =null;
       if(rewardsAndRedeem!=null && rewardsAndRedeem.size()>0){
    	   redeemOfferId = rewardsAndRedeem.get(REDEEM_REWARD_LIST);
    	   
       }
     
      // Set<Integer> offeridobj = getRedeemedOfferId(consumerID, appId);
       
       List<OfferResponse> consumerActiveofferlist=new ArrayList<OfferResponse>();
       consumerActiveofferlist.addAll(activeofferlist);
      /* for(OfferResponse offerResponse:activeofferlist){
    	   removeOfferFromActiveofferlist.add(offerResponse);
       }*/
       if(redeemOfferId!=null){
	        Iterator<OfferResponse> activeofferlistiterator = consumerActiveofferlist.iterator();
	       

	        while (activeofferlistiterator.hasNext()) {
		        OfferResponse offer = activeofferlistiterator.next();

		        if ( redeemOfferId.contains(offer.getOfferId())) {
			        activeofferlistiterator.remove();
			        
		        }
	        }
        }
       
      if (lastRefresh == null) {
    	  
	      consumerUpdateResponse.setOfferList(consumerActiveofferlist);
      }
     else {

	      List<OfferResponse> offerobjectList2 = new ArrayList<OfferResponse>();
	      Date startDate;
	     for (OfferResponse offer : consumerActiveofferlist) {
	       startDate = offer.getStartDate();
		   if (startDate.after(lastRefresh)) {
			   offerobjectList2.add(offer);
		   }
	     }
	    consumerUpdateResponse.setOfferList(offerobjectList2);
     }

		return consumerUpdateResponse;
		
	}

	public Map<String, List<Integer>>  getReward(int consumerId, java.util.Date lastRefresh,			
			String deviceId) {
		List<Integer> rewards =null;
		List<Integer> redeemOfferId =null;
		Map<String, List<Integer>> rewardsAndRedeem =null;
		Connection con = null;
		CallableStatement stmt = null;
		ResultSet rsRewardOfferId = null,rsRedeemOfferId=null;
		boolean flagToValidateExecution=false;
		try {
			con = ConnectionPool.getConnection();
			stmt = con.prepareCall("{ call GetConsumerUpdateOffer(?,?,?) }");
			stmt.setInt(1, consumerId);
			Timestamp timeStamp = null;
			if (lastRefresh != null) {
				timeStamp = new Timestamp(lastRefresh.getTime());
			}
			stmt.setTimestamp(2, timeStamp);
			stmt.setString(3, deviceId);
			flagToValidateExecution=stmt.execute();
	
			if(flagToValidateExecution){				
				rsRewardOfferId=stmt.getResultSet();
				rewardsAndRedeem=new HashMap<String, List<Integer>>();
			 if (rsRewardOfferId != null) {				 
					rewards= new ArrayList<Integer>();
					while (rsRewardOfferId.next()) {
						rewards.add(rsRewardOfferId.getInt("offerId"));
					}
				 rsRewardOfferId.close();
				
				 rewardsAndRedeem.put(REWARD_LIST, rewards) ;
				 
			}
			 
			if(stmt.getMoreResults()){
				rsRedeemOfferId=stmt.getResultSet();
				if(rsRedeemOfferId!=null){
					redeemOfferId=new ArrayList<Integer>();
					while(rsRedeemOfferId.next()){
						redeemOfferId.add(rsRedeemOfferId.getInt("offerId"));
					}
					 rewardsAndRedeem.put(REDEEM_REWARD_LIST, redeemOfferId) ;
					 
				}
				rsRedeemOfferId.close();
			}
			
		  }
			stmt.close();
		}

		catch (Exception e) {
			logger.info("Exception in getReward:- "+e.getMessage());
			e.printStackTrace();
		} finally {

			ConnectionPool.returnConnection(con);
		}

		return rewardsAndRedeem;

	}


	/**
	 * Description of getMessageObjectForOfferId()
	 * 
	 * @param offerIDForNotification
	 *            This is the last refresh time
	 * @return
	 * @return This is used in getConsumerUpdates to get the messages
	 *         associated.
	 */

	public List<Message> getActiveMessages(int ConsumerId,
			java.util.Date lastRefresh, String appId) {
		Connection con = null;
		ResultSet rs;
		List<Message> messagelist = new ArrayList<Message>();

		Message message = null;
		try {
			con = ConnectionPool.getConnection();
			PreparedStatement preparedStatement = con
					.prepareStatement(SqlStatements.GET_MESSAGES_FOR_OFFERID);

			preparedStatement.setInt(1, ConsumerId);
			preparedStatement.setString(2, appId);
			/*
			 * Timestamp timeStamp = null; if (lastRefresh != null) { timeStamp
			 * = new Timestamp(lastRefresh.getTime()); }
			 * preparedStatement.setTimestamp(3, timeStamp);
			 */
			rs = preparedStatement.executeQuery();
			if (!rs.next()) {

				return messagelist = null;
			}

			rs.previous();

			while (rs.next()) {
				message = new Message();
				message.setNotificationId(rs.getInt("NotificationId"));
				message.setContentEnglish(rs
						.getString("NotificationContentEnglish"));
				message.setContentRegional(rs
						.getString("NotificationContentRegional"));
				message.setOfferId(rs.getInt("offerid"));
				message.setMessageType(rs.getString("messagetype"));
				messagelist.add(message);
				/*
				 * System.out.println("messagelistr"+rs
				 * .getString("NotificationContentEnglish"));
				 */

			}
			rs.close();
			preparedStatement.close();
		} catch (SQLException e) {

			e.printStackTrace();

		} finally {
			ConnectionPool.returnConnection(con);
		}
		return messagelist;
	}

	public Set<Integer> getRedeemedOfferId(int ConsumerId, String appId) {
		Connection con = null;
		ResultSet rs;
		Set<Integer> offerid = new HashSet<Integer>();

		try {
			con = ConnectionPool.getConnection();
			PreparedStatement preparedStatement = con
					.prepareStatement(SqlStatements.GET_OFERID_REDEEMED_);

			preparedStatement.setInt(1, ConsumerId);
			preparedStatement.setString(2, appId);
			/*
			 * Timestamp timeStamp = null; if (lastRefresh != null) { timeStamp
			 * = new Timestamp(lastRefresh.getTime()); }
			 * preparedStatement.setTimestamp(3, timeStamp);
			 */
			rs = preparedStatement.executeQuery();
			if (!rs.next()) {

				return offerid = null;
			}

			rs.previous();

			while (rs.next()) {

				offerid.add(rs.getInt("offerid"));

			}
			rs.close();
			preparedStatement.close();
		} catch (SQLException e) {

			e.printStackTrace();

		} finally {
			ConnectionPool.returnConnection(con);
		}
		return offerid;
	}

	/**
	 * Description of getOfferWithOfferid()
	 * 
	 * @param offerId
	 *            This is offer Id to get the associated details with the offer
	 * @return This is used in getConsumerUpdates to get offer object.
	 */
	public Offer getOfferWithOfferid(int offerId) {

		Connection con = null;
		ResultSet rs1;
		List<Brand> brands = null;
		List<String> produts;
		String previousBrand = "";

		Offer offer = null;
		try {
			con = ConnectionPool.getConnection();
			PreparedStatement preparedStatement = con
					.prepareStatement(SqlStatements.getOfferFromOfferId);
			preparedStatement.setInt(1, offerId);

			rs1 = preparedStatement.executeQuery();
			if (!rs1.next()) {

				return offer;
			}
			rs1.previous();
			Brand b = new Brand();
			if (rs1.next()) {
				offer = new Offer();

				RedeemAbleDay redeemAbleDay = new RedeemAbleDay();
				Integer interval = rs1.getInt("ReoccurencPatternType");
				if (interval == 0) {
					redeemAbleDay.setInterval("Daily");
					// System.out.println("Daily");
					redeemAbleDay.setFrequency(rs1
							.getString("OccurrencePatternValue1"));
				} else if (interval == 1) {
					redeemAbleDay.setInterval("Weekly");
					// System.out.println("Weekly");
					redeemAbleDay.setFrequency(rs1
							.getString("OccurrencePatternValue1"));
					redeemAbleDay.setPattern(rs1.getString("Days"));
				} else {
					redeemAbleDay.setInterval("Monthly");
					// System.out.println("Monthly");
					redeemAbleDay.setFrequency(rs1
							.getString("OccurrencePatternValue1"));
					redeemAbleDay.setPattern(rs1
							.getString("OccurrencePatternValue2"));
				}

				offer.setRedeemableDay(redeemAbleDay);

				offer.setStartDate(rs1.getDate("StartDate"));
				offer.setEndDate(rs1.getDate("EndDate"));
				offer.setRedemptionDaysOccurancePatternId(rs1
						.getInt("OccurrencePatternId"));
				offer.setOfferTrade(rs1.getString("OfferTrade"));
				offer.setRegional(rs1.getString("regional"));
				offer.setEnglish(rs1.getString("english"));
				offer.setOfferName(rs1.getString("offername"));
				offer.setOfferValue(rs1.getString("consumeroffervalue"));
				offer.setCurrency(rs1.getString("currencycode"));
				offer.setCampaignId(rs1.getInt("campaignid"));
				offer.setRedemptionWindow(rs1.getString("RedemptionWindow"));

				Blob blob = rs1.getBlob("imageurl");
				if (blob == null) {
					offer.setImage("NO");
				} else {
					offer.setImage("YES");
				}

				// //phase2 start

				Blob blob1 = rs1.getBlob("GhostImage");
				if (blob1 == null) {
					offer.setGhostImage("NO");
				} else {
					offer.setGhostImage("YES");
				}
				offer.setRedemptionLimit(rs1.getInt("RedemptionLimit"));
				offer.setRedemptionValidity(rs1.getInt("RedemptionValidity"));
				offer.setVoucherCodeGeneration(rs1
						.getString("VoucherCodeGeneration"));
				if (rs1.getBoolean("RedemptionTrakable")) {
					offer.setRedemptionTrackable("YES");
				} else {
					offer.setRedemptionTrackable("NO");
				}

				if (rs1.getBoolean("IsGiftable")) {
					offer.setGiftable("YES");
				} else {
					offer.setGiftable("NO");
				}

				offer.setRedeemable(rs1.getBoolean("IsRedeemable"));
				offer.setSeperateOutlet(rs1.getBoolean("IsSeperateOutlet"));
				
				
				if (rs1.getBoolean("issueTrackable")) {
					offer.setIssueTrackable("YES");
				} else {
					offer.setIssueTrackable("NO");
				}
				// end

				Calendar endCalender = Calendar.getInstance();
				Calendar startCalender = Calendar.getInstance();

				if (!(rs1.getTime("StartTime") == null)) {
					startCalender.set(Calendar.HOUR_OF_DAY,
							rs1.getTime("StartTime").getHours());
					startCalender.set(Calendar.MINUTE, rs1.getTime("StartTime")
							.getMinutes());
					offer.setRedemptionStartTime(startCalender);
				}
				if (!(rs1.getTime("EndTime") == null)) {
					endCalender.set(Calendar.HOUR_OF_DAY, rs1
							.getTime("EndTime").getHours());
					endCalender.set(Calendar.MINUTE, rs1.getTime("EndTime")
							.getMinutes());

					offer.setRedemptionEndTime(endCalender);
				}

				offer.setRewardtype(rs1.getString("rewardtype"));
				offer.setCampaignId(rs1.getInt("campaignid"));

				String alcoholic = rs1.getString("alcoholic");

				if (alcoholic == "0") {
					offer.setAlcoholic("NO");
				}

				else {
					offer.setAlcoholic("YES");
				}
				int triggerid = rs1.getInt("triggerid");
				switch (triggerid) {
				case 1:
					offer.setOfferType("Installation");
					break;
				case 2:
					offer.setOfferType("Group Random Win Ratio");
					break;
				case 3:
					offer.setOfferType("Group Guaranteed Reward");
					break;
				case 4:
					offer.setOfferType("Group Manual Push");
					break;
				case 5:
					offer.setOfferType("Group Timed Reward");
					break;
				case 6:
					offer.setOfferType("Spontaneous");
					break;
				case 7:
					offer.setOfferType("Birthday");
					break;
				case 8:
					offer.setOfferType("Application Insatallation Anniversary");
					break;
				case 9:
					offer.setOfferType("External Trigger");
					break;
				case 10:
					offer.setOfferType("Limited");
					break;
				case 11:
					offer.setOfferType("Spontaneous Manual");
					break;
				case 12:
					offer.setOfferType("Limited Manual Push");
					break;
				case 13:
					offer.setOfferType("Random Win");
					break;

				}

				rs1.previous();
			}
			while (rs1.next()) {
				String brandname = rs1.getString("brandname");
				List<Brand> brandlist;
				if (brandname.equalsIgnoreCase(previousBrand)) {
					brandlist = offer.getValidBrands();
					b = brandlist.get(brandlist.size() - 1);
					produts = b.getProducts();
					String productName = rs1.getString("productname");
					produts.add(productName);
				} else {
					b = new Brand();
					previousBrand = brandname;
					b.setBrandName(brandname);
					String product = rs1.getString("ProductName");
					produts = new ArrayList<String>();
					produts.add(product);
					b.setProducts(produts);
					if (offer.getValidBrands() == null) {
						brands = new ArrayList<Brand>();
						brands.add(b);
						offer.setValidBrands(brands);
					} else {
						brands.add(b);
						offer.setValidBrands(brands);
					}
				}
			}
			rs1.close();
			preparedStatement.close();
		}

		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return offer;
	}

	/**
	 * Description of getOfferWithOfferid()
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @param appId
	 *            This is application Id to validate
	 * @return This returns true/false for depending on valid application id
	 */
	public boolean validateAppIDDao(String appId) {
		Connection con = null;
		ResultSet rs;
		Boolean b = false;
		try {
			con = ConnectionPool.getConnection();
			PreparedStatement preparedStatement = con
					.prepareStatement(SqlStatements.VALIDATE_APPID_CACHE);

			preparedStatement.setString(1, appId);
			rs = preparedStatement.executeQuery();
			rs.next();

			int value = rs.getInt(1);
			rs.close();
			preparedStatement.close();
			if (value == 0) {
				return false;
			} else {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return b;
	}

	/**
	 * Description of validateCountryCodedao()
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @param contryid
	 *            This is country Id to validate
	 * @return This is used for validating the country id
	 */
	public int validateCountryCodedao(int contryid) {
		Connection con = null;
		CallableStatement cs = null;
		Integer b = 1;
		try {
			con = ConnectionPool.getConnection();
			cs = con.prepareCall("{call db_mras.ConutryTimevalidation(?,?)}");
			cs.setInt(1, contryid);
			cs.registerOutParameter(2, java.sql.Types.INTEGER);
			cs.executeUpdate();
			b = cs.getInt(2);
			cs.close();
			if (logger.isDebugEnabled()) {
				logger.debug("reciving result from Database-" + b);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return b;
	}	
	
	
	
	/**
	 * Description of getAlcoholicVoucherCountryList()
	 * 
	 * @return This is used for fetching the list of countries where alcoholic
	 *         vouchers are allowed
	 */
	public List<Integer> getAlcoholicVoucherCountryList() {
		Connection con = null;
		ResultSet rs;
		List<Integer> countryIds = new ArrayList<Integer>();
		try {
			con = ConnectionPool.getConnection();
			PreparedStatement preparedStatement = con
					.prepareStatement(SqlStatements.GET__COUNTRY_SINGLEDAY);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				countryIds.add(rs.getInt("countryId"));
			}
			rs.close();
			preparedStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}

		return countryIds;
	}

	public Set<Integer> getAllOffersDao(String appId, int countryCode,int camapignId) {
		Connection con = null;

		ResultSet rs_main = null, rs_red = null;
		Boolean executed;

		Set<Integer> offerIdObj = new HashSet<Integer>();
		try {

			con = ConnectionPool.getConnection();
			PreparedStatement stmt = con.prepareCall("Call getalloffers(?,?,?)");
			stmt.setInt(1, countryCode);
			stmt.setString(2, appId);
			stmt.setInt(3, camapignId);
			executed = stmt.execute();
			if (executed) {

				rs_main = stmt.getResultSet();
				
				while (rs_main.next()) {					
					int offerId = rs_main.getInt("offerId");					
					offerIdObj.add(offerId);
					// OfferId.add(offerId);

				}

				rs_main.close();

				if (stmt.getMoreResults()) {

					rs_red = stmt.getResultSet();

					while (rs_red.next())

					{
						int offerId = rs_red.getInt("offerId");

						offerIdObj.add(offerId);						

					}
					rs_red.close();

				}

			}
		}

		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return offerIdObj;

	}

	/**
	 * Description of getAlcoholicVoucherCountryList()
	 * 
	 * @return This is used for fetching the list of countries where non
	 *         alcoholic vouchers are allowed
	 */
	public List<Integer> getNonAlcoholicVoucherCountryList() {
		Connection con = null;
		ResultSet rs;
		List<Integer> countryIds = new ArrayList<Integer>();
		try {
			con = ConnectionPool.getConnection();
			PreparedStatement preparedStatement = con
					.prepareStatement(SqlStatements.GET__COUNTRY_SINGLEDAY_NonAlcoholic);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				countryIds.add(rs.getInt("countryId"));
			}
			rs.close();
			preparedStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}

		return countryIds;
	}

	/**
	 * Description of getRewardswithConsumerId()
	 * 
	 * @param consumerId
	 *            This is consumer Id
	 * @param reward
	 *            This is the reward object
	 * @param offerId
	 *            This is offer Id
	 * @return This is used for fetching the reward list of countries where non
	 *         alcoholic vouchers are allowed
	 */
	public void getRewardswithConsumerId(int consumerId, Reward reward,
			int offerId, int redemptionvalidity) {

		Connection con = null;
		ResultSet rs1;

		try {
			con = ConnectionPool.getConnection();

			PreparedStatement preparedStatement = con
					.prepareStatement(SqlStatements.GET__RESULT_GETCONSUMERUPDATES);
			preparedStatement.setInt(1, offerId);
			preparedStatement.setInt(2, consumerId);

			rs1 = preparedStatement.executeQuery();

			if (rs1.next()) {
				reward.setRewardCode(rs1.getString("Rewardcode"));
				reward.setStatus(rs1.getInt("statusof"));

				String redemptionValidityDate = rs1.getString("LastUpdate");

				if (redemptionValidityDate != "") {

					Calendar c = Calendar.getInstance();
					try {
						c.setTime(formatter1.parse(redemptionValidityDate));
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					c.add(Calendar.DATE, redemptionvalidity); // number of days
																// to add
					redemptionValidityDate = formatter1.format(c.getTime());

					reward.setRedemptionValidityDate(redemptionValidityDate);

				}

			}
			rs1.close();
			preparedStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}

	}

	public void getRewardswithConsumerId(String deviceId, Reward reward,
			int offerId, int redemptionvalidity) {

		Connection con = null;
		ResultSet rs1;

		try {
			con = ConnectionPool.getConnection();

			PreparedStatement preparedStatement = con
					.prepareStatement(SqlStatements.GET__RESULT_GETDEVICEUPDATES);
			preparedStatement.setInt(1, offerId);
			preparedStatement.setString(2, deviceId);

			rs1 = preparedStatement.executeQuery();

			if (rs1.next()) {
				reward.setRewardCode(rs1.getString("Rewardcode"));
				reward.setStatus(rs1.getInt("statusof"));
				String redemptionValidityDate = rs1.getString("LastUpdate");

				if (redemptionValidityDate != "") {

					Calendar c = Calendar.getInstance();

					try {
						c.setTime(formatter1.parse(redemptionValidityDate));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					c.add(Calendar.DATE, redemptionvalidity); // number of days
																// to add
					redemptionValidityDate = formatter1.format(c.getTime());

					reward.setRedemptionValidityDate(redemptionValidityDate);

				}

			}
			rs1.close();
			preparedStatement.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}

	}

	/**
	 * Description of getOfferIdsForCampaignId()
	 * 
	 * @param campaignId
	 *            This is the campaign Id
	 * @return Offers associated with the campaign
	 */
	public List<Integer> getOfferIdsForCampaignId(int campaignId) {

		Connection con = null;
		ResultSet rs1;
		List<Integer> offerIdList = new ArrayList<Integer>();

		try {
			con = ConnectionPool.getConnection();
			PreparedStatement preparedStatement = con
					.prepareStatement(SqlStatements.GETALL_OFFERID_FOR_CAMPAIGNID);
			preparedStatement.setInt(1, campaignId);
			rs1 = preparedStatement.executeQuery();
			while (rs1.next()) {
				offerIdList.add(rs1.getInt("offerId"));
			}
			rs1.close();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}

		return offerIdList;
	}

	/**
	 * Description of getRewardCodeForConsumerId()
	 * 
	 * @param consumerId
	 *            This is the consumer Id
	 * @return list of rewards issued to the consumer
	 */
	public List<Integer> getRewardCodeForConsumerId(int consumerId) {

		Connection con = null;
		ResultSet rs1;
		List<Integer> rewardCodes = new ArrayList<Integer>();

		try {
			con = ConnectionPool.getConnection();
			PreparedStatement preparedStatement = con
					.prepareStatement(SqlStatements.CHECK_FOR_UNIQUE_VOUCHER);
			preparedStatement.setInt(1, consumerId);
			rs1 = preparedStatement.executeQuery();
			while (rs1.next()) {
				rewardCodes.add(rs1.getInt("rewardcode"));
			}
			rs1.close();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}

		return rewardCodes;
	}

	public List<Integer> getRewardCodeForPhoneNumber(String phoneNumber) {

		Connection con = null;
		ResultSet rs1;
		List<Integer> rewardCodes = new ArrayList<Integer>();

		try {
			con = ConnectionPool.getConnection();
			PreparedStatement preparedStatement = con
					.prepareStatement(SqlStatements.CHECK_FOR_UNIQUE_VOUCHER_WEB_VOUCHER);
			preparedStatement.setString(1, phoneNumber);
			rs1 = preparedStatement.executeQuery();
			while (rs1.next()) {
				rewardCodes.add(rs1.getInt("rewardcode"));
			}
			rs1.close();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}

		return rewardCodes;
	}

	public List<Integer> getRewardCodeForDeviceId(String deviceID) {

		Connection con = null;
		ResultSet rs1;
		List<Integer> rewardCodes = new ArrayList<Integer>();

		try {
			con = ConnectionPool.getConnection();
			PreparedStatement preparedStatement = con
					.prepareStatement(SqlStatements.CHECK_FOR_UNIQUE_VOUCHER_DEVICE_ID);
			preparedStatement.setString(1, deviceID);
			rs1 = preparedStatement.executeQuery();
			while (rs1.next()) {
				rewardCodes.add(rs1.getInt("rewardcode"));
			}
			rs1.close();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}

		return rewardCodes;
	}

	/**
	 * Description of getRewardCodeForConsumerId()
	 * 
	 * @param dateOfBirth
	 *            This is the dateOfBirth to validate
	 * @param emailId
	 *            This is the emailId to validate
	 * @param consumerId
	 *            This is the consumer id whose email id and date of birth has
	 *            to be validated
	 * @return list of rewards issued to the consumer
	 */

	public int getimagevalidationsdao(int consumerID, String deviceId,
			int offerid, String imageType) {
		Connection con = null;
		CallableStatement stmt = null;
		int response = 0;
		try {
			con = ConnectionPool.getConnection();
			stmt = con.prepareCall("{ call GetImagevalidations(?,?,?,?,?) }");
			stmt.setInt(1, consumerID);

			stmt.setString(2, deviceId);
			stmt.setInt(3, offerid);
			if (imageType.equalsIgnoreCase("main")) {
				stmt.setInt(4, 1);
			} else {
				stmt.setInt(4, 2);
			}
			stmt.registerOutParameter(5, java.sql.Types.INTEGER);
			stmt.executeQuery();
			response = stmt.getInt(5);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return response;
	}

	public List<Imageresponse> getImageFromOfferId(int offerId, String imageType) {

		Connection con = null;
		ResultSet rs1;
		PreparedStatement preparedStatement = null;
		List<Imageresponse> imageList = new ArrayList<Imageresponse>();

		try {
			con = ConnectionPool.getConnection();
			if (imageType.equalsIgnoreCase("main")) {
				preparedStatement = con
						.prepareStatement(SqlStatements.GET_IMAGE_FROM_OFFERID_MAIN);
			} else {
				preparedStatement = con
						.prepareStatement(SqlStatements.GET_IMAGE_FROM_OFFERID_GHOST);
			}

			preparedStatement.setInt(1, offerId);
			rs1 = preparedStatement.executeQuery();
			if (!rs1.next()) {
				return null;
			} else {
				Imageresponse image = new Imageresponse();
				Blob imageBlob = null;

				if (imageType.equalsIgnoreCase("main")) {
					// logger.debug("geting main image");
					imageBlob = rs1.getBlob("imageurl");
				} else {
					// logger.debug("geting GhostImage image");
					imageBlob = rs1.getBlob("GhostImage");
				}
				if (imageBlob == null) {
					// logger.debug("Putting rs1 is null");
					return imageList = null;
				}
				byte[] imageBytes = imageBlob.getBytes(1,
						(int) imageBlob.length());
				image.setImage(imageBytes);
				// logger.debug("setting Image into byte");

				imageList.add(image);
			}
			rs1.close();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}

		return imageList;
	}

	public int updateDetailsDao(int consumerId, String deviceId,
			int deviceType, String phoneNumber, String emailId, String appId) {

		Connection con = null;
		CallableStatement stmt = null;
		int response = 0;
		try {
			con = ConnectionPool.getConnection();
			stmt = con.prepareCall("{ call updateDetails(?,?,?,?,?,?,?) }");
			stmt.setInt(1, consumerId);
			stmt.setString(2, deviceId);
			stmt.setInt(3, deviceType);
			stmt.setString(4, phoneNumber);
			stmt.setString(5, emailId);
			stmt.setString(6, appId);

			stmt.registerOutParameter(7, java.sql.Types.INTEGER);
			stmt.executeQuery();
			response = stmt.getInt(7);

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return response;

	}

	public ConsumerResult getDetailsDao(int consumerId) {
		ConsumerResult consumeridwithstatusmap = null;
		Connection con = null;
		CallableStatement cs = null;
		String OwnerPhoneNumber = null;
		int groupId = 0;
		try {
			con = ConnectionPool.getConnection();
			cs = con.prepareCall("{call db_mras.GetDetails(?,?,?)}");
			cs.setInt(1, consumerId);

			cs.registerOutParameter(2, java.sql.Types.INTEGER);
			cs.registerOutParameter(3, java.sql.Types.VARCHAR);
			cs.executeUpdate();
			groupId = cs.getInt(2);
			OwnerPhoneNumber = cs.getString(3);

			consumeridwithstatusmap = new ConsumerResult();
			/*
			 * if(groupId==0){
			 * consumeridwithstatusmap.setPhoneNumber(OwnerPhoneNumber); } else{
			 */
			consumeridwithstatusmap.setPhoneNumber(OwnerPhoneNumber);
			consumeridwithstatusmap.setGroupId(groupId);

			cs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return consumeridwithstatusmap;
	}

	public int webIssueVoucherDao(int consumerId, int offerId,
			String phoneNumber, String urlText, long outletCode,
			String rewardCode, int todayLimit, int weekLimit, String appId,String productName) {
		Connection con = null;
		CallableStatement cs = null;
		int result = 0;

		try {
			con = ConnectionPool.getConnection();
			cs = con.prepareCall("{call WebIssueVoucherProcedure(?,?,?,?,?,?,?,?,?,?,?)}");
			cs.setInt(1, consumerId);
			cs.setInt(2, offerId);
			cs.setString(3, phoneNumber);
			cs.setString(4, urlText);
			cs.setLong(5, outletCode);
			cs.setString(6, rewardCode);
			cs.setInt(7, todayLimit);
			cs.setInt(8, weekLimit);
			cs.setString(9, appId);
			cs.setString(10,productName);

			cs.registerOutParameter(11, java.sql.Types.INTEGER);
			cs.executeUpdate();
			result = cs.getInt(11);
			cs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return result;
	}

	public String consumerSmsInfo(String phoneNumber, String textenter) {
		Connection con = null;
		ResultSet rs1;
		String resultUrl = "No URL found";

		try {
			con = ConnectionPool.getConnection();
			PreparedStatement preparedStatement = con
					.prepareStatement(SqlStatements.TEXT_URL);
			preparedStatement.setString(1, textenter);
			rs1 = preparedStatement.executeQuery();
			while (rs1.next()) {
				resultUrl = rs1.getString("URL");
			}
			rs1.close();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}

		return resultUrl;
	}

	public ArrayList<Integer> offerRedemptionStatus(int offerId) {
		Connection con = null;
		// ArrayList<Integer> result = new ArrayList<Integer>();
		ArrayList<Integer> result = new ArrayList<Integer>();
		CallableStatement stmt = null;
		try {
			// get the connection..
			con = ConnectionPool.getConnection();

			// preapare the callable statement to call the procedure..
			stmt = con.prepareCall("{ call offerRedemptionCount(?,?,?,?)}");

			// set the parameters..
			stmt.setInt(1, offerId);
			stmt.registerOutParameter(2, java.sql.Types.INTEGER);
			stmt.registerOutParameter(3, java.sql.Types.INTEGER);
			stmt.registerOutParameter(4, java.sql.Types.INTEGER);
			stmt.executeQuery();

			if (stmt.getInt(2) == 403) {
				result.add(403);
				// result.add(403);
			} else {
				result.add(200);
				result.add(stmt.getInt(3));
				result.add(stmt.getInt(4));
				/*
				 * result.add(200); result.add(stmt.getInt(3));
				 * result.add(stmt.getInt(4));
				 */
			}
			// result = stmt.getInt(3);
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// return the connection to the pool..
			ConnectionPool.returnConnection(con);
		}
		return result;
	}

	public int[] countInNumbersForConsumer(int offerId, int consumerId) {
		Connection con = null;
		int[] result = new int[2];
		// use array[]
		CallableStatement stmt = null;
		try {
			// get the connection..
			con = ConnectionPool.getConnection();

			// preapare the callable statement to call the procedure..
			stmt = con.prepareCall("{ call countCheckInForConsumer(?,?,?,?)}");
			// check for same day same consumer
			// set the parameters..
			stmt.setInt(1, offerId);
			stmt.setInt(2, consumerId);
			stmt.registerOutParameter(3, java.sql.Types.INTEGER);
			stmt.registerOutParameter(4, java.sql.Types.INTEGER);
			stmt.executeQuery();

			if (stmt.getInt(3) == 403) {
				result[0] = 403;
			} else if (stmt.getInt(3) == 405) {
				result[0] = 405;
			} else {
				result[0] = 200;
				result[1] = stmt.getInt(4);
				// result.add(stmt.getInt(4));
			}
			// result = stmt.getInt(3);
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// return the connection to the pool..
			ConnectionPool.returnConnection(con);
		}
		return result;
	}

	public CampaignsModal getAllCampaignsDao(String appId) {

		Connection con = null;
		ResultSet rs1;
		ArrayList<CampaignObject> campaignList = new ArrayList<CampaignObject>();
		// change name
		CampaignsModal campaignList1 = new CampaignsModal();

		try {
			con = ConnectionPool.getConnection();

			PreparedStatement preparedStatement = con
					.prepareStatement(SqlStatements.GET__CAMPAIGN_DETAILS);
			preparedStatement.setString(1, appId);
			rs1 = preparedStatement.executeQuery();

			if (!rs1.next()) {
				return null;
			} else {
				rs1.previous();
			}

			while (rs1.next()) {
				CampaignObject camObject = new CampaignObject();
				camObject.setCampaignId(rs1.getInt("CampaignId"));
				camObject.setMaxRewardsAllotted(rs1
						.getInt("MaxRewardsAllotted"));
				camObject.setCampaignName(rs1.getString("CampaignName"));
				camObject.setPromoCode(rs1.getString("PromoCode"));
				camObject.setStartDate(rs1.getDate("StartDate"));
				camObject.setEndDate(rs1.getDate("EndDate"));

				Integer isSpecialCampaign = rs1.getInt("isSpecialDescription");
				if (isSpecialCampaign == null || isSpecialCampaign == 0) {
					camObject.setSpecialCampaign("No");
				} else {
					camObject.setSpecialCampaign("Yes");
					camObject.setSpecialCampaignText(rs1
							.getString("descriptionText"));
				}

				campaignList.add(camObject);
			}
			rs1.close();
			preparedStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		campaignList1.setCampaignList(campaignList);
		return campaignList1;
	}

	public List<Integer> getGiftedOffers(String phoneNumber) {

		Connection con = null;
		ResultSet rs1;
		List<Integer> offerList = new ArrayList<Integer>();
		try {
			con = ConnectionPool.getConnection();

			PreparedStatement preparedStatement = con
					.prepareStatement(SqlStatements.GET__GIFT_OFFERID);
			preparedStatement.setString(1, phoneNumber);
			rs1 = preparedStatement.executeQuery();

			while (rs1.next()) {
				Integer offerid = rs1.getInt("offerid");
				offerList.add(offerid);
			}
			rs1.close();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return offerList;
	}

	public int GiftingToFriends(String phoneNumber1, String phoneNumber2,
			String phoneNumber3, int consumerId, int offerId, String message) {
		Connection con = null;
		int result = 0;
		CallableStatement stmt = null;
		try {
			con = ConnectionPool.getConnection();
			stmt = con
					.prepareCall("{ call giftingToFriendsProcedure(?,?,?,?,?,?,?)}");
			stmt.setString(1, phoneNumber1);
			stmt.setString(2, phoneNumber2);
			stmt.setString(3, phoneNumber3);
			stmt.setInt(4, offerId);
			stmt.setInt(5, consumerId);
			stmt.setString(6, message);
			stmt.registerOutParameter(7, java.sql.Types.INTEGER);
			stmt.executeQuery();
			result = stmt.getInt(7);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return result;
	}

	public int getConsumerIdFromPhoneNumber(String phoneNumber, int offerid) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int consumerId;
		int rewardCode;
		int result = 200;
		try {
			con = ConnectionPool.getConnection();
			stmt = con
					.prepareStatement(SqlStatements.GET_CONSUMER_FROM_PHONENUMBER);
			stmt.setString(1, phoneNumber);
			rs = stmt.executeQuery();

			while (rs.next()) {
				consumerId = rs.getInt("consumerId");
				rewardCode = SchedulerDAO.generateRewardCode(consumerId);
				int responseDao = SchedulerDAO.issueVoucher(consumerId, offerid, null,
						rewardCode);
				if (logger.isDebugEnabled()) {
					logger.debug("Issued Voucher to : " + consumerId
							+ " reward Code : " + rewardCode
							+ "response from dao :" + responseDao);
				}
				result = responseDao;
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {

			ConnectionPool.returnConnection(con);

		}
		return result;

	}

	public boolean isThirdPartyVoucher(int offerId) {
		boolean result = true;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = ConnectionPool.getConnection();
			stmt = con
					.prepareStatement(SqlStatements.GET_OFFERID_FOR_THIRDPARTYVOUCHER);
			stmt.setInt(1, offerId);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = false;
				if (logger.isDebugEnabled()) {
					logger.debug("Reward code is auto ganrated");
				}
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {

			ConnectionPool.returnConnection(con);

		}

		return result;
	}

	public String getRewardsCodeFromThirdParty(int offerId) {
		String result = null;
		Connection con = null;
		CallableStatement stmt = null;
		try {
			con = ConnectionPool.getConnection();
			stmt = con.prepareCall("{ call thirdPartyVoucherCode(?,?,?)}");
			stmt.setInt(1, offerId);

			stmt.registerOutParameter(2, java.sql.Types.INTEGER);
			stmt.registerOutParameter(3, java.sql.Types.VARCHAR);
			stmt.executeQuery();
			if (stmt.getInt(2) == 200) {
				result = stmt.getString(3);
			}
			stmt.close();

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {

			ConnectionPool.returnConnection(con);

		}

		return result;

	}

}
