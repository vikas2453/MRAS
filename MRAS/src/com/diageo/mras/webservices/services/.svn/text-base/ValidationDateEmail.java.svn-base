package com.diageo.mras.webservices.services;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.diageo.mras.webservices.dao.ValidationDateEmailDao;
import com.sun.jersey.core.util.Base64;

public class ValidationDateEmail {
	private static final Format formatter3 = new SimpleDateFormat("yyyy-MM-dd");
	private static final ValidationDateEmailDao validationDateEmailDao = new ValidationDateEmailDao();
	// ResponseMRAS responseObj = null;
	// Response response = null;
	static Logger logger = Logger
			.getLogger(ValidationDateEmail.class.getName());

	public int validateConsumer(HttpServletRequest request)  throws Exception{

		
			//logger.debug("value recive is:-" + request.getParameter("strAuth"));
		
		// System.out.println("-------"+Base64.base64Decode(request.getHeader("Authorization")));

		// String authorization= request.getHeader("Authorization");
		String authorization = request.getParameter("strAuth");
		
			//logger.debug("authorization string is :" + authorization);
		

		// System.out.println("authorization string is :" + authorization);
		if(authorization!=null){
			
			if (logger.isDebugEnabled()) {
				logger.debug("authorization from MCAL is  :"+authorization);
			}
			String[] temp = authorization.split(" ");
	
			if (!temp[0].equalsIgnoreCase("Basic")) {
				return 0;
			}
			// System.out.println("after split authorization string basic is :" +
			// temp[0] +" other "+temp[1]);
	
			if (logger.isDebugEnabled()) {
				logger.debug("after split authorization string basic is :"
						+ temp[0] + " other " + temp[1]);
			}
	
			String email_date = Base64.base64Decode(temp[1]);
	
			// System.out.println("decoding email_date" + email_date);
			String[] temp1 = email_date.split(":");
			if (logger.isDebugEnabled()) {
				logger.debug("decoding email_date" + email_date);
			}
	
		
			if (temp1[1] == null || temp1[0] == null) {
				return 0;
			}
	
			Date birthDate = (Date) formatter3.parseObject(temp1[1]);
	
			// System.out.println("passing parameter "+temp[0]+" date "+temp[1]);
			if (logger.isDebugEnabled()) {
				logger.debug("passing parameter " + temp[0] + " date " + temp[1]);
			}
			// Integer result =
			// validationDateEmailDao.validationDateEmailDao(temp[0],birthDate);
	
			int result = validationDateEmailDao.validationDateEmailDao(temp1[0],
					birthDate);
			if (logger.isDebugEnabled()) {
				logger.debug("return result " + result);
			}
	
			logger.info("return result " + result);
			return result;
		}
		else
		{
			if (logger.isDebugEnabled()) {
				logger.debug("authorization is null from MCAL");
			}
			return 0;
		}
		
		// System.out.println("return result "+result);

		
	}

}
