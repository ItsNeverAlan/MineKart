package nautilus.game.minekart.item.use_custom;

import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilEnt;
import nautilus.game.minekart.gp.GP;
import nautilus.game.minekart.item.KartItemManager;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.KartManager;
import nautilus.game.minekart.kart.condition.ConditionType;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class UsePig extends nautilus.game.minekart.item.use_default.ItemUse
{
  public void Use(KartItemManager manager, Kart kart)
  {
    kart.SetItemStored(null);
    
    kart.GetGP().Announce(F.main("MK", F.elem(UtilEnt.getName(kart.GetDriver())) + " used " + F.item("Pig Stink") + "."));
    
    for (Kart other : manager.KartManager.GetKarts().values())
    {
      if (!other.equals(kart))
      {

        if ((!other.HasCondition(ConditionType.Star)) && (!other.HasCondition(ConditionType.Ghost)))
        {

          PotionEffect effect = new PotionEffect(org.bukkit.potion.PotionEffectType.CONFUSION, 400, 0, false);
          effect.apply(other.GetDriver());
          

          other.GetDriver().getWorld().playSound(other.GetDriver().getLocation(), Sound.PIG_IDLE, 2.0F, 0.5F);
        }
      }
    }
    kart.GetDriver().getWorld().playSound(kart.GetDriver().getLocation(), Sound.PIG_IDLE, 2.0F, 0.5F);
    kart.GetDriver().getWorld().playSound(kart.GetDriver().getLocation(), Sound.PIG_IDLE, 2.0F, 0.5F);
  }
}
