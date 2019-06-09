package com.diageo.mras.webservices.smsaggregator;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;
import com.diageo.mras.webservices.responses.Root_SMS_MO;

public class InboundSmsUtil {

	
	private Root_SMS_MO obj;
	private static final Logger logger = Logger
	.getLogger(InboundSmsUtil.class.getName());
	//private SMSMO_Parameters parameters;
	//private Parameters_OpInfo operatorInfo;
	//private Parameters_RcvdTime receivedTime;
	//private OpInfo_OpCode operatorCode;
	
	/**
	 * parses the message body
	 * 
	 * @param messagebody String
	 * @return String array of length 2 :- number at position 0 and message at 1. 
	 */
	protected String[] parseXML(String urlInput)
	{	
		String[] numberMsg=null;
		if(null!=urlInput)
		{
		//String urlOutput=URLDecoder.decode(urlInput);
		String urlOutput="";
		
		try {
			urlOutput = URLDecoder.decode(urlInput , "ASCII");
			logger.info("output after decoding is : "+urlOutput);

		} catch (UnsupportedEncodingException e1) {		
			logger.error("UnsupportedEncodingException while decoding url body");
			e1.printStackTrace();
		}
		
		try{
			
		JAXBContext contextObj1 = JAXBContext
		.newInstance(Root_SMS_MO.class);
		
		Unmarshaller uuobj = contextObj1.createUnmarshaller();
		obj = (Root_SMS_MO) uuobj.unmarshal(new StreamSource(
				new StringReader(urlOutput)));
		}
		catch (JAXBException e) {
			logger.error("Error while unmarshalling.");
			e.printStackTrace();
		}
		
		if(obj!=null){
		numberMsg=new String[2];
		logger.info("Values of msisdn :- "+obj.getMsIsdn()+" : org add "+obj.getOrgAddress()+" : Message "+obj.getMessage());
		numberMsg[0]=obj.getMsIsdn().trim();
		numberMsg[1]=obj.getMessage();
								
		
		//TODO delete this code if other values not needed.
		/*parameters=obj.getParameters();
		if(parameters!=null)
		{
			System.out.println(" Values of Parameters children ");
			System.out.println("op Id :- "+parameters.getOpId()+" : Acc Id "+parameters.getAccountId()+" : Message ID "+parameters.getMsgId());
			System.out.println("DCS :- "+parameters.getdCS()+" : Class "+parameters.getClas()+" : Rec Serv No "+parameters.getRcvdServNo()+" : Keyword "+parameters.getKeyWord());
			operatorInfo=parameters.getOpInfo();
			receivedTime=parameters.getReceivedTime();
			if(operatorInfo!=null)
			{
				System.out.println(" Values of operatorInfo children ");
				System.out.println("op standard :- "+operatorInfo.getOpStandard());
				operatorCode=operatorInfo.getOpCode();
				if(operatorCode!=null)
				{
					System.out.println(" Values of operatorCode children ");
					System.out.println("MCC :- "+operatorCode.getmCC()+" : MNC :- "+operatorCode.getmNC());
				}
			}
			if(receivedTime!=null)
			{
				System.out.println(" Values of receivedTime children ");
				System.out.println("Date :- "+receivedTime.getDate()+" : Time"+receivedTime.getTime());
				
			}
		}
	*/	
		
		}
		}
		else
		{
			logger.info("Parameter not found in body of request");
		}
		return numberMsg;

	}	
	
	/*protected boolean validateParams(Object[] parameters)
	{
		
		for(Object param : parameters)
		{
			if(param.getClass().getSimpleName().equals("String"))
			{
				logger.info("in string "+((String)param).length());
				if(0==((String)param).length())
				{
					logger.info("in string return false");

					return false;
				}
			}
			else if(param.getClass().getSimpleName().equals("Integer"))
			{
				logger.info("in int "+((Integer)param).intValue());
				if(0==((Integer)param).intValue())
				{
					logger.info("in int return false");

					return false;
				}
			}
			else if(param.getClass().getSimpleName().equals("Double"))
			{
				logger.info("in double "+((Double)param).doubleValue());
				if(0.0==((Double)param).doubleValue())
				{
					logger.info("in double return false");
					return false;
				}
			}
			else
			{
				logger.info("class name is  : "+param.getClass().getSimpleName());
				
			}									
		}
		logger.info("return true ");
		return true;
	}*/

}
