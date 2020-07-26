package nautilus.game.minekart.gp.command;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.UtilEnum;
import nautilus.game.minekart.gp.GPManager;
import nautilus.game.minekart.item.KartItemType;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.KartManager;
import org.bukkit.entity.Player;

public class ItemCommand extends CommandBase<GPManager>
{
  public ItemCommand(GPManager plugin)
  {
    super(plugin, Rank.MODERATOR, new String[] { "item" });
  }
  

  public void Execute(Player caller, String[] args)
  {
    if (args.length < 1) {
      return;
    }
    Kart kart = ((GPManager)this.Plugin).KartManager.GetKart(caller);
    
    KartItemType kartItem = (KartItemType)UtilEnum.fromString(KartItemType.class, args[0]);
    
    if (kart != null) {
      kart.SetItemStored(kartItem);
    }
  }
}
