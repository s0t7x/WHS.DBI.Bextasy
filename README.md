<center>![alt text](https://github.com/s0t7x/WHS.DBI.Bextasy/raw/master/imageedit_1_2350814130.png "Logo")</center>


# Documentation

## `public class LoadDriver extends Thread`

From "DBI: Aufgabenblatt 5": "[...] in einem Load-Driver-Programm, das 10 Minuten lang in einer Schleife jeweils zufällig gewählt eine der obigen TXs mit zufällig gewählten, sinnvollen Parametern1 durchführt und dabei die bekannten ACID-Eigenschaften garantiert. Zwischen zwei einzelnen TXs soll jeweils eine feste „Nachdenkzeit“ (engl. Think Time) von genau 50 msec liegen, in der das Benchmark-Programm nach der erfolgreichen Abarbeitung einer TX einfach wartet, bevor es die nächste Lasttransaktion startet. [...]"

 * **Author:** s0T7x

     <p>

## `private void init()`

"Einschwingphase" Does transactions for 4 minutes WITHOUT counting!

## `private void benchmark()`

"Messphase" Does transactions for 5 minutes WITH counting

## `private void end()`

"Ausschwingphase" Does transactions for 1 minute WITHOUT counting!

## `private void Transaction() throws SQLException`

Either does "getBalance(accid)", "Deposit(...)" or "Analyse(delta)" depending on a chance of 35/50/15 From "DBI: Aufgabenblatt 5": "Die relative Gewichtung für die zufällige Auswahl der TXs sei dabei (35 zu 50 zu 15) für Kontostands-, Einzahlungs- und Analyse-TXs."

 * **Exceptions:** `SQLException` — 

## `private void TPS()`

Calculates and prints TPS based on transactionCount

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

## `public static int Deposit(int accid, int tellerid, int branchid, int delta) throws SQLException`

From "DBI: Aufgabenblatt 5": "Die Methode erwartet als Eingabeparameter jeweils Werte für
- eine Kontonummer ACCID,
- eine Geldautomatennummer TELLERID,
- eine Zweigstellennummer BRANCHID
- und einen Einzahlungsbetrag DELTA.
Damit sollen innerhalb dieser Transaktion die folgenden Einzelaktionen durchgeführt werden:
- In der Relation BRANCHES soll die zu BRANCHID gehörige Bilanzsumme BALANCE aktualisiert werden.
- In der Relation TELLERS soll die zu TELLERID gehörige Bilanzsumme BALANCE aktualisiert werden.
- In der Relation ACCOUNTS soll der zu ACCID gehörige Kontostand BALANCE aktualisiert werden, und
- in der Relation HISTORY soll die Einzahlung (incl. des aktualisierten Kontostandes ACCOUNTS.BALANCE) protokolliert werden.
Der ermittelte neue Kontostand soll als Ausgabewert der Methode zurückgegeben werden."

 * **Parameters:**
   * `accid` — 
   * `tellerid` — 
   * `branchid` — 
   * `delta` — 
 * **Returns:** New Balance for ACCID
 * **Exceptions:** `SQLException` — 

## `public static int Analyse(int delta) throws SQLException`

From "DBI: Aufgabenblatt 5": "Die Methode erwartet als Eingabeparameter den Wert eines Einzahlungsbetrages DELTA und gibt die Anzahl bisher protokollierter Einzahlungen mit genau diesem Betrag als Ausgabewert zurück."

 * **Parameters:** `delta` — 
 * **Returns:** Amount of Deposits with given Value delta
 * **Exceptions:** `SQLException` — 

## `public class Main`

Main Class and Method

 * **Author:** s0T7x

     <p>

## `static void _Menu_Home() throws SQLException, InterruptedException`

Prints a simple console based Menu and lets the user choose between several options. This is the Home Menu. From here the user starts and can get into other menus.

Menu:
- 1. Goes to "Manage Connection" Menu
- 2. Goes to "Database Functions" Menu
- 3. Starts the Benchmarking
- 4. Exits application

Points 2/3 are disabled till the user connected with a database in the "Manage connection" menu.

 * **Exceptions:**
   * `SQLException` — 
   * `InterruptedException` — 

## `static void _Menu_ManageConnection()`

Menu to manage Connections and to interact with DBmgmt.

Menu:
- 1. Connects / Reconnect to given address, database with given credentials
- 2. Connects to localhost database "benchmark" with root and no password which was used to test locally
- 3. Goes back to "Home Menu"

## `static void _Menu_DatabaseFunctions() throws SQLException, InterruptedException`

Menu to do different DBmgmt methods

Menu:
- 1. Get the Balance of a given ACCID
- 2. Deposit a specified amount to an ACCID
- 3. Search for an deposit amount in the History
- 4. Goes back to Home Menu

Suppress "static-access" warnings, because they are annoying in eclipse...

 * **Exceptions:**
   * `SQLException` — 
   * `InterruptedException` — 

## `static void _Menu_BenchmarkDatabase() throws SQLException, InterruptedException`

Asks for the amount of threads and starts LoadDriver threads on connected Database

 * **Exceptions:**
   * `SQLException` — 
   * `InterruptedException` — 
