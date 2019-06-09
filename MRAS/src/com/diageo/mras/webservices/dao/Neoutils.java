package com.diageo.mras.webservices.dao;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

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
import com.diageo.mras.webservices.responses.Responses;
import com.diageo.mras.webservices.responses.SecretQuestions;
import com.diageo.mras.webservices.responses.UserAccount;
import com.diageo.mras.webservices.services.ValidationDateEmail;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class Neoutils {

	private static String NEO_URL_AGE_AFFIRMATION = PropertyReader
			.getPropertyValue(MrasConstants.NEO_URL_AGE_AFFIRMATION);
	private static String NEO_resigtration_reponse = PropertyReader
			.getPropertyValue(MrasConstants.NEO_resigtration_reponse);
	private static String NEO_URL_CONSUMER_REGISTRATION = PropertyReader
			.getPropertyValue(MrasConstants.NEO_URL_CONSUMER_REGISTRATION);
	private static String NEO_URL_USER_ACCOUNT = PropertyReader
			.getPropertyValue(MrasConstants.NEO_URL_USER_ACCOUNT);
	static Client client = Client.create();
	private static Logger logger = Logger
			.getLogger(Neoutils.class.getName());
	public static ClientResponse neoConsumerReg(StringWriter writer,
			String appId) {
		ClientResponse responseobj = null;
		try {

			String NEO_URL_WITH_APPID = NEO_URL_CONSUMER_REGISTRATION.replace(
					"temp_mras_appId", appId);

			/*
			 * WebResource webResource = client
			 * .resource(NEO_URL_CONSUMER_REGISTRATION);
			 */
			WebResource webResource = client.resource(NEO_URL_WITH_APPID);
			responseobj = webResource.type("text/xml").post(

			ClientResponse.class, writer.toString());

		}

		catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Exception   in neoConsumerReg method."+e.getMessage());
			}
			e.printStackTrace();
		}
		return responseobj;

	}

	public static StringWriter neoXmlGenerator(String emailId,
			String modifyFlag, String phoneNumber,
			List<BrandListModal> commuChannelList, String firstname,
			String lastName, String dateOfBirth, String promoCode,
			String countrycode, String loginName, String passWord,
			String answerText, boolean updateFlag) {
		StringWriter writer = new StringWriter();
		try {

			if (updateFlag == true) {
				modifyFlag = "M";
				JAXBContext contextObj = JAXBContext
						.newInstance(Consumer.class);
				Marshaller marshallerObj = contextObj.createMarshaller();
				marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
						true);

				ArrayList<QuestionCategoryNew> questionCategorysListCL = new ArrayList<QuestionCategoryNew>();
				// ArrayList<QuestionAnswers> questionAnswersListCL = new
				// ArrayList<QuestionAnswers>();
				ArrayList<QuestionMultipleAnswers> questionAnswersListCL1 = new ArrayList<QuestionMultipleAnswers>();
				ArrayList<QuestionMultipleAnswers> questionAnswersListCL4 = new ArrayList<QuestionMultipleAnswers>();
				ConsumerProfile consumerProfile = new ConsumerProfile(null,
						null, null, null, promoCode, null);

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

				Answer answerDO92 = new Answer(countrycode, modifyFlag, null,
						null, null);
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

				PreferencesNew pobj = new PreferencesNew(
						questionCategorysListCL);

				Consumer consumer = new Consumer(consumerProfile, pobj, null);
				marshallerObj.marshal(consumer, writer);
			}

			else {

				JAXBContext contextObj = JAXBContext
						.newInstance(Consumer.class);
				Marshaller marshallerObj = contextObj.createMarshaller();
				marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
						true);

				ArrayList<QuestionCategoryNew> questionCategorysListCL = new ArrayList<QuestionCategoryNew>();
				// ArrayList<QuestionAnswers> questionAnswersListCL = new
				// ArrayList<QuestionAnswers>();
				ArrayList<QuestionMultipleAnswers> questionAnswersListCL1 = new ArrayList<QuestionMultipleAnswers>();
				ArrayList<QuestionMultipleAnswers> questionAnswersListCL4 = new ArrayList<QuestionMultipleAnswers>();
				NeoEmailBean eobj = new NeoEmailBean(emailId, "1", "1",
						modifyFlag);

				NeoPhoneBean phoneobj = new NeoPhoneBean(phoneNumber, "3",
						modifyFlag);
				ConsumerProfile consumerProfile = new ConsumerProfile(
						firstname, lastName, dateOfBirth, phoneobj, promoCode,
						eobj);

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

				Answer answerDO92 = new Answer(countrycode, modifyFlag, null,
						null, null);
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

				PreferencesNew pobj = new PreferencesNew(
						questionCategorysListCL);
				LoginCredentials lobj = new LoginCredentials(loginName,
						passWord);

				Answer objanswer = new Answer(null, modifyFlag, answerText,
						null, null);

				SecretQuestions ss = new SecretQuestions(62, objanswer);
				UserAccount uobj = new UserAccount(lobj, ss);
				Consumer consumer = new Consumer(consumerProfile, pobj, uobj);
				marshallerObj.marshal(consumer, writer);

			}
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Exception   in neoXmlGenerator method."+e.getMessage());
			}
			e.printStackTrace();
		}
		return writer;

	}

	public static ClientResponse neoConfirmReg(int consumerId,
			StringWriter writer, String appId) {
		String NEO_URL_WITH_APPID = NEO_URL_CONSUMER_REGISTRATION.replace(
				"temp_mras_appId", appId);

		WebResource webResourceconfirmreg = client.resource(NEO_URL_WITH_APPID
				+ "/" + consumerId + "/registration");

		ClientResponse responseobjConfirmReg = webResourceconfirmreg.type(
				"text/xml").post(ClientResponse.class, writer.toString());
		return responseobjConfirmReg;

	}

	public static ClientResponse neoUserAccount(String loginName,
			String passWord, String promoCode, String appId) {
		String NEO_URL_WITH_APPID_USER_ACCOUNT = NEO_URL_USER_ACCOUNT.replace(
				"temp_mras_appId", appId);
		WebResource webResourceUserAccount = client
				.resource(NEO_URL_WITH_APPID_USER_ACCOUNT + "loginname="
						+ loginName + "&password=" + passWord + "&promocode="
						+ promoCode);

		ClientResponse responseobjUserAccount = webResourceUserAccount.accept(
				"text/xml").get(ClientResponse.class);
		return responseobjUserAccount;

	}

	public static ClientResponse neoUpdateProfile(int consumerId, String appId,
			StringWriter writer1) {
		String NEO_URL_WITH_APPID = NEO_URL_CONSUMER_REGISTRATION.replace(
				"temp_mras_appId", appId);

		WebResource webResourceupdateConsumer = client
				.resource(NEO_URL_WITH_APPID + "/" + consumerId + "/");
		ClientResponse responseobjupdateConsumer = webResourceupdateConsumer
				.type("text/xml").put(

				ClientResponse.class, writer1.toString());
		return responseobjupdateConsumer;

	}

	public static Responses clientResponse(ClientResponse responseobjConfirmReg)
			throws JAXBException {
		Responses obj = null;
		String output = responseobjConfirmReg.getEntity(String.class);
		JAXBContext contextObj1 = JAXBContext.newInstance(Responses.class);
		Unmarshaller uuobj = contextObj1.createUnmarshaller();

		StringBuffer xmlStr = new StringBuffer(output);
		obj = (Responses) uuobj.unmarshal(new StreamSource(new StringReader(
				xmlStr.toString())));

		/*
		 * logger.info("Message recieved from server is" +
		 * obj.getResponse().getResponseCode());
		 * logger.info("Output from Server .... \n"); logger.info(output);
		 */
		return obj;

	}

}
