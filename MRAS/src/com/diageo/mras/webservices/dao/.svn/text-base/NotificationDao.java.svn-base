package com.diageo.mras.webservices.dao;

import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import java.util.Vector;

import java.util.Map;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;

import com.diageo.mras.webservices.init.ConnectionPool;
import com.diageo.mras.webservices.init.PropertyReader;
import com.diageo.mras.webservices.modals.Notificationmodal;
import com.diageo.mras.webservices.services.SpontaneousDistribution;

public class NotificationDao {

	private static final Logger logger = Logger.getLogger(NotificationDao.class
			.getName());

	public List<Notificationmodal> notificationdaomethod(int timeInMinutes) {
		Connection con = null;
		List<Notificationmodal> notificationlist = new ArrayList<Notificationmodal>();
		List<Notificationmodal> notificationlistDistribution = new ArrayList<Notificationmodal>();
		List<Notificationmodal> notificationlistGift = new ArrayList<Notificationmodal>();
		ResultSet rs = null, rs1 = null, rs2 = null, rsGiftNotification=null;

		try {
			/*
			 * if (logger.isDebugEnabled()) {
			 * logger.debug("paremeter recevied are TimeinMinutes " +
			 * timeInMinutes); }
			 */
			con = ConnectionPool.getConnection();

			/*
			 * logger.debug("calling to mapConsumerId"+notificationlist);
			 * List<Notificationmodal>
			 * notificationlist_bub_busy=pubGetBusy(con); for(Notificationmodal
			 * object:notificationlist_bub_busy){ notificationlist.add(object);
			 * }
			 */

			PreparedStatement stmt = con
					.prepareStatement(SqlStatements.GET_NOTIFICATION_DEVICES);
			// set the parameters..
			stmt.setInt(1, timeInMinutes);
			// execute the query..
			rs = stmt.executeQuery();

			String RewardsNotificationMessageEnglish = PropertyReader
					.getPropertyValue("MRAS_REWARD_ENGLISH_MESSAGE");
			String RewardsNotificationMessageRegional = PropertyReader
					.getPropertyValue("MRAS_REWARD_REGIONAL_MESSAGE");

			
			//Old code block for distribution noti
		/*	PreparedStatement stmt1 = con
					.prepareStatement(SqlStatements.GET_NOTIFICATION_DEVICES1);
			// set the parameters..
			stmt1.setInt(3, timeInMinutes);
			stmt1.setString(1, RewardsNotificationMessageEnglish);
			stmt1.setString(2, RewardsNotificationMessageRegional);
			// execute the query..
			rs1 = stmt1.executeQuery();*/

			
			//new code block for distribution noti
			PreparedStatement stmt1 = con
					.prepareStatement(SqlStatements.GET_NOTIFICATION_DEVICES1);
			// set the parameters..
			stmt1.setInt(1, timeInMinutes);			
			// execute the query..
			rs1 = stmt1.executeQuery();
			
			//new ends
			
			
			
			//start changes gift notification

			PreparedStatement stmtGiftNotification = con
					.prepareStatement(SqlStatements.GET_NOTIFICATION_GIFTING);
			// set the parameters..
			stmtGiftNotification.setInt(1, timeInMinutes);		
			// execute the query..
			rsGiftNotification = stmtGiftNotification.executeQuery();
			
			
			// end changes gift notification

			String groupNotificationMessage = PropertyReader
					.getPropertyValue("MRASGROUP_MESSAGE");
			PreparedStatement stmt2 = con
					.prepareStatement(SqlStatements.GET_NOTIFICATION_DEVICES2);
			// set the parameters..
			stmt2.setInt(2, timeInMinutes);
			stmt2.setString(1, groupNotificationMessage);
			// execute the query..
			rs2 = stmt2.executeQuery();

			if (!rs.next()) {

			} else {
				rs.previous();
				/*while (rs.next()) {
					Notificationmodal notificationobj = new Notificationmodal(
							rs.getString("deviceid"), rs.getInt("devicetype"),
							rs.getString("NotificationContentEnglish"),
							rs.getString("NotificationContentRegional"));

					if (logger.isDebugEnabled()) {
						logger.debug("Result received for  are"
								+ notificationlist);
					}
					notificationlist.add(notificationobj);
				}*/
				
				
				notificationlist=getNotificationmodal( rs, notificationlist,1);
				rs.close();
				stmt.close();
			}
			//start changes gift notification
			
			if (!rsGiftNotification.next()) {

			} else {
				rsGiftNotification.previous();
				
				notificationlistGift=getNotificationmodal( rsGiftNotification, notificationlistGift,2);
				
				/*while (rsGiftNotification.next()) {
					Notificationmodal notificationobj = new Notificationmodal(
							rsGiftNotification.getString("deviceid"),
							rsGiftNotification.getInt("devicetype"),
							rsGiftNotification.getString("NotificationContentEnglish"),
							rsGiftNotification.getString("NotificationContentRegional"),
							rsGiftNotification.getString("name"),
							rsGiftNotification.getInt("offerId"));

					if (logger.isDebugEnabled()) {
						logger.debug("Result received for gifting is  are"
								+ notificationlist);
					}
							
					notificationlistGift.add(notificationobj);					
				}*/	
				
				logger.debug("Result received for gifting is  are"
						+ notificationlistGift.toString());
				rsGiftNotification.close();
				stmtGiftNotification.close();
			}
			
			
			
			
			if (!rs1.next()) {

			} else {
				rs1.previous();
				while (rs1.next()) {
					Notificationmodal notificationobj = new Notificationmodal(
							rs1.getString("deviceid"),
							rs1.getInt("devicetype"),
							RewardsNotificationMessageEnglish,
							RewardsNotificationMessageRegional,
							"",
							rs1.getInt("offerId"));

					if (logger.isDebugEnabled()) {
						logger.debug("Result received for distribution are"
								+ notificationlist);
					}
					//start changes gift notification
					logger.debug("Result received for distribution are"
							+ notificationlistDistribution.toString());
					notificationlistDistribution.add(notificationobj);
					
					//notificationlist.add(notificationobj);
				}
				//notificationlistDistribution=getNotificationmodal( rs1, notificationlistDistribution);	
				notificationlist=addGiftNotification(notificationlist,notificationlistDistribution,notificationlistGift);
				
				logger.debug("result notificationlist "	+ notificationlist.toString());
			}
			rs1.close();
			stmt1.close();

			if (!rs2.next()) {

			} else {
				rs2.previous();
				/*while (rs2.next()) {
					Notificationmodal notificationobj = new Notificationmodal(
							rs2.getString("deviceid"),
							rs2.getInt("devicetype"),
							rs2.getString("NotificationContentEnglish"),
							rs2.getString("NotificationContentRegional"));

					if (logger.isDebugEnabled()) {
						logger.debug("Result received for  are"
								+ notificationlist);
					}
					notificationlist.add(notificationobj);
				}*/
				notificationlist=getNotificationmodal( rs2, notificationlist,1);
				rs2.close();
				stmt2.close();
			}

			// for IndependentNotification notification starts

			independentNotification(notificationlist, con);

			// ----------for IndependentNotification notification
			// ends------------------------

			// ---

		}

		catch (SQLException e) {
			
			logger.info("exception from notificationdaomethod "+e.getMessage());
			e.printStackTrace();
		}

		finally {
			// returning the connection to the pool..
			ConnectionPool.returnConnection(con);
		}
		return notificationlist;
	}

	private List<Notificationmodal> addGiftNotification(List<Notificationmodal> notificationlist,
			       List<Notificationmodal> notificationlistDistribution,
		        	List<Notificationmodal> notificationlistGift) {
		
		/*if(logger.isDebugEnabled()){
		logger.debug("after calling notificationlist"	+ notificationlist.toString());
		logger.debug("after calling notificationlistDistribution"	+ notificationlistDistribution.toString());
		logger.debug("after calling notificationlistGift"	+ notificationlistGift.toString());
		}*/
	
		 notificationlistDistribution=(ArrayList<Notificationmodal>)ListUtils.subtract
		(notificationlistDistribution, notificationlistGift);
		
		logger.debug("after subtract calling notificationlistDistribution"	+ notificationlistDistribution.toString());
		
		notificationlist=(ArrayList<Notificationmodal>)ListUtils.sum(notificationlist, notificationlistDistribution);
		
		String RewardsNotificationGifting = PropertyReader.getPropertyValue("MRAS_GIFT_MESSAGE");
		
		
		
		for(Notificationmodal objectNotificationmodal1:notificationlistGift){
			
			objectNotificationmodal1.setNotificationContentEnglish(objectNotificationmodal1.getName()+" "+RewardsNotificationGifting+" "+objectNotificationmodal1.getNotificationContentEnglish());
			objectNotificationmodal1.setNotificationContentRegional(objectNotificationmodal1.getName()+" "+RewardsNotificationGifting+" "+objectNotificationmodal1.getNotificationContentRegional());
			notificationlist.add(objectNotificationmodal1);
			
		}
	
		//notificationlistDistribution=(ArrayList<Notificationmodal>)ListUtils.sum(list1, list2);
		
		return notificationlist;
	}
	
	public List<Notificationmodal> getNotificationmodal(ResultSet resultset,List<Notificationmodal> notificationlist,int x){
		//Notificationmodal notificationmodal=new Notificationmodal();
		try {
			while (resultset.next()) {
				
				
			  Notificationmodal notificationmodal=new Notificationmodal();
			  if(x==1){
				  notificationmodal = new Notificationmodal(
						resultset.getString("deviceid"), resultset.getInt("devicetype"),
						resultset.getString("NotificationContentEnglish"),
						resultset.getString("NotificationContentRegional"));
			  }else{
				  notificationmodal = new Notificationmodal(
						resultset.getString("deviceid"),
						resultset.getInt("devicetype"),
						resultset.getString("NotificationContentEnglish"),
						resultset.getString("NotificationContentRegional"),
						resultset.getString("name"),
						resultset.getInt("offerId"));
			  }
			  notificationlist.add(notificationmodal);
			  
			}
			
			
		} catch (SQLException e) {
		    logger.info("exception in getNotificationmodal"+e.getMessage());
			e.printStackTrace();
		}
		return notificationlist;
	}
	
	
	
	
	
	
	

	/**
	 * This function adds notification in the notification list corresponding to
	 * Independent notifications.
	 * 
	 * @param List
	 *            <Notificationmodal> , Connection
	 * @return void
	 * 
	 */
	private void independentNotification(
			List<Notificationmodal> notificationlist, Connection con) {
		// logger.info("in independent notification");

		try {

			/*
			 * PreparedStatement stmtIndependentNotification = con
			 * .prepareStatement(SqlStatements.GET_INDEPENDENT_NOTIFICATION);
			 * 
			 * ResultSet rsIndependentNotification =
			 * stmtIndependentNotification.executeQuery();
			 */

			CallableStatement cs = con
					.prepareCall("{call independentnotification()}");
			cs.execute();
			ResultSet rsIndependentNotification = cs.getResultSet();

			while (rsIndependentNotification.next()) {

				String path = rsIndependentNotification
						.getString("UserGroupFilePath");
				String appid = rsIndependentNotification
						.getString("appid");
				int targetDays = rsIndependentNotification.getInt("targetDays");
				int targetCheckIn = rsIndependentNotification
						.getInt("targetCheckIn");

				int notificationId = rsIndependentNotification
						.getInt("NotificationId");
				if (logger.isDebugEnabled()) {
					logger.debug("for notification id : " + notificationId+" : appid :"+appid);
				}

				if (path != null && path.length() > 1) {

					// logger.info("in independent notification inside path");

					List<Integer> consumerIdList;

					File yourFile = new File(
							System.getProperty("jboss.server.data.dir"), path);
					String str = null;
					try {
						str = yourFile.getCanonicalPath();

					} catch (IOException e) {
						e.printStackTrace();
					}
					Vector dataHolder = SpontaneousDistribution
							.readUserList(str);
					consumerIdList = SpontaneousDistribution
							.getConsumerList(dataHolder);

					insertNotificationForConsumers(notificationlist,
							consumerIdList, notificationId, con);

				} else if (0 != targetCheckIn && 0 != targetDays) {

					// logger.info("in independent notification inside checkin filter");

					List<Integer> consumerIdList = new ArrayList<Integer>();

					ResultSet rsCheckinConsumers = null;
					PreparedStatement stmtCheckinConsumers = null;

					stmtCheckinConsumers = con
							.prepareStatement(SqlStatements.INDEPENDENT_NOTIFICATION_CHECKIN_FILTER);
					stmtCheckinConsumers.setInt(1, targetDays);
					stmtCheckinConsumers.setString(2, appid);
					stmtCheckinConsumers.setInt(3, targetCheckIn);
					rsCheckinConsumers = stmtCheckinConsumers.executeQuery();
					while (rsCheckinConsumers.next()) {
						consumerIdList.add(rsCheckinConsumers
								.getInt("consumerId"));
					}

					rsCheckinConsumers.close();
					stmtCheckinConsumers.close();

					insertNotificationForConsumers(notificationlist,
							consumerIdList, notificationId, con);

				} else {

					// logger.info("in independent notification inside all consumers");

					ResultSet rsAllConsumer = null;
					PreparedStatement stmtAllConsumer = null;										
					String contentEnglish=rsIndependentNotification.getString("ContentEnglish");
					String contentRegional=rsIndependentNotification.getString("ContentRegional");
					
					stmtAllConsumer = con
							.prepareStatement(SqlStatements.GET_INDEPENDENT_NOTIFICATION_ALL);
					stmtAllConsumer.setString(1, appid);
					rsAllConsumer = stmtAllConsumer.executeQuery();
					while (rsAllConsumer.next()) {
						Notificationmodal notificationobj = new Notificationmodal(
								rsAllConsumer.getString("deviceid"),
								rsAllConsumer.getInt("devicetype"),
								contentEnglish,contentRegional
								);
						notificationlist.add(notificationobj);
					}
					//notificationlist=getNotificationmodal( rsAllConsumer, notificationlist,1);
					
					

					rsAllConsumer.close();
					stmtAllConsumer.close();
				}
			}

		} catch (SQLException e) {
			logger.info("Exception in independent notification "
					+ e.getMessage());
			e.printStackTrace();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("in independent notification ends noti list is :- "
					+ notificationlist.toString());
		}

	}

	/**
	 * This method inserts the notification in notification list for every
	 * consumer in consumer list, for the given notificationId.
	 * 
	 * @param List
	 *            <Notificationmodal> notificationlist, List<Integer>
	 *            consumerIdList, int notificationId,Connection con
	 * @return void
	 */
	private void insertNotificationForConsumers(
			List<Notificationmodal> notificationlist,
			List<Integer> consumerIdList, int notificationId, Connection con) {
		try {
			for (Integer id : consumerIdList) {
				ResultSet rsAllConsumer = null;
				PreparedStatement stmtAllConsumer = null;

				stmtAllConsumer = con
						.prepareStatement(SqlStatements.GET_INDEPENDENT_NOTIFICATION_CONSUMER);
				stmtAllConsumer.setInt(1, notificationId);
				stmtAllConsumer.setInt(2, id);
				rsAllConsumer = stmtAllConsumer.executeQuery();
				while (rsAllConsumer.next()) {

					Notificationmodal notificationobj = new Notificationmodal(
							rsAllConsumer.getString("deviceid"),
							rsAllConsumer.getInt("devicetype"),
							rsAllConsumer.getString("ContentEnglish"),
							rsAllConsumer.getString("ContentRegional"));
					notificationlist.add(notificationobj);
				}

				rsAllConsumer.close();
				stmtAllConsumer.close();
			}
		} catch (SQLException e) {
			logger.info("Exception  in  insertNotificationForConsumers "
					+ e.getMessage());
			e.printStackTrace();
		}

	}

	private List<Notificationmodal> pubGetBusy(Connection con) {
		ResultSet rs = null;
		CallableStatement stmt = null;
		List<Notificationmodal> notificationlist1 = new ArrayList<Notificationmodal>();
		try {
			logger.debug("calling to pubBusyNotification");
			stmt = con.prepareCall("{ call pubBusyNotification() }");
			boolean resultsAvailable = stmt.execute();
			logger.debug("result is not empty:-" + resultsAvailable);

			if (resultsAvailable) {

				while (resultsAvailable) {
					logger.debug("In while loop with resultsAvailable is true");
					rs = stmt.getResultSet(); // Get a result set 2a
					Map<Integer, Notificationmodal> consumerIdMap = new HashMap<Integer, Notificationmodal>();
					while (rs.next()) {
						logger.debug("In while loop with resultsAvailable is true and rs have values");
						Integer consumerId = rs.getInt("cu.consumerid");
						String deviceId = rs.getString("deviceId");
						int deviceType = rs.getInt("devicetype");
						long Outlet_ShipTo = rs.getLong("Outlet_ShipTo");
						String MessageEnglish_text = rs
								.getString("MessageRegional_text");
						String MessageRegional_text = rs
								.getString("MessageRegional_text");

						if (consumerIdMap.containsKey(consumerId)) {
							logger.debug("contain keys" + consumerId);
							Notificationmodal notificationobj = consumerIdMap
									.get(consumerId);
							notificationobj
									.setNotificationContentEnglish(notificationobj
											.getNotificationContentEnglish()
											+ ", " + Outlet_ShipTo);
							notificationobj
									.setNotificationContentRegional(notificationobj
											.getNotificationContentRegional()
											+ ", " + Outlet_ShipTo);
							consumerIdMap.put(consumerId, notificationobj);
							logger.debug("set values"
									+ notificationobj
											.getNotificationContentEnglish()
									+ "--" + notificationobj.getDeviceId());
						} else {
							Notificationmodal notificationobj = new Notificationmodal();
							notificationobj.setDeviceId(deviceId);
							notificationobj.setDevicetype(deviceType);
							notificationobj
									.setNotificationContentEnglish(MessageEnglish_text
											+ " busy outletCode:- "
											+ Outlet_ShipTo);
							notificationobj
									.setNotificationContentRegional(MessageRegional_text
											+ " busy outletCode:-"
											+ Outlet_ShipTo);
							logger.debug("set values in if loop:-"
									+ notificationobj
											.getNotificationContentEnglish()
									+ "--" + notificationobj.getDeviceId());
							consumerIdMap.put(consumerId, notificationobj);
						}

					}
					for (Integer key : consumerIdMap.keySet()) {
						Notificationmodal notificationmodal = consumerIdMap
								.get(key);
						notificationlist1.add(notificationmodal);
					}
					resultsAvailable = stmt.getMoreResults();
					logger.debug("checking valuesfor true or false"
							+ resultsAvailable);
				}
				rs.close();
				stmt.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return notificationlist1;
	}

}
