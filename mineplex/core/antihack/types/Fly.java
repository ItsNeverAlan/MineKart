package mineplex.core.antihack.types;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map.Entry;
import mineplex.core.MiniPlugin;
import mineplex.core.antihack.AntiHack;
import mineplex.core.antihack.Detector;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilMath;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class Fly
  extends MiniPlugin
  implements Detector
{
  private AntiHack Host;
  private HashMap<Player, Map.Entry<Integer, Double>> _floatTicks = new HashMap();
  private HashMap<Player, Map.Entry<Integer, Double>> _hoverTicks = new HashMap();
  private HashMap<Player, Map.Entry<Integer, Double>> _riseTicks = new HashMap();
  private HashMap<Player, Vector> _momentum = new HashMap();
  
  public Fly(AntiHack host)
  {
    super("Fly Detector", host.GetPlugin());
    this.Host = host;
  }
  
  @EventHandler(priority=EventPriority.MONITOR)
  public void updateFlyhack(PlayerMoveEvent event)
  {
    Player player = event.getPlayer();
    

    if (this.Host.isValid(player, true)) {
      return;
    }
    
    if (UtilMath.offset(event.getFrom(), event.getTo()) <= 0.0D)
    {
      updateFloat(player);
      return;
    }
    
    updateHover(player);
    updateRise(player);
    updateMomentum(player, event);
  }
  
  private void updateFloat(Player player)
  {
    int count = 0;
    
    if (this._floatTicks.containsKey(player))
    {
      if (player.getLocation().getY() == ((Double)((Map.Entry)this._floatTicks.get(player)).getValue()).doubleValue())
      {
        count = ((Integer)((Map.Entry)this._floatTicks.get(player)).getKey()).intValue() + 1;
      }
      else
      {
        count = 0;
      }
    }
    
    if (count > this.Host.FloatHackTicks)
    {
      this.Host.addSuspicion(player, "Fly (Float)");
      count = 0;
    }
    
    this._floatTicks.put(player, new AbstractMap.SimpleEntry(Integer.valueOf(count), Double.valueOf(player.getLocation().getY())));
  }
  
  private void updateHover(Player player)
  {
    int count = 0;
    
    if (this._hoverTicks.containsKey(player))
    {
      if (player.getLocation().getY() == ((Double)((Map.Entry)this._hoverTicks.get(player)).getValue()).doubleValue())
      {
        count = ((Integer)((Map.Entry)this._hoverTicks.get(player)).getKey()).intValue() + 1;
      }
      else
      {
        count = 0;
      }
    }
    
    if (count > this.Host.HoverHackTicks)
    {
      this.Host.addSuspicion(player, "Fly (Hover)");
      count = 0;
    }
    
    this._hoverTicks.put(player, new AbstractMap.SimpleEntry(Integer.valueOf(count), Double.valueOf(player.getLocation().getY())));
  }
  
  private void updateRise(Player player)
  {
    int count = 0;
    
    if (this._riseTicks.containsKey(player))
    {
      if (player.getLocation().getY() > ((Double)((Map.Entry)this._riseTicks.get(player)).getValue()).doubleValue())
      {
        count = ((Integer)((Map.Entry)this._riseTicks.get(player)).getKey()).intValue() + 1;
      }
      else
      {
        count = 0;
      }
    }
    
    if (count > this.Host.RiseHackTicks)
    {
      this.Host.addSuspicion(player, "Fly (Rise)");
      count = 0;
    }
    
    this._riseTicks.put(player, new AbstractMap.SimpleEntry(Integer.valueOf(count), Double.valueOf(player.getLocation().getY())));
  }
  
  private void updateMomentum(Player player, PlayerMoveEvent event)
  {
    if (!this._momentum.containsKey(player)) {
      this._momentum.put(player, UtilAlg.getTrajectory2d(event.getFrom(), event.getTo()));
    }
    Vector newVel = UtilAlg.getTrajectory2d(event.getFrom(), event.getTo());
    Vector oldVel = (Vector)this._momentum.get(player);
    

    if ((oldVel.subtract(newVel).length() > 1.42D) && (UtilMath.offset2d(event.getFrom(), event.getTo()) > 0.4D))
    {
      this.Host.addSuspicion(player, "Fly (Momentum)");
    }
    
    this._momentum.put(player, newVel);
  }
  

  public void Reset(Player player)
  {
    this._floatTicks.remove(player);
    this._hoverTicks.remove(player);
    this._riseTicks.remove(player);
    this._momentum.remove(player);
  }
}
