package bextasy.Connection;

import bextasy.Connection.DBmgmt;
import java.sql.*;
import java.util.Random;

/**
 * From "DBI: Aufgabenblatt 5": "[...] in einem Load-Driver-Programm, das 10
 * Minuten lang in einer Schleife jeweils zuf�llig gew�hlt eine der obigen TXs
 * mit zuf�llig gew�hlten, sinnvollen Parametern1 durchf�hrt und dabei die
 * bekannten ACID-Eigenschaften garantiert. Zwischen zwei einzelnen TXs soll
 * jeweils eine feste �Nachdenkzeit� (engl. Think Time) von genau 50 msec
 * liegen, in der das Benchmark-Programm nach der erfolgreichen Abarbeitung
 * einer TX einfach wartet, bevor es die n�chste Lasttransaktion startet. [...]"
 * 
 * @author s0T7x
 *
 */
public class LoadDriver extends Thread {
	// 50ms Think Time
	private int thinkTime = 50;

	// 4 Minuten Einschwingphase
	private int initTime = 240000;//240000;

	// 1 Minute Ausschwingphase
	private int endTime = 60000;//60000;

	// 5 Minuten Messphase
	private int benchmarkTime = 300000;//300000;

	// Counts the amount of transactions and is used to calculate TPSs
	private int transactionCount;

	private DBmgmt dbmgmt;
	Connection conn;
	private Random random = new Random();

	@SuppressWarnings("static-access")
	public LoadDriver(DBmgmt dbmgmt) {
		this.dbmgmt = dbmgmt;
		this.conn = dbmgmt.conn;
	}

	public void run() {
		init();
		
		benchmark();
		
		// Starts "Ausschwingphase"
		end();

		// Calculate and print TPS, cause thats what we want to know
		tps();

		// Even without print we should see when it's done but to print it makes it much cooler
		System.out.println("LoadDriver done!");
	}

	/**
	 * "Einschwingphase" Does transactions for 4 minutes WITHOUT counting!
	 */
	private void init() {
		// Remember when we started
		long startTime = System.currentTimeMillis();
		// And loop till we reach the defined time
		while ((System.currentTimeMillis() - startTime) < initTime) {
			try {
				// Do some cool transaction stuff
				transaction();
				// And sleep for the defined thinkTime
				Thread.sleep(thinkTime);
			} catch (InterruptedException | SQLException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	/**
	 * "Messphase" Does transactions for 5 minutes WITH counting
	 */
	private void benchmark() {
		long startTime = System.currentTimeMillis();
		while ((System.currentTimeMillis() - startTime) < benchmarkTime) {
			try {
				transaction();
				Thread.sleep(thinkTime);
			} catch (InterruptedException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			++transactionCount;
		}
	}

	/**
	 * "Ausschwingphase" Does transactions for 1 minute WITHOUT counting!
	 */
	private void end() {
		long startTime = System.currentTimeMillis();
		while ((System.currentTimeMillis() - startTime) < endTime) {
			try {
				transaction();
				Thread.sleep(thinkTime);
			} catch (InterruptedException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Either does "getBalance(accid)", "Deposit(...)" or "Analyse(delta)"
	 * depending on a chance of 35/50/15 From "DBI: Aufgabenblatt 5": "Die
	 * relative Gewichtung f�r die zuf�llige Auswahl der TXs sei dabei (35 zu 50
	 * zu 15) f�r Kontostands-, Einzahlungs- und Analyse-TXs."
	 * 
	 * @throws SQLException
	 */
	@SuppressWarnings("static-access")
	private void transaction() throws SQLException {
		// Get us a random number between 0 and 100
		int chance = random.nextInt(100);
		// So it is decided what to do based on the Chance 35/50/15
		if (chance < 35) {
			// Get the balance of a random accid
			int accid = random.nextInt(100 * 100000) + 1;
			dbmgmt.getBalance(accid);
		} else if (chance >= 35 && chance < 85) {
			// Deposit random parameters on random accid
			// "+ 1" because we dont want it to be 0
			int accid = random.nextInt(100 * 100000) + 1;
			int tellerid = random.nextInt(100 * 10) + 1;
			int branchid = random.nextInt(100 * 1) + 1;
			int delta = random.nextInt(10000) + 1;
			dbmgmt.deposit(accid, tellerid, branchid, delta);
		} else {
			// Analyse with random delta
			int delta = random.nextInt(10000) + 1;
			dbmgmt.analyse(delta);
		}
		
	}

	/**
	 * Calculates and prints TPS based on transactionCount
	 */
	private void tps() {
		// Calculates
		float tps = (float) transactionCount / (float) (benchmarkTime / 1000);
		// And prints
		System.out.println("TPS: " + tps);
		System.out.println("TXs: " + transactionCount);
	}
}