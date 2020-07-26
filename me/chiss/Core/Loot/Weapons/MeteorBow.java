package me.chiss.Core.Loot.Weapons;

import java.util.HashSet;
import me.chiss.Core.Loot.LootBase;
import me.chiss.Core.Loot.LootFactory;
import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilEvent.ActionType;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.condition.ConditionFactory;
import mineplex.minecraft.game.core.condition.ConditionManager;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class MeteorBow extends LootBase
{
  private HashSet<Entity> _arrows = new HashSet();
  











  public MeteorBow(LootFactory factory)
  {
    super(factory, "Meteor Bow", new String[] {"", C.cGray + "Damage: " + C.cYellow + "10 (AoE)", C.cGray + "Passive: " + C.cYellow + "Explosive Arrows", "", C.cWhite + "The mythical bow that reigned down", C.cWhite + "hell from the heavens. Each shot", C.cWhite + "is as deadly as a meteor.", "" }, org.bukkit.Material.BOW, UtilEvent.ActionType.L, 2);
  }
  

  @EventHandler
  public void DamageTrigger(CustomDamageEvent event)
  {
    if (event.IsCancelled()) {
      return;
    }
    Projectile proj = event.GetProjectile();
    if (proj == null) { return;
    }
    Player damager = event.GetDamagerPlayer(true);
    if (damager == null) { return;
    }
    if (!GetUsers().contains(damager)) {
      return;
    }
    event.SetCancelled("Meteor Shot");
  }
  


  public void Damage(CustomDamageEvent event) {}
  


  @EventHandler
  public void ProjectileShoot(EntityShootBowEvent event)
  {
    if (!GetUsers().contains(event.getEntity())) {
      return;
    }
    this._arrows.add(event.getProjectile());
  }
  
  @EventHandler
  public void ProjectileHit(ProjectileHitEvent event)
  {
    Projectile proj = event.getEntity();
    
    if (proj.getShooter() == null) {
      return;
    }
    if (!(proj.getShooter() instanceof Player)) {
      return;
    }
    Player damager = (Player)proj.getShooter();
    
    if ((!this._arrows.contains(proj)) && (!GetUsers().contains(damager))) {
      return;
    }
    
    for (Player cur : UtilPlayer.getNearby(proj.getLocation(), 6.0D)) {
      this.Factory.Condition().Factory().Explosion(C.mLoot + "Meteor Arrow", cur, damager, 10, 1.0D, false, true);
    }
    proj.getWorld().createExplosion(proj.getLocation(), 1.6F);
    proj.remove();
  }
  


  public void Ability(PlayerInteractEvent event) {}
  


  @EventHandler
  public void Clean(UpdateEvent event)
  {
    if (event.getType() != UpdateType.SEC) {
      return;
    }
    HashSet<Entity> remove = new HashSet();
    
    for (Entity cur : this._arrows) {
      if ((cur.isDead()) || (!cur.isValid()))
        remove.add(cur);
    }
    for (Entity cur : remove) {
      this._arrows.remove(cur);
    }
  }
  
  public void ResetCustom(Player player) {}
}
