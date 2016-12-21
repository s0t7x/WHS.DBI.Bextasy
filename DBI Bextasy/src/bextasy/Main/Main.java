package bextasy.Main;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import bextasy.Connection.*;

/**
 * Main Class and Method
 * 
 * @author s0T7x
 *
 */
public class Main {
	static DBmgmt DB_conn_main = null;
	static Scanner ReadConsole = new Scanner(System.in);

	public static void main(String[] args) throws SQLException, InterruptedException {
		// TODO Auto-generated method stub

		_Menu_Home();
	}

	/**
	 * Prints a simple console based Menu and lets the user choose between
	 * several options. This is the Home Menu. From here the user starts and can
	 * get into other menus.
	 * 
	 * Menu: - 1. Goes to "Manage Connection" Menu - 2. Goes to "Database
	 * Functions" Menu - 3. Starts the Benchmarking 4. Exits application
	 * 
	 * Points 2/3 are disabled till the user connected with a database in the
	 * "Manage connection" menu.
	 * 
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	static void _Menu_Home() throws SQLException, InterruptedException {
		int selection = 0;

		do {
			System.out.println("[1] Manage Connection");
			System.out.println("[2] Database Functions");
			System.out.println("[3] Benchmark Database");
			System.out.println("[4] Exit");
			System.out.print("\nBextasy~> ");
			selection = ReadConsole.nextInt();

			switch (selection) {
			case 1:
				_Menu_ManageConnection();
				System.out.println("");
				break;
			case 2:
				if (DB_conn_main != null)
					_Menu_DatabaseFunctions();
				System.out.println("");
				break;
			case 3:
				if (DB_conn_main != null)
					_Menu_BenchmarkDatabase();
				System.out.println("");
				break;
			case 4:
				break;

			default:
				System.out.println("\nInvalid selection!\n");
				break;
			}
		} while (selection != 4);
		ReadConsole.close();
	}

	/**
	 * Menu to manage Connections and to interact with DBmgmt.
	 * 
	 * Menu: - 1. Connects / Reconnect to given address, database with given
	 * credentials - 2. Connects to localhost database "benchmark" with root and
	 * no password which was used to test locally - 3. Goes back to "Home Menu"
	 */
	static void _Menu_ManageConnection() {
		// DB_conn_main = new DBmgmt("localhost","benchmark", "root", "");
		int selection = 0;

		do {
			if (DB_conn_main != null)
				System.out.println("Connected: " + DB_conn_main.UserName + " @ " + DB_conn_main.ServerAddress + "/"
						+ DB_conn_main.DatabaseName + "\n");
			System.out.println("[1] Re-/Connect");
			System.out.println("[2] [DEBUG] Connect to localhost");
			System.out.println("[3] Back");
			System.out.print("\nBextasy~> ");
			selection = ReadConsole.nextInt();

			switch (selection) {
			case 1:
				DB_conn_main = null;
				System.out.print("Server Address: ");
				String temp_address = ReadConsole.next();
				System.out.print("Database Name: ");
				String temp_name = ReadConsole.next();
				System.out.print("Username: ");
				String temp_user = ReadConsole.next();
				System.out.print("Password: ");
				String temp_pw = ReadConsole.next();
				DB_conn_main = new DBmgmt(temp_address, temp_name, temp_user, temp_pw);
				System.out.print("");
				selection = 3;
				break;
			case 2:
				// DB_conn_main = new DBmgmt("localhost", "benchmark", "root",
				// "");
				DB_conn_main = new DBmgmt("192.168.122.55", "benchmark", "dbi", "dbi_pass");
				System.out.println("");
				selection = 3;
				break;
			case 3:
				break;

			default:
				System.out.println("\nInvalid selection!\n");
				break;
			}
		} while (selection != 3);
	}

	/**
	 * Menu to do different DBmgmt methods
	 * 
	 * Menu: - 1. Get the Balance of a given ACCID - 2. Deposit a specified
	 * amount to an ACCID - 3. Search for an deposit amount in the History - 4.
	 * Goes back to Home Menu
	 * 
	 * Suppress "static-access" warnings, because they are annoying in
	 * eclipse...
	 * 
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	@SuppressWarnings("static-access")
	static void _Menu_DatabaseFunctions() throws SQLException, InterruptedException {
		int selection = 0;

		do {
			System.out.println("[1] GetBalance(ACCID)");
			System.out.println("[2] Deposit(ACCID, AMOUNT)");
			System.out.println("[3] Analyse(AMOUNT)");
			System.out.println("[4] Back");
			System.out.print("\nBextasy~> ");
			selection = ReadConsole.nextInt();

			int accid = 0;
			switch (selection) {
			case 1:
				System.out.print("Enter ACCID: ");
				accid = ReadConsole.nextInt();
				System.out.println("Balance = " + DB_conn_main.getBalance(accid));
				System.out.println("");
				break;
			case 2:
				// System.out.print("Could do manual deposit here, but its not
				// coded yet\n");
				// Above comment is a LIE!!!
				System.out.print("Enter ACCID: ");
				int temp_accid = ReadConsole.nextInt();
				System.out.print("Enter TELLERID: ");
				int temp_tellerid = ReadConsole.nextInt();
				System.out.print("Enter BRANCHID: ");
				int temp_branchid = ReadConsole.nextInt();
				System.out.print("Enter DELTA: ");
				int temp_delta = ReadConsole.nextInt();
				DB_conn_main.Deposit(temp_accid, temp_tellerid, temp_branchid, temp_delta);
				break;
			case 3:
				System.out.print("Could do manual analyse here, but its not coded yet\n");
				break;
			case 4:
				break;

			default:
				System.out.println("\nInvalid selection!\n");
				break;
			}
		} while (selection != 4);
	}

	/**
	 * Asks for the amount of threads and starts LoadDriver threads on connected
	 * Database
	 * 
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	@SuppressWarnings("static-access")
	static void _Menu_BenchmarkDatabase() throws SQLException, InterruptedException {
		System.out.print("Amount of LoadDrivers to execute: ");
		int drivers = ReadConsole.nextInt();
		
		Statement st1 = DB_conn_main.conn.createStatement();
		st1.execute("DELETE FROM history");
		
		Thread threads[] = new Thread[drivers];
		LoadDriver loadDrivers[] = new LoadDriver[drivers];
		for (int i = 0; i < drivers; i++) {
			loadDrivers[i] = new LoadDriver(DB_conn_main);
			threads[i] = new Thread(loadDrivers[i]);
			threads[i].start();
		}

		// Waits till all threads finished
		for (int i = 0; i < drivers; i++) {
			threads[i].join();
		}
		System.out.println("Rollbacks: " + DB_conn_main.RollbackCounter);
		DB_conn_main.conn.close();
	}
}
