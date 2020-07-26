package nautilus.game.minekart.gp.command;

import mineplex.core.command.MultiCommandBase;
import mineplex.core.common.Rank;
import nautilus.game.minekart.gp.GPManager;
import nautilus.game.minekart.gp.command.gp.FinishCommand;
import nautilus.game.minekart.gp.command.gp.StartCommand;
import org.bukkit.entity.Player;

public class GpCommand
  extends MultiCommandBase<GPManager>
{
  public GpCommand(GPManager plugin)
  {
    super(plugin, Rank.MODERATOR, new String[] { "gp" });
    
    AddCommand(new StartCommand(plugin));
    AddCommand(new FinishCommand(plugin));
  }
  
  protected void Help(Player caller, String[] args) {}
}
