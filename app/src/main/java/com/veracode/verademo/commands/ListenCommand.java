package com.veracode.verademo.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.commons.lang3.StringUtils;
import java.net.URLEncoder;

public class ListenCommand implements BlabberCommand {
	private static final Logger logger = LogManager.getLogger("VeraDemo:ListenCommand");

	private Connection connect;

	private String username;

	public ListenCommand(Connection connect, String username) {
		super();
		this.connect = connect;
		this.username = username;
	}

	@Override
	public void execute(String blabberUsername) {
		String sqlQuery = StringUtils.normalizeSpace("INSERT INTO listeners (blabber, listener, status) values (?, ?, 'Active');");
		logger.info(sqlQuery);
		PreparedStatement action;
		try {
			action = connect.prepareStatement(sqlQuery);

			action.setString(1, blabberUsername);
			action.setString(2, username);
			action.execute();

			sqlQuery = "SELECT blab_name FROM users WHERE username = '" + blabberUsername + "'";
			Statement sqlStatement = connect.createStatement();
			logger.info(sqlQuery);
			ResultSet result = sqlStatement.executeQuery(sqlQuery);
			result.next();

			/* START EXAMPLE VULNERABILITY */
			String event = username + " started listening to " + blabberUsername + " (" + URLEncoder.encode(result.getString(1).toString()) + ")";
			sqlQuery = "INSERT INTO users_history (blabber, event) VALUES (?,?)";
			logger.info(sqlQuery);
			PreparedStatement sqlStatement = connection.prepareStatement(sqlQuery);
			sqlStatement.setString(1, username);
			sqlStatement.setString(2, event);
			sqlStatement.execute();
			/* END EXAMPLE VULNERABILITY */
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

