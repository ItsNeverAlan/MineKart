package nautilus.game.minekart.gp.command;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import nautilus.game.minekart.gp.GPManager;
import org.bukkit.entity.Player;

public class VoteCommand
  extends CommandBase<GPManager>
{
  public VoteCommand(GPManager plugin)
  {
    super(plugin, Rank.ALL, new String[] { "vote" });
  }
  

  public void Execute(Player caller, String[] args)
  {
    ((GPManager)this.Plugin).Vote(caller);
  }
}
