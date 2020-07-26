package nautilus.game.minekart.menu;

import mineplex.core.shop.item.IButton;
import nautilus.game.minekart.shop.KartItem;
import org.bukkit.entity.Player;

public class KartSelectButton
  implements IButton
{
  private KartPage _page;
  private KartItem _kartItem;
  
  public KartSelectButton(KartPage shop, KartItem kartItem)
  {
    this._page = shop;
    this._kartItem = kartItem;
  }
  

  public void Clicked(Player player)
  {
    this._page.SelectKart(player, this._kartItem);
  }
}
