package me.chiss.Core.Loot.Weapons;

import java.util.HashSet;
import me.chiss.Core.Loot.LootBase;
import me.chiss.Core.Loot.LootFactory;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilEvent.ActionType;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.energy.Energy;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class WindBlade extends LootBase
{
  private HashSet<Player> _active = new HashSet();
  












  public WindBlade(LootFactory factory)
  {
    super(factory, "Wind Blade", new String[] {"", C.cGray + "Damage: " + C.cYellow + "7", C.cGray + "Ability: " + C.cYellow + "Wind Rider", C.cGray + "Knockback: " + C.cYellow + "300%", "", C.cWhite + "Once owned by the God Zephyrus,", C.cWhite + "it is rumoured the Wind Blade", C.cWhite + "grants its owner flight.", "" }, Material.IRON_SWORD, UtilEvent.ActionType.R, 3);
  }
  

  public void Damage(CustomDamageEvent event)
  {
    event.AddMod("Negate", GetName(), -event.GetDamageInitial(), false);
    

    event.AddMod(event.GetDamagerPlayer(false).getName(), C.mLoot + GetName(), 7.0D, true);
    

    event.AddKnockback("Wind Blade", 3.0D);
  }
  

  public void Ability(PlayerInteractEvent event)
  {
    Player player = event.getPlayer();
    
    if (player.getLocation().getBlock().isLiquid())
    {
      UtilPlayer.message(player, F.main(GetName(), "You cannot use " + F.skill("Wind Rider") + " in water."));
      return;
    }
    
    if (!this.Factory.Energy().Use(player, "Wind Rider", 20.0D, true, true)) {
      return;
    }
    this._active.add(player);
  }
  
  @org.bukkit.event.EventHandler
  public void Update(UpdateEvent event)
  {
    if (event.getType() != UpdateType.TICK) {
      return;
    }
    for (Player cur : GetUsers())
    {
      if (this._active.contains(cur))
      {

        if (!cur.isBlocking())
        {
          this._active.remove(cur);


        }
        else if (cur.getLocation().getBlock().isLiquid())
        {
          UtilPlayer.message(cur, F.main(GetName(), "You cannot use " + F.skill("Wind Rider") + " in water."));
          this._active.remove(cur);


        }
        else if (!this.Factory.Energy().Use(cur, "Wind Rider", 2.0D, true, true))
        {
          this._active.remove(cur);
        }
        else
        {
          UtilAction.velocity(cur, 0.6D, 0.1D, 1.0D, true);
          

          cur.getWorld().playEffect(cur.getLocation(), Effect.STEP_SOUND, 80);
          cur.getWorld().playSound(cur.getLocation(), org.bukkit.Sound.FIZZ, 1.2F, 1.5F);
        }
      }
    }
  }
  
  public void ResetCustom(Player player) {
    this._active.remove(player);
  }
}
