import java.util.HashMap;

public class Zamestnanec {
    private int id;
    private String meno;
    private String priezvisko;
    private int rokNarodenia;
    private Skupina skupina;
    private HashMap<Zamestnanec, UrovenSpoluprace> spoluprace;

    public Zamestnanec(int id, String meno, String priezvisko, int rokNarodenia, Skupina skupina) {
        this.id = id;
        this.meno = meno;
        this.priezvisko = priezvisko;
        this.rokNarodenia = rokNarodenia;
        this.skupina = skupina;
        this.spoluprace = new HashMap<>();
    }

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

    public Skupina getSkupina() {
        return skupina;
    }

    public HashMap<Zamestnanec, UrovenSpoluprace> getSpoluprace() {
        return spoluprace;
    }

    public void pridajSpolupracu(Zamestnanec kolega, UrovenSpoluprace uroven) {
        spoluprace.put(kolega, uroven);
    }

   public void odstranSpolupracu(Zamestnanec kolega) {
        spoluprace.remove(kolega);
    }

    public int getPocetSpoluprac() {
        return spoluprace.size();
    }

    @Override
    public String toString() {
        return id + " - " + meno + " " + priezvisko + ", nar. " + rokNarodenia;
	    }
	
}
