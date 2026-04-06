
public enum UrovenSpoluprace {
	DOBRA,
	PRIEMERNA,
	ZLA;
	
	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
