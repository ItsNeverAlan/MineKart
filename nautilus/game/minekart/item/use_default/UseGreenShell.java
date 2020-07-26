package nautilus.game.minekart.item.use_default;

import java.util.ArrayList;
import nautilus.game.minekart.item.KartItemEntity;
import nautilus.game.minekart.item.KartItemManager;
import nautilus.game.minekart.item.use_active.ActiveStandard;
import nautilus.game.minekart.item.world_items_default.GreenShell;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.KartUtil;





public class UseGreenShell
  extends ItemUse
{
  public void Use(KartItemManager manager, Kart kart)
  {
    ArrayList<KartItemEntity> ents = new ArrayList();
    
    ents.add(new GreenShell(manager, kart, KartUtil.GetLook(kart)));
    
    new ActiveStandard(manager, kart, ents);
  }
}
