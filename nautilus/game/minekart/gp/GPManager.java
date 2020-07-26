package nautilus.game.minekart.gp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import mineplex.core.MiniPlugin;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.common.util.UtilWorld;
import mineplex.core.donation.DonationManager;
import mineplex.core.portal.Portal;
import mineplex.core.recharge.Recharge;
import mineplex.core.teleport.Teleport;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.RestartServerEvent;
import mineplex.core.updater.event.UpdateEvent;
import nautilus.game.minekart.gp.command.GpCommand;
import nautilus.game.minekart.gp.command.ItemCommand;
import nautilus.game.minekart.gp.command.KartCommand;
import nautilus.game.minekart.gp.command.VoteCommand;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.KartManager;
import nautilus.game.minekart.kart.KartType;
import nautilus.game.minekart.track.Track;
import nautilus.game.minekart.track.Track.TrackState;
import nautilus.game.minekart.track.TrackManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class GPManager
  extends MiniPlugin
{
  private DonationManager _donationManager;
  private Teleport _teleport;
  private Recharge _recharge;
  public KartManager KartManager;
  public TrackManager TrackManager;
  public Portal Portal;
  private GPSet _set;
  private Location _spawn;
  private ArrayList<Player> _players = new ArrayList();
  private HashMap<Player, Boolean> _playerVote = new HashMap();
  private HashMap<Player, KartType> _kartSelect = new HashMap();
  
  private long _voteTimer = 0L;
  private int _startTimer = 0;
  

  private HashSet<GP> _live = new HashSet();
  

  private HashSet<GPResult> _results = new HashSet();
  
  public GPManager(JavaPlugin plugin, DonationManager donationManager, Teleport teleport, Recharge recharge, KartManager kartManager, TrackManager trackManager)
  {
    super("Race Manager", plugin);
    
    this._donationManager = donationManager;
    this._teleport = teleport;
    this._recharge = recharge;
    this.KartManager = kartManager;
    this.TrackManager = trackManager;
    this.Portal = new Portal(plugin);
    
    this._spawn = new Location(UtilWorld.getWorld("world"), 8.5D, 18.0D, -22.5D);
  }
  

  public void AddCommands()
  {
    AddCommand(new GpCommand(this));
    AddCommand(new ItemCommand(this));
    AddCommand(new KartCommand(this));
    AddCommand(new VoteCommand(this));
  }
  
  public void SelectKart(Player player, KartType kart)
  {
    this._kartSelect.put(player, kart);
    UtilPlayer.message(player, F.main("MK", "You selected " + F.elem(new StringBuilder(String.valueOf(kart.GetName())).append(" Kart").toString()) + "."));
  }
  
  public KartType GetSelectedKart(Player player)
  {
    if (!this._kartSelect.containsKey(player)) {
      this._kartSelect.put(player, KartType.Sheep);
    }
    return (KartType)this._kartSelect.get(player);
  }
  
  @EventHandler
  public void restartServerCheck(RestartServerEvent event)
  {
    if ((this._live.size() > 0) || (this._results.size() > 0)) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler(priority=EventPriority.MONITOR)
  public void Motd(ServerListPingEvent event) {
    if (this._live.size() > 0)
    {
      if ((this._live.iterator().next() instanceof GPBattle))
      {
        event.setMotd(ChatColor.AQUA + "In Battle...");
      }
      else
      {
        event.setMotd(ChatColor.AQUA + "In Race...");
      }
      
      event.setMaxPlayers(this._plugin.getServer().getOnlinePlayers().length);
    }
    else
    {
      event.setMotd(ChatColor.GREEN + GetSet().GetName());
      event.setMaxPlayers(10);
    }
  }
  
  @EventHandler
  public void CheckStart(UpdateEvent event)
  {
    if (event.getType() != UpdateType.SEC) {
      return;
    }
    
    if (this._players.size() >= 10)
    {
      StartGP(false);
      return;
    }
    

    int votes = 0;
    for (Iterator localIterator = this._playerVote.values().iterator(); localIterator.hasNext();) { boolean vote = ((Boolean)localIterator.next()).booleanValue();
      
      if (vote) {
        votes++;
      }
    }
    int needed = this._players.size() / 2 + this._players.size() % 2;
    
    if ((votes >= needed) && (this._players.size() >= 4))
    {
      StartGP(false);


    }
    else if (UtilTime.elapsed(this._voteTimer, 30000L))
    {
      if (this._players.size() >= 4)
      {
        Announce(F.main("MK", "Type " + F.elem(new StringBuilder(String.valueOf(C.cGreen)).append("/vote").toString()) + " to start the game with less players."));
        Announce(F.main("MK", F.elem(new StringBuilder(String.valueOf(needed - votes)).toString()) + " more votes needed..."));
      }
      else
      {
        Announce(F.main("MK", "Waiting for players..."));
      }
      

      this._voteTimer = System.currentTimeMillis();
    }
  }
  

  public void StartGP(boolean force)
  {
    if ((!force) && (this._startTimer > 0))
    {
      Announce(F.main("MK", "Starting in " + F.time(UtilTime.MakeStr(this._startTimer * 1000)) + "."));
      this._startTimer -= 1; return;
    }
    
    GP gp;
    
    GP gp;
    if (GetSet() != GPSet.Battle) gp = new GP(this, GetSet()); else {
      gp = new GPBattle(this, GetSet());
    }
    
    int added = 0;
    while (added < 10)
    {
      if (this._players.isEmpty()) {
        break;
      }
      Player player = (Player)this._players.remove(0);
      gp.AddPlayer(player, GetSelectedKart(player));
      

      this._playerVote.remove(player);
      this._kartSelect.remove(player);
      
      added++;
    }
    

    gp.SetState(GP.GPState.Live);
    gp.NextTrack();
    this._live.add(gp);
  }
  
  public void Vote(Player caller)
  {
    if (!this._playerVote.containsKey(caller)) {
      return;
    }
    boolean vote = ((Boolean)this._playerVote.get(caller)).booleanValue();
    
    this._playerVote.put(caller, Boolean.valueOf(!vote));
    
    if (!vote) Announce(F.main("MK", F.elem(caller.getName()) + " has " + F.elem(new StringBuilder(String.valueOf(C.cGreen)).append("voted").toString()) + " to start the game.")); else {
      Announce(F.main("MK", F.elem(caller.getName()) + " has " + F.elem(new StringBuilder(String.valueOf(C.cRed)).append("unvoted").toString()) + " to start the game."));
    }
  }
  
  public void Announce(String string) {
    for (Player player : this._players) {
      UtilPlayer.message(player, string);
    }
  }
  
  public GP GetGP(Player player) {
    for (GP race : this._live) {
      if (race.GetPlayers().contains(player))
        return race;
    }
    return null;
  }
  
  @EventHandler
  public void PlayerLogin(PlayerLoginEvent event)
  {
    if (this._live.size() > 0)
    {
      event.disallow(PlayerLoginEvent.Result.KICK_FULL, ChatColor.AQUA + "A race is already in progress.");
    }
  }
  
  @EventHandler
  public void PlayerJoin(PlayerJoinEvent event)
  {
    Player player = event.getPlayer();
    this._players.add(player);
    this._playerVote.put(player, Boolean.valueOf(false));
    this._startTimer = 20;
    
    player.teleport(this._spawn);
  }
  
  @EventHandler
  public void PlayerQuit(PlayerQuitEvent event)
  {
    Player player = event.getPlayer();
    

    this._kartSelect.remove(player);
    

    Kart kart = this.KartManager.GetKart(player);
    if (kart != null) {
      this.KartManager.RemoveKart(kart.GetDriver());
    }
    
    this._players.remove(player);
    this._playerVote.remove(player);
    this._kartSelect.remove(player);
    

    GP gp = GetGP(player);
    if (gp != null) {
      gp.RemovePlayer(player, kart);
    }
    
    for (GPResult result : this._results) {
      result.RemovePlayer(player);
    }
    if ((gp != null) && (gp.GetPlayers().size() == 0)) {
      this._plugin.getServer().shutdown();
    }
  }
  
  @EventHandler
  public void TeleportSpawn(EntityDamageEvent event) {
    if (event.getCause() != EntityDamageEvent.DamageCause.VOID) {
      return;
    }
    if (!this._players.contains(event.getEntity())) {
      return;
    }
    event.getEntity().teleport(this._spawn);
  }
  
  @EventHandler
  public void UpdateGPScoreboard(UpdateEvent event)
  {
    if (event.getType() != UpdateType.FASTEST) {
      return;
    }
    for (GP gp : this._live)
    {
      if ((gp.GetState() != GP.GPState.Ended) && (gp.GetTrack().GetState() != Track.TrackState.Countdown))
      {
        gp.UpdateScoreBoard();
      }
    }
  }
  
  @EventHandler
  public void UpdateGP(UpdateEvent event)
  {
    if (event.getType() != UpdateType.TICK) {
      return;
    }
    HashSet<GP> remove = new HashSet();
    
    for (GP gp : this._live)
    {
      if (gp.GetState() == GP.GPState.Ended)
      {
        remove.add(gp);
      }
      else
      {
        if (gp.GetTrack().GetState() == Track.TrackState.Countdown)
        {
          gp.GetTrack().SpawnTeleport();
        }
        
        if ((gp instanceof GPBattle)) {
          ((GPBattle)gp).CheckBattleEnd();
        }
        gp.GetTrack().Update();
      }
    }
    for (GP gp : remove)
    {
      gp.Unload();
      this._live.remove(gp);
    }
  }
  
  @EventHandler
  public void UpdateGPResult(UpdateEvent event)
  {
    if (event.getType() != UpdateType.SEC) {
      return;
    }
    HashSet<GPResult> remove = new HashSet();
    
    for (GPResult result : this._results)
    {
      if (result.End()) {
        remove.add(result);
      }
    }
    for (GPResult result : remove)
    {
      this._results.remove(result);
      this._plugin.getServer().getScheduler().scheduleSyncDelayedTask(this._plugin, new Runnable()
      {
        public void run()
        {
          GPManager.this._plugin.getServer().shutdown();
        }
      }, 100L);
    }
  }
  
  @EventHandler
  public void CreatureSpawn(CreatureSpawnEvent event)
  {
    if ((event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) || (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.DEFAULT) || (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.EGG)) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler
  public void BlockSpread(BlockSpreadEvent event) {
    event.setCancelled(true);
  }
  
  public GPResult CreateResult(GP gp)
  {
    GPResult result = new GPResult(gp, this._donationManager);
    this._results.add(result);
    return result;
  }
  
  public void DeleteResult(GPResult result)
  {
    this._results.remove(result);
  }
  
  @EventHandler
  public void HandleChat(AsyncPlayerChatEvent event)
  {
    event.setCancelled(true);
    
    final String message = event.getMessage();
    final Player sender = event.getPlayer();
    final GP senderGP = GetGP(event.getPlayer());
    

    if (senderGP != null)
    {
      this._plugin.getServer().getScheduler().scheduleSyncDelayedTask(this._plugin, new Runnable()
      {
        public void run()
        {
          senderGP.Announce(C.cYellow + sender.getName() + " " + C.cWhite + message);
        }
      }, 0L);
    }
    else
    {
      for (Player player : UtilServer.getPlayers())
      {
        if (GetGP(player) == null)
        {

          final Player recipient = player;
          
          this._plugin.getServer().getScheduler().scheduleSyncDelayedTask(this._plugin, new Runnable()
          {
            public void run()
            {
              UtilPlayer.message(recipient, C.cYellow + sender.getName() + " " + C.cWhite + message);
            }
          }, 0L);
        }
      }
    }
  }
  
  public boolean InGame(Player player) {
    return GetGP(player) != null;
  }
  
  public GPSet GetSet()
  {
    if (this._set == null)
    {
      File file = new File("GPSet.dat");
      

      if (!file.exists())
      {
        try
        {
          FileWriter fstream = new FileWriter(file);
          BufferedWriter out = new BufferedWriter(fstream);
          
          out.write("MushroomCup");
          
          out.close();
        }
        catch (Exception e)
        {
          System.out.println("Error: GP Set Write Exception");
        }
      }
      

      try
      {
        FileInputStream fstream = new FileInputStream(file);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = br.readLine();
        
        this._set = GPSet.valueOf(line);
        
        in.close();
      }
      catch (Exception e)
      {
        System.out.println("Error: GP Set Read Exception");
      }
    }
    
    if (this._set == null)
    {
      return GPSet.MushroomCup;
    }
    
    return this._set;
  }
  
  public Teleport GetTeleport()
  {
    return this._teleport;
  }
  
  public Recharge GetRecharge()
  {
    return this._recharge;
  }
}
