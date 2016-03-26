public class Coord {
	public int x;
	public int y;

	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode() {
		return 17*x + 31*y + x*y;
	}

	@Override
	public boolean equals(Object other) {
		if(other instanceof Coord) {
			if(x == ((Coord) (other)).x && y == ((Coord) (other)).y) {
				return true;
			}
		}
		return false;
	}

}
