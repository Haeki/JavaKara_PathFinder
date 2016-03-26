package javakara;

public class Pfad {
	
	int laenge;
	Kreuzung k1;
	Kreuzung k2;
	
	public Pfad(Kreuzung k1, Kreuzung k2, int laenge) {
		this.laenge = laenge;
		this.k1 = k1;
		this.k2 = k2;
	}
	
	public int getLaenge() {
		return laenge;
	}
	
	Kreuzung getAndereKreuzung(Kreuzung k) {
		if(k == k1) {return k2;}
		if(k == k2) {return k1;}
		return null;
	}
	
}