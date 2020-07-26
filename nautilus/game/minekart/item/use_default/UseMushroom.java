package nautilus.game.minekart.item.use_default;

import nautilus.game.minekart.item.KartItemManager;
import nautilus.game.minekart.item.KartItemType;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.condition.ConditionData;
import nautilus.game.minekart.kart.condition.ConditionType;


public class UseMushroom
  extends ItemUse
{
  public void Use(KartItemManager manager, Kart kart)
  {
    if (kart.GetItemStored() == KartItemType.SuperMushroom)
    {
      if (!kart.HasCondition(ConditionType.SuperMushroom)) {
        kart.AddCondition(new ConditionData(ConditionType.SuperMushroom, 8000L));
      }
      
    }
    else if (kart.GetItemStored() == KartItemType.TripleMushroom) {
      kart.SetItemStored(KartItemType.DoubleMushroom);

    }
    else if (kart.GetItemStored() == KartItemType.DoubleMushroom) {
      kart.SetItemStored(KartItemType.SingleMushroom);
    }
    else
    {
      kart.SetItemStored(null);
    }
    kart.AddCondition(new ConditionData(ConditionType.Boost, 2000L));
  }
}
