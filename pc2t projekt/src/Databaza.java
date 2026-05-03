import java.io.*;
import java.util.*;

public class Databaza {
    private ArrayList<Zamestnanec> zamestnanci;

    public Databaza() {
        this.zamestnanci = new ArrayList<>();
    }

    public void pridajZamestnanca(Zamestnanec zamestnanec) {
        zamestnanci.add(zamestnanec);
    }

    public void odstranZamestnanca(int id) {
        Zamestnanec z = najdiZamestnancaPodlaId(id);
        
        if (z != null) {
            for (Zamestnanec ostatny : zamestnanci) {
                ostatny.odstranSpolupracu(z);
            }
            zamestnanci.remove(z);
        }
    }
    
    public Zamestnanec najdiZamestnancaPodlaId(int id) {
        for (Zamestnanec z : zamestnanci) {
            if (z.getId() == id) {
                return z;
            }
        }
        return null;
    }

    public void pridajSpolupracu(int id1, int id2, UrovenSpoluprace urovenSpoluprace) {
        Zamestnanec z1 = najdiZamestnancaPodlaId(id1);
        Zamestnanec z2 = najdiZamestnancaPodlaId(id2);
        
        if (z1 != null && z2 != null && z1 != z2) {
            z1.pridajSpolupracu(z2, urovenSpoluprace);
            z2.pridajSpolupracu(z1, urovenSpoluprace);
        }
    }

    public void vypisZamestnanovPodlaSkupinAPriezviska() {
    	for (TypSkupiny typSkupiny : TypSkupiny.values()) {
    		System.out.println("=== "+typSkupiny+" ===");
    		ArrayList<Zamestnanec> zamestnanciKopia = new ArrayList<>();
    		for (Zamestnanec z : zamestnanci) {
    			if(z.getTypSkupiny() == typSkupiny) {
    				zamestnanciKopia.add(z);
    			}
    		}
    		Collections.sort(zamestnanciKopia, Comparator.comparing(Zamestnanec::getPriezvisko));
    		for (Zamestnanec z : zamestnanciKopia) {
    			System.out.println(z);
    		}
    	}
    }
    
    public void vypisPoctyVSkupinach() {
    	if(zamestnanci.isEmpty()) {
    		System.out.println("Databaza je prazdna");
    		return;
    	}
    	for (TypSkupiny typSkupiny : TypSkupiny.values()) {
    		int pocet = 0;
    		for(Zamestnanec z : zamestnanci) {
    			if(z.getTypSkupiny() == typSkupiny) {
    				pocet++;
    			}
    		}
    		System.out.println("["+typSkupiny+"]: "+pocet);
    	}
    }
    
    public void vypisStatistiky() {
    	Zamestnanec zNajviacVazieb = null;
    	if(zamestnanci.isEmpty()) {
    		System.out.println("Databaza je prazdna.");
    		return;
    	}
    	for (Zamestnanec z : zamestnanci) {
    		if (zNajviacVazieb == null || z.getPocetSpoluprac() > zNajviacVazieb.getPocetSpoluprac()) {
    			zNajviacVazieb = z;
    		}
    	}
    	if(zNajviacVazieb != null) {
    		System.out.println("Najviac vazieb: "+zNajviacVazieb+" ("+zNajviacVazieb.getPocetSpoluprac()+")");
    	}
    	
    	int dobra = 0, priemerna = 0, zla = 0;
    	for (Zamestnanec z : zamestnanci) {
    		for (UrovenSpoluprace u : z.getSpoluprace().values()) {
    			if (u == UrovenSpoluprace.DOBRA) dobra++;
    			else if (u == UrovenSpoluprace.PRIEMERNA) priemerna++;
    			else zla++;
    		}
    	}
    	
    	String prevazujuca;
    	if (zla >= priemerna && zla >= dobra) prevazujuca = "ZLA";
    	else if(priemerna >= zla && priemerna >= dobra) prevazujuca = "PRIEMERNA";
    	else prevazujuca = "DOBRA";
    	
    	System.out.println("Prevazujuca kvalita spoluprace: "+prevazujuca);
    }
    
    public void ulozenieDoSuboru(String nazovSuboru) {
		try (PrintWriter pw = new PrintWriter(new FileWriter(nazovSuboru))) {		
			for (Zamestnanec z : zamestnanci) {
				pw.println(z.getTypSkupiny());
				pw.println(z.getMeno());
				pw.println(z.getPriezvisko());
				pw.println(z.getRokNarodenia());
			}
			
			pw.println("SPOLUPRACE");
			for (Zamestnanec z : zamestnanci) {
				for(Map.Entry<Zamestnanec, UrovenSpoluprace> entry : z.getSpoluprace().entrySet()) {
					if (z.getId() < entry.getKey().getId()) {
						pw.println(z.getId()+":"+entry.getKey().getId()+":"+entry.getValue());
					}
				}
			}
			System.out.println("Databaza ulozena do suboru: "+nazovSuboru);
			
		} catch (IOException e) {
			System.out.println("Chyba pri ukladani: "+ e.getMessage());
		}
	}
	public void nacitanieZoSuboru(String nazovSuboru) {
	    try (Scanner fileSc = new Scanner(new File(nazovSuboru))) {
	    	while (fileSc.hasNextLine()) {
	    		String riadok = fileSc.nextLine().trim();
	    		
	    		if (riadok.equals("SPOLUPRACE")) break;
	    		if(riadok.equals("--------")) continue;
	    		
	    		TypSkupiny typ = TypSkupiny.valueOf(riadok);
	    		String meno = fileSc.nextLine().trim();
	    		String priezvisko = fileSc.nextLine().trim();
	    		int rok = Integer.parseInt(fileSc.nextLine().trim());
	    		
	    		Zamestnanec z = switch(typ) {
	    			case ANALYTIK -> new DataAnalitik(meno, priezvisko, rok);
	    			case BEZPECNOST -> new BezpecnostnySpecialista(meno, priezvisko, rok);
	    		};
	    		pridajZamestnanca(z);
	    	}
	    	
	    	while(fileSc.hasNextLine()) {
	    		String[] cast = fileSc.nextLine().trim().split(":");
	    		int id1 = Integer.parseInt(cast[0]);
	    		int id2 = Integer.parseInt(cast[1]);
	    		UrovenSpoluprace uroven = UrovenSpoluprace.valueOf(cast[2].toUpperCase());
	    		pridajSpolupracu(id1, id2, uroven);
	    	}
			System.out.println("Databaza nacitana zo suboru: "+nazovSuboru);
		} catch (IOException e) {
			System.out.println("Subor neexistuje, pokracujem bez nacitania.");
		} catch (Exception e) {
			System.out.println("Chyba pri nacitani: "+e.getMessage());
		}
	}
}







