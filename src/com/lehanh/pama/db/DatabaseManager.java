package com.lehanh.pama.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.lehanh.pama.util.PamaHome;

public class DatabaseManager {
	
	private static DatabaseManager instance;
	private Connection connection;
	
	private final String defaultUserName = "root"; //$NON-NLS-1$
	private final String defaultPass = "root"; //$NON-NLS-1$
	private final String defaultDB = "pama"; //$NON-NLS-1$
	private final String defaultIP = 
									//"127.0.0.1";
									"localhost"; //$NON-NLS-1$
									//"192.168.86.110";
	
	private DatabaseManager() {
	}
	
	public static void initialize() throws SQLException, IOException {
		if (instance != null) {
			throw new RuntimeException("DatabaseManager is already initialized."); //$NON-NLS-1$
		}
		instance = new DatabaseManager();
		instance.initConnectionIfNeeded();
	}

	public static DatabaseManager getInstance() {
		return instance;
	}

	private void initConnectionIfNeeded() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			return;
		}
		try {
			PamaHome.application.logInfo("Loading db driver..."); //$NON-NLS-1$
			Class.forName("com.mysql.jdbc.Driver"); //$NON-NLS-1$
		} catch (ClassNotFoundException e) {
			SQLException se = new SQLException("Failed to load db driver"); //$NON-NLS-1$
			se.initCause(e);
			throw se;
		}
		PamaHome.application.logInfo("Opening db connection..."); //$NON-NLS-1$
		String dburl = "jdbc:mysql://" + PamaHome.getDbIp(defaultIP) + ":3307/" + PamaHome.getDbName(defaultDB) //$NON-NLS-1$ //$NON-NLS-2$
					+ "?useUnicode=true&characterEncoding=UTF-8" //$NON-NLS-1$
					//+ "?" + "user=" + PamaHome.getDbUserName(defaultUserName) + "&password=" + PamaHome.getDbPass(defaultPass)
					;
		connection = DriverManager.getConnection(dburl, PamaHome.getDbUserName(defaultUserName), PamaHome.getDbPass(defaultPass)
				//+ ";create=false"
				);
		// TODO for later ? migrationCheck();
		// TODO prepareStatements();
	}

	public Connection getConn() throws SQLException {
		initConnectionIfNeeded();
		return this.connection;
	}
}
