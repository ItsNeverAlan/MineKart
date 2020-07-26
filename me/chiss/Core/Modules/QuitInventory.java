package me.chiss.Core.Modules;

import java.util.Collection;
import java.util.Iterator;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class QuitInventory
{
  public int[] limSword = { 3 };
  public int[] limSwordEnch = { 12 };
  
  public int[] limAxe = { 3 };
  public int[] limAxeEnch = { 12 };
  
  public int[] limBow = { 2 };
  public int[] limBowEnch = { 8 };
  public int[] limBowArrow = { 128 };
  
  public int[] limHelm = { 3 };
  public int[] limHelmEnch = { 8 };
  
  public int[] limChest = { 3 };
  public int[] limChestEnch = { 8 };
  
  public int[] limLeg = { 3 };
  public int[] limLegEnch = { 8 };
  
  public int[] limBoot = { 3 };
  public int[] limBootEnch = { 8 };
  
  public int[] limTNT = { 8 };
  
  public int[] limMaterial = { 128 };
  public int[] limEmerald = { 64 };
  private Quit Quit;
  
  public QuitInventory(Quit quit)
  {
    this.Quit = quit;
  }
  
  public int CountEnch(ItemStack stack)
  {
    int count = 0;
    
    for (Iterator localIterator = stack.getEnchantments().values().iterator(); localIterator.hasNext();) { int cur = ((Integer)localIterator.next()).intValue();
      count += cur;
    }
    return count;
  }
  
  public boolean Check(Player player)
  {
    PlayerInventory inv = player.getInventory();
    
    for (ItemStack cur : inv.getContents())
    {
      if ((cur != null) && 
        (cur.getType() != Material.AIR))
      {

        if (cur.getType() == Material.IRON_SWORD) { this.limSword[1] += 1;this.limSwordEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.GOLD_SWORD) { this.limSword[1] += 1;this.limSwordEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.DIAMOND_SWORD) { this.limSword[1] += 1;this.limSwordEnch[1] += CountEnch(cur);

        }
        else if (cur.getType() == Material.IRON_AXE) { this.limAxe[1] += 1;this.limAxeEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.GOLD_AXE) { this.limAxe[1] += 1;this.limAxeEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.DIAMOND_AXE) { this.limSword[1] += 1;this.limSwordEnch[1] += CountEnch(cur);

        }
        else if (cur.getType() == Material.BOW) { this.limBow[1] += 1;this.limBowEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.ARROW) { this.limBowArrow[1] += cur.getAmount();

        }
        else if (cur.getType() == Material.IRON_HELMET) { this.limHelm[1] += 1;this.limHelmEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.GOLD_HELMET) { this.limHelm[1] += 1;this.limHelmEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.DIAMOND_HELMET) { this.limHelm[1] += 1;this.limHelmEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.LEATHER_HELMET) { this.limHelm[1] += 1;this.limHelmEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.CHAINMAIL_HELMET) { this.limHelm[1] += 1;this.limHelmEnch[1] += CountEnch(cur);

        }
        else if (cur.getType() == Material.IRON_CHESTPLATE) { this.limChest[1] += 1;this.limChestEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.GOLD_CHESTPLATE) { this.limChest[1] += 1;this.limChestEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.DIAMOND_CHESTPLATE) { this.limChest[1] += 1;this.limChestEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.LEATHER_CHESTPLATE) { this.limChest[1] += 1;this.limChestEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.CHAINMAIL_CHESTPLATE) { this.limChest[1] += 1;this.limChestEnch[1] += CountEnch(cur);

        }
        else if (cur.getType() == Material.IRON_LEGGINGS) { this.limLeg[1] += 1;this.limLegEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.GOLD_LEGGINGS) { this.limLeg[1] += 1;this.limLegEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.DIAMOND_LEGGINGS) { this.limLeg[1] += 1;this.limLegEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.LEATHER_LEGGINGS) { this.limLeg[1] += 1;this.limLegEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.CHAINMAIL_LEGGINGS) { this.limLeg[1] += 1;this.limLegEnch[1] += CountEnch(cur);

        }
        else if (cur.getType() == Material.IRON_BOOTS) { this.limBoot[1] += 1;this.limBootEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.GOLD_BOOTS) { this.limBoot[1] += 1;this.limBootEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.DIAMOND_BOOTS) { this.limBoot[1] += 1;this.limBootEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.LEATHER_BOOTS) { this.limBoot[1] += 1;this.limBootEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.CHAINMAIL_BOOTS) { this.limBoot[1] += 1;this.limBootEnch[1] += CountEnch(cur);

        }
        else if (cur.getType() == Material.TNT) { this.limTNT[1] += 1;
        } else if (cur.getType() == Material.IRON_ORE) { this.limMaterial[1] += 1;
        } else if (cur.getType() == Material.IRON_INGOT) { this.limMaterial[1] += 1;
        } else if (cur.getType() == Material.IRON_BLOCK) { this.limMaterial[1] += 9;
        } else if (cur.getType() == Material.GOLD_ORE) { this.limMaterial[1] += 1;
        } else if (cur.getType() == Material.GOLD_INGOT) { this.limMaterial[1] += 1;
        } else if (cur.getType() == Material.GOLD_BLOCK) { this.limMaterial[1] += 9;
        } else if (cur.getType() == Material.DIAMOND) { this.limMaterial[1] += 1;
        } else if (cur.getType() == Material.DIAMOND_BLOCK) { this.limMaterial[1] += 9;
        } else if (cur.getType() == Material.LEATHER) { this.limMaterial[1] += 1;
        } else if (cur.getType() == Material.EMERALD) { this.limEmerald[1] += 1;
        } else if (cur.getType() == Material.EMERALD_BLOCK) { this.limEmerald[1] += 9;
        } }
    }
    for (ItemStack cur : inv.getArmorContents())
    {
      if ((cur != null) && 
        (cur.getType() != Material.AIR))
      {

        if (cur.getType() == Material.IRON_HELMET) { this.limHelm[1] += 1;this.limHelmEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.GOLD_HELMET) { this.limHelm[1] += 1;this.limHelmEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.DIAMOND_HELMET) { this.limHelm[1] += 1;this.limHelmEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.LEATHER_HELMET) { this.limHelm[1] += 1;this.limHelmEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.CHAINMAIL_HELMET) { this.limHelm[1] += 1;this.limHelmEnch[1] += CountEnch(cur);

        }
        else if (cur.getType() == Material.IRON_CHESTPLATE) { this.limChest[1] += 1;this.limChestEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.GOLD_CHESTPLATE) { this.limChest[1] += 1;this.limChestEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.DIAMOND_CHESTPLATE) { this.limChest[1] += 1;this.limChestEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.LEATHER_CHESTPLATE) { this.limChest[1] += 1;this.limChestEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.CHAINMAIL_CHESTPLATE) { this.limChest[1] += 1;this.limChestEnch[1] += CountEnch(cur);

        }
        else if (cur.getType() == Material.IRON_LEGGINGS) { this.limLeg[1] += 1;this.limLegEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.GOLD_LEGGINGS) { this.limLeg[1] += 1;this.limLegEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.DIAMOND_LEGGINGS) { this.limLeg[1] += 1;this.limLegEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.LEATHER_LEGGINGS) { this.limLeg[1] += 1;this.limLegEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.CHAINMAIL_LEGGINGS) { this.limLeg[1] += 1;this.limLegEnch[1] += CountEnch(cur);

        }
        else if (cur.getType() == Material.IRON_BOOTS) { this.limBoot[1] += 1;this.limBootEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.GOLD_BOOTS) { this.limBoot[1] += 1;this.limBootEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.DIAMOND_BOOTS) { this.limBoot[1] += 1;this.limBootEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.LEATHER_BOOTS) { this.limBoot[1] += 1;this.limBootEnch[1] += CountEnch(cur);
        } else if (cur.getType() == Material.CHAINMAIL_BOOTS) { this.limBoot[1] += 1;this.limBootEnch[1] += CountEnch(cur);
        } }
    }
    boolean valid = true;
    

    if (this.limSword[1] > this.limSword[0])
    {
      UtilPlayer.message(player, F.main(this.Quit.GetName(), "You have too many Swords [" + this.limSword[1] + "/" + this.limSword[0] + "]."));
      valid = false;
    }
    
    if (this.limSwordEnch[1] > this.limSwordEnch[0])
    {
      UtilPlayer.message(player, F.main(this.Quit.GetName(), "You have too many Sword Enchantments [" + this.limSwordEnch[1] + "/" + this.limSwordEnch[0] + "]."));
      valid = false;
    }
    
    if (this.limAxe[1] > this.limAxe[0])
    {
      UtilPlayer.message(player, F.main(this.Quit.GetName(), "You have too many Axes [" + this.limAxe[1] + "/" + this.limAxe[0] + "]."));
      valid = false;
    }
    
    if (this.limAxeEnch[1] > this.limAxeEnch[0])
    {
      UtilPlayer.message(player, F.main(this.Quit.GetName(), "You have too many Axe Enchantments [" + this.limAxeEnch[1] + "/" + this.limAxeEnch[0] + "]."));
      valid = false;
    }
    
    if (this.limBow[1] > this.limBow[0])
    {
      UtilPlayer.message(player, F.main(this.Quit.GetName(), "You have too many Bows [" + this.limBow[1] + "/" + this.limBow[0] + "]."));
      valid = false;
    }
    
    if (this.limBowEnch[1] > this.limBowEnch[0])
    {
      UtilPlayer.message(player, F.main(this.Quit.GetName(), "You have too many Bow Enchantments [" + this.limBowEnch[1] + "/" + this.limBowEnch[0] + "]."));
      valid = false;
    }
    
    if (this.limBowArrow[1] > this.limBowArrow[0])
    {
      UtilPlayer.message(player, F.main(this.Quit.GetName(), "You have too many Arrows [" + this.limBowArrow[1] + "/" + this.limBowArrow[0] + "]."));
      valid = false;
    }
    

    if (this.limHelm[1] > this.limHelm[0])
    {
      UtilPlayer.message(player, F.main(this.Quit.GetName(), "You have too many Helmets [" + this.limHelm[1] + "/" + this.limHelm[0] + "]."));
      valid = false;
    }
    
    if (this.limHelmEnch[1] > this.limHelmEnch[0])
    {
      UtilPlayer.message(player, F.main(this.Quit.GetName(), "You have too many Helmet Enchantments [" + this.limHelmEnch[1] + "/" + this.limHelmEnch[0] + "]."));
      valid = false;
    }
    
    if (this.limChest[1] > this.limChest[0])
    {
      UtilPlayer.message(player, F.main(this.Quit.GetName(), "You have too many Chestplates [" + this.limChest[1] + "/" + this.limChest[0] + "]."));
      valid = false;
    }
    
    if (this.limChestEnch[1] > this.limChestEnch[0])
    {
      UtilPlayer.message(player, F.main(this.Quit.GetName(), "You have too many Chestplate Enchantments [" + this.limChestEnch[1] + "/" + this.limChestEnch[0] + "]."));
      valid = false;
    }
    
    if (this.limLeg[1] > this.limLeg[0])
    {
      UtilPlayer.message(player, F.main(this.Quit.GetName(), "You have too many Leggings [" + this.limLeg[1] + "/" + this.limLeg[0] + "]."));
      valid = false;
    }
    
    if (this.limLegEnch[1] > this.limLegEnch[0])
    {
      UtilPlayer.message(player, F.main(this.Quit.GetName(), "You have too many Legging Enchantments [" + this.limLegEnch[1] + "/" + this.limLegEnch[0] + "]."));
      valid = false;
    }
    
    if (this.limBoot[1] > this.limBoot[0])
    {
      UtilPlayer.message(player, F.main(this.Quit.GetName(), "You have too many Boots [" + this.limBoot[1] + "/" + this.limBoot[0] + "]."));
      valid = false;
    }
    
    if (this.limBootEnch[1] > this.limBootEnch[0])
    {
      UtilPlayer.message(player, F.main(this.Quit.GetName(), "You have too many Boot Enchantments [" + this.limBootEnch[1] + "/" + this.limBootEnch[0] + "]."));
      valid = false;
    }
    

    if (this.limTNT[1] > this.limTNT[0])
    {
      UtilPlayer.message(player, F.main(this.Quit.GetName(), "You have too much TNT [" + this.limTNT[1] + "/" + this.limTNT[0] + "]."));
      valid = false;
    }
    
    if (this.limMaterial[1] > this.limMaterial[0])
    {
      UtilPlayer.message(player, F.main(this.Quit.GetName(), "You have too much Ore/Material [" + this.limMaterial[1] + "/" + this.limMaterial[0] + "]."));
      valid = false;
    }
    
    if (this.limEmerald[1] > this.limEmerald[0])
    {
      UtilPlayer.message(player, F.main(this.Quit.GetName(), "You have too many Emeralds [" + this.limEmerald[1] + "/" + this.limEmerald[0] + "]."));
      valid = false;
    }
    
    return valid;
  }
}
