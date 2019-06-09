/*
 * Copyright (c) 2005, Mobile 365, Inc.  4511 Singer Court, Suite 300,
 * Chantilly, VA, 20152.  703-961-8300.  http://www.mobile365.com.  All
 * rights reserved.
 * ---------------------------------------------------------------------------
 * This file contains unpublished proprietary source code of Mobile 365,
 * Inc.  The material in this file is the exclusive confidential property of
 * Mobile 365, Inc., is intended for internal use only within Mobile 365, and
 * is protected by copyright law.  The copyright notice found above does not
 * evidence any actual or intended publication of this source material.  Any
 * reproduction or distribution of the material in this file, either in whole
 * or in part, without the explicit prior written approval of Mobile 365, Inc.
 * is prohibited and will be prosecuted to the maximum extent possible under
 * the law.
 * ---------------------------------------------------------------------------
 *
 *   File Name: MyAuthenticator.java
 *
 *   Contents: Sample source file permitting basic HTTP authentication. This
 *             class is used by the Authenticator class, and is registered
 *             via the setDefault method.  This is performed in the main 
 *             method of the HttpMt class.  The user/password in this example
 *             should not be used - each customer will be assigned their own
 *             username/password combination. 
 * 
 * ---------------------------------------------------------------------------
 *
 */
package com.diageo.mras.webservices.smsaggregator;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import com.diageo.mras.webservices.init.MrasConstants;
import com.diageo.mras.webservices.init.PropertyReader;

public class MyAuthenticator extends Authenticator {
   private static String SMS_AGGREGATOR_USER_NAME = PropertyReader.getPropertyValue(MrasConstants.SMS_AGGREGATOR_USER_NAME);
   private static String SMS_AGGREGATOR_PASSWORD = PropertyReader.getPropertyValue(MrasConstants.SMS_AGGREGATOR_PASSWORD);
   public PasswordAuthentication getPasswordAuthentication() {

	   
	   
      String user =SMS_AGGREGATOR_USER_NAME;
      String password =SMS_AGGREGATOR_PASSWORD;
      return new PasswordAuthentication(user, password.toCharArray());
   }
}
