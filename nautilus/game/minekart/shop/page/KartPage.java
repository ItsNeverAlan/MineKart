package nautilus.game.minekart.shop.page;

import java.util.List;
import mineplex.core.account.CoreClient;
import mineplex.core.account.CoreClientManager;
import mineplex.core.common.CurrencyType;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.donation.DonationManager;
import mineplex.core.donation.Donor;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.shop.item.ShopItem;
import mineplex.core.shop.page.ConfirmationPage;
import mineplex.core.shop.page.ShopPageBase;
import nautilus.game.minekart.KartFactory;
import nautilus.game.minekart.kart.KartType;
import nautilus.game.minekart.shop.KartItem;
import nautilus.game.minekart.shop.KartItemButton;
import nautilus.game.minekart.shop.KartShop;
import net.minecraft.server.v1_6_R3.IInventory;
import net.minecraft.server.v1_6_R3.NBTTagCompound;
import net.minecraft.server.v1_6_R3.NBTTagList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class KartPage extends ShopPageBase<KartFactory, KartShop>
{
  public KartPage(KartFactory plugin, CoreClientManager clientManager, DonationManager donationManager, KartShop shop, Player player)
  {
    super(plugin, shop, clientManager, donationManager, "     Kart Shop", player);
    
    this.CurrencySlot = 22;
    
    BuildPage();
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
  }
  
  public void PlayerOpened()
  {
    UpdateKarts();
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
      List<String> itemLore = new java.util.ArrayList();
      
      if (locked)
      {
        StringBuilder currencyBuilder = new StringBuilder();
        
        int cost = item.GetCost(CurrencyType.Gems);
        
        if (cost > 0)
        {
          currencyBuilder.append(C.cYellow + cost + " Gems");
        }
        
        itemLore.add(currencyBuilder.toString());
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
      
      if (!locked)
      {
        shopItem.getHandle().tag.set("ench", new NBTTagList());
      }
      
      AddButton(slot, shopItem, new KartItemButton(this, item));
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
  
  public void PurchaseKart(Player player, KartItem kartItem)
  {
    if ((this.DonationManager.Get(this.Client.GetPlayerName()).GetGems() > kartItem.GetCost(CurrencyType.Gems)) && (!this.DonationManager.Get(this.Client.GetPlayerName()).Owns(Integer.valueOf(kartItem.GetSalesPackageId()))) && (!this.DonationManager.Get(this.Client.GetPlayerName()).OwnsUnknownPackage("Minekart ULTRA")) && (!this.Client.GetRank().Has(this.Client.GetPlayer(), Rank.ULTRA, false)) && (!kartItem.IsFree()))
    {
      PlayAcceptSound(player);
      ((KartShop)this.Shop).OpenPageForPlayer(player, new ConfirmationPage((KartFactory)this.Plugin, (KartShop)this.Shop, this.ClientManager, this.DonationManager, null, 
        this, kartItem, this.SelectedCurrency, player));
    }
    else
    {
      PlayDenySound(player);
      ShowKartStats(kartItem.GetKartType());
    }
  }
}
