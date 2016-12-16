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
		Connect();
	}

	/**
	 * Connects to the Database defined in this object It's not Public because
	 * it is only used by the class itself in the constructor
	 */
	void Connect() {
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
	 * den Wert einer Kontonummer ACCID und gibt den zugehörigen Kontostand
	 * BALANCE als Ausgabewert zurück."
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
	 * jeweils Werte für - eine Kontonummer ACCID, - eine Geldautomatennummer
	 * TELLERID, - eine Zweigstellennummer BRANCHID - und einen
	 * Einzahlungsbetrag DELTA. Damit sollen innerhalb dieser Transaktion die
	 * folgenden Einzelaktionen durchgeführt werden: - In der Relation BRANCHES
	 * soll die zu BRANCHID gehörige Bilanzsumme BALANCE aktualisiert werden. -
	 * In der Relation TELLERS soll die zu TELLERID gehörige Bilanzsumme BALANCE
	 * aktualisiert werden. - In der Relation ACCOUNTS soll der zu ACCID
	 * gehörige Kontostand BALANCE aktualisiert werden, und - in der Relation
	 * HISTORY soll die Einzahlung (incl. des aktualisierten Kontostandes
	 * ACCOUNTS.BALANCE) protokolliert werden. Der ermittelte neue Kontostand
	 * soll als Ausgabewert der Methode zurückgegeben werden."
	 * 
	 * @param accid
	 * @param tellerid
	 * @param branchid
	 * @param delta
	 * @return New Balance for ACCID
	 * @throws SQLException
	 */
	public static int Deposit(int accid, int tellerid, int branchid, int delta) throws SQLException {
		int newBalance = getBalance(accid) + delta;

		String string30 = "123456789012345678901234567890";

		PreparedStatement stmt = conn.prepareStatement("");
		stmt.executeQuery("SET FOREIGN_KEY_CHECKS=0");
		
		stmt = conn.prepareStatement("UPDATE branches SET balance=balance+? WHERE ? = ?");
		stmt.setInt(1, delta);
		stmt.setString(2, "branchid");
		stmt.setInt(3, branchid);
		stmt.execute();
		
		stmt = conn.prepareStatement("UPDATE tellers SET balance=balance+? WHERE ? = ?");
		stmt.setInt(1, delta);
		stmt.setString(2, "tellerid");
		stmt.setInt(3, tellerid);
		stmt.execute();
		
		stmt = conn.prepareStatement("UPDATE accounts SET balance=balance+? WHERE ? = ?");
		stmt.setInt(1, delta);
		stmt.setString(2, "accid");
		stmt.setInt(3, accid);
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
	 * zurück."
	 * 
	 * @param delta
	 * @return Amount of Deposits with given Value delta
	 * @throws SQLException
	 */
	public static int Analyse(int delta) throws SQLException {
		int count = 0;
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM history WHERE delta=" + delta);
		if (rs.next())
			count = rs.getInt("count");
		conn.commit();
		return count;
	}

}
