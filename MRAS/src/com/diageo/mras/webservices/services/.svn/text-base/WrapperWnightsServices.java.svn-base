package com.diageo.mras.webservices.services;

import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.diageo.mras.cache.MrasCache;
import com.diageo.mras.webservices.dao.Neoutils;
import com.diageo.mras.webservices.dao.OutletResourceDao;
import com.diageo.mras.webservices.dao.RewardResourceDao;
import com.diageo.mras.webservices.dao.SqlStatements;
import com.diageo.mras.webservices.encryption.StringEncrypter;
import com.diageo.mras.webservices.init.ConnectionPool;
import com.diageo.mras.webservices.init.MrasConstants;
import com.diageo.mras.webservices.init.PropertyReader;
import com.diageo.mras.webservices.modals.BrandListModal;
import com.diageo.mras.webservices.modals.Consumer;
import com.diageo.mras.webservices.modals.Offer;
import com.diageo.mras.webservices.modals.OuletInformation;
import com.diageo.mras.webservices.responses.ResponseMRAS;
import com.diageo.mras.webservices.responses.Responses;
import com.diageo.mras.webservices.responses.WebRedeemWNResponse;
import com.diageo.mras.webservices.responses.WrapperResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Path("/WN")
public class WrapperWnightsServices {
	Response response = null;
	Responses obj = null;
	private static final Logger logger = Logger
			.getLogger(WrapperWnightsServices.class.getName());
	private static String NEO_resigtration_reponse = PropertyReader
			.getPropertyValue(MrasConstants.NEO_resigtration_reponse);
	private static final RewardResourceDao rewardResourceDao = new RewardResourceDao();
	private static final OutletResourceDao outletResourceDao = new OutletResourceDao();
	static final String passPhrase = "Diageo@123";

	private static StringEncrypter desEncrypter = new StringEncrypter(
			passPhrase);
	private static final RewardResource rewardResource = new RewardResource();
	private static final ValidationDateEmail validationDateEmail = new ValidationDateEmail();
	private static final String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*"
			+ "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String pattern = "[+|0]?\\d{2,3}\\s?[-]?\\d{2,4}\\s?[-]?\\d{3,6}";
	private static final Format formatter2 = new SimpleDateFormat("yyyy-MM-dd");
	private static String NEO_URL_AGE_AFFIRMATION = PropertyReader
			.getPropertyValue(MrasConstants.NEO_URL_AGE_AFFIRMATION);
	private static String WN_REWARD_CODE = PropertyReader
	.getPropertyValue(MrasConstants.WN_REWARD_CODE);
	private static long timeToLive = 1800000;
	private static final int TODAY_LIMIT_WEBISSUEVOUCHER = Integer
			.parseInt(PropertyReader
					.getPropertyValue(MrasConstants.TODAY_LIMIT_WEBISSUEVOUCHER));

	private static final int WEEK_LIMIT_WEBISSUEVOUCHER =Integer
			.parseInt(PropertyReader
					.getPropertyValue(MrasConstants.WEEK_LIMIT_WEBISSUEVOUCHER));
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/CompleteRegistration")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response completeRegistration(@Context HttpServletRequest Req,
			com.diageo.mras.webservices.modals.Consumer consumerobj) {

		String appId = consumerobj.getAppId();
		String phoneNumber = consumerobj.getPhoneNumber();
		String emailId = consumerobj.getEmailId();
		String dateOfBirth = consumerobj.getDateOfBirth();
		String firstname = consumerobj.getFirstName();
		String lastName = consumerobj.getLastName();
		String countrycode = Integer.toString(consumerobj.getCountryCode());
		String modifyFlag = consumerobj.getModifyFlag();
		String answerText = consumerobj.getAnswerText();
		String loginName = consumerobj.getLoginName();
		String passWord = consumerobj.getPassword();
		String promoCode = consumerobj.getPromoCode();
		String encyptPhoneNumber = consumerobj.getEncryptPh();
		List<BrandListModal> commuChannelList = consumerobj
				.getCommuChannelList();

		ResponseMRAS responseObj = RewardResource.validateAppId(appId);

		if (responseObj != null) {
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			return response;
		} else {
			responseObj = new ResponseMRAS();
		}
		if ((emailId == null) || encyptPhoneNumber == null
				|| countrycode == null || (phoneNumber == null)
				|| firstname == null || lastName == null || loginName == null
				|| passWord == null || (dateOfBirth == null)
				|| commuChannelList == null || answerText == null
				|| modifyFlag == null || promoCode == null) {

			responseObj.setResponseCode(404);
			responseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Return response 404");
			}
			return response;
		}

		for (BrandListModal brandModal : commuChannelList) {
			if (null == brandModal.getBrandId()
					|| null == brandModal.getCommChannel()) {
				responseObj.setResponseCode(404);
				responseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);
				response = Response.status(Response.Status.OK)
						.entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Return response getCommChannel missing 404");
				}
				return response;
			}

		}

		if (!phoneNumber.matches(pattern)) {
			responseObj.setResponseCode(425);
			responseObj.setResponseMessage("Wrong PhoneNumber format");
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Return response 425");
			}
			return response;
		}
		if (!emailId.matches(emailPattern)) {
			responseObj.setResponseCode(437);
			responseObj.setResponseMessage("Wrong Email format");
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Return response 437");
			}
			return response;
		}

		Date birthDate = null;
		try {
			birthDate = (Date) formatter2.parseObject(dateOfBirth);
		} catch (ParseException e) {
			e.printStackTrace();
			logger.info(WebConstant.DATE_FORMAT);

			responseObj.setResponseCode(406);
			responseObj.setResponseMessage(WebConstant.DATE_FORMAT);
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Return response 406");
			}
			return response;
		}

		if ((encyptPhoneNumber != null)
				&& (!encyptPhoneNumber.equalsIgnoreCase("Dummy"))) {

			String decryptedPhoneNumber = desEncrypter
					.decrypt(encyptPhoneNumber);
			logger.info("Decrypted ph no" + decryptedPhoneNumber);
			decryptedPhoneNumber.trim();
			if ((!phoneNumber.equalsIgnoreCase(decryptedPhoneNumber))
					|| decryptedPhoneNumber.equalsIgnoreCase("Mismatch")) {
				responseObj.setResponseCode(520);
				responseObj
						.setResponseMessage(WebConstant.PHONE_NUMBER_MISMATCH);
				response = Response.status(Response.Status.OK)
						.entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Return response 520");
				}
				return response;

			}
		}

		try {
			boolean updateFlag = false;

			StringWriter writer;

			writer = Neoutils.neoXmlGenerator(emailId, modifyFlag, phoneNumber,
					commuChannelList, firstname, lastName, dateOfBirth,
					promoCode, countrycode, loginName, passWord, answerText,
					updateFlag);
			if (writer == null) {
				responseObj
						.setResponseMessage("Some Unhandled Error has occurred");
				responseObj.setResponseCode(430);
				response = Response.status(Response.Status.OK)
						.entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Return response 430 , writer null if block");
				}
				return response;
			}

			try {

				ClientResponse responseobj = Neoutils.neoConsumerReg(writer,
						appId);
				if (responseobj == null) {
					responseObj
							.setResponseMessage("Some Unhandled Error has occurred");
					responseObj.setResponseCode(430);
					response = Response.status(Response.Status.OK)
							.entity(responseObj).build();
					if (logger.isDebugEnabled()) {
						   logger.debug("Return response 430, response null from neo registration.");
					}
					return response;
				}
				if (responseobj.getStatus() == 201) {
					// The output recieved need to be parsed to get consumerid

					String location = responseobj.getLocation().toString();
					if (logger.isDebugEnabled()) {
					logger.debug("location >>>>" + location);
					}
					int left = location.indexOf("consumers");
					if (logger.isDebugEnabled()) {
					logger.debug("left >>>>" + left);
					}
					int right = location
							.indexOf("/" + NEO_resigtration_reponse);

					if (right < 0) {
						right = location.indexOf("/registration");
					}
					if (logger.isDebugEnabled()) {
					logger.debug("right >>>>" + right);
					logger.debug(location.substring(left + 10, right));
					}
					int consumerId = Integer.parseInt(location.substring(
							left + 10, right));
					
					logger.info("ConsumerId generated from NeoConsumerRegistration Web service"
							+ consumerId);

					ClientResponse responseobjConfirmReg = Neoutils
							.neoConfirmReg(consumerId, writer, appId);
					if (responseobjConfirmReg == null) {
						responseObj
								.setResponseMessage("Some Unhandled Error has occurred");
						responseObj.setResponseCode(430);
						response = Response.status(Response.Status.OK)
								.entity(responseObj).build();
						if (logger.isDebugEnabled()) {
							   logger.debug("Return response 430, responseobjConfirmReg null from neo.");
						}
						return response;
					}
					if (responseobjConfirmReg.getStatus() == 200) {

						WrapperResponse responseneoobj = new WrapperResponse();

						responseneoobj.setConsumerId(consumerId);
						response = Response.status(201).entity(responseneoobj)
								.build();

					}

					else {

						obj = Neoutils.clientResponse(responseobjConfirmReg);

						responseObj.setResponseCode(responseobjConfirmReg
								.getStatus());
						responseObj.setResponseMessage(obj.getResponse()
								.getResponseCode());
						response = Response.status(Response.Status.OK)
								.entity(responseObj).build();
						if (logger.isDebugEnabled()) {
							   logger.debug("Return response :- "+responseObj.getResponseCode());
						}
						return response;

					}

				} else {

					obj = Neoutils.clientResponse(responseobj);

					if (obj.getResponse().getResponseCode()
							.equalsIgnoreCase("LOGINNAME_ALREADY_EXISTS")
							|| obj.getResponse()
									.getResponseCode()
									.equalsIgnoreCase(
											"EMAIL_ID_AND_DOB_COMBINATION_ALREADY_EXISTS")) {

						ClientResponse responseobjUserAccount = Neoutils
								.neoUserAccount(loginName, passWord, promoCode,
										appId);
						if (responseobjUserAccount == null) {
							responseObj
									.setResponseMessage("Some Unhandled Error has occurred");
							responseObj.setResponseCode(430);
							response = Response.status(Response.Status.OK)
									.entity(responseObj).build();

						}

						if (responseobjUserAccount.getStatus() == 200) {

							// ===============Added by sachin

							int consumerId = Integer
									.parseInt(responseobjUserAccount
											.getEntity(String.class));

							updateFlag = true;
							StringWriter writer1;
							writer1 = Neoutils.neoXmlGenerator(emailId,
									modifyFlag, phoneNumber, commuChannelList,
									firstname, lastName, dateOfBirth,
									promoCode, countrycode, loginName,
									passWord, answerText, updateFlag);

							// Re-registartion!!!!!!!!!!!!! logic for updating
							// profile.only
							// preference will be updated

							ClientResponse responseobjupdateConsumer = Neoutils
									.neoUpdateProfile(consumerId, appId,
											writer1);

							if (responseobjupdateConsumer.getStatus() == 200) {

								logger.info("Details updated succesfully.Response from neo update consumer profile is 200");

							}

							else {

								obj = Neoutils
										.clientResponse(responseobjupdateConsumer);
								logger.info("Response form NEOupdateconsumer"
										+ obj.getResponse().getResponseCode());

								logger.info("Response form NEOupdateconsumer"
										+ responseobjupdateConsumer.getStatus());

							}

							WrapperResponse responseneoobj = new WrapperResponse();

							responseneoobj.setConsumerId(consumerId);

							response = Response.status(201)
									.entity(responseneoobj).build();
							if (logger.isDebugEnabled()) {
								   logger.debug("Returned response , 201 ");
							}
							return response;
						}

						else {
							obj = Neoutils
									.clientResponse(responseobjUserAccount);

							responseObj.setResponseCode(responseobjUserAccount
									.getStatus());
							responseObj.setResponseMessage(obj.getResponse()
									.getResponseCode());
							response = Response.status(Response.Status.OK)
									.entity(responseObj).build();
							if (logger.isDebugEnabled()) {
								   logger.debug("Returned response :- "+responseObj.getResponseCode());
							}
							return response;

						}

					}

					else {

						responseObj.setResponseCode(responseobj.getStatus());
						responseObj.setResponseMessage(obj.getResponse()
								.getResponseCode());
						response = Response.status(Response.Status.OK)
								.entity(responseObj).build();
						if (logger.isDebugEnabled()) {
							   logger.debug("Returned response :- "+responseObj.getResponseCode());
						}
						return response;
					}
				}

			}

			catch (Exception e) {
				e.printStackTrace();
				responseObj
						.setResponseMessage("Some Unhandled Error has occurred");
				responseObj.setResponseCode(430);
				response = Response.status(Response.Status.OK)
						.entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Exception occured , Returned response :- "+430);
				}
				return response;
				
			}

		}

		catch (Exception e) {
			e.printStackTrace();
			responseObj.setResponseMessage("Some Unhandled Error has occurred");
			responseObj.setResponseCode(430);
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Exception occured , Returned response :- "+430);
			}
			return response;
		
		}
		if (logger.isDebugEnabled()) {
			   logger.debug(" Returned response ");
		}
		return response;

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/WebRedeemWN")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response webRedeemRewardWN(@Context HttpServletRequest Req,
			Consumer consumer) {

		// get the various values from the consumer.
		String appId = consumer.getAppId();
		String phoneNumber = consumer.getPhoneNumber();
		long outletCode = consumer.getOutletCode();
		int offerId = consumer.getOfferId();
		int countryCode = consumer.getCountryCode();
		String rewardCode = consumer.getRewardCode();

		if (logger.isDebugEnabled()) {
			logger.debug("Parameter received to redeem are phoneNumber "
					+ phoneNumber + ", AppId: " + appId + ", OutletCode : "
					+ outletCode + ",OfferId : " + offerId + ",RewardCode : "
					+ rewardCode + ", ContryCode:" + countryCode);
		}
		Response response = null;

		ResponseMRAS responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(200).entity(responseObj).build();
			return response;
		}
		responseObj = new ResponseMRAS();

		if ((phoneNumber == null) || (offerId == 0) || (countryCode == 0)
				|| (outletCode == 0) || (rewardCode == null)) {
			responseObj.setResponseCode(404);
			responseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Return response 404 ");
			}
			return response;
		}

		String productName = getProductName(appId, phoneNumber, rewardCode);

		if (productName == null) {
			responseObj.setResponseCode(444);
			responseObj.setResponseMessage(WebConstant.PRODUCT_NAME_NOTFOUND);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Return response 444 ");
			}
			return response;

		}

		int value = rewardResource.validateOffer(offerId, countryCode);

		// value =3 OfferId does not exist in the database.
		if (value == 3) {
			// logger.info("OfferId  is incorrect");
			responseObj.setResponseCode(403);
			responseObj.setResponseMessage(WebConstant.INVALID_OFFERID);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				logger.debug("INVALID_OFFERID , reponse :- 403");
			}
			return response;
		}
		// offer is not redeemable
		else if (value == 4) {
			responseObj.setResponseCode(446);
			responseObj.setResponseMessage(WebConstant.OFFER_NON_REDEEMABLE);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				logger.debug("OFFER_NON_REDEEMABLE , reponse :- 446");
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
			int valueCountryId = rewardResource
					.validateCountryCode(countryCode);
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
					if (logger.isDebugEnabled()) {
						logger.debug("Reward code: " + rewardCode
								+ "with respect to Offer " + offerId
								+ " for phoneNumber" + phoneNumber
								+ "is redeemed");
					}

					OuletInformation ouletInformation = outletResourceDao
							.getOutletDetailsDao(outletCode);

					Offer offer = (Offer) MrasCache.MCache.recover("Offer" + offerId);

					if (offer == null) {
						// not found in cache recive from db and update cache
						offer = rewardResourceDao.getOfferWithOfferid(offerId);
						if (offer == null) {
							responseObj.setResponseCode(403);
							responseObj.setResponseMessage(WebConstant.INVALID_OFFERID);
							response = Response.status(200).entity(responseObj).build();
							if (logger.isDebugEnabled()) {
								logger.debug("INVALID_OFFERID , reponse :- 403");
							}
							return response;
						}
						
						MrasCache.MCache.admit("Offer" + offerId, offer, timeToLive,
								timeToLive);

					}
					
						WebRedeemWNResponse responseobj = new WebRedeemWNResponse(
							offer.getOfferName(),
							ouletInformation.getOutletName(),
							ouletInformation.getAddress(),
							ouletInformation.getZipCode(),
							ouletInformation.getTown(),productName);

					response = Response.status(200).entity(responseobj).build();
					if (logger.isDebugEnabled()) {
						   logger.debug("Return response 200 ");
					}
					return response;

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

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/RedeemNow")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response RedeemNow(@Context HttpServletRequest Req, Consumer consumer) {

		String appId = consumer.getAppId();
		int consumerId = consumer.getConsumerId();
		long outletCode = consumer.getOutletCode();
		int offerId = consumer.getOfferId();
		int countryCode = consumer.getCountryCode();

		String productName = consumer.getProductName();

		if (logger.isDebugEnabled()) {
			logger.debug("Parameter received to redeem are consumerId "
					+ consumerId + ", AppId: " + appId + ", OutletCode : "
					+ outletCode + ",OfferId : " + offerId + ", productName:"
					+ productName + ", ContryCode:" + countryCode);
		}
		Response response = null;

		ResponseMRAS responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			return response;
		}
		responseObj = new ResponseMRAS();

		if ((consumerId == 0) || (offerId == 0) || (countryCode == 0)
				|| (outletCode == 0) || (productName == null)) {
			responseObj.setResponseCode(404);
			responseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			return response;
		}

		int value = rewardResource.validateOffer(offerId, countryCode);

		// value =3 OfferId does not exist in the database.
		if (value == 3) {
			// logger.info("OfferId  is incorrect");
			responseObj.setResponseCode(403);
			responseObj.setResponseMessage(WebConstant.INVALID_OFFERID);
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			if (logger.isDebugEnabled()) {
				logger.debug("INVALID_OFFERID , reponse :- 403");
			}
			return response;
		}
		// offer is not redeemable
		else if (value == 4) {
			responseObj.setResponseCode(446);
			responseObj.setResponseMessage(WebConstant.OFFER_NON_REDEEMABLE);
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();

			if (logger.isDebugEnabled()) {
				logger.debug("OFFER_NON_REDEEMABLE , reponse :- 446");
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
			int valueCountryId = rewardResource
					.validateCountryCode(countryCode);
			// value =3 countyriD does not exist in the database.
			if (valueCountryId == 3) {
				// logger.debug("ContryCode :" + countryCode + "is incorrect ");
				responseObj.setResponseMessage("ContryCode :" + countryCode
						+ "is incorrect ");
				responseObj.setResponseCode(423);
				response = Response.status(Response.Status.OK)
						.entity(responseObj).build();
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
					// /Hardcoding reward code
					String rewardCode = "WN";
					resultResponse = rewardResourceDao.ValidateRewardWN(
							consumerId, offerId, productName, countryCode,
							outletCode, rewardCode, appId);
				} catch (SQLException e) {

					responseObj.setResponseCode(430);
					responseObj
							.setResponseMessage("Some error in input parameter");

					response = Response.status(Response.Status.OK)
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
					logger.info("Response recevied  for consumerID "
							+ consumerId + " is " + resultResponse);
					responseObj.setResponseCode(resultResponse);
					if (logger.isDebugEnabled()) {
						logger.debug("Offer " + offerId
								+ " is redeemed by  consumerid" + consumerId);
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
						logger.debug("Consumer has already redeemed the offer for today");
					}

					responseObj
							.setResponseMessage("Consumer has already redeemed the offer for today");
					break;

				case 410:
					responseObj.setResponseCode(429);
					/*
					 * logger.debug("Consumer " + consumerId +
					 * " has already exceeded the voucher redemeption limit for country "
					 * + countryCode);
					 */
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
					response = Response.status(Response.Status.OK)
							.entity(responseObj).build();

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

				response = Response.status(Response.Status.OK)
						.entity(responseObj).build();
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
				response = Response.status(Response.Status.OK)
						.entity(responseObj).build();
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
					response = Response.status(Response.Status.OK)
							.entity(responseObj).build();
					if (logger.isDebugEnabled()) {
						logger.debug(" INVALID_OFFERID , response :- 403");
					}
					return response;
				}
				/*
				 * timeToLive = offer.getRedemptionEndTime().getTimeInMillis() -
				 * now.getTime(); MrasCache.MCache.admit("Offer" + offerId,
				 * offer, timeToLive, timeToLive);
				 */

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
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			if (logger.isDebugEnabled()) {
				logger.debug("Current time is not valid to redeem the offerId "
						+ offerId + " response :- 428");
			}

			return response;
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/GetAllOffers")
	public Response getAllOffers(@QueryParam("AppId") String appId,
			@QueryParam("CountryCode") int countryCode,
			@QueryParam("CampaignId") int campaignId) {

		ResponseMRAS responseobj = null;
		Response response = null;

		Set<Integer> offerIdObj;
		List<Offer> offerList;

		if (logger.isDebugEnabled()) {
			logger.debug("Parameter received are  AppId "+ appId+"Country code" +countryCode+"campaignId"+campaignId);
		}

		ResponseMRAS responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			return response;
		}

		responseobj = new ResponseMRAS();
	
	offerIdObj = (Set<Integer>) MrasCache.MCache.recover("AllOfferlist" + appId
				+ countryCode+campaignId);

		if (offerIdObj == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("No found in cache putting into cache");
			}
			offerIdObj = rewardResourceDao.getAllOffersDao(appId, countryCode,campaignId);
			if ((offerIdObj.size() != 0)) {
				MrasCache.MCache.admit("AllOfferlist" + appId + countryCode+campaignId,
						offerIdObj, 3600000, 3600000);
			} else {
				// need to change the response
				responseobj
						.setResponseMessage(WebConstant.NO_OFFER_FOUND_APPID);
				responseobj.setResponseCode(434);
				response = Response.status(Response.Status.OK)
						.entity(responseobj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Return response 434 ");
				}
				return response;

			}
		}

		if (((offerIdObj != null))) {
			offerList=new ArrayList<Offer>();
			for (int offerId : offerIdObj) {

				Offer offer = (Offer) MrasCache.MCache.recover("Offer"
						+ offerId);

				if (offer == null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Offer Id :" + offerId
								+ " not found in Cache");
					}
					offer = rewardResourceDao.getOfferWithOfferid(offerId);

					if (offer != null) {
						
						// key for offerId starting from 30000 to get reward
						// object, for getting consumer updates.
						offer.setOfferId(offerId);
						MrasCache.MCache.admit("Offer" + offerId, offer);
						offerList.add(offer);
					}

				} else {
					offer.setOfferId(offerId);
					offerList.add(offer);
				}

			}
			GenericEntity<List<Offer>> listentity = new GenericEntity<List<Offer>>(
					offerList) {
			};

			
			Comparator<Offer> offerComparator=new Comparator<Offer>() {

				@Override
				public int compare(Offer arg0, Offer arg1) {
					
					
					if(arg0.getOfferId()>arg1.getOfferId())
						return 1;
					else if(arg0.getOfferId()>arg1.getOfferId())
						return -1;
					else
						return 0;
				}
			};
			
			Collections.sort(offerList, offerComparator);
			
			
			response = Response.status(Response.Status.OK).entity(listentity)
					.build();

		}
		
		if (logger.isDebugEnabled()) {
			   logger.debug("Return response from last ");
		}
		return response;

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/AgeAffirmation")
	public Response completeInstallation(@Context HttpServletRequest Req,
			com.diageo.mras.webservices.modals.Consumer consumer) {

		Date birthDate = null;
		Response response = null;
		String appId = consumer.getAppId();

		String dateOfBirth = consumer.getDateOfBirth();
		String gatewayid = consumer.getGatewayid();
		int countrycode = consumer.getCountryCode();

		if (logger.isDebugEnabled()) {
			logger.debug("AppId in AgeAffirmation : " + appId);
		}
		// Validating the AppId
		ResponseMRAS responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			return response;
		} else {
			responseObj = new ResponseMRAS();
		}

		if ((dateOfBirth == null || countrycode == 0 || gatewayid == null)) {
			responseObj.setResponseCode(404);
			responseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Return response 404");
			}
			return response;
		}

		try {
			birthDate = (Date) formatter2.parseObject(dateOfBirth);
		} catch (ParseException e) {
			e.printStackTrace();
			logger.info(WebConstant.DATE_FORMAT);

			responseObj.setResponseCode(406);
			responseObj.setResponseMessage(WebConstant.DATE_FORMAT_HOURS);

			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			/*
			 * logger.debug("responseObj for the consumerId: " + consumerId +
			 * " is " + responseObj);
			 */
			if (logger.isDebugEnabled()) {
				   logger.debug("Return response 406");
			}
			return response;
		}

		String countrycodeCache = null;

		if (MrasCache.MCache.recover("countryCode" + countrycode) instanceof String) {
			countrycodeCache = (String) MrasCache.MCache.recover("countryCode"
					+ countrycode);

		}

		if (countrycodeCache == null) {
			logger.debug("country code is null" + countrycode);

			// not found in cache recive from db and update cache

			countrycodeCache = rewardResourceDao.getCountrycode(countrycode);

			if (countrycodeCache == null) {
			
				responseObj.setResponseMessage("INVALID_COUNTRY");
				responseObj.setResponseCode(400);
				response = Response.status(Response.Status.OK)
						.entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Return response 400");
				}
				return response;

			}
			MrasCache.MCache.admit("countryCode" + countrycode,
					countrycodeCache);
		}

		try {

			Client client = Client.create();

			String dobformaated = dateOfBirth.replace("-", "");

			String NEO_URL_AGE_WITH_APPID = NEO_URL_AGE_AFFIRMATION.replace(
					"temp_mras_appId", appId);
			
			if (logger.isDebugEnabled()) {
				logger.debug("calling neo webservice : " );
			}

			WebResource webResource = client.resource(NEO_URL_AGE_WITH_APPID
					+ "countrycode=" + countrycodeCache + "&dateofbirth="
					+ dobformaated + "&gatewayid=" + gatewayid);

			/*
			 * WebResource webResource = client.resource(
			 * "http://209.207.228.8/neowebservices/67131/1.3/consumers/affirmage?"
			 * + "countrycode=" + countrycode + "&dateofbirth=" + dateOfBirth +
			 * "&gatewayid=" + gatewayid);
			 */

			ClientResponse responses = webResource.accept("text/xml").get(
					ClientResponse.class);
			
			if (logger.isDebugEnabled()) {
				logger.debug("getting response neo webservice : " );
			}

			if (responses.getStatus() == 200) {
				responseObj.setResponseCode(200);
				responseObj.setResponseMessage("Successful");
				response = Response.status(Response.Status.OK)
						.entity(responseObj).build();

			} else {

				obj = Neoutils.clientResponse(responses);

				responseObj.setResponseCode(responses.getStatus());
				responseObj.setResponseMessage(obj.getResponse()
						.getResponseCode());
				response = Response.status(Response.Status.OK)
						.entity(responseObj).build();
				
				if (logger.isDebugEnabled()) {
					   logger.debug("Return response from neo "+responseObj.getResponseCode());
				}
				return response;
			}

		} catch (Exception e) {

			e.printStackTrace();
			if (logger.isDebugEnabled()) {
				   logger.debug("Exception occurred "+e.getMessage());
			}
			responseObj.setResponseMessage("Some Unhandled Error has occurred");
			responseObj.setResponseCode(430);
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			

		}
		if (logger.isDebugEnabled()) {
			logger.debug("Returned response " );
		}
		return response;

	}

	public String getProductName(String appid, String phoneNumber,
			String rewardcode) {

		Connection con = null;
		ResultSet rs;
		String productName = null;
		try {
			con = ConnectionPool.getConnection();
			PreparedStatement preparedStatement = con
					.prepareStatement(SqlStatements.GET_PRODUCTNAME);
			
			preparedStatement.setString(1, rewardcode);
			preparedStatement.setString(2, phoneNumber);
			preparedStatement.setString(3, appid);
			rs = preparedStatement.executeQuery();
			while (rs.next()){
				productName = rs.getString("productname");
			}
			rs.close();
			preparedStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return productName;

		// / logger.debug("found in the catche, value for contryid " + value);

	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/WebIssueVoucher")
	/** Description of searchForNearbyOutlets() .
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @param Req       	This is the http request.
	 * @param consumer       	This is the json data as consumer object.
	 * @return                	Issues a voucher.
	 */
	public Response webIssueVoucher(@Context HttpServletRequest Req,
			Consumer consumer) {

		String appId = consumer.getAppId();
		int consumerId = consumer.getConsumerId();
		String phoneNumber = consumer.getPhoneNumber();
		int offerId = consumer.getOfferId();
		
		String URL = consumer.getURL();
		long outletCode = consumer.getOutletCode();
		String productName=consumer.getProductName();
		
		String smsText=consumer.getSmsText();
		
		ResponseMRAS responseobj = null;
		Response response = null;

		if (logger.isDebugEnabled()) {
			logger.info("Parameter received are  AppId" + appId
					+ ", consumerId: " + consumerId + " ,phoneNumber: "
					+ phoneNumber + ", offerId: " + offerId+ ", smsText: " + smsText);

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
		if (phoneNumber == null || offerId == 0 || URL == null||consumerId==0) {
			responseobj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			responseobj.setResponseCode(404);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Return response 404");
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
				   logger.debug("Return response 425");
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
				response = Response.status(Response.Status.OK).entity(responseobj)
						.build();
				
				if (logger.isDebugEnabled()) {
					logger.debug("Invalid offer id , response :- 420" );
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
					rewardCode = String.valueOf(RewardResource.generateRewardCode());
					resultCode = rewardResourceDao.webIssueVoucherDao(
							consumerId, offerId, phoneNumber, URL, outletCode,
							rewardCode, TODAY_LIMIT_WEBISSUEVOUCHER,
							WEEK_LIMIT_WEBISSUEVOUCHER, appId,productName);
				} else {
					rewardCode = "1111";
					resultCode = rewardResourceDao.webIssueVoucherDao(
							consumerId, offerId, phoneNumber, URL, outletCode,
							rewardCode, TODAY_LIMIT_WEBISSUEVOUCHER,
							WEEK_LIMIT_WEBISSUEVOUCHER, appId,productName);
				}

				// generate the reward code..

			} else {
				String result = rewardResourceDao
						.getRewardsCodeFromThirdParty(offerId);
				if (result != null) {
					resultCode = rewardResourceDao.webIssueVoucherDao(
							consumerId, offerId, phoneNumber, URL, outletCode,
							result, TODAY_LIMIT_WEBISSUEVOUCHER,
							WEEK_LIMIT_WEBISSUEVOUCHER, appId,productName);
				} else {
					responseobj
							.setResponseMessage(WebConstant.THIRD_PARTY_VOUCHERCODE_NOT_PRESENT);
					responseobj.setResponseCode(445);
					response = Response.status(Response.Status.OK)
							.entity(responseobj).build();
					
					if (logger.isDebugEnabled()) {
						logger.debug("THIRD_PARTY_VOUCHERCODE_NOT_PRESENT , response :- 445" );
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
				logger.debug("INVALID_OFFERID , response :- 420" );
			}
			return response;
		}

	

		switch (resultCode) {
		case 200: {
			Thread t = new Thread(new ExecuteSmsService(phoneNumber, URL,
					rewardCode, outletCode, offerId,smsText));
			t.setName("SMSThread");
			t.start();
			responseobj.setResponseMessage(WebConstant.SMS_SUCESS_Voucher);
			responseobj.setResponseCode(200);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Return response SMS_SUCESS_Voucher , consumerId "+consumerId);
			}
			return response;
		}
		case 420: {
			responseobj.setResponseMessage(WebConstant.INVALID_OFFERID);
			responseobj.setResponseCode(420);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				logger.debug("INVALID_OFFERID , response :- 420" );
			}
			return response;
		}
		case 432: {
			responseobj.setResponseMessage(WebConstant.ISSUE_LIMIT_TODAY);
			responseobj.setResponseCode(432);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				logger.debug("ISSUE_LIMIT_TODAY , response :- 432" );
			}
			return response;
		}

		case 433: {
			responseobj.setResponseMessage(WebConstant.ISSUE_LIMIT_WEEK);
			responseobj.setResponseCode(433);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				logger.debug("ISSUE_LIMIT_WEEK , response :- 433" );
			}
			return response;
		}
		case 437: {
			responseobj.setResponseMessage(WebConstant.OFFER_MAXIMUM_LIMIT_COMPLETE);
			responseobj.setResponseCode(437);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				logger.debug("OFFER_MAXIMUM_LIMIT_COMPLETE , response :- 437" );
			}
			return response;
		}
		case 414: {
			responseobj.setResponseMessage(WebConstant.INVALID_OUTLET_CODE);
			responseobj.setResponseCode(414);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				logger.debug("INVALID_OUTLET_CODE , response :- 414" );
			}
			return response;
		}
		case 447: {

			responseobj.setResponseCode(447);
			responseobj.setResponseMessage("PhoneNumber is not registered");
			response = Response.status(200).entity(responseobj).build();
			if (logger.isDebugEnabled()) {
				logger.debug("PhoneNumber is not registered , response :- 447" );
			}
			return response;
		}
		default: {
			responseobj.setResponseMessage(WebConstant.UNHANDLED_ERROR);
			responseobj.setResponseCode(430);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				logger.debug("UNHANDLED_ERROR , response :- 430" );
			}
			return response;
		}
		}

	}
	
}
