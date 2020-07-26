package me.chiss.Core.Loot.Weapons;

import java.util.HashSet;
import me.chiss.Core.Loot.LootBase;
import me.chiss.Core.Loot.LootFactory;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilEvent.ActionType;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.energy.Energy;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;


public class AlligatorsTooth
  extends LootBase
{
  private HashSet<Player> _active = new HashSet();
  














  public AlligatorsTooth(LootFactory factory)
  {
    super(factory, "Alligators Tooth", new String[] {"", C.cGray + "Damage: " + C.cYellow + "7 + 2 in Water", C.cGray + "Ability: " + C.cYellow + "Alliagtor Rush", C.cGray + "Passive: " + C.cYellow + "Water Breathing", C.cGray + "Knockback: " + C.cYellow + "100%", "", C.cWhite + "A blade forged from hundreds of", C.cWhite + "alligators teeth. It's powers allow ", C.cWhite + "its owner to swim with great speed,", C.cWhite + "able to catch any prey.", "" }, Material.IRON_SWORD, UtilEvent.ActionType.R, 1);
  }
  

  public void Damage(CustomDamageEvent event)
  {
    event.AddMod("Negate", GetName(), -event.GetDamageInitial(), false);
    

    event.AddMod(event.GetDamagerPlayer(false).getName(), C.mLoot + GetName(), 7.0D, true);
    
    if (event.GetDamageeEntity().getLocation().getBlock().isLiquid())
    {
      event.AddMod(event.GetDamagerPlayer(false).getName(), C.mLoot + "Water Bonus", 2.0D, false);
      event.AddKnockback(GetName(), 0.5D);
    }
  }
  

  public void Ability(PlayerInteractEvent event)
  {
    Player player = event.getPlayer();
    
    if (!player.getLocation().getBlock().isLiquid())
    {
      UtilPlayer.message(player, F.main(GetName(), "You cannot use " + F.skill("Alligator Rush") + " out of water."));
      return;
    }
    
    if (!this.Factory.Energy().Use(player, "Alligator Rush", 10.0D, true, true)) {
      return;
    }
    this._active.add(player);
  }
  


































  @EventHandler
  public void Update(UpdateEvent paramUpdateEvent)
  {
    throw new Error("Unresolved compilation problem: \n\tThe method Breath(String, LivingEntity, LivingEntity, double, int, boolean, boolean, boolean) in the type ConditionFactory is not applicable for the arguments (String, Player, Player, double, int, boolean, boolean)\n");
  }
  

  public void ResetCustom(Player player)
  {
    this._active.remove(player);
  }
}
