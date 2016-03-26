package javakara;

public class SuchKreuzung {
	
	Kreuzung kreuzung;
	SuchKreuzung elternKreuzung;
	int richtung;
	private double kosten;
	int luftlinie = -1;
	
	public SuchKreuzung(Kreuzung kreuzung, SuchKreuzung elternKreuzung, int richtung, double pfadKosten) {
		this.kreuzung = kreuzung;
		this.richtung = richtung;
		this.elternKreuzung = elternKreuzung;
		setKosten(pfadKosten);
		if (elternKreuzung != null) {
			addKosten(elternKreuzung.getKosten());
		}
	}
	
	public void calcLuftlinie(Kreuzung ziel) {
		luftlinie = Math.abs(kreuzung.getX()-ziel.getX()) + Math.abs(kreuzung.getY()-ziel.getY());
	}
	
	public int getLuftlinie() {
		return luftlinie;
	}

	public void setLuftlinie(int luftlinie) {
		this.luftlinie = luftlinie;
	}

	public Kreuzung getKreuzung() {
		return kreuzung;
	}
	
	public int getRichtung() {
		return richtung;
	}
	
	public void setRichtung(int richtung) {
		this.richtung = richtung;
	}
	
	public double getKosten() {
		return kosten;
	}
	
	public void addKosten(double kosten) {
		this.kosten += kosten;
	}

	public void setKosten(double kosten) {
		this.kosten = kosten;
	}	
}