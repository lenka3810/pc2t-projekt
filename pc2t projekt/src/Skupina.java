import java.util.ArrayList;
	
public class Skupina {
    private TypSkupiny typ;
    private ArrayList<Zamestnanec> zamestnanci;

    public Skupina(TypSkupiny typ) {
        this.typ = typ;
        this.zamestnanci = new ArrayList<>();
    }

    public TypSkupiny getTyp() {
        return typ;
    }

    public ArrayList<Zamestnanec> getZamestnanci() {
        return zamestnanci;
    }

    public void pridajZamestnanca(Zamestnanec z) {
        zamestnanci.add(z);
    }

    public void odstranZamestnanca(Zamestnanec z) {
        zamestnanci.remove(z);
    }

    public int getPocetZamestnancov() {
        return zamestnanci.size();
    }

    @Override
    public String toString() {
        return "Skupina: " + typ + ", pocet zamestnancov: " + zamestnanci.size();
    }

}
