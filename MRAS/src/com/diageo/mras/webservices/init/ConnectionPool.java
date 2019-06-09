package com.diageo.mras.webservices.init;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionPool {

	public static Connection getConnection() throws SQLException {
		Connection connection = null;

		while (connection == null) {
			try {
				InitialContext jndiCntx = new InitialContext();
				DataSource ds = (DataSource) jndiCntx.lookup("jdbc/MrasDS");
				connection = ds.getConnection();
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return connection;
	}

	public static void returnConnection(Connection returned) {
		try {
			if (null != returned && !returned.isClosed()) {
				returned.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
