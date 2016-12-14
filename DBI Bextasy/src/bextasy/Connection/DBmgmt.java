/**
 * 
 */
package bextasy.Connection;

import java.sql.*;

/**
 * Class to manage connections.
 * @author s0t7x
 */
public class DBmgmt extends Thread{
	String ServerAddress;
	String DatabaseName;
	String UserName;
	String Password;
	Connection conn = null;
	
	DBmgmt(String _ServerAddress, String _DatabaseName, String _UserName, String _Password) {
		// Initialize a specified Connection and connect()
		ServerAddress = _ServerAddress;
		DatabaseName = _DatabaseName;
		UserName = _UserName;
		Password = _Password;
		System.out.println("Ready to connect to " + _ServerAddress);
		Connect();
	}
	
	void Connect(){
		// Try to establish a connection
		System.out.println("Connect to " + ServerAddress + "with given credentials for " + UserName + "...");
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + ServerAddress + "/" + DatabaseName + "?allowMultiQueries=true", UserName, Password);
			System.out.println("Connection established!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
