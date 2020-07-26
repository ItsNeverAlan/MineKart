package nautilus.game.minekart.item;

import nautilus.game.minekart.kart.Kart;

public abstract class KartItemActive {
  private Kart _kart;
  private ActiveType _type;
  private java.util.List<KartItemEntity> _ents;
  
  public static enum ActiveType {
    Behind, 
    Orbit, 
    Trail;
  }
  




  public KartItemActive(KartItemManager manager, Kart kart, ActiveType type, java.util.List<KartItemEntity> ents)
  {
    this._kart = kart;
    this._type = type;
    this._ents = ents;
    
    for (KartItemEntity item : ents) {
      item.SetHost(this);
    }
    kart.SetItemActive(this);
    manager.RegisterKartItem(this);
  }
  
  public Kart GetKart()
  {
    return this._kart;
  }
  
  public ActiveType GetType()
  {
    return this._type;
  }
  
  public java.util.List<KartItemEntity> GetEntities()
  {
    return this._ents;
  }
  
  public abstract boolean Use();
}
