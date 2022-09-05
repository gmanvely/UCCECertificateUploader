package com.cisco.tac.ucce.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GetDataFromAW {
	String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=pcce_awdb;user=sa;password=C1scoIPCC123";
	String databaseName;
	String user;
	String password;
	boolean windows_auth = false;

	public String getDatabaseName() {
		return databaseName;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public boolean isWindows_auth() {
		return windows_auth;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setWindows_auth(boolean windows_auth) {
		this.windows_auth = windows_auth;
	}

	GetDataFromAW(){
	 }
	
	void SetDataFromAW_ConnectionSQL(String dbname, String usr, String pwd) {
		if (dbname.contains("\\")) {
			
			connectionUrl = ("jdbc:sqlserver://"+dbname.substring(0,(dbname.indexOf("\\")))+":1433;databaseName=") + dbname.substring((dbname.indexOf("\\")+1)) + (";user=") + usr + (";password=")
					+ pwd;
			
		}
		else {connectionUrl = ("jdbc:sqlserver://localhost:1433;databaseName=") + dbname + (";user=") + usr + (";password=")
				+ pwd;}

	}

	void SetDataFromAW_ConnectionWin(String dbname) {
		
		if (dbname.contains("\\")) {
			connectionUrl = ("jdbc:sqlserver://"+dbname.substring(0,(dbname.indexOf("\\")))+":1433;databaseName=") + dbname.substring((dbname.indexOf("\\")+1)) + (";integratedSecurity=true");
		}
		else {
		connectionUrl = ("jdbc:sqlserver://localhost:1433;databaseName=") + dbname + (";integratedSecurity=true");
		}
	}

	int getRowsCount() {
		int rows = 0;
		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
			String SQL = "select count(Address) as NumberOfRows from t_Machine_Address, t_Machine_Host Where AddressType=1 and MachineType !=1 and t_Machine_Host.MachineHostID=t_Machine_Address.MachineHostID";
			ResultSet rsRows = stmt.executeQuery(SQL);
			while (rsRows.next()) {
				rows = rsRows.getInt("NumberOfRows");
			}

		}
		// Handle any errors that may have occurred.
		catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}

	public InventoryHost[] getHosts(int rows) {

		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
			String SQL = "select MachineName, Address, HostName, MachineType from t_Machine_Address, t_Machine_Host Where AddressType=1 and MachineType !=1 and t_Machine_Host.MachineHostID=t_Machine_Address.MachineHostID order by MachineType";
			ResultSet rsHosts = stmt.executeQuery(SQL);
			InventoryHost[] HostsArray = new InventoryHost[rows];
			int count = 0;
			while (rsHosts.next()) {
				System.out.println(rsHosts.getString("HostName") + " : " + rsHosts.getString("MachineType"));
				HostsArray[count] = new InventoryHost(rsHosts.getString("MachineName"), rsHosts.getString("HostName"),
						rsHosts.getString("Address"), rsHosts.getString("MachineType"));
				System.out.println(HostsArray[count].getAddress());
				count++;
			}
			return HostsArray;

		}
		// Handle any errors that may have occurred.
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}
	String testSQLconnection() {
		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
			String SQL = "SELECT count (MachineHostID) as result  from t_Machine_Host";
			ResultSet rsRows = stmt.executeQuery(SQL);
			int result = 0;
			while (rsRows.next()) {
				result = rsRows.getInt("result");
			}
			if (result==0) {
				return "Inventory tables are empty or not found";
			}

		}
		// Handle any errors that may have occurred.
		catch (SQLException e) {
			return e.getMessage();
		}
		return "done";
	}
}
