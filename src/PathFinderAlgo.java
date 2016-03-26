import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import ch.karatojava.kapps.abstractscriptide.ScriptTools;

public class PathFinderAlgo {

	private ArrayList<SuchKreuzung> openList = new ArrayList<>();
	private ArrayList<Kreuzung> closeList = new ArrayList<>();
	private ArrayList<WegKreuzung> path = new ArrayList<>();
	Comparator<SuchKreuzung> dijkstraComparator = new DijkstraComparator();
	Comparator<SuchKreuzung> aStarComparator = new AStarComparator();
	private Kreuzung zielKreuzung;
	public int besuchteKreuzungen;
	public double gesamtkosten;
	ScriptTools tool;
	SuchType suchType; // = SuchType.breitenSuche;

	public enum SuchType {
		tiefenSuche, breitenSuche, dijkstraSuche, aStarSuche
	}

	public PathFinderAlgo(SuchType type) {
		this(null, type);
	}

	public PathFinderAlgo(ScriptTools tool) {
		this(tool, SuchType.breitenSuche);
	}

	public PathFinderAlgo(ScriptTools tool, SuchType type) {
		this.tool = tool;
		this.suchType = type;
	}

	public PathFinderAlgo() {
		this(null, SuchType.breitenSuche);
	}

	void println(String text) {
		if(tool != null) {
			tool.println(text);
		}
	}

	void showMessage(String text) {
		if(tool != null) {
			tool.showMessage(text);
		}
	}

	public void suche(Kreuzung start, Kreuzung ziel) {
		SuchKreuzung suchKreuzung = new SuchKreuzung(start, null, -1, 0);
		clearPath();
		setZielKreuzung(ziel);
		addToOpenList(suchKreuzung);
		while (!openList.isEmpty()) {
			SuchKreuzung aktuelleSuchKreuzung = removeNextFromOpenList();
			besuchteKreuzungen++;
			if (istZielKreuzung(aktuelleSuchKreuzung)) {
				println("Ziel erreicht!");
				clearPath();
				gesamtkosten = aktuelleSuchKreuzung.getKosten();
				extrahierePfadAusSuchKreuzung(aktuelleSuchKreuzung);
				return;
			}
			expandiere(aktuelleSuchKreuzung);
			println("OpenList Size: " + openList.size());
		}
	}

	public void expandiere(SuchKreuzung aSuchKreuzung) {
		Kreuzung aKreuzung = aSuchKreuzung.getKreuzung();
		Pfad[] pfade = aKreuzung.getPfade();
		//for(Pfad pfad : aKreuzung.getPfade()) {
		for(int i = 0; i < pfade.length; i++) {
			if(pfade[i] != null) {
				Kreuzung neueKreuzung = pfade[i].getAndereKreuzung(aKreuzung);
				if(!closeList.contains(neueKreuzung)) {
					addToOpenList(new SuchKreuzung(neueKreuzung, aSuchKreuzung, i, pfade[i].getLaenge()));
				}
			}
		}
	}

	public SuchKreuzung removeNextFromOpenList() {
		SuchKreuzung skn = openList.remove(0);
		closeList.add(skn.getKreuzung());
		return skn;
	}

	public void clearPath() {
		path.clear();
	}

	ArrayList<SuchKreuzung> suchKreuzungWeg = new ArrayList<>();
	public void extrahierePfadAusSuchKreuzung(SuchKreuzung suchKreuzung) {
		fillSKWegAusSuchKreuzung(suchKreuzung);
		for(int i = 0; i < suchKreuzungWeg.size(); i++) {
			SuchKreuzung sk = suchKreuzungWeg.get(i);
			int r = -1;
			try {
				r = suchKreuzungWeg.get(i+1).getRichtung();
			} catch(Exception e) {
				//e. printStackTrace();
			}
			//println("K: " + sk.getKreuzung().getX() + "|" + sk.getKreuzung().getY() + " R: " + r);
			path.add(new WegKreuzung(sk.getKreuzung(), r));
		}
	}

	public void fillSKWegAusSuchKreuzung(SuchKreuzung suchKreuzung) {
		if (suchKreuzung.elternKreuzung != null) {
			fillSKWegAusSuchKreuzung(suchKreuzung.elternKreuzung);
		}
		suchKreuzungWeg.add(suchKreuzung);
	}


	public boolean istZielKreuzung(SuchKreuzung suchKreuzung) {
		return suchKreuzung.getKreuzung().equals(zielKreuzung);
	}

	public Kreuzung getZielKreuzung() {
		return zielKreuzung;
	}

	public void setZielKreuzung(Kreuzung zielKreuzung) {
		this.zielKreuzung = zielKreuzung;
	}
	public ArrayList<SuchKreuzung> getOpenList() {
		return openList;
	}

	public ArrayList<WegKreuzung> getPath() {
		return path;
	}

	public boolean addToCloseList(Kreuzung kreuzung) {
		return closeList.add(kreuzung);

	}

	public boolean isInCloseList(SuchKreuzung suchKreuzung) {
		return closeList.contains(suchKreuzung);
	}

	public void removeFromOpenList(SuchKreuzung suchKreuzung) {
		openList.remove(suchKreuzung);
	}

	public void addToOpenList(SuchKreuzung suchKreuzung) {
		switch(suchType) {
			case breitenSuche: openList.add(suchKreuzung); break;
			case tiefenSuche: openList.add(0, suchKreuzung); break;
			case dijkstraSuche: openList.add(suchKreuzung); Collections.sort(openList, dijkstraComparator); break;
			case aStarSuche: suchKreuzung.calcLuftlinie(zielKreuzung); openList.add(suchKreuzung); Collections.sort(openList, aStarComparator); break;
		}
	}

	class DijkstraComparator implements Comparator<SuchKreuzung> {

		@Override
		public int compare(SuchKreuzung sk1, SuchKreuzung sk2) {
			if(sk1.equals(sk2)) {
				removeFromOpenList(sk2);
				return 0;
			}
			double k1 = sk1.getKosten();
			double k2 = sk2.getKosten();
			if (k1 < k2)
				return -1;
			if (k2 < k1)
				return 1;
			return 0;
		}
	}

	class AStarComparator implements Comparator<SuchKreuzung> {

		@Override
		public int compare(SuchKreuzung sk1, SuchKreuzung sk2) {
			if(sk1.equals(sk2)) {
				removeFromOpenList(sk2);
				return 0;
			}
			double k1 = sk1.getKosten() + sk1.getLuftlinie();
			double k2 = sk2.getKosten() + sk2.getLuftlinie();
			if (k1 < k2)
				return -1;
			if (k2 < k1)
				return 1;
			return 0;
		}

	}



}
