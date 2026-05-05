import java.util.*;

public abstract class Zamestnanec 
{
    private int id;
    private int rokNarodenia;
    private String meno;
    private String priezvisko;
    
    private static int nasledujuceId = 1;
    
    public HashMap<Zamestnanec, UrovenSpoluprace> spoluprace;

    public Zamestnanec(String meno, String priezvisko, int rokNarodenia) {
        this.id = nasledujuceId++;
        this.meno = meno;
        this.priezvisko = priezvisko;
        this.rokNarodenia = rokNarodenia;
        this.spoluprace = new HashMap<>();
    }

    public abstract void pouzitZrucnost();
    public abstract TypSkupiny getTypSkupiny();
    
    public int getId() {
        return id;
    }

    public String getMeno() {
        return meno;
    }

    public String getPriezvisko() {
        return priezvisko;
    }

    public int getRokNarodenia() {
        return rokNarodenia;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public static void nastavDalsieId(int id) {
        nasledujuceId = id;
    }

    public HashMap<Zamestnanec, UrovenSpoluprace> getSpoluprace() {
        return spoluprace;
    }

    public void pridajSpolupracu(Zamestnanec zamestnanec, UrovenSpoluprace urovenSpoluprace) {
        spoluprace.put(zamestnanec, urovenSpoluprace);
    }

   public void odstranSpolupracu(Zamestnanec zamestnanec) {
        spoluprace.remove(zamestnanec);
        
    }

    public int getPocetSpoluprac() {
        return spoluprace.size();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Zamestnanec)) return false;
        return id == ((Zamestnanec) o).id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
    	return String.format("[%d] %s %s (nar. %d)", id, meno, priezvisko, rokNarodenia);
	    }
	
}
