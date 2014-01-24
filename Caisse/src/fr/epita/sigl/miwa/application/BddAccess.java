package fr.epita.sigl.miwa.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class BddAccess {
	private String url;
	private Connection connection;

	public BddAccess() {
	}

	public void connect() {
		url = "jdbc:postgresql://localhost:5432/miwa";
		Connection db;
		try {
			db = DriverManager.getConnection(url, "user", "user");
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}
		connection = db;
	}

	public ResultSet select(String sqlQuery) throws SQLException {
		Statement state = null;
		state = connection.createStatement();
		ResultSet rs = state.executeQuery(sqlQuery);
		return rs;
	}

	public void insert(String sqlQuery) throws SQLException {
		Statement state = null;
		state = connection.createStatement();
		state.executeUpdate(sqlQuery);
	}

	public int update(String sqlQuery) throws SQLException {
		Statement state = null;
		state = connection.createStatement();
		int n = state.executeUpdate(sqlQuery);
		return n;
		// n = 1 because one row had a change in it
	}

	public String[] parseresult(String string) {
		string = string.replace("(", "");
		string = string.replace(")", "");
		String[] str1Array = string.split(",");
		return str1Array;
	}

}
