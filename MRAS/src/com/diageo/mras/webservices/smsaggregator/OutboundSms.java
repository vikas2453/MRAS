package com.diageo.mras.webservices.smsaggregator;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.httpclient.HttpConnection;
import org.apache.log4j.Logger;

import com.diageo.mras.webservices.init.MrasConstants;
import com.diageo.mras.webservices.init.PropertyReader;

public class OutboundSms {
	private static final Logger logger = Logger.getLogger(OutboundSms.class
			.getName());
	private static String SMS_AGGREGATOR = PropertyReader
			.getPropertyValue(MrasConstants.SMS_AGGREGATOR_URL);
	private static String SMS_AGGREGATOR_ACKNOWLEDGEMENT = PropertyReader
			.getPropertyValue(MrasConstants.SMS_AGGREGATOR_ACKNOWLEDGEMENT);
	private static MyAuthenticator authobj = new MyAuthenticator();

	public static void outboundSmsClient(String phoneNumber, String text) {
		String moServlet, data;

		logger.info("data recieved are " + phoneNumber + " url text  " + text);
		// include the path to the MO servlet as the AckReplyAddress parameter
		moServlet = SMS_AGGREGATOR_ACKNOWLEDGEMENT;
		if (!phoneNumber.contains("+")) {
			phoneNumber = "+" + phoneNumber;
		}
		logger.info("Phonenumber check" + phoneNumber);
		data = "[MSISDN]\n";

		data += "List=" + phoneNumber + "\n";
		data += "[MESSAGE]\n";
		data += "Text=" + text + "\n";
		data += "[SETUP]\n";
		data += "AckReplyAddress=" + moServlet + "\n";
		data += "[END]";
		logger.info("Sending data:\n" + data);

		// install MyAuthenticator class as the default authenticator

		Authenticator.setDefault(authobj);

		try {
			// connect to the Mobile 365 hub
			URL u = new URL(SMS_AGGREGATOR);
			URLConnection uc = u.openConnection();
			// enabling sending data to server
			uc.setDoOutput(true);
			// send HTTP POST request, using buffered output
			OutputStream rawOut = uc.getOutputStream();
			OutputStream bufferOut = new BufferedOutputStream(rawOut);
			OutputStreamWriter out = new OutputStreamWriter(bufferOut);
			out.write(data);
			out.close();
			// read HTTP response, using buffered input
			// the HTTP response contains an orderid for each mt message sent
			InputStream rawIn = uc.getInputStream();
			InputStream bufferIn = new BufferedInputStream(rawIn);
			InputStreamReader in = new InputStreamReader(bufferIn);

			// hardcoding buffer to 1k...a real application should use
			// a more robust method for retrieving the data
			int c, avail = 1024, offset = 0;
			char cbuf[] = new char[avail];
			while ((c = in.read(cbuf, offset, avail)) != -1) {
				offset += c;
				avail -= c;
			}
			in.close();

			String response = new String(cbuf);
			logger.info("Received response:" + response);

		} catch (IOException e) {
			logger.error("Error while sending sms ");
			e.printStackTrace();
		}

	}
}
