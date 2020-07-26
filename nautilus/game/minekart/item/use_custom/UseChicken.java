package nautilus.game.minekart.item.use_custom;

import nautilus.game.minekart.item.KartItemManager;
import nautilus.game.minekart.kart.Kart;
import org.bukkit.Location;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

public class UseChicken extends nautilus.game.minekart.item.use_default.ItemUse
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
    
    Egg egg = (Egg)kart.GetDriver().launchProjectile(Egg.class);
    
    Vector vel = kart.GetDriver().getLocation().getDirection();
    vel.setY(0.1D);
    vel.multiply(2);
    egg.setVelocity(vel);
    
    kart.GetDriver().getWorld().playSound(kart.GetDriver().getLocation(), org.bukkit.Sound.CHICKEN_EGG_POP, 2.0F, 1.0F);
  }
}
