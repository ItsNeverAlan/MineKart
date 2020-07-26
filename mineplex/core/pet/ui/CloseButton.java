package mineplex.core.pet.ui;

import mineplex.core.shop.item.IButton;
import org.bukkit.entity.Player;


public class CloseButton
  implements IButton
{
  public void Clicked(Player player)
  {
    player.closeInventory();
  }
}
