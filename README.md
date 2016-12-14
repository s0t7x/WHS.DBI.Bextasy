# WHS.DBI.Bextasy
![alt text](https://github.com/s0t7x/WHS.DBI.Bextasy/raw/master/imageedit_1_2350814130.png "Logo")


# Documentation

## `public class Main`

Main Class and Method

 * **Author:** s0T7x

     <p>

## `static void _Menu_Home() throws SQLException`

Prints a simple console based Menu and lets the user choose between several options. This is the Home Menu. From here the user starts and can get into other menus.

Menu: 1. Goes to "Manage Connection" Menu 2. Goes to "Database Functions" Menu 3. Starts the Benchmarking 4. Exits application

Points 2/3 are disabled til the user connected with a database in the "Manage connection" menu.

 * **Exceptions:** `SQLException` — 

## `static void _Menu_ManageConnection()`

Menu to manage Connections and to interact with DBmgmt.

Menu: 1. Connects / Reconnect to given address, database with given credentials 2. Connects to localhost database "benchmark" with root and no password 3. Goes back to "Home Menu"

## `static void _Menu_DatabaseFunctions() throws SQLException`

Menu to do different DBmgmt methods

Menu: 1. Get the Balance of a given ACCID 2. Deposit a specified amount to an ACCID 3. Search for an deposit amount in the History

 * **Exceptions:** `SQLException` — 

## `static void _Menu_BenchmarkDatabase() throws SQLException`

Starts the benchmark on connected Database

 * **Exceptions:** `SQLException` — 

## `public class DBmgmt extends Thread`

Class to manage a connection with connection specific Methods.

 * **Author:** s0t7x

## `public DBmgmt(String _ServerAddress, String _DatabaseName, String _UserName, String _Password)`

Constructor directly connects to the database

 * **Parameters:**
   * `_ServerAddress` — 
   * `_DatabaseName` — 
   * `_UserName` — 
   * `_Password` — 

## `void Connect()`

Connects to the Database defined in this object It's not Public because it is only used by the class itself in the constructor

## `public static int getBalance(int accid) throws SQLException`

From "DBI: Aufgabenblatt 5": "Die Methode erwartet als Eingabeparameter den Wert einer Kontonummer ACCID und gibt den zugehörigen Kontostand BALANCE als Ausgabewert zurück."

 * **Parameters:** `accid` — 
 * **Returns:** Balance of ACCID
 * **Exceptions:** `SQLException` — 

## `public static void Deposit(int accid, int tellerid, int branchid, int delta) throws SQLException`

From "DBI: Aufgabenblatt 5": "Die Methode erwartet als Eingabeparameter jeweils Werte für - eine Kontonummer ACCID, - eine Geldautomatennummer TELLERID, - eine Zweigstellennummer BRANCHID - und einen Einzahlungsbetrag DELTA. Damit sollen innerhalb dieser Transaktion die folgenden Einzelaktionen durchgeführt werden: - In der Relation BRANCHES soll die zu BRANCHID gehörige Bilanzsumme BALANCE aktualisiert werden. - In der Relation TELLERS soll die zu TELLERID gehörige Bilanzsumme BALANCE aktualisiert werden. - In der Relation ACCOUNTS soll der zu ACCID gehörige Kontostand BALANCE aktualisiert werden, und - in der Relation HISTORY soll die Einzahlung (incl. des aktualisierten Kontostandes ACCOUNTS.BALANCE) protokolliert werden. Der ermittelte neue Kontostand soll als Ausgabewert der Methode zurückgegeben werden."

 * **Parameters:**
   * `accid` — 
   * `tellerid` — 
   * `branchid` — 
   * `delta` — 
 * **Exceptions:** `SQLException` — 

## `public static int Analyse(int delta) throws SQLException`

From "DBI: Aufgabenblatt 5": "Die Methode erwartet als Eingabeparameter den Wert eines Einzahlungsbetrages DELTA und gibt die Anzahl bisher protokollierter Einzahlungen mit genau diesem Betrag als Ausgabewert zurück."

 * **Parameters:** `delta` — 
 * **Returns:** Amount of Deposits with given Value delta
 * **Exceptions:** `SQLException` — 
