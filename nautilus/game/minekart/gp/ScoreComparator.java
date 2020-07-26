package nautilus.game.minekart.gp;

import java.util.Comparator;
import nautilus.game.minekart.kart.Kart;

public class ScoreComparator
  implements Comparator<Kart>
{
  private GP _gp;
  
  public ScoreComparator(GP gp)
  {
    this._gp = gp;
  }
  

  public int compare(Kart kart1, Kart kart2)
  {
    if (this._gp.GetScore(kart1) > this._gp.GetScore(kart2)) {
      return -1;
    }
    if (this._gp.GetScore(kart1) == this._gp.GetScore(kart2)) {
      return 0;
    }
    return 1;
  }
}
