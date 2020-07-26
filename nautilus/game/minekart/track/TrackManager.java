package nautilus.game.minekart.track;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import mineplex.core.MiniPlugin;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.fakeEntity.FakeEntity;
import mineplex.core.fakeEntity.FakeEntityManager;
import mineplex.core.teleport.Teleport;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import nautilus.game.minekart.gp.GP;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.KartState;
import net.minecraft.server.v1_6_R3.ChunkPreLoadEvent;
import net.minecraft.server.v1_6_R3.EntityPlayer;
import net.minecraft.server.v1_6_R3.Packet;
import net.minecraft.server.v1_6_R3.PlayerConnection;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public class TrackManager extends MiniPlugin
{
  private Teleport _teleport;
  private HashSet<Track> _tracks = new HashSet();
  private HashSet<Track> _trackLoader = new HashSet();
  
  public TrackManager(JavaPlugin plugin, Teleport teleport)
  {
    super("Track Manager", plugin);
    
    this._teleport = teleport;
  }
  
  public Set<Track> GetTracks()
  {
    return this._tracks;
  }
  
  public void RegisterTrack(Track track)
  {
    this._tracks.add(track);
  }
  
  public void LoadTrack(Track track)
  {
    this._trackLoader.add(track);
  }
  
  @EventHandler
  public void LoadTrackUpdate(UpdateEvent event)
  {
    if (event.getType() != UpdateType.TICK) {
      return;
    }
    Iterator<Track> trackIterator = this._trackLoader.iterator();
    
    long endTime = System.currentTimeMillis() + 25L;
    
    while (trackIterator.hasNext())
    {
      long timeLeft = endTime - System.currentTimeMillis();
      if (timeLeft > 0L)
      {
        final Track track = (Track)trackIterator.next();
        
        if (track.GetWorld() == null)
        {
          trackIterator.remove();
        }
        else if (track.LoadChunks(timeLeft))
        {
          trackIterator.remove();
          track.SetState(Track.TrackState.Countdown);
          track.GetGP().Announce(F.main("MK", "Starting Track: " + F.elem(track.GetName())));
          
          track.SpawnTeleport();
          Iterator localIterator2;
          for (Iterator localIterator1 = track.GetGP().GetKarts().iterator(); localIterator1.hasNext(); 
              





              localIterator2.hasNext())
          {
            Kart kart = (Kart)localIterator1.next();
            
            kart.GetEntity().SetLocation(kart.GetDriver().getLocation());
            
            Packet spawnPacket = kart.GetEntity().Spawn();
            Packet attachPacket = kart.GetEntity().SetPassenger(kart.GetDriver().getEntityId());
            
            localIterator2 = track.GetGP().GetKarts().iterator(); continue;Kart otherPlayer = (Kart)localIterator2.next();
            
            if (kart != otherPlayer)
            {

              kart.GetDriver().hidePlayer(otherPlayer.GetDriver());
              
              EntityPlayer entityPlayer = ((CraftPlayer)otherPlayer.GetDriver()).getHandle();
              
              entityPlayer.playerConnection.sendPacket(spawnPacket);
              FakeEntityManager.Instance.ForwardMovement(otherPlayer.GetDriver(), kart.GetDriver(), kart.GetEntity().GetEntityId());
              FakeEntityManager.Instance.FakeVehicle(otherPlayer.GetDriver(), kart.GetDriver().getEntityId(), attachPacket);
            }
          }
          
          org.bukkit.Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(GetPlugin(), new Runnable()
          {
            public void run()
            {
              for (Kart kart : track.GetGP().GetKarts())
              {
                for (Kart player : track.GetGP().GetKarts())
                {
                  if (kart.GetDriver() != player)
                  {

                    kart.GetDriver().showPlayer(player.GetDriver());
                  }
                }
                kart.Equip();
              }
            }
          }, 5L);
        }
      }
    }
  }
  
  @EventHandler
  public void DeleteTrack(UpdateEvent event) {
    if (event.getType() == UpdateType.TICK) {
      for (Track track : this._tracks)
        if ((track.GetWorld() != null) && 
          (track.GetState() == Track.TrackState.Ended) && 
          (track.GetWorld().getPlayers().isEmpty()))
          track.Uninitialize();
    }
  }
  
  @EventHandler
  public void Update(UpdateEvent event) {
    if (event.getType() == UpdateType.TICK) {
      for (Track track : this._tracks)
        if ((!(track.GetGP() instanceof nautilus.game.minekart.gp.GPBattle)) && 
          (track.GetWorld() != null))
          TrackLogic.Positions(track);
    }
    if (event.getType() == UpdateType.TICK) {
      for (Track track : this._tracks)
        if (track.GetWorld() != null)
          TrackLogic.Jump(track);
    }
    if (event.getType() == UpdateType.FAST) {
      for (Track track : this._tracks)
        if (track.GetWorld() != null)
          TrackLogic.UpdateItems(track);
    }
    if (event.getType() == UpdateType.TICK) {
      for (Track track : this._tracks)
        if (track.GetWorld() != null)
          TrackLogic.UpdateEntities(track);
    }
    if (event.getType() == UpdateType.TICK) {
      for (Track track : this._tracks)
        if (track.GetWorld() != null)
          TrackLogic.PickupItem(track);
    }
    if (event.getType() == UpdateType.TICK) {
      for (Track track : this._tracks)
        if (track.GetWorld() != null)
          TrackLogic.CollideMob(track);
    }
  }
  
  @EventHandler
  public void BlockIgnite(BlockIgniteEvent event) {
    if (event.getCause() == org.bukkit.event.block.BlockIgniteEvent.IgniteCause.ENDER_CRYSTAL) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler
  public void PlayerIgnite(EntityCombustEvent event) {
    if (event.getDuration() < 100) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler
  public void ChunkUnload(ChunkUnloadEvent event) {
    for (Track track : this._tracks) {
      track.ChunkUnload(event);
    }
  }
  
  @EventHandler
  public void ChunkLoad(ChunkPreLoadEvent event) {
    for (Track track : this._tracks)
    {
      track.ChunkLoad(event);
    }
  }
  
  @EventHandler(priority=org.bukkit.event.EventPriority.LOWEST)
  public void Lakitu(UpdateEvent event)
  {
    if (event.getType() != UpdateType.FAST) {
      return;
    }
    for (Track track : this._tracks)
    {
      if (track.GetWorld() != null)
      {

        if (track.GetState() == Track.TrackState.Live)
        {

          for (Kart kart : track.GetGP().GetKarts())
          {
            kart.SetLakituTick(-1);
            
            if (kart.GetKartState() != KartState.Lakitu)
            {


              int locId = -1;
              double bestDist = 9999.0D;
              
              for (int i = 0; i < kart.GetGP().GetTrack().GetReturn().size(); i++)
              {
                Location loc = (Location)kart.GetGP().GetTrack().GetReturn().get(i);
                
                double dist = UtilMath.offset(loc, kart.GetDriver().getLocation());
                
                if (locId == -1)
                {
                  locId = i;
                  bestDist = dist;

                }
                else if (dist < bestDist)
                {
                  locId = i;
                  bestDist = dist;
                }
              }
              
              if (locId != -1)
              {


                if (bestDist > 120.0D)
                {
                  Lakitu(kart, locId);





                }
                else if (kart.GetDriver().getLocation().getY() < 0.0D)
                {
                  Lakitu(kart, locId);

                }
                else
                {
                  Block block = kart.GetDriver().getLocation().getBlock().getRelative(BlockFace.UP);
                  if ((block.getTypeId() == 8) || (block.getTypeId() == 9))
                  {
                    int neighbours = 0;
                    
                    if ((block.getRelative(1, 0, 0).getTypeId() == 8) || (block.getRelative(1, 0, 0).getTypeId() == 9)) neighbours++;
                    if ((block.getRelative(-1, 0, 0).getTypeId() == 8) || (block.getRelative(1, 0, 0).getTypeId() == 9)) neighbours++;
                    if ((block.getRelative(0, 0, 1).getTypeId() == 8) || (block.getRelative(1, 0, 0).getTypeId() == 9)) neighbours++;
                    if ((block.getRelative(0, 0, -1).getTypeId() == 8) || (block.getRelative(1, 0, 0).getTypeId() == 9)) { neighbours++;
                    }
                    if (neighbours >= 3)
                    {
                      kart.SetLakituTick(2);
                      
                      if (kart.GetLakituTick() > 6)
                      {
                        Lakitu(kart, locId);
                        continue;
                      }
                    }
                  }
                  

                  block = kart.GetDriver().getLocation().getBlock().getRelative(BlockFace.DOWN);
                  if (kart.GetGP().GetTrack().GetReturnBlocks().contains(Integer.valueOf(block.getTypeId())))
                  {
                    kart.SetLakituTick(2);
                    
                    if (kart.GetLakituTick() > 6)
                    {
                      Lakitu(kart, locId); }
                  }
                } }
            }
          } }
      }
    }
  }
  
  public void Lakitu(Kart kart, int locId) {
    kart.SetVelocity(new Vector(0, 0, 0));
    
    kart.SetKartState(KartState.Lakitu);
    
    kart.ExpireConditions();
    
    kart.SetLakituTick(-1000);
    
    kart.LoseLife();
    
    Location loc;
    Location loc;
    if (locId > 0) loc = (Location)kart.GetGP().GetTrack().GetReturn().get(locId - 1); else {
      loc = (Location)kart.GetGP().GetTrack().GetReturn().get(kart.GetGP().GetTrack().GetReturn().size());
    }
    Location next = (Location)kart.GetGP().GetTrack().GetReturn().get(locId);
    
    Vector dir = UtilAlg.getTrajectory(loc, next);
    loc.setYaw(UtilAlg.GetYaw(dir));
    loc.setPitch(0.0F);
    
    this._teleport.TP(kart.GetDriver(), loc);
    
    UtilPlayer.message(kart.GetDriver(), F.main("MK", "You are being returned to the track."));
    UtilPlayer.message(kart.GetDriver(), F.main("MK", "You cannot drive for 8 seconds."));
    
    kart.GetDriver().playSound(kart.GetDriver().getLocation(), org.bukkit.Sound.NOTE_BASS_GUITAR, 2.0F, 1.0F);
  }
}
