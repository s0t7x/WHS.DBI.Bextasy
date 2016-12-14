package bextasy.Connection;

import bextasy.Connection.DBmgmt;
import java.sql.*;
import java.util.Random;

public class LoadDriver extends Thread {
	private int thinkTime;
	private int initTime;
	private int endTime;
	private int benchmarkTime;
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
		end();

		TPS();

		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("LoadDriver done!");
	}

	private void init() {
		long startTime = System.currentTimeMillis();
		while ((System.currentTimeMillis() - startTime) < initTime) {
			try {
				Transaction();
				Thread.sleep(thinkTime);
			} catch (InterruptedException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void benchmark() {
		long startTime = System.currentTimeMillis();
		while ((System.currentTimeMillis() - startTime) < benchmarkTime) {
			try {
				Transaction();
				Thread.sleep(thinkTime);
			} catch (InterruptedException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			++transactionCount;
		}
	}

	private void end() {
		long startTime = System.currentTimeMillis();
		while ((System.currentTimeMillis() - startTime) < endTime) {
			try {
				Transaction();
				Thread.sleep(thinkTime);
			} catch (InterruptedException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("static-access")
	private void Transaction() throws SQLException {
		int chance = random.nextInt(100);
		if (chance < 35) {
			int accid = random.nextInt(100 * 100000) + 1;
			dbmgmt.getBalance(accid);
		} else if (chance >= 35 && chance < 85) {
			int accid = random.nextInt(100 * 100000) + 1;
			int tellerid = random.nextInt(100 * 10) + 1;
			int branchid = random.nextInt(100 * 1) + 1;
			int delta = random.nextInt(10000) + 1;
			dbmgmt.Deposit(accid, tellerid, branchid, delta);
		} else {
			int delta = random.nextInt(10000) + 1;
			dbmgmt.Analyse(delta);
		}
	}

	private void TPS() {
		float tps = (float) transactionCount / (float) (benchmarkTime / 1000);
		System.out.println("TPS: " + tps);
	}
}