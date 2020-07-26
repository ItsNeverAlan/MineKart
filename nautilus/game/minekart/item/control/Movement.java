package nautilus.game.minekart.item.control;

import java.lang.reflect.Field;
import java.util.List;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilTime;
import nautilus.game.minekart.item.KartItemEntity;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.condition.ConditionType;
import nautilus.game.minekart.kart.crash.Crash;
import net.minecraft.server.v1_6_R3.EntityInsentient;
import net.minecraft.server.v1_6_R3.EntityLiving;
import net.minecraft.server.v1_6_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_6_R3.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Movement
{
  public static void Move(KartItemEntity item)
  {
    if (item.GetEntity() == null) {
      return;
    }
    if (item.GetVelocity() == null) {
      return;
    }
    if (item.GetVelocity().length() <= 0.0D) {
      return;
    }
    
    item.GetEntity().teleport(item.GetEntity().getLocation());
    

    item.GetEntity().setVelocity(item.GetVelocity());
  }
  
  public static void Behind(Kart kart, List<KartItemEntity> moons)
  {
    for (int i = 0; i < moons.size(); i++)
    {
      Entity cur = ((KartItemEntity)moons.get(i)).GetEntity();
      
      Location target = nautilus.game.minekart.kart.KartUtil.GetBehind(kart);
      
      double offset = UtilMath.offset(cur.getLocation(), target);
      
      Vector vel = UtilAlg.getTrajectory(cur.getLocation(), target);
      
      UtilAction.velocity(cur, vel, offset, false, 0.0D, 0.0D, 10.0D, false);
    }
  }
  
  public static void Orbit(Kart kart, List<KartItemEntity> moons)
  {
    int i = 0;
    for (KartItemEntity item : moons)
    {
      if ((item.GetEntity() instanceof LivingEntity)) {
        ClearGoals((LivingEntity)item.GetEntity());
      }
      double radialLead = i * (6.283185307179586D / moons.size());
      i++;
      
      Location desiredA = GetTarget(kart.GetDriver().getLocation(), kart.GetDriver().getTicksLived(), radialLead);
      Location desiredB = GetTarget(kart.GetDriver().getLocation(), kart.GetDriver().getTicksLived() + 1, radialLead);
      
      double distA = UtilMath.offset(item.GetEntity().getLocation(), desiredA);
      double distB = UtilMath.offset(item.GetEntity().getLocation(), desiredB);
      double distAB = UtilMath.offset(desiredA, desiredB);
      
      if (distA <= distB)
      {

        if (distA >= distAB / 2.0D)
        {

          Vector vel = UtilAlg.getTrajectory(item.GetEntity().getLocation(), desiredA);
          

          Vector kartVel = kart.GetVelocity();
          if ((kart.GetCrash() != null) && (kart.GetCrash().GetVelocity() != null))
            kartVel = kart.GetCrash().GetVelocity();
          kartVel.setY(0);
          
          vel.add(kartVel);
          
          UtilAction.velocity(item.GetEntity(), vel, 0.2D + kartVel.length(), false, 0.0D, 0.0D, 10.0D, false);
        }
      }
    }
  }
  
  private static void ClearGoals(LivingEntity ent) {
    try {
      Field _goalSelector = EntityInsentient.class.getDeclaredField("goalSelector");
      _goalSelector.setAccessible(true);
      Field _targetSelector = EntityInsentient.class.getDeclaredField("targetSelector");
      _targetSelector.setAccessible(true);
      
      EntityLiving creature = ((CraftLivingEntity)ent).getHandle();
      
      PathfinderGoalSelector goalSelector = new PathfinderGoalSelector(((CraftWorld)ent.getWorld()).getHandle().methodProfiler);
      
      _goalSelector.set(creature, goalSelector);
      _targetSelector.set(creature, new PathfinderGoalSelector(((CraftWorld)ent.getWorld()).getHandle().methodProfiler));
    }
    catch (IllegalArgumentException e)
    {
      e.printStackTrace();
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
    }
    catch (NoSuchFieldException e)
    {
      e.printStackTrace();
    }
    catch (SecurityException e)
    {
      e.printStackTrace();
    }
  }
  

  public static Location GetTarget(Location origin, int ticks, double radialLead)
  {
    double speed = 10.0D;
    
    double oX = Math.sin(ticks / speed + radialLead) * 1.5D;
    double oY = 0.5D;
    double oZ = Math.cos(ticks / speed + radialLead) * 1.5D;
    
    return new Location(origin.getWorld(), origin.getX() + oX, origin.getY() + oY, origin.getZ() + oZ);
  }
  
  public static void Trail(Kart kart, List<KartItemEntity> moons)
  {
    for (int i = 0; i < moons.size(); i++)
    {
      Entity cur = ((KartItemEntity)moons.get(i)).GetEntity();
      
      Entity target = kart.GetDriver();
      if (i > 0) {
        target = ((KartItemEntity)moons.get(i - 1)).GetEntity();
      }
      double offset = UtilMath.offset(cur, target);
      
      Vector vel = UtilAlg.getTrajectory(cur, target);
      
      UtilAction.velocity(cur, vel, offset, false, 0.0D, 0.0D, 10.0D, false);
    }
  }
  
  public static void Home(KartItemEntity item)
  {
    if (!UtilTime.elapsed(item.GetFireTime(), 800L)) {
      return;
    }
    if ((item.GetEntity() == null) || (item.GetTarget() == null)) {
      return;
    }
    if (item.GetTarget().HasCondition(ConditionType.Ghost)) {
      return;
    }
    Vector vec = UtilAlg.getTrajectory2d(item.GetEntity(), item.GetTarget().GetDriver());
    UtilAlg.Normalize(vec);
    vec.multiply(1.2D);
    vec.setY(-0.4D);
    
    item.SetVelocity(vec);
  }
}
