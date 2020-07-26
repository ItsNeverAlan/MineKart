package me.chiss.Core.Modules;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import me.chiss.Core.Module.AModule;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.fakeEntity.FakeEntity;
import mineplex.core.fakeEntity.FakeEntityManager;
import mineplex.core.fakeEntity.FakePlayer;
import mineplex.core.updater.event.UpdateEvent;
import net.minecraft.server.v1_6_R3.EntityInsentient;
import net.minecraft.server.v1_6_R3.EntityPlayer;
import net.minecraft.server.v1_6_R3.MathHelper;
import net.minecraft.server.v1_6_R3.Packet28EntityVelocity;
import net.minecraft.server.v1_6_R3.Packet31RelEntityMove;
import net.minecraft.server.v1_6_R3.Packet34EntityTeleport;
import net.minecraft.server.v1_6_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_6_R3.PlayerConnection;
import net.minecraft.server.v1_6_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftAgeable;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public class Tester extends AModule
{
  private HashMap<Player, HashSet<String>> _test = new HashMap();
  
  private HashMap<Player, Vector> _speed = new HashMap();
  private HashMap<Player, Double> _speedVert = new HashMap();
  
  public Tester(JavaPlugin plugin)
  {
    super("Tester", plugin);
  }
  




  public void enable() {}
  




  public void disable() {}
  



  public void config() {}
  



  public void commands()
  {
    AddCommand("e1");
    AddCommand("spleef");
    AddCommand("coinset");
    AddCommand("sin");
    AddCommand("wi");
    AddCommand("arraylist");
    AddCommand("spinme");
    AddCommand("karts");
    AddCommand("blocks");
    AddCommand("testi");
    AddCommand("flag");
  }
  

  public void command(Player paramPlayer, String paramString, String[] paramArrayOfString)
  {
    throw new Error("Unresolved compilation problems: \n\tThe method Clients() is undefined for the type Tester\n\tThe method Clients() is undefined for the type Tester\n");
  }
  






































































  @EventHandler
  public void Update(UpdateEvent event)
  {
    if (event.getType() != mineplex.core.updater.UpdateType.TICK) {
      return;
    }
    for (Player cur : this._test.keySet()) {
      if (((HashSet)this._test.get(cur)).contains("e1"))
        cur.getWorld().playEffect(cur.getLocation().add(0.0D, 2.0D, 0.0D), Effect.ENDER_SIGNAL, 0);
    }
  }
  
  @EventHandler
  public void Spleef(PlayerInteractEvent event) {
    if (!event.getPlayer().getName().equals("Chiss")) {
      return;
    }
    if (event.getClickedBlock() == null) {
      return;
    }
    if (event.getPlayer().getItemInHand() == null) {
      return;
    }
    if (event.getPlayer().getItemInHand().getType() != Material.BOWL) {
      return;
    }
    event.getPlayer().sendMessage("Light Level: " + event.getClickedBlock().getLightFromSky());
  }
  
  public Vector GetSpeed(Player player)
  {
    if (!this._speed.containsKey(player)) {
      this._speed.put(player, new Vector(0, 0, 0));
    }
    return (Vector)this._speed.get(player);
  }
  
  public double GetSpeedVert(Player player)
  {
    if (!this._speedVert.containsKey(player)) {
      this._speedVert.put(player, Double.valueOf(0.0D));
    }
    return ((Double)this._speedVert.get(player)).doubleValue();
  }
  
  public void ShowBobInvis(Player player)
  {
    final EntityPlayer mcPlayer = ((CraftPlayer)player).getHandle();
    final FakePlayer fakePlayer = new FakePlayer("BOB", player.getEyeLocation().add(1.0D, 0.0D, -3.0D));
    
    mcPlayer.playerConnection.sendPacket(fakePlayer.Spawn());
    
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugins()[0], new Runnable()
    {
      public void run()
      {
        mcPlayer.playerConnection.sendPacket(fakePlayer.Hide());
        System.out.println("Sent meta packet");
      }
    }, 20L);
  }
  
  public void ShowFakePig(Player player)
  {
    FakeEntity entity = new FakeEntity(EntityType.PIG, player.getLocation());
    
    EntityPlayer mcPlayer = ((CraftPlayer)player).getHandle();
    
    mcPlayer.playerConnection.sendPacket(entity.Spawn());
    mcPlayer.playerConnection.sendPacket(new Packet28EntityVelocity(entity.GetEntityId(), 100.0D, 0.0D, 0.0D));
  }
  
  public void ShowRealPig(Player player)
  {
    FakePlayer fakePlayer = new FakePlayer(player.getName() + "1", player.getLocation());
    
    EntityPlayer mcPlayer = ((CraftPlayer)player).getHandle();
    
    mcPlayer.playerConnection.sendPacket(fakePlayer.Spawn());
    mcPlayer.playerConnection.sendPacket(new Packet28EntityVelocity(fakePlayer.GetEntityId(), 10000.0D, 0.0D, 0.0D));
  }
  
  public void ShowPlayersInKarts()
  {
    for (Player player : )
    {
      FakeEntity entity = new FakeEntity(EntityType.PIG, player.getLocation());
      
      for (Player otherPlayer : Bukkit.getOnlinePlayers())
      {
        if (player != otherPlayer)
        {

          EntityPlayer mcPlayer = ((CraftPlayer)otherPlayer).getHandle();
          
          mcPlayer.playerConnection.sendPacket(entity.Spawn());
          mcPlayer.playerConnection.sendPacket(entity.SetPassenger(player.getEntityId()));
          FakeEntityManager.Instance.ForwardMovement(otherPlayer, player, entity.GetEntityId());
        }
      }
    }
    Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugins()[0], new Runnable()
    {
      public void run()
      {
        for (Player player : )
        {
          Location location = player.getLocation();
          Packet34EntityTeleport teleportPacket = new Packet34EntityTeleport(player.getEntityId(), MathHelper.floor(location.getX() * 32.0D), MathHelper.floor(location.getY() * 32.0D), MathHelper.floor(location.getZ() * 32.0D), (byte)MathHelper.d(location.getYaw() * 256.0F / 360.0F), (byte)MathHelper.d(location.getPitch() * 256.0F / 360.0F));
          
          for (Player otherPlayer : Bukkit.getOnlinePlayers())
          {
            EntityPlayer mcPlayer = ((CraftPlayer)otherPlayer).getHandle();
            
            if (player != otherPlayer)
            {

              mcPlayer.playerConnection.sendPacket(teleportPacket); }
          }
        }
      }
    }, 0L, 40L);
  }
  





























  public void TestFlag(Player player)
  {
    final EntityPlayer mcPlayer = ((CraftPlayer)player).getHandle();
    
    for (final Player onlinePlayer : Bukkit.getOnlinePlayers())
    {
      if (onlinePlayer != player)
      {



        Item anchor = player.getWorld().dropItem(onlinePlayer.getEyeLocation(), new ItemStack(Material.WOOL.getId(), 1, (short)11));
        Item flag12 = player.getWorld().dropItem(onlinePlayer.getEyeLocation().add(0.0D, 1.0D, 0.0D), new ItemStack(Material.WOOL.getId(), 1, (short)12));
        Item flag13 = player.getWorld().dropItem(onlinePlayer.getEyeLocation().add(0.0D, 2.0D, 0.0D), new ItemStack(Material.WOOL.getId(), 1, (short)13));
        Item flag14 = player.getWorld().dropItem(onlinePlayer.getEyeLocation().add(0.0D, 2.0D, 0.0D), new ItemStack(Material.WOOL.getId(), 1, (short)14));
        Item flag15 = player.getWorld().dropItem(onlinePlayer.getEyeLocation().add(0.0D, 4.0D, 0.0D), new ItemStack(Material.WOOL.getId(), 1, (short)15));
        
        anchor.setPassenger(flag12);
        flag12.setPassenger(flag13);
        flag13.setPassenger(flag14);
        flag14.setPassenger(flag15);
        
        anchor.setPickupDelay(600);
        flag12.setPickupDelay(600);
        flag13.setPickupDelay(600);
        flag14.setPickupDelay(600);
        flag15.setPickupDelay(600);
        
        Item anchor2 = player.getWorld().dropItem(onlinePlayer.getEyeLocation().add(1.0D, 1.0D, 0.0D), new ItemStack(Material.WOOL.getId(), 1, (short)0));
        Item flag22 = player.getWorld().dropItem(onlinePlayer.getEyeLocation().add(1.0D, 2.0D, 0.0D), new ItemStack(Material.WOOL.getId(), 1, (short)1));
        Item flag23 = player.getWorld().dropItem(onlinePlayer.getEyeLocation().add(1.0D, 3.0D, 0.0D), new ItemStack(Material.WOOL.getId(), 1, (short)2));
        Item flag24 = player.getWorld().dropItem(onlinePlayer.getEyeLocation().add(1.0D, 4.0D, 0.0D), new ItemStack(Material.WOOL.getId(), 1, (short)3));
        
        anchor2.setPassenger(flag22);
        flag22.setPassenger(flag23);
        flag23.setPassenger(flag24);
        
        anchor2.setPickupDelay(600);
        flag22.setPickupDelay(600);
        flag23.setPickupDelay(600);
        flag24.setPickupDelay(600);
        
        Item anchor3 = player.getWorld().dropItem(onlinePlayer.getEyeLocation().add(2.0D, 1.0D, 0.0D), new ItemStack(Material.WOOL.getId(), 1, (short)4));
        Item flag32 = player.getWorld().dropItem(onlinePlayer.getEyeLocation().add(2.0D, 2.0D, 0.0D), new ItemStack(Material.WOOL.getId(), 1, (short)5));
        Item flag33 = player.getWorld().dropItem(onlinePlayer.getEyeLocation().add(2.0D, 3.0D, 0.0D), new ItemStack(Material.WOOL.getId(), 1, (short)6));
        Item flag34 = player.getWorld().dropItem(onlinePlayer.getEyeLocation().add(2.0D, 4.0D, 0.0D), new ItemStack(Material.WOOL.getId(), 1, (short)7));
        
        anchor3.setPassenger(flag32);
        flag32.setPassenger(flag33);
        flag33.setPassenger(flag34);
        
        anchor3.setPickupDelay(600);
        flag32.setPickupDelay(600);
        flag33.setPickupDelay(600);
        flag34.setPickupDelay(600);
        
        onlinePlayer.setPassenger(anchor);
        
        final FakePlayer fakePlayer = new FakePlayer("test", onlinePlayer.getLocation().add(1.0D, 0.0D, 0.0D));
        final FakePlayer fakePlayer2 = new FakePlayer("test2", onlinePlayer.getLocation().add(2.0D, 0.0D, 0.0D));
        
        mcPlayer.playerConnection.sendPacket(fakePlayer.Spawn());
        mcPlayer.playerConnection.sendPacket(fakePlayer.Hide());
        
        mcPlayer.playerConnection.sendPacket(fakePlayer2.Spawn());
        mcPlayer.playerConnection.sendPacket(fakePlayer2.Hide());
        
        mcPlayer.playerConnection.sendPacket(fakePlayer.SetPassenger(anchor2.getEntityId()));
        mcPlayer.playerConnection.sendPacket(fakePlayer2.SetPassenger(anchor3.getEntityId()));
        
        for (int i = 0; i < 101; i++)
        {
          Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugins()[0], new Runnable()
          {
            public void run()
            {
              Vector player1 = UtilAlg.getTrajectory(fakePlayer.GetLocation().toVector(), onlinePlayer.getLocation().toVector().subtract(new Vector(1, 0, 0)));
              Vector player2 = UtilAlg.getTrajectory(fakePlayer2.GetLocation().toVector(), onlinePlayer.getLocation().toVector().subtract(new Vector(2, 0, 0)));
              
              player1.multiply(20);
              player2.multiply(20);
              
              mcPlayer.playerConnection.sendPacket(new Packet31RelEntityMove(fakePlayer.GetEntityId(), (byte)(int)player1.getX(), (byte)(int)player1.getY(), (byte)(int)player1.getZ()));
              mcPlayer.playerConnection.sendPacket(new Packet31RelEntityMove(fakePlayer2.GetEntityId(), (byte)(int)player2.getX(), (byte)(int)player2.getY(), (byte)(int)player2.getZ()));
              
              fakePlayer.SetLocation(onlinePlayer.getLocation().subtract(new Vector(1, 0, 0)));
              fakePlayer2.SetLocation(onlinePlayer.getLocation().subtract(new Vector(2, 0, 0)));
            }
          }, i + 1);
        }
      }
    }
  }
  
  public void SpinHim(final Player player) {
    org.bukkit.entity.Entity entity = player.getWorld().spawnEntity(player.getLocation(), EntityType.OCELOT);
    ((CraftAgeable)entity).setBaby();
    ((CraftAgeable)entity).setAgeLock(true);
    
    try
    {
      Field _goalSelector = EntityInsentient.class.getDeclaredField("goalSelector");
      _goalSelector.setAccessible(true);
      Field _targetSelector = EntityInsentient.class.getDeclaredField("targetSelector");
      _targetSelector.setAccessible(true);
      
      _goalSelector.set(((CraftLivingEntity)entity).getHandle(), new PathfinderGoalSelector(((CraftWorld)entity.getWorld()).getHandle().methodProfiler));
      _targetSelector.set(((CraftLivingEntity)entity).getHandle(), new PathfinderGoalSelector(((CraftWorld)entity.getWorld()).getHandle().methodProfiler));
    }
    catch (IllegalArgumentException e)
    {
      e.printStackTrace();
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
    }
    catch (NoSuchFieldException e)
    {
      e.printStackTrace();
    }
    catch (SecurityException e)
    {
      e.printStackTrace();
    }
    
    entity.setPassenger(player);
    
    final org.bukkit.entity.Entity newEntity = entity;
    
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugins()[0], new Runnable()
    {
      public void run()
      {
        ((CraftEntity)newEntity).getHandle().yaw += 179.0F;
      }
    }, 20L);
    
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugins()[0], new Runnable()
    {
      public void run()
      {
        player.leaveVehicle();
        newEntity.remove();
      }
    }, 110L);
  }
}
