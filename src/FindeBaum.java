import javakara.JavaKaraProgram;
import Coord;

/* BEFEHLE:  kara.
 *   move()  turnRight()  turnLeft()
 *   putLeaf()  removeLeaf()
 *
 * SENSOREN: kara.
 *   treeFront()  treeLeft()  treeRight()
 *   mushroomFront()  onLeaf()
 */
public class FindeBaum extends JavaKaraProgram {

  // hier k�nnen Sie eigene Methoden definieren

  public void myProgram() {
     Coord c = new Coord(10,20);
    // hier kommt das Hauptprogramm hin, zB:
    while (!kara.treeFront()) {
      kara.move();
    }
  }
}
