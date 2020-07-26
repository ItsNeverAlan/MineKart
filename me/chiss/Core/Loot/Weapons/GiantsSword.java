package me.chiss.Core.Loot.Weapons;

import me.chiss.Core.Loot.LootBase;
import me.chiss.Core.Loot.LootFactory;
import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilEvent.ActionType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;















public class GiantsSword
  extends LootBase
{
  public GiantsSword(LootFactory factory)
  {
    super(factory, "Giants Broadsword", new String[] {"", C.cGray + "Damage: " + C.cYellow + "10", C.cGray + "Ability: " + C.cYellow + "Giant Recovery", C.cGray + "Knockback: " + C.cYellow + "100%", "", C.cWhite + "Forged by the ancient giants. It's blade", C.cWhite + "deals more damage than any other weapon.", C.cWhite + "Upon blocking, it protects its owner.", "" }, Material.IRON_SWORD, UtilEvent.ActionType.R, 3);
  }
  

  public void Damage(CustomDamageEvent event)
  {
    event.AddMod("Negate", GetName(), -event.GetDamageInitial(), false);
    

    event.AddMod(event.GetDamagerPlayer(false).getName(), C.mLoot + GetName(), 10.0D, true);
  }
  







  public void Ability(PlayerInteractEvent event) {}
  






  @EventHandler
  public void Update(UpdateEvent paramUpdateEvent)
  {
    throw new Error("Unresolved compilation problems: \n\tThe method Regen(String, LivingEntity, LivingEntity, double, int, boolean, boolean, boolean) in the type ConditionFactory is not applicable for the arguments (String, Player, Player, double, int, boolean, boolean)\n\tThe method Protection(String, LivingEntity, LivingEntity, double, int, boolean, boolean, boolean) in the type ConditionFactory is not applicable for the arguments (String, Player, Player, double, int, boolean, boolean)\n");
  }
  
  public void ResetCustom(Player player) {}
}
