package com.diageo.mras.webservices.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;

import com.diageo.mras.webservices.init.ConnectionPool;

public class OccurrencepatternDao {

	static Logger logger = Logger.getLogger(OccurrencepatternDao.class
			.getName());

	public static boolean occurrencePattern(int OccurrencePatternId) {
		Connection con = null;
		ResultSet rs = null;

		try {
			con = ConnectionPool.getConnection();
			PreparedStatement stmt = con
					.prepareStatement(SqlStatements.OCCRENCEPATTERN_STRING);
			stmt.setInt(1, OccurrencePatternId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				/*
				 * logger.debug("Getting all parameter of occurancepatterID : "+
				 * OccurrencePatternId +"\nstartdate " +
				 * rs.getDate("StartDate")+
				 * "End date "+rs.getDate("EndDate")+"\nReoccurencPatternType "
				 * +
				 * rs.getInt("ReoccurencPatternType")+"\nOccurrencePatternValue1: "
				 * +rs.getInt("OccurrencePatternValue1")+
				 * "\ndays:"+rs.getString("Days"));
				 */
				if (logger.isDebugEnabled()) {
				logger.debug("OccurrencePatternId in occurrencePattern:- " +OccurrencePatternId);
				}
				Date today = new Date();

				if (today.after(rs.getDate("StartDate"))) {
					long time=today.getTime()-3600*24*1000;
					Date todayAfterOneDay = new Date();
					todayAfterOneDay.setTime(time);
					if (todayAfterOneDay.before(rs.getDate("EndDate"))) {

						
						
						
						int ReoccurencPatternTyp = rs
								.getInt("ReoccurencPatternType");
						int OccurrencePatternValu1 = rs
								.getInt("OccurrencePatternValue1");

						if (ReoccurencPatternTyp == 0) {

							long differenceInDays = today.getTime()
									- rs.getDate("StartDate").getTime();
							differenceInDays = differenceInDays / (86400000);
							// int i=d1.compareTo(rs.getDate("StartDate"));
							if (differenceInDays % OccurrencePatternValu1 == 0) {
								if (logger.isDebugEnabled()) {
									logger.debug("OccurrencePatternId :"
											+ OccurrencePatternId
											+ " is varified for " + today);
								}
								return true;
							} else {
								if (logger.isDebugEnabled()) {
									logger.debug("OccurrencePatternId :"
											+ OccurrencePatternId
											+ " is not varified for " + today);
								}
								return false;
							}
						}
						if (ReoccurencPatternTyp == 1) {
							
							String daysreturn = rs.getString("Days");
							char daysArray[] = daysreturn.toCharArray();

							int currentDay = today.getDay();
							int reciveDay = rs.getDate("StartDate").getDay();

							if (daysArray[currentDay] == '0') {
								if (logger.isDebugEnabled()) {
									logger.debug("weekly OccurrencePatternId :"
											+ OccurrencePatternId
											+ " is not varified for " + today);
								}
								return false;
							}
							reciveDay = currentDay - reciveDay;
							if (reciveDay < 0) {
								if (logger.isDebugEnabled()) {
								logger.debug("reciveDay is less then 0");
								}
								reciveDay = reciveDay + 7;
							} 
								
								long differenceInDays = today.getTime()
										- rs.getDate("StartDate").getTime();
								differenceInDays = differenceInDays / (86400000);
								if (logger.isDebugEnabled()) {
									logger.debug("difference in days"
											+ differenceInDays);
								}
								differenceInDays = differenceInDays - reciveDay;
								if (differenceInDays
										% (OccurrencePatternValu1 * 7) == 0) {
									if (logger.isDebugEnabled()) {
										logger.debug("weekly OccurrencePatternId :"
												+ OccurrencePatternId
												+ " is varified for " + today);
									}
									return true;
								} else {
									if (logger.isDebugEnabled()) {
										logger.debug("weekly OccurrencePatternId :"
												+ OccurrencePatternId
												+ " is not varified for "
												+ today);
									}
									return false;
								}
							
						}
						if (ReoccurencPatternTyp == 2) {

							Integer OccurrencePatternValu2 = rs
									.getInt("OccurrencePatternValue2");
							int reciveMonth = rs.getDate("StartDate")
									.getMonth();
							int currentdate = today.getDate();
							int currentMonth = today.getMonth();

							if (OccurrencePatternValu2 == currentdate) {

								if ((currentMonth - reciveMonth)
										% OccurrencePatternValu1 == 0) {
									// logger.debug("Monthly OccurrencePatternId :"+OccurrencePatternId+
									// " is varified for "+today);
									return true;
								} else {
									if (logger.isDebugEnabled()) {
										logger.debug("Monthly OccurrencePatternId :"
												+ OccurrencePatternId
												+ " is not varified for "
												+ today);
									}
									return false;
								}

							} else {
								if (logger.isDebugEnabled()) {
									logger.debug("Monthly OccurrencePatternId :"
											+ OccurrencePatternId
											+ " is not varified for " + today);
								}
								return false;
							}
						}

					} else {
						if (logger.isDebugEnabled()) {
						 logger.debug("OccurrencePatternId :"+OccurrencePatternId+
						 " is not varified for "+today +" as current date is after end date");
					}
						return false;
					}
				} else {
					if (logger.isDebugEnabled()) {
					 logger.debug("OccurrencePatternId :"+OccurrencePatternId+
					 " is not varified for "+today+" as current date is before start date");
					}
					return false;
				}
			} else {
				if (logger.isDebugEnabled()) {
				 logger.debug("OccurrencePatternId :"+OccurrencePatternId+
				 " is not valid");
				}
				return false;
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		if (logger.isDebugEnabled()) {
		logger.debug("returning from common false ");
		}
		return false;
	}

}
