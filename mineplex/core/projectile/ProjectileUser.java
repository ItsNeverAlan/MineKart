package mineplex.core.projectile;

import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.disguise.DisguiseManager;
import mineplex.core.disguise.disguises.DisguiseSquid;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import net.minecraft.server.v1_6_R3.WorldServer;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ProjectileUser
{
  public ProjectileManager Throw;
  private org.bukkit.entity.Entity _thrown;
  private LivingEntity _thrower;
  private IThrown _callback;
  private long _expireTime;
  private boolean _hitPlayer = false;
  private boolean _hitBlock = false;
  private boolean _idle = false;
  private boolean _pickup = false;
  
  private Sound _sound = null;
  private float _soundVolume = 1.0F;
  private float _soundPitch = 1.0F;
  private UtilParticle.ParticleType _particle = null;
  private Effect _effect = null;
  private int _effectData = 0;
  private UpdateType _effectRate = UpdateType.TICK;
  
  private double _hitboxMult = 1.0D;
  


  private DisguiseManager _disguise;
  


  public ProjectileUser(ProjectileManager throwInput, org.bukkit.entity.Entity thrown, LivingEntity thrower, IThrown callback, long expireTime, boolean hitPlayer, boolean hitBlock, boolean idle, boolean pickup, Sound sound, float soundVolume, float soundPitch, Effect effect, int effectData, UpdateType effectRate, UtilParticle.ParticleType particle, double hitboxMult, DisguiseManager disguise)
  {
    this.Throw = throwInput;
    
    this._thrown = thrown;
    this._thrower = thrower;
    this._callback = callback;
    
    this._expireTime = expireTime;
    this._hitPlayer = hitPlayer;
    this._hitBlock = hitBlock;
    this._idle = idle;
    this._pickup = pickup;
    
    this._sound = sound;
    this._soundVolume = soundVolume;
    this._soundPitch = soundPitch;
    this._particle = particle;
    this._effect = effect;
    this._effectData = effectData;
    this._effectRate = effectRate;
    
    this._hitboxMult = hitboxMult;
    this._disguise = disguise;
  }
  
  public void Effect(UpdateEvent event)
  {
    if (event.getType() != this._effectRate) {
      return;
    }
    if (this._sound != null) {
      this._thrown.getWorld().playSound(this._thrown.getLocation(), this._sound, this._soundVolume, this._soundPitch);
    }
    if (this._effect != null) {
      this._thrown.getWorld().playEffect(this._thrown.getLocation(), this._effect, this._effectData);
    }
    if (this._particle != null) {
      UtilParticle.PlayParticle(this._particle, this._thrown.getLocation(), 0.0F, 0.0F, 0.0F, 0.0F, 1);
    }
  }
  
  public boolean Collision()
  {
    if ((this._expireTime != -1L) && (System.currentTimeMillis() > this._expireTime))
    {
      this._callback.Expire(this);
      return true;
    }
    


    if (this._hitPlayer)
    {
      for (Object entity : ((CraftWorld)this._thrown.getWorld()).getHandle().entityList)
      {
        if ((entity instanceof net.minecraft.server.v1_6_R3.Entity))
        {
          org.bukkit.entity.Entity bukkitEntity = ((net.minecraft.server.v1_6_R3.Entity)entity).getBukkitEntity();
          
          if ((bukkitEntity instanceof LivingEntity))
          {
            LivingEntity ent = (LivingEntity)bukkitEntity;
            

            if (!ent.equals(this._thrower))
            {

              if ((!(ent instanceof Player)) || 
                (((Player)ent).getGameMode() != GameMode.CREATIVE))
              {

                EntityType disguise = null;
                if ((this._disguise != null) && (this._disguise.getDisguise(ent) != null))
                {
                  if ((this._disguise.getDisguise(ent) instanceof DisguiseSquid)) {
                    disguise = EntityType.SQUID;
                  }
                }
                
                if (UtilEnt.hitBox(this._thrown.getLocation(), ent, this._hitboxMult, disguise))
                {
                  this._callback.Collide(ent, null, this);
                  return true;
                }
              }
            }
          }
        }
      }
    }
    try
    {
      if (this._hitBlock)
      {
        Block block = this._thrown.getLocation().add(this._thrown.getVelocity().normalize().multiply(0.6D)).getBlock();
        if ((!UtilBlock.airFoliage(block)) && (!block.isLiquid()))
        {
          this._callback.Collide(null, block, this);
          return true;
        }
      }
      


      if (this._idle)
      {
        if ((this._thrown.getVelocity().length() < 0.2D) && 
          (!UtilBlock.airFoliage(this._thrown.getLocation().getBlock().getRelative(BlockFace.DOWN))))
        {
          this._callback.Idle(this);
          return true;
        }
      }
    }
    catch (Exception ex)
    {
      if (this._hitBlock)
      {
        return true;
      }
      
      if (this._idle)
      {
        return true;
      }
    }
    
    return false;
  }
  
  public LivingEntity GetThrower()
  {
    return this._thrower;
  }
  
  public org.bukkit.entity.Entity GetThrown()
  {
    return this._thrown;
  }
  
  public boolean CanPickup(LivingEntity thrower)
  {
    if (!thrower.equals(this._thrower)) {
      return false;
    }
    return this._pickup;
  }
}
