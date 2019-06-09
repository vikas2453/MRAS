package com.diageo.mras.webservices.services;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.diageo.mras.webservices.dao.NotificationDao;
import com.diageo.mras.webservices.modals.Notificationmodal;
import com.diageo.mras.webservices.responses.ResponseMRAS;

@Path("/Notification")
public class Notification {
	private static final Logger logger = Logger.getLogger(Notification.class.getName());

	private static final NotificationDao notifyobj = new NotificationDao();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/Notify")
	public Response NotificationDevice(
			@QueryParam("TimeInMinutes") int timeInMinutes) {

		Response response = null;

		ResponseMRAS responseObj = new ResponseMRAS();
		
		//logger.info("in notification time is :-"+timeInMinutes);
		// validate the mandatory parameters..
		if ((timeInMinutes == 0)) {
			// logger.debug("Mandatory parameters are missing");
			responseObj.setResponseMessage(WebConstant.MISSING_PARAMETER);
			responseObj.setResponseCode(404);

			response = Response.status(200).entity(responseObj).build();

			return response;

		}
		// validate campaingId and appId..

		// fetch the nearby outlets..
		List<Notificationmodal> notificationlist = notifyobj
				.notificationdaomethod(timeInMinutes);
		

		if (notificationlist == null) {
			// responseObj.setResponseMessage("No Result");
			// responseObj.setResponseCode(411);
			// response = Response.status(200).entity(responseObj).build();
		} else {
			GenericEntity<List<Notificationmodal>> listentity = new GenericEntity<List<Notificationmodal>>(
					notificationlist) {
			};
			response = Response.status(200).type(MediaType.APPLICATION_JSON)
					.entity(listentity).build();
		}

		return response;

	}

}
