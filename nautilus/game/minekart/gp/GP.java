package nautilus.game.minekart.gp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.fakeEntity.FakeEntityManager;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.KartManager;
import nautilus.game.minekart.kart.KartType;
import nautilus.game.minekart.track.Track;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class GP
{
  public GPManager Manager;
  
  public static enum GPState
  {
    Recruit, 
    Live, 
    Ended;
  }
  


  private GPState _state = GPState.Recruit;
  
  private int _gpId = 0;
  
  private HashMap<Player, KartType> _players = new HashMap();
  
  private Scoreboard _scoreScoreboard;
  
  private Objective _scoreObjective;
  
  private Scoreboard _posScoreboard;
  
  private Objective _posObjective;
  private boolean _switchScoreboards;
  private GPSet _trackSet;
  private int _trackIndex = -1;
  private Track[] _trackArray = null;
  
  private int _maxKarts = 10;
  
  public GP(GPManager manager, GPSet trackSet)
  {
    this.Manager = manager;
    this._trackSet = trackSet;
    
    this._gpId = GetNewId();
    

    this._trackArray = new Track[this._trackSet.GetMapNames().length];
    for (int i = 0; i < this._trackArray.length; i++)
    {
      this._trackArray[i] = new Track(this, this.Manager.GetTeleport(), this.Manager.GetRecharge(), this._trackSet.GetMapNames()[i], i);
    }
  }
  
  public GPState GetState()
  {
    return this._state;
  }
  
  public int GetId()
  {
    return this._gpId;
  }
  
  public GPSet GetSet()
  {
    return this._trackSet;
  }
  
  public Track GetTrack()
  {
    if (this._trackIndex == -1) {
      return null;
    }
    if (this._trackIndex >= this._trackArray.length) {
      return null;
    }
    return this._trackArray[this._trackIndex];
  }
  
  public Track GetTrack(int id)
  {
    try
    {
      return this._trackArray[id];
    }
    catch (Exception e) {}
    
    return null;
  }
  

  public Track[] GetTracks()
  {
    return this._trackArray;
  }
  
  public int GetMaxKarts()
  {
    return this._maxKarts;
  }
  
  public Collection<Player> GetPlayers()
  {
    return this._players.keySet();
  }
  
  public Collection<Kart> GetKarts()
  {
    HashSet<Kart> _karts = new HashSet();
    
    for (Player player : GetPlayers())
    {
      Kart kart = this.Manager.KartManager.GetKart(player);
      
      if (kart != null) {
        _karts.add(kart);
      }
    }
    return _karts;
  }
  
  public void SetState(GPState state)
  {
    this._state = state;
  }
  
  public void NextTrack()
  {
    this._trackIndex += 1;
    
    for (Kart kart : GetKarts())
    {
      kart.ClearTrackData();
      
      kart.SetItemCycles(0);
      kart.SetItemStored(null);
    }
    
    if (this._trackIndex < this._trackArray.length)
    {
      for (Kart kart : GetKarts())
      {
        kart.SetItemStored(null);
        kart.SetItemActive(null);
        
        kart.GetDriver().eject();
        kart.GetDriver().leaveVehicle();
      }
      
      GetTrack().Initialize();
    }
    else
    {
      this._trackIndex -= 1;
      
      this.Manager.CreateResult(this);
      
      this._trackIndex += 1;
      
      for (Kart kart : GetKarts())
      {
        this.Manager.KartManager.RemoveKart(kart.GetDriver());
        FakeEntityManager.Instance.RemoveForward(kart.GetDriver());
        FakeEntityManager.Instance.RemoveFakeVehicle(kart.GetDriver(), kart.GetEntity().GetEntityId());
      }
      
      Announce(F.main("MK", "Ended Set: " + F.elem(this._trackSet.GetName())));
      
      SetState(GPState.Ended);
    }
  }
  
  public void AddPlayer(Player player, KartType type)
  {
    this._players.put(player, type);
    
    this.Manager.KartManager.AddKart(player, type, this);
  }
  
  public void RemovePlayer(Player player, Kart kart)
  {
    this._players.remove(player);
    
    if (this._scoreScoreboard != null)
    {
      this._scoreScoreboard.resetScores(Bukkit.getOfflinePlayer(org.fusesource.jansi.Ansi.Color.WHITE + player.getName()));
    }
    
    if (this._posScoreboard != null)
    {
      ChatColor col = ChatColor.YELLOW;
      if ((kart != null) && (kart.GetLap() > 3)) {
        col = ChatColor.GREEN;
      }
      this._posScoreboard.resetScores(Bukkit.getOfflinePlayer(col + player.getName()));
    }
    
    if (kart != null) {
      for (Track track : this._trackArray)
        track.RemoveKart(kart);
    }
    if ((this instanceof GPBattle)) {
      Announce(F.main("MK", player.getName() + " has left the Battle."));
    } else {
      Announce(F.main("MK", player.getName() + " has left the Grand Prix."));
    }
  }
  
  public void UpdateScoreBoard() {
    if (GetTrack() == null) {
      return;
    }
    if (GetTrack().GetState() == nautilus.game.minekart.track.Track.TrackState.Ended)
    {
      if (this._scoreScoreboard == null)
      {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        this._scoreScoreboard = manager.getNewScoreboard();
        
        this._scoreObjective = this._scoreScoreboard.registerNewObjective("showposition", "dummy");
        this._scoreObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this._scoreObjective.setDisplayName(ChatColor.AQUA + "Total Score");
        
        this._switchScoreboards = true;
      }
      
      if (this._switchScoreboards)
      {
        for (Kart kart : GetTrack().GetPositions())
        {
          if ((kart.GetDriver() != null) && (kart.GetDriver().isOnline()))
          {

            kart.GetDriver().setScoreboard(this._scoreScoreboard);
          }
        }
        this._switchScoreboards = false;
      }
      
      for (Kart kart : GetTrack().GetPositions())
      {
        if ((kart.GetDriver() != null) && (kart.GetDriver().isOnline()))
        {

          Score score = this._scoreObjective.getScore(Bukkit.getOfflinePlayer(UtilPlayer.safeNameLength(ChatColor.WHITE + kart.GetDriver().getName())));
          score.setScore(GetScore(kart));
        }
      }
    }
    else {
      if (this._posScoreboard == null)
      {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        this._posScoreboard = manager.getNewScoreboard();
        
        this._posObjective = this._posScoreboard.registerNewObjective("showposition", "dummy");
        this._posObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this._posObjective.setDisplayName(ChatColor.AQUA + "Kart Positions");
        
        this._switchScoreboards = true;
      }
      
      for (Kart kart : GetTrack().GetPositions())
      {
        if ((kart.GetDriver() != null) && (kart.GetDriver().isOnline()))
        {

          ChatColor col = ChatColor.YELLOW;
          
          if (kart.GetLap() > 3)
          {
            col = ChatColor.GREEN;
            this._posScoreboard.resetScores(Bukkit.getOfflinePlayer(UtilPlayer.safeNameLength(ChatColor.YELLOW + kart.GetDriver().getName())));
          }
          else
          {
            this._posScoreboard.resetScores(Bukkit.getOfflinePlayer(UtilPlayer.safeNameLength(ChatColor.GREEN + kart.GetDriver().getName())));
          }
          
          Score score = this._posObjective.getScore(Bukkit.getOfflinePlayer(UtilPlayer.safeNameLength(col + kart.GetDriver().getName())));
          score.setScore(kart.GetLapPlace() + 1);
        }
      }
      if (this._switchScoreboards)
      {
        for (Kart kart : GetTrack().GetPositions())
        {
          if ((kart.GetDriver() != null) && (kart.GetDriver().isOnline()))
          {


            kart.GetDriver().setScoreboard(this._posScoreboard);
          }
        }
        this._switchScoreboards = false;
      }
    }
  }
  
  public int GetScore(Kart kart)
  {
    int score = 0;
    
    for (Track track : this._trackArray) {
      for (int i = 0; i < track.GetPositions().size(); i++) {
        if (((Kart)track.GetPositions().get(i)).equals(kart))
        {
          score += Math.max(1, 10 - i * 2); }
      }
    }
    return score;
  }
  
  public int GetNewId()
  {
    File file = new File("TrackId.dat");
    

    if (!file.exists())
    {
      try
      {
        FileWriter fstream = new FileWriter(file);
        BufferedWriter out = new BufferedWriter(fstream);
        
        out.write("0");
        
        out.close();
      }
      catch (Exception e)
      {
        System.out.println("Error: Track GetId Write Exception");
      }
    }
    
    int id = 0;
    

    try
    {
      FileInputStream fstream = new FileInputStream(file);
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new java.io.InputStreamReader(in));
      String line = br.readLine();
      
      id = Integer.parseInt(line);
      
      in.close();
    }
    catch (Exception e)
    {
      System.out.println("Error: Track GetId Read Exception");
      id = 0;
    }
    
    try
    {
      FileWriter fstream = new FileWriter(file);
      BufferedWriter out = new BufferedWriter(fstream);
      
      out.write(id + 1);
      
      out.close();
    }
    catch (Exception e)
    {
      System.out.println("Error: Track GetId Re-Write Exception");
    }
    
    return id;
  }
  
  public void Announce(String string)
  {
    for (Player player : GetPlayers()) {
      UtilPlayer.message(player, string);
    }
  }
  
  public void Unload() {
    for (Track track : this._trackArray)
    {
      track.GetPositions().clear();
      track.GetScores().clear();
    }
  }
  
  public void SwitchScoreboards()
  {
    this._switchScoreboards = true;
  }
}
