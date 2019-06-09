package com.diageo.mras.webservices.services;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import com.diageo.mras.cache.MrasCache;
import com.diageo.mras.scheduler.dao.SchedulerDAO;
import com.diageo.mras.webservices.dao.RewardResourceDao;
import com.diageo.mras.webservices.init.MrasConstants;
import com.diageo.mras.webservices.init.PropertyReader;
import com.diageo.mras.webservices.modals.BrandListModal;
import com.diageo.mras.webservices.modals.PreferencesNew;
import com.diageo.mras.webservices.modals.QuestionCategoryNew;
import com.diageo.mras.webservices.modals.QuestionMultipleAnswers;
import com.diageo.mras.webservices.responses.Answer;
import com.diageo.mras.webservices.responses.Consumer;
import com.diageo.mras.webservices.responses.ConsumerProfile;
import com.diageo.mras.webservices.responses.LoginCredentials;
import com.diageo.mras.webservices.responses.NeoEmailBean;
import com.diageo.mras.webservices.responses.NeoPhoneBean;
import com.diageo.mras.webservices.responses.ResponseMRAS;
import com.diageo.mras.webservices.responses.Responses;
import com.diageo.mras.webservices.responses.SecretQuestions;
import com.diageo.mras.webservices.responses.UserAccount;
import com.diageo.mras.webservices.responses.WrapperResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Path("/Wrapper")
public class WrapperNeoWebServices {
	private static String NEO_URL_AGE_AFFIRMATION = PropertyReader
			.getPropertyValue(MrasConstants.NEO_URL_AGE_AFFIRMATION);
	private static String NEO_resigtration_reponse = PropertyReader
			.getPropertyValue(MrasConstants.NEO_resigtration_reponse);
	private static String NEO_URL_CONSUMER_REGISTRATION = PropertyReader
			.getPropertyValue(MrasConstants.NEO_URL_CONSUMER_REGISTRATION);
	private static String NEO_URL_USER_ACCOUNT = PropertyReader
			.getPropertyValue(MrasConstants.NEO_URL_USER_ACCOUNT);
	private static Client client = Client.create();

	private static final Logger logger = Logger
			.getLogger(WrapperNeoWebServices.class.getName());
	private static final RewardResourceDao rewardResourceDao = new RewardResourceDao();
	private static final RewardResource rewardResource = new RewardResource();
	private static final String pattern = "[+|0]?\\d{2,3}\\s?[-]?\\d{2,4}\\s?[-]?\\d{3,6}";
	private static final Format formatter2 = new SimpleDateFormat("yyyy-MM-dd");
	private static final Format formatter3 = new SimpleDateFormat("yyyyMMdd");
	private static final String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*"
			+ "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	Response response = null;
	Responses obj = null;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/CompleteRegistration")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response completeRegistration(@Context HttpServletRequest Req,
			com.diageo.mras.webservices.modals.Consumer consumerobj) {

		String appId = consumerobj.getAppId();
		String deviceId = consumerobj.getDeviceId();
		int deviceType = consumerobj.getDeviceType();
		String phoneNumber = consumerobj.getPhoneNumber();
		String emailId = consumerobj.getEmailId();
		String dateOfBirth = consumerobj.getDateOfBirth();
		String gender = consumerobj.getGender();
		String firstname = consumerobj.getFirstName();
		String lastName = consumerobj.getLastName();
		String countrycode = Integer.toString(consumerobj.getCountryCode());
		// String optionId = consumerobj.getOptionId();
		String modifyFlag = consumerobj.getModifyFlag();
		// String communicationChannel = consumerobj.getCommunicationChannel();
		String answerText = consumerobj.getAnswerText();
		// String brandId = consumerobj.getBrandId();
		String loginName = consumerobj.getLoginName();
		String passWord = consumerobj.getPassword();
		String promoCode = consumerobj.getPromoCode();
		List<BrandListModal> commuChannelList = consumerobj
				.getCommuChannelList();



		// notification id to be removed
		if (logger.isDebugEnabled()) {
			   logger.debug("completeRegistration parameter recieve emailId "+emailId+" ,dateOfBirth "+dateOfBirth);
		}
		int notificationId = 1;

		ResponseMRAS responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(200).entity(responseObj).build();
			return response;
		} else {
			responseObj = new ResponseMRAS();
		}
		if ((deviceId == null) || (deviceType == 0) || (emailId == null)
				|| countrycode == null || (phoneNumber == null)
				|| firstname == null || lastName == null || loginName == null
				|| passWord == null || (dateOfBirth == null)
				|| commuChannelList == null || answerText == null
				|| modifyFlag == null || promoCode == null) {

			responseObj.setResponseCode(404);
			responseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			response = Response.status(200).entity(responseObj).build();
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
				response = Response.status(200).entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Return response getCommChannel missing 404");
				}
				return response;
			}

		}

		if (!phoneNumber.matches(pattern)) {
			responseObj.setResponseCode(425);
			responseObj.setResponseMessage("Wrong PhoneNumber format");
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Return response 425");
			}
			return response;
		}
		if (!emailId.matches(emailPattern)) {
			responseObj.setResponseCode(437);
			responseObj.setResponseMessage("Wrong Email format");
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Return response 437");
			}
			return response;
		}

		Date birthDate = null;
		try {
			birthDate = (Date) formatter2.parseObject(dateOfBirth);
		} catch (ParseException e) {
			logger.info(WebConstant.DATE_FORMAT);
			e.printStackTrace();
			responseObj.setResponseCode(406);
			responseObj.setResponseMessage(WebConstant.DATE_FORMAT);
			response = Response.status(200).entity(responseObj).build();
			return response;
		}

		try {
			JAXBContext contextObj = JAXBContext.newInstance(Consumer.class);
			Marshaller marshallerObj = contextObj.createMarshaller();
			marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// By dhananjay
			/*
			 * ArrayList<QuestionCategory> questionCategorysListCL = new
			 * ArrayList<QuestionCategory>(); // ArrayList<QuestionAnswers>
			 * questionAnswersListCL = new // ArrayList<QuestionAnswers>();
			 * ArrayList<QuestionAnswers> questionAnswersListCL1 = new
			 * ArrayList<QuestionAnswers>(); ArrayList<QuestionAnswers>
			 * questionAnswersListCL4 = new ArrayList<QuestionAnswers>();
			 * NeoEmailBean eobj = new NeoEmailBean(emailId, "1", "1",
			 * modifyFlag); NeoPhoneBean phoneobj = new
			 * NeoPhoneBean(phoneNumber, "3", modifyFlag); ConsumerProfile
			 * consumerProfile = new ConsumerProfile(firstname, lastName,
			 * dateOfBirth, phoneobj, promoCode, eobj);
			 * 
			 * 
			 * Answer answerDO68 = new Answer("2", "I", "email email", "10",
			 * "1");
			 * 
			 * QuestionAnswers questionAnswersDO68 = new QuestionAnswers(68,
			 * answerDO68); questionAnswersListCL.add(questionAnswersDO68);
			 * 
			 * Answer answerDO69 = new Answer("3", "I", "email email", "10",
			 * "1");
			 * 
			 * QuestionAnswers questionAnswersDO69 = new QuestionAnswers(69,
			 * answerDO69); questionAnswersListCL.add(questionAnswersDO69);
			 * 
			 * 
			 * Answer answerDO64 = new Answer(optionId, modifyFlag, null,
			 * brandId, communicationChannel);
			 * 
			 * QuestionAnswers questionAnswersDO64 = new QuestionAnswers(64,
			 * answerDO64);
			 * 
			 * questionAnswersListCL1.add(questionAnswersDO64);
			 * 
			 * Answer answerDO92 = new Answer(countrycode, modifyFlag, null,
			 * null, null);
			 * 
			 * QuestionAnswers questionAnswersDO92 = new QuestionAnswers(92,
			 * answerDO92); questionAnswersListCL4.add(questionAnswersDO92);
			 * 
			 * QuestionCategory questionCategoryDO6 = new QuestionCategory(6,
			 * questionAnswersListCL);
			 * 
			 * 
			 * // questionCategorysListCL.add(questionCategoryDO6);
			 * QuestionCategory questionCategoryDO1 = new QuestionCategory(1,
			 * questionAnswersListCL1); QuestionCategory questionCategoryDO4 =
			 * new QuestionCategory(4, questionAnswersListCL4);
			 * 
			 * questionCategorysListCL.add(questionCategoryDO4);
			 * 
			 * questionCategorysListCL.add(questionCategoryDO1);
			 * 
			 * Preferences pobj = new Preferences(questionCategorysListCL);
			 * LoginCredentials lobj = new LoginCredentials(loginName,
			 * passWord);
			 * 
			 * Answer objanswer = new Answer(null, modifyFlag, answerText, null,
			 * null); SecretQuestions ss = new SecretQuestions(62, objanswer);
			 * UserAccount uobj = new UserAccount(lobj, ss);
			 * 
			 * Consumer consumer = new Consumer(consumerProfile, pobj, uobj);
			 * 
			 * StringWriter writer = new StringWriter();
			 * marshallerObj.marshal(consumer, writer);
			 */

			// by Sachin

			ArrayList<QuestionCategoryNew> questionCategorysListCL = new ArrayList<QuestionCategoryNew>();
			// ArrayList<QuestionAnswers> questionAnswersListCL = new
			// ArrayList<QuestionAnswers>();
			ArrayList<QuestionMultipleAnswers> questionAnswersListCL1 = new ArrayList<QuestionMultipleAnswers>();
			ArrayList<QuestionMultipleAnswers> questionAnswersListCL4 = new ArrayList<QuestionMultipleAnswers>();
			NeoEmailBean eobj = new NeoEmailBean(emailId, "1", "1", modifyFlag);

			NeoPhoneBean phoneobj = new NeoPhoneBean(phoneNumber, "3",
					modifyFlag);
			ConsumerProfile consumerProfile = new ConsumerProfile(firstname,
					lastName, dateOfBirth, phoneobj, promoCode, eobj);

			ArrayList<Answer> answerListDO64 = new ArrayList<Answer>();

			for (BrandListModal brandModal : commuChannelList) {
				String optionForBrand = brandModal.getOptionId();

				if (null == optionForBrand) {
					optionForBrand = "1";
				}

				Answer answerDO64 = new Answer(optionForBrand, modifyFlag,
						null, brandModal.getBrandId(),
						brandModal.getCommChannel());

				answerListDO64.add(answerDO64);

			}

			/*
			 * Answer answerDO64 = new Answer(optionId, modifyFlag, null,
			 * brandId, communicationChannel);
			 */

			QuestionMultipleAnswers questionAnswersDO64 = new QuestionMultipleAnswers(
					64, answerListDO64);

			questionAnswersListCL1.add(questionAnswersDO64);

			ArrayList<Answer> answerListDO92 = new ArrayList<Answer>();

			Answer answerDO92 = new Answer(countrycode, modifyFlag, null, null,
					null);
			answerListDO92.add(answerDO92);
			QuestionMultipleAnswers questionAnswersDO92 = new QuestionMultipleAnswers(
					92, answerListDO92);
			questionAnswersListCL4.add(questionAnswersDO92);

			// questionCategorysListCL.add(questionCategoryDO6);
			QuestionCategoryNew questionCategoryDO1 = new QuestionCategoryNew(
					1, questionAnswersListCL1);
			QuestionCategoryNew questionCategoryDO4 = new QuestionCategoryNew(
					4, questionAnswersListCL4);

			questionCategorysListCL.add(questionCategoryDO4);

			questionCategorysListCL.add(questionCategoryDO1);

			PreferencesNew pobj = new PreferencesNew(questionCategorysListCL);
			LoginCredentials lobj = new LoginCredentials(loginName, passWord);

			Answer objanswer = new Answer(null, modifyFlag, answerText, null,
					null);
			SecretQuestions ss = new SecretQuestions(62, objanswer);
			UserAccount uobj = new UserAccount(lobj, ss);

			Consumer consumer = new Consumer(consumerProfile, pobj, uobj);

			StringWriter writer = new StringWriter();
			marshallerObj.marshal(consumer, writer);

			// Sachin code ends
			if (logger.isInfoEnabled()) {
				logger.info("XML as input to neo is :- "+writer.toString());
			}

			try {
				// Client client = Client.create();

				String NEO_URL_WITH_APPID = NEO_URL_CONSUMER_REGISTRATION
						.replace("temp_mras_appId", appId);
				if(logger.isDebugEnabled()){
				logger.debug("Calling Neo Webservice with appid for create consumer  " + appId);
				}
				WebResource webResource = client.resource(NEO_URL_WITH_APPID);
				ClientResponse responseobj = webResource.type("text/xml").post(

				ClientResponse.class, writer.toString());

				if (responseobj.getStatus() == 201) {
					if (logger.isDebugEnabled()) {
					logger.debug("getting response Neo Webservice with appid for create consumer  " + 201);
					}
					// The output recieved need to be parsed to get consumerid

					String location = responseobj.getLocation().toString();
					if (logger.isDebugEnabled()) {
					logger.debug("location >>>>" + location);
					}
					int left = location.indexOf("consumers");

					int right = location
							.indexOf("/" + NEO_resigtration_reponse);

					if (right < 0) {
						right = location.indexOf("/registration");
					}

					int consumerId = Integer.parseInt(location.substring(
							left + 10, right));
					if(logger.isDebugEnabled()){
					logger.debug("Calling neo webservice to check consumerRegister or not with consumerid: "
							+ consumerId);
					}
					WebResource webResourceconfirmreg = client
							.resource(NEO_URL_WITH_APPID + "/" + consumerId
									+ "/registration");

					ClientResponse responseobjConfirmReg = webResourceconfirmreg
							.type("text/xml").post(ClientResponse.class,
									writer.toString());
					if (logger.isInfoEnabled()) {
					logger.info("ConsumerId generated from NeoConsumerRegistration Web service"
							+ consumerId);
					}
					// Hardcoding consumerId for testing
					// int consumerId = 21234;
					if (responseobjConfirmReg.getStatus() == 200) {
						if(logger.isDebugEnabled()){
						logger.debug("got response 200 from neo webservice to check consumerRegister ");
						}
						int resultResponse = rewardResourceDao
								.capturedeviceDetails(consumerId, deviceId,
										deviceType, appId, notificationId,
										phoneNumber, birthDate, gender,
										emailId, firstname, lastName,
										consumerobj.getCountryCode());

						switch (resultResponse) {
						case 200: {
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
								response = Response.status(200)
										.entity(responseObj).build();
								if (logger.isDebugEnabled()) {
									   logger.debug("Exception occurred , Return response 430");
								}
								return response;
							}
							WrapperResponse responseneoobj = new WrapperResponse();

							responseneoobj.setConsumerId(consumerId);

							response = Response.status(200)
									.entity(responseneoobj).build();
							if (logger.isDebugEnabled()) {
								   logger.debug("Return response 430");
							}
							return response;
						}
						case 403: {
							responseObj.setResponseCode(418);
							responseObj
									.setResponseMessage(WebConstant.INVALID_DEVICEID);

							response = Response.status(200).entity(responseObj)
									.build();
							if (logger.isDebugEnabled()) {
								   logger.debug("Return response 403");
							}

							return response;
						}
						case 441: {
							responseObj.setResponseCode(426);
							responseObj
									.setResponseMessage(WebConstant.INVALID_PHONENUMBER_441);
							response = Response.status(200).entity(responseObj)
									.build();
							if (logger.isDebugEnabled()) {
								   logger.debug("Return response 441");
							}

							return response;
						}
						case 419: {
							responseObj.setResponseCode(419);
							responseObj
									.setResponseMessage(WebConstant.DEVICE_ID_ALREADY_EXISTS);
							response = Response.status(200).entity(responseObj)
									.build();
							if (logger.isDebugEnabled()) {
								   logger.debug("Return response 419");
							}

							return response;
						}
						}

					}

					else {
						if(logger.isDebugEnabled()){
						logger.debug("response from neo webservice to check consumerRegister " +responseobjConfirmReg.getStatus());
						}
						obj = clientResponse(responseobjConfirmReg);
						responseObj.setResponseCode(responseobjConfirmReg
								.getStatus());
						responseObj.setResponseMessage(obj.getResponse()
								.getResponseCode());
						response = Response.status(200).entity(responseObj)
								.build();
						if (logger.isDebugEnabled()) {
							   logger.debug("Return response");
						}
						return response;

					}

				} else {
					if(logger.isDebugEnabled()){
					logger.debug("getting response Neo Webservice with appid for create consumer  " + responseobj.getStatus());
					}
					obj = clientResponse(responseobj);

					if (obj.getResponse().getResponseCode()
							.equalsIgnoreCase("LOGINNAME_ALREADY_EXISTS")
							|| obj.getResponse()
									.getResponseCode()
									.equalsIgnoreCase(
											"EMAIL_ID_AND_DOB_COMBINATION_ALREADY_EXISTS")) {
						// the required call to get consumerID from neo.
						
						
						StringBuffer NEO_URL_WITH_APPID_USER_ACCOUNT = new StringBuffer(NEO_URL_USER_ACCOUNT
								.replace("temp_mras_appId", appId));
						WebResource webResourceUserAccount = client
								.resource(NEO_URL_WITH_APPID_USER_ACCOUNT.append(
										"loginname=").append(loginName).append(
										"&password=").append(passWord).append(
										"&promocode=").append(promoCode).toString());

						ClientResponse responseobjUserAccount = webResourceUserAccount
								.accept("text/xml").get(ClientResponse.class);
						if(logger.isDebugEnabled()){
						logger.debug("After calling user account "+responseobjUserAccount.getStatus());
						}
						
						if (responseobjUserAccount.getStatus() == 200) {

							// ===============Added by sachin

							int consumerId = Integer
									.parseInt(responseobjUserAccount
											.getEntity(String.class));

							ConsumerProfile consumerProfileUpdate = new ConsumerProfile(
									null, null, null, null, promoCode, null);

							for (Answer answerDO64 : answerListDO64) {
								answerDO64.setModifyFlag("M");
							}

							for (Answer answerDO921 : answerListDO92) {
								answerDO921.setModifyFlag("M");
							}

							consumer.setConsumerProfile(consumerProfileUpdate);
							uobj.setLogincredentials(null);
							uobj.setSecretquestions(null);
							consumer.setUseraccount(uobj);
							consumer.setPreferences(pobj);
							;
							StringWriter writer1 = new StringWriter();

							marshallerObj.marshal(consumer, writer1);

							// Sachin code ends
							if (logger.isInfoEnabled()) {
								logger.info("XML as input to neo if user already exists:- "+writer1.toString());
							}
							

							// Re-registartion!!!!!!!!!!!!! logic for updating
							// profile.only
							// preference will be updated
							if (logger.isInfoEnabled()) {
							logger.info("calling neo webservice  update consumer profile "+NEO_URL_WITH_APPID);
							}
							WebResource webResourceupdateConsumer = client
									.resource(NEO_URL_WITH_APPID + "/"
											+ consumerId + "/");
							ClientResponse responseobjupdateConsumer = webResourceupdateConsumer
									.type("text/xml").put(

									ClientResponse.class, writer1.toString());

							if (responseobjupdateConsumer.getStatus() == 200) {
								if (logger.isInfoEnabled()) {
								logger.info("Details updated succesfully.Response from neo update consumer profile is 200");
								}
							}

							else {

								obj = clientResponse(responseobjupdateConsumer);
								if (logger.isInfoEnabled()) {
								logger.info("Response form NEOupdateconsumer"
										+ obj.getResponse().getResponseCode());

								logger.info("Response form NEOupdateconsumer"
										+ responseobjupdateConsumer.getStatus());
								}

							}
							int resultResponse = rewardResourceDao
									.capturedeviceDetails(consumerId, deviceId,
											deviceType, appId, notificationId,
											phoneNumber, birthDate, gender,
											emailId, firstname, lastName,
											consumerobj.getCountryCode());
							if (logger.isInfoEnabled()) {
							logger.info("response from capturedevicedetails if not captured previously"
									+ resultResponse);
							}

							// ========ends by Sachin

							WrapperResponse responseneoobj = new WrapperResponse();

							responseneoobj.setConsumerId(consumerId);

							response = Response.status(200)
									.entity(responseneoobj).build();
							if (logger.isDebugEnabled()) {
								   logger.debug("Return response 200");
							}
							return response;
						}

						else {
							if (logger.isInfoEnabled()) {
							logger.info("getting response 200 from neo webservice to check consumerRegister "+responseobjUserAccount.getStatus() );
							}
							obj = clientResponse(responseobjUserAccount);

							responseObj.setResponseCode(responseobjUserAccount
									.getStatus());
							responseObj.setResponseMessage(obj.getResponse()
									.getResponseCode());
							response = Response.status(200).entity(responseObj)
									.build();
							if (logger.isDebugEnabled()) {
								   logger.debug("Return response");
							}
							return response;

						}

					}

					else {

						responseObj.setResponseCode(responseobj.getStatus());
						responseObj.setResponseMessage(obj.getResponse()
								.getResponseCode());
						response = Response.status(200).entity(responseObj)
								.build();
						if (logger.isDebugEnabled()) {
							   logger.debug("Return response");
						}
						return response;
					}
				}

			}

			catch (Exception e) {
				if (logger.isInfoEnabled()) {
					logger.info("Exception : "+e.getMessage());
				}
				e.printStackTrace();
				responseObj
						.setResponseMessage("Some Unhandled Error has occurred");
				responseObj.setResponseCode(430);
				response = Response.status(200).entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Return response 430");
				}
				return response;
			
			}

		}

		catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.info("Exception : "+e.getMessage());
			}
			e.printStackTrace();
			responseObj.setResponseMessage("Some Unhandled Error has occurred");
			responseObj.setResponseCode(430);
			response = Response.status(200).entity(responseObj).build();
			return response;
			
		}
		if (logger.isDebugEnabled()) {
			   logger.debug("Return response from last.");
		}
		return response;

	}

	public Responses clientResponse(ClientResponse responseobjConfirmReg)
			throws JAXBException {

		String output = responseobjConfirmReg.getEntity(String.class);
		JAXBContext contextObj1 = JAXBContext.newInstance(Responses.class);
		Unmarshaller uuobj = contextObj1.createUnmarshaller();

		StringBuffer xmlStr = new StringBuffer(output);
		obj = (Responses) uuobj.unmarshal(new StreamSource(new StringReader(
				xmlStr.toString())));
		if (logger.isInfoEnabled()) {
		logger.info("Message recieved from server is"
				+ obj.getResponse().getResponseCode());
		logger.info("Output from Server .... \n");
		logger.info(output);
		}
		return obj;

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/CompleteInstallation")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response completeInstallation(@Context HttpServletRequest Req,
			com.diageo.mras.webservices.modals.Consumer consumer) {

		String appId = consumer.getAppId();
		String deviceId = consumer.getDeviceId();
		String dateOfBirth = consumer.getDateOfBirth();
		String gatewayid = consumer.getGatewayid();
		int countrycode = consumer.getCountryCode();

		int consumerid = 0;

		Date birthDate = null;
		Response response = null;
		if (logger.isDebugEnabled()) {
			logger.debug("AppId : " + appId + " ,DeviceId: " + deviceId+" ,dateOfBirth "+dateOfBirth);
		}
		// Validating the AppId
		ResponseMRAS responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(200).entity(responseObj).build();
			return response;
		} else {
			responseObj = new ResponseMRAS();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("After validating appid");
		}
		
		if ((deviceId == null || dateOfBirth == null || countrycode == 0 || gatewayid == null)) {
			responseObj.setResponseCode(404);
			responseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Return response 404");
			}
			return response;
		}

		
		
		try {
			birthDate = (Date) formatter2.parseObject(dateOfBirth);
		} catch (ParseException e) {
			if (logger.isInfoEnabled()) {
				logger.info("ParseException : "+e.getMessage());
				logger.info(WebConstant.DATE_FORMAT);
			}
			
			e.printStackTrace();
			responseObj.setResponseCode(406);
			responseObj.setResponseMessage(WebConstant.DATE_FORMAT_HOURS);

			response = Response.status(200).entity(responseObj).build();
			/*
			 * logger.debug("responseObj for the consumerId: " + consumerId +
			 * " is " + responseObj);
			 */
			return response;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("After validating params and parsing DOB");
		}
		
		String countrycodeCache = null;

		if (MrasCache.MCache.recover("countryCode" + countrycode) instanceof String) {
			countrycodeCache = (String) MrasCache.MCache.recover("countryCode"
					+ countrycode);

		}

		
		if (countrycodeCache == null) {
			
			if (logger.isDebugEnabled()) {
			logger.debug("country code is null in cache" + countrycode);
			}
			// not found in cache recive from db and update cache

			countrycodeCache = rewardResourceDao.getCountrycode(countrycode);

			if (countrycodeCache == null) {
				if (logger.isDebugEnabled()) {
				logger.debug("countrycode is not valid");
				}
				responseObj.setResponseMessage("INVALID_COUNTRY");
				responseObj.setResponseCode(400);
				response = Response.status(200).entity(responseObj).build();
				return response;

			}
			MrasCache.MCache.admit("countryCode" + countrycode,
					countrycodeCache,864000000,86400000);
		}

		
		if (logger.isDebugEnabled()) {
			logger.debug("After validating country code");
		}
		
		try {

			
			if (logger.isDebugEnabled()) {
				logger.debug("After creating client");
			}
			String dobformaated = dateOfBirth.replace("-", "");

			StringBuffer NEO_URL_AGE_WITH_APPID = new StringBuffer(
					NEO_URL_AGE_AFFIRMATION.replace("temp_mras_appId", appId));

			if (logger.isDebugEnabled()) {
			logger.debug("Calling neo affirmage DOB is " + dateOfBirth);
			}
			WebResource webResource = client.resource(NEO_URL_AGE_WITH_APPID
					.append("countrycode=").append(countrycodeCache)
					.append("&dateofbirth=").append(dobformaated)
					.append("&gatewayid=").append(gatewayid).toString());

			ClientResponse responses = webResource.accept("text/xml").get(
					ClientResponse.class);

			if (logger.isDebugEnabled()) {
			logger.debug("getting response from neo affirmage is "
					+ responses.getStatus());
			}
			if (responses.getStatus() == 200) {

				int offerId = rewardResourceDao.InstallationOfferId(appId);

				if (offerId == 0) {

					responseObj.setResponseCode(435);
					responseObj
							.setResponseMessage(WebConstant.No_Installationoffer_for_this_AppID);
					response = Response.status(200).entity(responseObj).build();
					if (logger.isDebugEnabled()) {
						   logger.debug("Response returned 435");
					}
					return response;
					}


				int responseDao = SchedulerDAO.issueVoucher(consumerid,
						offerId, deviceId, RewardResource.generateRewardCode());

				switch (responseDao) {
				case 405: {
					responseObj.setResponseCode(responseDao);
					responseObj
							.setResponseMessage(WebConstant.INVALID_CONSUMERID);
					break;
				}
				case 406: {
					responseObj.setResponseCode(420);
					responseObj.setResponseMessage(WebConstant.INVALID_OFFERID);
					break;
				}
				case 403: {
					responseObj.setResponseCode(419);
					responseObj
							.setResponseMessage(WebConstant.DEVICE_ID_ALREADY_EXISTS);
					break;
				}
				case 435: {
					responseObj.setResponseCode(435);
					responseObj
							.setResponseMessage(WebConstant.CHECK_IN_REQUIRED);
					break;
				}

				case 437: {
					responseObj.setResponseCode(437);
					responseObj
							.setResponseMessage(WebConstant.OFFER_MAXIMUM_LIMIT_COMPLETE);
					break;

				}

				default: {

					if (logger.isInfoEnabled()) {
					logger.info("Response recevied from DL layer for issue voucher for consumerId "
							+ consumerid + " is " + responseDao);
					}
					responseObj.setResponseCode(responseDao);
					responseObj.setResponseMessage(WebConstant.SUCCESSFUL);
					break;
				}

				}
				response = Response.status(200).entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned");
				}
				return response;

			} else {
				String output = responses.getEntity(String.class);

				JAXBContext contextObj = JAXBContext
						.newInstance(Responses.class);
				Unmarshaller uu = contextObj.createUnmarshaller();

				// StringBuffer xmlStr = new StringBuffer(output);
				Responses obj = (Responses) uu.unmarshal(new StreamSource(
						new StringReader(output)));
				if (logger.isInfoEnabled()) {
				logger.info("Message recieved from server is"
						+ obj.getResponse().getResponseCode());

				logger.info("Output from Server .... \n");
				}
				responseObj.setResponseCode(responses.getStatus());
				responseObj.setResponseMessage(obj.getResponse()
						.getResponseCode());
				response = Response.status(200).entity(responseObj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned");
				}
				return response;
			}

		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
			logger.info("Exception : "+e.getMessage());
			}
			e.printStackTrace();
			responseObj.setResponseMessage("Some Unhandled Error has occurred");
			responseObj.setResponseCode(430);
			response = Response.status(200).entity(responseObj).build();

		}
		if (logger.isDebugEnabled()) {
			   logger.debug("Return response from last.");
		}
		return response;

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/CacheClearPrivate")
	public Response clearCache() {

		ResponseMRAS responseobj = new ResponseMRAS();
		Response response = null;
		if (logger.isDebugEnabled()) {
		logger.debug(" in clearing the Cache");
		}
		MrasCache.MCache.clear();
		responseobj.setResponseCode(200);
		responseobj.setResponseMessage("Sucessfully cleared");
		response = Response.status(200).entity(responseobj).build();

		return response;

	}
	/*
	 * public int DeleteRecord(String phoneNumber, String emailId, String appId)
	 * { Connection con = null; int result = 0; CallableStatement stmt = null;
	 * 
	 * 
	 * try { con = ConnectionPool.getConnection(); stmt = con
	 * .prepareCall("{ call deleterecords(?,?,?,?)}"); stmt.setString(1,
	 * phoneNumber); stmt.setString(2, emailId); stmt.setString(3, appId);
	 * 
	 * stmt.registerOutParameter(4, java.sql.Types.INTEGER);
	 * stmt.executeQuery(); result = stmt.getInt(4); stmt.close(); } catch
	 * (SQLException e) { e.printStackTrace(); } finally {
	 * ConnectionPool.returnConnection(con); } return result; }
	 */

}
