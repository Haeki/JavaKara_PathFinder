public class Kreuzung {

	int posX;
	int posY;
	Pfad[] pfade = new Pfad[4];

	public Kreuzung(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}

	public int getX() {return posX;}
	public int getY() {return posY;}

	public void setY(int y) {posY = y;}
	public void setX(int x) {posX = x;}

	public void setPfad(int index, Pfad pfad) {
		pfade[index] = pfad;
	}

	public Pfad[] getPfade() {
		return pfade;
	}

	public Pfad getPfad(int index) {
		if(index >= 0 && index < 4) {
			return pfade[index];
		}
		return null;
	}

}
