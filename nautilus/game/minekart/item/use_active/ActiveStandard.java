package nautilus.game.minekart.item.use_active;

import java.util.List;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilMath;
import nautilus.game.minekart.gp.GP;
import nautilus.game.minekart.gp.GPBattle;
import nautilus.game.minekart.item.KartItemActive;
import nautilus.game.minekart.item.KartItemEntity;
import nautilus.game.minekart.item.KartItemManager;
import nautilus.game.minekart.item.world_items_default.GreenShell;
import nautilus.game.minekart.item.world_items_default.RedShell;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.KartUtil;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ActiveStandard extends KartItemActive
{
  public ActiveStandard(KartItemManager manager, Kart kart, List<KartItemEntity> ents)
  {
    super(manager, kart, nautilus.game.minekart.item.KartItemActive.ActiveType.Behind, ents);
  }
  

  public boolean Use()
  {
    if (GetEntities().isEmpty()) {
      return true;
    }
    KartItemEntity item = (KartItemEntity)GetEntities().get(0);
    

    item.SetVelocity(null);
    item.SetFired();
    

    if (((item instanceof RedShell)) || ((item instanceof GreenShell)))
    {
      item.GetEntity().teleport(KartUtil.GetInfront(item.GetHost().GetKart()));
      
      Vector vel = UtilAlg.Normalize(GetKart().GetDriver().getLocation().getDirection().setY(0));
      vel.multiply(1.4D);
      vel.setY(-0.4D);
      item.SetVelocity(vel);
    }
    

    item.SetHost(null);
    GetEntities().remove(item);
    

    if ((item instanceof RedShell))
    {
      Kart target = null;
      
      if ((GetKart().GetGP() instanceof GPBattle))
      {
        double closest = 9999.0D;
        
        for (Kart other : GetKart().GetGP().GetKarts())
        {
          if (!GetKart().equals(other))
          {

            if (GetKart().GetLives() > 0)
            {

              double dist = UtilMath.offset(GetKart().GetDriver(), other.GetDriver());
              
              if (target == null)
              {
                target = other;
                closest = dist;


              }
              else if (dist < closest)
              {
                target = other;
                closest = dist;
              }
            }
          }
        }
      } else {
        for (Kart other : GetKart().GetGP().GetKarts())
        {
          if (other.GetLapPlace() + 1 == GetKart().GetLapPlace()) {
            target = other;
          }
        }
      }
      item.SetTarget(target);
    }
    
    return GetEntities().isEmpty();
  }
}
