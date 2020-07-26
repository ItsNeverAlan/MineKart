package nautilus.game.minekart.menu;

import java.util.ArrayList;
import java.util.List;
import mineplex.core.account.CoreClient;
import mineplex.core.account.CoreClientManager;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.donation.DonationManager;
import mineplex.core.donation.Donor;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.shop.item.ShopItem;
import mineplex.core.shop.page.ShopPageBase;
import nautilus.game.minekart.KartFactory;
import nautilus.game.minekart.gp.GPManager;
import nautilus.game.minekart.gp.GPSet;
import nautilus.game.minekart.item.KartItemType;
import nautilus.game.minekart.kart.KartType;
import nautilus.game.minekart.shop.KartItem;
import net.minecraft.server.v1_6_R3.IInventory;
import net.minecraft.server.v1_6_R3.NBTTagList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class KartPage extends ShopPageBase<KartFactory, KartMenu>
{
  private GPManager _gpManager;
  private org.bukkit.inventory.ItemStack[] _playerInventoryContents;
  
  public KartPage(KartFactory plugin, CoreClientManager clientManager, DonationManager donationManager, KartMenu shop, GPManager gpManager, Player player)
  {
    super(plugin, shop, clientManager, donationManager, " ", player);
    
    this._gpManager = gpManager;
    this._playerInventoryContents = ((org.bukkit.inventory.ItemStack[])player.getInventory().getContents().clone());
    
    BuildPage();
  }
  
  public void PlayerOpened()
  {
    UpdateKarts();
  }
  


  public void PlayerClosed()
  {
    super.PlayerClosed();
    
    this.Player.getInventory().setContents(this._playerInventoryContents);
    
    this.Player.updateInventory();
  }
  

  protected void BuildPage()
  {
    getInventory().setItem(0, new ShopItem(Material.INK_SACK, (byte)1, "", new String[0], 1, false, true).getHandle());
    getInventory().setItem(1, new ShopItem(Material.INK_SACK, (byte)4, "", new String[0], 1, false, true).getHandle());
    getInventory().setItem(2, new ShopItem(Material.INK_SACK, (byte)3, "", new String[0], 1, false, true).getHandle());
    getInventory().setItem(3, new ShopItem(Material.INK_SACK, (byte)6, "", new String[0], 1, false, true).getHandle());
    getInventory().setItem(4, new ShopItem(Material.INK_SACK, (byte)8, "", new String[0], 1, false, true).getHandle());
    getInventory().setItem(5, new ShopItem(Material.INK_SACK, (byte)2, "", new String[0], 1, false, true).getHandle());
    getInventory().setItem(6, new ShopItem(Material.INK_SACK, (byte)12, "", new String[0], 1, false, true).getHandle());
    getInventory().setItem(7, new ShopItem(Material.INK_SACK, (byte)10, "", new String[0], 1, false, true).getHandle());
    getInventory().setItem(8, new ShopItem(Material.INK_SACK, (byte)13, "", new String[0], 1, false, true).getHandle());
    
    UpdateKarts();
    
    ShowKartStats(this._gpManager.GetSelectedKart(this.Player));
  }
  
  public void SelectKart(Player player, KartItem kartItem)
  {
    if ((this.DonationManager.Get(player.getName()).Owns(Integer.valueOf(kartItem.GetSalesPackageId()))) || (kartItem.IsFree()) || (this.DonationManager.Get(this.Client.GetPlayerName()).OwnsUnknownPackage("Minekart ULTRA")) || (this.Client.GetRank().Has(player, Rank.ULTRA, false)))
    {
      ((KartMenu)this.Shop).SelectKart(player, kartItem);
      PlayAcceptSound(player);
      ShowKartStats(this._gpManager.GetSelectedKart(this.Player));
      UpdateKarts();
    }
    else
    {
      PlayDenySound(player);
      ShowKartStats(kartItem.GetKartType());
    }
  }
  
  protected void ShowKartStats(KartType kartType)
  {
    for (KartItem item : ((KartFactory)this.Plugin).GetKarts())
    {
      if (kartType == item.GetKartType())
      {
        PlayerInventory inventory = this.Player.getInventory();
        inventory.clear();
        
        double statValue = Math.ceil((kartType.GetTopSpeed() * 100.0D - 79.0D) / 2.625D);
        Material statMaterial = GetStatMaterial(statValue);
        int slot = 9;
        inventory.setItem(slot, ItemStackFactory.Instance.CreateStack(Material.WOOD_HOE, (byte)0, (int)(kartType.GetTopSpeed() * 100.0D), "§e§lTop Speed"));
        
        slot++;
        while (statValue >= 1.0D)
        {
          inventory.setItem(slot, ItemStackFactory.Instance.CreateStack(statMaterial, (byte)0, 1, "§e§lSpeed Bar"));
          slot++;
          statValue -= 1.0D;
        }
        
        statValue = kartType.GetAcceleration() - 10.0D;
        statMaterial = GetStatMaterial(statValue);
        slot = 18;
        inventory.setItem(slot, ItemStackFactory.Instance.CreateStack(Material.STONE_HOE, (byte)0, (int)statValue, "§e§lAcceleration"));
        
        slot++;
        while (statValue >= 1.0D)
        {
          inventory.setItem(slot, ItemStackFactory.Instance.CreateStack(statMaterial, (byte)0, 1, "§e§lAcceleration Bar"));
          slot++;
          statValue -= 1.0D;
        }
        
        statValue = kartType.GetHandling() - 10.0D;
        statMaterial = GetStatMaterial(statValue);
        slot = 27;
        inventory.setItem(slot, ItemStackFactory.Instance.CreateStack(Material.IRON_HOE, (byte)0, (int)statValue, "§e§lHandling"));
        
        slot++;
        while (statValue >= 1.0D)
        {
          inventory.setItem(slot, ItemStackFactory.Instance.CreateStack(statMaterial, (byte)0, 1, "§e§lHandling Bar"));
          slot++;
          statValue -= 1.0D;
        }
      }
    }
  }
  
  protected boolean IsValidGPSet(GPSet gpSet)
  {
    return (gpSet == GPSet.MushroomCup) || (gpSet == GPSet.Battle);
  }
  
  public void UpdateKarts()
  {
    int slot = 45;
    boolean locked = true;
    
    for (KartItem item : ((KartFactory)this.Plugin).GetKarts())
    {
      if ((this.DonationManager.Get(this.Client.GetPlayerName()).Owns(Integer.valueOf(item.GetSalesPackageId()))) || (this.DonationManager.Get(this.Client.GetPlayerName()).OwnsUnknownPackage("Minekart ULTRA")) || (this.Client.GetRank().Has(this.Client.GetPlayer(), Rank.ULTRA, false)) || (item.IsFree())) {
        locked = false;
      } else {
        locked = true;
      }
      List<String> itemLore = new ArrayList();
      
      if (locked)
      {
        itemLore.add(C.cGreen + "Purchase at Kart Shop");
      }
      
      itemLore.add(C.cBlack);
      
      itemLore.add(C.cWhite + "Max Speed: " + GetStatColor(Math.ceil((item.GetKartType().GetTopSpeed() * 100.0D - 79.0D) / 2.625D)) + item.GetKartType().GetTopSpeed() * 100.0D);
      itemLore.add(C.cWhite + "Acceleration: " + GetStatColor(item.GetKartType().GetAcceleration() - 10.0D) + (item.GetKartType().GetAcceleration() - 10.0D));
      itemLore.add(C.cWhite + "Handling: " + GetStatColor(item.GetKartType().GetHandling() - 10.0D) + (item.GetKartType().GetHandling() - 10.0D));
      itemLore.add(C.cWhite + "Weight: " + GetStatColor(item.GetKartType().GetStability() - 10.0D) + (item.GetKartType().GetStability() - 10.0D));
      
      itemLore.add(C.cBlack);
      
      itemLore.add(C.cWhite + "Special Item: " + C.cYellow + item.GetKartType().GetKartItem().GetName());
      
      ShopItem shopItem = new ShopItem(item.GetDisplayMaterial(), 
        item.GetDisplayData(), item.GetName(), 
        (String[])itemLore.toArray(new String[itemLore.size()]), 1, locked, 
        false);
      
      if (this._gpManager.GetSelectedKart(this.Player) == item.GetKartType())
      {
        shopItem.getHandle().tag.set("ench", new NBTTagList());
        
        KartItemType kartItemType = item.GetKartType().GetKartItem();
        
        ShopItem kartItem = new ShopItem(kartItemType.GetMaterial(), kartItemType.GetName(), kartItemType.GetDesc(), kartItemType.GetAmount(), false);
        kartItem.getHandle().c((locked ? ChatColor.RED : ChatColor.GREEN) + kartItem.GetName() + (locked ? ChatColor.RED + " (Locked)" : ""));
        
        getInventory().setItem(slot - 9, kartItem.getHandle());
      }
      else
      {
        getInventory().setItem(slot - 9, null);
      }
      
      AddButton(slot, shopItem, new KartSelectButton(this, item));
      slot++;
    }
  }
  
  private ChatColor GetStatColor(double statValue)
  {
    return statValue >= 3.0D ? ChatColor.YELLOW : statValue >= 6.0D ? ChatColor.GREEN : ChatColor.RED;
  }
  
  private Material GetStatMaterial(double statValue)
  {
    return statValue >= 3.0D ? Material.GOLD_NUGGET : statValue >= 6.0D ? Material.EMERALD : Material.REDSTONE;
  }
}
