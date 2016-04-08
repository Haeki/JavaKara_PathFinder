import javakara.JavaKaraProgram;
import javakara.JavaKaraProgram.JavaKara;
import javakara.JavaKaraProgram.JavaKaraWorld;

import java.util.HashMap;
import ch.karatojava.kapps.abstractscriptide.ScriptTools;

/* BEFEHLE:  kara.
 *   move()  turnRight()  turnLeft()
 *   putLeaf()  removeLeaf()
 *
 * SENSOREN: kara.
 *   treeFront()  treeLeft()  treeRight()
 *   mushroomFront()  onLeaf()
 */
public class PathFinder {

    HashMap<Coord ,Kreuzung> kreuzungen = new HashMap<>();
    PathFinderAlgo algo;
    Kreuzung start;
    Kreuzung ziel;
    int richtung = -1;
	  ScriptTools tools;
	  JavaKaraWorld world;
	  JavaKara kara;
    PathFinderAlgo.SuchType suchType = PathFinderAlgo.SuchType.breitenSuche;


    public PathFinder(ScriptTools tools, JavaKaraWorld world, JavaKara kara) {
    	this.tools = tools;
    	this.world = world;
    	this.kara = kara;
    }

    public void start() {
        tools.println("----------------------Starte Pfadfinder-------------------------------------");
		scanWorld();
		runAlgo();
        laufePfad();
    }
	
	public void runAlgo() {
		tools.println("------SUCHE SHORTEST PFAD:");
        algo = new PathFinderAlgo(tools, suchType);
        startTime = System.currentTimeMillis();
        algo.suche(start, ziel);
        tools.println("Pfad gefunden in: " + (System.currentTimeMillis() - startTime));
        printPfad();

        tools.showMessage("Weg gefunden");
	}
	
	public void scanWorld() {
		tools.println("------SUCHE KREUZUNGEN:");
		long startTime = System.currentTimeMillis();
        findeKreuzungen();
		tools.println("Found " + kreuzungen.size() + " Kreuzungen in: " + (System.currentTimeMillis() - startTime));
        createStartZiel();

		tools.println("------SUCHE PFADE:");
        findePfade();
		tools.println("Alle Kreuzungen und Pfade gefunden");
	}

	public void markKreuzungen() {
		for(Kreuzung k : kreuzungen.values()) {
			world.setLeaf(k.getX(), k.getY(), true);
		}
	}

	public void setSuchType(PathFinderAlgo.SuchType type) {
		suchType = type;
	}

    public void laufePfad() {
		tools.println("------LAUFE PFAD:");
        for(int i = 0; i < algo.getPath().size()-1; i++) {
            WegKreuzung wK = algo.getPath().get(i);
            Kreuzung zK = algo.getPath().get(i+1).getKreuzung();
            tools.println("Gehe von K: " + wK.getKreuzung().getX() + "|" + wK.getKreuzung().getX() + " nach " + zK.getX() + "|" + zK.getY());
            drehRichtung(wK.getRichtung());
            while(kara.getPosition().getX() != zK.getX() || kara.getPosition().getY() != zK.getY()) {
                tools.println("Pos Kara = " + kara.getPosition().getX() + "|" + kara.getPosition().getY() + " zK Pos = " + zK.getX() + "|" + zK.getY());
                if(!kara.treeFront()) {
                    kara.move();
                } else if(!kara.treeRight()) {
                    addRichtung(1);
                    kara.turnRight();
                    tools.println("Rechts Kurve");
                } else if(!kara.treeLeft()) {
                    addRichtung(-1);
                    kara.turnLeft();
                    tools.println("Links Kurve");
                }
            }
        }

    }

    public void addRichtung(int n) {
        richtung += n;
        if(richtung > 3) {
            richtung = 0;
        } else if(richtung < 0) {
            richtung = 3;
        }
    }

    public void drehRichtung(int neueRichtung) {
        if(richtung == -1) {
            sucheStartRichtung();
        }
        int drehRichtung = richtung - neueRichtung;
        if(drehRichtung == 1 || drehRichtung == -3) {
            tools.println("Links Drehung von " + richtung + " nach " + neueRichtung);
            addRichtung(-1);
            kara.turnLeft();
        } else if(drehRichtung == -1 || drehRichtung == 3) {
            tools.println("Rechts Drehung von " + richtung + " nach " + neueRichtung);
            addRichtung(1);
            kara.turnRight();
        } else if(drehRichtung == 0) {
            tools.println("Richtige Richtung: " + richtung + " " + neueRichtung);
        } else if(drehRichtung == 2 || drehRichtung == -2) {
            tools.println("Umdrehen von " + richtung + " nach " + neueRichtung);
            //Warum ??
            richtung = neueRichtung;
            kara.turnLeft();
            kara.turnLeft();
        }
    }

    void sucheStartRichtung() {
        while(kara.treeFront()) {
            kara.turnLeft();
        }
        richtung = algo.getPath().get(0).getRichtung();
    }

    public boolean isInKreuzungen(Coord pos) {
        return kreuzungen.containsKey(pos);
    }

    public boolean isInKreuzungen(int x, int y) {
        return isInKreuzungen(new Coord(x, y));
    }

    void printPfad() {
		tools.println("Pfad zum Ziel:");
        for(WegKreuzung wK : algo.getPath()) {
            tools.println("K: " + wK.getKreuzung().getX() + "|" + wK.getKreuzung().getY() + " R: " + wK.getRichtung());
        }
    }

    void createStartZiel() {
		Coord startPos = new Coord((int) kara.getPosition().getX(), (int) kara.getPosition().getY());
		start = kreuzungen.get(startPos);
		if(start == null) {
			start = new Kreuzung(startPos.x, startPos.y);
			kreuzungen.put(startPos, start);
		}

        if(ziel == null) {
			int zielX = tools.intInput("Ziel X Koordinate:");
			int zielY = tools.intInput("Ziel Y Koordinate:");
			
			if(zielX < 0 || zielX >= world.getSizeX()) {tools.showMessage("X Koordinate invalid!); zielX = 1;}
			if(zielY < 0 || zielY >= world.getSizeY()) {tools.showMessage("Y Koordinate invalid!); zielY = 1;}
            ziel = new Kreuzung(zielX, zielY);
            kreuzungen.put(new Coord(ziel.getX(), ziel.getY()), ziel);
        }
        tools.println("Start: " + start.getX() + "|" + start.getY() + " Ziel: " + ziel.getX() + "|" + ziel.getY());
    }

    void findePfade() {
        for(Kreuzung k : kreuzungen.values()) {
            createPfade(k);
        }
    }

    void createPfade(Kreuzung k) {
		try {
			if(istFeldFrei(k.getX(), k.getY()-1) && k.getPfad(0) == null) {
				k.setPfad(0, folgePfad(k, 0, -1));
			}
			if(istFeldFrei(k.getX()+1, k.getY()) && k.getPfad(1) == null) {
				k.setPfad(1, folgePfad(k, 1, 0));
			}
			if(istFeldFrei(k.getX(), k.getY()+1) && k.getPfad(2) == null) {
				k.setPfad(2, folgePfad(k, 0, 1));
			}
			if(istFeldFrei(k.getX()-1, k.getY()) && k.getPfad(3) == null) {
				k.setPfad(3, folgePfad(k, -1, 0));
			}
		} catch(Exception e) {
			tools.println("createPfade " + e.getMessage());
		}

    }

	int fixPosX(int posX) {
		if(posX < 0) {posX = world.getSizeX() - 1;}
		if(posX >= world.getSizeX()) {posX = 0;}
		return posX;
	}

	int fixPosY(int posY) {
		if(posY < 0) {posY = world.getSizeY() - 1;}
		if(posY >= world.getSizeY()) {posY = 0;}
		return posY;
	}

    Pfad folgePfad(Kreuzung k, int dirX, int dirY) {
        int laenge = 1;
        int posX = k.getX();
        int posY = k.getY();
        int breake = 0;
		posX = fixPosX(posX += dirX);
		posY = fixPosY(posY += dirY);

        while(!kreuzungen.containsKey(new Coord(posX, posY)) && breake < 800) {
            breake++;
			try {
				if(istFeldFrei(posX + dirX, posY + dirY)) {
					posX = fixPosX(posX += dirX);
					posY = fixPosY(posY += dirY);
					laenge++;
				} else {
					//tools.println("Kurve oder Sackgasse bei: " + posX + "|" + posY + " Dir = " + dirX + "|" + dirY);
					if(dirX != -1 && istFeldFrei(posX+1, posY)) {
						dirX = 1; dirY = 0;
					}
					else if(dirX != 1 && istFeldFrei(posX-1, posY)) {
						dirX = -1; dirY = 0;
					}
					else if(dirY != -1 && istFeldFrei(posX, posY+1)) {
						dirX = 0; dirY = 1;
					}
					else if(dirY != 1 && istFeldFrei(posX, posY-1)) {
						dirX = 0; dirY = -1;
					}
					else {
						return null;
					}
				}
            } catch(Exception e) {
				tools.println("folege Pfad " + e.getMessage());
			}
        }
        tools.println("Pfad gefunden: zwischen " + k.getX() + "|" + k.getY() + " und " + posX + "|" + posY + "Laenge: " + laenge);
        Kreuzung k2 = kreuzungen.get(new Coord(posX, posY));
        Pfad pfad = new Pfad(k, k2, laenge);
        if(dirY == 1) {k2.setPfad(0,pfad);}
        if(dirX == -1) {k2.setPfad(1,pfad);}
        if(dirY == -1) {k2.setPfad(2,pfad);}
        if(dirX == 1) {k2.setPfad(3,pfad);}
        return pfad;
    }

    public void findeKreuzungen() {
        for(int x = 0; x < world.getSizeX(); x++) {
            for(int y = 0; y < world.getSizeY(); y++) {
                if(isKreuzung(x,y)) {
                    kreuzungen.put(new Coord(x,y), new Kreuzung(x,y));
                } else if(world.isLeaf(x, y)) {
                    ziel = new Kreuzung(x,y);
					kreuzungen.put(new Coord(x,y), ziel);
				}

            }
        }

    }

	public boolean istFeldFrei(int x, int y) {
		try {
			if(x == -1 && (world.isEmpty(world.getSizeX()-1, y) || world.isLeaf(world.getSizeX()-1, y))) {
				return true;
			} else if(x == world.getSizeX() && (world.isEmpty(0, y) || world.isLeaf(0, y))) {
				return true;
			} else if(y == -1 && (world.isEmpty(x, world.getSizeY()-1) || world.isLeaf(x, world.getSizeY()-1))) {
				return true;
			} else if(y == world.getSizeY() && (world.isEmpty(x, 0) || world.isLeaf(x, 0))) {
				return true;
			} else if(x < -1 || y < -1 || x >= world.getSizeX()+1 || y  >= world.getSizeY()+1) {
				return false;
			} else if(world.isEmpty(x, y) || world.isLeaf(x, y) || (kara.getPosition().getX() == x && kara.getPosition().getY() == y)) {
				return true;
			}
		} catch(Exception e) {
			//tools.println("istFeldFrei " + e.getMessage());
		}
		return false;
    }



    public boolean isKreuzung(int posX, int posY) {
        int freieRichtungen = 0;
        try {
			if(world.isEmpty(posX, posY)) {
				if(istFeldFrei(posX+1, posY)) {freieRichtungen++;}
				if(istFeldFrei(posX-1, posY)) {freieRichtungen++;}
				if(istFeldFrei(posX, posY+1)) {freieRichtungen++;}
				if(istFeldFrei(posX, posY-1)) {freieRichtungen++;}
			}
		} catch(Exception e) {
			tools.println("is Kreuzung " + e.getMessage());
		}
		//tools.println("K: " + posX + "|" + posY + " freieRichtungen: " + freieRichtungen);
        return freieRichtungen > 2;
    }
}
