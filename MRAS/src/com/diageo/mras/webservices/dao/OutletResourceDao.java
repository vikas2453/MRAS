package com.diageo.mras.webservices.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import com.diageo.mras.cache.MrasCache;
import com.diageo.mras.webservices.init.ConnectionPool;
import com.diageo.mras.webservices.modals.Message;
import com.diageo.mras.webservices.modals.Offer;
import com.diageo.mras.webservices.modals.OuletInformation;
import com.diageo.mras.webservices.modals.Outlet;
import com.diageo.mras.webservices.modals.OutletBandDetails;
import com.diageo.mras.webservices.modals.OutletDistance;
import com.diageo.mras.webservices.modals.OutletOfferList;
import com.diageo.mras.webservices.modals.OutletWithoutCampaignId;
import com.diageo.mras.webservices.responses.MessageResponse;
import com.diageo.mras.webservices.responses.OfferOutletTrade;
import com.diageo.mras.webservices.responses.OfferResponse;
import com.diageo.mras.webservices.responses.ResponceSearchOfferOutlet;

/**
 * This is a DAO class for the OutletResource.
 * 
 * @author Infosys Limited
 * @version 1.0
 */
/**
 * 
 * @author Infosys Limited
 */
public class OutletResourceDao {
	private static final Logger logger = Logger
			.getLogger(OutletResourceDao.class.getName());
	private static final RewardResourceDao rewardResourceDao = new RewardResourceDao();
	private static final long timeToLive = 1800000;

	/**
	 * Description of checkIn().
	 * 
	 * @author Infosys Limited
	 * @Version 1.0
	 * @param consumerid
	 *            This is the Id of consumer
	 * @param outletCode
	 *            This is the outlet Id where Consumer has checked in
	 * @param isGroupCheckIn
	 *            This is the flag 0-Single Check In 1-Group Check in
	 * @param latitude
	 *            This is the latitude of the outlet to check in.
	 * @param longitude
	 *            This is the longitude of the outlet to check in.
	 * @return Various Response code to capturedeviceDetails Web service
	 */

	public int checkIn(int consumerId, long outletshiptoCode, String appId,
			double latitude, double longitude) {
		Connection con = null;
		int result = 0;
		CallableStatement stmt = null;
		try {

			// get the connection..
			con = ConnectionPool.getConnection();

			// preapare the callable statement to call the procedure..
			stmt = con.prepareCall("{ call checkinupdated(?,?,?,?,?,?)}");

			// set the parameters..
			stmt.setInt(1, consumerId);
			stmt.setString(2, appId);
			stmt.setLong(3, outletshiptoCode);
			stmt.setDouble(4, latitude);
			stmt.setDouble(5, longitude);
			stmt.registerOutParameter(6, java.sql.Types.INTEGER);

			// execute the procudure..
			stmt.executeQuery();

			// get the result..
			result = stmt.getInt(6);
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// return the connection to the pool..
			ConnectionPool.returnConnection(con);
		}
		return result;

	}

	public OutletOfferList getOutletOfferCampaign(long outletCode,
			int campaignId, String appid) {
		Connection con = null;
		List<OfferResponse> offerResponseList = new ArrayList<OfferResponse>();
		List<Integer> offerIdList = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			

			offerIdList = (ArrayList<Integer>) MrasCache.MCache
					.recover("OutletOffers" + outletCode + campaignId);
			if (offerIdList == null) {

				if (logger.isDebugEnabled()) {
					logger.debug("OutletOffers not found in cache, putting in cache");
				}

				con = ConnectionPool.getConnection();
				if (campaignId == 0) {
					// logger.debug("for campaign is 0 outletCode"+outletCode);
					
					stmt = con
							.prepareStatement(SqlStatements.GET_OFFER_OUTLET_OFFERID);
					stmt.setLong(1, outletCode);
					stmt.setString(2, appid);
					stmt.setLong(3, outletCode);
					stmt.setString(4, appid);
					rs = stmt.executeQuery();
				} else {
					// logger.debug("for campaign is  campaignId"+campaignId+" outletCode"+outletCode);
					
					stmt = con
							.prepareStatement(SqlStatements.GET_OFFER_OUTLET_OFFERID_CAMPAIGN);
					stmt.setLong(1, outletCode);
					stmt.setInt(2, campaignId);
					stmt.setString(3, appid);
					stmt.setLong(4, outletCode);
					stmt.setInt(5, campaignId);
					stmt.setString(6, appid);
					rs = stmt.executeQuery();
				}
				// if not outlet found, return null
				if (!rs.next()) {
					return null;
				} else {
					offerIdList = new ArrayList<Integer>();
					rs.previous();
					int offerId;
					while (rs.next()) {
						offerId= rs.getInt("offerId");
						offerIdList.add(offerId);
					}
					if (!offerIdList.isEmpty()) {
						MrasCache.MCache.admit("OutletOffers" + outletCode
								+ campaignId, offerIdList, timeToLive,
								timeToLive);
					}			

				}
				rs.close();
				stmt.close();
			}

			if (offerIdList.isEmpty()) {
				return null;
				// getOfferOutletdata(offerIdList);
			} else {
				offerResponseList = getOfferOutletdata(offerIdList);
			}

		} catch (SQLException e) {
			// print the stac trace..
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}

		OutletOfferList outletOfferList = new OutletOfferList();
		outletOfferList.setOfferList(offerResponseList);
		return outletOfferList;

	}

	public List<OfferResponse> getOfferOutletdata(List<Integer> offerIdList) {
		List<OfferResponse> offerResponseList = new ArrayList<OfferResponse>();

		for (int offerId : offerIdList) {

			Offer offer = (Offer) MrasCache.MCache.recover("Offer" + offerId);

			if (offer == null) {
				// not found in cache recive from db and update cache
				offer = rewardResourceDao.getOfferWithOfferid(offerId);
				if (offer != null) {
					long timeToLive1 = offer.getRedemptionEndTime().getTimeInMillis()
							- new Date().getTime();
					MrasCache.MCache.admit("Offer" + offerId, offer,
							timeToLive1, timeToLive1);
				}

			}

			if (offer != null) {
				// while (rs.next()) {
				OfferResponse outletOffer = new OfferResponse();
				outletOffer.setCampaignId(offer.getCampaignId());
				outletOffer.setCurrency(offer.getCurrency());
				outletOffer.setEndDate(offer.getEndDate());
				outletOffer.setStartDate(offer.getStartDate());
				outletOffer.setEnglish(offer.getEnglish());
				outletOffer.setOffercheckIn(offer.getOffercheckIn());
				outletOffer.setOfferId(offerId);
				outletOffer.setOfferName(offer.getOfferName());
				outletOffer.setOfferValue(offer.getOfferValue());

				outletOffer.setPromocode(offer.getPromocode());
				outletOffer.setRedemptionLimit(offer.getRedemptionLimit());
				outletOffer.setRedemptionTrackable(offer
						.getRedemptionTrackable());

				outletOffer.setRegional(offer.getRegional());
				outletOffer.setMainImage(offer.getImage());
				outletOffer.setGhostImage(offer.getGhostImage());
				outletOffer.setValidBrands(offer.getValidBrands());
				outletOffer.setRedeemable(offer.isRedeemable());
				offerResponseList.add(outletOffer);
				// }
			}

		}

		return offerResponseList;
	}

	public int voteForOutlet(int consumerId, long outletshiptoCode) {
		Connection con = null;
		int result = 0;
		CallableStatement stmt = null;
		try {
			// get the connection..
			con = ConnectionPool.getConnection();

			// preapare the callable statement to call the procedure..
			stmt = con.prepareCall("{ call VoteForLocalProcedure(?,?,?)}");

			// set the parameters..
			stmt.setInt(2, consumerId);
			stmt.setLong(1, outletshiptoCode);
			stmt.registerOutParameter(3, java.sql.Types.INTEGER);
			stmt.executeQuery();
			result = stmt.getInt(3);
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// return the connection to the pool..
			ConnectionPool.returnConnection(con);
		}
		return result;
	}

	/**
	 * Search Nearby outlets.
	 * 
	 * @param lattitude
	 *            This is the lattitude.
	 * @param longitude
	 *            This is the longitude.
	 * @param radius
	 *            This is the distance.
	 * @param campaignId
	 *            This is the campaignId.
	 * @return Outletlist List of outlets.
	 */
	public List<Outlet> searchForNearbyOutlets(double latitude,
			double longitude, double radius, int campaignId, int offerId,
			boolean ceckinRequired, boolean redemptionRequired , String appId) {
		Connection con = null;
		List<Outlet> outletlist = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("paremeter recevied are Latitude " + latitude
						+ ", Longitude: " + longitude + " ,Radius: " + radius
						+ "");
			}
			con = ConnectionPool.getConnection();
			double deviator = (radius / 69.0);

			// prepare the query to search the nearby outlets..
			
			if(campaignId==0 && offerId==0){
				stmt = con.prepareStatement(SqlStatements.SEARCH_FOR_NEARBY_OUTLETS_WITH_APPID);
		        stmt.setDouble(1, latitude + deviator);
		        stmt.setDouble(2, latitude - deviator);
		        stmt.setDouble(3, longitude + deviator);
		        stmt.setDouble(4, longitude - deviator);
		        stmt.setString(5, appId);
			}	
			else if (campaignId > 0) {
				stmt = con
						.prepareStatement(SqlStatements.SEARCH_FOR_NEARBY_OUTLETS);
				stmt.setDouble(1, latitude + deviator);
				stmt.setDouble(2, latitude - deviator);
				stmt.setDouble(3, longitude + deviator);
				stmt.setDouble(4, longitude - deviator);
				stmt.setInt(5, campaignId);
			} else {
				/*
				 * boolean seperateOutlet=false; seperateOutlet=
				 * checkForSeperateOutlet(offerId);
				 */

				Offer offer = (Offer) MrasCache.MCache.recover("Offer"
						+ offerId);
				if (offer == null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Offer Id :" + offerId
								+ " not found in Cache");
					}
					offer = rewardResourceDao.getOfferWithOfferid(offerId);

					if (offer == null) {
						logger.debug("offerid is not correct");		
						
						return null;
					}else{
						// key for offerId starting from 30000 to get reward
						// object, for getting consumer updates.
						MrasCache.MCache.admit("Offer" + offerId, offer);
						
					}

				}

				if (offer.isSeperateOutlet()) {
					stmt = con
							.prepareStatement(SqlStatements.SEARCH_FOR_NEARBY_OUTLETS_OFFERID);
					stmt.setInt(1, offerId);
					stmt.setInt(2, offerId);
					stmt.setDouble(3, latitude + deviator);
					stmt.setDouble(4, latitude - deviator);
					stmt.setDouble(5, longitude + deviator);
					stmt.setDouble(6, longitude - deviator);
					stmt.setInt(7, offerId);
				} else {
					stmt = con
							.prepareStatement(SqlStatements.SEARCH_FOR_NEARBY_OUTLETS_OFFER);
					stmt.setInt(1, offerId);
					stmt.setInt(2, offerId);
					stmt.setDouble(3, latitude + deviator);
					stmt.setDouble(4, latitude - deviator);
					stmt.setDouble(5, longitude + deviator);
					stmt.setDouble(6, longitude - deviator);
					stmt.setInt(7, offerId);
				}
			}
			// set the parameters..

			// execute the query..
			rs = stmt.executeQuery();

			// if not outlet found, return null
			if (!rs.next()) {
				return null;
			} else {
				// else perpare the list of outlet..
				rs.previous();
				if (campaignId > 0) {
					outletlist = prepareOutletFromResultSetOfferId(rs,ceckinRequired,false);
				} else {

					outletlist = prepareOutletFromResultSetOfferId(rs,
							ceckinRequired, redemptionRequired);
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Result received for the search are"
							+ outletlist);
				}
				rs.close();
				stmt.close();
			}
		} catch (SQLException e) {
			// print the stac trace..
			e.printStackTrace();
		}

		finally {
			// returning the connection to the pool..
			ConnectionPool.returnConnection(con);
		}
		return outletlist;
	}

	/**
	 * Search Outlet without Geolocation.
	 * 
	 * @param searchquery
	 *            This is the searchQuery.
	 * @param countryCode
	 *            This is the countryCode.
	 * @param zipcode
	 *            This is the zipcode.
	 * @param campaignId
	 *            This is the campainId.
	 * @return outletlist List of outlets.
	 */
	/*public List<Outlet> searchwithoutgeolocation(String searchquery,
			String countryCode, int zipcode, int campaignId, String appid) {
		List<Outlet> outletlist = null;
		Connection con = null;
		if (logger.isDebugEnabled()) {
			logger.debug("Searching with searchquery: " + searchquery
					+ ", countrycode: " + countryCode + ", zipcode: " + zipcode);
		}

		try {
			// get the connection..
			con = ConnectionPool.getConnection();

			// check if country code is not null of zipcode is not zero..
			if ((countryCode != null) || (zipcode != 0)) {

				// prepare the query to get the outlet according to the country
				// code and the qipcode..
				PreparedStatement stmt = con
						.prepareStatement(SqlStatements.SEARCH_WITHOUT_GEOLOCATION_WITH_COUNTRYCODE_ZIPCODE);
				stmt.setString(1, countryCode);
				stmt.setInt(2, zipcode);
				stmt.setInt(3, campaignId);
				ResultSet resultset = stmt.executeQuery();

				// if not outlet found, return null..
				if (!resultset.next()) {
					return null;
				} else {
					// else iterate over the outlet and return the list of
					// outlets..
					resultset.previous();
					outletlist = prepareOutletFromResultSet(resultset);
					if (logger.isDebugEnabled()) {
						logger.debug("Outlet list returned is " + outletlist);
					}
					resultset.close();
					stmt.close();
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Searching with the searchquery as country code and zip code are not provided.");
				}
				// prepare the query to fetch the outlet without countrycode and
				// zip code..
				PreparedStatement stmt = con
						.prepareStatement(SqlStatements.SEARCH_WITHOUT_GEOLOCATION);

				// set the parameters..
				stmt.setString(1, searchquery);
				stmt.setInt(2, campaignId);
				stmt.setString(3, appid);

				// execute the query..
				ResultSet resultsetforsearchquery = stmt.executeQuery();

				// if the result set is empty, return null..
				if (!resultsetforsearchquery.next()) {
					return null;
				} else {
					// else return the list of outlets..
					resultsetforsearchquery.previous();
					outletlist = prepareOutletFromResultSet(resultsetforsearchquery);
					if (logger.isDebugEnabled()) {
						logger.debug("Outlet list returned is " + outletlist);
					}
					resultsetforsearchquery.close();
					stmt.close();

				}
			}
		}

		catch (SQLException e) {
			// print the stack trace..
			e.printStackTrace();
		}

		finally {
			// return the connection to the pool..
			ConnectionPool.returnConnection(con);
		}
		return outletlist;

	}*/

	/*public List<Outlet> searchwithoutgeolocation(String searchquery,
			String AppId) {

		List<Outlet> outletlist = null;
		Connection con = null;
		try {
			con = ConnectionPool.getConnection();
			PreparedStatement stmt = con
					.prepareStatement(SqlStatements.SEARCH_WITHOUT_GEOLOCATION_WITHOUT_CAMPAIGNID);

			// set the parameters..
			stmt.setString(1, searchquery);
			stmt.setString(2, AppId);

			// execute the query..
			ResultSet resultsetforsearchquery = stmt.executeQuery();

			// if the result set is empty, return null..
			if (!resultsetforsearchquery.next()) {
				return null;
			} else {
				// else return the list of outlets..
				resultsetforsearchquery.previous();
				outletlist = prepareOutletFromResultSet(resultsetforsearchquery);
				if (logger.isDebugEnabled()) {
					logger.debug("Outlet list returned is " + outletlist);
				}
				resultsetforsearchquery.close();
				stmt.close();

			}
		} catch (SQLException e) {
			// print the stack trace..
			e.printStackTrace();
		}

		finally {
			// return the connection to the pool..
			ConnectionPool.returnConnection(con);
		}
		return outletlist;

	}*/

	public List<Outlet> SearchWithOutletShipTo(long outlet_Ship_To,boolean CheckinRequired) {
		Connection con = null;
		List<Outlet> outletlist = null;
		ResultSet rs = null;
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("paremeter recevied are outlet_Ship_To= "
						+ outlet_Ship_To);
			}
			con = ConnectionPool.getConnection();

			// prepare the query to search the nearby outlets..
			PreparedStatement stmt = con
					.prepareStatement(SqlStatements.SEARCH_WITHOUT_GEOLOCATION_WITH_Outlet_ship_to);

			// set the parameters..
			stmt.setLong(1, outlet_Ship_To);

			// execute the query..
			rs = stmt.executeQuery();

			// if not outlet found, return null
			if (!rs.next()) {
				return null;
			} else {
				// else perpare the list of outlet..
				rs.previous();

				//if (offerId == 0) {
					outletlist = prepareOutletFromResultSet(rs,CheckinRequired);
				/*} else {
					
					 * boolean seperateOutlet=false; seperateOutlet=
					 * checkForSeperateOutlet(offerId);
					 
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
						}

					}
					if (offer.isSeperateOutlet()) {
						outletlist = prepareOutletFromResultSetOfferId(rs,
								ceckinRequired, redemptionRequired);
					} else {
						outletlist = prepareOutletFromResultSet(rs);
					}
				}//
*/
				if (logger.isDebugEnabled()) {
					logger.debug("Result received for the search are"
							+ outletlist);
				}
				rs.close();
				stmt.close();
			}
		} catch (SQLException e) {
			// print the stac trace..
			e.printStackTrace();
		}

		finally {
			// returning the connection to the pool..
			ConnectionPool.returnConnection(con);
		}
		return outletlist;

	}

	/*public List<Outlet> SearchOutletWithAppId(String appId) {
		Connection con = null;
		List<Outlet> outletlist = null;
		ResultSet rs = null;
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("paremeter recevied are appId= " + appId);
			}
			con = ConnectionPool.getConnection();

			// prepare the query to search the nearby outlets..
			PreparedStatement stmt = con
					.prepareStatement(SqlStatements.SEARCH_WITHOUT_GEOLOCATION_WITH_AppId);

			// set the parameters..
			stmt.setString(1, appId);

			// execute the query..
			rs = stmt.executeQuery();

			// if not outlet found, return null
			if (!rs.next()) {
				return null;
			} else {
				// else perpare the list of outlet..
				rs.previous();
				outletlist = prepareOutletFromResultSet(rs);
				if (logger.isDebugEnabled()) {
					logger.debug("Result received for the search are"
							+ outletlist);
				}
				rs.close();
				stmt.close();
			}
		} catch (SQLException e) {
			// print the stac trace..
			e.printStackTrace();
		}

		finally {
			// returning the connection to the pool..
			ConnectionPool.returnConnection(con);
		}
		return outletlist;

	}*/

	/*public List<Outlet> SearchOutletWithAppIdCampaignID(int campaignid) {
		Connection con = null;
		List<Outlet> outletlist = null;
		ResultSet rs = null;
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("paremeter recevied are campaignid= " + campaignid);
			}
			con = ConnectionPool.getConnection();

			// prepare the query to search the nearby outlets..
			PreparedStatement stmt = con
					.prepareStatement(SqlStatements.SEARCH_WITHOUT_GEOLOCATION_WITH_AppId_Campaign_ID);

			// set the parameters..
			stmt.setInt(1, campaignid);

			// execute the query..
			rs = stmt.executeQuery();

			// if not outlet found, return null
			if (!rs.next()) {
				return null;
			} else {
				// else perpare the list of outlet..
				rs.previous();
				outletlist = prepareOutletFromResultSet(rs);
				if (logger.isDebugEnabled()) {
					logger.debug("Result received for the search are"
							+ outletlist);
				}
				rs.close();
				stmt.close();
			}
		} catch (SQLException e) {
			// print the stac trace..
			e.printStackTrace();
		}

		finally {
			// returning the connection to the pool..
			ConnectionPool.returnConnection(con);
		}
		return outletlist;

	}*/

	/*public List<OutletWithoutCampaignId> SearchWithCountryCodeZipCodeWithoutCampaignId(
			int ZipCode, String CountryCode) {
		Connection con = null;
		List<OutletWithoutCampaignId> outletlist = null;
		ResultSet rs = null;
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("paremeter recevied are CountryCode= "
						+ CountryCode);
			}
			con = ConnectionPool.getConnection();

			// prepare the query to search the nearby outlets..
			PreparedStatement stmt = con
					.prepareStatement(SqlStatements.SEARCH_WITHOUT_GEOLOCATION_WITH_COUNTRYCODE_ZIPCODE_WITHOUT_CAMPAIGNID);

			// set the parameters..
			stmt.setString(1, CountryCode);
			stmt.setInt(2, ZipCode);

			// execute the query..
			rs = stmt.executeQuery();

			// if not outlet found, return null
			if (!rs.next()) {
				return null;
			} else {
				// else perpare the list of outlet..
				rs.previous();
				outletlist = prepareOutlet_Without_CampaignID(rs);
				if (logger.isDebugEnabled()) {
					logger.debug("Result received for the search are"
							+ outletlist);
				}
				rs.close();
				stmt.close();
			}
		} catch (SQLException e) {
			// print the stac trace..
			e.printStackTrace();
		}

		finally {
			// returning the connection to the pool..
			ConnectionPool.returnConnection(con);
		}
		return outletlist;

	}*/

	// All validation come true here

	public List<OutletWithoutCampaignId> SearchWithOutGeolocationWithoutCampaignId(
			String querry,boolean checkinRequired) {
		Connection con = null;
		List<OutletWithoutCampaignId> outletlist = null;
		ResultSet rs = null;
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("paremeter recevied are querry= " + querry);
			}
			con = ConnectionPool.getConnection();
			PreparedStatement stmt = con.prepareStatement(querry);

			// execute the query..
			rs = stmt.executeQuery();

			// logger.debug("After Execution");
			// if not outlet found, return null
			if (!rs.next()) {
				logger.info("rs is  null ");
				return null;
			} else {
				// logger.debug("rs is not null ");
				// else perpare the list of outlet..
				rs.previous();
				outletlist = prepareOutlet_Without_CampaignID(rs,checkinRequired);
				if (logger.isDebugEnabled()) {
					logger.debug("Result received for the search are"
							+ outletlist);
				}
				rs.close();
				stmt.close();
			}
		} catch (SQLException e) {
			// print the stac trace..
			e.printStackTrace();
		}

		finally {
			// returning the connection to the pool..
			ConnectionPool.returnConnection(con);
		}
		return outletlist;
	}

	public List<Outlet> SearchWithOutGeolocationWithCampaignId(String querry,boolean checkinRequired) {
		Connection con = null;
		List<Outlet> outletlist = null;
		ResultSet rs = null;
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("paremeter recevied are querry= " + querry);
			}
			con = ConnectionPool.getConnection();
			PreparedStatement stmt = con.prepareStatement(querry);

			// execute the query..
			rs = stmt.executeQuery();
			// logger.debug("After Execution ");

			// if not outlet found, return null
			if (!rs.next()) {
				logger.info("rs is null ");
				return null;

			} else {
				// else perpare the list of outlet..
				// logger.debug("rs is not null ");
				rs.previous();
				outletlist = prepareOutletFromResultSetOfferId(rs, checkinRequired, false);
				if (logger.isDebugEnabled()) {
					logger.debug("Result received for the search are"
							+ outletlist);
				}
				rs.close();
				stmt.close();
			}
		} catch (SQLException e) {
			// print the stac trace..
			e.printStackTrace();
		}

		finally {
			// returning the connection to the pool..
			ConnectionPool.returnConnection(con);
		}
		return outletlist;
	}

	/**
	 * Prepare outlet From ResultSet.
	 * 
	 * @param rs
	 *            ResultSet.
	 * @return List of Outlets.
	 * @throws SQLException
	 *             SQLException
	 */
	private List<Outlet> prepareOutletFromResultSet(ResultSet rs,boolean CheckinRequired)
			throws SQLException {
		List<Outlet> outletlist = new ArrayList<Outlet>();

		// iterate over the result set..
		while (rs.next()) {

			// create a new outlet instance and set required parameters..
			Outlet outletobj = new Outlet();
			outletobj.setOutletname(rs.getString("OutletName"));
			outletobj.setOutletid(rs.getInt("OutletId"));
			outletobj.setOutlet_Ship_To(rs.getLong("Outlet_ShipTo"));
			outletobj.setAddress(rs.getString("Address"));
			outletobj.setTown(rs.getString("town"));
			outletobj.setCountryCode(rs.getInt("CountryId"));
			outletobj.setCountry(rs.getString("County"));
			outletobj.setLatitude(rs.getDouble("Lattitude"));
			outletobj.setLongitude(rs.getDouble("Longitude"));
			outletobj.setChannel(rs.getString("Channel"));
			outletobj.setPhoneNumber(rs.getString("Phone"));
			outletobj.setZipCode(rs.getString("ZipCode"));
			//Long outletShipto = rs.getLong("Outlet_ShipTo");
			outletobj.setCheckins(rs.getInt("checkins"));
			//outletobj.setGroups(getGroupListFromOutletCode(outletShipto));
			// add outlet to the list..
			outletlist.add(outletobj);

		}
		return outletlist;
	}

	private List<Outlet> prepareOutletFromResultSetOfferId(ResultSet rs,
			boolean ceckinRequired, boolean redemptionRequired)
			throws SQLException {
		List<Outlet> outletlist = new ArrayList<Outlet>();

		// iterate over the result set..
		while (rs.next()) {

			// create a new outlet instance and set required parameters..
			Outlet outletobj = new Outlet();
			outletobj.setOutletname(rs.getString("OutletName"));
			outletobj.setOutletid(rs.getInt("OutletId"));
			outletobj.setOutlet_Ship_To(rs.getLong("Outlet_ShipTo"));
			outletobj.setAddress(rs.getString("Address"));
			outletobj.setTown(rs.getString("town"));
			outletobj.setCountryCode(rs.getInt("CountryId"));
			outletobj.setCountry(rs.getString("County"));
			outletobj.setLatitude(rs.getDouble("Lattitude"));
			outletobj.setLongitude(rs.getDouble("Longitude"));
			outletobj.setChannel(rs.getString("Channel"));
			outletobj.setPhoneNumber(rs.getString("Phone"));
			outletobj.setZipCode(rs.getString("ZipCode"));
			Long outletShipto = rs.getLong("Outlet_ShipTo");
			
			//outletobj.setGroups(getGroupListFromOutletCode(outletShipto));
			if (ceckinRequired) {
				outletobj.setCheckins(rs.getInt("checkins"));
			}
			if (redemptionRequired) {
				outletobj.setRedemptionTillNow(rs.getInt("redemptionTillNow"));
				outletobj.setMaxredemption(rs.getInt("maxredemption"));
			}

			// add outlet to the list..
			outletlist.add(outletobj);

		}
		return outletlist;
	}

	private List<String> getGroupListFromOutletCode(Long outletShipto) {
		List<String> listgroup = new ArrayList<String>();
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			con = ConnectionPool.getConnection();
			stmt = con.prepareStatement(SqlStatements.GROUPNAME_FROM_OUTLETID);
			stmt.setLong(1, outletShipto);
			rs = stmt.executeQuery();

			// if not outlet found, return null
			if (!rs.next()) {
				return null;
			} else {
				rs.previous();
				while (rs.next()) {
					String groupName = rs.getString("groupname");
					listgroup.add(groupName);
				}
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// print the stac trace..
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		return listgroup;
	}

	private List<OutletWithoutCampaignId> prepareOutlet_Without_CampaignID(
			ResultSet rs,boolean checkinRequired) throws SQLException {
		List<OutletWithoutCampaignId> outletlist = new ArrayList<OutletWithoutCampaignId>();

		// iterate over the result set..
		while (rs.next()) {

			// create a new outlet instance and set required parameters..
			OutletWithoutCampaignId outletobj = new OutletWithoutCampaignId();

			outletobj.setOutletname(rs.getString("OutletName"));
			outletobj.setOutletid(rs.getInt("OutletId"));
			outletobj.setOutlet_Ship_To(rs.getLong("Outlet_ShipTo"));
			outletobj.setAddress(rs.getString("Address"));
			outletobj.setTown(rs.getString("town"));
			outletobj.setCountryid(rs.getInt("CountryId"));
			outletobj.setCountry(rs.getString("County"));
			outletobj.setLatitude(rs.getDouble("Lattitude"));
			outletobj.setLongitude(rs.getDouble("Longitude"));
			outletobj.setChannel(rs.getString("Channel"));
			outletobj.setPhoneNumber(rs.getString("Phone"));
			outletobj.setZipCode(rs.getString("ZipCode"));
			//Long outletShipto = rs.getLong("Outlet_ShipTo");
			//outletobj.setGroups(getGroupListFromOutletCode(outletShipto));
			outletobj.setCheckins(rs.getInt("checkins"));
			// add outlet to the list..
			outletlist.add(outletobj);

		}
		return outletlist;
	}

	public ArrayList<Integer> checkVoucherIssueCount(int offerId) {
		Connection con = null;
		ArrayList<Integer> result = new ArrayList<Integer>();
		CallableStatement stmt = null;
		try {
			// get the connection..
			con = ConnectionPool.getConnection();

			// preapare the callable statement to call the procedure..
			stmt = con.prepareCall("{ call checkVoucherIssueCount(?,?,?,?)}");

			// set the parameters..
			stmt.setLong(1, offerId);
			stmt.registerOutParameter(2, java.sql.Types.INTEGER);
			stmt.registerOutParameter(3, java.sql.Types.INTEGER);
			stmt.registerOutParameter(4, java.sql.Types.INTEGER);
			stmt.executeQuery();

			if (stmt.getInt(2) == 403) {
				result.add(403);
			} else {
				result.add(200);
				result.add(stmt.getInt(3));
				result.add(stmt.getInt(4));
			}
			// result = stmt.getInt(3);
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// return the connection to the pool..
			ConnectionPool.returnConnection(con);
		}
		return result;

	}

	public List<Outlet> searchWithOutGeolocationWithOfferId(String querry,
			int offerId, boolean checkinRequired, boolean redemptionRequired) {

		Connection con = null;
		List<Outlet> outletlist = null;
		ResultSet rs = null;
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("paremeter recevied are querry= " + querry);
			}
			con = ConnectionPool.getConnection();
			PreparedStatement stmt = con.prepareStatement(querry);
			stmt.setInt(1, offerId);
			stmt.setInt(2, offerId);

			// execute the query..
			rs = stmt.executeQuery();
			// logger.debug("After Execution ");

			// if not outlet found, return null
			if (!rs.next()) {
				// logger.debug("rs is null ");
				return null;

			} else {
				// / else perpare the list of outlet..
				// logger.debug("rs is not null ");
				rs.previous();
				outletlist = prepareOutletFromResultSetOfferId(rs,
						checkinRequired, redemptionRequired);
				if (logger.isDebugEnabled()) {
					logger.debug("Result received for the search are"
							+ outletlist);
				}
				rs.close();
				stmt.close();
			}
		} catch (SQLException e) {
			// print the stac trace..
			e.printStackTrace();
		}

		finally {
			// returning the connection to the pool..
			ConnectionPool.returnConnection(con);
		}
		return outletlist;
	}

	public OuletInformation getOutletDetailsDao(long outletCode) {

		Connection con = null;
		OuletInformation outletInformation = null;
		ResultSet rs, rs1 = null;
		try {

			con = ConnectionPool.getConnection();

			// prepare the query to get the information of outlets..
			PreparedStatement stmt = con
					.prepareStatement(SqlStatements.GET_INFORMATION_ABOUT_OUTLET_PUB);

			// set the parameters..
			stmt.setLong(1, outletCode);

			// execute the query..
			rs = stmt.executeQuery();
			outletInformation = new OuletInformation();
			ArrayList<OutletBandDetails> listOutletBandDetails = new ArrayList<OutletBandDetails>();

			while (rs.next()) {
				logger.debug("In while loop");
				OutletBandDetails outletBandDetails = new OutletBandDetails();
				outletBandDetails.setBandNumber(rs.getInt("band_number"));
				outletBandDetails.setPubBandAbout(rs
						.getString("pub_band_about"));
				outletBandDetails.setPubBandFaceBook(rs
						.getString("pub_band_facebook"));
				outletBandDetails.setPubBandName(rs.getString("pub_band_name"));
				outletBandDetails.setPubBandPhoto(rs
						.getString("pub_band_photo"));
				outletBandDetails.setPubBandTime(rs.getString("pub_band_time"));
				outletBandDetails.setPubBandWebsite(rs
						.getString("pub_band_website"));
				listOutletBandDetails.add(outletBandDetails);
			}

			PreparedStatement stmt1 = con
					.prepareStatement(SqlStatements.GET_INFORMATION_ABOUT_OUTLET);
			// set the parameters..
			stmt1.setLong(1, outletCode);
			// execute the query..
			rs1 = stmt1.executeQuery();
			
			if(!rs1.next()){
				return null;
			}else{
				rs1.previous();
			}
			
			while (rs1.next()) {

				outletInformation.setAddress(rs1.getString("Address"));
				outletInformation.setChannel(rs1.getString("Channel"));
				outletInformation.setCountryId(rs1.getInt("CountryId"));
				outletInformation.setCounty(rs1.getString("County"));
				outletInformation.setDoingMusic(rs1.getString("DoingMusic"));
				outletInformation.setEmail(rs1.getString("Email"));
				outletInformation.setFaceBookURL(rs1.getString("FaceBookURL"));
				outletInformation.setLattitude(rs1.getDouble("Lattitude"));
				outletInformation.setLongitude(rs1.getDouble("Longitude"));
				outletInformation.setList(listOutletBandDetails);
				/*logger.debug("list size" + listOutletBandDetails.size()
						+ "list complete" + listOutletBandDetails);
				logger.debug("list size eliment "
						+ listOutletBandDetails.toString());*/

				outletInformation.setOutletName(rs1.getString("outletname"));
				outletInformation.setPhone(rs1.getString("Phone"));
				outletInformation.setPhotoURL(rs1.getString("PhotoURL"));
				outletInformation.setPubRegion(rs1.getString("PubRegion"));
				outletInformation.setSales_Org(rs1.getString("Sales_Org"));
				outletInformation.setPlant(rs1.getString("Plant"));
				outletInformation.setTown(rs1.getString("town"));
				outletInformation.setTwitterURL(rs1.getString("TwitterURL"));
				outletInformation.setWebsite(rs1.getString("Website"));
				outletInformation.setZipCode(rs1.getString("ZipCode"));

			}
			rs.close();
			stmt.close();
			rs1.close();
			stmt1.close();
		} catch (SQLException e) {
			// print the stac trace..
			e.printStackTrace();
		}

		finally {
			// returning the connection to the pool..
			ConnectionPool.returnConnection(con);
		}
		return outletInformation;
	}

	public MessageResponse getConsumerMessageList(int consumerId,
			java.util.Date lastRefreshm, String appId) {

		List<Message> listOfMessage = new RewardResourceDao()
				.getActiveMessages(consumerId, lastRefreshm, appId);
		if (listOfMessage == null || listOfMessage.size()<1) {
		/*	listOfMessage = new ArrayList<Message>();
			Message message = new Message();
			message.setOfferId(null);
		*/	//listOfMessage.add(message);
		}
		MessageResponse messageResponse = new MessageResponse();
		messageResponse.setMessagelist(listOfMessage);
		return messageResponse;

	}

	public boolean validateConsumerId(int consumerId) {
		boolean flagConsumerId = false;
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			con = ConnectionPool.getConnection();
			stmt = con.prepareStatement(SqlStatements.SELECT_CONSUMERID);
			stmt.setInt(1, consumerId);
			rs = stmt.executeQuery();

			// if not outlet found, return null

			while (rs.next()) {
				flagConsumerId = true;
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// print the stac trace..
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}

		return flagConsumerId;
	}

	public List<OutletWithoutCampaignId>  getOfferOutletsDao(int offerId, String appId,int outletFlag) {	
		
		Connection con = null;
		List<OutletWithoutCampaignId> outletlist = null;
		PreparedStatement stmt=null;
		ResultSet rs = null;
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("paremeter recevied are offerId= "
						+ offerId);
			}
			con = ConnectionPool.getConnection();

			// prepare the query to search the nearby outlets..
			if(outletFlag==1){
			    stmt = con
					.prepareStatement(SqlStatements.GET_OUTLET_FROM_OFFER_CAMPAIGN);
			}else{
				 stmt = con
					.prepareStatement(SqlStatements.GET_OUTLET_FROM_OFFER_OFFER);
			}
			// set the parameters..
			stmt.setInt(1, offerId);
			
			// execute the query..
			rs = stmt.executeQuery();

			// if not outlet found, return null
			if (!rs.next()) {
				return null;
			} else {
				// else perpare the list of outlet..
				rs.previous();
				outletlist = prepareOutlet_Without_CampaignID(rs,false);
				if (logger.isDebugEnabled()) {
					logger.debug("Result received for the search are"
							+ outletlist);
				}
				rs.close();
				stmt.close();
			}
		} catch (SQLException e) {
			// print the stac trace..
			e.printStackTrace();
		}

		finally {
			// returning the connection to the pool..
			ConnectionPool.returnConnection(con);
		}
		return outletlist;
	}

	public ResponceSearchOfferOutlet searchOfferOutletDao(double latitude,	double longitude, double radius,
			String appId, int campaignId,int numberOfOutlet) {
		ResponceSearchOfferOutlet responceSearchOfferOutlet=null;
		
		
		/*List<OfferResponse> onTradeOffer=null;
		List<OfferResponse> offTradeOffer=null;
		List<OutletDistance> offTradeOutlet=null;
		List<OutletDistance> onTradeOutlet=null;	*/	
	
		OfferOutletTrade offerOutletTradeForOnTrade=
			getOfferDetailForTrade(campaignId,appId,1, latitude,longitude, radius,numberOfOutlet);
		
		OfferOutletTrade offerOutletTradeForOffTrade=
			getOfferDetailForTrade(campaignId,appId,2,latitude,longitude, radius,numberOfOutlet);
		
		if(offerOutletTradeForOnTrade==null &&  offerOutletTradeForOffTrade==null){
			return responceSearchOfferOutlet;
		}
		
		responceSearchOfferOutlet=new ResponceSearchOfferOutlet();
		
		if(offerOutletTradeForOnTrade!=null){
			responceSearchOfferOutlet.setOnTrade(offerOutletTradeForOnTrade.getOfferList());			
			responceSearchOfferOutlet.setOnTradeOutlet(offerOutletTradeForOnTrade.getOutletList());
		}
		if(offerOutletTradeForOffTrade!=null){
			responceSearchOfferOutlet.setOffTrade(offerOutletTradeForOffTrade.getOfferList());
			responceSearchOfferOutlet.setOffTradeOutlet(offerOutletTradeForOffTrade.getOutletList());
		}
			
		/*if( (onTradeOffer==null || onTradeOffer.isEmpty())  && offTradeOffer==null ){
			logger.debug("both null");
			return null;
		}
		
		//logger.debug("offer Name for on trade:-"+onTradeOffer.get(0).getOfferName());
		//logger.debug("offer Name:-"+offTradeOffer.get(0).getOfferName());
		
		offTradeOutlet=getOutletAccordingToDistance( latitude,longitude, radius,offTradeOffer,numberOfOutlet);
		onTradeOutlet=getOutletAccordingToDistance( latitude,longitude, radius,onTradeOffer,numberOfOutlet);
		
		if(offTradeOutlet==null && onTradeOutlet==null ){
			return null;
		}
		
		//logger.debug("outlet Name for on trade:-"+offTradeOutlet.get(0).getOutletname());
		//logger.debug("outlet Name:-"+onTradeOutlet.get(0).getOutletname());
		
		
		//try {
			List<String> listOfonTradeOffer=new ArrayList<String>();
			if(onTradeOffer!=null){
			    for(OfferResponse offerResponse:onTradeOffer){
				    listOfonTradeOffer.add(offerResponse.getString());
			    }
			}
			
			List<String> listOfoffTradeOffer=new ArrayList<String>();
			if(offTradeOffer!=null){
			  for(OfferResponse offerResponse:offTradeOffer){
				  listOfoffTradeOffer.add(offerResponse.getString());
			  }
			}*/
	
		
	
		
		return responceSearchOfferOutlet;
		//return jsonArray;
	}

	private List<OutletDistance> getOutletAccordingToDistance(double latitude, double longitude, 
			double radius,List<OfferResponse> TradeOffer, int numberOfOutlet) {		
				
		if(TradeOffer==null){
			logger.debug("list is null :-");
			return null;
		}
		List<OutletDistance> listOutletWithoutCampaignId=new ArrayList<OutletDistance>();
		List<OutletDistance> tempListOffer1=new ArrayList<OutletDistance>();
		List<OutletDistance> tempListOffer2=new ArrayList<OutletDistance>();
		boolean changeFlag=true;
		if(numberOfOutlet==0){
			numberOfOutlet=2;
		}
				
        for(OfferResponse offerResponseon:TradeOffer){
			
			int offerId=offerResponseon.getOfferId();
			logger.debug("offerId is "+offerId);
			logger.debug("offerId is "+offerResponseon.getOfferName());
			Offer offer = (Offer) MrasCache.MCache.recover("Offer" + offerId);
			if (offer == null) {
				// not found in cache recive from db and update cache
				offer = rewardResourceDao.getOfferWithOfferid(offerId);
				if (offer == null) {
					logger.debug("offer cache is null is null :-");
				}else{
					long timeToLive1 = offer.getRedemptionEndTime().getTimeInMillis()
					- new Date().getTime();
		        	MrasCache.MCache.admit("Offer" + offerId, offer, timeToLive1,
					timeToLive1);
				}
			}
			if(offerId!=0){
				numberOfOutlet=numberOfOutlet/2;
				numberOfOutlet++;
				logger.debug("offerId is not 0 :-"+offerId);
				Connection con = null;
				PreparedStatement stmt=null;
				ResultSet rs = null;
				
				try {
				
					con = ConnectionPool.getConnection();
					if(offer.isSeperateOutlet()){
					    stmt = con
							.prepareStatement(SqlStatements.GET_OUTLET_ONTRADE_OFFER);
					}else{
						 stmt = con
							.prepareStatement(SqlStatements.GET_OUTLET_ONTRADE);
					}
					// set the parameters..
					stmt.setDouble(1, latitude);
					stmt.setDouble(2, longitude);
					stmt.setInt(3, offerId);
					stmt.setDouble(4, radius);
					stmt.setInt(5, numberOfOutlet);
					
					// execute the query..
					rs = stmt.executeQuery();

					logger.debug("after execution resultSet");
					// if not outlet found, return null
					if (!rs.next()) {
						logger.debug("null resultSet");
						return null;
					} else {
						// else perpare the list of outlet..
						rs.previous();
						while(rs.next()){
							logger.debug("null not resultSet");
							OutletDistance outletDistance=new OutletDistance();
							outletDistance.setAddress(rs.getString("Address"));
							outletDistance.setChannel(rs.getString("Channel"));
							outletDistance.setCountry(rs.getString("County"));
							outletDistance.setCountryCode(rs.getInt("CountryId"));
							outletDistance.setDistance(rs.getDouble("distance"));
							outletDistance.setLatitude(rs.getDouble("Lattitude"));
							outletDistance.setLongitude(rs.getDouble("Longitude"));
							outletDistance.setOfferId(offerId);
							outletDistance.setOutlet_Ship_To(rs.getLong("Outlet_ShipTo"));
							outletDistance.setOutletCode(rs.getLong("Outlet_ShipTo"));
							outletDistance.setOutletid(rs.getInt("outletid"));
							outletDistance.setOutletname(rs.getString("outletname"));
							outletDistance.setPhoneNumber(rs.getString("Phone"));
							outletDistance.setTown(rs.getString("town"));
							outletDistance.setZipCode(rs.getString("ZipCode"));
							
							//logger.debug("name"+rs.getString("outletname")+" "+outletDistance.getOutletname());
							
							if(changeFlag){
								tempListOffer1.add(outletDistance);
							}else{
								tempListOffer2.add(outletDistance);
							}
							
						}	
						
						changeFlag=!changeFlag;
						rs.close();
						stmt.close();
					}
				} catch (SQLException e) {
					// print the stac trace..
					e.printStackTrace();
				}

				finally {
					// returning the connection to the pool..
					ConnectionPool.returnConnection(con);
				}
				
			}
		}
		if(tempListOffer1.size()>1 && tempListOffer2.size()>1){
			
			//logger.debug("size > 1");
          long outletId1= tempListOffer1.get(0).getOutlet_Ship_To();
          long outletId2= tempListOffer2.get(0).getOutlet_Ship_To();
          
            if(outletId1==outletId2){
            	
            	 double distance1=tempListOffer1.get(1).getDistance();
                 double distance2=tempListOffer2.get(1).getDistance();
                 if(distance1>distance2){
                	 listOutletWithoutCampaignId.add(tempListOffer1.get(0));
                	 listOutletWithoutCampaignId.add(tempListOffer2.get(1));
                 }else{
                	 listOutletWithoutCampaignId.add(tempListOffer2.get(0));
                	 listOutletWithoutCampaignId.add(tempListOffer1.get(1));
                 }
                 
            }else{
            	listOutletWithoutCampaignId.add(tempListOffer1.get(0));
            	listOutletWithoutCampaignId.add(tempListOffer2.get(0));
            }          
		}else{
			if(tempListOffer1.size()>0){
				listOutletWithoutCampaignId.add(tempListOffer1.get(0));
			}
            if(tempListOffer2.size()>0){
            	listOutletWithoutCampaignId.add(tempListOffer2.get(0));
			}
		}
	//	logger.debug("size of "+listOutletWithoutCampaignId.size());
		return listOutletWithoutCampaignId;
	}

	private OfferOutletTrade getOfferDetailForTrade(int campaignId,String appId,int trade,double latitude, double longitude, 
			double radius, int numberOfOutlet) {
		Connection con = null;
		PreparedStatement stmt=null;
		ResultSet rs = null;
		List<OfferResponse> listOfOfferResponse=null;
		List<OutletDistance> listOfOutletDistance=null;
		OfferOutletTrade offerOutletTrade=null;
		try {
			con = ConnectionPool.getConnection();
			stmt = con.prepareStatement(SqlStatements.GET_OFFER_ONTRADE_ON);
			stmt.setDouble(1, latitude);
			stmt.setDouble(2, longitude);
			stmt.setString(3, appId);			
			stmt.setInt(4, campaignId);			
			if(trade==1){
				stmt.setString(5, "ON");
				//System.out.println("calling on");
			}else{
				stmt.setString(5, "OFF");	
				//System.out.println("calling off");
			}
			stmt.setDouble(6, radius);			
			rs=stmt.executeQuery();
			
			if (!rs.next()) {
				logger.debug("getOfferDetailForTrade resultset is null");
				return null;
			} else {
				rs.previous();	
				listOfOfferResponse=new ArrayList<OfferResponse>();
				listOfOutletDistance=new ArrayList<OutletDistance>();
				int preOfferId=0,preOutletId=0;
				offerOutletTrade=new OfferOutletTrade();
				while(rs.next()){
					//rs.isLast();
					
					int offerId=rs.getInt("offerid");
					int outletId=rs.getInt("outletid");
					//System.out.println("offerid"+offerId+"-"+outletId);
					if(offerId!=preOfferId && outletId!=preOutletId){
						preOfferId=offerId;
						preOutletId=outletId;
						OfferResponse objOffer=getOfferObjResponse( offerId, trade);
						
												
						OutletDistance outletDistance=new OutletDistance();
						outletDistance.setAddress(rs.getString("Address"));
						outletDistance.setChannel(rs.getString("Channel"));
						outletDistance.setCountry(rs.getString("County"));
						outletDistance.setCountryCode(rs.getInt("CountryId"));
						outletDistance.setDistance(rs.getDouble("distance"));
						outletDistance.setLatitude(rs.getDouble("Lattitude"));
						outletDistance.setLongitude(rs.getDouble("Longitude"));
						outletDistance.setOfferId(offerId);
						outletDistance.setOutlet_Ship_To(rs.getLong("Outlet_ShipTo"));
						outletDistance.setOutletCode(rs.getLong("Outlet_ShipTo"));
						outletDistance.setOutletid(outletId);
						outletDistance.setOutletname(rs.getString("outletname"));
						outletDistance.setPhoneNumber(rs.getString("Phone"));
						outletDistance.setTown(rs.getString("town"));
						outletDistance.setZipCode(rs.getString("ZipCode"));
						
						listOfOutletDistance.add(outletDistance);
						listOfOfferResponse.add(objOffer);						
					}
					
					if(rs.isLast() && listOfOfferResponse.size()==1){
						
								
						/*if(outletId!=preOutletId){							
							OutletDistance outletDistance=new OutletDistance();
							outletDistance.setAddress(rs.getString("Address"));
							outletDistance.setChannel(rs.getString("Channel"));
							outletDistance.setCountry(rs.getString("County"));
							outletDistance.setCountryCode(rs.getInt("CountryId"));
							outletDistance.setDistance(rs.getDouble("distance"));
							outletDistance.setLatitude(rs.getDouble("Lattitude"));
							outletDistance.setLongitude(rs.getDouble("Longitude"));
							outletDistance.setOfferId(offerId);
							outletDistance.setOutlet_Ship_To(rs.getLong("Outlet_ShipTo"));
							outletDistance.setOutletCode(rs.getLong("Outlet_ShipTo"));
							outletDistance.setOutletid(outletId);
							outletDistance.setOutletname(rs.getString("outletname"));
							outletDistance.setPhoneNumber(rs.getString("Phone"));
							outletDistance.setTown(rs.getString("town"));
							outletDistance.setZipCode(rs.getString("ZipCode"));
							System.out.println("rs.isLast()"+offerId+"-"+outletId);
							listOfOutletDistance.add(outletDistance);
						}*/
						if(offerId!=preOfferId){
							OfferResponse objOffer=getOfferObjResponse( offerId, trade);
							listOfOfferResponse.add(objOffer);	
							
						}
						
					}
					
					if(listOfOfferResponse.size()>1){
						break;
					}
					
					
			}
				
				rs.close();
				stmt.close();
				offerOutletTrade.setOfferList(listOfOfferResponse);
				offerOutletTrade.setOutletList(listOfOutletDistance);
				
			}
			
		} catch (SQLException e) {
			logger.info("Error in function getOfferDetailForTrade class OutletResourceDuo"+e.getMessage());
			e.printStackTrace();
		}finally {
			// returning the connection to the pool..
			ConnectionPool.returnConnection(con);
		}
		
		
		
		return offerOutletTrade;
	}
	
	public OfferResponse getOfferObjResponse(int offerId,int trade){
		OfferResponse objOffer = null;
		Date now=new Date();
		//int offerId=rs.getInt("offerid");
		
		Offer offer = (Offer) MrasCache.MCache.recover("Offer" + offerId);

		if (offer == null) {
			// not found in cache recive from db and update cache
			offer = rewardResourceDao.getOfferWithOfferid(offerId);
			if (offer == null) {
				
			}else{
				long timeToLive1 = offer.getRedemptionEndTime().getTimeInMillis()
				- now.getTime();
	        	MrasCache.MCache.admit("Offer" + offerId, offer, timeToLive1,
				timeToLive1);
			}
		}
		
		if(offer!=null){	
			objOffer = new OfferResponse();
			objOffer.setCampaignId(offer.getCampaignId());
			objOffer.setCurrency(offer.getCurrency());
			objOffer.setEndDate(offer.getEndDate());
			objOffer.setEnglish(offer.getEnglish());
			objOffer.setGhostImage(offer.getGhostImage());
			objOffer.setGiftable(offer.getGiftable());
			objOffer.setOfferId(offerId);
			logger.debug("offer is not null offername"+offer.getOfferName());
			
			objOffer.setOfferName(offer.getOfferName());
			if(trade==1)
			   objOffer.setOfferTrade("OnTrade");
			else
			   objOffer.setOfferTrade("OffTrade");
			
			objOffer.setOfferType(offer.getOfferType());
			objOffer.setOfferValue(offer.getOfferValue());
			objOffer.setRedemptionLimit(offer.getRedemptionLimit());
			objOffer.setRedemptionWindow(offer.getRedemptionWindow());
			objOffer.setRegional(offer.getRegional());
			objOffer.setStartDate(offer.getStartDate());
			objOffer.setValidBrands(offer.getValidBrands());
									
		   
		}		
		return objOffer;
	}
	
	public OutletDistance getOuletObjResponse(int offerId,int trade){
		return null;
	}
	
	
}