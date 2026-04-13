
public class DataAnalitik extends Zamestnanec 
{

	public DataAnalitik(String meno, String priezvisko, int rokNarodenia) {
		super(meno, priezvisko, rokNarodenia);
	}

	@Override
	public TypSkupiny getTypSkupiny() {
		return TypSkupiny.ANALYTIK;
	}

	@Override
	public void pouzitZrucnost() {
			if(spoluprace.isEmpty()) {
				System.out.println(this+" nema ziadnych spolupracovnikov.");
			}
			
			Zamestnanec najlepsi = null;
			int maxSpolocnych = -1;
			
			for (Zamestnanec kolega : spoluprace.keySet()) {
				int spolocnych = 0;
				for (Zamestnanec kolegaKolegu : kolega.getSpoluprace().keySet()) {
					if (spoluprace.containsKey(kolegaKolegu) && !kolegaKolegu.equals(this)) {
						spolocnych++;
					}	
				}
				if(najlepsi == null || spolocnych > maxSpolocnych) {
					najlepsi = kolega;
					maxSpolocnych = spolocnych;
				}
			}
			System.out.println(this+" ma najviac spolocnych spoloupracovnikov s: "
							+najlepsi+"("+maxSpolocnych+")");
	}
}
