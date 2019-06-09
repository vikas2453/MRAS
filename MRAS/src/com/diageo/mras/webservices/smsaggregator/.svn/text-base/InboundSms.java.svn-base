package com.diageo.mras.webservices.smsaggregator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.diageo.mras.webservices.dao.ConsumerURLFromMessageDao;
import com.diageo.mras.webservices.encryption.StringEncrypter;
import com.diageo.mras.webservices.modals.TextUrl;

/**
 * Servlet implementation class MessageParsing
 */
public class InboundSms extends HttpServlet {
	static String passPhrase = "Diageo@123";

	private static StringEncrypter desEncrypter = new StringEncrypter(
			passPhrase);
	private static final long serialVersionUID = 1L;
	private static final String smsBody = "XmlMsg";
	private static final Logger logger = Logger.getLogger(InboundSms.class
			.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InboundSms() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// request.getParameterNames();

		InboundSmsUtil parser = new InboundSmsUtil();

		// parser.validateParams(new Object[]{5,"",0.1});
		// logger.info("param from body is :- "+request.getParameter(smsBody));

		// InputStream is= request.getInputStream();

		int c, avail = 2048, offset = 0;
		char cbuf[] = new char[avail];

		BufferedReader in = request.getReader();

		// read the body of the http message, using buffered input
		while ((c = in.read(cbuf, offset, avail)) != -1) {
			offset += c;
			avail -= c;

		}

		in.close();

		// MO messages are contained in the body, xml-encoded
		String body = new String(cbuf);

		/*
		 * URLDecoder decoder = new URLDecoder(); body =
		 * decoder.decode(body,"UTF-8");
		 */

		String newbody = body.substring(7, body.length()).trim();

		// String[] numberMsg=parser.parseXML(request.getParameter(smsBody));
		String[] numberMsg = parser.parseXML(newbody);

		PrintWriter out = response.getWriter();

		if (null != numberMsg) {

			TextUrl texturlobj = ConsumerURLFromMessageDao
					.getUrlFromMessage(numberMsg[1]);

			if (texturlobj == null) {
				logger.info("rs is null and URL not found for the message.");
				out.println("SMS will not be sent to consumer " + numberMsg[0]
						+ "URL not found for the message.");

			}

			else {
			
				if ((!(texturlobj.getURL() == null))
						&& texturlobj.getURL().trim().length() > 0) {

					if (texturlobj.isSMSPhone() == true
							&& texturlobj.isPhoneEncryption() == true) {
						// An encrypted phone-Number will be sent along with the
						// URL
						String encrptedPhoneNo = desEncrypter
								.encrypt(numberMsg[0]);
						OutboundSms.outboundSmsClient(numberMsg[0],
								texturlobj.getURL() + "/" + encrptedPhoneNo);
						out.println("SMS will be sent to consumer "
								+ numberMsg[0] + "With URL"
								+ texturlobj.getURL() + "/" + encrptedPhoneNo);

					}

					if (texturlobj.isSMSPhone() == true
							&& texturlobj.isPhoneEncryption() == false) {
						// Phone number to be sent with URL
						OutboundSms.outboundSmsClient(numberMsg[0],
								texturlobj.getURL() + "/" + numberMsg[0]);
						out.println("SMS will be sent to consumer "
								+ numberMsg[0] + "With URL"
								+ texturlobj.getURL() + "/" + numberMsg[0]);

					}

					if (texturlobj.isSMSPhone() == false
							&& texturlobj.isPhoneEncryption() == false) {
						// only URL will be sent
						OutboundSms.outboundSmsClient(numberMsg[0],
								texturlobj.getURL());
						out.println("SMS will be sent to consumer "
								+ numberMsg[0] + "With URL"
								+ texturlobj.getURL());
					}

				}

				else {
					logger.info("rs is null and URL not found for the message.");
					out.println("SMS will not be sent to consumer "
							+ numberMsg[0] + "URL not found for the message.");
				}

			}
		}

	}

}
