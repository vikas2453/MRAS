package com.diageo.mras.webservices.services;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.diageo.mras.cache.MrasCache;
import com.diageo.mras.webservices.dao.OutletResourceDao;
import com.diageo.mras.webservices.dao.RewardResourceDao;
import com.diageo.mras.webservices.modals.Consumer;
import com.diageo.mras.webservices.modals.Message;
import com.diageo.mras.webservices.modals.Offer;
import com.diageo.mras.webservices.modals.OuletInformation;
import com.diageo.mras.webservices.modals.Outlet;
import com.diageo.mras.webservices.modals.OutletOfferList;
import com.diageo.mras.webservices.modals.OutletWithoutCampaignId;
import com.diageo.mras.webservices.modals.Reward;
import com.diageo.mras.webservices.modals.VoucherIssueCountModal;
import com.diageo.mras.webservices.responses.MessageResponse;
import com.diageo.mras.webservices.responses.OfferResponse;
import com.diageo.mras.webservices.responses.OutletList;
import com.diageo.mras.webservices.responses.ResponceSearchOfferOutlet;
import com.diageo.mras.webservices.responses.ResponseMRAS;

/**
 * This is a Web Services class for the Outlets.
 * 
 * @author Infosys Limited
 * @version 1.0
 */
@Path("/Outlet")
public class OutletResource {
	private static final Logger logger = Logger.getLogger(OutletResource.class
			.getName());
	private static final OutletResourceDao outletResourceDao = new OutletResourceDao();
	private static final RewardResourceDao rewardResourceDao = new RewardResourceDao();
	private static final Format formatter = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");
	private final long timeToLive = 1800000;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/SearchWithGeolocation")
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
	public Response searchForNearbyOutlets(
			@QueryParam("Latitude") double latitude,
			@QueryParam("Longitude") double longitude,
			@QueryParam("Distance") double radius,
			@QueryParam("AppId") String appId,
			@QueryParam("CampaignId") int campaignId
			// phase2
			, @QueryParam("OfferId") int offerId,
			@QueryParam("CheckinRequired") boolean ceckinRequired,
			@QueryParam("RedemptionRequired") boolean redemptionRequired) {
		
		
		ResponseMRAS responseobj = null;
		Response response = null;
		if (logger.isDebugEnabled()) {
			logger.debug("Parameter received are Latitude " + latitude
					+ ", Longitude: " + longitude + " ,Radius: " + radius
					+ " ,offerId: " + offerId + " ,ceckinRequired: "
					+ ceckinRequired + " ,RedemptionRequired: "
					+ redemptionRequired + "");
		}

		ResponseMRAS responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(200).entity(responseObj).build();
			return response;
		}
		responseobj = new ResponseMRAS();

		if ((latitude == 0.0) || (longitude == 0.0) || (radius == 0.0)

		) {

			responseobj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			responseobj.setResponseCode(404);

			response = Response.status(200).entity(responseobj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 404");
			}
			return response;

		}

		// fetch the list of nearby outlets..
		List<Outlet> listoutlet = outletResourceDao.searchForNearbyOutlets(
				latitude, longitude, radius, campaignId, offerId,
				ceckinRequired, redemptionRequired, appId);

		if (listoutlet == null) {

			responseobj.setResponseMessage("no match found");
			responseobj.setResponseCode(411);
			response = Response.status(200).entity(responseobj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 411");
			}
			return response;

		}
		// logger.debug("listoutlet : " + listoutlet);

		GenericEntity<List<Outlet>> listentity = new GenericEntity<List<Outlet>>(
				listoutlet) {
		};
		response = Response.status(200).type(MediaType.APPLICATION_JSON)
				.entity(listentity).build();
		if (logger.isDebugEnabled()) {
			   logger.debug("Response returned ");
		}
		return response;

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/WNSearchWithGeolocation")
	/** Description of WNSearchWithGeolocation() .
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
	public Response wNsearchForNearbyOutlets(
			@QueryParam("Latitude") double latitude,
			@QueryParam("Longitude") double longitude,
			@QueryParam("Distance") double radius,
			@QueryParam("AppId") String appId,
			@QueryParam("CampaignId") int campaignId
			// phase2
			, @QueryParam("OfferId") int offerId,
			@QueryParam("CheckinRequired") boolean ceckinRequired,
			@QueryParam("RedemptionRequired") boolean redemptionRequired,
			@QueryParam("CallBack") String callBack) {

		Response responseIn = null;
		Response responseOutput = null;
		JSONObject myObject = new JSONObject();

		responseIn = searchForNearbyOutlets(latitude, longitude, radius, appId,
				campaignId, offerId, ceckinRequired, redemptionRequired);
		if (responseIn.getEntity() instanceof ResponseMRAS) {
			logger.debug("geting object of ResponseMRAS and message "
					+ responseIn.getEntity().toString());
			responseOutput = Response
					.status(200)
					.entity(callBack + "(" + responseIn.getEntity().toString()
							+ ")").build();
			return responseOutput;
		} else {
			logger.debug("geting object of GenericEntity<List<Object>> ");
			GenericEntity<List<Object>> genericListOfObjects = (GenericEntity<List<Object>>) responseIn
					.getEntity();

			if (genericListOfObjects.getEntity().get(0) instanceof Outlet) {
				logger.debug("geting object of Outlet ");
				JSONArray array = new JSONArray();
				Iterator iterator = genericListOfObjects.getEntity().iterator();
				while (iterator.hasNext()) {
					try {
						myObject = new JSONObject(
								((Outlet) iterator.next()).toString());
					} catch (JSONException e) {
						logger.info("Exception while itrating Outlet"
								+ e.getMessage());
						e.printStackTrace();
					}
					array.put(myObject);
				}
				responseOutput = Response.status(200)
						.entity(callBack + "(" + array.toString() + ")")
						.build();
				return responseOutput;

			} else {
				ResponseMRAS responseMRAS = new ResponseMRAS();
				responseMRAS.setResponseCode(450);
				responseMRAS
						.setResponseMessage("Not able to parse the object return");
				responseOutput = Response.status(200)
						.entity(callBack + "(" + responseMRAS.toString() + ")")
						.build();
				return responseOutput;
			}
		}

		// return null;
	}

	/**
	 * Description of checkIn().
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @param Req
	 *            HttpServletRequest
	 * @param consumer
	 *            Consumer
	 * @return Various Response code
	 */

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/CheckIn")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response checkIn(@Context HttpServletRequest Req, Consumer consumer) {

		String appId = consumer.getAppId();
		int consumerId = consumer.getConsumerId();
		long outlet_Ship_To = (long) consumer.getOutlet_Ship_To();

		double latitude = consumer.getLatitude();
		double longitude = consumer.getLongitude();
		if (logger.isDebugEnabled()) {
			logger.debug("appId :" + appId + " consumerId :" + consumerId
					+ " shipToCode :" + outlet_Ship_To + "" + " latitude  : "
					+ latitude + " longitude: " + longitude);
		}
		Response response = null;

		// validate the appId
		ResponseMRAS responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(200).entity(responseObj).build();
			return response;
		}
		responseObj = new ResponseMRAS();
		// Check for Mandatory consumer id

		if (consumerId == 0) {
			responseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			responseObj.setResponseCode(404);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
		    logger.debug("resultResponse is " + response);
			}
			return response;
		}

		if ((outlet_Ship_To == 0) && ((latitude == 0.0) || (longitude == 0.0))) {
			// logger.debug("Mandatory parameters are missing");
			responseObj
					.setResponseMessage("Either outlet ship to code or latitude longitude mandatory");
			responseObj.setResponseCode(413);

			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 404");
			}

			return response;

		}

		// Check for Either outlet code or latitude longitude mandatory

		int resultResponse = outletResourceDao.checkIn(consumerId,
				outlet_Ship_To, appId, latitude, longitude);
		// logger.debug("resultResponse of checkin for consumerId: " +
		// consumerId+" is "+resultResponse);
		if (resultResponse == 405) {
			responseObj.setResponseMessage(WebConstant.INVALID_CONSUMERID);
			responseObj.setResponseCode(405);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 405");
			}
			return response;
		} else if (resultResponse == 406) {
			responseObj.setResponseMessage(WebConstant.INVALID_OUTLET_CODE);
			responseObj.setResponseCode(414);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 406");
			}
			return response;
		} else if (resultResponse == 407) {
			responseObj
					.setResponseMessage("No outlet found with these latitude and longitude values");
			responseObj.setResponseCode(415);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 407");
			}
			return response;
		} else {
			responseObj.setResponseMessage(WebConstant.SUCCESSFUL);
			logger.info("Successful check in for consumerId: " + consumerId);
			responseObj.setResponseCode(200);
			response = Response.status(200).entity(responseObj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 200");
			}
			return response;
		}
	}

	/**
	 * Description of searchForNearbyOutlets()
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @param searchquery
	 *            This is the searchQuery.
	 * @param countryCode
	 *            This is the countrycode.
	 * @param zipcode
	 *            This is the zipcode.
	 * @param appId
	 *            This is the appId.
	 * @param campaignId
	 *            This is the campaignId.
	 * @return A list of nearby outlets
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/WNSearchWithoutGeolocation")
	public Response wNSearchWithoutGeolocation(
			@QueryParam("SearchQuery") String searchquery,
			@QueryParam("CountryCode") String countryCode,
			@QueryParam("ZipCode") String zipcode,
			@QueryParam("AppId") String appId,
			@QueryParam("CampaignId") int campaignId,
			@QueryParam("Outlet_Ship_To") long outlet_Ship_To,// phase 11
																// changes
			@QueryParam("OfferId") int offerId,
			@QueryParam("CheckinRequired") boolean checkinRequired,
			@QueryParam("RedemptionRequired") boolean redemptionRequired,
			@QueryParam("CallBack") String callBack) {

		Response responseIn = null;
		Response responseOutput = null;
		JSONObject myObject = new JSONObject();

		responseIn = SearchOutletsWithoutGeolocation(searchquery, countryCode,
				zipcode, appId, campaignId, outlet_Ship_To,// phase 11
				offerId, checkinRequired, redemptionRequired);

		if (responseIn.getEntity() instanceof ResponseMRAS) {
			logger.debug("geting object of ResponseMRAS and message "
					+ responseIn.getEntity().toString());
			responseOutput = Response
					.status(200)
					.entity(callBack + "(" + responseIn.getEntity().toString()
							+ ")").build();
			return responseOutput;
		} else {
			logger.debug("geting object of GenericEntity<List<Object>> ");
			GenericEntity<List<Object>> genericListOfObjects = (GenericEntity<List<Object>>) responseIn
					.getEntity();

			if (genericListOfObjects.getEntity().get(0) instanceof Outlet) {
				logger.debug("geting object of Outlet ");
				JSONArray array = new JSONArray();
				Iterator iterator = genericListOfObjects.getEntity().iterator();
				while (iterator.hasNext()) {
					try {
						myObject = new JSONObject(
								((Outlet) iterator.next()).toString());
					} catch (JSONException e) {
						logger.info("Exception while itrating Outlet"
								+ e.getMessage());
						e.printStackTrace();
					}
					array.put(myObject);
				}
				responseOutput = Response.status(200)
						.entity(callBack + "(" + array.toString() + ")")
						.build();
				return responseOutput;

			} else if (genericListOfObjects.getEntity().get(0) instanceof OutletWithoutCampaignId) {
				logger.debug("geting object of OutletWithoutCampaignId ");
				JSONArray array = new JSONArray();
				Iterator iterator = genericListOfObjects.getEntity().iterator();
				while (iterator.hasNext()) {
					try {
						myObject = new JSONObject(
								((OutletWithoutCampaignId) iterator.next())
										.toString());
					} catch (JSONException e) {
						logger.info("Exception while itrating OutletWithoutCampaignId"
								+ e.getMessage());
						e.printStackTrace();
					}
					array.put(myObject);
				}
				responseOutput = Response.status(200)
						.entity(callBack + "(" + array.toString() + ")")
						.build();
				return responseOutput;
			} else {
				ResponseMRAS responseMRAS = new ResponseMRAS();
				responseMRAS.setResponseCode(450);
				responseMRAS
						.setResponseMessage("Not able to parse the object return");
				responseOutput = Response.status(200)
						.entity(callBack + "(" + responseMRAS.toString() + ")")
						.build();
				return responseOutput;
			}
		}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/SearchWithoutGeolocation")
	// @Encoded
	public Response SearchOutletsWithoutGeolocation(
			@QueryParam("SearchQuery") String searchquery,
			@QueryParam("CountryCode") String countryCode,
			@QueryParam("ZipCode") String zipcode,
			@QueryParam("AppId") String appId,
			@QueryParam("CampaignId") int campaignId,
			@QueryParam("Outlet_Ship_To") long outlet_Ship_To,// phase 11
																// changes
			@QueryParam("OfferId") int offerId,
			@QueryParam("CheckinRequired") boolean checkinRequired,
			@QueryParam("RedemptionRequired") boolean redemptionRequired) {           
			
		
			Response response = null;
            List<Outlet> listoutlet = null;
            if (logger.isDebugEnabled()) {
                  logger.debug("Parameter received are SearchQuery " + searchquery
                              + " ,AppID:" + appId + " , CountryCode: " + countryCode
                              + " ,ZipCode: " + zipcode + " Campaignid" + campaignId
                              + ", offerId:" + offerId + ", ceckinRequired:"
                              + checkinRequired + ", redemptionRequired"
                              + redemptionRequired);
            }

            // validate the appId..
            ResponseMRAS responseObj = RewardResource.validateAppId(appId);
            if (responseObj != null) {
                  response = Response.status(200).entity(responseObj).build();
                  return response;
            }
            responseObj = new ResponseMRAS();

            // validate the mandatory parameters..

            if (outlet_Ship_To != 0) {

                  listoutlet = outletResourceDao.SearchWithOutletShipTo(
                              outlet_Ship_To, checkinRequired);
                  if (listoutlet == null) {
                        responseObj.setResponseMessage("no match found");
                        responseObj.setResponseCode(411);
                        response = Response.status(200).entity(responseObj).build();
                  } else {
                        GenericEntity<List<Outlet>> listentity = new GenericEntity<List<Outlet>>(
                                    listoutlet) {
                        };
                        response = Response.status(200)
                                    .type(MediaType.APPLICATION_JSON).entity(listentity)
                                    .build();

                  }
              	if (logger.isDebugEnabled()) {
 				   logger.info("Response recevied ");
 				}
                  return response;

            } else {

                  if (offerId == 0) {
                        String str = "SELECT OutletId,OutletName,Outlet_ShipTo,Address,town,CountryId,County,Lattitude,Longitude,Channel,Phone,ZipCode,(select count(checkinId) from checkin c where  date(date_add(timestamps,interval 5 hour))=date(date_add(current_TIMESTAMP, INTERVAL 5 HOUR)) and c.Outlet_ShipTo=o.Outlet_ShipTo) as checkins FROM outletmyisam o WHERE ";
                        String str1 = " Outlet_ShipTo in(SELECT outletid from outletcampaignmapping where campaignid in(select CampaignId from campaign where AppId="
                                    + appId + " and StatusId=8))";
                        String str2 = " Outlet_ShipTo in(SELECT outletid from outletcampaignmapping o,campaign c where o.campaignid=c.campaignid and c.statusid=8 and o.campaignid=";
                        StringBuilder querry = new StringBuilder(str);
                        if (logger.isDebugEnabled()) {
                              logger.debug("querry is - " + querry);
                        }

                        if (searchquery != null) {

                              if ((searchquery.contains("'"))
                                          || (searchquery.contains("\""))) {

                                    searchquery = searchquery.replace("'", "").replace(
                                                "\"", "");

                              }

                              querry.append("MATCH(OutletName,Address,town) AGAINST ('"
                                          + searchquery + "') and ");
                              if (logger.isDebugEnabled()) {
                                    logger.debug("After append searchquerry querry is - "
                                                + querry);
                              }
                        }
                        if (zipcode != null) {
                              if (zipcode.contains("'") || zipcode.contains("\"")) {
                                    zipcode = zipcode.replace("'", "").replace("\"", "");
                              }
                              querry.append("ZipCode='" + zipcode + "' and ");
                              if (logger.isDebugEnabled()) {
                                    logger.debug("After append zipcode querry is - "
                                                + querry);
                              }
                        }
                        if (countryCode != null) {
                              querry.append("CountryId=" + countryCode + " and ");
                              if (logger.isDebugEnabled()) {
                                    logger.debug("After append zipcode querry is - "
                                                + querry);
                              }
                        }
                        if (campaignId != 0) {
                              querry.append(str2 + campaignId + ")");
                              if (logger.isDebugEnabled()) {
                                    logger.debug("After append campaignId querry is - "
                                                + querry);
                              }
                              listoutlet = outletResourceDao
                                          .SearchWithOutGeolocationWithCampaignId(
                                                      querry.toString(), checkinRequired);
                              if (listoutlet == null) {
                                    responseObj.setResponseMessage("no match found");
                                    responseObj.setResponseCode(411);
                                    response = Response.status(200).entity(responseObj)
                                                .build();
                              } else {
                                    GenericEntity<List<Outlet>> listentity = new GenericEntity<List<Outlet>>(
                                                listoutlet) {
                                    };
                                    response = Response.status(200)
                                                .type(MediaType.APPLICATION_JSON)
                                                .entity(listentity).build();

                              }
                          	if (logger.isDebugEnabled()) {
             				   logger.info("Response recevied ");
             				}
                              return response;

                        } else {
                              querry.append(str1);
                              if (logger.isDebugEnabled()) {
                                    logger.debug("After append campaignId querry is - "
                                                + querry);
                              }
                              List<OutletWithoutCampaignId> listoutlets = outletResourceDao
                                          .SearchWithOutGeolocationWithoutCampaignId(
                                                      querry.toString(), checkinRequired);
                              if (listoutlets == null) {
                                    responseObj.setResponseMessage("no match found");
                                    responseObj.setResponseCode(411);
                                    response = Response.status(200).entity(responseObj)
                                                .build();
                              } else {
                                    GenericEntity<List<OutletWithoutCampaignId>> listentity = new GenericEntity<List<OutletWithoutCampaignId>>(
                                                listoutlets) {
                                    };
                                    response = Response.status(200)
                                                .type(MediaType.APPLICATION_JSON)
                                                .entity(listentity).build();

                              }
                          	if (logger.isDebugEnabled()) {
             				   logger.info("Response recevied ");
             				}
                              return response;
                        }
                  } else {
                        /*
                        * boolean seperateOutlet=false; seperateOutlet=
                        * outletResourceDao.checkForSeperateOutlet(offerId);
                        */
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
                                    MrasCache.MCache.admit("Offer" + offerId, offer);
                              } else {
                                    responseObj
                                                .setResponseMessage(WebConstant.NO_MATCH_FOUND);
                                    responseObj.setResponseCode(411);
                                    response = Response.status(200).entity(responseObj)
                                                .build();
                                	if (logger.isDebugEnabled()) {
                     				   logger.info("Response recevied ");
                     				}
                                    return response;
                              }

                        }

                        if (offer.isSeperateOutlet()) {
                              String str = "SELECT OutletId,OutletName,Outlet_ShipTo,Address,town,CountryId,County,Phone,ZipCode,Lattitude,Longitude,Channel , (select count(r.offerid) from redemption r where r.outletid=o.outletid and r.offerId=?) as redemptionTillNow,(select MaxRewards from offeroutletmax off where off.outletid=o.Outlet_ShipTo and offerid=?) as maxredemption,(select count(checkinId) from checkin c where date(date_add(timestamps,interval 5 hour))=date(date_add(current_TIMESTAMP, INTERVAL 5 HOUR)) and c.Outlet_ShipTo=o.Outlet_ShipTo) as checkins FROM outletmyisam o WHERE ";
                              String str1 = " Outlet_ShipTo in( SELECT outletid from offeroutletmax where offerid="
                                          + offerId + ")";

                              StringBuilder querry = new StringBuilder(str);
                              if (logger.isDebugEnabled()) {
                                    logger.debug("querry is - " + querry);
                              }

                              if (searchquery != null) {
                                    querry.append("MATCH(OutletName,Address,town) AGAINST ('"
                                                + searchquery + "') and ");
                                    if (logger.isDebugEnabled()) {
                                          logger.debug("After append searchquerry querry is - "
                                                      + querry);
                                    }
                              }
                              if (zipcode != null) {
                                    querry.append("ZipCode='" + zipcode + "' and ");
                                    if (logger.isDebugEnabled()) {
                                          logger.debug("After append zipcode querry is - "
                                                      + querry);
                                    }
                              }
                              if (countryCode != null) {
                                    querry.append("CountryId=" + countryCode + " and ");
                                    if (logger.isDebugEnabled()) {
                                          logger.debug("After append zipcode querry is - "
                                                      + querry);
                                    }
                              }

                              querry.append(str1);

                              List<Outlet> listoutlets = outletResourceDao
                                          .searchWithOutGeolocationWithOfferId(
                                                      querry.toString(), offerId,
                                                      checkinRequired, redemptionRequired);
                              if (listoutlets == null) {
                                    responseObj
                                                .setResponseMessage(WebConstant.NO_MATCH_FOUND);
                                    responseObj.setResponseCode(411);
                                    response = Response.status(200).entity(responseObj)
                                                .build();
                              } else {
                                    GenericEntity<List<Outlet>> listentity = new GenericEntity<List<Outlet>>(
                                                listoutlets) {
                                    };
                                    response = Response.status(200)
                                                .type(MediaType.APPLICATION_JSON)
                                                .entity(listentity).build();

                              }
                          	if (logger.isDebugEnabled()) {
             				   logger.info("Response recevied ");
             				}
                              return response;
                        } else {
                              String str = "SELECT OutletId,OutletName,Outlet_ShipTo,Address,town,CountryId,County,Lattitude,Longitude,Channel,Phone,ZipCode,(select count(checkinId) from checkin c where date(date_add(timestamps,interval 5 hour))=date(date_add(current_TIMESTAMP, INTERVAL 5 HOUR)) and c.Outlet_ShipTo=o.Outlet_ShipTo) as checkins  FROM outletmyisam o WHERE ";
                              // String str2 =
                              // String str2 =
                              // " Outlet_ShipTo in(SELECT outletid from outletcampaignmapping where campaignid=(select campaignId from offer where offerId=? limit 1)";
                              String str1 = " Outlet_ShipTo in(SELECT outletid from outletcampaignmapping where campaignid in(select CampaignId from offer where OfferId="
                                          + offerId + "))";
                              StringBuilder querry = new StringBuilder(str);
                          	if (logger.isDebugEnabled()) {
                              logger.debug("querry is - " + querry);
                          	}

                              if (searchquery != null) {
                                    querry.append("MATCH(OutletName,Address,town) AGAINST ('"
                                                + searchquery + "') and ");

                              }
                              if (zipcode != null) {
                                    querry.append("ZipCode='" + zipcode + "' and ");

                              }
                              if (countryCode != null) {
                                    querry.append("CountryId=" + countryCode + " and ");

                              }
                              querry.append(str1);

                              listoutlet = outletResourceDao
                                          .SearchWithOutGeolocationWithCampaignId(
                                                      querry.toString(), checkinRequired);
                              if (listoutlet == null) {
                                    responseObj
                                                .setResponseMessage(WebConstant.NO_MATCH_FOUND);
                                    responseObj.setResponseCode(411);
                                    response = Response.status(200).entity(responseObj)
                                                .build();
                              } else {
                                    GenericEntity<List<Outlet>> listentity = new GenericEntity<List<Outlet>>(
                                                listoutlet) {
                                    };
                                    response = Response.status(200)
                                                .type(MediaType.APPLICATION_JSON)
                                                .entity(listentity).build();

                              }
                          	if (logger.isDebugEnabled()) {
             				   logger.info("Response returned ");
             				}
                              return response;
                        }
                  }

            }

      
}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/WNGetOutletOffers")
	/** Description of wNGetOutletOffers() .
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @param appId       	This is the application id.
	 * @param campaignId       	This is the campaignId.
	 * @param outletCode        	This is the outletCode.
	 * @param callBack        		This is the callBack String, will be used by webservice consumers to get the jsonp response.
	 * @return                	A list of offers , for the given outlet for the given appId.
	 * 							(If campaignid is also given , then offers related to only that campaign will be given )
	 */
	public Response wNGetOutletOffers(@QueryParam("AppId") String appId,
			@QueryParam("CampaignId") int campaignId,
			@QueryParam("OutletCode") long outletCode,
			@QueryParam("CallBack") String callBack) {

		Response responseIn = null;
		Response responseOutput = null;
		JSONObject myObject = new JSONObject();

		responseIn = getOutletOffers(appId, campaignId, outletCode);
		if (responseIn.getEntity() instanceof ResponseMRAS) {
			if (logger.isDebugEnabled()) {
				logger.debug("geting object of ResponseMRAS and message "
						+ responseIn.getEntity().toString());
			}
			responseOutput = Response
					.status(200)
					.entity(callBack + "(" + responseIn.getEntity().toString()
							+ ")").build();
			return responseOutput;
		} else if (responseIn.getEntity() instanceof OutletOfferList) {

			OutletOfferList outletOfferList = (OutletOfferList) responseIn
					.getEntity();
			List<OfferResponse> offerList = outletOfferList.getOfferList();
			if (logger.isDebugEnabled()) {
				logger.debug("geting object of OfferResponse ");
			}
			JSONArray array = new JSONArray();
			Iterator iterator = offerList.iterator();
			while (iterator.hasNext()) {
				try {
					myObject = new JSONObject(
							((OfferResponse) iterator.next()).getString());
				} catch (JSONException e) {
					logger.info("Exception while itrating OfferResponse"
							+ e.getMessage());
					e.printStackTrace();
				}
				array.put(myObject);
			}
			JSONObject jSONresultObject = new JSONObject();
			try {
				jSONresultObject.put("OfferList", array);
			} catch (JSONException e) {
				logger.info("Exception while itrating OfferList"
						+ e.getMessage());
				e.printStackTrace();
			}
			responseOutput = Response.status(200)
					.entity(callBack + "(" + jSONresultObject.toString() + ")")
					.build();
			return responseOutput;

		} else {
			ResponseMRAS responseMRAS = new ResponseMRAS();
			responseMRAS.setResponseCode(450);
			responseMRAS
					.setResponseMessage("Not able to parse the object returned");
			responseOutput = Response.status(200)
					.entity(callBack + "(" + responseMRAS.toString() + ")")
					.build();
			return responseOutput;
		}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/GetOutletOffers")
	/** Description of getOutletOffers() .
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
	public Response getOutletOffers(@QueryParam("AppId") String appId,
			@QueryParam("CampaignId") int campaignId,
			@QueryParam("OutletCode") long outletCode) {
		ResponseMRAS responseobj = null;
		Response response = null;
		if (logger.isDebugEnabled()) {
			logger.debug("Parameter received are  AppId" + appId
					+ ", campaignId: " + campaignId + " ,outletCode: "
					+ outletCode + "");
		}

		ResponseMRAS responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			return response;
		}

		responseobj = new ResponseMRAS();

		if (outletCode == 0) {
			responseobj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			responseobj.setResponseCode(404);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 404");
			}
			return response;
		}
		OutletOfferList outletOfferList = outletResourceDao
				.getOutletOfferCampaign(outletCode, campaignId, appId);

		if (outletOfferList == null || outletOfferList.getOfferList().isEmpty()) {
			responseobj.setResponseMessage(WebConstant.NO_MATCH_FOUND);
			responseobj.setResponseCode(411);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 411");
			}
			return response;

		}

		response = Response.status(Response.Status.OK)
				.type(MediaType.APPLICATION_JSON).entity(outletOfferList)
				.build();
		if (logger.isDebugEnabled()) {
			   logger.debug("Response returned 200");
		}
		return response;

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/VoteForLocal")
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
	public Response voteForLocal(
			// @Context HttpServletRequest Req, Consumer consumer
			@QueryParam("AppId") String appId,
			@QueryParam("ConsumerId") int consumerId,
			@QueryParam("OutletCode") long outletCode) {

		/*
		 * String appId = consumer.getAppId(); int consumerId =
		 * consumer.getConsumerId(); long outletCode = (long)
		 * consumer.getOutletCode();
		 */
		ResponseMRAS responseobj = null;
		Response response = null;

		if (logger.isDebugEnabled()) {
			logger.debug("Parameter received are  AppId" + appId
					+ ", consumerId: " + consumerId + " ,outletCode: "
					+ outletCode + "");
		}

		ResponseMRAS responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			return response;
		}

		responseobj = new ResponseMRAS();

		if (outletCode == 0 || consumerId == 0) {
			responseobj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			responseobj.setResponseCode(404);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 404");
			}
			return response;
		}

		// fetch the list of nearby outlets..
		int resultCode = outletResourceDao
				.voteForOutlet(consumerId, outletCode);

		if (resultCode == 200) {
			responseobj.setResponseMessage("Successfully Voted");
			logger.info("Successfully Voted");
			responseobj.setResponseCode(200);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.info("Response recevied "+responseobj.getResponseCode());
				}
			return response;
		} else if (resultCode == 405) {
			responseobj.setResponseMessage(WebConstant.INVALID_CONSUMERID);
			responseobj.setResponseCode(405);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.info("Response recevied "+responseobj.getResponseCode());
				}
			return response;
		} else if (resultCode == 414) {
			responseobj.setResponseMessage(WebConstant.INVALID_OUTLET_CODE);
			responseobj.setResponseCode(414);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.info("Response recevied "+responseobj.getResponseCode());
				}
			return response;
			
		} else {
			responseobj.setResponseMessage(WebConstant.UNHANDLED_ERROR);
			responseobj.setResponseCode(430);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.info("Response recevied "+responseobj.getResponseCode());
				}
			return response;
		}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/VoucherIssueCount")
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
	public Response voucherIssueCount(
			// @Context HttpServletRequest Req, Consumer consumer
			@QueryParam("AppId") String appId,
			@QueryParam("OfferId") int offerId) {

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
				   logger.debug("Response returned 404");
			}
			return response;
		}

		ArrayList<Integer> resultCode = null;
		resultCode = (ArrayList<Integer>) MrasCache.MCache
				.recover("VoucherIssueCount" + offerId);
		if (resultCode == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Not found in cache putting into the cache");
			}
			resultCode = outletResourceDao.checkVoucherIssueCount(offerId);
			if (resultCode.size() > 0
					&& ((resultCode.get(0) == 200) || (resultCode.get(0) == 403))) {
				MrasCache.MCache.admit("VoucherIssueCount" + offerId,
						resultCode, 120000, 120000);
			}
		}

		if (resultCode.size() > 0) {

			if (resultCode.get(0) == 200) {
				VoucherIssueCountModal voucherIssueCountModal = new VoucherIssueCountModal();
				voucherIssueCountModal.setMaxRedemeption(resultCode.get(1));
				voucherIssueCountModal.setRedemptionTillNow(resultCode.get(2));
				response = Response.status(Response.Status.OK)
						.entity(voucherIssueCountModal).build();
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
					   logger.debug("Response returned 430");
				}
				return response;
			}

		} else {
			responseobj.setResponseMessage(WebConstant.UNHANDLED_ERROR);
			responseobj.setResponseCode(430);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.info("Response recevied "+responseobj.getResponseCode());
				}
			return response;
		}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/WNGetOutletDetails")
	/** Description of wNGetOutletDetails() .
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @param appId        		This is the appId.
	 * @param outletCode        This is the outletCode.
	 * @return                	Details of the given outlet.
	 * @param callBack        	This is the callBack String, will be used by webservice consumers to get the jsonp response.
	 */
	public Response wNGetOutletDetails(@QueryParam("AppId") String appId,
			@QueryParam("OutletCode") long outletCode,
			@QueryParam("CallBack") String callBack) {

		Response responseIn = null;
		Response responseOutput = null;
		JSONObject myObject = new JSONObject();

		responseIn = getOutletDetails(appId, outletCode);

		if (responseIn.getEntity() instanceof ResponseMRAS) {
			if (logger.isDebugEnabled()) {
				logger.debug("geting object of ResponseMRAS and message "
						+ responseIn.getEntity().toString());
			}
			responseOutput = Response
					.status(200)
					.entity(callBack + "(" + responseIn.getEntity().toString()
							+ ")").build();
			return responseOutput;
		} else {

			GenericEntity<Object> genericListOfObjects = (GenericEntity<Object>) responseIn
					.getEntity();
			if (genericListOfObjects.getEntity() instanceof OuletInformation) {
				logger.debug("geting object of OuletInformation ");

				responseOutput = Response
						.status(200)
						.entity(callBack + "("
								+ genericListOfObjects.getEntity().toString()
								+ ")").build();

				// responseOutput =
				// Response.status(200).entity(callBack+"("+array.toString()+")").build();
				return responseOutput;
			} else {
				ResponseMRAS responseMRAS = new ResponseMRAS();
				responseMRAS.setResponseCode(450);
				responseMRAS
						.setResponseMessage("Not able to parse the object return");
				responseOutput = Response.status(200)
						.entity(callBack + "(" + responseMRAS.toString() + ")")
						.build();
				return responseOutput;
			}
		}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/GetOutletDetails")
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
	public Response getOutletDetails(@QueryParam("AppId") String appId,
			@QueryParam("OutletCode") long outletCode) {
		ResponseMRAS responseobj = null;
		Response response = null;
		if (logger.isDebugEnabled()) {
			logger.debug("Parameter received are appId " + appId
					+ ", outletCode: " + outletCode + "");
		}

		ResponseMRAS responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(200).entity(responseObj).build();
			return response;
		}
		responseobj = new ResponseMRAS();

		if (outletCode == 0) {

			responseobj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			responseobj.setResponseCode(404);
			response = Response.status(200).entity(responseobj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 404");
			}
			return response;

		}

		// fetch the list of nearby outlets..
		OuletInformation ouletInformation = outletResourceDao
				.getOutletDetailsDao(outletCode);

		if (ouletInformation == null) {

			responseobj.setResponseMessage(WebConstant.INVALID_OUTLET_CODE);
			responseobj.setResponseCode(414);
			response = Response.status(200).entity(responseobj).build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 414");
			}
			return response;

		}
		// logger.debug("listoutlet : " + listoutlet);

		GenericEntity<OuletInformation> listentity = new GenericEntity<OuletInformation>(
				ouletInformation) {
		};
		response = Response.status(200).type(MediaType.APPLICATION_JSON)
				.entity(listentity).build();
		if (logger.isDebugEnabled()) {
			   logger.debug("Response returned 200");
		}

		return response;

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/GetConsumerMessage")
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
	public Response getConsumerMessage(@QueryParam("AppId") String appId,
			@QueryParam("ConsumerId") int consumerId,
			@QueryParam("LastRefresh") String lastRefresh) {
		ResponseMRAS responseobj = null;
		Response response = null;
		if (logger.isDebugEnabled()) {
			logger.debug("Parameter received are  AppId" + appId
					+ ", consumerId: " + consumerId + " ,lastRefresh: "
					+ lastRefresh + "");
		}

		ResponseMRAS responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			return response;
		}

		responseobj = new ResponseMRAS();

		if (consumerId == 0) {
			responseobj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			responseobj.setResponseCode(404);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 404");
			}
			return response;
		}
		Date date = null;

		if (lastRefresh != null) {
			try {
				date = (Date) formatter.parseObject(lastRefresh);

			} catch (ParseException e) {

				logger.info(WebConstant.DATE_FORMAT);
				e.printStackTrace();
				responseobj.setResponseCode(406);
				responseobj.setResponseMessage(WebConstant.DATE_FORMAT);

				response = Response.status(200).entity(responseobj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned 406");
				}
				return response;
			}
		}

		boolean validateConsumerId = outletResourceDao
				.validateConsumerId(consumerId);
		if (validateConsumerId) {
			// MessageResponse messagelist=new MessageResponse();
			MessageResponse messagelist = outletResourceDao
					.getConsumerMessageList(consumerId, date, appId);

			if (messagelist == null) {
				List<Message> messagelist1 = new ArrayList<Message>();
				messagelist = new MessageResponse();
				messagelist.setMessagelist(messagelist1);
			}

			response = Response.status(Response.Status.OK)
					.type(MediaType.APPLICATION_JSON).entity(messagelist)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.info("Response recevied ");
				}
			return response;
		} else {
			responseobj.setResponseMessage(WebConstant.INVALID_CONSUMERID);
			responseobj.setResponseCode(405);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 405");
			}
			return response;
		}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/GetOfferOutlets")
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
	public Response getOfferOutlets(@QueryParam("AppId") String appId,
			@QueryParam("OfferId") int offerId) {
		ResponseMRAS responseobj = null;
		OutletList outletList = null;
		Response response = null;
		if (logger.isDebugEnabled()) {
			logger.debug("Parameter received are  AppId" + appId
					+ ", offerId: " + offerId + "");
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
				   logger.debug("Response returned 404");
			}
			return response;
		}

		Offer offer = (Offer) MrasCache.MCache.recover("Offer" + offerId);
		Date now = new Date();
		if (offer == null) {
			// not found in cache recive from db and update cache
			offer = rewardResourceDao.getOfferWithOfferid(offerId);

			if (offer == null) {
				responseobj.setResponseCode(420);
				responseobj.setResponseMessage(WebConstant.INVALID_OFFERID);
				response = Response.status(200).entity(responseobj).build();
				if (logger.isDebugEnabled()) {
					   logger.debug("Response returned 420");
				}
				return response;
			}

			long timeToLive1 = offer.getRedemptionEndTime().getTimeInMillis()
					- now.getTime();
			MrasCache.MCache.admit("Offer" + offerId, offer, timeToLive1,
					timeToLive1);

		}

		int seperateOutletFlag = 1;
		if (offer.isSeperateOutlet()) {
			seperateOutletFlag = 2;
		}

		List<OutletWithoutCampaignId> offerOutletList = outletResourceDao
				.getOfferOutletsDao(offerId, appId, seperateOutletFlag);

		if (offerOutletList == null) {
			responseobj.setResponseMessage(WebConstant.NO_MATCH_FOUND);
			responseobj.setResponseCode(411);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 411");
			}
			return response;

		}
		/*
		 * JSONObject myObject = new JSONObject(); JSONArray array = new
		 * JSONArray(); Iterator iterator = offerOutletList.iterator(); while
		 * (iterator.hasNext()) { try { myObject = new
		 * JSONObject(((OutletWithoutCampaignId) iterator.next()).toString()); }
		 * catch (JSONException e) { logger.info(
		 * "Exception while itrating OutletWithoutCampaignId in getOfferOutlets"
		 * +e.getMessage()); e.printStackTrace(); } array.put(myObject); }
		 * response =
		 * Response.status(200).entity(callBack+"("+array.toString()+")"
		 * ).build(); return response;
		 */
		outletList = new OutletList();

		outletList.setOutletList(offerOutletList);
		response = Response.status(Response.Status.OK)
				.type(MediaType.APPLICATION_JSON).entity(outletList).build();
		if (logger.isDebugEnabled()) {
			   logger.info("Response recevied ");
			}

		return response;

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/SearchOfferOutlet")
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
	public Response searchOfferOutlet(@QueryParam("Latitude") double latitude,
			@QueryParam("Longitude") double longitude,
			@QueryParam("Radius") double radius,
			@QueryParam("AppId") String appId,
			@QueryParam("CampaignId") int campaignId,
			@QueryParam("NumberOfOutlet") int numberOfOutlet) {

		ResponseMRAS responseobj = null;
		OutletList outletList = null;
		Response response = null;
		if (logger.isDebugEnabled()) {
			logger.debug("Parameter received are  AppId" + appId
					+ ", Latitude: " + latitude + ", longitude: " + longitude
					+ ", radius: " + radius + ", campaignId: " + campaignId
					+ ", numberOfOutlet: " + numberOfOutlet + "");
		}

		ResponseMRAS responseObj = RewardResource.validateAppId(appId);
		if (responseObj != null) {
			response = Response.status(Response.Status.OK).entity(responseObj)
					.build();
			return response;
		}

		responseobj = new ResponseMRAS();

		if (campaignId == 0 || latitude == 0 || longitude == 0 || radius == 0) {
			responseobj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			responseobj.setResponseCode(404);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 404");
			}
			return response;
		}

		// logger.debug("before calling function");

		/*
		 * ResponceSearchOfferOutlet responceSearchOfferOutlet =
		 * outletResourceDao .searchOfferOutletDao(latitude, longitude, radius,
		 * appId, campaignId, numberOfOutlet);
		 */

		ResponceSearchOfferOutlet responceSearchOfferOutlet = outletResourceDao
				.searchOfferOutletDao(latitude, longitude, radius, appId,
						campaignId, numberOfOutlet);

		
		if (responceSearchOfferOutlet == null) {
			
			responseobj.setResponseMessage(WebConstant.NO_MATCH_FOUND);
			responseobj.setResponseCode(411);
			response = Response.status(Response.Status.OK).entity(responseobj)
					.build();
			if (logger.isDebugEnabled()) {
				   logger.debug("Response returned 411");
			}
			return response;

		}

		response = Response.status(Response.Status.OK)
				.type(MediaType.APPLICATION_JSON)
				.entity(responceSearchOfferOutlet).build();
		if (logger.isDebugEnabled()) {
			   logger.info("Response recevied ");
			}

		return response;

	}

}