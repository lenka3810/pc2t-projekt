
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
			
	}
}
