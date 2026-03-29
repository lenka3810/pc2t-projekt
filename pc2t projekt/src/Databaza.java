import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Databaza {
    private ArrayList<Zamestnanec> zamestnanci;
    private ArrayList<Skupina> skupiny;

    public Databaza() {
        this.zamestnanci = new ArrayList<>();
        this.skupiny = new ArrayList<>();
    }

    public void pridajSkupinu(Skupina skupina) {
        skupiny.add(skupina);
    }

    public void pridajZamestnanca(Zamestnanec zamestnanec) {
        zamestnanci.add(zamestnanec);
        zamestnanec.getSkupina().pridajZamestnanca(zamestnanec);
    }

    public Zamestnanec najdiZamestnancaPodlaId(int id) {
        for (Zamestnanec z : zamestnanci) {
            if (z.getId() == id) {
                return z;
            }
        }
        return null;
    }

    public void pridajSpolupracu(int id1, int id2, UrovenSpoluprace uroven) {
        Zamestnanec z1 = najdiZamestnancaPodlaId(id1);
        Zamestnanec z2 = najdiZamestnancaPodlaId(id2);

        if (z1 != null && z2 != null && z1 != z2) {
            z1.pridajSpolupracu(z2, uroven);
            z2.pridajSpolupracu(z1, uroven);
        }
    }

    public void odstranZamestnanca(int id) {
        Zamestnanec z = najdiZamestnancaPodlaId(id);

        if (z != null) {
            for (Zamestnanec iny : zamestnanci) {
                iny.odstranSpolupracu(z);
            }

            z.getSkupina().odstranZamestnanca(z);
            zamestnanci.remove(z);
        }
    }

    public void vypisVsetkychZamestnancov() {
        for (Zamestnanec z : zamestnanci) {
            System.out.println(z);
        }
    }

    public void vypisZamestnancovPodlaPriezviska() {
        ArrayList<Zamestnanec> kopia = new ArrayList<>(zamestnanci);
        Collections.sort(kopia, Comparator.comparing(Zamestnanec::getPriezvisko));

        for (Zamestnanec z : kopia) {
            System.out.println(z);
        }
    }

    public void vypisPoctyVSkupinach() {
        for (Skupina s : skupiny) {
            System.out.println(s.getTyp() + ": " + s.getPocetZamestnancov());
        }
    }
}
