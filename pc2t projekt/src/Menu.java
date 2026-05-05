import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Menu {
	private Scanner sc;
	private Databaza db;
	
	public Menu(Scanner sc, Databaza db)
	{
		this.sc = sc;
		this.db = db;
	}
	
	public void pridatZamestnanca() {
		int skupina = 0;
		
		while(skupina != 1 && skupina != 2) {
		System.out.println("\nVyber skupinu noveho zamestnanca: ");
		System.out.println("[1] Datovy analitik");
		System.out.println("[2] Bezpecnostny specialista");
		volba();
	
			try{
				skupina = sc.nextInt();
				if (skupina != 1 && skupina != 2) {
					System.out.println("Neplatna volba.");
				}
			} catch (InputMismatchException e) {
				System.out.println("Zadajte cislo.");
				sc.nextLine();
			}
		}
		
		System.out.println("\nMeno zamestnanca: ");
		String meno = nacitajString();
		
		System.out.println("Priezvisko zamestnanca: ");
		String priezvisko = nacitajString();
				
		System.out.println("Rok narodenia:");
		int rok = nacitajCislo();
		
		switch(skupina) {
		case 1 -> { DataAnalitik da = new DataAnalitik(meno, priezvisko, rok); 
			db.pridajZamestnanca(da); System.out.println("Pridany zamestnanec: "+da); }
		case 2 -> { BezpecnostnySpecialista bs = new BezpecnostnySpecialista(meno, priezvisko, rok); 
			db.pridajZamestnanca(bs); System.out.println("Pridany zamestnanec:"+bs); }
		}
	}
	
	public void pridatSpolupracu() {
	    int volba = 0;
	    UrovenSpoluprace urovenSpoluprace = null;

	    
	    System.out.println("Zadajte ID zamestnanca:");
	    int id1 = nacitajCislo();

	    System.out.println("Zadajte ID kolegu:");
	    int id2 = nacitajCislo();

	    if (db.najdiZamestnancaPodlaId(id1) == null || db.najdiZamestnancaPodlaId(id2) == null) {
	        System.out.println("Zamestnanec s danym ID neexistuje.");
	        return;
	    }

	    if (id1 == id2) {
	        System.out.println("Nemozno pridat spolupracu sam so sebou.");
	        return;
	    }

	    while (volba != 1 && volba != 2 && volba != 3) {
	        System.out.println("\nZvol uroven spoluprace:");
	        System.out.println("[1] " + UrovenSpoluprace.DOBRA);
	        System.out.println("[2] " + UrovenSpoluprace.PRIEMERNA);
	        System.out.println("[3] " + UrovenSpoluprace.ZLA);
	        volba();
	        volba = nacitajCislo();
	        if (volba != 1 && volba != 2 && volba != 3) {
	            System.out.println("Neplatna volba.");
	        }
	    }

	    switch (volba) {
	        case 1 -> urovenSpoluprace = UrovenSpoluprace.DOBRA;
	        case 2 -> urovenSpoluprace = UrovenSpoluprace.PRIEMERNA;
	        case 3 -> urovenSpoluprace = UrovenSpoluprace.ZLA;
	    }

	    db.pridajSpolupracu(id1, id2, urovenSpoluprace);
	    Zamestnanec z1 = db.najdiZamestnancaPodlaId(id1);
	    Zamestnanec z2 = db.najdiZamestnancaPodlaId(id2);
	    System.out.println("Spolupraca uzavreta: " + z1 + " <-> " + z2 + " (" + urovenSpoluprace + ")");
	}
	
	public void odstranitZamestnanca() {
		System.out.println("Zadajte ID zamestnanca, ktoreho chcete odstranit: ");
		int id = nacitajCislo();
		
		Zamestnanec z = db.najdiZamestnancaPodlaId(id);
		if(z == null) {
			System.out.println("Zamestnanec s danym ID neexistuje.");
			return;
		}
		db.odstranZamestnanca(id);
		System.out.println("Odstranili ste zamestnanca: "+z);
	}
	
	public void vyhladatZamestnancaPodlaID() {
	    System.out.println("Zadajte ID zamestnanca:");
	    int id = nacitajCislo();

	    Zamestnanec z = db.najdiZamestnancaPodlaId(id);
	    if (z == null) {
	        System.out.println("Zamestnanec s danym ID neexistuje.");
	        return;
	    }

	    System.out.println("\n=== ZAMESTNANEC ===");
	    System.out.println(z);
	    System.out.println("Skupina: " + z.getTypSkupiny());
	    System.out.println("Pocet spoluprac: " + z.getPocetSpoluprac());

	    int dobra = 0, priemerna = 0, zla = 0;
	    for (UrovenSpoluprace u : z.getSpoluprace().values()) {
	        if (u == UrovenSpoluprace.DOBRA) dobra++;
	        else if (u == UrovenSpoluprace.PRIEMERNA) priemerna++;
	        else zla++;
	    }

	    System.out.println("Dobre spoluprace: " + dobra);
	    System.out.println("Priemerne spoluprace: " + priemerna);
	    System.out.println("Zle spoluprace: " + zla);
	}
	
	public void pouzitZrucnostZamestnanca() {
		Zamestnanec z = nacitajZamestnanca("Zadajte ID zamestnanca: ");
	    z.pouzitZrucnost();
	}
	
	public void vypisPodlaSkupinAPriezviska() {
		db.vypisZamestnanovPodlaSkupinAPriezviska();
		}
	
	public void statistiky() {
		db.vypisStatistiky();
		}
	
	public void pocetZamestnancovVSkupinach() {
		db.vypisPoctyVSkupinach();
		}
	
	public void ulozenieDoSQL() {
	    db.ulozenieDoSQL();
	}

	public void nacitanieZSQL() {
	    db.nacitanieZoSQL();
	}	
	
	public void volba()
	{
		System.out.print("Volba: ");
	}
	public int nacitajCislo() {
	    while (true) {
	        try {
	            return sc.nextInt();
	        } catch (InputMismatchException e) {
	            System.out.println("Zadajte cislo.");
	            sc.nextLine();
	        }
	    }
	}
	
	private String nacitajString() {
		while (true) {
			String vstup = sc.next();
			if(vstup.matches("[a-zA-zÀ-ž]+")) {
				return vstup;
			}
			System.out.println("Zadajte len pismena.");
		}
	}
	
	private Zamestnanec nacitajZamestnanca(String sprava) {
	    System.out.println(sprava);
	    int id = nacitajCislo();
	    
	    Zamestnanec z = db.najdiZamestnancaPodlaId(id);
	    if (z == null) {
	        System.out.println("Zamestnanec s danym ID neexistuje.");
	    }
	    return z;
	}
}




