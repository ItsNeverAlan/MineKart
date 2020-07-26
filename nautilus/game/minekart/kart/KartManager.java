package nautilus.game.minekart.kart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import mineplex.core.MiniPlugin;
import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import nautilus.game.minekart.gp.GP;
import nautilus.game.minekart.item.KartItemManager;
import nautilus.game.minekart.kart.condition.Condition;
import nautilus.game.minekart.kart.condition.ConditionData;
import nautilus.game.minekart.kart.condition.ConditionType;
import nautilus.game.minekart.kart.control.Collision;
import nautilus.game.minekart.kart.control.Drive;
import nautilus.game.minekart.kart.control.DriveDrift;
import nautilus.game.minekart.kart.crash.Crash;
import nautilus.game.minekart.track.Track;
import nautilus.game.minekart.track.Track.TrackState;
import net.minecraft.server.v1_6_R3.EntityPlayer;
import net.minecraft.server.v1_6_R3.MathHelper;
import net.minecraft.server.v1_6_R3.Packet;
import net.minecraft.server.v1_6_R3.Packet34EntityTeleport;
import net.minecraft.server.v1_6_R3.PlayerConnection;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class KartManager
  extends MiniPlugin
{
  private Recharge _recharge;
  public KartItemManager ItemManager;
  private HashMap<Player, Kart> _karts = new HashMap();
  
  public KartManager(JavaPlugin plugin, Recharge recharge)
  {
    super("Kart Manager", plugin);
    
    this._recharge = recharge;
    this.ItemManager = new KartItemManager(plugin, this);
  }
  
  @EventHandler
  public void KartUpdate(UpdateEvent event)
  {
    if (event.getType() == UpdateType.FASTER)
    {
      for (Kart kart : GetKarts().values())
      {
        Condition.WolfHeart(kart);
        Condition.SuperMushroom(kart);
      }
    }
    
    if (event.getType() == UpdateType.FAST)
    {
      for (Kart kart : GetKarts().values())
      {
        Location location = kart.GetDriver().getLocation();
        Packet teleportPacket = new Packet34EntityTeleport(
          kart.GetDriver().getEntityId(), 
          MathHelper.floor(location.getX() * 32.0D), 
          MathHelper.floor(location.getY() * 32.0D), 
          MathHelper.floor(location.getZ() * 32.0D), 
          (byte)MathHelper.d(location.getYaw() * 256.0F / 360.0F), 
          (byte)MathHelper.d(location.getPitch() * 256.0F / 360.0F));
        
        for (Kart otherPlayer : kart.GetGP().GetKarts())
        {
          if (kart.GetGP().GetTrack().GetState() != Track.TrackState.Live) {
            break;
          }
          if (otherPlayer.GetDriver() != kart.GetDriver())
          {

            EntityPlayer entityPlayer = ((CraftPlayer)otherPlayer
              .GetDriver()).getHandle();
            
            entityPlayer.playerConnection.sendPacket(teleportPacket);
          }
        }
      }
    }
    if (event.getType() == UpdateType.TICK)
    {
      for (Kart kart : GetKarts().values())
      {

        nautilus.game.minekart.kart.control.World.Gravity(kart);
        

        nautilus.game.minekart.kart.control.World.AirDrag(kart);
        nautilus.game.minekart.kart.control.World.FireDrag(kart);
        nautilus.game.minekart.kart.control.World.RoadDrag(kart);
        nautilus.game.minekart.kart.control.World.BlockDrag(kart);
        

        Condition.Boost(kart);
        Condition.LightningSlow(kart);
        Condition.StarEffect(kart);
        Condition.StarCollide(kart);
        Condition.BlazeFire(kart);
        

        Collision.CollideBlock(kart);
        Collision.CollidePlayer(kart);
        

        if (kart.GetKartState() == KartState.Drive)
        {

          Drive.Accelerate(kart);
          Drive.Brake(kart);
          

          if (kart.GetDrift() == Kart.DriftDirection.None) {
            Drive.Turn(kart);
          } else {
            DriveDrift.Drift(kart);
          }
          
          Drive.TopSpeed(kart);
          

          Drive.Move(kart);
          
          kart.GetDriver()
            .getWorld()
            .playSound(kart.GetDriver().getLocation(), 
            Sound.PIG_IDLE, 0.15F - (float)kart.GetSpeed() / 10.0F, 
            0.5F + (float)kart.GetSpeed());
        }
        if (kart.GetKartState() == KartState.Crash)
        {
          Crash crash = kart.GetCrash();
          
          if ((crash == null) || (
            (crash.CrashEnd()) && (KartUtil.IsGrounded(kart))))
          {

            kart.SetKartState(KartState.Drive);
            

            if ((crash == null) || (crash.StabilityRestore())) {
              kart.GetDriver().setFoodLevel(20);
            }
          }
          else
          {
            crash.Move(kart);
          }
        } else if (kart.GetKartState() == KartState.Lakitu)
        {
          if (UtilTime.elapsed(kart.GetKartStateTime(), 8000L))
          {
            kart.SetKartState(KartState.Drive);
          }
          else {
            kart.GetDriver().playSound(
              kart.GetDriver().getLocation(), 
              Sound.NOTE_BASS, 0.3F, 0.1F);
          }
          

          Drive.Move(kart);
        }
      }
    }
  }
  
  @EventHandler
  public void ConditionExpire(UpdateEvent event)
  {
    if (event.getType() != UpdateType.TICK) {
      return;
    }
    for (Iterator localIterator1 = GetKarts().values().iterator(); localIterator1.hasNext(); 
        








        ???.hasNext())
    {
      Kart kart = (Kart)localIterator1.next();
      
      HashSet<ConditionData> remove = new HashSet();
      
      for (ConditionData data : kart.GetConditions())
      {
        if (data.IsExpired()) {
          remove.add(data);
        }
      }
      ??? = remove.iterator(); continue;ConditionData data = (ConditionData)???.next();
      
      kart.GetConditions().remove(data);
      
      if ((data.IsCondition(ConditionType.Star)) || 
        (data.IsCondition(ConditionType.Ghost)))
      {
        kart.SetPlayerArmor();
      }
      
      if (data.IsCondition(ConditionType.SuperMushroom))
      {
        kart.SetItemStored(null);
      }
    }
  }
  

  @EventHandler
  public void StabilityRecover(UpdateEvent event)
  {
    if (event.getType() != UpdateType.SEC) {
      return;
    }
    for (Kart kart : GetKarts().values()) {
      UtilPlayer.hunger(kart.GetDriver(), 1);
    }
  }
  
  @EventHandler
  public void DriftHop(PlayerToggleSneakEvent event) {
    if (this._recharge.use(event.getPlayer(), "Drift Hop", 250L, false)) {
      DriveDrift.Hop(GetKart(event.getPlayer()), event);
    }
  }
  
  @EventHandler(priority=EventPriority.LOWEST)
  public void Damage(EntityDamageEvent event) {
    event.setCancelled(true);
  }
  
  @EventHandler(priority=EventPriority.LOWEST)
  public void Damage(EntityRegainHealthEvent event)
  {
    if (event.getRegainReason() != EntityRegainHealthEvent.RegainReason.CUSTOM) {
      event.setCancelled(true);
    }
  }
  
  public HashMap<Player, Kart> GetKarts() {
    return this._karts;
  }
  
  public Kart GetKart(Player player)
  {
    return (Kart)this._karts.get(player);
  }
  
  public void AddKart(Player player, KartType type, GP gp)
  {
    RemoveKart(player);
    
    this._karts.put(player, new Kart(player, type, gp));
  }
  
  public void RemoveKart(Player player)
  {
    this._karts.remove(player);
    UtilInv.Clear(player);
  }
}
