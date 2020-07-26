package nautilus.game.minekart.gp;

import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.track.Track;
import nautilus.game.minekart.track.Track.TrackState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class GPBattle extends GP
{
  public GPBattle(GPManager manager, GPSet trackSet)
  {
    super(manager, trackSet);
  }
  

  public void UpdateScoreBoard()
  {
    if (GetTrack() == null) {
      return;
    }
    if (GetTrack().GetState() == Track.TrackState.Ended)
    {
      ScoreboardManager manager = Bukkit.getScoreboardManager();
      Scoreboard board = manager.getNewScoreboard();
      
      Objective objective = board.registerNewObjective("showposition", "dummy");
      objective.setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);
      objective.setDisplayName(ChatColor.AQUA + "Total Score");
      
      for (Kart kart : GetKarts())
      {
        if ((kart.GetDriver() != null) && (kart.GetDriver().isOnline()))
        {

          ChatColor col = ChatColor.GRAY;
          if (kart.GetLives() == 3) { col = ChatColor.GREEN;
          } else if (kart.GetLives() == 2) { col = ChatColor.YELLOW;
          } else if (kart.GetLives() == 1) { col = ChatColor.RED;
          }
          String name = col + kart.GetDriver().getName();
          if (name.length() > 16) {
            name = name.substring(0, 16);
          }
          Score score = objective.getScore(Bukkit.getOfflinePlayer(name));
          score.setScore(GetScore(kart));
        }
      }
      for (Kart kart : GetKarts())
      {
        if ((kart.GetDriver() != null) && (kart.GetDriver().isOnline()))
        {

          kart.GetDriver().setScoreboard(board);
        }
      }
    }
    else {
      ScoreboardManager manager = Bukkit.getScoreboardManager();
      Scoreboard board = manager.getNewScoreboard();
      
      Objective objective = board.registerNewObjective("showposition", "dummy");
      objective.setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);
      objective.setDisplayName(ChatColor.AQUA + "Kart Balloons");
      
      for (Kart kart : GetKarts())
      {
        if ((kart.GetDriver() != null) && (kart.GetDriver().isOnline()))
        {

          ChatColor col = ChatColor.GRAY;
          if (kart.GetLives() == 3) { col = ChatColor.GREEN;
          } else if (kart.GetLives() == 2) { col = ChatColor.YELLOW;
          } else if (kart.GetLives() == 1) { col = ChatColor.RED;
          }
          String name = col + kart.GetDriver().getName();
          if (name.length() > 16) {
            name = name.substring(0, 16);
          }
          Score score = objective.getScore(Bukkit.getOfflinePlayer(name));
          score.setScore(kart.GetLives());
        }
      }
      for (Kart kart : GetKarts())
      {
        if ((kart.GetDriver() != null) && (kart.GetDriver().isOnline()))
        {

          kart.GetDriver().setScoreboard(board);
        }
      }
    }
  }
  
  public void CheckBattleEnd() {
    if ((GetTrack().GetState() == Track.TrackState.Loading) || (GetTrack().GetState() == Track.TrackState.Countdown)) {
      return;
    }
    int alive = 0;
    Kart winner = null;
    for (Kart kart : GetKarts())
    {
      if (kart.GetLives() > 0)
      {
        alive++;
        winner = kart;
      }
    }
    
    if (alive > 1) {
      return;
    }
    if (GetTrack().GetState() != Track.TrackState.Ended)
    {
      GetTrack().GetPositions().add(0, winner);
      GetTrack().SetState(Track.TrackState.Ended);
    }
  }
}
