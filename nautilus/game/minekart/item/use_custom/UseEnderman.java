package nautilus.game.minekart.item.use_custom;

import mineplex.core.common.util.UtilBlock;
import nautilus.game.minekart.item.KartItemManager;
import nautilus.game.minekart.kart.Kart;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

public class UseEnderman extends nautilus.game.minekart.item.use_default.ItemUse
{
  public void Use(KartItemManager manager, Kart kart)
  {
    if (kart.GetDriver().getInventory().getItem(3) == null)
    {
      kart.SetItemStored(null);
    }
    else if (kart.GetDriver().getInventory().getItem(3).getAmount() > 1)
    {
      kart.GetDriver().getInventory().getItem(3).setAmount(kart.GetDriver().getInventory().getItem(3).getAmount() - 1);
    }
    else
    {
      kart.SetItemStored(null);
    }
    
    Player player = kart.GetDriver();
    

    Block lastEffect = player.getLocation().getBlock();
    
    double maxRange = 20.0D;
    double curRange = 0.0D;
    while (curRange <= maxRange)
    {
      Vector look = player.getLocation().getDirection();
      look.setY(0);
      look.normalize();
      look.multiply(curRange);
      
      Location newTarget = player.getLocation().add(0.0D, 0.1D, 0.0D).add(look);
      
      if (UtilBlock.solid(newTarget.getBlock())) {
        break;
      }
      
      curRange += 0.2D;
      

      if (!lastEffect.equals(newTarget.getBlock())) {
        lastEffect.getWorld().playEffect(lastEffect.getLocation(), Effect.STEP_SOUND, 49);
      }
      lastEffect = newTarget.getBlock();
    }
    

    curRange -= 0.4D;
    if (curRange < 0.0D) {
      curRange = 0.0D;
    }
    
    Vector look = player.getLocation().getDirection();
    look.setY(0);
    look.normalize();
    look.multiply(curRange);
    
    Location loc = player.getLocation().add(look).add(new Vector(0.0D, 0.4D, 0.0D));
    

    kart.GetDriver().getWorld().playSound(kart.GetDriver().getLocation(), Sound.ZOMBIE_UNFECT, 2.0F, 2.0F);
    
    Entity item = player.getPassenger();
    if (item != null) item.leaveVehicle();
    player.eject();
    

    if (curRange > 0.0D)
    {
      player.leaveVehicle();
      player.teleport(loc);
    }
    

    if (item != null)
    {
      item.teleport(loc.add(0.0D, 1.5D, 0.0D));
      player.setPassenger(item);
    }
    

    double length = kart.GetVelocity().length();
    Vector vel = player.getLocation().getDirection();
    vel.setY(0);
    vel.normalize();
    vel.multiply(length);
    kart.SetVelocity(vel);
  }
}
