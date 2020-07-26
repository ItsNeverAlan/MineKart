package me.chiss.Core.Loot.Weapons;

import me.chiss.Core.Loot.LootBase;
import me.chiss.Core.Loot.LootFactory;
import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilEvent.ActionType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.condition.ConditionFactory;
import mineplex.minecraft.game.core.condition.ConditionManager;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;



















public class LightningScythe
  extends LootBase
{
  public LightningScythe(LootFactory factory)
  {
    super(factory, "Lightning Scythe", new String[] {"", C.cGray + "Damage: " + C.cYellow + "7", C.cGray + "Ability: " + C.cYellow + "Lightning Strike", C.cGray + "Passive: " + C.cYellow + "Lightning Speed", C.cGray + "Passive: " + C.cYellow + "Electric Shock", C.cGray + "Knockback: " + C.cYellow + "100%", "", C.cWhite + "An old scythe that is infused with the", C.cWhite + "powers of the skies, able to strike ", C.cWhite + "lightning at the owners command.", "" }, Material.IRON_HOE, UtilEvent.ActionType.R, 2);
  }
  

  public void Damage(CustomDamageEvent event)
  {
    event.AddMod("Negate", GetName(), -event.GetDamageInitial(), false);
    

    event.AddMod(event.GetDamagerPlayer(false).getName(), C.mLoot + GetName(), 7.0D, true);
    

    event.SetKnockback(false);
    

    this.Factory.Condition().Factory().Shock(GetName(), event.GetDamageeEntity(), event.GetDamagerPlayer(false), 2.0D, false, false);
  }
  

















  public void Ability(PlayerInteractEvent paramPlayerInteractEvent)
  {
    throw new Error("Unresolved compilation problem: \n\tRecharge cannot be resolved\n");
  }
  

























  @EventHandler
  public void Update(UpdateEvent paramUpdateEvent)
  {
    throw new Error("Unresolved compilation problem: \n\tThe method Speed(String, LivingEntity, LivingEntity, double, int, boolean, boolean, boolean) in the type ConditionFactory is not applicable for the arguments (String, Player, Player, double, int, boolean, boolean)\n");
  }
  
  public void ResetCustom(Player player) {}
}
