package com.diageo.mras.webservices.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import com.diageo.mras.webservices.init.ConnectionPool;
import com.diageo.mras.webservices.modals.TextUrl;

public class ConsumerURLFromMessageDao {

	public static TextUrl getUrlFromMessage(String message) {
		TextUrl textUrlobj = new TextUrl();
		Connection con = null;
		ResultSet rs;
		try {

			con = ConnectionPool.getConnection();
			// System.out.println("paramter recive "+email+" -- "+birthDate);
			PreparedStatement preparedStatement = con
					.prepareStatement(SqlStatements.TEXT_URL);
			preparedStatement.setString(1, message);
			// System.out.println("return result11 "+consumerid);

			rs = preparedStatement.executeQuery();

			if (!rs.next()) {
				return null;
			} else {
				rs.previous();
				while (rs.next()) {
					textUrlobj.setURL(rs.getString("URL"));
					textUrlobj.setSMSPhone(rs.getBoolean("SMSPhone"));
					textUrlobj.setPhoneEncryption(rs
							.getBoolean("PhoneEncryption"));

				}
			}
			rs.close();
			preparedStatement.close();

		} catch (Exception e) {
			System.out.println("Exception in :- "
					+ ConsumerURLFromMessageDao.class.getSimpleName());
			e.printStackTrace();
		}

		finally {
			ConnectionPool.returnConnection(con);
		}
		return textUrlobj;

	}

}
