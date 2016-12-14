# WHS.DBI.Bextasy

Schreiben Sie für die Benchmark-Datenbanken aus der Praktikumsaufgabe
7) drei Methoden (oder Funktionen), die die folgenden Lasttransaktionen (TXs)
innerhalb einer bestehenden(!) Datenbankverbindung (engl. Connection) durchführen:
i) Kontostands-TX
Die Methode erwartet als Eingabeparameter den Wert einer Kontonummer ACCID und
gibt den zugehörigen Kontostand BALANCE als Ausgabewert zurück.
ii) Einzahlungs-TX
Die Methode erwartet als Eingabeparameter jeweils Werte für
 eine Kontonummer ACCID,
 eine Geldautomatennummer TELLERID,
 eine Zweigstellennummer BRANCHID
 und einen Einzahlungsbetrag DELTA.
Damit sollen innerhalb dieser Transaktion die folgenden Einzelaktionen durchgeführt
werden:
 In der Relation BRANCHES soll die zu BRANCHID gehörige Bilanzsumme
BALANCE aktualisiert werden. 
 In der Relation TELLERS soll die zu TELLERID gehörige Bilanzsumme BALANCE
aktualisiert werden.
 In der Relation ACCOUNTS soll der zu ACCID gehörige Kontostand BALANCE
aktualisiert werden, und
 in der Relation HISTORY soll die Einzahlung (incl. des aktualisierten Kontostandes
ACCOUNTS.BALANCE) protokolliert werden.
Der ermittelte neue Kontostand soll als Ausgabewert der Methode zurückgegeben werden.
iii) Analyse-TX
Die Methode erwartet als Eingabeparameter den Wert eines Einzahlungsbetrages DELTA
und gibt die Anzahl bisher protokollierter Einzahlungen mit genau diesem Betrag als
Ausgabewert zurück.
Nutzen Sie die obigen Methoden in einem Load-Driver-Programm, das 10 Minuten lang in
einer Schleife jeweils zufällig gewählt eine der obigen TXs mit zufällig gewählten, sinnvollen
Parametern1
 durchführt und dabei die bekannten ACID-Eigenschaften garantiert. Zwischen
zwei einzelnen TXs soll jeweils eine feste „Nachdenkzeit“ (engl. Think Time) von genau
50 msec liegen, in der das Benchmark-Programm nach der erfolgreichen Abarbeitung einer
TX einfach wartet, bevor es die nächste Lasttransaktion startet. Die relative Gewichtung für
die zufällige Auswahl der TXs sei dabei (35 zu 50 zu 15) für Kontostands-, Einzahlungs- und
Analyse-TXs.
Nach den ersten 4 Minuten „Einschwingphase“ soll Ihr Programm in der „Messphase“ die
Anzahl der innerhalb der nächsten 5 Minuten durchgeführten Transaktionen bestimmen
(zusammen mit der durchschnittlichen Anzahl von Transaktionen pro Sekunde). Danach
verbleibt noch eine 1-minütige „Ausschwingphase“.
Erzeugen Sie eine 100-tps-Datenbank und führen Sie die folgenden Benchmark-Messungen
durch, nachdem Sie vorab jeweils die HISTORY-Relation geleert haben (die drei anderen
Relationen müssen nicht bei jeder Messung neu initialisiert werden!):
a) Last 1: Starten Sie Ihren Load Driver 5-mal nebenläufig auf einem Client-Rechner2
 und
bestimmen Sie die Gesamtsumme der TXs und die durchschnittliche Anzahl von TXs pro
Sekunde, die der Server insgesamt bearbeitet. (Achten Sie darauf, dass nicht alle
Programminstanzen dieselben Folgen von Zufallszahlen nutzen!)
b) Last 2: Starten Sie Ihren Load Driver 5-mal nebenläufig auf jeweils zwei verschiedenen
Client-Rechnern. Ermitteln Sie wieder die Gesamtzahl der TXs sowie die
durchschnittliche Anzahl pro Sekunde!
c) Last 3: Starten Sie Ihren Load Driver 5-mal nebenläufig auf jeweils drei verschiedenen
Client-Rechnern. Ermitteln Sie wieder die Gesamtzahl der TXs sowie die
durchschnittliche Anzahl pro Sekunde!
d) Verbessern Sie den Durchsatz Ihres Datenbankmanagementsystems und dokumentieren
Sie durchgeführte Verbesserungsideen und ihre Auswirkungen auf den erzielten Durchsatz!
(Vollständige Messergebnisse müssen nur für die optimierte Version vorliegen!)
Falls bei den Benchmark-Messungen unvorhergesehene Probleme auftreten, beschreiben Sie
diese und versuchen Sie, deren Ursachen zu ergründen und zu erläutern. Geben Sie im
Moodle ein gut kommentiertes Programm, Ihre Dokumentation und die protokollierten
Ergebnisse als ein pdf-Dokument ab! 


# Documentation

## `public class DBmgmt extends Thread`

Class to manage connections.

 * **Author:** s0t7x
