package nautilus.game.minekart.menu;

import mineplex.core.account.CoreClientManager;
import mineplex.core.common.CurrencyType;
import mineplex.core.donation.DonationManager;
import mineplex.core.shop.ShopBase;
import mineplex.core.shop.page.ShopPageBase;
import nautilus.game.minekart.KartFactory;
import nautilus.game.minekart.gp.GPManager;
import nautilus.game.minekart.shop.KartItem;
import org.bukkit.entity.Player;

public class KartMenu extends ShopBase<KartFactory>
{
  private GPManager _gpManager;
  
  public KartMenu(KartFactory plugin, CoreClientManager clientManager, DonationManager donationManager, GPManager gpManager)
  {
    super(plugin, clientManager, donationManager, "Kart Select", new CurrencyType[0]);
    
    this._gpManager = gpManager;
  }
  

  protected ShopPageBase<KartFactory, ? extends ShopBase<KartFactory>> BuildPagesFor(Player player)
  {
    return new KartPage((KartFactory)this.Plugin, this.ClientManager, this.DonationManager, this, this._gpManager, player);
  }
  
  public void SelectKart(Player player, KartItem kartItem)
  {
    this._gpManager.SelectKart(player, kartItem.GetKartType());
  }
}
