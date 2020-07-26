package nautilus.game.minekart.kart;

import java.util.ArrayList;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilMath;
import mineplex.core.fakeEntity.FakeEntity;
import mineplex.core.fakeEntity.FakeEntityManager;
import mineplex.core.fakeEntity.FakeItemDrop;
import mineplex.core.fakeEntity.FakePlayer;
import mineplex.core.itemstack.ItemStackFactory;
import nautilus.game.minekart.gp.GP;
import nautilus.game.minekart.gp.GPBattle;
import nautilus.game.minekart.item.KartItemActive;
import nautilus.game.minekart.item.KartItemType;
import nautilus.game.minekart.kart.condition.ConditionData;
import nautilus.game.minekart.kart.condition.ConditionType;
import nautilus.game.minekart.kart.crash.Crash;
import nautilus.game.minekart.track.Track;
import nautilus.game.minekart.track.Track.TrackState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

public class Kart
{
  public static enum DriftDirection
  {
    Left, 
    Right, 
    None;
  }
  
  private Player _driver = null;
  private FakeEntity _entity = null;
  private FakeItemDrop _fakeItem = null;
  private FakePlayer _fakePlayer = null;
  
  private GP _gp;
  
  private KartType _kartType;
  
  private KartState _kartState = KartState.Drive;
  private long _kartStateTime = System.currentTimeMillis();
  
  private Vector _yaw;
  
  private DriftDirection _drift = DriftDirection.None;
  private long _driftStart = 0L;
  

  private Crash _crash;
  

  private int _itemCycles = 0;
  private KartItemActive _itemActive = null;
  private KartItemType _itemStored = null;
  

  private ArrayList<ConditionData> _conditions = new ArrayList();
  

  private int _lives = 3;
  

  private int _lap = 0;
  private int _lapNode = 0;
  private double _lapScore = 0.0D;
  private int _lapMomentum = 10000;
  
  private int _lapPlace = 0;
  
  private int _lakituTick = 0;
  

  private int _songStarTick = 0;
  

  private Vector _velocity = new Vector(0, 0, 0);
  
  public Kart(Player player, KartType kartType, GP gp)
  {
    this._gp = gp;
    
    this._driver = player;
    this._kartType = kartType;
    this._entity = new FakeEntity(kartType.GetType(), player.getLocation());
    
    Equip();
  }
  
  public GP GetGP()
  {
    return this._gp;
  }
  
  public FakeEntity GetEntity()
  {
    return this._entity;
  }
  
  public Player GetDriver()
  {
    return this._driver;
  }
  
  public KartType GetKartType()
  {
    return this._kartType;
  }
  
  public void SetKartType(KartType type)
  {
    this._kartType = type;
    Equip();
  }
  
  public KartState GetKartState()
  {
    return this._kartState;
  }
  
  public long GetKartStateTime()
  {
    return this._kartStateTime;
  }
  
  public void SetKartState(KartState state)
  {
    this._kartState = state;
    this._kartStateTime = System.currentTimeMillis();
  }
  
  public Vector GetKartDirection()
  {
    return this._velocity;
  }
  
  public int GetLakituTick()
  {
    return this._lakituTick;
  }
  
  public void SetLakituTick(int tick)
  {
    this._lakituTick += tick;
    
    if (this._lakituTick <= 0) {
      this._lakituTick = 0;
    }
  }
  
  public ArrayList<ConditionData> GetConditions() {
    return this._conditions;
  }
  
  public void AddCondition(ConditionData data)
  {
    this._conditions.add(data);
    
    if (data.IsCondition(ConditionType.Star)) {
      SetStarSongTick(0);
    }
  }
  
  public boolean HasCondition(ConditionType type) {
    for (ConditionData data : this._conditions)
    {
      if (data.IsCondition(type)) {
        return true;
      }
    }
    return false;
  }
  
  public void ExpireCondition(ConditionType type)
  {
    for (ConditionData data : GetConditions()) {
      if (data.IsCondition(type))
        data.Expire();
    }
  }
  
  public void ExpireConditions() {
    for (ConditionData data : GetConditions()) {
      data.Expire();
    }
  }
  
  public void Equip() {
    this._driver.getInventory().clear();
    

    GetDriver().getInventory().setItem(0, ItemStackFactory.Instance.CreateStack(Material.STONE_SWORD, (byte)0, 1, "§a§lAccelerate"));
    GetDriver().getInventory().setItem(1, ItemStackFactory.Instance.CreateStack(Material.WOOD_SWORD, (byte)0, 1, "§a§lHand Brake"));
    

    GetDriver().getInventory().setItem(2, null);
    GetDriver().getInventory().setItem(3, null);
    GetDriver().getInventory().setItem(4, null);
    

    ItemStack a = ItemStackFactory.Instance.CreateStack(Material.WOOD_HOE, (byte)0, (int)(GetKartType().GetTopSpeed() * 100.0D), "§e§lTop Speed");
    ItemStack b = ItemStackFactory.Instance.CreateStack(Material.STONE_HOE, (byte)0, (int)GetKartType().GetAcceleration() - 10, "§e§lAcceleration");
    ItemStack c = ItemStackFactory.Instance.CreateStack(Material.IRON_HOE, (byte)0, (int)GetKartType().GetHandling() - 10, "§e§lHandling");
    ItemStack d = ItemStackFactory.Instance.CreateStack(GetKartType().GetAvatar(), (byte)0, 1, "§e§l" + GetKartType().GetName() + " Kart");
    
    GetDriver().getInventory().setItem(5, d);
    GetDriver().getInventory().setItem(6, a);
    GetDriver().getInventory().setItem(7, b);
    GetDriver().getInventory().setItem(8, c);
  }
  

  public Vector GetVelocity()
  {
    return this._velocity;
  }
  
  public Vector GetVelocityClone()
  {
    return new Vector(this._velocity.getX(), this._velocity.getY(), this._velocity.getZ());
  }
  
  public void SetVelocity(Vector vec)
  {
    this._velocity = vec;
  }
  
  public Vector GetYaw()
  {
    return this._yaw;
  }
  
  public void CrashStop()
  {
    this._velocity = new Vector(0, 0, 0);
    ExpireCondition(ConditionType.Boost);
    
    if (GetKartState() == KartState.Drive) {
      LoseLife();
    }
  }
  
  public double GetSpeed() {
    Vector vec = new Vector(this._velocity.getX(), 0.0D, this._velocity.getZ());
    return vec.length();
  }
  
  public void SetDrift()
  {
    ClearDrift();
    

    if (GetSpeed() < 0.4D)
    {
      return;
    }
    

    Vector look = GetDriver().getLocation().getDirection();
    look.setY(0);
    look.normalize();
    
    Vector vel = new Vector(GetVelocity().getX(), 0.0D, GetVelocity().getZ());
    vel.normalize();
    
    look.subtract(vel);
    
    if (look.length() < 0.2D)
    {
      return;
    }
    


    Vector kartVec = new Vector(this._velocity.getX(), 0.0D, this._velocity.getZ());
    kartVec.normalize();
    
    Vector lookVec = GetDriver().getLocation().getDirection();
    lookVec.setY(0);
    lookVec.normalize();
    
    Vector left = new Vector(kartVec.getZ(), 0.0D, kartVec.getX() * -1.0D);
    Vector right = new Vector(kartVec.getZ() * -1.0D, 0.0D, kartVec.getX());
    
    double distLeft = UtilMath.offset(left, lookVec);
    double distRight = UtilMath.offset(right, lookVec);
    
    if (distLeft < distRight) this._drift = DriftDirection.Left; else {
      this._drift = DriftDirection.Right;
    }
    this._driftStart = System.currentTimeMillis();
  }
  
  public DriftDirection GetDrift()
  {
    return this._drift;
  }
  
  public Vector GetDriftVector()
  {
    if (this._drift == DriftDirection.None) {
      return new Vector(0, 0, 0);
    }
    
    Vector kartVec = new Vector(this._velocity.getX(), 0.0D, this._velocity.getZ());
    kartVec.normalize();
    
    Vector lookVec = GetDriver().getLocation().getDirection();
    lookVec.setY(0);
    lookVec.normalize();
    
    if (this._drift == DriftDirection.Left)
    {
      Vector drift = new Vector(kartVec.getZ(), 0.0D, kartVec.getX() * -1.0D);
      return drift.subtract(kartVec);
    }
    

    Vector drift = new Vector(kartVec.getZ() * -1.0D, 0.0D, kartVec.getX());
    return drift.subtract(kartVec);
  }
  

  public long GetDriftTime()
  {
    return System.currentTimeMillis() - this._driftStart;
  }
  
  public void ClearDrift()
  {
    this._driftStart = System.currentTimeMillis();
    this._drift = DriftDirection.None;
  }
  
  public Crash GetCrash()
  {
    if (GetKartState() != KartState.Crash) {
      return null;
    }
    return this._crash;
  }
  
  public void SetCrash(Crash crash)
  {
    this._crash = crash;
  }
  
  public void PickupItem()
  {
    if ((GetItemStored() == null) && (GetItemCycles() == 0)) {
      SetItemCycles(40);
    }
  }
  
  public void SetItemCycles(int cycles) {
    this._itemCycles = cycles;
  }
  
  public int GetItemCycles()
  {
    return this._itemCycles;
  }
  
  public KartItemType GetItemStored()
  {
    return this._itemStored;
  }
  
  public KartItemActive GetItemActive()
  {
    return this._itemActive;
  }
  
  public void SetItemStored(KartItemType item)
  {
    this._itemStored = item;
    
    if (item == null)
    {
      if (this._fakeItem != null) {
        RemoveFakeKartItemInfo();
      }
    }
    else {
      SetFakeKartItemInfo(item);
    }
  }
  
  private void SetFakeKartItemInfo(KartItemType item)
  {
    boolean showPlayer = false;
    boolean spawnItem = false;
    
    if (this._fakeItem == null)
    {
      this._fakeItem = new FakeItemDrop(new ItemStack(item.GetMaterial()), GetDriver().getLocation());
      spawnItem = true;
    }
    else {
      this._fakeItem.SetItemStack(new ItemStack(item.GetMaterial()));
    }
    if (this._fakePlayer == null)
    {
      showPlayer = true;
      this._fakePlayer = new FakePlayer("Buffer", GetDriver().getLocation().subtract(0.0D, 10.0D, 0.0D));
    }
    

    GetDriver().getInventory().setItem(3, ItemStackFactory.Instance.CreateStack(item.GetMaterial(), (byte)0, item.GetAmount(), "§a§l" + item.GetName()));
    
    for (Kart kart : GetGP().GetKarts())
    {
      if (kart == this)
      {
        if (spawnItem)
        {
          FakeEntityManager.Instance.SendPacketTo(this._fakeItem.Spawn(), kart.GetDriver());
          FakeEntityManager.Instance.SendPacketTo(this._fakeItem.SetVehicle(kart.GetDriver().getEntityId()), kart.GetDriver());
        }
        
        FakeEntityManager.Instance.SendPacketTo(this._fakeItem.Show(), kart.GetDriver());

      }
      else
      {
        if (showPlayer)
        {
          FakeEntityManager.Instance.SendPacketTo(this._fakePlayer.Spawn(), kart.GetDriver());
          FakeEntityManager.Instance.SendPacketTo(this._fakePlayer.Hide(), kart.GetDriver());
          FakeEntityManager.Instance.SendPacketTo(this._fakePlayer.SetVehicle(GetDriver().getEntityId()), kart.GetDriver());
          
          FakeEntityManager.Instance.FakePassenger(kart.GetDriver(), GetDriver().getEntityId(), this._fakePlayer.SetVehicle(GetDriver().getEntityId()));
        }
        
        if (spawnItem)
        {
          FakeEntityManager.Instance.SendPacketTo(this._fakeItem.Spawn(), kart.GetDriver());
          FakeEntityManager.Instance.SendPacketTo(this._fakeItem.SetVehicle(this._fakePlayer.GetEntityId()), kart.GetDriver());
          
          FakeEntityManager.Instance.FakePassenger(kart.GetDriver(), this._fakePlayer.GetEntityId(), this._fakeItem.SetVehicle(this._fakePlayer.GetEntityId()));
        }
        
        FakeEntityManager.Instance.SendPacketTo(this._fakeItem.Show(), kart.GetDriver());
      }
    }
  }
  
  private void RemoveFakeKartItemInfo() {
    GetDriver().getInventory().setItem(3, null);
    
    for (Kart kart : GetGP().GetKarts())
    {
      if (this._fakeItem != null)
      {
        FakeEntityManager.Instance.SendPacketTo(this._fakeItem.Destroy(), kart.GetDriver());
      }
      
      if (kart != this)
      {
        if ((this._fakeItem != null) && (this._fakePlayer != null))
        {
          FakeEntityManager.Instance.RemoveFakePassenger(kart.GetDriver(), this._fakePlayer.GetEntityId());
        }
      }
    }
    
    this._fakeItem = null;
  }
  
  public void SetItemActive(KartItemActive item)
  {
    this._itemActive = item;
  }
  
  public void SetStability(int i)
  {
    GetDriver().setFoodLevel(i);
  }
  
  public boolean CanControl()
  {
    if (GetGP() == null) {
      return true;
    }
    if (GetGP().GetState() != nautilus.game.minekart.gp.GP.GPState.Live) {
      return false;
    }
    if ((GetGP().GetTrack().GetState() != Track.TrackState.Live) && (GetGP().GetTrack().GetState() != Track.TrackState.Ending)) {
      return false;
    }
    if (HasFinishedTrack()) {
      return false;
    }
    return true;
  }
  
  public int GetLives()
  {
    return this._lives;
  }
  
  public void LoseLife()
  {
    if (this._lives < 1) {
      return;
    }
    if (GetGP() == null) {
      return;
    }
    if (!(GetGP() instanceof GPBattle)) {
      return;
    }
    GPBattle battle = (GPBattle)GetGP();
    this._lives -= 1;
    
    if (this._lives == 0)
    {
      battle.GetTrack().GetPositions().add(0, this);
      battle.CheckBattleEnd();
    }
  }
  
  public int GetLap()
  {
    return this._lap;
  }
  
  public void SetLap(int lap)
  {
    this._lap = lap;
  }
  
  public int GetLapNode()
  {
    return this._lapNode;
  }
  
  public void SetLapNode(int lapNode)
  {
    if (((lapNode > this._lapNode) && ((this._lapNode > GetGP().GetTrack().GetProgress().size() / 5) || (lapNode < GetGP().GetTrack().GetProgress().size() * 4 / 5))) || ((lapNode <= GetGP().GetTrack().GetProgress().size() / 5) && (this._lapNode >= GetGP().GetTrack().GetProgress().size() * 4 / 5))) {
      SetLapMomentum(GetLapMomentum() + 1);
    } else if ((lapNode < this._lapNode) || ((this._lapNode <= GetGP().GetTrack().GetProgress().size() / 5) && (lapNode >= GetGP().GetTrack().GetProgress().size() * 4 / 5))) {
      SetLapMomentum(GetLapMomentum() - 1);
    }
    this._lapNode = lapNode;
    

    if (lapNode <= GetGP().GetTrack().GetProgress().size() / 5)
    {
      if (this._lapMomentum > GetGP().GetTrack().GetProgress().size() / 4)
      {
        SetLap(GetLap() + 1);
        SetLapMomentum(0);
        
        GetDriver().getWorld().playSound(GetDriver().getLocation(), GetKartType().GetSoundMain(), 0.5F, 1.0F);
        
        if (GetLap() > 3)
        {
          int place = GetLapPlace() + 1;
          
          String placeString = "st";
          if (place == 2) { placeString = "nd";
          } else if (place == 3) { placeString = "rd";
          } else if (place >= 4) { placeString = "th";
          }
          GetGP().Announce(F.main("MK", F.name(GetDriver().getName()) + " finished in " + F.elem(new StringBuilder(String.valueOf(place)).append(placeString).toString()) + " place."));
        }
        else {
          mineplex.core.common.util.UtilPlayer.message(GetDriver(), F.main("MK", "Lap " + GetLap()));
        }
      }
    }
  }
  
  public double GetLapScore() {
    return this._lapScore;
  }
  
  public void SetLapScore(double lapScore)
  {
    this._lapScore = (lapScore + 1000000 * (this._lap - (GetLapMomentum() < 0 ? 1 : 0)));
  }
  
  public int GetLapMomentum()
  {
    return this._lapMomentum;
  }
  
  public void SetLapMomentum(int lapMomentum)
  {
    this._lapMomentum = lapMomentum;
  }
  
  public int GetLapPlace()
  {
    return this._lapPlace;
  }
  

  public void SetLapPlace(int place)
  {
    if (place < this._lapPlace) {
      GetDriver().getWorld().playSound(GetDriver().getLocation(), GetKartType().GetSoundMain(), 0.5F, 1.0F);
    }
    this._lapPlace = place;
  }
  
  public boolean HasFinishedTrack()
  {
    return (GetLap() > 3) || (GetLives() <= 0);
  }
  

  public void ClearTrackData()
  {
    this._lives = 3;
    

    this._lap = 0;
    this._lapNode = 0;
    this._lapScore = 0.0D;
    this._lapMomentum = 10000;
    
    this._lapPlace = 0;
  }
  
  public int GetStarSongTick()
  {
    return this._songStarTick;
  }
  
  public void SetStarSongTick(int tick)
  {
    this._songStarTick = tick;
  }
  
  public void SetPlayerArmor()
  {
    GetDriver().getInventory().setHelmet(null);
    GetDriver().getInventory().setChestplate(null);
    GetDriver().getInventory().setLeggings(null);
    GetDriver().getInventory().setBoots(null);
  }
  
  public boolean IsInvulnerable(boolean useHeart)
  {
    if ((useHeart) && (HasCondition(ConditionType.WolfHeart)))
    {
      ExpireCondition(ConditionType.WolfHeart);
      return true;
    }
    
    return (HasCondition(ConditionType.Star)) || (HasCondition(ConditionType.Ghost));
  }
}
