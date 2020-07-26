package nautilus.game.minekart.item.use_custom;

import java.util.HashMap;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilMath;
import nautilus.game.minekart.item.KartItemManager;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.KartManager;
import nautilus.game.minekart.kart.KartUtil;
import nautilus.game.minekart.kart.condition.ConditionType;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class UseGolem extends nautilus.game.minekart.item.use_default.ItemUse
{
  public void Use(KartItemManager manager, Kart kart)
  {
    kart.SetItemStored(null);
    
    kart.GetGP().Announce(F.main("MK", F.elem(UtilEnt.getName(kart.GetDriver())) + " used " + F.item("Earthquake") + "."));
    
    for (Kart other : manager.KartManager.GetKarts().values())
    {
      if (!other.equals(kart))
      {

        if (KartUtil.IsGrounded(other))
        {

          if ((!other.HasCondition(ConditionType.Star)) && (!other.HasCondition(ConditionType.Ghost)))
          {

            double offset = UtilMath.offset(kart.GetDriver(), other.GetDriver());
            

            if (offset < 100.0D) {
              new nautilus.game.minekart.kart.crash.Crash_Explode(other, 0.4D + (100.0D - offset) / 100.0D, false);
            }
            
            other.GetVelocity().multiply(0.5D);
            

            other.GetDriver().getWorld().playSound(other.GetDriver().getLocation(), Sound.IRONGOLEM_THROW, 2.0F, 0.5F);
          } }
      }
    }
    kart.GetDriver().getWorld().playSound(kart.GetDriver().getLocation(), Sound.IRONGOLEM_THROW, 2.0F, 0.5F);
    kart.GetDriver().getWorld().playSound(kart.GetDriver().getLocation(), Sound.IRONGOLEM_THROW, 2.0F, 0.5F);
    

    for (Block cur : UtilBlock.getInRadius(kart.GetDriver().getLocation(), Double.valueOf(4.0D)).keySet()) {
      if ((UtilBlock.airFoliage(cur.getRelative(BlockFace.UP))) && (!UtilBlock.airFoliage(cur))) {
        cur.getWorld().playEffect(cur.getLocation(), org.bukkit.Effect.STEP_SOUND, cur.getTypeId());
      }
    }
  }
}
