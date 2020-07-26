package nautilus.game.minekart.gp.command.gp;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import nautilus.game.minekart.gp.GP;
import nautilus.game.minekart.gp.GP.GPState;
import nautilus.game.minekart.gp.GPManager;
import nautilus.game.minekart.kart.Kart;
import org.bukkit.entity.Player;

public class FinishCommand
  extends CommandBase<GPManager>
{
  public FinishCommand(GPManager plugin)
  {
    super(plugin, Rank.MODERATOR, new String[] { "finish" });
  }
  

  public void Execute(Player caller, String[] args)
  {
    GP race = ((GPManager)this.Plugin).GetGP(caller);
    if (race == null)
    {
      caller.sendMessage("You are not in a race");
      return;
    }
    
    if (race.GetState() != GP.GPState.Live)
    {
      caller.sendMessage("Your race is not live...");
      return;
    }
    
    for (Kart kart : race.GetKarts()) {
      kart.SetLap(4);
    }
  }
}
