package com.diageo.mras.webservices.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jboss.logging.Logger;

import com.diageo.mras.webservices.dao.GroupResourceDao;
import com.diageo.mras.webservices.modals.Consumer;
import com.diageo.mras.webservices.modals.Group;
import com.diageo.mras.webservices.modals.ListOfPhone;
import com.diageo.mras.webservices.modals.ViewConsumerInGroupResponseList;
import com.diageo.mras.webservices.responses.ResponseMRAS;
import com.diageo.mras.webservices.responses.ResponseMrasCreateGroup;
import com.diageo.mras.webservices.responses.ViewConsumerInGroupResponse;

/**
 * This is a Web Services class for the Group.
 * 
 * @author Infosys Limited
 * @version 1.0
 */
@Path("/Group")
public class GroupResource {
	static Logger logger = Logger.getLogger(GroupResource.class.getName());

	static final String pattern = "[+|0]?\\d{2,3}\\s?[-]?\\d{2,4}\\s?[-]?\\d{3,6}";
	private static final GroupResourceDao groupResourceDao = new GroupResourceDao();
	private static final ValidationDateEmail validationDateEmail = new ValidationDateEmail();

	/**
	 * Description of SearchConsumerIdByPhoneNumbers()
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @param list
	 *            of Phone Numbers This of phone numbers for which consumer id
	 *            has to be found.
	 * @return A map of phone numbers(key), phone numbers(value)
	 * 
	 * 
	 * 
	 */

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/CheckRegisteredAssociate")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response CheckRegisteredAssociate(@Context HttpServletRequest Req,
			Consumer consumer) {

		String appId = consumer.getAppId();
		
		if (logger.isDebugEnabled()) {
			logger.debug("AppId : " + appId );
		}

		Response response = null;
		// As the phone number data would be large so needs to send this data in
		// the body

		// validating AppId first
		ResponseMRAS responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {

			response = Response.status(200).entity(responseObj).build();
			return response;
		}

		responseObj = new ResponseMRAS();
		// Let's check if there are any phone number sent by the users.
		if ((consumer.getPhoneNumbers() == null)) {

			responseObj.setResponseCode(404);
			responseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 404");
			}
			return response;
		} else {

			List<String> phoneNumbers = consumer.getPhoneNumbers();
			
			//for (String phone : phoneNumbers) {
			for(int temp=0;temp<phoneNumbers.size();temp++){
				
			    if (!phoneNumbers.get(temp).matches(pattern)) {
			    	phoneNumbers.remove(temp);
				  
			   }
			}
			
			
			ListOfPhone listOfPhone = new ListOfPhone();
			//HashSet<String> registeredNumbers = new HashSet<String>();
			
			
			
			
			try {
				if(!phoneNumbers.isEmpty()){
				listOfPhone.setPhoneNumbers(groupResourceDao
						.SearchConsumerIdByPhoneNumbers(phoneNumbers));
				}
				else
				{
					HashSet<String> registeredNumbers = new HashSet<String>();
					listOfPhone.setPhoneNumbers(registeredNumbers);					
				}
			} catch (SQLException e) {

				e.printStackTrace();
				responseObj.setResponseCode(430);
				responseObj.setResponseMessage("Some error in input parameter");

				response = Response.status(200)
						.type(MediaType.APPLICATION_JSON).entity(responseObj)
						.build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned 430");
				}
				return response;

			}
			responseObj.setResponseCode(200);
			responseObj.setResponseMessage(listOfPhone.toString());

			response = Response.status(200).type(MediaType.APPLICATION_JSON)
					.entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 200");
			}
			return response;
		}
	}

	/**
	 * Description of ViewGroups()
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @param ConsumerID
	 *            This is the id for consumer.
	 * @return A list of groups along with the response code
	 */

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ViewGroups")
	public Response ViewGroups(@QueryParam("ConsumerId") int consumerId,
			@Context HttpServletRequest req, @QueryParam("AppId") String appId) {

		Response response = null;
		/*
		 * response = (Response)
		 * MrasCache.MCache.recover("ViewGroups"+consumerId+appId);
		 * if(response==null){
		 */

		ResponseMRAS responseObj = null;

		// validating AppId first
		responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(200).entity(responseObj).build();
			return response;
		}
		responseObj = new ResponseMRAS();

		// if the consumerId is not given as the input parameter
		if (consumerId == 0) {
			responseObj.setResponseCode(404);
			responseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			response = Response.status(200).entity(responseObj).build();
			return response;
		}

		try {
			int resultOfConsumerId = validationDateEmail.validateConsumer(req);
			logger.debug(" Checking Validation EmailId and DateOfBirth");
			if (resultOfConsumerId == 0 || consumerId != resultOfConsumerId) {
				logger.debug(" recive consumerId from DB :- "
						+ resultOfConsumerId + " ConsumerId enter" + consumerId);
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

		// logger.debug("Calling dao viewAllGroups method");
		List<Group> groupList = groupResourceDao.viewAllGroups(consumerId);

		if (groupList == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("ConsumerID " + consumerId + " was not found");
			}

			responseObj.setResponseCode(431);
			responseObj.setResponseMessage(WebConstant.CONSUMER_NOT_PART);

			response = Response.status(200).entity(responseObj).build();
		} else {

			JSONArray array = new JSONArray();
			JSONObject myObject = new JSONObject();
			JSONObject myObject1 = new JSONObject();

			Iterator iterator = groupList.iterator();
			while (iterator.hasNext()) {
				try {
					myObject = new JSONObject(
							((Group) iterator.next()).toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// System.out.println( iterator.next() );
				array.put(myObject);
			}
			try {
				myObject1.put("group", array);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			logger.info("ViewGroups responce code is " + 200);
			GenericEntity<List<Group>> listentity = new GenericEntity<List<Group>>(
					groupList) {
			};
			response = Response.status(200).type(MediaType.APPLICATION_JSON)
					.entity(myObject1).build();
			if (logger.isDebugEnabled()) {
				logger.debug("group list for the consumer ID " + consumerId
						+ " is " + groupList);
			}
		}
		/*
		 * MrasCache.MCache.admit("ViewGroups"+consumerId+appId, response,
		 * 180000, 180000); }
		 */

		return response;

	}

	/**
	 * Description of createGroup()
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @FormParam consumerid This is the Id of consumer
	 * @FormParam offerid This is the OfferId.
	 * @FormParam consumeridOwner This is the consumeridOwner.
	 * @FormParam appId This is the appId.
	 * @return Various Responses
	 */

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/CreateGroup")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createGroup(@Context HttpServletRequest Req,
			Consumer consumer) {
		String appId = consumer.getAppId();
		int consumeridOwner = consumer.getConsumerId();
		int countryId = consumer.getCountryCode();
		List<String> groupMembersPhone = consumer.getPhoneNumbers();

		if (logger.isDebugEnabled()) {
			logger.debug("paremeter recevied are ConsumerIdOwner "
					+ consumeridOwner + " phone " + groupMembersPhone
					+ ", AppId: " + appId + " countryId: " + countryId);
		}
		Response response = null;

		// Validating AppId
		ResponseMRAS responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(200).entity(responseObj).build();
			return response;
		}
		responseObj = new ResponseMRAS();

		// Checking madatory parameters
		if ((consumeridOwner == 0) || (countryId == 0)) {
			responseObj.setResponseCode(404);
			responseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			response = Response.status(200).entity(responseObj).build();
			return response;
		}

		// Let's check the format of all the phoneNumber,
		List<String> wrongPhoneNumbers = new ArrayList<String>(5);
		List<String> rightPhoneNumber = new ArrayList<String>(5);
		if (groupMembersPhone != null) {

			for (String temp : groupMembersPhone) {

				if (temp.matches(pattern)) {
					rightPhoneNumber.add(temp);
				} else {
					wrongPhoneNumbers.add(temp);
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Right format PhoneNumber list for the consuemrId "
						+ consumeridOwner + " is " + rightPhoneNumber);
				logger.debug("Wrong format PhoneNumber list for the consuemrId "
						+ consumeridOwner + " is " + wrongPhoneNumbers);
			}
		}

		// Calling to create the group for group owner

		int[] resultResponse1 = groupResourceDao.createGroupOwner(
				consumeridOwner, appId, rightPhoneNumber, countryId);

		int resultResponse = resultResponse1[0];
		int groupId = resultResponse1[1];

		switch (resultResponse) {

		case 200:
			logger.info("CreateGroup responce code is " + resultResponse
					+ " groupid is " + groupId);
			ResponseMrasCreateGroup responseObj1 = new ResponseMrasCreateGroup();
			responseObj1.setGroupId(groupId);
			responseObj1.setInvalidPhoneNumber(wrongPhoneNumbers);
			responseObj1.setResponseMessage(WebConstant.SUCCESSFUL);
			response = Response.status(200).entity(responseObj1).build();
			return response;

		case 406:
			responseObj.setResponseCode(412);
			responseObj
					.setResponseMessage("Consumer is already an owner of the group");
			break;

		case 405:
			responseObj.setResponseCode(405);
			responseObj.setResponseMessage("Invalid OwnerId");
			break;

		case 423:
			responseObj.setResponseCode(423);
			responseObj.setResponseMessage("Invalid CountryId");
			break;

		}
		response = Response.status(200).entity(responseObj).build();
		return response;
	}

	/**
	 * Description of ConfirmStatus()
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @param ConsumerID
	 *            This is the id for consumer.
	 * @param GroupID
	 *            This is the Groupid for consumer.
	 * @param statusflag
	 *            This is the Statusflag set for that consumerid.
	 * @return Response code as Sucess/Failure
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ConfirmStatus")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response ConfirmMembership(@Context HttpServletRequest Req,
			Consumer consumer) {

		String appId = consumer.getAppId();
		int consumerId = consumer.getConsumerId();
		int groupId = consumer.getGroupId();
		int statusFlag = consumer.getStatusFlag();

		Response response = null;
		if (logger.isDebugEnabled()) {
			logger.debug("paremeter recevied are ConsumerId " + consumerId
					+ " GroupId " + groupId + ", AppId: " + appId
					+ " statusFlag: " + statusFlag);
		}
		ResponseMRAS responseObj = new ResponseMRAS();

		if (consumerId == 0
				|| groupId == 0
				|| statusFlag == 0
				|| ((!(statusFlag == 1)) && (!(statusFlag == 2)) && (!(statusFlag == 3)))) {
			responseObj.setResponseCode(404);
			responseObj
					.setResponseMessage(WebConstant.MISSING_PARAMETER_STATUS_FLAG);
			response = Response.status(200).entity(responseObj).build();
			return response;
		}

		responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(200).entity(responseObj).build();
			return response;
		}

		int result = groupResourceDao.ConfirmStatus(consumerId, groupId,
				statusFlag);
		if (logger.isDebugEnabled()) {
			logger.debug("result is " + result);
		}

		responseObj = new ResponseMRAS();
		logger.info("ConfirmStatus result is " + result);
		if (result == 200) {

			responseObj.setResponseCode(200);
			responseObj.setResponseMessage("StatusFlag updated");

			response = Response.status(200).entity(responseObj).build();

		}
		if (result == 405) {

			responseObj.setResponseCode(417);
			responseObj
					.setResponseMessage("Invalid combination of groupid and consumerid");

			response = Response.status(200).entity(responseObj).build();

		}

		/*
		 * if (result == 406) {
		 * 
		 * responseObj.setResponseCode(416);
		 * responseObj.setResponseMessage(WebConstant.INVALID_GROUPID);
		 * 
		 * response = Response.status(200).entity(responseObj).build();
		 * 
		 * }
		 */

		if (result == 407) {
			responseObj.setResponseCode(417);
			responseObj
					.setResponseMessage("Invalid combination of groupid and consumerid");

			response = Response.status(200).entity(responseObj).build();

		}

		return response;

	}

	/**
	 * Description of addMembertoGroup()
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @FormParam consumerid This is the Id of consumer
	 * @FormParam offerid This is the OfferId.
	 * @FormParam consumeridOwner This is the consumeridOwner.
	 * @FormParam appId This is the appId.
	 * @return Various Responses
	 */

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/AddMemberToGroup")
	@Consumes(MediaType.APPLICATION_JSON)
	// @Consumes("application/x-www-form-urlencoded")
	public Response addMemberToGroup(@Context HttpServletRequest Req,
			Consumer consumer) {

		String appId = consumer.getAppId();
		String PhoneNumber = consumer.getPhoneNumber();
		int groupId = consumer.getGroupId();
		int consumerId = consumer.getConsumerId();

		Response r = null;
		ResponseMRAS responseObj = null;
		responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			r = Response.status(200).entity(responseObj).build();
			return r;
		}
		responseObj = new ResponseMRAS();

		if ((groupId == 0 && consumerId == 0) || PhoneNumber == null) {
			responseObj.setResponseCode(404);
			responseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			r = Response.status(200).entity(responseObj).build();
			return r;
		}

		int response = groupResourceDao.addMemberInGroupDAO(groupId,
				PhoneNumber, appId, consumerId);
		logger.info("AddMemberToGroup response is " + response);

		if (response == 200) {

			responseObj.setResponseCode(200);
			responseObj.setResponseMessage("Member added");

			r = Response.status(200).entity(responseObj).build();
			return r;
		}
		if (response == 405) {

			responseObj.setResponseCode(405);
			responseObj.setResponseMessage("Invalid Consumer Id");

			r = Response.status(200).entity(responseObj).build();
			return r;
		}

		if (response == 409) {

			responseObj.setResponseCode(409);
			responseObj
					.setResponseMessage("No group found for given consumerId");

			r = Response.status(200).entity(responseObj).build();
			return r;
		}

		else {
			responseObj.setResponseCode(408);
			responseObj.setResponseMessage(WebConstant.INVALID_GROUPID);

			r = Response.status(200).entity(responseObj).build();

		}
		return r;

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/RemoveMemberFromGroup")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeMemberFromGroup(@Context HttpServletRequest Req,
			Consumer consumer) {

		String appId = consumer.getAppId();
		int groupId = consumer.getGroupId();
		int consumerID = consumer.getConsumerId();
		String PhoneNumber = consumer.getPhoneNumber();
		logger.debug(" recive parameter"
				+ groupId + " ConsumerId enter"
				+ consumerID+" phonemunber"+PhoneNumber);
		Response r = null;

		ResponseMRAS responseObj;
		responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			r = Response.status(200).entity(responseObj).build();
			return r;
		}
		responseObj = new ResponseMRAS();
		if ((groupId == 0 && consumerID == 0) || PhoneNumber == null) {
			responseObj.setResponseCode(404);
			responseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			r = Response.status(200).entity(responseObj).build();
			return r;
		}
		int response = groupResourceDao.removeMemberFromGroupDAO(groupId,
				PhoneNumber, consumerID);

		if (response == 200) {
			logger.info(" member remove successful ");
			responseObj.setResponseCode(200);
			responseObj.setResponseMessage(WebConstant.SUCCESSFUL);

			r = Response.status(200).entity(responseObj).build();
			return r;
		}
		if (response == 405) {

			responseObj.setResponseCode(405);
			responseObj.setResponseMessage("Invalid Consumer Id");

			r = Response.status(200).entity(responseObj).build();
			return r;
		}

		if (response == 409) {

			responseObj.setResponseCode(409);
			responseObj
					.setResponseMessage("No Consumer found with given Phone Number and group ID");

			r = Response.status(200).entity(responseObj).build();
			return r;
		}

		if (response == 406) {
			responseObj.setResponseCode(422);
			responseObj
					.setResponseMessage("No Consumer found with given Phone Number and group ID");

			r = Response.status(200).entity(responseObj).build();
			return r;

		}

		return r;

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ViewConsumersInGroup")
	public Response viewConsumersInGroup(@Context HttpServletRequest req,
			@QueryParam("AppId") String appId,
			@QueryParam("ConsumerId") int consumerId) {
		Response response = null;

		/*
		 * response = (Response)
		 * MrasCache.MCache.recover("ViewConsumersInGroup"+consumerId+appId);
		 * if(response==null){
		 */

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
			return response;
		}

		else {

			try {
				int resultOfConsumerId = validationDateEmail
						.validateConsumer(req);
				logger.debug(" Checking Validation EmailId and DateOfBirth");
				if (resultOfConsumerId == 0 || consumerId != resultOfConsumerId) {
					logger.debug(" recive consumerId from DB :- "
							+ resultOfConsumerId + " ConsumerId enter"
							+ consumerId);
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

			Map<String, Integer> result = groupResourceDao
					.viewConsumerInGroup(consumerId);
			if (result == null) {
				responseObj.setResponseCode(405);
				responseObj.setResponseMessage(WebConstant.CONSUMER_NOT_PART);
				response = Response.status(200).entity(responseObj).build();
				return response;
			}

			List<ViewConsumerInGroupResponse> entry = new ArrayList<ViewConsumerInGroupResponse>();
			for (Map.Entry<String, Integer> e : result.entrySet())
				if (e != null) {
					// logger.debug("only puttng if not null");
					entry.add(new ViewConsumerInGroupResponse(e));
				}
			// return entry;
			ViewConsumerInGroupResponseList listobj = new ViewConsumerInGroupResponseList();

			listobj.setViewConsumerinGroupList(entry);
			response = Response.status(200).type(MediaType.APPLICATION_JSON)
					.entity(listobj).build();
			// MrasCache.MCache.admit("ViewConsumersInGroup"+consumerId+appId,
			// response, 180000,
			// 180000);
			// return response;
		}

		return response;
	}

	/*
	 * @POST
	 * 
	 * @Produces(MediaType.APPLICATION_JSON)
	 * 
	 * @Path("/Gifting")
	 * 
	 * @Consumes(MediaType.APPLICATION_JSON)
	 * 
	 * public Response Gifting(
	 * 
	 * @QueryParam("AppId") String appId,
	 * 
	 * @QueryParam("ConsumerId") int consumerId,
	 * 
	 * @Context HttpServletRequest Req, Consumer consumer){
	 * 
	 * int offerId=consumer.getOfferId(); String
	 * messageRecive=consumer.getGiftMessage(); Response response = null; // As
	 * the phone number data would be large so needs to send this data in the
	 * body
	 * 
	 * // validating AppId first ResponseMRAS responseObj = new
	 * RewardResource().validateAppId(appId); if (responseObj != null) {
	 * 
	 * response = Response.status(200).entity(responseObj).build(); return
	 * response; }
	 * 
	 * responseObj=new ResponseMRAS(); // Let's check if there are any phone
	 * number sent by the users. if ((consumer.getMemberPhoneNumber() == null)
	 * || offerId==0 || consumerId==0) { responseObj.setResponseCode(404);
	 * responseObj .setResponseMessage(WebConstant.MISSING_PARAMETER); response
	 * = Response.status(200).entity(responseObj).build(); return response; }
	 * 
	 * List<String> phoneNumbers=consumer.getMemberPhoneNumber();
	 * 
	 * for(String phoneNumber:phoneNumbers){ if (!phoneNumber.matches(pattern))
	 * { responseObj.setResponseCode(425);
	 * responseObj.setResponseMessage("Wrong PhoneNumber format"); response =
	 * Response.status(200).entity(responseObj).build(); return response; } }
	 * 
	 * String phoneNumber1,phoneNumber2,phoneNumber3; int resultCode;
	 * 
	 * if(phoneNumbers.size()>2){ phoneNumber1=phoneNumbers.get(0);
	 * phoneNumber2=phoneNumbers.get(1); phoneNumber3=phoneNumbers.get(2); }else
	 * if(phoneNumbers.size()>1){ phoneNumber1=phoneNumbers.get(0);
	 * phoneNumber2=phoneNumbers.get(1); phoneNumber3="N"; }else{
	 * phoneNumber1=phoneNumbers.get(0); phoneNumber2="N"; phoneNumber3="N"; }
	 * 
	 * System.out.println("@@@"+phoneNumber1+"-"+phoneNumber2+"-"+phoneNumber3);
	 * 
	 * //try{
	 * resultCode=groupResourceDao.GiftingToFriends(phoneNumber1,phoneNumber2
	 * ,phoneNumber3,consumerId,offerId,messageRecive); }catch(Exception e){
	 * 
	 * responseObj.setResponseCode(430); responseObj
	 * .setResponseMessage("Some Unhandled Error has occurred");
	 * 
	 * response = Response.status(200).type(MediaType.APPLICATION_JSON)
	 * .entity(responseObj).build(); return response;
	 * 
	 * }
	 * 
	 * System.out.println("@@@@"+resultCode);
	 * 
	 * OfferGiftModal offerGiftModal=new OfferGiftModal(); ArrayList<String>
	 * notRegisterList=new ArrayList<String>(); ArrayList<String>
	 * registerList=new ArrayList<String>();
	 * 
	 * if (resultCode == 405) { responseObj.setResponseCode(resultCode);
	 * responseObj.setResponseMessage(WebConstant.INVALID_CONSUMERID); response
	 * = Response.status(200).entity(responseObj).build(); return response; }
	 * else if (resultCode == 403) { responseObj.setResponseCode(420);
	 * responseObj.setResponseMessage(WebConstant.INVALID_OFFERID); response =
	 * Response.status(200).entity(responseObj).build(); return response; } else
	 * if (resultCode == 1) { notRegisterList.add(phoneNumber1);
	 * registerList.add(phoneNumber2); registerList.add(phoneNumber3); }else if
	 * (resultCode == 2) { notRegisterList.add(phoneNumber2);
	 * registerList.add(phoneNumber1); registerList.add(phoneNumber3); }else if
	 * (resultCode == 4) { notRegisterList.add(phoneNumber3);
	 * registerList.add(phoneNumber2); registerList.add(phoneNumber1); } else if
	 * (resultCode == 3) { notRegisterList.add(phoneNumber1);
	 * notRegisterList.add(phoneNumber2); registerList.add(phoneNumber3); }else
	 * if (resultCode == 5) { notRegisterList.add(phoneNumber1);
	 * notRegisterList.add(phoneNumber3); registerList.add(phoneNumber2); }else
	 * if (resultCode == 6) { notRegisterList.add(phoneNumber3);
	 * notRegisterList.add(phoneNumber2); registerList.add(phoneNumber1); }else
	 * if (resultCode == 7) { notRegisterList.add(phoneNumber1);
	 * notRegisterList.add(phoneNumber2); notRegisterList.add(phoneNumber3);
	 * }else if (resultCode == 0) { registerList.add(phoneNumber1);
	 * registerList.add(phoneNumber2); registerList.add(phoneNumber3); }else{
	 * responseObj.setResponseCode(430); responseObj
	 * .setResponseMessage("Some Unhandled Error has occurred");
	 * 
	 * response = Response.status(200).type(MediaType.APPLICATION_JSON)
	 * .entity(responseObj).build(); return response; }
	 * offerGiftModal.setUnRegisteredPhoneNumbers(notRegisterList);
	 * offerGiftModal.setOffersGiftedTo(registerList);
	 * 
	 * System.out.println("@@@!"+notRegisterList+"-"+registerList);
	 * 
	 * 
	 * try{ for(String phoneNumber:registerList){
	 * groupResourceDao.getConsumerIdFromPhoneNumber(phoneNumber,offerId); }
	 * }catch(Exception e){ responseObj.setResponseCode(430); responseObj
	 * .setResponseMessage("Some Unhandled Error has occurred");
	 * 
	 * response = Response.status(200).type(MediaType.APPLICATION_JSON)
	 * .entity(responseObj).build(); return response; } response =
	 * Response.status(200).type(MediaType.APPLICATION_JSON)
	 * .entity(offerGiftModal).build(); return response;
	 * 
	 * }
	 */

}