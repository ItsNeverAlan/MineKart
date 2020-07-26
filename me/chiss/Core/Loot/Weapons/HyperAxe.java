package me.chiss.Core.Loot.Weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import me.chiss.Core.Loot.LootBase;
import me.chiss.Core.Loot.LootFactory;
import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilEvent.ActionType;
import mineplex.core.common.util.UtilTime;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;


public class HyperAxe
  extends LootBase
{
  private HashMap<Player, Long> _rate = new HashMap();
  












  public HyperAxe(LootFactory factory)
  {
    super(factory, "Hyper Axe", new String[] {"", C.cGray + "Damage: " + C.cYellow + "3", C.cGray + "Ability: " + C.cYellow + "Hyper Speed", C.cGray + "Passive: " + C.cYellow + "Hyper Attack", C.cGray + "Knockback: " + C.cYellow + "25%", "", C.cWhite + "Rumoured to attack foes 500% faster", C.cWhite + "than any other weapon known to man.", "" }, Material.IRON_AXE, UtilEvent.ActionType.R, 1);
  }
  


  public void Damage(CustomDamageEvent event) {}
  


  @EventHandler(priority=EventPriority.LOWEST)
  public void Rate(CustomDamageEvent event)
  {
    if (event.GetCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
      return;
    }
    Player damager = event.GetDamagerPlayer(false);
    if (damager == null) { return;
    }
    if (!GetUsers().contains(damager)) {
      return;
    }
    if (!damager.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
      return;
    }
    
    if ((this._rate.containsKey(damager)) && 
      (!UtilTime.elapsed(((Long)this._rate.get(damager)).longValue(), 80L)))
    {
      event.SetCancelled("Damage Rate");
      return;
    }
    
    this._rate.put(damager, Long.valueOf(System.currentTimeMillis()));
    event.SetIgnoreRate(true);
    
    event.SetCancelled(GetName());
  }
  
  @EventHandler(priority=EventPriority.HIGHEST)
  public void DoDamage(CustomDamageEvent event)
  {
    if (!event.IsCancelled()) {
      return;
    }
    if (event.GetCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
      return;
    }
    Player damager = event.GetDamagerPlayer(false);
    if (damager == null) { return;
    }
    if (!GetUsers().contains(damager)) {
      return;
    }
    if (!event.GetCancellers().remove(GetName())) {
      return;
    }
    if (event.IsCancelled()) {
      return;
    }
    
    event.AddMod("Negate", GetName(), -event.GetDamageInitial(), false);
    

    event.AddMod(damager.getName(), C.mLoot + GetName(), 3.0D, true);
    event.AddKnockback(GetName(), 0.25D);
    damager.getWorld().playSound(damager.getLocation(), Sound.NOTE_PLING, 0.5F, 2.0F);
  }
  






  public void Ability(PlayerInteractEvent paramPlayerInteractEvent)
  {
    throw new Error("Unresolved compilation problems: \n\tRecharge cannot be resolved\n\tThe method Speed(String, LivingEntity, LivingEntity, double, int, boolean, boolean, boolean) in the type ConditionFactory is not applicable for the arguments (String, Player, Player, int, int, boolean, boolean)\n");
  }
  














  @EventHandler
  public void SwingSpeed(UpdateEvent paramUpdateEvent)
  {
    throw new Error("Unresolved compilation problem: \n\tThe method DigFast(String, LivingEntity, LivingEntity, double, int, boolean, boolean, boolean) in the type ConditionFactory is not applicable for the arguments (String, Player, Player, double, int, boolean, boolean)\n");
  }
  

  public void ResetCustom(Player player)
  {
    this._rate.remove(player);
  }
}
