package nautilus.game.minekart.gp;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import mineplex.core.common.util.FireworkUtil;
import mineplex.core.common.util.MapUtil;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.WorldChunkLoader;
import mineplex.core.common.util.WorldLoadInfo;
import mineplex.core.common.util.WorldUtil;
import mineplex.core.donation.DonationManager;
import mineplex.core.fakeEntity.FakeEntity;
import mineplex.core.fakeEntity.FakePlayer;
import mineplex.core.portal.Portal;
import mineplex.core.teleport.Teleport;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.track.Track;
import nautilus.minecraft.core.utils.ZipUtil;
import net.minecraft.server.v1_6_R3.EntityPlayer;
import net.minecraft.server.v1_6_R3.PlayerConnection;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class GPResult
{
  private GPManager Manager;
  private String _file = "Result";
  
  private World _world;
  
  private GP _gp;
  private boolean _initialized = false;
  
  private long _time;
  
  private Kart _first;
  
  private Kart _second;
  private Kart _third;
  private List<Location> _fireworkLocations;
  
  public GPResult(GP gp, DonationManager manager)
  {
    this.Manager = gp.Manager;
    
    this._gp = gp;
    
    this._time = System.currentTimeMillis();
    
    List<Kart> sortedScores = new ArrayList(this._gp.GetKarts());
    java.util.Collections.sort(sortedScores, new ScoreComparator(this._gp));
    
    int buffer = (this._gp instanceof GPBattle) ? 9 : 14;
    
    if ((this._gp.GetTrack().GetPositions().size() > 0) && (sortedScores.size() > 0))
    {
      this._first = ((Kart)sortedScores.get(0));
      manager.RewardGems(null, "Earned Minekart", this._first.GetDriver().getName(), 8 * this._gp.GetPlayers().size() + buffer);
    }
    
    buffer += 2;
    
    if ((this._gp.GetTrack().GetPositions().size() > 1) && (sortedScores.size() > 1))
    {
      this._second = ((Kart)sortedScores.get(1));
      manager.RewardGems(null, "Earned Minekart", this._second.GetDriver().getName(), 5 * this._gp.GetPlayers().size() + buffer);
    }
    
    buffer += 2;
    
    if ((this._gp.GetTrack().GetPositions().size() > 2) && (sortedScores.size() > 2))
    {
      this._third = ((Kart)sortedScores.get(2));
      manager.RewardGems(null, "Earned Minekart", this._third.GetDriver().getName(), 2 * this._gp.GetPlayers().size() + buffer);
    }
    
    buffer += 2;
    
    for (int i = 3; i < sortedScores.size(); i++)
    {
      manager.RewardGems(null, "Earned Minekart", ((Kart)sortedScores.get(i)).GetDriver().getName(), buffer);
    }
    
    this._fireworkLocations = new ArrayList(5);
    
    Initialise();
  }
  
  public void TeleportPlayers()
  {
    Location loc = new Location(this._world, 10.0D, 23.0D, -22.0D);
    loc.setYaw(180.0F);
    
    FakePlayer firstPlayer = null;
    FakePlayer secondPlayer = null;
    FakePlayer thirdPlayer = null;
    
    FakeEntity firstKart = null;
    FakeEntity secondKart = null;
    FakeEntity thirdKart = null;
    
    if (this._first != null)
    {
      Location location = new Location(this._world, 10.0D, 25.0D, -29.0D);
      firstPlayer = new FakePlayer(this._first.GetDriver().getName(), location);
      firstKart = new FakeEntity(this._first.GetEntity().GetEntityType(), location);
    }
    
    if (this._second != null)
    {
      Location location = new Location(this._world, 6.0D, 24.0D, -29.0D);
      secondPlayer = new FakePlayer(this._second.GetDriver().getName(), location);
      secondKart = new FakeEntity(this._second.GetEntity().GetEntityType(), location);
    }
    
    if (this._third != null)
    {
      Location location = new Location(this._world, 14.0D, 23.0D, -29.0D);
      thirdPlayer = new FakePlayer(this._third.GetDriver().getName(), location);
      thirdKart = new FakeEntity(this._third.GetEntity().GetEntityType(), location);
    }
    
    for (Player player : this._gp.GetPlayers())
    {
      if (player.isOnline())
      {

        this.Manager.GetTeleport().TP(player, loc);
        
        EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
        
        if (this._first != null)
        {
          entityPlayer.playerConnection.sendPacket(firstPlayer.Spawn());
          entityPlayer.playerConnection.sendPacket(firstKart.Spawn());
          entityPlayer.playerConnection.sendPacket(firstKart.SetPassenger(firstPlayer.GetEntityId()));
        }
        
        if (this._second != null)
        {
          entityPlayer.playerConnection.sendPacket(secondPlayer.Spawn());
          entityPlayer.playerConnection.sendPacket(secondKart.Spawn());
          entityPlayer.playerConnection.sendPacket(secondKart.SetPassenger(secondPlayer.GetEntityId()));
        }
        
        if (this._third != null)
        {
          entityPlayer.playerConnection.sendPacket(thirdPlayer.Spawn());
          entityPlayer.playerConnection.sendPacket(thirdKart.Spawn());
          entityPlayer.playerConnection.sendPacket(thirdKart.SetPassenger(thirdPlayer.GetEntityId()));
        }
      }
    }
  }
  
  public void Initialise() {
    UtilServer.getServer().getScheduler().runTaskAsynchronously(this.Manager.GetPlugin(), new Runnable()
    {

      public void run()
      {
        GPResult.this.UnzipWorld();
        
        GPResult.this.SetLocations();
        

        UtilServer.getServer().getScheduler().runTask(GPResult.this.Manager.GetPlugin(), new Runnable()
        {
          public void run()
          {
            WorldChunkLoader.AddWorld(new WorldLoadInfo(GPResult.this._world, -5, -6, 5, 2), new Runnable()
            {
              public void run()
              {
                GPResult.this._initialized = true;
                GPResult.this.TeleportPlayers();
              }
            });
          }
        });
      }
    });
  }
  
  protected void SetLocations()
  {
    this._fireworkLocations.add(new Location(this._world, -9.5D, 42.0D, -54.5D));
    this._fireworkLocations.add(new Location(this._world, 29.5D, 42.0D, -54.5D));
    this._fireworkLocations.add(new Location(this._world, 32.5D, 43.0D, -77.5D));
    this._fireworkLocations.add(new Location(this._world, -12.5D, 43.0D, -77.5D));
    this._fireworkLocations.add(new Location(this._world, 10.0D, 61.0D, -61.0D));
  }
  

  public void UnzipWorld()
  {
    String folder = this._gp.GetId() + "-" + this._gp.GetSet().GetName() + "-" + this._file;
    new File(folder).mkdir();
    new File(folder + File.separatorChar + "region").mkdir();
    new File(folder + File.separatorChar + "data").mkdir();
    ZipUtil.UnzipToDirectory(this._file + ".zip", folder);
    

    this._world = WorldUtil.LoadWorld(new org.bukkit.WorldCreator(folder));
  }
  
  public void Uninitialise()
  {
    MapUtil.UnloadWorld(this.Manager.GetPlugin(), this._world);
    MapUtil.ClearWorldReferences(this._world.getName());
    mineplex.core.common.util.FileUtil.DeleteFolder(new File(this._world.getName()));
    
    this._first = null;
    this._second = null;
    this._third = null;
    
    this._fireworkLocations.clear();
    
    this._world = null;
  }
  
  public boolean End()
  {
    if (!this._initialized) {
      return false;
    }
    if (!mineplex.core.common.util.UtilTime.elapsed(this._time, 30000L))
    {
      FireworkUtil.LaunchRandomFirework((Location)this._fireworkLocations.get(RandomUtils.nextInt(5)));
      
      return false;
    }
    
    for (Player player : this._gp.GetPlayers())
    {
      this.Manager.Portal.SendPlayerToServer(player, "Lobby");
    }
    

    this._gp.GetPlayers().clear();
    this._gp = null;
    Uninitialise();
    
    return true;
  }
  
  public void RemovePlayer(Player player)
  {
    this._gp.RemovePlayer(player, null);
  }
}
