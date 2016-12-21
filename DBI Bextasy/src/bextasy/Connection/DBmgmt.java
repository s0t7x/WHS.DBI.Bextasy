package bextasy.Connection;

import java.sql.*;

/**
 * Class to manage a connection with connection specific Methods.
 * 
 * @author s0t7x
 */
public class DBmgmt extends Thread {

	// ----------- Definitions -------------
	public String ServerAddress;
	public String DatabaseName;
	public String UserName;
	public String Password;
	public static Connection conn = null;
	// -------------------------------------

	/**
	 * Constructor directly connects to the database
	 * 
	 * @param _ServerAddress
	 * @param _DatabaseName
	 * @param _UserName
	 * @param _Password
	 */
	public DBmgmt(String _ServerAddress, String _DatabaseName, String _UserName, String _Password) {
		// Initialize a specified Connection and connect()
		ServerAddress = _ServerAddress;
		DatabaseName = _DatabaseName;
		UserName = _UserName;
		Password = _Password;
		System.out.println("Ready to connect to " + _ServerAddress);
		connect();
	}

	/**
	 * Connects to the Database defined in this object It's not Public because
	 * it is only used by the class itself in the constructor
	 */
	void connect() {
		// Try to establish a connection
		System.out.println("Connect to " + ServerAddress + " with given credentials for " + UserName + "...");
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://" + ServerAddress + "/" + DatabaseName + "?allowMultiQueries=true&useLocalSessionState=true", UserName,
					Password);
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			System.out.println("Connection established!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * From "DBI: Aufgabenblatt 5": "Die Methode erwartet als Eingabeparameter
	 * den Wert einer Kontonummer ACCID und gibt den zugeh�rigen Kontostand
	 * BALANCE als Ausgabewert zur�ck."
	 * 
	 * @param accid
	 * @return Balance of ACCID
	 * @throws SQLException
	 */
	public static int getBalance(int accid) throws SQLException {
		int balance = 0;
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT balance FROM accounts WHERE accid=" + accid);
		if (rs.next())
			balance = rs.getInt("balance");
		conn.commit();
		stmt.close();
		return balance;
	}

	/**
	 * From "DBI: Aufgabenblatt 5": "Die Methode erwartet als Eingabeparameter
	 * jeweils Werte f�r - eine Kontonummer ACCID, - eine Geldautomatennummer
	 * TELLERID, - eine Zweigstellennummer BRANCHID - und einen
	 * Einzahlungsbetrag DELTA. Damit sollen innerhalb dieser Transaktion die
	 * folgenden Einzelaktionen durchgef�hrt werden: - In der Relation BRANCHES
	 * soll die zu BRANCHID geh�rige Bilanzsumme BALANCE aktualisiert werden. -
	 * In der Relation TELLERS soll die zu TELLERID geh�rige Bilanzsumme BALANCE
	 * aktualisiert werden. - In der Relation ACCOUNTS soll der zu ACCID
	 * geh�rige Kontostand BALANCE aktualisiert werden, und - in der Relation
	 * HISTORY soll die Einzahlung (incl. des aktualisierten Kontostandes
	 * ACCOUNTS.BALANCE) protokolliert werden. Der ermittelte neue Kontostand
	 * soll als Ausgabewert der Methode zur�ckgegeben werden."
	 * 
	 * @param accid
	 * @param tellerid
	 * @param branchid
	 * @param delta
	 * @return New Balance for ACCID
	 * @throws SQLException
	 */
	public static int deposit(int accid, int tellerid, int branchid, int delta) throws SQLException {
		int newBalance = getBalance(accid) + delta;

		String string30 = "123456789012345678901234567890";

		PreparedStatement stmt = conn.prepareStatement("");
		stmt.executeQuery("SET FOREIGN_KEY_CHECKS=0");
		
		stmt = conn.prepareStatement("UPDATE accounts SET balance=balance+? WHERE accid = ?");
		stmt.setInt(1, delta);
		stmt.setInt(2, accid);
		stmt.execute();
		
		stmt = conn.prepareStatement("UPDATE tellers SET balance=balance+? WHERE tellerid = ?");
		stmt.setInt(1, delta);
		stmt.setInt(2, tellerid);
		stmt.execute();
		
		stmt = conn.prepareStatement("UPDATE branches SET balance=balance+? WHERE branchid = ?");
		stmt.setInt(1, delta);
		stmt.setInt(2, branchid);
		stmt.execute();
		
		stmt.executeUpdate("INSERT INTO history(accid, tellerid, delta, branchid, accbalance, cmmnt)" + "VALUES("
				+ accid + "," + tellerid + "," + delta + "," + branchid + "," + newBalance + ",'" + string30 + "')");
		
		// conn.commit();
//		stmt.executeUpdate("UPDATE branches SET balance=balance+" + delta + " WHERE branchid=" + branchid);
//		stmt.executeUpdate("UPDATE tellers SET balance=balance+" + delta + " WHERE tellerid=" + tellerid);
//		stmt.executeUpdate("UPDATE accounts SET balance=balance+" + delta + " WHERE accid=" + accid);
//		stmt.executeUpdate("INSERT INTO history(accid, tellerid, delta, branchid, accbalance, cmmnt)" + "VALUES("
//				+ accid + "," + tellerid + "," + delta + "," + branchid + "," + newBalance + ",'" + string30 + "')");
//		// conn.commit();
		// stmt.executeQuery("SET FOREIGN_KEY_CHECKS=1");
		try {
			conn.commit();
		} catch (SQLException e) {
			try {
				e.printStackTrace();
				conn.rollback();
			} catch (SQLException e2) {
				conn.rollback();
			}
		}
		stmt.close();
		return getBalance(accid);
	}

	/**
	 * From "DBI: Aufgabenblatt 5": "Die Methode erwartet als Eingabeparameter
	 * den Wert eines Einzahlungsbetrages DELTA und gibt die Anzahl bisher
	 * protokollierter Einzahlungen mit genau diesem Betrag als Ausgabewert
	 * zur�ck."
	 * 
	 * @param delta
	 * @return Amount of Deposits with given Value delta
	 * @throws SQLException
	 */
	public static int analyse(int delta) throws SQLException {
		int count = 0;
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM history WHERE delta=" + delta);
		if (rs.next())
			count = rs.getInt("count");
		conn.commit();
		return count;
	}

}
