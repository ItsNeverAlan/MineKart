

java.util.ArrayList
java.util.Collection
java.util.Collections
java.util.Comparator
java.util.List
mineplex.core.MiniPlugin
mineplex.core.common.util.NautHashMap
mineplex.core.donation.repository.GameSalesPackageToken
nautilus.game.minekart.kart.KartType
nautilus.game.minekart.repository.KartItemToken
nautilus.game.minekart.repository.KartRepository
nautilus.game.minekart.shop.KartItem
org.bukkit.Material
org.bukkit.plugin.java.JavaPlugin



KartFactory
  

  _repository
  , _karts
  _sortedKarts
  
  KartFactory, 
  
    "Kart Factory", 
    
    _repository = repository;
    this._karts = new NautHashMap();
    
    PopulateKarts();
  }
  
  public Collection<KartItem> GetKarts()
  {
    return this._sortedKarts;
  }
  
  private void PopulateKarts()
  {
    this._karts.put("Chicken", new KartItem(Material.FEATHER, KartType.Chicken));
    this._karts.put("Sheep", new KartItem(Material.WHEAT, KartType.Sheep));
    this._karts.put("Cow", new KartItem(Material.MILK_BUCKET, KartType.Cow));
    this._karts.put("Pig", new KartItem(Material.GRILLED_PORK, KartType.Pig));
    this._karts.put("Spider", new KartItem(Material.STRING, KartType.Spider));
    this._karts.put("Wolf", new KartItem(Material.SUGAR, KartType.Wolf));
    this._karts.put("Enderman", new KartItem(Material.FIREBALL, KartType.Enderman));
    this._karts.put("Blaze", new KartItem(Material.BLAZE_ROD, KartType.Blaze));
    this._karts.put("Golem", new KartItem(Material.IRON_INGOT, KartType.Golem));
    
    List<KartItemToken> itemTokens = new ArrayList();
    
    for (KartItem item : this._karts.values())
    {
      KartItemToken itemToken = new KartItemToken();
      itemToken.Name = item.GetName();
      itemToken.Material = item.GetDisplayMaterial().toString();
      itemToken.Data = item.GetDisplayData();
      itemToken.SalesPackage = new GameSalesPackageToken();
      
      itemTokens.add(itemToken);
    }
    
    for (KartItemToken itemToken : this._repository.GetKartItems(itemTokens))
    {
      if (this._karts.containsKey(itemToken.Name))
      {
        ((KartItem)this._karts.get(itemToken.Name)).Update(itemToken.SalesPackage);
      }
    }
    
    this._sortedKarts = new ArrayList(this._karts.values());
    
    Collections.sort(this._sortedKarts, new Comparator()
    {

      public int compare(KartItem kartItem1, KartItem kartItem2)
      {
        if (kartItem1.GetKartType().GetStability() < kartItem2.GetKartType().GetStability()) {
          return -1;
        }
        if (kartItem1.GetKartType().GetStability() == kartItem2.GetKartType().GetStability()) {
          return 0;
        }
        return 1;
      }
    });
  }
}
