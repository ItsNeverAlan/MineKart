package nautilus.game.minekart.gp.command;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import nautilus.game.minekart.gp.GPManager;
import nautilus.game.minekart.kart.KartType;
import org.bukkit.entity.Player;

public class KartCommand
  extends CommandBase<GPManager>
{
  public KartCommand(GPManager plugin)
  {
    super(plugin, Rank.ADMIN, new String[] { "kart" });
  }
  

  public void Execute(Player caller, String[] args)
  {
    if (args.length < 1) {
      return;
    }
    for (KartType type : KartType.values())
    {
      if (args[0].equalsIgnoreCase(type.GetName()))
      {
        ((GPManager)this.Plugin).SelectKart(caller, type);
      }
    }
  }
}
