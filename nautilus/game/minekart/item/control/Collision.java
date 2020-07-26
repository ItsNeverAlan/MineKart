package nautilus.game.minekart.item.control;

import java.util.Collection;
import java.util.HashSet;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilTime;
import nautilus.game.minekart.item.KartItemEntity;
import nautilus.game.minekart.item.world_items_default.RedShell;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.KartState;
import nautilus.game.minekart.kart.condition.ConditionType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Collision
{
  public static boolean CollideBlock(KartItemEntity item)
  {
    Entity ent = item.GetEntity();
    if (ent == null) { return true;
    }
    
    Vector vel = item.GetVelocity();
    if (vel == null) { return true;
    }
    if (vel.length() <= 0.0D) {
      return true;
    }
    boolean collided = false;
    double range = 0.31D;
    


    Block block = ent.getLocation().add(range, 0.0D, 0.0D).getBlock();
    if ((vel.getX() > 0.0D) && (UtilBlock.solid(block))) { vel.setX(-vel.getX());collided = true;
    }
    block = ent.getLocation().add(-range, 0.0D, 0.0D).getBlock();
    if ((vel.getX() < 0.0D) && (UtilBlock.solid(block))) { vel.setX(-vel.getX());collided = true;
    }
    block = ent.getLocation().add(0.0D, 0.0D, range).getBlock();
    if ((vel.getZ() > 0.0D) && (UtilBlock.solid(block))) { vel.setZ(-vel.getZ());collided = true;
    }
    block = ent.getLocation().add(0.0D, 0.0D, -range).getBlock();
    if ((vel.getZ() < 0.0D) && (UtilBlock.solid(block))) { vel.setZ(-vel.getZ());collided = true;
    }
    return collided;
  }
  
  public static boolean CollidePlayer(KartItemEntity item, Collection<Kart> allKarts)
  {
    if (item.GetEntity() == null) {
      return false;
    }
    for (Kart kart : allKarts)
    {
      if (kart.GetKartState() != KartState.Lakitu)
      {

        if (kart.HasCondition(ConditionType.Ghost))
        {
          if (((item instanceof RedShell)) && 
            (item.GetTarget() != null) && (item.GetTarget().equals(kart))) {
            return true;

          }
          

        }
        else if ((item.GetOwner() != null) && (kart.equals(item.GetOwner())))
        {
          if (item.GetHost() == null)
          {

            if (!UtilTime.elapsed(item.GetFireTime(), 1000L)) {}
          }
          
        }
        else if ((UtilMath.offset(kart.GetDriver(), item.GetEntity()) < item.GetRadius()) && (kart.GetDriver().getWorld() == item.GetEntity().getWorld()))
        {
          item.CollideHandle(kart);
          return true;
        }
      }
    }
    return false;
  }
  
  public static KartItemEntity CollideItem(KartItemEntity item, HashSet<KartItemEntity> allItems)
  {
    if (item.GetEntity() == null) {
      return null;
    }
    for (KartItemEntity other : allItems)
    {
      if (!item.equals(other))
      {


        if ((item.GetVelocity() != null) || (other.GetVelocity() != null))
        {


          if ((item.GetVelocity() == null) || (item.GetVelocity().length() > 0.0D) || (item.GetVelocity() == null) || (item.GetVelocity().length() > 0.0D))
          {


            if ((item.GetHost() == null) || (other.GetHost() == null) || 
              (!item.GetHost().equals(other.GetHost())))
            {


              if ((item.GetOwner() == null) || (other.GetOwner() == null) || (!item.GetOwner().equals(other.GetOwner())) || 
              

                ((item.GetHost() != null) && (other.GetHost() == null) ? 
                
                !UtilTime.elapsed(other.GetFireTime(), 1000L) : 
                




                (item.GetHost() != null) || (other.GetHost() == null) || 
                
                (UtilTime.elapsed(item.GetFireTime(), 1000L))))
              {





                if (UtilMath.offset(other.GetEntity(), item.GetEntity()) < item.GetRadius())
                {
                  return other; } } } }
        }
      }
    }
    return null;
  }
}
