package nautilus.game.minekart.shop;

import mineplex.core.shop.item.IButton;
import nautilus.game.minekart.shop.page.KartPage;
import org.bukkit.entity.Player;

public class KartItemButton
  implements IButton
{
  private KartPage _shop;
  private KartItem _kartItem;
  
  public KartItemButton(KartPage shop, KartItem kartItem)
  {
    this._shop = shop;
    this._kartItem = kartItem;
  }
  

  public void Clicked(Player player)
  {
    this._shop.PurchaseKart(player, this._kartItem);
  }
}
