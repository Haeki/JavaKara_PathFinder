import ch.karatojava.kapps.abstractscriptide.ScriptTools;
import javakara.JavaKaraProgram;
import javakara.JavaKaraProgram.JavaKara;
import javakara.JavaKaraProgram.JavaKaraWorld;

/* BEFEHLE:  kara.
 *   move()  turnRight()  turnLeft()
 *   putLeaf()  removeLeaf()
 *
 * SENSOREN: kara.
 *   treeFront()  treeLeft()  treeRight()
 *   mushroomFront()  onLeaf()
 */
public class PathFinderModded extends PathFinder {
    public PathFinderModded(ScriptTools tools, JavaKaraWorld world, JavaKara kara) {
		super(tools, world, kara);
	}

	@Override
    void sucheStartRichtung() {
        if(kara.kara.getDirection() == 0 || kara.kara.getDirection() == 2) {
            richtung = kara.kara.getDirection();
        } else if(kara.kara.getDirection() == 3) {
            richtung = 1;
        } else if(kara.kara.getDirection() == 1) {
            richtung = 3;
        }
    }
}
