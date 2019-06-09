package com.diageo.mras.webservices.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import com.diageo.mras.webservices.init.ConnectionPool;

public class ValidationDateEmailDao {

	public int validationDateEmailDao(String email, java.util.Date birthDate) {

		Connection con = null;
		ResultSet rs;
		int consumerid = 0;
		try {

			con = ConnectionPool.getConnection();
			// System.out.println("paramter recive "+email+" -- "+birthDate);
			PreparedStatement preparedStatement = con
					.prepareStatement(SqlStatements.VALIDATE_EMAIL_DATEOFBIRTH);
			preparedStatement.setString(1, email);
			// System.out.println("return result11 "+consumerid);

			Timestamp timeStamp = null;
			if (birthDate != null) {
				timeStamp = new Timestamp(birthDate.getTime());
			}
			preparedStatement.setTimestamp(2, timeStamp);

			rs = preparedStatement.executeQuery();

			if (!rs.next()) {
				// System.out.println("return result "+0);
				return 0;
			}

			rs.previous();

			if (rs.next()) {
				consumerid = rs.getInt("consumerid");
				// System.out.println("return result1 "+consumerid);
				return consumerid;
			}
			rs.close();
			preparedStatement.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(con);
		}
		// System.out.println("return result123 "+consumerid);
		return consumerid;
	}

}
