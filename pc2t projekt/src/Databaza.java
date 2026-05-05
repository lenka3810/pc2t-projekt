import java.io.*;
import java.util.*;
import java.sql.*;

public class Databaza {
    private ArrayList<Zamestnanec> zamestnanci;
    private Connection conn;

    public Databaza() {
        this.zamestnanci = new ArrayList<>();
        
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:databaza.db");
            vytvorTabulky();
        } catch (SQLException e) {
            System.out.println("Chyba pripojenia k DB: " + e.getMessage());
        }
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
	
	private void vytvorTabulky() throws SQLException {
        Statement st = conn.createStatement();

        st.executeUpdate(
            "CREATE TABLE IF NOT EXISTS zamestnanec (" +
            "id INTEGER PRIMARY KEY, " +
            "meno TEXT, " +
            "priezvisko TEXT, " +
            "rok INTEGER, " +
            "typ TEXT)"
        );

        st.executeUpdate(
            "CREATE TABLE IF NOT EXISTS spolupraca (" +
            "id1 INTEGER, " +
            "id2 INTEGER, " +
            "uroven TEXT)"
        );
    }
	
	public void ulozenieDoSQL() {
	    try {
	        Statement st = conn.createStatement();

	        st.executeUpdate("DELETE FROM zamestnanec");
	        st.executeUpdate("DELETE FROM spolupraca");

	        PreparedStatement ps = conn.prepareStatement(
	            "INSERT INTO zamestnanec VALUES (?, ?, ?, ?, ?)"
	        );

	        for (Zamestnanec z : zamestnanci) {
	            ps.setInt(1, z.getId());
	            ps.setString(2, z.getMeno());
	            ps.setString(3, z.getPriezvisko());
	            ps.setInt(4, z.getRokNarodenia());
	            ps.setString(5, z.getTypSkupiny().toString());
	            ps.executeUpdate();
	        }

	        PreparedStatement ps2 = conn.prepareStatement(
	            "INSERT INTO spolupraca VALUES (?, ?, ?)"
	        );

	        for (Zamestnanec z : zamestnanci) {
	            for (Map.Entry<Zamestnanec, UrovenSpoluprace> e : z.getSpoluprace().entrySet()) {
	                if (z.getId() < e.getKey().getId()) {
	                    ps2.setInt(1, z.getId());
	                    ps2.setInt(2, e.getKey().getId());
	                    ps2.setString(3, e.getValue().toString());
	                    ps2.executeUpdate();
	                }
	            }
	        }

	        System.out.println("Uložené do SQL databázy.");

	    } catch (SQLException e) {
	        System.out.println("Chyba pri ukladaní: " + e.getMessage());
	    }
	}
	
	public void nacitanieZoSQL() {
	    try {
	        zamestnanci.clear();

	        Statement st = conn.createStatement();
	        ResultSet rs = st.executeQuery("SELECT * FROM zamestnanec");

	        while (rs.next()) {
	        	int id = rs.getInt("id");
	        	
	            String typ = rs.getString("typ");
	            String meno = rs.getString("meno");
	            String priezvisko = rs.getString("priezvisko");
	            int rok = rs.getInt("rok");

	            Zamestnanec z;
	            if (typ.equals("ANALYTIK")) {
	                z = new DataAnalitik(meno, priezvisko, rok);
	            } else {
	                z = new BezpecnostnySpecialista(meno, priezvisko, rok);
	            }

	            z.setId(id);
	            
	            zamestnanci.add(z);
	        }

	        rs = st.executeQuery("SELECT * FROM spolupraca");

	        while (rs.next()) {
	            int id1 = rs.getInt("id1");
	            int id2 = rs.getInt("id2");
	            UrovenSpoluprace u = UrovenSpoluprace.valueOf(rs.getString("uroven").toUpperCase());

	            pridajSpolupracu(id1, id2, u);
	        }
	        
	        int maxId = 0;
	        for (Zamestnanec z : zamestnanci) {
	            if (z.getId() > maxId) {
	                maxId = z.getId();
	            }
	        }
	        Zamestnanec.nastavDalsieId(maxId + 1);

	        System.out.println("Načítané zo SQL databázy.");

	    } catch (SQLException e) {
	        System.out.println("Chyba pri načítaní: " + e.getMessage());
	    }
	}
	
	
}







