package nautilus.game.minekart.item.use_default;

import java.util.ArrayList;
import mineplex.core.common.util.UtilMath;
import nautilus.game.minekart.item.KartItemManager;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.condition.ConditionData;
import nautilus.game.minekart.kart.condition.ConditionType;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class UseGhost extends ItemUse
{
  public void Use(KartItemManager manager, Kart kart)
  {
    if ((kart.HasCondition(ConditionType.Star)) || (kart.HasCondition(ConditionType.Ghost)) || (kart.HasCondition(ConditionType.Lightning))) {
      return;
    }
    kart.SetItemStored(null);
    
    kart.GetDriver().getWorld().playSound(kart.GetDriver().getLocation(), Sound.GHAST_MOAN, 2.0F, 1.0F);
    
    kart.AddCondition(new ConditionData(ConditionType.Ghost, 8000L));
    
    ArrayList<Kart> steal = new ArrayList();
    
    for (Kart other : kart.GetGP().GetKarts())
    {
      if (!kart.equals(other))
      {

        if (other.GetItemStored() != null)
          steal.add(other);
      }
    }
    if (!steal.isEmpty())
    {
      Kart target = (Kart)steal.get(UtilMath.r(steal.size()));
      kart.SetItemStored(target.GetItemStored());
      target.SetItemStored(null);
      
      target.GetDriver().getWorld().playSound(target.GetDriver().getLocation(), Sound.GHAST_MOAN, 2.0F, 1.0F);
    }
    
    Color color = Color.WHITE;
    

    ItemStack head = new ItemStack(Material.LEATHER_HELMET);
    LeatherArmorMeta meta = (LeatherArmorMeta)head.getItemMeta();
    meta.setColor(color);
    head.setItemMeta(meta);
    
    ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
    meta = (LeatherArmorMeta)chest.getItemMeta();
    meta.setColor(color);
    chest.setItemMeta(meta);
    
    ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
    meta = (LeatherArmorMeta)legs.getItemMeta();
    meta.setColor(color);
    legs.setItemMeta(meta);
    
    ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
    meta = (LeatherArmorMeta)boots.getItemMeta();
    meta.setColor(color);
    boots.setItemMeta(meta);
    
    kart.GetDriver().getInventory().setHelmet(head);
    kart.GetDriver().getInventory().setChestplate(chest);
    kart.GetDriver().getInventory().setLeggings(legs);
    kart.GetDriver().getInventory().setBoots(boots);
  }
}
