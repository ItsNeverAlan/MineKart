package nautilus.game.minekart.kart.control;

import mineplex.core.common.util.UtilAlg;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.Kart.DriftDirection;
import nautilus.game.minekart.kart.KartType;
import nautilus.game.minekart.kart.KartUtil;
import org.bukkit.Effect;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

public class DriveDrift
{
  public static void Hop(Kart kart, PlayerToggleSneakEvent event)
  {
    if (kart == null) {
      return;
    }
    if (!kart.CanControl()) {
      return;
    }
    if (event.getPlayer().isSneaking())
    {
      Boost(kart);
      kart.ClearDrift();
      return;
    }
    
    if (!KartUtil.IsGrounded(kart)) {
      return;
    }
    
    kart.SetDrift();
  }
  


































  public static void Drift(Kart kart)
  {
    if (!kart.CanControl()) {
      return;
    }
    if (!kart.GetDriver().isSneaking())
    {
      kart.ClearDrift();
      return;
    }
    
    if (!KartUtil.IsGrounded(kart)) {
      return;
    }
    if (kart.GetDrift() == Kart.DriftDirection.None) {
      return;
    }
    
    Vector vel = kart.GetVelocity();
    double speed = kart.GetVelocity().length();
    
    if (speed < 0.5D)
    {
      kart.ClearDrift();
      return;
    }
    

    vel.add(kart.GetDriftVector().multiply(0.03D));
    

    Vector turn = kart.GetDriver().getLocation().getDirection();
    turn.setY(0);
    UtilAlg.Normalize(turn);
    
    turn.subtract(UtilAlg.Normalize(new Vector(vel.getX(), 0.0D, vel.getZ())));
    
    turn.multiply(0.0015D * kart.GetKartType().GetHandling());
    
    vel.add(turn);
    

    UtilAlg.Normalize(vel);
    vel.multiply(speed);
    

    long driftTime = kart.GetDriftTime();
    int effectId = 42;
    if (driftTime > 4000L) { effectId = 57;
    } else if (driftTime > 2500L) { effectId = 41;
    }
    kart.GetDriver().getWorld().playEffect(kart.GetDriver().getLocation().add(0.0D, -1.0D, 0.0D), Effect.STEP_SOUND, effectId);
  }
  
  public static void Boost(Kart kart)
  {
    if (kart.GetDrift() == Kart.DriftDirection.None) {
      return;
    }
    if (kart.GetDriftTime() < 4000L) {
      return;
    }
    kart.AddCondition(new nautilus.game.minekart.kart.condition.ConditionData(nautilus.game.minekart.kart.condition.ConditionType.Boost, 2000L));
    

    kart.GetDriver().getWorld().playEffect(kart.GetDriver().getLocation(), Effect.STEP_SOUND, 57);
    kart.GetDriver().getWorld().playSound(kart.GetDriver().getLocation(), kart.GetKartType().GetSoundMain(), 2.0F, 1.0F);
  }
}
