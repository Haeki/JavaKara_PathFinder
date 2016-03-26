package javakara;

public class WegKreuzung {
	Kreuzung kreuzung;
	int richtung;
	
	public WegKreuzung(Kreuzung kreuzung, int richtung) {
		this.kreuzung = kreuzung;
		this.richtung = richtung;
	}
	
	public Kreuzung getKreuzung() {
		return kreuzung;
	}
	
	public int getRichtung() {
		return richtung;
	}
	
	
}