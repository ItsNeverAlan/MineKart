package mineplex.core.packethandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mineplex.core.MiniPlugin;
import mineplex.core.common.util.NautHashMap;
import net.minecraft.server.v1_6_R3.EntityPlayer;
import net.minecraft.server.v1_6_R3.NetworkManager;
import net.minecraft.server.v1_6_R3.Packet;
import net.minecraft.server.v1_6_R3.PlayerConnection;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketHandler extends MiniPlugin
{
  private Field syncField;
  private Field highField;
  private NautHashMap<String, NautHashMap<Integer, Integer>> _forwardMap;
  private NautHashMap<String, HashSet<Integer>> _blockMap;
  private NautHashMap<String, NautHashMap<Integer, Packet>> _fakeVehicleMap;
  private NautHashMap<String, NautHashMap<Integer, Packet>> _fakePassengerMap;
  private ArrayList<IPacketRunnable> _packetRunnables;
  
  public PacketHandler(JavaPlugin plugin)
  {
    super("PacketHandler", plugin);
    
    this._forwardMap = new NautHashMap();
    this._blockMap = new NautHashMap();
    this._fakeVehicleMap = new NautHashMap();
    this._fakePassengerMap = new NautHashMap();
    this._packetRunnables = new ArrayList();
    
    try
    {
      this.syncField = NetworkManager.class.getDeclaredField("h");
      this.syncField.setAccessible(true);
      this.highField = NetworkManager.class.getDeclaredField("highPriorityQueue");
      this.highField.setAccessible(true);
    }
    catch (Exception e)
    {
      System.out.println("Error initializing " + GetName() + " NetworkManager fields...");
    }
  }
  
  public String GetDataType(int c)
  {
    switch (c)
    {
    case 0: 
      return "Byte";
    case 1: 
      return "Short";
    case 2: 
      return "Integer";
    case 3: 
      return "Float";
    case 4: 
      return "String";
    case 5: 
      return "ItemStack";
    case 6: 
      return "ChunkCoordinates";
    }
    
    return "Say what?";
  }
  
  public void AddPacketRunnable(IPacketRunnable runnable)
  {
    this._packetRunnables.add(runnable);
  }
  
  public void RemovePacketRunnable(IPacketRunnable runnable)
  {
    this._packetRunnables.remove(runnable);
  }
  
  public Packet GetFakeAttached(Player owner, int a)
  {
    return (Packet)((NautHashMap)this._fakeVehicleMap.get(owner.getName())).get(Integer.valueOf(a));
  }
  
  public boolean IsFakeAttached(Player owner, int a)
  {
    return (this._fakeVehicleMap.containsKey(owner.getName())) && (((NautHashMap)this._fakeVehicleMap.get(owner.getName())).containsKey(Integer.valueOf(a)));
  }
  
  public Packet GetFakePassenger(Player owner, int a) {
    return (Packet)((NautHashMap)this._fakePassengerMap.get(owner.getName())).get(Integer.valueOf(a));
  }
  
  public boolean IsFakePassenger(Player owner, int a)
  {
    return (this._fakePassengerMap.containsKey(owner.getName())) && (((NautHashMap)this._fakePassengerMap.get(owner.getName())).containsKey(Integer.valueOf(a)));
  }
  
  public boolean IsBlocked(Player owner, int a)
  {
    return (this._blockMap.containsKey(owner.getName())) && (((HashSet)this._blockMap.get(owner.getName())).contains(Integer.valueOf(a)));
  }
  
  public int GetForwardId(Player owner, int a)
  {
    return ((Integer)((NautHashMap)this._forwardMap.get(owner.getName())).get(Integer.valueOf(a))).intValue();
  }
  
  public boolean IsForwarded(Player owner, int a)
  {
    return ((NautHashMap)this._forwardMap.get(owner.getName())).containsKey(Integer.valueOf(a));
  }
  
  public boolean IsForwarding(Player owner)
  {
    return this._forwardMap.containsKey(owner.getName());
  }
  
  @EventHandler(priority=EventPriority.MONITOR)
  public void onPlayerJoin(PlayerJoinEvent event)
  {
    in(event.getPlayer());
  }
  
  @EventHandler(priority=EventPriority.MONITOR)
  public void onPlayerQuit(PlayerQuitEvent event)
  {
    out(event.getPlayer());
  }
  
  public void in(Player player)
  {
    try
    {
      nom(getManager(player), Collections.synchronizedList(new PacketArrayList(player, this)));
    }
    catch (Exception localException) {}
  }
  




  public void out(Player player)
  {
    try
    {
      nom(getManager(player), Collections.synchronizedList(new ArrayList()), true);
    }
    catch (Exception e)
    {
      this._plugin.getLogger().log(Level.WARNING, "Failed to restore " + player.getName() + ". Could be a problem.", e);
    }
  }
  
  public void shutdown()
  {
    for (Player player : this._plugin.getServer().getOnlinePlayers())
    {
      if (player != null)
      {
        out(player);
      }
    }
  }
  
  private NetworkManager getManager(Player player)
  {
    return (NetworkManager)((CraftPlayer)player).getHandle().playerConnection.networkManager;
  }
  
  private void nom(NetworkManager nm, List list) throws IllegalArgumentException, IllegalAccessException
  {
    nom(nm, list, false);
  }
  
  private void nom(NetworkManager nm, List list, boolean onlyIfOldIsHacked) throws IllegalArgumentException, IllegalAccessException
  {
    List old = (List)this.highField.get(nm);
    boolean copy = true;
    
    if (onlyIfOldIsHacked)
    {
      if (!(old instanceof PacketArrayList))
      {
        return;
      }
      

      copy = false;
      ((PacketArrayList)old).Deactivate();
    }
    

    synchronized (this.syncField.get(nm))
    {
      if (copy)
      {
        for (Object object : old)
        {
          list.add(object);
        }
      }
      
      this.highField.set(nm, list);
    }
  }
  
  public void ForwardMovement(Player viewer, int travellerId, int entityId)
  {
    if (!this._forwardMap.containsKey(viewer.getName()))
    {
      this._forwardMap.put(viewer.getName(), new NautHashMap());
    }
    
    ((NautHashMap)this._forwardMap.get(viewer.getName())).put(Integer.valueOf(travellerId), Integer.valueOf(entityId));
  }
  
  public void BlockMovement(Player otherPlayer, int entityId)
  {
    if (!this._blockMap.containsKey(otherPlayer.getName()))
    {
      this._blockMap.put(otherPlayer.getName(), new HashSet());
    }
    
    ((HashSet)this._blockMap.get(otherPlayer.getName())).add(Integer.valueOf(entityId));
  }
  
  public void FakeVehicle(Player viewer, int entityId, Packet packet)
  {
    if (!this._fakeVehicleMap.containsKey(viewer.getName()))
    {
      this._fakeVehicleMap.put(viewer.getName(), new NautHashMap());
    }
    
    ((NautHashMap)this._fakeVehicleMap.get(viewer.getName())).put(Integer.valueOf(entityId), packet);
  }
  
  public void RemoveFakeVehicle(Player viewer, int entityId)
  {
    if (this._fakeVehicleMap.containsKey(viewer.getName()))
    {
      ((NautHashMap)this._fakeVehicleMap.get(viewer.getName())).remove(Integer.valueOf(entityId));
    }
  }
  
  public void FakePassenger(Player viewer, int entityId, Packet packet)
  {
    if (!this._fakePassengerMap.containsKey(viewer.getName()))
    {
      this._fakePassengerMap.put(viewer.getName(), new NautHashMap());
    }
    
    ((NautHashMap)this._fakePassengerMap.get(viewer.getName())).put(Integer.valueOf(entityId), packet);
  }
  
  public void RemoveFakePassenger(Player viewer, int entityId)
  {
    if (this._fakePassengerMap.containsKey(viewer.getName()))
    {
      ((NautHashMap)this._fakePassengerMap.get(viewer.getName())).remove(Integer.valueOf(entityId));
    }
  }
  
  public void RemoveForward(Player viewer)
  {
    this._forwardMap.remove(viewer.getName());
  }
  
  public boolean FireRunnables(Packet o, Player owner, PacketArrayList packetList)
  {
    boolean addOriginal = true;
    
    for (IPacketRunnable packetRunnable : this._packetRunnables)
    {
      if (!packetRunnable.run(o, owner, packetList)) {
        addOriginal = false;
      }
    }
    return addOriginal;
  }
}
