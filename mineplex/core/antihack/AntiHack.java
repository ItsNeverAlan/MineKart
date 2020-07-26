package mineplex.core.antihack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import mineplex.core.MiniPlugin;
import mineplex.core.antihack.types.Fly;
import mineplex.core.antihack.types.Idle;
import mineplex.core.antihack.types.Speed;
import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.portal.Portal;
import mineplex.core.punish.Punish;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiHack extends MiniPlugin
{
  public static AntiHack Instance;
  public Punish Punish;
  public Portal Portal;
  private HashMap<Player, HashMap<String, ArrayList<Long>>> _suspicion = new HashMap();
  

  private HashMap<Player, HashMap<String, ArrayList<Long>>> _offense = new HashMap();
  

  private HashMap<Player, Long> _ignore = new HashMap();
  

  private HashSet<Player> _velocityEvent = new HashSet();
  private HashMap<Player, Long> _lastMoveEvent = new HashMap();
  

  public int FloatHackTicks = 6;
  public int HoverHackTicks = 4;
  public int RiseHackTicks = 10;
  public int SpeedHackTicks = 6;
  public int IdleTime = 16000;
  

  public int FlightTriggerCancel = 2000;
  
  public ArrayList<Detector> _detectors;
  
  protected AntiHack(JavaPlugin plugin, Punish punish, Portal portal)
  {
    super("AntiHack", plugin);
    
    this.Punish = punish;
    this.Portal = portal;
    



    this._detectors = new ArrayList();
    
    this._detectors.add(new Fly(this));
    this._detectors.add(new Idle(this));
    this._detectors.add(new Speed(this));
  }
  
  public static void Initialize(JavaPlugin plugin, Punish punish, Portal portal)
  {
    Instance = new AntiHack(plugin, punish, portal);
  }
  
  @EventHandler
  public void playerMove(PlayerMoveEvent event)
  {
    this._lastMoveEvent.put(event.getPlayer(), Long.valueOf(System.currentTimeMillis()));
  }
  
  @EventHandler
  public void playerTeleport(PlayerTeleportEvent event)
  {
    this._ignore.put(event.getPlayer(), Long.valueOf(System.currentTimeMillis() + 2000L));
  }
  
  @EventHandler
  public void playerVelocity(PlayerVelocityEvent event)
  {
    this._velocityEvent.add(event.getPlayer());
  }
  
  @EventHandler
  public void playerToggleFly(PlayerToggleFlightEvent event)
  {
    Player player = event.getPlayer();
    
    if (!this._suspicion.containsKey(player)) {
      return;
    }
    Iterator<Long> offenseIterator;
    for (Iterator localIterator = ((HashMap)this._suspicion.get(player)).values().iterator(); localIterator.hasNext(); 
        


        offenseIterator.hasNext())
    {
      ArrayList<Long> offenseList = (ArrayList)localIterator.next();
      
      offenseIterator = offenseList.iterator();
      
      continue;
      
      long time = ((Long)offenseIterator.next()).longValue();
      
      if (!UtilTime.elapsed(time, this.FlightTriggerCancel)) {
        offenseIterator.remove();
      }
    }
  }
  
  @EventHandler
  public void playerQuit(PlayerQuitEvent event)
  {
    ResetAll(event.getPlayer());
  }
  
  @EventHandler
  public void startIgnore(PlayerMoveEvent event)
  {
    Player player = event.getPlayer();
    
    if (this._velocityEvent.remove(player))
    {
      setIgnore(player, 2000L);
    }
    

    if (this._lastMoveEvent.containsKey(player))
    {
      long timeBetweenPackets = System.currentTimeMillis() - ((Long)this._lastMoveEvent.get(player)).longValue();
      
      if (timeBetweenPackets > 1000L)
      {
        setIgnore(player, 2000L);
      }
    }
  }
  

  public void setIgnore(Player player, long time)
  {
    for (Detector detector : this._detectors) {
      detector.Reset(player);
    }
    
    if ((this._ignore.containsKey(player)) && (((Long)this._ignore.get(player)).longValue() > System.currentTimeMillis() + time)) {
      return;
    }
    
    this._ignore.put(player, Long.valueOf(System.currentTimeMillis() + time));
  }
  
  public boolean isValid(Player player, boolean groundValid)
  {
    if ((player.isFlying()) || (player.isInsideVehicle()) || (player.getGameMode() != GameMode.SURVIVAL))
    {
      return true;
    }
    
    if (groundValid)
    {
      if ((UtilEnt.onBlock(player)) || (player.getLocation().getBlock().getType() != Material.AIR))
      {
        return true;
      }
    }
    
    return (this._ignore.containsKey(player)) && (System.currentTimeMillis() < ((Long)this._ignore.get(player)).longValue());
  }
  



  public void addSuspicion(Player player, String type)
  {
    if (!this._suspicion.containsKey(player)) {
      this._suspicion.put(player, new HashMap());
    }
    if (!((HashMap)this._suspicion.get(player)).containsKey(type)) {
      ((HashMap)this._suspicion.get(player)).put(type, new ArrayList());
    }
    ((ArrayList)((HashMap)this._suspicion.get(player)).get(type)).add(Long.valueOf(System.currentTimeMillis()));
    

    for (Player admin : UtilServer.getPlayers()) {
      if ((admin.isOp()) && (admin.getGameMode() == GameMode.CREATIVE)) {
        UtilPlayer.message(admin, C.cGold + C.Bold + player.getName() + " suspected for " + type + ".");
      }
    }
  }
  

  @EventHandler
  public void processOffenses(UpdateEvent event)
  {
    if (event.getType() != UpdateType.SEC)
      return;
    Iterator localIterator2;
    for (Iterator localIterator1 = this._suspicion.keySet().iterator(); localIterator1.hasNext(); 
        



        localIterator2.hasNext())
    {
      Player player = (Player)localIterator1.next();
      
      if (!this._offense.containsKey(player)) {
        this._offense.put(player, new HashMap());
      }
      localIterator2 = ((HashMap)this._suspicion.get(player)).keySet().iterator(); continue;String type = (String)localIterator2.next();
      
      if (!((HashMap)this._offense.get(player)).containsKey(type)) {
        ((HashMap)this._offense.get(player)).put(type, new ArrayList());
      }
      Iterator<Long> offenseIterator = ((ArrayList)((HashMap)this._suspicion.get(player)).get(type)).iterator();
      
      while (offenseIterator.hasNext())
      {
        long time = ((Long)offenseIterator.next()).longValue();
        

        if (UtilTime.elapsed(time, this.FlightTriggerCancel))
        {
          offenseIterator.remove();
          ((ArrayList)((HashMap)this._offense.get(player)).get(type)).add(Long.valueOf(time));
        }
      }
    }
  }
  

  @EventHandler
  public void generateReports(UpdateEvent event)
  {
    if (event.getType() != UpdateType.SLOW) {
      return;
    }
    for (Player player : this._offense.keySet())
    {
      String out = "";
      int total = 0;
      
      for (String type : ((HashMap)this._offense.get(player)).keySet())
      {

        Iterator<Long> offenseIterator = ((ArrayList)((HashMap)this._suspicion.get(player)).get(type)).iterator();
        while (offenseIterator.hasNext())
        {
          long time = ((Long)offenseIterator.next()).longValue();
          
          if (UtilTime.elapsed(time, 300000L)) {
            offenseIterator.remove();
          }
        }
        
        int count = ((ArrayList)((HashMap)this._offense.get(player)).get(type)).size();
        total += count;
        
        out = out + count + " " + type + ", ";
      }
      
      if (out.length() > 0) {
        out = out.substring(0, out.length() - 2);
      }
      String severity = "";
      if (total > 30) { severity = "Extreme";
      } else if (total > 20) { severity = "High";
      } else if (total > 10) { severity = "Medium";
      } else if (total > 5) { severity = "Low";
      } else {
        return;
      }
      
      sendReport(player, out, severity);
    }
  }
  
  public void sendReport(Player player, String report, String severity)
  {
    if (severity.equals("Extreme")) {
      player.kickPlayer(C.cRed + "MAC" + C.cWhite + " - " + C.cYellow + "You were kicked for suspicious movement.");
    }
  }
  

  private void ResetAll(Player player)
  {
    this._ignore.remove(player);
    this._velocityEvent.remove(player);
    this._lastMoveEvent.remove(player);
    
    this._offense.remove(player);
    this._suspicion.remove(player);
    
    for (Detector detector : this._detectors) {
      detector.Reset(player);
    }
  }
  
  @EventHandler
  public void cleanupPlayers(UpdateEvent event) {
    if (event.getType() != UpdateType.SLOW) return;
    Iterator localIterator;
    label156:
    for (Iterator<Map.Entry<Player, Long>> playerIterator = this._ignore.entrySet().iterator(); playerIterator.hasNext(); 
        












        localIterator.hasNext())
    {
      Player player = (Player)((Map.Entry)playerIterator.next()).getKey();
      
      if ((player.isOnline()) && (!player.isDead()) && (player.isValid()))
        break label156;
      playerIterator.remove();
      
      this._velocityEvent.remove(player);
      this._lastMoveEvent.remove(player);
      
      this._offense.remove(player);
      this._suspicion.remove(player);
      
      localIterator = this._detectors.iterator(); continue;Detector detector = (Detector)localIterator.next();
      detector.Reset(player);
    }
  }
}
