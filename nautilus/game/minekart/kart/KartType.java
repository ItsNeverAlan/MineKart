package nautilus.game.minekart.kart;

import mineplex.core.common.util.NautHashMap;
import nautilus.game.minekart.item.KartItemType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;


public enum KartType
{
  Pig(
    "Pig", EntityType.PIG, Material.GRILLED_PORK, Material.GRILLED_PORK, 
    0.9D, 6.0D, 5.0D, 3.0D, 
    Sound.PIG_IDLE, Sound.PIG_DEATH, Sound.PIG_WALK, 
    KartItemType.Pig), 
  
  Chicken("Chicken", EntityType.CHICKEN, Material.FEATHER, Material.POTATO_ITEM, 
    0.85D, 8.0D, 6.0D, 1.0D, 
    Sound.CHICKEN_IDLE, Sound.CHICKEN_HURT, Sound.CHICKEN_WALK, 
    KartItemType.Chicken), 
  
  Wolf("Wolf", EntityType.WOLF, Material.SUGAR, Material.BAKED_POTATO, 
    1.0D, 5.0D, 3.0D, 2.0D, 
    Sound.WOLF_BARK, Sound.WOLF_HURT, Sound.WOLF_WALK, 
    KartItemType.Wolf), 
  
  Sheep(
    "Sheep", EntityType.SHEEP, Material.WHEAT, Material.WHEAT, 
    0.95D, 4.0D, 6.0D, 5.0D, 
    Sound.SHEEP_IDLE, Sound.SHEEP_SHEAR, Sound.SHEEP_WALK, 
    KartItemType.Sheep), 
  
  Enderman("Enderman", EntityType.ENDERMAN, Material.FIREBALL, Material.ROTTEN_FLESH, 
    0.9D, 6.0D, 5.0D, 4.0D, 
    Sound.ENDERMAN_IDLE, Sound.ENDERMAN_SCREAM, Sound.ENDERMAN_TELEPORT, 
    KartItemType.Enderman), 
  
  Spider("Spider", EntityType.SPIDER, Material.STRING, Material.SPIDER_EYE, 
    0.8D, 7.0D, 8.0D, 4.0D, 
    Sound.SPIDER_IDLE, Sound.SPIDER_DEATH, Sound.SPIDER_WALK, 
    KartItemType.Spider), 
  
  Cow(
    "Cow", EntityType.COW, Material.MILK_BUCKET, Material.MILK_BUCKET, 
    1.0D, 3.0D, 5.0D, 7.0D, 
    Sound.COW_IDLE, Sound.COW_HURT, Sound.COW_WALK, 
    KartItemType.Cow), 
  
  Blaze("Blaze", EntityType.BLAZE, Material.BLAZE_ROD, Material.COOKED_BEEF, 
    1.0D, 7.0D, 1.0D, 6.0D, 
    Sound.BLAZE_BREATH, Sound.BLAZE_BREATH, Sound.BLAZE_BREATH, 
    KartItemType.Blaze), 
  
  Golem("Golem", EntityType.IRON_GOLEM, Material.IRON_INGOT, Material.NETHER_BRICK_ITEM, 
    1.05D, 1.0D, 7.0D, 8.0D, 
    Sound.IRONGOLEM_THROW, Sound.IRONGOLEM_HIT, Sound.IRONGOLEM_WALK, 
    KartItemType.Golem);
  

  private static NautHashMap<EntityType, KartType> _kartTypes;
  
  private String _name;
  
  private EntityType _type;
  
  private Material _kartAvatar;
  private Material _grayAvatar;
  private double _topSpeed;
  private double _acceleration;
  private double _handling;
  private double _stability;
  private Sound _soundUse;
  private Sound _soundCrash;
  private Sound _soundEngine;
  private KartItemType _kartItem;
  
  private KartType(String name, EntityType type, Material kartAvatar, Material grayAvatar, double topSpeed, double acceleration, double handling, double stability, Sound use, Sound crash, Sound engine, KartItemType item)
  {
    this._name = name;
    this._type = type;
    this._kartAvatar = kartAvatar;
    this._grayAvatar = grayAvatar;
    
    this._topSpeed = topSpeed;
    this._acceleration = acceleration;
    this._handling = handling;
    this._stability = stability;
    
    this._soundUse = use;
    this._soundCrash = crash;
    this._soundEngine = engine;
    
    this._kartItem = item;
    
    GetMap().put(type, this);
  }
  
  private static NautHashMap<EntityType, KartType> GetMap()
  {
    if (_kartTypes == null) {
      _kartTypes = new NautHashMap();
    }
    return _kartTypes;
  }
  
  public static KartType GetByEntityType(EntityType entityType)
  {
    return (KartType)_kartTypes.get(entityType);
  }
  
  public String GetName()
  {
    return this._name;
  }
  
  public EntityType GetType()
  {
    return this._type;
  }
  
  public double GetTopSpeed()
  {
    return this._topSpeed;
  }
  
  public double GetAcceleration()
  {
    return 10.0D + this._acceleration;
  }
  
  public double GetHandling()
  {
    return 10.0D + this._handling;
  }
  
  public double GetStability()
  {
    return 10.0D + this._stability;
  }
  
  public Sound GetSoundMain()
  {
    return this._soundUse;
  }
  
  public Sound GetSoundCrash()
  {
    return this._soundCrash;
  }
  
  public Sound GetSoundEngine()
  {
    return this._soundEngine;
  }
  
  public KartItemType GetKartItem()
  {
    return this._kartItem;
  }
  
  public String[] GetDescription()
  {
    return new String[0];
  }
  
  public Material GetAvatar()
  {
    return this._kartAvatar;
  }
  
  public Material GetAvatarGray()
  {
    return this._grayAvatar;
  }
}
