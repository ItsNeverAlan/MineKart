package me.chiss.Core.Scheduler;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import org.bukkit.entity.Player;


public class ForceDailyCommand
  extends CommandBase<Scheduler>
{
  public ForceDailyCommand(Scheduler plugin)
  {
    super(plugin, Rank.ADMIN, new String[] { "forcedaily" });
  }
  

  public void Execute(Player caller, String[] args)
  {
    ((Scheduler)this.Plugin).ResetStartOfDay();
  }
}
