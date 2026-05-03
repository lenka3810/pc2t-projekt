
public class BezpecnostnySpecialista extends Zamestnanec {

	public BezpecnostnySpecialista(String meno, String priezvisko, int rokNarodenia) {
		super(meno, priezvisko, rokNarodenia);
	}
	
	@Override
	public TypSkupiny getTypSkupiny() {
		return TypSkupiny.BEZPECNOST;
	}
	
	@Override
	public void pouzitZrucnost() {
			if(spoluprace.isEmpty()) {
				System.out.println(this+" nema ziadnych spolupracovnikov.");
				return;
			}
			
			int pocet = spoluprace.size();
			double sucet = 0.0;
			
			for (UrovenSpoluprace u : spoluprace.values()) {
				if (u == UrovenSpoluprace.DOBRA) sucet += 1;
				else if (u == UrovenSpoluprace.PRIEMERNA) sucet += 2;
				else sucet += 3;
			}
			
			double priemer = sucet/pocet;
			double skore = pocet*priemer;
			
			System.out.println(this+" rizikove skore: "+ String.format("%.2f", skore));
	}
}
