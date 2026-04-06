
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
		
	}


}
