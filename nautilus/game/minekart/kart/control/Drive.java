package nautilus.game.minekart.kart.control;

import mineplex.core.common.util.UtilAlg;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.KartType;
import nautilus.game.minekart.kart.KartUtil;
import nautilus.game.minekart.kart.condition.ConditionType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Drive
{
  public static void TopSpeed(Kart kart)
  {
    if (!kart.CanControl()) {
      return;
    }
    if (!KartUtil.IsGrounded(kart)) {
      return;
    }
    if (kart.HasCondition(ConditionType.Boost)) {
      return;
    }
    double topSpeed = kart.GetKartType().GetTopSpeed();
    
    if (kart.HasCondition(ConditionType.Star)) {
      topSpeed *= 1.2D;
    }
    if (kart.GetSpeed() > topSpeed)
    {
      Vector vel = kart.GetVelocityClone();
      vel.setY(0);
      vel.normalize();
      vel.multiply(topSpeed);
      
      kart.SetVelocity(vel);
    }
  }
  
  public static void Accelerate(Kart kart)
  {
    if (!kart.CanControl()) {
      return;
    }
    if (!KartUtil.IsGrounded(kart)) {
      return;
    }
    if (!kart.GetDriver().isBlocking()) {
      return;
    }
    ItemStack item = kart.GetDriver().getItemInHand();
    if ((item == null) || (item.getType() != org.bukkit.Material.STONE_SWORD)) {
      return;
    }
    
    Vector vel = kart.GetVelocity();
    

    if (vel.length() == 0.0D)
    {
      vel = kart.GetDriver().getLocation().getDirection().setY(0);
      

      if (vel.length() == 0.0D) {
        return;
      }
      vel.normalize();
      vel.multiply(0.001D);
      
      kart.SetVelocity(vel);
    }
    

    Vector acc = new Vector(vel.getX(), 0.0D, vel.getZ());
    UtilAlg.Normalize(acc);
    
    double acceleration = kart.GetKartType().GetAcceleration();
    if (kart.HasCondition(ConditionType.Star)) {
      acceleration *= 1.2D;
    }
    acc.multiply(0.001D * acceleration);
    vel.add(acc);
  }
  
  public static void Brake(Kart kart)
  {
    if (!kart.CanControl()) {
      return;
    }
    if (!KartUtil.IsGrounded(kart)) {
      return;
    }
    if (!kart.GetDriver().isBlocking()) {
      return;
    }
    ItemStack item = kart.GetDriver().getItemInHand();
    if ((item == null) || (item.getType() != org.bukkit.Material.WOOD_SWORD)) {
      return;
    }
    
    Vector vel = kart.GetVelocity();
    

    vel.multiply(0.95D);
    
    if (vel.length() < 0.05D) {
      vel.multiply(0);
    }
  }
  
  public static void Turn(Kart kart) {
    if (!kart.CanControl()) {
      return;
    }
    if (!KartUtil.IsGrounded(kart)) {
      return;
    }
    
    Vector vel = kart.GetVelocity();
    
    if (vel.length() <= 0.0D) {
      return;
    }
    double speed = vel.length();
    
    double handling = kart.GetKartType().GetHandling();
    if (kart.HasCondition(ConditionType.Star)) {
      handling *= 1.2D;
    }
    
    Vector turn = kart.GetDriver().getLocation().getDirection();
    turn.setY(0);
    UtilAlg.Normalize(turn);
    
    turn.subtract(UtilAlg.Normalize(new Vector(vel.getX(), 0.0D, vel.getZ())));
    
    turn.multiply(0.003D * handling);
    
    vel.add(turn);
    

    speed = (speed + vel.length() * 3.0D) / 4.0D;
    UtilAlg.Normalize(vel);
    vel.multiply(speed);
  }
  

  public static void Move(Kart kart)
  {
    Vector vel = kart.GetVelocity();
    
    kart.GetDriver().setVelocity(vel);
    

    kart.GetDriver().setExp(Math.min(0.999F, (float)kart.GetSpeed() / (float)kart.GetKartType().GetTopSpeed()));
    kart.GetDriver().setLevel((int)(kart.GetSpeed() * 100.0D));
  }
}
