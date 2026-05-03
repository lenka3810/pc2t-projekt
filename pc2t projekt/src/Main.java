import java.util.*;

public class Main {

	public static void main(String[] args) {
		final String NAZOV_SUBORU = "databaza.txt";
		Scanner sc = new Scanner(System.in);
		Databaza db = new Databaza();
		Menu menu = new Menu(sc, db);
		
		menu.nacitanieZSQL();
		
		while(true)
		{
			System.out.println("\n=== MENU ===");
			System.out.println("[1] Pridat zamestnanca");
			System.out.println("[2] Pridat spolupracu");
			System.out.println("[3] Odstranit zamestnanca");
			System.out.println("[4] Vyhladat zamestnanca podla ID");
			System.out.println("[5] Pouzit zrucnost zamestnanca");
			System.out.println("[6] Vypis zamestnancov podla skupina a priezviska");
			System.out.println("[7] Statistiky");
			System.out.println("[8] Pocet zamestnancov v skupinach");
			System.out.println("[9] Ulozenie zamestnancov do suboru");
			System.out.println("[10] Nacitanie zamestnancov zo suboru");
			System.out.println("[0] Koniec");
			menu.volba();

			int volba = menu.nacitajCislo();
			
			switch(volba) {
			case 1 -> menu.pridatZamestnanca();
			case 2 -> menu.pridatSpolupracu();
			case 3 -> menu.odstranitZamestnanca();
			case 4 -> menu.vyhladatZamestnancaPodlaID();
			case 5 -> menu.pouzitZrucnostZamestnanca();
			case 6 -> menu.vypisPodlaSkupinAPriezviska();
			case 7 -> menu.statistiky();
			case 8 -> menu.pocetZamestnancovVSkupinach();
			case 9 -> db.ulozenieDoSuboru(NAZOV_SUBORU);
			case 10 -> db.nacitanieZoSuboru(NAZOV_SUBORU);
			case 0 -> { menu.ulozenieDoSQL(); System.out.println("Koniec programu."); return; }
			default -> System.out.println("Neplatna volba."); }
		}

	}
}
