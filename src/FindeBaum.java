import javakara.JavaKaraProgram;

/* BEFEHLE:  kara.
 *   move()  turnRight()  turnLeft()
 *   putLeaf()  removeLeaf()
 *
 * SENSOREN: kara.
 *   treeFront()  treeLeft()  treeRight()
 *   mushroomFront()  onLeaf()
 */
public class FindeBaum extends JavaKaraProgram {
	PathFinder pathFinder;
 
	public void myProgram() {
		pathFinder = new PathFinder(tools, world, kara);
        pathFinder.setSuchType(PathFinderAlgo.SuchType.dijkstraSuche);
		pathFinder.start();
	}
}
