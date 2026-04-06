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
}








