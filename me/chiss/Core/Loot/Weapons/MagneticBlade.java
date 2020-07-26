package me.chiss.Core.Loot.Weapons;

import java.util.HashSet;
import me.chiss.Core.Loot.LootBase;
import me.chiss.Core.Loot.LootFactory;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilEvent.ActionType;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.energy.Energy;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class MagneticBlade extends LootBase
{
  private HashSet<Player> _active = new HashSet();
  












  public MagneticBlade(LootFactory factory)
  {
    super(factory, "Magnetic Blade", new String[] {"", C.cGray + "Damage: " + C.cYellow + "7", C.cGray + "Ability: " + C.cYellow + "Magnetic Pull", C.cGray + "Knockback: " + C.cYellow + "Negative 40%", "", C.cWhite + "The Magnetic Blade is said to be able", C.cWhite + "to pull nearby objects towards itself", C.cWhite + "with unstoppable force.", "" }, org.bukkit.Material.IRON_SWORD, UtilEvent.ActionType.R, 2);
  }
  

  public void Damage(CustomDamageEvent event)
  {
    event.AddMod("Negate", GetName(), -event.GetDamageInitial(), false);
    

    event.AddMod(event.GetDamagerPlayer(false).getName(), C.mLoot + GetName(), 7.0D, true);
    

    event.AddKnockback(GetName(), -0.4D);
  }
  

  public void Ability(PlayerInteractEvent event)
  {
    Player player = event.getPlayer();
    
    if (player.getLocation().getBlock().isLiquid())
    {
      UtilPlayer.message(player, F.main(GetName(), "You cannot use " + F.skill(GetName()) + " in water."));
      return;
    }
    
    if (!this.Factory.Energy().Use(player, GetName(), 10.0D, true, true)) {
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
        else if (!cur.getLocation().getBlock().isLiquid())
        {


          Block target = cur.getTargetBlock(null, 0);
          
          if (target != null)
          {

            if (UtilMath.offset(target.getLocation(), cur.getLocation()) <= 16.0D)
            {


              if (!this.Factory.Energy().Use(cur, GetName(), 1.0D, true, true))
              {
                this._active.remove(cur);

              }
              else
              {
                cur.getWorld().playEffect(cur.getLocation(), Effect.STEP_SOUND, 42);
                

                for (Player other : cur.getWorld().getEntitiesByClass(Player.class))
                {

                  if (UtilMath.offset(target.getLocation(), other.getLocation()) <= 3.0D)
                  {


                    if (UtilMath.offset(cur, other) >= 2.0D)
                    {

                      if (this.Factory.Relation().CanHurt(cur, other))
                      {

                        UtilAction.velocity(other, UtilAlg.getTrajectory(other, cur), 
                          0.3D, false, 0.0D, 0.0D, 1.0D, true); } }
                  }
                }
                for (Entity other : cur.getWorld().getEntitiesByClass(Item.class))
                {

                  if (UtilMath.offset(target.getLocation(), other.getLocation()) <= 3.0D)
                  {

                    UtilAction.velocity(other, UtilAlg.getTrajectory(other.getLocation(), cur.getEyeLocation()), 
                      0.3D, false, 0.0D, 0.0D, 1.0D, true); } }
              } } }
        }
      }
    }
  }
  
  public void ResetCustom(Player player) {
    this._active.remove(player);
  }
}
