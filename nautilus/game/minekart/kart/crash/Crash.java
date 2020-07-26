package nautilus.game.minekart.kart.crash;

import mineplex.core.common.util.UtilTime;
import nautilus.game.minekart.kart.Kart;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Crash
{
  private Vector _velocity;
  private long _crashTime;
  private long _crashReq;
  private boolean _canEnd;
  private boolean _restoreStability;
  
  public Crash(Kart kart, Vector vel, long timeReq, boolean canEnd, boolean restoreStability)
  {
    kart.SetCrash(this);
    kart.ClearDrift();
    
    this._velocity = vel;
    this._crashTime = System.currentTimeMillis();
    this._crashReq = timeReq;
    this._canEnd = canEnd;
    this._restoreStability = restoreStability;
    
    kart.GetDriver().playEffect(EntityEffect.HURT);
  }
  
  public void Update(Kart kart)
  {
    Move(kart);
  }
  
  public void Move(Kart kart)
  {
    kart.GetDriver().setVelocity(this._velocity);
    

    kart.GetDriver().setExp(Math.min(0.999F, (float)this._velocity.length() / 1.8F));
  }
  
  public boolean CrashEnd()
  {
    return (this._canEnd) && (UtilTime.elapsed(this._crashTime, this._crashReq));
  }
  
  public Vector GetVelocity()
  {
    return this._velocity;
  }
  
  public void SetVelocity(Vector vel)
  {
    this._velocity = vel;
  }
  
  public long GetCrashTime()
  {
    return this._crashTime;
  }
  
  public long GetCrashReq()
  {
    return this._crashReq;
  }
  
  public void SetCanEnd(boolean end)
  {
    this._canEnd = end;
  }
  
  public void SetCrashReq(long time)
  {
    this._crashReq = time;
  }
  
  public boolean StabilityRestore()
  {
    return this._restoreStability;
  }
}
