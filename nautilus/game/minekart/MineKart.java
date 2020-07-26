package nautilus.game.minekart;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import me.chiss.Core.MemoryFix.MemoryFix;
import me.chiss.Core.Modules.JoinQuit;
import mineplex.core.account.CoreClientManager;
import mineplex.core.antistack.AntiStack;
import mineplex.core.blockrestore.BlockRestore;
import mineplex.core.command.CommandCenter;
import mineplex.core.common.CurrencyType;
import mineplex.core.creature.Creature;
import mineplex.core.donation.DonationManager;
import mineplex.core.explosion.Explosion;
import mineplex.core.fakeEntity.FakeEntity;
import mineplex.core.fakeEntity.FakeEntityManager;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.monitor.LagMeter;
import mineplex.core.npc.NpcManager;
import mineplex.core.packethandler.PacketHandler;
import mineplex.core.punish.Punish;
import mineplex.core.recharge.Recharge;
import mineplex.core.server.ServerListener;
import mineplex.core.status.ServerStatusManager;
import mineplex.core.teleport.Teleport;
import mineplex.core.updater.Updater;
import nautilus.game.minekart.gp.GPManager;
import nautilus.game.minekart.kart.KartManager;
import nautilus.game.minekart.menu.KartMenu;
import nautilus.game.minekart.repository.KartRepository;
import nautilus.game.minekart.shop.KartShop;
import nautilus.game.minekart.track.TrackProcessor;
import nautilus.minecraft.core.INautilusPlugin;
import net.minecraft.server.v1_6_R3.EntityPlayer;
import net.minecraft.server.v1_6_R3.PlayerConnection;
import org.apache.commons.io.FileDeleteStrategy;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MineKart extends JavaPlugin implements INautilusPlugin, Listener
{
  private String WEB_CONFIG = "webServer";
  
  private CoreClientManager _clientManager;
  
  private DonationManager _donationManager;
  
  private BlockRestore _blockRestore;
  
  private Creature _creature;
  
  private mineplex.core.spawn.Spawn _spawn;
  
  private Teleport _teleport;
  
  private GPManager _gpManager;
  private ServerListener _serverListener;
  private Location _spawnLocation;
  private FakeEntity _chicken;
  private FakeEntity _wolf;
  private FakeEntity _pig;
  private FakeEntity _spider;
  private FakeEntity _sheep;
  private FakeEntity _cow;
  private FakeEntity _golem;
  private FakeEntity _blaze;
  private FakeEntity _enderman;
  
  public void onEnable()
  {
    ClearRaceFolders();
    
    getConfig().addDefault(this.WEB_CONFIG, "http://accounts.mineplex.com/");
    getConfig().set(this.WEB_CONFIG, getConfig().getString(this.WEB_CONFIG));
    saveConfig();
    
    this._spawnLocation = new Location((World)getServer().getWorlds().get(0), 8.5D, 17.0D, -22.5D, 0.0F, 0.0F);
    
    this._clientManager = CoreClientManager.Initialize(this, GetWebServerAddress());
    CommandCenter.Initialize(this, this._clientManager);
    FakeEntityManager.Initialize(this);
    Recharge.Initialize(this);
    
    this._donationManager = new DonationManager(this, GetWebServerAddress());
    
    this._creature = new Creature(this);
    
    new Punish(this, GetWebServerAddress(), this._clientManager);
    new Explosion(this, this._blockRestore);
    
    this._teleport = new Teleport(this, this._clientManager, this._spawn);
    

    new AntiStack(this);
    
    new JoinQuit();
    new ServerStatusManager(this, new LagMeter(this, this._clientManager));
    

    PacketHandler packetHandler = new PacketHandler(this);
    
    ItemStackFactory.Initialize(this, true);
    

    this._gpManager = new GPManager(this, this._donationManager, this._teleport, Recharge.Instance, new KartManager(this, Recharge.Instance), new nautilus.game.minekart.track.TrackManager(this, this._teleport));
    new TrackProcessor();
    

    new Updater(this);
    




    FakeEntityManager.Instance.SetPacketHandler(packetHandler);
    DonationManager donationManager = new DonationManager(this, GetWebServerAddress());
    
    new NpcManager(this, this._creature);
    KartFactory _kartFactory = new KartFactory(this, new KartRepository(GetWebServerAddress()));
    new KartShop(_kartFactory, this._clientManager, donationManager, new CurrencyType[] { CurrencyType.Gems });
    new KartMenu(_kartFactory, this._clientManager, donationManager, this._gpManager);
    
    new MemoryFix(this);
    
    getServer().getPluginManager().registerEvents(this, this);
    
    CreateFakeKarts();
  }
  
  @EventHandler
  public void OnPlayerJoin(PlayerJoinEvent event)
  {
    event.getPlayer().teleport(this._spawnLocation);
    event.getPlayer().setGameMode(GameMode.SURVIVAL);
    event.getPlayer().setFoodLevel(20);
    event.getPlayer().setHealth(20.0D);
    ShowFakeKarts(event.getPlayer());
  }
  
  @EventHandler
  public void PreventFoodChange(FoodLevelChangeEvent event)
  {
    if (((event.getEntity() instanceof Player)) && (!this._gpManager.InGame((Player)event.getEntity())))
    {
      event.setCancelled(true);
    }
  }
  
  @EventHandler
  public void onBlockBreakEvent(BlockBreakEvent event)
  {
    if (!event.getPlayer().isOp())
    {
      event.setCancelled(true);
    }
  }
  
  @EventHandler
  public void onBlockPlaceEvent(BlockPlaceEvent event)
  {
    if (!event.getPlayer().isOp()) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler
  public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
    if (!event.getPlayer().isOp()) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler
  public void onPlayerBucketFill(PlayerBucketFillEvent event) {
    if (!event.getPlayer().isOp()) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler
  public void PreventDrop(PlayerDropItemEvent event) {
    event.setCancelled(true);
  }
  
  @EventHandler
  public void BurnCancel(BlockBurnEvent event)
  {
    event.setCancelled(true);
  }
  
  @EventHandler
  public void SpreadCancel(BlockFromToEvent event)
  {
    event.setCancelled(true);
  }
  
  @EventHandler
  public void GrowCancel(BlockGrowEvent event)
  {
    event.setCancelled(true);
  }
  

  public void onDisable()
  {
    this._serverListener.Shutdown();
  }
  

  public JavaPlugin GetPlugin()
  {
    return this;
  }
  

  public String GetWebServerAddress()
  {
    return getConfig().getString(this.WEB_CONFIG);
  }
  

  public Server GetRealServer()
  {
    return getServer();
  }
  

  public PluginManager GetPluginManager()
  {
    return GetRealServer().getPluginManager();
  }
  
  private void CreateFakeKarts()
  {
    this._chicken = new FakeEntity(EntityType.CHICKEN, new Location(this._spawnLocation.getWorld(), 6.5D, 17.5D, -39.5D, 0.0F, 0.0F));
    this._wolf = new FakeEntity(EntityType.WOLF, new Location(this._spawnLocation.getWorld(), 8.5D, 17.5D, -39.5D, 0.0F, 0.0F));
    this._pig = new FakeEntity(EntityType.PIG, new Location(this._spawnLocation.getWorld(), 10.5D, 17.5D, -39.5D, 0.0F, 0.0F));
    this._spider = new FakeEntity(EntityType.SPIDER, new Location(this._spawnLocation.getWorld(), 6.5D, 19.5D, -39.5D, 0.0F, 0.0F));
    this._sheep = new FakeEntity(EntityType.SHEEP, new Location(this._spawnLocation.getWorld(), 8.5D, 19.5D, -39.5D, 0.0F, 0.0F));
    this._cow = new FakeEntity(EntityType.COW, new Location(this._spawnLocation.getWorld(), 10.5D, 19.5D, -39.5D, 0.0F, 0.0F));
    this._golem = new FakeEntity(EntityType.IRON_GOLEM, new Location(this._spawnLocation.getWorld(), 6.5D, 21.5D, -39.5D, 0.0F, 0.0F));
    this._blaze = new FakeEntity(EntityType.BLAZE, new Location(this._spawnLocation.getWorld(), 8.5D, 21.5D, -39.5D, 0.0F, 0.0F));
    this._enderman = new FakeEntity(EntityType.ENDERMAN, new Location(this._spawnLocation.getWorld(), 10.5D, 21.5D, -39.5D, 0.0F, 0.0F));
  }
  
  private void ShowFakeKarts(Player player)
  {
    EntityPlayer mcPlayer = ((CraftPlayer)player).getHandle();
    
    mcPlayer.playerConnection.sendPacket(this._chicken.Spawn());
    mcPlayer.playerConnection.sendPacket(this._wolf.Spawn());
    mcPlayer.playerConnection.sendPacket(this._pig.Spawn());
    mcPlayer.playerConnection.sendPacket(this._spider.Spawn());
    mcPlayer.playerConnection.sendPacket(this._sheep.Spawn());
    mcPlayer.playerConnection.sendPacket(this._cow.Spawn());
    mcPlayer.playerConnection.sendPacket(this._golem.Spawn());
    mcPlayer.playerConnection.sendPacket(this._blaze.Spawn());
    mcPlayer.playerConnection.sendPacket(this._enderman.Spawn());
  }
  
  private void ClearRaceFolders()
  {
    File mainDirectory = new File(".");
    
    FileFilter statsFilter = new FileFilter()
    {

      public boolean accept(File arg0)
      {
        return (arg0.isDirectory()) && (arg0.getName().contains("-"));
      }
    };
    
    for (File f : mainDirectory.listFiles(statsFilter))
    {
      try
      {
        FileDeleteStrategy.FORCE.delete(f);

      }
      catch (IOException e)
      {
        System.out.println("Error deleting " + f.getName() + " on startup.");
      }
    }
  }
}
