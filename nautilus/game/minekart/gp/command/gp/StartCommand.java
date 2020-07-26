package nautilus.game.minekart.gp.command.gp;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import nautilus.game.minekart.gp.GPManager;
import org.bukkit.entity.Player;

public class StartCommand
  extends CommandBase<GPManager>
{
  public StartCommand(GPManager plugin)
  {
    super(plugin, Rank.MODERATOR, new String[] { "start" });
  }
  

  public void Execute(Player caller, String[] args)
  {
    ((GPManager)this.Plugin).StartGP(true);
  }
}
