package nautilus.game.minekart.item.use_active;

import java.util.List;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilMath;
import nautilus.game.minekart.gp.GP;
import nautilus.game.minekart.item.KartItemActive;
import nautilus.game.minekart.item.KartItemEntity;
import nautilus.game.minekart.item.KartItemManager;
import nautilus.game.minekart.item.world_items_default.RedShell;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.KartUtil;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ActiveShells extends KartItemActive
{
  public ActiveShells(KartItemManager manager, Kart kart, List<KartItemEntity> ents)
  {
    super(manager, kart, nautilus.game.minekart.item.KartItemActive.ActiveType.Orbit, ents);
  }
  

  public boolean Use()
  {
    if (GetEntities().isEmpty()) {
      return true;
    }
    
    Location loc = KartUtil.GetLook(GetKart());
    
    KartItemEntity closest = null;
    double closestDist = 10.0D;
    double dist;
    for (KartItemEntity item : GetEntities())
    {
      dist = UtilMath.offset(item.GetEntity().getLocation(), loc);
      
      if (closest == null)
      {
        closest = item;
        closestDist = dist;


      }
      else if (dist < closestDist)
      {
        closest = item;
        closestDist = dist;
      }
    }
    

    closest.SetHost(null);
    closest.SetVelocity(UtilAlg.Normalize(GetKart().GetDriver().getLocation().getDirection().setY(0)));
    GetEntities().remove(closest);
    

    closest.SetFired();
    

    if ((closest instanceof RedShell))
    {
      Kart target = null;
      
      for (Kart other : GetKart().GetGP().GetKarts())
      {
        if (other.GetLapPlace() + 1 == GetKart().GetLapPlace()) {
          target = other;
        }
      }
      closest.SetTarget(target);
    }
    
    return GetEntities().isEmpty();
  }
}
