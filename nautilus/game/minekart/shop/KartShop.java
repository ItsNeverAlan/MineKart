package nautilus.game.minekart.shop;

import mineplex.core.account.CoreClientManager;
import mineplex.core.common.CurrencyType;
import mineplex.core.donation.DonationManager;
import mineplex.core.shop.ShopBase;
import mineplex.core.shop.page.ShopPageBase;
import nautilus.game.minekart.KartFactory;
import nautilus.game.minekart.shop.page.KartPage;
import org.bukkit.entity.Player;

public class KartShop
  extends ShopBase<KartFactory>
{
  public KartShop(KartFactory plugin, CoreClientManager clientManager, DonationManager donationManger, CurrencyType... currencyTypes)
  {
    super(plugin, clientManager, donationManger, "Kart Shop", currencyTypes);
  }
  

  protected ShopPageBase<KartFactory, ? extends ShopBase<KartFactory>> BuildPagesFor(Player player)
  {
    return new KartPage((KartFactory)this.Plugin, this.ClientManager, this.DonationManager, this, player);
  }
}
