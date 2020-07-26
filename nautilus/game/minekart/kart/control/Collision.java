package nautilus.game.minekart.kart.control;

import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilPlayer;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.KartState;
import nautilus.game.minekart.kart.KartType;
import nautilus.game.minekart.kart.KartUtil;
import nautilus.game.minekart.kart.condition.ConditionType;
import nautilus.game.minekart.kart.crash.Crash;
import nautilus.game.minekart.kart.crash.Crash_Bump;
import nautilus.game.minekart.kart.crash.Crash_Knockback;
import nautilus.game.minekart.kart.crash.Crash_Spin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Collision
{
  public static void CollideBlock(Kart kart)
  {
    Vector vel = kart.GetVelocity();
    if ((kart.GetCrash() != null) && 
      (kart.GetCrash().GetVelocity() != null)) {
      vel = kart.GetCrash().GetVelocity();
    }
    

    double dist = 0.5D;
    
    boolean done = false;
    
    Block block = kart.GetDriver().getLocation().add(dist, 0.0D, 0.0D).getBlock();
    if ((vel.getX() > 0.0D) && (UtilBlock.solid(block))) { CollideBlock(kart, block, vel, true);done = true;
    }
    block = kart.GetDriver().getLocation().add(-dist, 0.0D, 0.0D).getBlock();
    if ((vel.getX() < 0.0D) && (UtilBlock.solid(block))) { CollideBlock(kart, block, vel, true);done = true;
    }
    block = kart.GetDriver().getLocation().add(0.0D, 0.0D, dist).getBlock();
    if ((vel.getZ() > 0.0D) && (UtilBlock.solid(block))) { CollideBlock(kart, block, vel, false);done = true;
    }
    block = kart.GetDriver().getLocation().add(0.0D, 0.0D, -dist).getBlock();
    if ((vel.getZ() < 0.0D) && (UtilBlock.solid(block))) { CollideBlock(kart, block, vel, false);done = true;
    }
    if (done) {
      return;
    }
    block = kart.GetDriver().getLocation().add(dist, 1.0D, 0.0D).getBlock();
    if ((vel.getX() > 0.0D) && (UtilBlock.solid(block))) { CollideBlock(kart, block, vel, true);
    }
    block = kart.GetDriver().getLocation().add(-dist, 1.0D, 0.0D).getBlock();
    if ((vel.getX() < 0.0D) && (UtilBlock.solid(block))) { CollideBlock(kart, block, vel, true);
    }
    block = kart.GetDriver().getLocation().add(0.0D, 1.0D, dist).getBlock();
    if ((vel.getZ() > 0.0D) && (UtilBlock.solid(block))) { CollideBlock(kart, block, vel, false);
    }
    block = kart.GetDriver().getLocation().add(0.0D, 1.0D, -dist).getBlock();
    if ((vel.getZ() < 0.0D) && (UtilBlock.solid(block))) { CollideBlock(kart, block, vel, false);
    }
  }
  
  public static void CollideBlock(Kart kart, Block block, Vector vel, boolean X)
  {
    double power = vel.getX();
    if (!X) { power = vel.getZ();
    }
    power = Math.abs(power);
    

    if (X) vel.setX(0); else {
      vel.setZ(0);
    }
    if (power < 0.4D) {
      return;
    }
    
    block.getWorld().playSound(kart.GetDriver().getLocation(), org.bukkit.Sound.IRONGOLEM_WALK, (float)power * 3.0F, 1.0F);
    block.getWorld().playEffect(block.getLocation(), org.bukkit.Effect.STEP_SOUND, block.getTypeId());
    

    if (KartUtil.Stability(kart, (int)(power * 20.0D)))
    {
      new Crash_Knockback(kart, block.getLocation().add(0.5D, 0.5D, 0.5D), power);
    }
    else
    {
      new Crash_Bump(kart, block.getLocation().add(0.5D, 0.5D, 0.5D), power);
    }
  }
  
  public static void CollidePlayer(Kart kart)
  {
    if ((kart.HasCondition(ConditionType.Ghost)) || (kart.HasCondition(ConditionType.Star))) {
      return;
    }
    for (Kart other : kart.GetGP().GetKarts())
    {
      if (!other.equals(kart))
      {

        if ((!other.HasCondition(ConditionType.Ghost)) && (!other.HasCondition(ConditionType.Star)))
        {

          if ((kart.GetKartState() == KartState.Drive) || (other.GetKartState() == KartState.Drive))
          {

            if (kart.GetDriver().getWorld().equals(other.GetDriver().getWorld()))
            {

              if (mineplex.core.common.util.UtilMath.offset(kart.GetDriver(), other.GetDriver()) <= 1.0D)
              {

                double collisionVel = 0.0D;
                



                if ((kart.GetVelocity().getX() > 0.0D) && (other.GetDriver().getLocation().getX() > kart.GetDriver().getLocation().getX()))
                {
                  double vel = kart.GetVelocity().getX() - other.GetVelocity().getX();
                  
                  if (vel > 0.0D) {
                    collisionVel += vel;
                  }
                }
                else if ((kart.GetVelocity().getX() < 0.0D) && (other.GetDriver().getLocation().getX() < kart.GetDriver().getLocation().getX()))
                {
                  double vel = kart.GetVelocity().getX() - other.GetVelocity().getX();
                  
                  if (vel < 0.0D) {
                    collisionVel -= vel;
                  }
                }
                
                if ((kart.GetVelocity().getZ() > 0.0D) && (other.GetDriver().getLocation().getZ() > kart.GetDriver().getLocation().getZ()))
                {
                  double vel = kart.GetVelocity().getZ() - other.GetVelocity().getZ();
                  
                  if (vel > 0.0D) {
                    collisionVel += vel;
                  }
                }
                else if ((kart.GetVelocity().getZ() < 0.0D) && (other.GetDriver().getLocation().getZ() < kart.GetDriver().getLocation().getZ()))
                {
                  double vel = kart.GetVelocity().getZ() - other.GetVelocity().getZ();
                  
                  if (vel < 0.0D) {
                    collisionVel -= vel;
                  }
                }
                
                if (collisionVel <= 0.0D) {
                  return;
                }
                collisionVel *= 2.0D;
                

                kart.GetDriver().getWorld().playSound(kart.GetDriver().getLocation(), org.bukkit.Sound.IRONGOLEM_HIT, (float)collisionVel, 1.0F);
                
                double powScale = 0.05D;
                

                double relPower = collisionVel * other.GetKartType().GetStability() / (kart.GetKartType().GetStability() / 10.0D);
                if (KartUtil.Stability(kart, (int)relPower))
                {

                  UtilPlayer.message(kart.GetDriver(), F.main("MK", F.elem(UtilEnt.getName(other.GetDriver())) + " knocked you out."));
                  UtilPlayer.message(other.GetDriver(), F.main("MK", "You knocked out " + F.elem(UtilEnt.getName(kart.GetDriver())) + "."));
                  

                  if (collisionVel > 2.0D) new Crash_Knockback(kart, other.GetDriver().getLocation(), relPower * powScale); else {
                    new Crash_Spin(kart, other.GetDriver().getLocation(), relPower * powScale);
                  }
                }
                else {
                  new Crash_Bump(kart, other.GetDriver().getLocation(), relPower * powScale);
                }
                

                relPower = collisionVel * kart.GetKartType().GetStability() / (other.GetKartType().GetStability() / 10.0D);
                if (KartUtil.Stability(other, (int)relPower))
                {

                  UtilPlayer.message(other.GetDriver(), F.main("MK", F.elem(UtilEnt.getName(kart.GetDriver())) + " knocked you out."));
                  UtilPlayer.message(kart.GetDriver(), F.main("MK", "You knocked out " + F.elem(UtilEnt.getName(other.GetDriver())) + "."));
                  

                  if (collisionVel > 2.0D) new Crash_Knockback(other, kart.GetDriver().getLocation(), relPower * powScale); else {
                    new Crash_Spin(other, kart.GetDriver().getLocation(), relPower * powScale);
                  }
                }
                else {
                  new Crash_Bump(other, kart.GetDriver().getLocation(), relPower * powScale);
                }
              }
            }
          }
        }
      }
    }
  }
}
