package nautilus.game.minekart.item.use_active;

import java.util.List;
import nautilus.game.minekart.item.KartItemActive;
import nautilus.game.minekart.item.KartItemActive.ActiveType;
import nautilus.game.minekart.item.KartItemEntity;
import nautilus.game.minekart.item.KartItemManager;
import nautilus.game.minekart.kart.Kart;

public class ActiveBananas extends KartItemActive
{
  public ActiveBananas(KartItemManager manager, Kart kart, List<KartItemEntity> ents)
  {
    super(manager, kart, KartItemActive.ActiveType.Trail, ents);
  }
  

  public boolean Use()
  {
    if (GetEntities().isEmpty()) {
      return true;
    }
    
    KartItemEntity back = (KartItemEntity)GetEntities().get(GetEntities().size() - 1);
    

    back.SetHost(null);
    back.SetVelocity(null);
    GetEntities().remove(back);
    

    back.SetFired();
    
    return GetEntities().isEmpty();
  }
}
