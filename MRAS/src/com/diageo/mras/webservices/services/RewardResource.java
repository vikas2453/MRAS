package com.diageo.mras.webservices.services;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.diageo.mras.cache.MrasCache;
import com.diageo.mras.scheduler.dao.SchedulerDAO;
import com.diageo.mras.webservices.dao.OccurrencepatternDao;
import com.diageo.mras.webservices.dao.RewardResourceDao;
import com.diageo.mras.webservices.init.MrasConstants;
import com.diageo.mras.webservices.init.PropertyReader;
import com.diageo.mras.webservices.modals.Consumer;
import com.diageo.mras.webservices.modals.ConsumerResult;
import com.diageo.mras.webservices.modals.CampaignsModal;
import com.diageo.mras.webservices.modals.NumberOfCheckInModal;
import com.diageo.mras.webservices.modals.Offer;
import com.diageo.mras.webservices.modals.OfferGiftModal;
import com.diageo.mras.webservices.modals.OfferRedemptionStatusModal;
import com.diageo.mras.webservices.responses.ConsumerUpdateResponse;
import com.diageo.mras.webservices.responses.Imageresponse;
import com.diageo.mras.webservices.responses.ResponseMRAS;

@Path("/Rewards")
/** This is a Web Services class for the Rewards.
 *
 * @author Infosys Limited
 * @version 1.0
 */
public class RewardResource {

	private static final Logger logger = Logger.getLogger(RewardResource.class
			.getName());
	private static final String pattern = "[+|0]?\\d{2,3}\\s?[-]?\\d{2,4}\\s?[-]?\\d{3,6}";
	private static final Format formatter = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");
	private static int TODAY_LIMIT_WEBISSUEVOUCHER = Integer
			.parseInt(PropertyReader
					.getPropertyValue(MrasConstants.TODAY_LIMIT_WEBISSUEVOUCHER));

	private static int WEEK_LIMIT_WEBISSUEVOUCHER = Integer
			.parseInt(PropertyReader
					.getPropertyValue(MrasConstants.WEEK_LIMIT_WEBISSUEVOUCHER));

	private static final Format formatter2 = new SimpleDateFormat("yyyy-MM-dd");
	private static final ValidationDateEmail validationDateEmail = new ValidationDateEmail();
	private static final RewardResourceDao rewardResourceDao = new RewardResourceDao();
	private static long timeToLive = 1800000;

	/**
	 * Description of generateRewardCode()
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @param ConsumerId
	 *            This is the Id of consumer
	 * @return generates a unique reward code for the consumer
	 */

	public static int generateRewardCode() {
		int randomNumber = 0;
		double randomNum = 0;
		try {
			SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
			// generate a random number

			while (true) {
				randomNum = prng.nextDouble() * 100000;
				randomNumber = (int) randomNum;

				if (randomNumber > 9999) {

					return randomNumber;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return randomNumber;
	}

	/**
	 * Description of issueVoucher()
	 * 
	 * @param Req
	 *            HttpServletRequest.
	 * @param consumer
	 *            Consumer.
	 * @return response various responses.
	 */

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/Issue")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response issueVoucher(@Context HttpServletRequest Req,
			Consumer consumer) {
		String appId = consumer.getAppId();
		int consumerId = consumer.getConsumerId();
		String deviceId = consumer.getDeviceId();
		int offerId = consumer.getOfferId();

		Response response = null;
		if (logger.isDebugEnabled()) {
			logger.debug("AppId : " + appId + "Consumer ID :" + consumerId
					+ " ,DeviceId: " + deviceId + ", offerId: " + offerId);
		}
		// Validating the AppId
		ResponseMRAS responseObj = validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(200).entity(responseObj).build();
			return response;
		} else {
			responseObj = new ResponseMRAS();
		}

		if (offerId == 0 || (consumerId == 0 && deviceId == null)) {
			responseObj.setResponseCode(404);
			responseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 404");
			}
			return response;
		}
		int responseDao = 0;

		Offer offer = (Offer) MrasCache.MCache.recover("Offer" + offerId);
		Date now = new Date();
		if (offer == null) {
			// not found in cache recive from db and update cache
			offer = rewardResourceDao.getOfferWithOfferid(offerId);

			if (offer == null) {
				responseObj.setResponseCode(420);
				responseObj.setResponseMessage(WebConstant.INVALID_OFFERID);
				response = Response.status(200).entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned 420");
				}
				return response;
			}

			timeToLive = offer.getRedemptionEndTime().getTimeInMillis()
					- now.getTime();
			MrasCache.MCache.admit("Offer" + offerId, offer, timeToLive,
					timeToLive);

		}

		if (offer != null && offer.getVoucherCodeGeneration() != null) {

			if (offer.getVoucherCodeGeneration().equalsIgnoreCase("MRAS")) {

				if (offer.isRedeemable()) {
					int rewardCode = SchedulerDAO
							.generateRewardCode(consumerId);
					responseDao = SchedulerDAO.issueVoucher(consumerId,
							offerId, deviceId, rewardCode);
				} else {
					int rewardCode = 11111;
					responseDao = SchedulerDAO.issueVoucher(consumerId,
							offerId, deviceId, rewardCode);
				}

				// generate the reward code..

			} else {
				responseDao = rewardResourceDao
						.issueVoucherForThirdPartyVoucher(consumerId, offerId,
								deviceId);
				if (responseDao == 500) {
					/*
					 * int rewardCode = generateRewardCode(consumerId);
					 * responseDao = rewardResourceDao.issueVoucher(consumerId,
					 * offerId, deviceId, rewardCode);
					 */
					responseObj
							.setResponseMessage(WebConstant.THIRD_PARTY_VOUCHERCODE_NOT_PRESENT);
					responseObj.setResponseCode(445);
					response = Response.status(Response.Status.OK)
							.entity(responseObj).build();
					if (logger.isDebugEnabled()) {
						   logger.debug("Response returned 445");
					}
					return response;
				}
			}
		} else {
			responseObj.setResponseCode(420);
			responseObj.setResponseMessage(WebConstant.INVALID_OFFERID);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 420");
			}
			return response;
		}
		// return responce according to result
		// logger.debug("Response recevied from DL layer for issue voucher for consumerId "
		// + consumerId + " is " + responseDao);

		// set the response parameters according to the issue voucher..
		if (responseDao == 405) {
			responseObj.setResponseCode(responseDao);
			responseObj.setResponseMessage(WebConstant.INVALID_CONSUMERID);
		} else if (responseDao == 406) {
			responseObj.setResponseCode(420);
			responseObj.setResponseMessage(WebConstant.INVALID_OFFERID);
		} else if (responseDao == 403) {
			responseObj.setResponseCode(419);
			responseObj.setResponseMessage(WebConstant.DEVICEID_ALREADY_EXISTS);
		} else if (responseDao == 435) {
			responseObj.setResponseCode(435);
			responseObj.setResponseMessage("Check In Required");
		} else if (responseDao == 437) {
			responseObj.setResponseCode(437);
			responseObj
					.setResponseMessage(WebConstant.OFFER_MAXIMUM_LIMIT_COMPLETE);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Response recevied from DL layer for issue voucher for consumerId "
						+ consumerId + " is " + responseDao);
			}
			responseObj.setResponseCode(responseDao);
			responseObj.setResponseMessage(WebConstant.SUCCESSFUL);
		}

		response = Response.status(200).entity(responseObj).build();
		if (logger.isDebugEnabled()) {
			   logger.debug("Response returned ");
		}
		return response;

	}

	/**
	 * Description of redeemReward()
	 * 
	 * @param req
	 *            HttpServletRequest.
	 * @param consumer
	 *            Consumer.
	 * @return response various responses.
	 */

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/Redeem")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response redeemReward(@Context HttpServletRequest Req,
			Consumer consumer) {

		// get the various values from the consumer.
		String appId = consumer.getAppId();
		int consumerId = consumer.getConsumerId();
		double latitude = consumer.getLatitude();
		double longitude = consumer.getLongitude();
		long outletCode = consumer.getOutletCode();
		int groupId = consumer.getGroupId();
		int offerId = consumer.getOfferId();
		int countryCode = consumer.getCountryCode();
		String rewardCode = consumer.getRewardCode();
		String productName = consumer.getProductName();
		if (logger.isDebugEnabled()) {
			logger.debug("Parameter received to redeem are consumerID "
					+ consumerId + ", AppId: " + appId + " ,Latitude: "
					+ latitude + ", Longitude: " + longitude
					+ ", OutletCode : " + outletCode + ",OfferId : " + offerId
					+ ",RewardCode : " + rewardCode + ", GroupId:" + groupId
					+ ", ContryCode:" + countryCode + " ,productName "
					+ productName);
		}
		Response response = null;

		ResponseMRAS responseObj = validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(200).entity(responseObj).build();
			return response;
		}
		responseObj = new ResponseMRAS();

		if (consumerId == 0 || (offerId == 0) || (countryCode == 0)
				|| (outletCode == 0) || (rewardCode == null)
				|| productName == null) {
			responseObj.setResponseCode(404);
			responseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 404");
			}
			return response;
		}

		int value = validateOffer(offerId, countryCode);

		// value =3 OfferId does not exist in the database.
		if (value == 3) {
			// logger.info("OfferId  is incorrect");
			responseObj.setResponseCode(403);
			responseObj.setResponseMessage(WebConstant.INVALID_OFFERID);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 403");
			}
			return response;
		}
		// offer is not redeemable
		else if (value == 4) {
			responseObj.setResponseCode(446);
			responseObj.setResponseMessage(WebConstant.OFFER_NON_REDEEMABLE);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 446");
			}
			return response;
		}
		// value =2 OfferId offer is valid and hence validating country Rules
		else if (value == 2) {
			/*
			 * logger.debug(
			 * "Validating country if offer can be redeem in country at the current time for "
			 * + countryCode);
			 */
			int valueCountryId = validateCountryCode(countryCode);
			// value =3 countyriD does not exist in the database.
			if (valueCountryId == 3) {
				// logger.debug("ContryCode :" + countryCode + "is incorrect ");
				responseObj.setResponseMessage("ContryCode :" + countryCode
						+ "is incorrect ");
				responseObj.setResponseCode(423);
				response = Response.status(200).entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned 423");
				}
				return response;
			}
			// value =2 countyriD is valid, let's validate rewardcode and all.
			else if (valueCountryId == 2) {
				// logger.debug("calling ValidateRewardVoucher ");
				int resultResponse;
				try {
					resultResponse = rewardResourceDao.ValidateRewardVoucher(
							consumerId, offerId, groupId, countryCode,
							outletCode, rewardCode, productName, appId);
				} catch (SQLException e) {

					if (logger.isDebugEnabled()) {
						logger.debug("SQLException from ValidateRewardVoucher "
								+ e.getMessage());
					}
					e.printStackTrace();
					responseObj.setResponseCode(430);
					responseObj
							.setResponseMessage("Some error in input parameter");

					response = Response.status(200)
							.type(MediaType.APPLICATION_JSON)
							.entity(responseObj).build();
					if (logger.isDebugEnabled()) {
						   logger.debug("Response returned 430");
					}
					return response;

				}

				switch (resultResponse) {

				case 200:
					logger.info("Response recevied  for consumerId "
							+ consumerId + " is " + resultResponse);
					responseObj.setResponseCode(resultResponse);
					if (logger.isDebugEnabled()) {
						logger.debug("Reward code: " + rewardCode
								+ "with respect to Offer " + offerId
								+ " for consumer" + consumerId + "is redeemed");
					}
					responseObj.setResponseMessage(WebConstant.SUCCESSFUL);
					break;

				case 401:
					responseObj.setResponseCode(resultResponse);
					responseObj
							.setResponseMessage("Invalid OutletCode, Offer cannot be Redeemed in this outlet");
					break;
				case 403:
					responseObj.setResponseCode(408);
					responseObj.setResponseMessage("GroupId  " + groupId
							+ " is not  valid");
					break;

				case 407:
					responseObj.setResponseCode(417);
					if (logger.isDebugEnabled()) {
						logger.debug("Reward code: " + rewardCode
								+ "with respect to Offer " + offerId
								+ " for consumer" + consumerId + "is not valid");
					}
					responseObj
							.setResponseMessage(WebConstant.INVALID_REWARD_CODE);
					break;

				case 410:
					responseObj.setResponseCode(429);
					responseObj
							.setResponseMessage("Consumer "
									+ consumerId
									+ " has already exceeded the voucher redemption limit for country "
									+ countryCode);
					break;
				case 428:
					responseObj
							.setResponseMessage("Current time is not valid to redeem the offerId");
					responseObj.setResponseCode(428);
					response = Response.status(200).entity(responseObj).build();

					break;

				case 435:
					responseObj.setResponseCode(435);
					responseObj.setResponseMessage("Check In Required");
					break;
				case 441:
					responseObj.setResponseCode(441);
					responseObj
							.setResponseMessage(WebConstant.OFFER_REDEMPTION_LIMIT_COMPLETE);
					break;

				case 442:
					responseObj.setResponseCode(442);
					responseObj
							.setResponseMessage(WebConstant.OUTLET_REDEMPTION_LIMIT_COMPLETE);
					break;
				case 443:
					responseObj.setResponseCode(443);
					responseObj
							.setResponseMessage(WebConstant.OFFER_PRODUCTNAME_NOT_VALID);
					break;

				}

				response = Response.status(200).entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					logger.debug("returning with response code :- "
							+ resultResponse);

				}
				
				return response;
				// else means that countriD was valid however offer can not be
				// redeemed at this time.
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Current time is not valid to redeem the offer for the country ");
				}
				responseObj
						.setResponseMessage("Current time is not valid to redeem the voucher for the country "
								+ countryCode);
				responseObj.setResponseCode(430);
				response = Response.status(200).entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned 430");
				}
				return response;
			}
			// offer time is not valid for this
		} else {

			Date now = new Date();
			Offer offer = (Offer) MrasCache.MCache.recover("Offer" + offerId);

			if (offer == null) {
				// not found in cache recive from db and update cache
				offer = rewardResourceDao.getOfferWithOfferid(offerId);
				if (offer == null) {
					responseObj.setResponseCode(403);
					responseObj.setResponseMessage(WebConstant.INVALID_OFFERID);
					response = Response.status(200).entity(responseObj).build();
					if (logger.isDebugEnabled()) {
						   logger.debug("Response returned 403");
					}
					return response;
				}
				timeToLive = offer.getRedemptionEndTime().getTimeInMillis()
						- now.getTime();
				MrasCache.MCache.admit("Offer" + offerId, offer, timeToLive,
						timeToLive);

			}
			responseObj
					.setResponseMessage("Current time is not valid to redeem the offerId "
							+ offerId
							+ ". Please try between "
							+ offer.getRedemptionStartTime().get(
									Calendar.HOUR_OF_DAY)
							+ ":"
							+ offer.getRedemptionStartTime().get(
									Calendar.MINUTE)
							+ " and "
							+ offer.getRedemptionEndTime().get(
									Calendar.HOUR_OF_DAY)
							+ ":"
							+ offer.getRedemptionEndTime().get(Calendar.MINUTE));
			responseObj.setResponseCode(428);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 428");
			}
			return response;
		}
	}

	/**
	 * Description of CaptureDeviceDetails()
	 * 
	 * @param req
	 *            HttpServletRequest.
	 * @param consumer
	 *            Consumer.
	 * @return Various Responses
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/CaptureDeviceDetails")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response captureDeviceDetails(
			@QueryParam("ConsumerId") int consumerId,
			@Context HttpServletRequest Req, Consumer consumer) {

		String appId = consumer.getAppId();
		// int consumerId = consumer.getConsumerId();
		String deviceId = consumer.getDeviceId();
		int deviceType = consumer.getDeviceType();
		int notificationId = consumer.getNotificationId();
		String phoneNumber = consumer.getPhoneNumber();
		String dateOfBirth = consumer.getDateOfBirth();
		String emailId = consumer.getEmailId();
		String gender = consumer.getGender();
		String firstName = consumer.getFirstName();
		String lastName = consumer.getLastName();
		int countryId = consumer.getCountryCode();

		Response response = null;
		if (logger.isDebugEnabled()) {
			logger.debug("Parameter received are consumerID " + consumerId
					+ ", deviceId: " + deviceId + " ,deviceType: " + deviceType
					+ ", appId: " + appId + ", notificationId : "
					+ notificationId + ", phoneNumber:" + phoneNumber
					+ " DateOfBirth: " + dateOfBirth + " Gender: " + gender
					+ ", EmailId: " + emailId + ", firstName: " + firstName
					+ ", lastName: " + lastName + ", countryId: " + countryId);
		}

		ResponseMRAS responseObj = validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(200).entity(responseObj).build();
			return response;
		} else {
			responseObj = new ResponseMRAS();
		}
		if (((consumerId == 0) || (deviceId == null)) || (deviceType == 0)
				|| (emailId == null) || (phoneNumber == null)
				|| (dateOfBirth == null) || (gender == null)
				|| (firstName == null) || (lastName == null) || countryId == 0) {

			responseObj.setResponseCode(404);
			responseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);

			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 404");
			}
			return response;
		}

		if (!phoneNumber.matches(pattern)) {

			responseObj.setResponseCode(425);
			responseObj.setResponseMessage("Wrong PhoneNumber format");
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 425");
			}
			return response;
		} else {
			Date birthDate = null;
			try {
				birthDate = (Date) formatter2.parseObject(dateOfBirth);
			} catch (ParseException e) {
				logger.info(WebConstant.DATE_FORMAT);
				e.printStackTrace();

				responseObj.setResponseCode(406);
				responseObj.setResponseMessage(WebConstant.DATE_FORMAT);

				response = Response.status(200).entity(responseObj).build();
				/*
				 * logger.debug("responseObj for the consumerId: " + consumerId
				 * + " is " + responseObj);
				 */
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned 406");
				}
				return response;
			}
			int resultResponse = rewardResourceDao.capturedeviceDetails(
					consumerId, deviceId, deviceType, appId, 1, phoneNumber,
					birthDate, gender, emailId, firstName, lastName, countryId);

			if (resultResponse == 200) {

				try {
					List<Integer> offerList = rewardResourceDao
							.getGiftedOffers(phoneNumber);

					if (offerList != null) {
						for (Integer offerid : offerList) {
							int rewardCode = SchedulerDAO
									.generateRewardCode(consumerId);
							 SchedulerDAO.issueVoucher(consumerId,
									offerid, null, rewardCode);

						}
					}
				} catch (Exception e) {
					e.printStackTrace();					
					responseObj
							.setResponseMessage("Some Unhandled Error has occurred");
					responseObj.setResponseCode(430);
					response = Response.status(200).entity(responseObj).build();
					if (logger.isDebugEnabled()) {
						   logger.debug("Response returned 430");
					}
					return response;
				}

				logger.info(WebConstant.SUCCESFUL_CAPTURED);

				responseObj.setResponseCode(200);
				responseObj.setResponseMessage(WebConstant.SUCCESFUL_CAPTURED);

				response = Response.status(200).entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned 200");
				}
				return response;

			}

			else if (resultResponse == 403) {

				responseObj.setResponseCode(418);
				responseObj.setResponseMessage(WebConstant.INVALID_DEVICEID);

				response = Response.status(200).entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned 418");
				}

				return response;
			} else if (resultResponse == 405) {

				responseObj.setResponseCode(405);
				responseObj.setResponseMessage(WebConstant.INVALID_CONSUMERID);

				response = Response.status(200).entity(responseObj).build();
				/*
				 * logger.debug("responseObj for the consumerId: " + consumerId
				 * + " is " + responseObj);
				 */
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned 405");
				}
				return response;
			} else if (resultResponse == 419) {
				responseObj.setResponseCode(419);
				responseObj
						.setResponseMessage(WebConstant.DEVICEID_ALREADY_EXISTS);
				response = Response.status(200).entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned 419");
				}
				return response;
			} else if (resultResponse == 441) {

				responseObj.setResponseCode(441);
				responseObj
						.setResponseMessage(WebConstant.INVALID_PHONENUMBER_441);

				response = Response.status(200).entity(responseObj).build();
				/*
				 * logger.debug("responseObj for the consumerId: " + consumerId
				 * + " is " + responseObj);
				 */
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned 441");
				}
				return response;
			}

		}
		if (logger.isDebugEnabled()) {
			   logger.debug("Response returned ");
		}
		return response;

	}

	/**
	 * Description of getConsumerUpdates()
	 * 
	 * @param ConsumerId
	 *            This is the Id of consumer
	 * @return returns all rewards applicable to the consumer
	 */

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/GetConsumerUpdates")
	public Response getConsumerUpdates(@Context HttpServletRequest req,
			@QueryParam("ConsumerId") int consumerId,
			@QueryParam("AppId") String appId,
			@QueryParam("LastRefresh") String lastRefresh,
			@QueryParam("DeviceId") String deviceId) {

		Response response;
		if (logger.isDebugEnabled()) {
			   logger.info("Parameter Recive for GetConsumerUpdates consumerId "+consumerId+", appId "+appId+"' deviceId "+deviceId);
			}
		// System.out.println("Perameter recive Appid"+appId+" consumerId "+consumerId+" deviceId "+deviceId);

		// logger.debug(" Authorization header from request is:- "+req.getHeader("Authorization"));

		if (consumerId != 0) {
			try {
				int resultOfConsumerId = validationDateEmail
						.validateConsumer(req);
				logger.info(" Checking Validation EmailId and DateOfBirth");
				if (resultOfConsumerId == 0 || consumerId != resultOfConsumerId) {
					if (logger.isDebugEnabled()) {
						logger.debug(" recive consumerId from DB :- "
								+ resultOfConsumerId + " ConsumerId enter"
								+ consumerId);
					}
					response = Response.status(411)
							.entity(WebConstant.INVALID_CRDENTIALS).build();
					return response;
				}
			} catch (Exception e) {
				e.printStackTrace();
				response = Response.status(411)
						.entity(WebConstant.INVALID_CRDENTIALS).build();
				return response;

			}
		}
		Date date = null;

		if (lastRefresh != null) {
			try {
				date = (Date) formatter.parseObject(lastRefresh);

			} catch (ParseException e) {
				ResponseMRAS responseObj = new ResponseMRAS();
				logger.info(WebConstant.DATE_FORMAT);
				e.printStackTrace();
				responseObj.setResponseCode(406);
				responseObj.setResponseMessage(WebConstant.DATE_FORMAT);

				response = Response.status(200).entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					   logger.info("Response recevied "+responseObj.getResponseCode());
					}
				return response;
			}
		}

		/*
		 * logger.debug("paremeters recevied are consumerID " + consumerId +
		 * ", appId: " + appId + ", lastRefresh: " + date);
		 */

		ResponseMRAS responseObj = validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned Invalid appid");
			}
			return response;
		}
		if ((consumerId == 0 && deviceId == null)) {
			responseObj = new ResponseMRAS();
			responseObj.setResponseCode(404);
			responseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 404");
			}
			return response;
		} else {
			responseObj = new ResponseMRAS();
			try {
				ConsumerUpdateResponse consumerUpdateResponse = rewardResourceDao
						.getConsumerUpdatesDao(consumerId, date, deviceId,
								appId);

				if (consumerUpdateResponse == null) {
					responseObj.setResponseCode(405);

					responseObj
							.setResponseMessage("Invalid DeviceId or ConsumerID");
					response = Response.status(200).entity(responseObj).build();
					if (logger.isDebugEnabled()) {
						   logger.debug("Response returned 405");
					}
					return response;
				}
				java.util.Date dt = new java.util.Date();
				consumerUpdateResponse.setLastRefresh(formatter.format(dt));

				if (consumerUpdateResponse.getRewardList() == null
						|| consumerUpdateResponse.getOfferList() == null
						|| consumerUpdateResponse.getMessageList() == null) {
					responseObj = new ResponseMRAS();

					response = Response.status(200)
							.entity(consumerUpdateResponse).build();
					if (logger.isDebugEnabled()) {
						   logger.info("Response recevied 200");
					}

					return response;
				}

				if (consumerUpdateResponse.getRewardList().isEmpty()
						&& consumerUpdateResponse.getOfferList().isEmpty()
						&& consumerUpdateResponse.getMessageList().isEmpty()) {
					responseObj = new ResponseMRAS();
					responseObj.setResponseCode(407);
					responseObj
							.setResponseMessage("No offer found for consumerId :"
									+ consumerId);
					logger.info("No offer found for consumerId :" + consumerId);
					response = Response.status(200).entity(responseObj).build();
					if (logger.isDebugEnabled()) {
						   logger.debug("Response returned 407");
					}
					return response;
				}

				// creating a new date object, to set the last refresh for this
				// consumer to be sent in the response.

				response = Response.status(200).entity(consumerUpdateResponse)
						.build();

			} catch (Exception e) {
				e.printStackTrace();
				responseObj.setResponseCode(430);
				responseObj.setResponseMessage(WebConstant.UNHANDLED_ERROR);

				response = Response.status(200).entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned 430");
				}
				return response;
			}
		}
		if (logger.isDebugEnabled()) {
			   logger.debug("Response returned ");
		}
		return response;
	}

	/**
	 * Description of validateOffer()
	 * 
	 * @param offerId
	 *            This is the offer Id
	 * @param countryId
	 *            This is the countryId
	 * @return returns an integer indicating offer is valid or not with country
	 *         settings for counrtyid
	 */

	public int validateOffer(int offerId, int countryId) {
		Date now = new Date();
		Calendar currentCalender = Calendar.getInstance();
		Integer responceValue = (Integer) MrasCache.MCache.recover("Redeem"
				+ offerId);

		if (responceValue == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("offerId: "
						+ offerId
						+ " not found in the cache and hence putting it in the cache"
						+ ("Redeem" + offerId));
			}
			// checking from the cache
			Offer offer = (Offer) MrasCache.MCache.recover("Offer" + offerId);

			if (offer == null) {
				// not found in cache recive from db and update cache
				offer = rewardResourceDao.getOfferWithOfferid(offerId);
				if (offer == null) {
					return 3;
				}

				MrasCache.MCache.admit("Offer" + offerId, offer, timeToLive,
						timeToLive);

			}
			// check if offer is valid
			if (logger.isDebugEnabled()) {
				logger.debug("Offer timings  are  " + "\n" + "StartDate : "
						+ offer.getRedemptionStartTime().getTime()
						+ "\nEnd Time: "
						+ offer.getRedemptionEndTime().getTime());
			}

			if (offer.isRedeemable()) {
				if (offer.getRedemptionStartTime().before(currentCalender)
						&& offer.getRedemptionEndTime().after(currentCalender)

				) {

					logger.info("all validations related to offer timings redemption are ok for offerId");
					// it confirm that
					if (offer.getAlcoholic() == "YES") {
						// logger.debug("Enter in where reward type is 1 ");
						List<Integer> AlcoholicVoucherCountryList = (List<Integer>) MrasCache.MCache
								.recover("Alcoholic");
						if (AlcoholicVoucherCountryList == null) {

							AlcoholicVoucherCountryList = rewardResourceDao
									.getAlcoholicVoucherCountryList();
							MrasCache.MCache.admit("Alcoholic",
									AlcoholicVoucherCountryList, timeToLive,
									timeToLive);
						}
						if (AlcoholicVoucherCountryList.contains(countryId)) {
							// Country does n't support the Alcoholic voucher
							if (logger.isDebugEnabled()) {
								logger.debug("CountryId "
										+ countryId
										+ "does n't support the Alcoholic voucher");
							}

							MrasCache.MCache.admit("Redeem" + offerId, 1,
									timeToLive, timeToLive);
							return 1;
						}
					} else {
						// logger.debug("Enter in where reward type is 0 ");
						List<Integer> NonAlcoholicVoucherCountryList = (List<Integer>) MrasCache.MCache
								.recover("NonAlcoholic");
						if (NonAlcoholicVoucherCountryList == null) {
							// logger.debug("NonAlcoholicVoucherCountryList not found in the catche and hence putting it in the catche ");
							NonAlcoholicVoucherCountryList = rewardResourceDao
									.getNonAlcoholicVoucherCountryList();
							MrasCache.MCache.admit("NonAlcoholic",
									NonAlcoholicVoucherCountryList, timeToLive,
									timeToLive);
						}
						if (NonAlcoholicVoucherCountryList.contains(countryId)) {
							// Country does n't support the NonAlcoholic voucher
							if (logger.isDebugEnabled()) {
								logger.debug("CountryId "
										+ countryId
										+ "does n't support the NonAlcoholic voucherr");
							}
							MrasCache.MCache.admit("Redeem" + offerId, 1,
									timeToLive, timeToLive);
							return 1;
						}
					}

					int occurancePatternId = offer
							.getRedemptionDaysOccurancePatternId();

					if (occurancePatternId == 1) {
						/*
						 * logger.info("no verification of occurrence  pattern");
						 * MrasCache.MCache.admit("Redeem" + offerId, 2,
						 * timeToLive, timeToLive);
						 */
						// changes start
						long endTimeToLive = offer.getRedemptionEndTime()
								.getTimeInMillis() - now.getTime();
						if (endTimeToLive < 0) {
							endTimeToLive = timeToLive;
						}
						MrasCache.MCache.admit("Redeem" + offerId, 2,
								endTimeToLive, timeToLive);
						// changes end

						return 2;
					}

					// checcking in the cache
					Boolean occurreneValue = (Boolean) MrasCache.MCache
							.recover("occurancePattern" + occurancePatternId);
					if (occurreneValue == null) {
						// not found in cache receive from db and update cache
						if (logger.isDebugEnabled()) {
							logger.debug("occurancePatternId : "
									+ occurancePatternId
									+ " not found in the cache and hence putting it in the cache");
						}

						occurreneValue = OccurrencepatternDao
								.occurrencePattern(occurancePatternId);
						MrasCache.MCache.admit("occurancePattern"
								+ occurancePatternId, occurreneValue,
								timeToLive, timeToLive);
					}
					if (!occurreneValue) {

						MrasCache.MCache.admit("Redeem" + offerId, 1,
								timeToLive, timeToLive);
						return 1;
					}
					// changes start
					long endTimeToLive = offer.getRedemptionEndTime()
							.getTimeInMillis() - now.getTime();
					if (endTimeToLive < 0) {
						endTimeToLive = timeToLive;
					}
					MrasCache.MCache.admit("Redeem" + offerId, 2,
							endTimeToLive, timeToLive);
					// changes end
					return 2;
				} else {
					// changes start
					long startTimeToLive = offer.getRedemptionStartTime()
							.getTimeInMillis() - now.getTime();

					if (startTimeToLive < 0) {
						startTimeToLive = offer.getRedemptionStartTime()
								.getTimeInMillis()
								+ 3600
								* 24000
								- now.getTime();

					}
					logger.debug("putting into cache in redeem in milisecound "
							+ startTimeToLive);
					MrasCache.MCache.admit("Redeem" + offerId, 1,
							startTimeToLive, timeToLive);
					// changes end
					return 1;
				}
			} else {

				MrasCache.MCache.admit("Redeem" + offerId, 4, timeToLive,
						timeToLive);
				return 4;
			}
		}
		if(logger.isDebugEnabled()){
		logger.debug("found in the cache, value for offerId " + offerId
				+ " is " + responceValue);
		}
		return responceValue;

	}

	/**
	 * Description of validateCountryCode()
	 * 
	 * @param countryid
	 *            This is the countryid Id to validate
	 * @return returns an integer indicating country id valid or not
	 */

	public int validateCountryCode(int countryid) {
		// checking in cache

		Integer value = (Integer) MrasCache.MCache.recover("country"
				+ countryid);
		if (value == null) {

			value = rewardResourceDao.validateCountryCodedao(countryid);
			MrasCache.MCache.admit("country" + countryid, value, timeToLive,
					timeToLive);
			return value;
		}
		// / logger.debug("found in the catche, value for contryid " + value);
		return value;

	}

	/**
	 * Description of validateAppId()
	 * 
	 * @param appId
	 *            This is the app Id to be validated
	 * @return returns response if appid valid or not
	 */
	public static ResponseMRAS validateAppId(String appId) {
		// logger.debug("Checking AppId " + appId);
		ResponseMRAS repsonseObj = null;
		// checking appID
		if (appId == null) {
			repsonseObj = new ResponseMRAS();
			repsonseObj.setResponseCode(404);
			repsonseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			return repsonseObj;
		}
		// checking in cache
		Boolean value = (Boolean) MrasCache.MCache.recover(appId);
		if (value == null) {
			// not found in cache receive from db and update cache
			value = rewardResourceDao.validateAppIDDao(appId);
			if (logger.isDebugEnabled()) {
				logger.debug("AppId "
						+ appId
						+ " not found in the cache and hence putting it in the cache with value "
						+ value);
			}
			MrasCache.MCache.admit(appId, value);
		}
		// response according to value
		if (value) {
			// logger.debug("AppId " + appId + " is valid");
			return repsonseObj;
		} else {
			repsonseObj = new ResponseMRAS();
			repsonseObj.setResponseCode(402);
			if (logger.isDebugEnabled()) {
				logger.debug("AppId " + appId + " not found in the database");
			}
			repsonseObj.setResponseMessage(WebConstant.INVALID_APPID);
			return repsonseObj;
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/GetImage")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response Getimage(@Context HttpServletRequest req,
			@QueryParam("ConsumerId") int consumerId,
			@QueryParam("AppId") String appId,
			@QueryParam("DeviceId") String deviceId,
			@QueryParam("ImageType") String imageType,
			@QueryParam("OfferId") int offerId) {

		{

			Response response = null;
			if (logger.isDebugEnabled()) {
				logger.debug("AppId : " + appId + "Consumer ID :" + consumerId
						+ " ,DeviceId: " + deviceId + ", offerId: " + offerId
						+ ", imageType:" + imageType);
			}

			if (consumerId != 0) {
				try {
					int resultOfConsumerId = validationDateEmail
							.validateConsumer(req);
					// logger.debug(" Checking Validation EmailId and DateOfBirth");
					if (resultOfConsumerId == 0
							|| consumerId != resultOfConsumerId) {

						response = Response.status(411)
								.entity(WebConstant.INVALID_CRDENTIALS).build();
						if (logger.isDebugEnabled()) {
							   logger.debug("Response returned 411");
						}
						return response;
					}
				} catch (Exception e) {
					e.printStackTrace();
					response = Response.status(411)
							.entity(WebConstant.INVALID_CRDENTIALS).build();
					if (logger.isDebugEnabled()) {
						   logger.debug("Response returned 411");
					}
					return response;

				}
			}

			// Validating the AppId
			ResponseMRAS responseObj = validateAppId(appId);
			if (responseObj != null) {
				response = Response.status(200).entity(responseObj).build();
				return response;
			} else {
				responseObj = new ResponseMRAS();
			}

			if (imageType == null || offerId == 0
					|| (consumerId == 0 && (deviceId == null))) {
				responseObj.setResponseCode(404);
				responseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);
				response = Response.status(200).entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned 404");
				}
				return response;
			}
			if (!(imageType.equalsIgnoreCase("main") || imageType
					.equalsIgnoreCase("ghost"))) {
				responseObj.setResponseCode(445);
				responseObj.setResponseMessage("ImageType is not correct");
				response = Response.status(200).entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned 445");
				}
				return response;
			}
			int responseDao = 200;

			if (imageType.equalsIgnoreCase("main")) {
				responseDao = rewardResourceDao.getimagevalidationsdao(
						consumerId, deviceId, offerId, imageType);
			}

			if (responseDao == 200) {
				// logger.debug("validation is done:-");

				List<Imageresponse> imagelist = (List<Imageresponse>) MrasCache.MCache
						.recover(imageType + offerId);

				if (imagelist == null) {
					if (logger.isDebugEnabled()) {
					logger.debug("no data found in cache putting it into the cache with offerid:-"
							+ offerId);
					}
					imagelist = rewardResourceDao.getImageFromOfferId(offerId,
							imageType);
					if (imagelist == null) {
						if (logger.isDebugEnabled()) {
						logger.debug("imagelist is null");
						}
						responseObj
								.setResponseMessage(WebConstant.OFFER_IMAGE_NOT_AVAILABLE);
						responseObj.setResponseCode(427);
						response = Response.status(200).entity(responseObj)
								.build();
						if (logger.isDebugEnabled()) {
							   logger.debug("Response returned 427");
						}
						return response;

					}
					int mb = 1048576;
					// int mb=1024;
					Runtime runtime = Runtime.getRuntime();
					if (logger.isDebugEnabled()) {
					logger.debug("current heap size totall Memory:"
							+ runtime.totalMemory() / mb);
					}
					if (logger.isDebugEnabled()) {
					logger.debug("totall max Memory:" + runtime.maxMemory()
							/ mb);
					}

					long totalFreeMemory = (runtime.maxMemory()
							- runtime.totalMemory() + runtime.freeMemory())
							/ mb;
					if (logger.isDebugEnabled()) {
					logger.debug("free Memory total:" + totalFreeMemory);
					}
					if (totalFreeMemory > 256) {
						// System.out.println("putting into the cache as free memory is greater than 256 MB");
						MrasCache.MCache.admit(imageType + offerId, imagelist);
					}/*
					 * else{ //System.out.println(
					 * "putting into the cache as free memory is less than 256 MB"
					 * ); }
					 */
				}

				GenericEntity<List<Imageresponse>> listentity = new GenericEntity<List<Imageresponse>>(
						imagelist) {
				};
				response = Response.status(200)
						.type(MediaType.APPLICATION_JSON).entity(listentity)
						.build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Response recevied 200");
					}
				return response;

			}

			else {

				if (responseDao == 405) {
					responseObj.setResponseCode(405);
					responseObj
							.setResponseMessage(WebConstant.INVALID_CONSUMERID);

					response = Response.status(200).entity(responseObj).build();
					if (logger.isDebugEnabled()) {
						   logger.debug("Response returned 405");
					}
					return response;

				} else if (responseDao == 417) {
					responseObj.setResponseCode(427);
					responseObj
							.setResponseMessage(WebConstant.OFFER_IMAGE_NOT_AVAILABLE);
					response = Response.status(200).entity(responseObj).build();
					if (logger.isDebugEnabled()) {
						   logger.debug("Response returned 427");
					}
					return response;
				} else if (responseDao == 418) {
					responseObj.setResponseCode(418);
					responseObj
							.setResponseMessage(WebConstant.INVALID_DEVICEID);
					response = Response.status(200).entity(responseObj).build();
					if (logger.isDebugEnabled()) {
						   logger.debug("Response returned 418");
					}
					return response;
				} else if (responseDao == 420) {
					responseObj.setResponseCode(420);
					responseObj
							.setResponseMessage("No offer found for this consumerId");
					response = Response.status(200).entity(responseObj).build();
					if (logger.isDebugEnabled()) {
						   logger.debug("Response returned 420");
					}
					return response;
				}

			}
			
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned ");
			}
			return response;

		}

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/UpdateConsumerDetails")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response UpdateConsumerDetails(@Context HttpServletRequest req,
			Consumer consumer, @QueryParam("ConsumerId") int consumerId) {

		int deviceType = consumer.getDeviceType();
		String phoneNumber = consumer.getPhoneNumber();
		String emailId = consumer.getEmailId();
		String appId = consumer.getAppId();
		// int consumerId = consumer.getConsumerId();
		String deviceId = consumer.getDeviceId();
		// int offerId = consumer.getOfferId();

		Response response = null;
		if (logger.isDebugEnabled()) {
			logger.debug("Consumer ID :" + consumerId + " ,DeviceId: "
					+ deviceId + " ,deviceType: " + deviceType
					+ " ,phoneNumber: " + phoneNumber + " ,emailId: " + emailId
					+ " ,appId: " + appId);
		}
		// Validating the AppId

		ResponseMRAS responseObj = null;

		responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(200).entity(responseObj).build();
			return response;

		}
		responseObj = new ResponseMRAS();

		if (consumerId == 0) {
			responseObj.setResponseCode(404);
			responseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 404");
			}
			return response;
		}

		try {
			int resultOfConsumerId = validationDateEmail.validateConsumer(req);
			logger.info(" Checking Validation EmailId and DateOfBirth");
			if (resultOfConsumerId == 0 || consumerId != resultOfConsumerId) {
				if (logger.isDebugEnabled()) {
					logger.debug(" recive consumerId from DB :- "
							+ resultOfConsumerId + " ConsumerId enter"
							+ consumerId);
				}
				response = Response.status(411)
						.entity(WebConstant.INVALID_CRDENTIALS).build();
				return response;
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = Response.status(411)
					.entity(WebConstant.INVALID_CRDENTIALS).build();
			return response;

		}

		if (phoneNumber != null && (!phoneNumber.matches(pattern))) {

			responseObj.setResponseCode(425);
			responseObj.setResponseMessage(WebConstant.WRONG_PHONENUMBER_425);
			response = Response.status(200).entity(responseObj).build();
			
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned WRONG_PHONENUMBER_425");
			}
			return response;
		}

		Integer responseDao = rewardResourceDao.updateDetailsDao(consumerId,
				deviceId, deviceType, phoneNumber, emailId, appId);

		if (responseDao == 200) {
			responseObj.setResponseCode(200);
			responseObj.setResponseMessage(WebConstant.SUCCESSFUL);
			response = Response.status(200).entity(responseObj).build();
		} else if (responseDao == 405) {

			responseObj.setResponseCode(405);
			responseObj.setResponseMessage(WebConstant.INVALID_CONSUMERID);
			response = Response.status(200).entity(responseObj).build();

		} else if (responseDao == 440) {

			responseObj.setResponseCode(440);
			responseObj
					.setResponseMessage("PhoneNumber is already registered with given AppId");
			response = Response.status(200).entity(responseObj).build();

		} else {
			responseObj.setResponseCode(430);
			responseObj.setResponseMessage("Some error in input parameter");
			response = Response.status(200).entity(responseObj).build();
		}
		if (logger.isDebugEnabled()) {
			   logger.info("Response returned "+responseObj.getResponseCode());
			}
		return response;

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/GetConsumerDetails")
	public Response GetConsumerDetails(@Context HttpServletRequest req,
			@QueryParam("AppId") String appId,
			@QueryParam("ConsumerId") int consumerId) {

		Response response = null;
		if (logger.isDebugEnabled()) {
			logger.debug("Consumer ID :" + consumerId + " ,appId: " + appId);
		}
		// Validating the AppId

		ResponseMRAS responseObj = null;

		// validating AppId first
		responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(200).entity(responseObj).build();
			return response;

		}
		responseObj = new ResponseMRAS();

		if (consumerId == 0) {
			responseObj.setResponseCode(404);
			responseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 404");
			}
			return response;
		}

		try {
			int resultOfConsumerId = validationDateEmail.validateConsumer(req);
			logger.debug(" Checking Validation EmailId and DateOfBirth");
			if (resultOfConsumerId == 0 || consumerId != resultOfConsumerId) {
				if (logger.isDebugEnabled()) {
					logger.debug(" recive consumerId from DB :- "
							+ resultOfConsumerId + " ConsumerId enter"
							+ consumerId);
				}
				response = Response.status(411)
						.entity(WebConstant.INVALID_CRDENTIALS).build();
				return response;
			}

		} catch (Exception e) {
			e.printStackTrace();
			response = Response.status(411)
					.entity(WebConstant.INVALID_CRDENTIALS).build();
			return response;

		}

		ConsumerResult consumerObject = rewardResourceDao
				.getDetailsDao(consumerId);
		if (consumerObject != null) {
		JSONObject myObject = new JSONObject();

		try {

			if (consumerObject.getGroupId() != 0) {
				myObject.put("GroupId", consumerObject.getGroupId());
			}
			if (consumerObject.getPhoneNumber() != null) {
				myObject.put("phoneNumber", consumerObject.getPhoneNumber());
			}

		} catch (JSONException ex) {
			if (logger.isInfoEnabled()) {
				   logger.info("JSONException occured : "+ex.getMessage());
			}
			ex.printStackTrace();
		}

		response = Response.status(200).type(MediaType.APPLICATION_JSON)
				.entity(myObject).build();
		
		}

		else {
			
			responseObj
			.setResponseMessage("Some Unhandled Error has occurred");
			responseObj.setResponseCode(430);
			response = Response.status(200).entity(responseObj).build();
							
		}
		if (logger.isDebugEnabled()) {
			   logger.debug("Response returned 200");
		}
		return response;

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/WebIssueVoucher")
	/** Description of searchForNearbyOutlets() .
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @param lattitude       	This is the lattitude.
	 * @param longitude       	This is the longitude.
	 * @param radius        	This is the distance.
	 * @param appId        		This is the appId.
	 * @param campai	gnId        This is the campaignId.
	 * @return                	A list of nearby outlets
	 */
	public Response webIssueVoucher(@Context HttpServletRequest Req,
			Consumer consumer) {

		String appId = consumer.getAppId();
		int consumerId = consumer.getConsumerId();
		String phoneNumber = consumer.getPhoneNumber();
		int offerId = consumer.getOfferId();

		String URL = consumer.getURL();
		long outletCode = consumer.getOutletCode();
		String productName = consumer.getProductName();
		ResponseMRAS responseobj = null;
		Response response = null;

		if (logger.isDebugEnabled()) {
			logger.info("Parameter received are  AppId" + appId
					+ ", consumerId: " + consumerId + " ,phoneNumber: "
					+ phoneNumber + ", offerId: " + offerId);

		}

		ResponseMRAS responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			return response;
		}

		responseobj = new ResponseMRAS();

		// if (phoneNumber == null || consumerId =rr= 0 || offerId == 0) {
		// //remove consumerId
		if (phoneNumber == null || offerId == 0 || URL == null
				|| consumerId == 0) {
			responseobj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			responseobj.setResponseCode(404);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 404");
			}
			return response;
		}

		if (!phoneNumber.matches(pattern)) {

			responseobj.setResponseCode(425);
			responseobj.setResponseMessage(WebConstant.WRONG_PHONENUMBER_425
					+ ":- " + phoneNumber);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 425");
			}
			return response;
		}
		// fetch the list of nearby outlets..
		Offer offer = (Offer) MrasCache.MCache.recover("Offer" + offerId);
		Date now = new Date();
		if (offer == null) {
			// not found in cache recive from db and update cache
			offer = rewardResourceDao.getOfferWithOfferid(offerId);

			if (offer == null) {
				responseobj.setResponseCode(420);
				responseobj.setResponseMessage(WebConstant.INVALID_OFFERID);
				response = Response.status(Response.Status.OK)
						.entity(responseobj).build();

				if (logger.isDebugEnabled()) {
					logger.debug("Invalid offer id , response :- 420");
				}
				return response;
			}

			timeToLive = offer.getRedemptionEndTime().getTimeInMillis()
					- now.getTime();
			MrasCache.MCache.admit("Offer" + offerId, offer, timeToLive,
					timeToLive);

		}

		String rewardCode = null;
		int resultCode = 0;

		if (offer != null && offer.getVoucherCodeGeneration() != null) {

			if (offer.getVoucherCodeGeneration().equalsIgnoreCase("MRAS")) {

				if (offer.isRedeemable()) {
					rewardCode = String.valueOf(generateRewardCode());
					resultCode = rewardResourceDao.webIssueVoucherDao(
							consumerId, offerId, phoneNumber, URL, outletCode,
							rewardCode, TODAY_LIMIT_WEBISSUEVOUCHER,
							WEEK_LIMIT_WEBISSUEVOUCHER, appId, productName);
				} else {
					rewardCode = "11111";
					resultCode = rewardResourceDao.webIssueVoucherDao(
							consumerId, offerId, phoneNumber, URL, outletCode,
							rewardCode, TODAY_LIMIT_WEBISSUEVOUCHER,
							WEEK_LIMIT_WEBISSUEVOUCHER, appId, productName);
				}

				// generate the reward code..

			} else {
				String result = rewardResourceDao
						.getRewardsCodeFromThirdParty(offerId);
				if (result != null) {
					resultCode = rewardResourceDao.webIssueVoucherDao(
							consumerId, offerId, phoneNumber, URL, outletCode,
							result, TODAY_LIMIT_WEBISSUEVOUCHER,
							WEEK_LIMIT_WEBISSUEVOUCHER, appId, productName);
				} else {
					responseobj
							.setResponseMessage(WebConstant.THIRD_PARTY_VOUCHERCODE_NOT_PRESENT);
					responseobj.setResponseCode(445);
					response = Response.status(Response.Status.OK)
							.entity(responseobj).build();

					if (logger.isDebugEnabled()) {
						logger.debug("Reponse returned THIRD_PARTY_VOUCHERCODE_NOT_PRESENT , response :- 445");
					}
					return response;
				}
			}
		} else {

			responseobj.setResponseCode(420);
			responseobj.setResponseMessage(WebConstant.INVALID_OFFERID);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				logger.debug("Reponse returned INVALID_OFFERID , response :- 420");
			}
			return response;
		}

		switch (resultCode) {
		case 200: {
			Thread t = new Thread(new ExecuteSmsService(phoneNumber, URL,
					rewardCode, outletCode, offerId));
			t.setName("SMSThread");
			t.start();
			responseobj.setResponseMessage(WebConstant.SMS_SUCESS_Voucher);
			if (logger.isDebugEnabled()) {
			logger.debug("Success response return" + consumerId);
			}
			responseobj.setResponseCode(200);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			return response;
		}
		case 420: {
			responseobj.setResponseMessage(WebConstant.INVALID_OFFERID);
			responseobj.setResponseCode(420);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				logger.debug("Reponse returned INVALID_OFFERID , response :- 420");
			}
			return response;
		}
		case 432: {
			responseobj.setResponseMessage(WebConstant.ISSUE_LIMIT_TODAY);
			responseobj.setResponseCode(432);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				logger.debug("Reponse returned ISSUE_LIMIT_TODAY , response :- 432");
			}
			return response;
		}

		case 433: {
			responseobj.setResponseMessage(WebConstant.ISSUE_LIMIT_WEEK);
			responseobj.setResponseCode(433);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				logger.debug(" Reponse returned ISSUE_LIMIT_WEEK , response :- 433");
			}
			return response;
		}
		case 437: {
			responseobj
					.setResponseMessage(WebConstant.OFFER_MAXIMUM_LIMIT_COMPLETE);
			responseobj.setResponseCode(437);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				logger.debug("Reponse returned OFFER_MAXIMUM_LIMIT_COMPLETE , response :- 437");
			}
			return response;
		}
		case 414: {
			responseobj.setResponseMessage(WebConstant.INVALID_OUTLET_CODE);
			responseobj.setResponseCode(414);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				logger.debug("Reponse returned INVALID_OUTLET_CODE , response :- 414");
			}
			return response;
		}
		case 447: {

			responseobj.setResponseCode(447);
			responseobj.setResponseMessage("PhoneNumber is not registered");
			response = Response.status(200).entity(responseobj).build();
			if (logger.isDebugEnabled()) {
				logger.debug("Reponse returned PhoneNumber is not registered , response :- 447");
			}
			return response;
		}
		default: {
			responseobj.setResponseMessage(WebConstant.UNHANDLED_ERROR);
			responseobj.setResponseCode(430);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				logger.debug("Reponse returned UNHANDLED_ERROR , response :- 430");
			}
			return response;
		}
		}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/OfferRedemptionStatus")
	/** Description of searchForNearbyOutlets() .
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @param lattitude       	This is the lattitude.
	 * @param longitude       	This is the longitude.
	 * @param radius        	This is the distance.
	 * @param appId        		This is the appId.
	 * @param campaignId        This is the campaignId.
	 * @return                	A list of nearby outlets
	 */
	public Response offerRedemptionStatus(
			// @Context HttpServletRequest Req, Consumer consumer
			@QueryParam("AppId") String appId,
			@QueryParam("OfferId") int offerId) {

		// check cache if required

		/*
		 * String appId = consumer.getAppId(); int consumerId =
		 * consumer.getConsumerId(); long outletCode = (long)
		 * consumer.getOutletCode();
		 */
		ResponseMRAS responseobj = null;
		Response response = null;

		if (logger.isDebugEnabled()) {
			logger.debug("Parameter received are  AppId" + appId
					+ ", OfferId: " + offerId);
		}

		ResponseMRAS responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			return response;
		}

		responseobj = new ResponseMRAS();

		if (offerId == 0) {
			responseobj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			responseobj.setResponseCode(404);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				logger.debug("Reponse returned MISSING_PARAMETER ");
			}
			return response;
		}

		ArrayList<Integer> resultCode = null;
		resultCode = (ArrayList<Integer>) MrasCache.MCache
				.recover("OfferRedemptionStatus" + offerId);

		if (resultCode == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Not found in cache putting into the cache");
			}
			resultCode = rewardResourceDao.offerRedemptionStatus(offerId);

			if (resultCode.size() > 0
					&& ((resultCode.get(0) == 200) || (resultCode.get(0) == 403))) {
				MrasCache.MCache.admit("OfferRedemptionStatus" + offerId,
						resultCode, 120000, 120000);
			}
		}

		if (resultCode.size() > 0) {

			if (resultCode.get(0) == 200) {
				OfferRedemptionStatusModal offerRedemptionStatusModal = new OfferRedemptionStatusModal();
				offerRedemptionStatusModal.setMaxRedemeption(resultCode.get(1));
				offerRedemptionStatusModal.setRedemptionTillNow(resultCode
						.get(2));
				response = Response.status(Response.Status.OK)
						.entity(offerRedemptionStatusModal).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned 200 ");
				}
				return response;
			} else if (resultCode.get(0) == 403) {
				responseobj.setResponseCode(403);
				responseobj.setResponseMessage(WebConstant.INVALID_OFFERID);
				response = Response.status(Response.Status.OK)
						.entity(responseobj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned 403");
				}
				return response;
			} else {
				responseobj.setResponseMessage(WebConstant.UNHANDLED_ERROR);
				responseobj.setResponseCode(430);
				response = Response.status(Response.Status.OK)
						.entity(responseobj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned 430 ");
				}
				return response;
			}

		} else {
			responseobj.setResponseMessage(WebConstant.UNHANDLED_ERROR);
			responseobj.setResponseCode(430);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 430");
			}
			return response;
		}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/CheckInNumbersForConsumer")
	/** Description of searchForNearbyOutlets() .
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @param lattitude       	This is the lattitude.
	 * @param longitude       	This is the longitude.
	 * @param radius        	This is the distance.
	 * @param appId        		This is the appId.
	 * @param campaignId        This is the campaignId.
	 * @return                	A list of nearby outlets
	 */
	public Response checkInNumbersForConsumer(
			// @Context HttpServletRequest Req, Consumer consumer
			@QueryParam("AppId") String appId,
			@QueryParam("OfferId") int offerId,
			@QueryParam("ConsumerId") int consumerId) {

		ResponseMRAS responseobj = null;
		Response response = null;

		if (logger.isDebugEnabled()) {
			logger.debug("Parameter received are  AppId" + appId
					+ ", OfferId: " + offerId + ", consumerId: " + consumerId);
		}

		ResponseMRAS responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			return response;
		}

		responseobj = new ResponseMRAS();

		if (offerId == 0 || consumerId == 0) {
			responseobj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			responseobj.setResponseCode(404);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 404");
			}
			return response;
		}
		int[] resultCode = rewardResourceDao.countInNumbersForConsumer(offerId,
				consumerId);

		if (resultCode.length > 0) {

			if (resultCode[0] == 200) {
				NumberOfCheckInModal numberOfCheckInModal = new NumberOfCheckInModal();
				numberOfCheckInModal.setNumberOfCheckin(resultCode[1]);
				response = Response.status(Response.Status.OK)
						.entity(numberOfCheckInModal).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned 200 ");
				}
				return response;
			} else if (resultCode[0] == 403) {
				responseobj.setResponseCode(403);
				responseobj.setResponseMessage(WebConstant.INVALID_OFFERID);
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned 403");
				}
				response = Response.status(Response.Status.OK)
						.entity(responseobj).build();
				return response;
			} else if (resultCode[0] == 405) {
				responseobj.setResponseCode(405);
				responseobj.setResponseMessage(WebConstant.INVALID_CONSUMERID);
				response = Response.status(Response.Status.OK)
						.entity(responseobj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned 405 ");
				}
				return response;
			} else {
				responseobj.setResponseMessage(WebConstant.UNHANDLED_ERROR);
				responseobj.setResponseCode(430);
				response = Response.status(Response.Status.OK)
						.entity(responseobj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned 430 ");
				}
				return response;
			}

		} else {
			responseobj.setResponseMessage(WebConstant.UNHANDLED_ERROR);
			responseobj.setResponseCode(430);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 430");
			}
			return response;
		}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/GetAllCampaigns")
	/** Description of searchForNearbyOutlets() .
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @param lattitude       	This is the lattitude.
	 * @param longitude       	This is the longitude.
	 * @param radius        	This is the distance.
	 * @param appId        		This is the appId.
	 * @param campaignId        This is the campaignId.
	 * @return                	A list of nearby outlets
	 */
	public Response getAllCampaigns(@QueryParam("AppId") String appId) {

		ResponseMRAS responseobj = null;
		Response response = null;

		if (logger.isDebugEnabled()) {
			logger.debug("Parameter received are  AppId" + appId);
		}

		ResponseMRAS responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			return response;
		}

		responseobj = new ResponseMRAS();
		CampaignsModal campaignList = new CampaignsModal();

		campaignList = (CampaignsModal) MrasCache.MCache
				.recover("campaignFromAppId" + appId);

		if (campaignList == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("No found in cache putting into cache");
			}
			campaignList = rewardResourceDao.getAllCampaignsDao(appId);
			if ((campaignList != null)) {
				MrasCache.MCache.admit("campaignFromAppId" + appId,
						campaignList, 3600000, 3600000);
			}
		}

		if ((campaignList != null)) {
			response = Response.status(Response.Status.OK).entity(campaignList)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.info("Response recevied 200 ");
				}
			return response;

		} else {
			// need to change the response
			responseobj.setResponseMessage(WebConstant.NO_MATCH_FOUND_CAMPAIGN);
			responseobj.setResponseCode(434);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.info("Response recevied 434");
				}
			return response;
		}

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/Gifting")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response Gifting(@QueryParam("AppId") String appId,
			@QueryParam("ConsumerId") int consumerId,
			@Context HttpServletRequest Req, Consumer consumer) {

		int offerId = consumer.getOfferId();
		String messageRecive = consumer.getGiftMessage();
		Response response = null;
		int resultGetFromIssue = 200;
		// As the phone number data would be large so needs to send this data in
		// the body
		if (logger.isDebugEnabled()) {
			logger.debug("Parameter received are  AppId" + appId
					+ " consumerId:-" + consumerId + " offerId:" + offerId
					+ " messageRecive:-" + messageRecive);
		}
		// validating AppId first
		ResponseMRAS responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {

			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			return response;
		}

		responseObj = new ResponseMRAS();
		// Let's check if there are any phone number sent by the users.
		if ((consumer.getPhoneNumbers() == null) || offerId == 0
				|| consumerId == 0 || messageRecive == null) {
			responseObj.setResponseCode(404);
			responseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 404");
			}
			return response;
		}

		List<String> phoneNumbers = consumer.getPhoneNumbers();

		for (String phoneNumber : phoneNumbers) {
			if (!phoneNumber.matches(pattern)) {
				responseObj.setResponseCode(425);
				// responseObj.setResponseMessage("Wrong PhoneNumber format");
				responseObj
						.setResponseMessage(WebConstant.WRONG_PHONENUMBER_425
								+ ":-" + phoneNumber);
				response = Response.status(Response.Status.OK)
						.entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					   logger.info("Response recevied "+responseObj.getResponseCode());
					}
				return response;
			}
		}

		String phoneNumber1, phoneNumber2 = "N", phoneNumber3 = "N";
		int resultCode;

		if (phoneNumbers.size() > 2) {
			phoneNumber1 = phoneNumbers.get(0);
			phoneNumber2 = phoneNumbers.get(1);
			phoneNumber3 = phoneNumbers.get(2);
		} else if (phoneNumbers.size() > 1) {
			phoneNumber1 = phoneNumbers.get(0);
			phoneNumber2 = phoneNumbers.get(1);
			// phoneNumber3="N";
		} else {
			phoneNumber1 = phoneNumbers.get(0);
			// phoneNumber2="N";
			// phoneNumber3="N";
		}

		// try{
		resultCode = rewardResourceDao.GiftingToFriends(phoneNumber1,
				phoneNumber2, phoneNumber3, consumerId, offerId, messageRecive);
		/*
		 * }catch(Exception e){
		 * 
		 * responseObj.setResponseCode(430); responseObj
		 * .setResponseMessage("Some Unhandled Error has occurred");
		 * 
		 * response = Response.status(200).type(MediaType.APPLICATION_JSON)
		 * .entity(responseObj).build(); return response;
		 * 
		 * }
		 */

		OfferGiftModal offerGiftModal = new OfferGiftModal();
		ArrayList<String> notRegisterList = new ArrayList<String>();
		ArrayList<String> registerList = new ArrayList<String>();

		switch (resultCode) {
		case 0:
			registerList.add(phoneNumber1);
			registerList.add(phoneNumber2);
			registerList.add(phoneNumber3);
			break;
		case 1:
			if (!phoneNumber1.equalsIgnoreCase("N"))
				notRegisterList.add(phoneNumber1);

			registerList.add(phoneNumber2);
			registerList.add(phoneNumber3);
			break;
		case 2:
			if (!phoneNumber2.equalsIgnoreCase("N"))
				notRegisterList.add(phoneNumber2);
			registerList.add(phoneNumber1);
			registerList.add(phoneNumber3);
			break;
		case 3:
			if (!phoneNumber1.equalsIgnoreCase("N"))
				notRegisterList.add(phoneNumber1);
			if (!phoneNumber2.equalsIgnoreCase("N"))
				notRegisterList.add(phoneNumber2);
			registerList.add(phoneNumber3);
			break;
		case 4:
			if (!phoneNumber3.equalsIgnoreCase("N"))
				notRegisterList.add(phoneNumber3);
			registerList.add(phoneNumber2);
			registerList.add(phoneNumber1);
			break;
		case 5:
			if (!phoneNumber1.equalsIgnoreCase("N"))
				notRegisterList.add(phoneNumber1);
			if (!phoneNumber3.equalsIgnoreCase("N"))
				notRegisterList.add(phoneNumber3);
			registerList.add(phoneNumber2);
			break;
		case 6:
			if (!phoneNumber3.equalsIgnoreCase("N"))
				notRegisterList.add(phoneNumber3);
			if (!phoneNumber2.equalsIgnoreCase("N"))
				notRegisterList.add(phoneNumber2);
			registerList.add(phoneNumber1);
			break;
		case 7:
			if (!phoneNumber1.equalsIgnoreCase("N"))
				notRegisterList.add(phoneNumber1);
			if (!phoneNumber2.equalsIgnoreCase("N"))
				notRegisterList.add(phoneNumber2);
			if (!phoneNumber3.equalsIgnoreCase("N"))
				notRegisterList.add(phoneNumber3);
			break;
		case 403:
			responseObj.setResponseCode(403);
			responseObj.setResponseMessage(WebConstant.INVALID_OFFERID);
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 403");
			}
			return response;
		case 405:
			responseObj.setResponseCode(resultCode);
			responseObj.setResponseMessage(WebConstant.INVALID_CONSUMERID);
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 405");
			}
			return response;
		case 447:
			responseObj.setResponseCode(resultCode);
			responseObj.setResponseMessage("Offer was already gifted");
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 447");
			}
			return response;
		case 437:
			responseObj.setResponseCode(437);
			responseObj
					.setResponseMessage(WebConstant.OFFER_MAXIMUM_LIMIT_COMPLETE);
			response = Response.status(Response.Status.OK)
					.type(MediaType.APPLICATION_JSON).entity(responseObj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 437");
			}
			return response;

		default:
			responseObj.setResponseCode(430);
			responseObj.setResponseMessage(WebConstant.UNHANDLED_ERROR);

			response = Response.status(Response.Status.OK)
					.type(MediaType.APPLICATION_JSON).entity(responseObj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 430");
			}
			return response;

		}

		offerGiftModal.setUnRegisteredPhoneNumbers(notRegisterList);
		offerGiftModal.setOffersGiftedTo(registerList);

		try {
			for (String phoneNumber : registerList) {
				resultGetFromIssue = rewardResourceDao
						.getConsumerIdFromPhoneNumber(phoneNumber, offerId);
			}
			if (resultGetFromIssue != 200) {
				responseObj.setResponseCode(437);
				responseObj
						.setResponseMessage(WebConstant.OFFER_MAXIMUM_LIMIT_COMPLETE);
				response = Response.status(Response.Status.OK)
						.type(MediaType.APPLICATION_JSON).entity(responseObj)
						.build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned 437");
				}
				return response;
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseObj.setResponseCode(430);
			responseObj.setResponseMessage(WebConstant.UNHANDLED_ERROR);

			response = Response.status(Response.Status.OK)
					.type(MediaType.APPLICATION_JSON).entity(responseObj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 430");
			}
			return response;
		}
		response = Response.status(Response.Status.OK)
				.type(MediaType.APPLICATION_JSON).entity(offerGiftModal)
				.build();
		if (logger.isDebugEnabled()) {
			   logger.debug("Response returned 200");
		}
		return response;

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/WebRedeem")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response webRedeemReward(@Context HttpServletRequest Req,
			Consumer consumer) {

		// get the various values from the consumer.
		String appId = consumer.getAppId();
		String phoneNumber = consumer.getPhoneNumber();
		long outletCode = consumer.getOutletCode();
		int offerId = consumer.getOfferId();
		int countryCode = consumer.getCountryCode();
		String rewardCode = consumer.getRewardCode();
		String productName = consumer.getProductName();

		if (logger.isDebugEnabled()) {
			logger.debug("Parameter received to redeem are phoneNumber "
					+ phoneNumber + ", AppId: " + appId + ", OutletCode : "
					+ outletCode + ",OfferId : " + offerId + ",RewardCode : "
					+ rewardCode + ", productName:" + productName
					+ ", ContryCode:" + countryCode);
		}
		Response response = null;

		ResponseMRAS responseObj = validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(200).entity(responseObj).build();
			return response;
		}
		responseObj = new ResponseMRAS();

		if ((phoneNumber == null) || (offerId == 0) || (countryCode == 0)
				|| (outletCode == 0) || (rewardCode == null)
				|| (productName == null)) {
			responseObj.setResponseCode(404);
			responseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.info("Response recevied "+responseObj.getResponseCode());
				}
			return response;
		}

		int value = validateOffer(offerId, countryCode);

		// value =3 OfferId does not exist in the database.
		if (value == 3) {
			// logger.info("OfferId  is incorrect");
			responseObj.setResponseCode(403);
			responseObj.setResponseMessage(WebConstant.INVALID_OFFERID);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				logger.debug("Reponse returned INVALID_OFFERID , reponse :- 403");
			}
			return response;
		}
		// offer is not redeemable
		else if (value == 4) {
			responseObj.setResponseCode(446);
			responseObj.setResponseMessage(WebConstant.OFFER_NON_REDEEMABLE);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				logger.debug("Reponse returned OFFER_NON_REDEEMABLE , reponse :- 446");
			}
			return response;
		}
		// value =2 OfferId offer is valid and hence validating country Rules
		else if (value == 2) {
			/*
			 * logger.debug(
			 * "Validating country if offer can be redeem in country at the current time for "
			 * + countryCode);
			 */
			int valueCountryId = validateCountryCode(countryCode);
			// value =3 countyriD does not exist in the database.
			if (valueCountryId == 3) {
				// logger.debug("ContryCode :" + countryCode + "is incorrect ");
				responseObj.setResponseMessage("ContryCode :" + countryCode
						+ "is incorrect ");
				responseObj.setResponseCode(423);
				response = Response.status(200).entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					logger.debug("ContryCode :" + countryCode
							+ "is incorrect  , reponse :- 423");
				}
				return response;
			}
			// value =2 countyriD is valid, let's validate rewardcode and all.
			else if (valueCountryId == 2) {
				// logger.debug("calling ValidateRewardVoucher ");
				int resultResponse;
				try {
					resultResponse = rewardResourceDao
							.ValidateWebRewardVoucher(phoneNumber, offerId,
									productName, countryCode, outletCode,
									rewardCode, appId);
				} catch (SQLException e) {

					responseObj.setResponseCode(430);
					responseObj
							.setResponseMessage("Some error in input parameter");

					response = Response.status(200)
							.type(MediaType.APPLICATION_JSON)
							.entity(responseObj).build();
					if (logger.isDebugEnabled()) {
						logger.debug("Some error in input parameter , reponse :- 430"
								+ e.getMessage());
					}
					e.printStackTrace();
					return response;

				}

				switch (resultResponse) {

				case 200:
					logger.info("Response recevied  for phoneNumber "
							+ phoneNumber + " is " + resultResponse);
					responseObj.setResponseCode(resultResponse);
					if (logger.isDebugEnabled()) {
						logger.debug("Reward code: " + rewardCode
								+ "with respect to Offer " + offerId
								+ " for phoneNumber" + phoneNumber
								+ "is redeemed");
					}
					responseObj.setResponseMessage(WebConstant.SUCCESSFUL);
					break;

				case 401:
					responseObj.setResponseCode(resultResponse);
					/*
					 * responseObj.setResponseMessage("offer " + offerId +
					 * " is not valid for Outlet " + outletCode);
					 */
					responseObj
							.setResponseMessage("Invalid OutletCode, Offer cannot be Redeemed in this outlet");
					break;
				case 443:
					responseObj.setResponseCode(443);
					responseObj
							.setResponseMessage(WebConstant.OFFER_PRODUCTNAME_NOT_VALID);
					break;

				case 407:
					responseObj.setResponseCode(417);
					if (logger.isDebugEnabled()) {
						logger.debug("Reward code:- " + rewardCode
								+ " with respect to Offer:- " + offerId
								+ " for phoneNumber:- " + phoneNumber
								+ " is not valid");
					}
					/*
					 * responseObj
					 * .setResponseMessage(WebConstant.INVALID_REWARD_CODE);
					 */
					/*
					 * responseObj .setResponseMessage("Reward code:- " +
					 * rewardCode + "with respect to Offer:- " + offerId +
					 * " for phoneNumber:- " + phoneNumber + " is not valid ");
					 */
					responseObj
							.setResponseMessage("Reward code with respect to Offer for consumer is not valid");
					break;

				case 410:
					responseObj.setResponseCode(429);
					/*
					 * logger.debug("Consumer " + consumerId +
					 * " has already exceeded the voucher redemeption limit for country "
					 * + countryCode);
					 */
					responseObj
							.setResponseMessage("phoneNumber "
									+ phoneNumber
									+ " has already exceeded the voucher redemption limit for country "
									+ countryCode);
					break;
				case 428:
					responseObj
							.setResponseMessage("Current time is not valid to redeem the offerId");
					responseObj.setResponseCode(428);
					response = Response.status(200).entity(responseObj).build();

					break;

				case 441:
					responseObj.setResponseCode(441);
					responseObj
							.setResponseMessage(WebConstant.OFFER_REDEMPTION_LIMIT_COMPLETE);
					break;

				case 442:
					responseObj.setResponseCode(442);
					responseObj
							.setResponseMessage(WebConstant.OUTLET_REDEMPTION_LIMIT_COMPLETE);
					break;

				}

				response = Response.status(200).entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					logger.debug(" reponse from ValidateWebRewardVoucher :- "
							+ responseObj.getResponseCode());
				}
				return response;
				// else means that countriD was valid however offer can not be
				// redeemed at this time.
			} else {
				logger.info("Current time is not valid to redeem the offer for the country ");
				responseObj
						.setResponseMessage("Current time is not valid to redeem the voucher for the country "
								+ countryCode);
				responseObj.setResponseCode(430);
				response = Response.status(200).entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					logger.debug(" Current time is not valid to redeem the voucher for the country , response :- 430");
				}
				return response;
			}
			// offer time is not valid for this
		} else {
			logger.info("Current time is not valid to redeem the offer ");
			Date now = new Date();
			Offer offer = (Offer) MrasCache.MCache.recover("Offer" + offerId);

			if (offer == null) {
				// not found in cache recive from db and update cache
				offer = rewardResourceDao.getOfferWithOfferid(offerId);
				if (offer == null) {
					responseObj.setResponseCode(403);
					responseObj.setResponseMessage(WebConstant.INVALID_OFFERID);
					response = Response.status(200).entity(responseObj).build();
					if (logger.isDebugEnabled()) {
						logger.debug(" INVALID_OFFERID , response :- 403");
					}
					return response;
				}
				timeToLive = offer.getRedemptionEndTime().getTimeInMillis()
						- now.getTime();
				MrasCache.MCache.admit("Offer" + offerId, offer, timeToLive,
						timeToLive);

			}
			responseObj
					.setResponseMessage("Current time is not valid to redeem the offerId "
							+ offerId
							+ ". Please try between "
							+ offer.getRedemptionStartTime().get(
									Calendar.HOUR_OF_DAY)
							+ ":"
							+ offer.getRedemptionStartTime().get(
									Calendar.MINUTE)
							+ " and "
							+ offer.getRedemptionEndTime().get(
									Calendar.HOUR_OF_DAY)
							+ ":"
							+ offer.getRedemptionEndTime().get(Calendar.MINUTE));
			responseObj.setResponseCode(428);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				logger.debug("Current time is not valid to redeem the offerId "
						+ offerId + " response :- 428");
			}

			return response;
		}
	}

}
