package com.diageo.mras.webservices.services;

import org.apache.commons.javaflow.utils.RewritingUtils;
import org.apache.log4j.Logger;

import com.diageo.mras.webservices.init.MrasConstants;
import com.diageo.mras.webservices.init.PropertyReader;
import com.diageo.mras.webservices.smsaggregator.OutboundSms;

public class ExecuteSmsService implements Runnable {
	private static final Logger logger = Logger
			.getLogger(ExecuteSmsService.class.getName());
	private String rewardCode, phoneNumber, urlText;
	private int offerId;
	private long outletCode;

	private static final String SMS_AGRREGATOR_MESSAGE= PropertyReader
	.getPropertyValue(MrasConstants.SMS_AGRREGATOR_MESSAGE);
	private String smsText;
	public ExecuteSmsService(String phoneNumber, String urlText,
			String rewardCode, long outletCode, int offerId) {
		// this.offerId = offerId;
		this.phoneNumber = phoneNumber;
		this.urlText = urlText;
		this.rewardCode = rewardCode;
		this.outletCode = outletCode;
		this.offerId = offerId;
		smsText=SMS_AGRREGATOR_MESSAGE;
	}
	
	
	public ExecuteSmsService(String phoneNumber, String urlText,
			String rewardCode, long outletCode, int offerId, String smsText) {
		// this.offerId = offerId;
		this.phoneNumber = phoneNumber;
		this.urlText = urlText;
		this.rewardCode = rewardCode;
		this.outletCode = outletCode;
		this.offerId = offerId;
		this.smsText=smsText;
	}

	@Override
	public void run() {

		StringBuffer strbuf = new StringBuffer(urlText);
	
		String phoneNumberTrim = phoneNumber.replace(" ", "");

		strbuf.append("/" + offerId + "/" + rewardCode + "/" + outletCode + "/"
				+ phoneNumberTrim);
		urlText = strbuf.toString();
		//String smsWelcome=smsText;
		
		/*  StringBuffer strbufSMSMessage = new StringBuffer(smsWelcome);
		strbufSMSMessage.append("\n"+urlText);
		
		smsWelcome=strbufSMSMessage.toString();*/
		if(logger.isDebugEnabled()){
		
			logger.debug("Inside run..The text to be sent is:-"+smsText+" "+urlText);
		}
		
		OutboundSms.outboundSmsClient(phoneNumber,smsText+" "+urlText);

	}
}
