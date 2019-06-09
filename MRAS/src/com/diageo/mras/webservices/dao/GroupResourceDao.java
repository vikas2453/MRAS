package com.diageo.mras.webservices.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;

import com.diageo.mras.scheduler.dao.SchedulerDAO;
import com.diageo.mras.webservices.init.ConnectionPool;
import com.diageo.mras.webservices.modals.Group;
import com.diageo.mras.webservices.services.RewardResource;

/**
 * This is a DAO class for the Group.
 * 
 * @author Infosys Limited
 * @version 1.0
 */
public class GroupResourceDao {

	static Logger logger = Logger.getLogger(GroupResourceDao.class.getName());

	/**
	 * Description of SearchConsumerIdByPhoneNumbers()
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @param groupMembersPhone
	 *            List of phone numbers.
	 * @return A map of phone numbers(key), phone numbers(value)
	 */

	public HashSet<String> SearchConsumerIdByPhoneNumbers(
			List<String> groupMembersPhone) throws SQLException {
		// List<String> registeredNumbers = new ArrayList<String>();
		HashSet<String> registeredNumbers = new HashSet<String>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;

		StringBuilder querry = new StringBuilder(
				SqlStatements.SEARCH_CONSUMERBY_PHONEO);
		for (String phone : groupMembersPhone) {
			querry.append("'" + phone + "'" + ",");
		}
		querry.deleteCharAt(querry.length() - 1);
		querry.append(")");
		if (logger.isDebugEnabled()) {
			logger.debug(querry);
		}

		try {
			con = ConnectionPool.getConnection();
			stmt = con.prepareStatement(querry.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				registeredNumbers.add(rs.getString("PhoneNumber"));
			}
			rs.close();
			stmt.close();
		}

		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return registeredNumbers;
	}

	/**
	 * Description of viewConsumerInGroup(consumerId)
	 * 
	 * @param ConsumerID
	 *            This is the id for consumer.
	 * @return A list of groups along with the response code to
	 *         viewConsumerInGroup() web service
	 */
	public Map<String, Integer> viewConsumerInGroup(int consumerId) {
		Map<String, Integer> consumeridwithstatusmap = new HashMap<String, Integer>();
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			con = ConnectionPool.getConnection();
			stmt = con
					.prepareStatement(SqlStatements.GET__CONSUMERIDSTATUS_INGROUP);

			stmt.setInt(1, consumerId);

			rs = stmt.executeQuery();

			if (!rs.next()) {
				if (logger.isDebugEnabled()) {
					logger.debug(consumerId
							+ " Consumerid does not exist in the database");
				}
				consumeridwithstatusmap = null;
			} else {
				rs.previous();
				while (rs.next()) {
					consumeridwithstatusmap.put(rs.getString("phonenumber"),
							rs.getInt("statusflag"));

				}

				rs.close();
				stmt.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return consumeridwithstatusmap;
	}

	/**
	 * Description of viewAllGroups()
	 * 
	 * @param ConsumerID
	 *            This is the id for consumer.
	 * @return A list of groups along with the response code to ViewGroups() web
	 *         service
	 */

	public List<Group> viewAllGroups(int consumerID) {
		Connection con = null;
		ResultSet rs = null;

		List<Group> groupList = null;
		try {
			con = ConnectionPool.getConnection();
			PreparedStatement stmt = con
					.prepareStatement(SqlStatements.GET_ALL_GROUPS_FOR_A_CONSUMER);
			stmt.setInt(1, consumerID);

			rs = stmt.executeQuery();

			// If there are no results then we will just pass the list as null
			if (!rs.next()) {
				groupList = null;
			} else {

				rs.previous();
				groupList = new ArrayList<Group>();
				Group group;
				while (rs.next()) {

					group = new Group();

					group.setGroupID(rs.getInt("GroupID"));

					group.setStatus(rs.getString("StatusFlag"));
					group.setGroupOwnerPhoneNumber(rs.getString("phoneNumber"));
					groupList.add(group);

				}

				rs.close();
				stmt.close();
				if (logger.isDebugEnabled()) {
					logger.debug("groupList for the consumerId " + consumerID
							+ " is " + consumerID);
				}
				return groupList;

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return groupList;

	}

	/**
	 * Description of ConfirmStatus()
	 * 
	 * @param ConsumerID
	 *            This is the id for consumer.
	 * @param GroupID
	 *            This is the Groupid for consumer.
	 * @param statusflag
	 *            This is the Statusflag set for that consumerid.
	 * @return Response code to ConfirmMembership web service as Sucess/Failure
	 */
	public int ConfirmStatus(int consumerId, int groupId, int statusFlag) {
		Connection con = null;
		int result = 0;

		try {
			con = ConnectionPool.getConnection();
			ResultSet resultsetConsumerId;
			PreparedStatement cid = con
					.prepareStatement(SqlStatements.CONSUMER_ID_VERIFICATION);
			cid.setInt(1, consumerId);
			cid.setInt(2, groupId);

			resultsetConsumerId = cid.executeQuery();
			if (!resultsetConsumerId.next()) {
				return result = 405;

			}
			resultsetConsumerId.close();
			cid.close();

			/*
			 * ResultSet resultSetGroupId; PreparedStatement gid = con
			 * .prepareStatement(SqlStatements.GROUP_ID_VERIFICATION);
			 * gid.setInt(1, groupId); resultSetGroupId = gid.executeQuery(); if
			 * (!resultSetGroupId.next()) { return result = 406; }
			 * resultSetGroupId.close();
			 */

			PreparedStatement stmt = con
					.prepareStatement(SqlStatements.CONFIRM_STATUS);

			stmt.setInt(1, statusFlag);
			stmt.setInt(2, consumerId);
			stmt.setInt(3, groupId);
			int numberRowsAffected = stmt.executeUpdate();
			stmt.close();
			if (numberRowsAffected == 0) {
				return result = 407;
			}
			if (logger.isDebugEnabled()) {
				logger.debug("groupId: " + groupId
						+ " is updated with the status flag: " + statusFlag
						+ " for the consumerId : " + consumerId);
			}
			return result = 200;

		}

		catch (SQLException e) {
			e.printStackTrace();

		} finally {

			ConnectionPool.returnConnection(con);

		}
		return result;

	}

	/**
	 * Description of createGroupOwner()
	 * 
	 * @param ConsumerID
	 *            This is the id for consumer.
	 * @param offerId
	 *            This is the offerId for consumer.
	 * @param appId
	 *            This is the appId.
	 * @return Response code to createGroup web service as Sucess/Failure
	 */

	public int[] createGroupOwner(int consumerId, String appId,
			List<String> groupMembersPhone, int countryId) {
		int[] responseArray = { 0, 0 };
		Connection con = null;
		CallableStatement cs = null;
		int resultCreateGroupOwner = 0;
		int groupId = 0;

		try {
			con = ConnectionPool.getConnection();
			cs = con.prepareCall("{call db_mras.CreateGroupOwner(?,?,?,?,?)}");
			cs.setInt(1, consumerId);
			cs.setString(2, appId);
			cs.setInt(3, countryId);
			cs.registerOutParameter(4, java.sql.Types.INTEGER);
			cs.registerOutParameter(5, java.sql.Types.INTEGER);
			cs.executeUpdate();
			groupId = cs.getInt(5);
			resultCreateGroupOwner = cs.getInt(4);

			cs.close();
			responseArray[0] = resultCreateGroupOwner;
			responseArray[1] = groupId;

			if (resultCreateGroupOwner == 200) {
				if (logger.isDebugEnabled()) {
					logger.debug("creating groups for groupdId " + groupId
							+ " and phone numbers " + groupMembersPhone);
				}
				createGroupMember(groupId, appId, groupMembersPhone, countryId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}

		return responseArray;
	}

	/**
	 * Description of createGroupMember()
	 * 
	 * @param groupMembersPhone
	 *            This is the List for consumer phone.
	 * @param offerId
	 *            This is the offerId for consumer.
	 * @param appId
	 *            This is the appId.
	 * @return Response code to createGroup web service as Sucess/Failure
	 */
	public void createGroupMember(int groupId, String appid,
			List<String> groupMembersPhone, int countryId) {
		Connection con = null;
		Connection con1 = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		// List<Integer> countryIdsForLegalCheck = null;

		// countryIdsForLegalCheck will be implemented in the next phase,
		// commenting this code for the time being.
		/*
		 * countryIdsForLegalCheck = (List<Integer>)
		 * MrasCache.MCache.recover("PhoneFlagList"); if
		 * (countryIdsForLegalCheck == null) { try {
		 * 
		 * countryIdsForLegalCheck = new ArrayList<Integer>(); con =
		 * ConnectionPool.getConnection(); PreparedStatement preparedStatement =
		 * con .prepareStatement(SqlStatements.COUNTRY_LIST_LEGALCHECK); rs =
		 * preparedStatement.executeQuery(); while (rs.next()) {
		 * countryIdsForLegalCheck.add(rs.getInt("countryid")); } rs.close();
		 * MrasCache.MCache .admit("PhoneFlagList", countryIdsForLegalCheck);
		 * 
		 * } catch (Exception e) { e.printStackTrace(); } finally {
		 * ConnectionPool.returnConnection(con); } }
		 */
		// if (countryIdsForLegalCheck.contains(countryId)) {

		try {
			con = ConnectionPool.getConnection();
			// logger.debug("CountryId " + countryId +
			// " allow  to save phone number");
			stmt = con
					.prepareStatement(SqlStatements.INSERT_GROUP_FOR_MEMBER_PHONE);
			stmt.setInt(1, 0);
			stmt.setString(2, appid);
			stmt.setInt(3, groupId);
			stmt.setInt(5, 1);

			for (String s : groupMembersPhone) {
				stmt.setString(4, s);
				stmt.addBatch();

			}

			stmt.executeBatch();
			stmt.close();

			con1 = ConnectionPool.getConnection();
			stmt1 = con1
					.prepareStatement(SqlStatements.UPDATE_GROUP_FOR_MEMBER_PHONE);
			stmt1.setInt(3, groupId);

			for (String s : groupMembersPhone) {
				stmt1.setString(1, s);
				stmt1.setString(2, s);
				stmt1.addBatch();
			}
			stmt1.executeBatch();

			stmt1.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
			ConnectionPool.returnConnection(con1);
		}

		// }

	}

	/**
	 * Description of groupRewardsIssue()
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @param ConsumerID
	 *            This is the id for consumer who checked in and the group is
	 *            complete.
	 * @return None
	 */
	public List<Integer> getAllOfferIdForCampaign(int campaignID) {
		Connection con = null;
		ResultSet rs;
		List<Integer> offerIdList = new ArrayList<Integer>();

		try {
			// logger.debug("paremeter recevied are campaignID " + campaignID);
			con = ConnectionPool.getConnection();
			PreparedStatement stmt = con
					.prepareStatement(SqlStatements.GETALL_OFFERID_FOR_CAMPAIGNID);
			stmt.setInt(1, campaignID);

			rs = stmt.executeQuery();

			if (!rs.next()) {
				return (offerIdList = null);
			} else {
				rs.previous();

				while (rs.next()) {
					offerIdList.add(rs.getInt("offerId"));

				}
				if (logger.isDebugEnabled()) {
					logger.debug("Result received for the search are"
							+ offerIdList);
				}
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		finally {
			ConnectionPool.returnConnection(con);
		}
		return offerIdList;
	}

	// Check for if any group is complete

	/*
	 * consumer id --- > mrasgroup table complete group ids[] completed group
	 * ids[] offers id[] and offerid status=active HASHMAP(group id, offerid)
	 * 
	 * for till end of hash map{ find the trigger id for every offer id[] if
	 * trigger id=2{ count(distinct groupid)from mrasgroup where offerid= ? and
	 * icomplete=1
	 * 
	 * 
	 * if count>=10{ hasmap --> group[id] pick the win ratio, occurrence for
	 * this check (occurence !=0){ List<Integer> listOfGroupIdTO GIVE REWARD for
	 * loop till win ratio(){
	 * 
	 * 
	 * int x=(math.random*10) while(list.contains){ int x=(math.random*10)
	 * listOfGroupIdTO.add(x)
	 * 
	 * 
	 * }
	 * 
	 * } for each list item{ hashmap[i] call issue voucher stored procedure for
	 * distribution of rewards. }
	 */

	/*
	 * public void groupRewardsIssue() { Connection con = null; ResultSet rs1,
	 * rs3; int counter = 0, groupId, offerId, winratio, occurrence;
	 * List<Integer> arrGroupId = new ArrayList<Integer>(); // int arrGroupId[]
	 * = new int[10]; HashMap<Integer, Integer> groupOfferMap = new
	 * HashMap<Integer, Integer>(); List<Integer> rewardToGiveList = new
	 * ArrayList<Integer>(); try {
	 * 
	 * con = ConnectionPool.getConnection(); // Query to find the all the group
	 * ids a consumer belongs to PreparedStatement stmt1 = con
	 * .prepareStatement(SqlStatements.GET_GROUPID_FOR_COMPLETE_GROUP); //
	 * stmt1.setInt(1, consumerId);
	 * 
	 * rs1 = stmt1.executeQuery(); while (rs1.next()) {
	 * 
	 * groupId = rs1.getInt("groupid"); offerId = rs1.getInt("offerid");
	 * arrGroupId.add(groupId); // arrGroupId[counter] = groupId; counter++;
	 * 
	 * groupOfferMap.put(groupId, offerId); } rs1.close();
	 * if(logger.isDebugEnabled()){ logger.debug("GroupId and OfferId map is :"
	 * + groupOfferMap); } // Put validation for status=active for offer id
	 * 
	 * // Set<Entry<Integer, Integer>> setGroupHashMap = //
	 * groupOfferMap.entrySet(); Iterator iterator =
	 * groupOfferMap.entrySet().iterator(); while (iterator.hasNext()) {
	 * 
	 * @SuppressWarnings("rawtypes") Map.Entry entry = (Map.Entry)
	 * iterator.next(); groupId = (Integer) entry.getKey(); offerId = (Integer)
	 * entry.getValue();
	 * 
	 * // List<Integer> offerIdList = // getAllOfferIdForCampaign(campaignId);
	 * if(logger.isDebugEnabled()){
	 * logger.debug("retreiving frm hash map groupId : " + groupId +
	 * "offer id : " + offerId); }
	 * 
	 * // cacheHashMapForOffer. 0 - trigger ID int triggerId = 0; Integer
	 * offerTriggerId = (Integer) MrasCache.MCache .recover("TriggerId" +
	 * offerId); if(logger.isDebugEnabled()){
	 * logger.debug("retreiving from recover : " + offerTriggerId); }
	 * 
	 * if (offerTriggerId != null) { // check for first location
	 * 
	 * triggerId = offerTriggerId; // triggerId= cacheArr[0];
	 * if(logger.isDebugEnabled()){ logger.debug("found in the cache trigger Id"
	 * + triggerId); }
	 * 
	 * } else { if(logger.isDebugEnabled()){
	 * logger.debug("retreiving frm hash map groupId : " + groupId +
	 * "offerId  : " + offerId); }
	 * 
	 * stmt1 = con .prepareStatement(SqlStatements.GET_TRIGGEID_FOR_OFFER);
	 * stmt1.setInt(1, offerId); rs1 = stmt1.executeQuery(); rs1.next();
	 * triggerId = rs1.getInt("triggerid");
	 * 
	 * rs1.close(); // Trigger Id 2 for Random Win ratio
	 * if(logger.isDebugEnabled()){ logger.debug("trigger id : " + triggerId); }
	 * 
	 * Object o = MrasCache.MCache.admit("TriggerId" + offerId, triggerId);
	 * if(logger.isDebugEnabled()){ logger.debug("Admitin obj " + o); }
	 * 
	 * } // Logic for Random Win Ratio} if (triggerId == 2) { // For getting all
	 * the completed distinct group ids for a // particluar offer
	 * 
	 * if (groupOfferMap.size() >= 10) { // For calculation the win ratio of the
	 * offer PreparedStatement stmt3 = con
	 * .prepareStatement(SqlStatements.GET_WINRATIO_OCCUR_FOR_OFFER);
	 * stmt3.setInt(1, offerId); rs3 = stmt3.executeQuery(); rs3.next();
	 * winratio = rs3.getInt("winratio"); occurrence =
	 * rs3.getInt("remainingoccurrence"); rs3.close();
	 * if(logger.isDebugEnabled()){ logger.debug("win ratio : " + winratio +
	 * "occurrence :" + occurrence); } // decrease the occurrence as well if
	 * (occurrence > 0) { // To generate the random numbers depending upon the
	 * // win ratio int random, groupIdToGive = 0; for (counter = 0; counter <
	 * winratio; counter++) {
	 * 
	 * do { random = (int) (Math.random() * 10); if (random != 0) {
	 * groupIdToGive = arrGroupId .get(random - 1); }
	 * 
	 * } while (rewardToGiveList .contains(groupIdToGive));
	 * rewardToGiveList.add(groupIdToGive); if(logger.isDebugEnabled()){
	 * logger.debug("Random win ration reward to be given to group id " +
	 * groupIdToGive); } } stmt3 = con
	 * .prepareStatement(SqlStatements.UPDATE_OCCURRENCE); stmt3.setInt(1,
	 * offerId); stmt3.executeUpdate(); stmt3.close();
	 * 
	 * } // Make call to issue voucher here for all the ids // } for (Integer
	 * groupIdToGive : rewardToGiveList) {
	 * 
	 * int offerIdToGive = groupOfferMap .get(groupIdToGive);
	 * 
	 * issueVoucherToGroup(groupIdToGive, offerIdToGive); }
	 * 
	 * } } else if (triggerId == 3) { // Trigger Id 3 for Guarantee group Reward
	 * issueVoucherToGroup(offerId, groupId);
	 * 
	 * // Make call to issue voucher here for above groupId
	 * 
	 * }
	 * 
	 * else if (triggerId == 5) { // Trigger Id 5 for Timed group Reward
	 * 
	 * // con = null; int result;
	 * 
	 * CallableStatement stmt = null; try {
	 * 
	 * // con = ConnectionPool.getConnection(); stmt =
	 * con.prepareCall("{ call timedRewardCheck(?,?,?,?)}"); stmt.setInt(1,
	 * offerId); stmt.setInt(2, groupId); // stmt.setInt(3, consumerId);
	 * stmt.registerOutParameter(4, java.sql.Types.INTEGER);
	 * stmt.executeQuery(); result = stmt.getInt(4); stmt.close();
	 * if(logger.isDebugEnabled()){ logger.debug("result for trigger Id =5 :" +
	 * result); } if (result == 1) { if(logger.isDebugEnabled()){
	 * logger.debug("Call issue voucher for group Id" + groupId); }
	 * issueVoucherToGroup(offerId, groupId);
	 * 
	 * }
	 * 
	 * } }
	 * 
	 * } catch (SQLException e) {
	 * 
	 * e.printStackTrace(); } finally {
	 * 
	 * ConnectionPool.returnConnection(con);
	 * 
	 * } }
	 */
	public int addMemberInGroupDAO(int groupId, String PhoneNumber,
			String appId, int consumerId) {
		Connection con = null;
		int result = 0;
		CallableStatement stmt = null;
		try {
			con = ConnectionPool.getConnection();

			stmt = con.prepareCall("{ call addmemberingroup(?,?,?,?,?)}");

			stmt.setString(1, PhoneNumber);
			stmt.setInt(2, groupId);
			stmt.setString(3, appId);
			stmt.setInt(4, consumerId);
			stmt.registerOutParameter(5, java.sql.Types.INTEGER);
			stmt.executeQuery();
			result = stmt.getInt(5);
			stmt.close();
			return result;

		}

		catch (SQLException e) {
			e.printStackTrace();
		} finally {

			ConnectionPool.returnConnection(con);

		}
		return result;

	}

	public int removeMemberFromGroupDAO(int groupId, String PhoneNumber,
			int consumerId) {
		Connection con = null;
		int result = 0;
		CallableStatement stmt = null;
		try {
			con = ConnectionPool.getConnection();
			stmt = con.prepareCall("{ call removeMemberFromGroup(?,?,?,?)}");

			stmt.setString(1, PhoneNumber);
			stmt.setInt(2, groupId);
			stmt.setInt(3, consumerId);
			stmt.registerOutParameter(4, java.sql.Types.INTEGER);
			stmt.executeQuery();

			result = stmt.getInt(4);
			stmt.close();
		}

		catch (SQLException e) {
			e.printStackTrace();

		} finally {

			ConnectionPool.returnConnection(con);

		}
		return result;

	}

	/**
	 * Description of issueVoucherToGroup()
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @param GroupID
	 *            This is the Groupid for consumer.
	 * @param offerId
	 *            This is the offer Id
	 * @return Response code to ConfirmMembership web service as Sucess/Failure
	 */

	public void issueVoucherToGroup(int groupId, int offerId) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		int rs1;
		int consumerId;
		int rewardCode;
		try {
			con = ConnectionPool.getConnection();
			stmt = con
					.prepareStatement(SqlStatements.GETALL_CONSUMERID_FOR_GROUP);
			stmt.setInt(1, groupId);
			rs = stmt.executeQuery();

			stmt1 = con.prepareStatement(SqlStatements.SET_ISCOMPLETE_ZERO);
			stmt1.setInt(1, groupId);
			stmt1.setInt(2, offerId);
			rs1 = stmt1.executeUpdate();
			if (logger.isDebugEnabled()) {
				logger.debug("Group Id to give the voucher :" + groupId);
			}
			while (rs.next()) {
				consumerId = rs.getInt("consumerId");
				rewardCode = SchedulerDAO
						.generateRewardCode(consumerId);
				int responseDao = SchedulerDAO.issueVoucher(
						consumerId, offerId, null, rewardCode);
				if (logger.isDebugEnabled()) {
					logger.debug("Issued Voucher to : " + consumerId
							+ " reward Code : " + rewardCode
							+ "response from dao :" + responseDao);
				}
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {

			ConnectionPool.returnConnection(con);

		}

	}

}
