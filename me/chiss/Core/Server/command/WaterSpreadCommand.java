package me.chiss.Core.Server.command;

import me.chiss.Core.Server.Server;
import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import org.bukkit.entity.Player;

public class WaterSpreadCommand
  extends CommandBase<Server>
{
  public WaterSpreadCommand(Server plugin)
  {
    super(plugin, Rank.ADMIN, new String[] { "waterspread" });
  }
  

  public void Execute(Player caller, String[] args)
  {
    ((Server)this.Plugin).ToggleLiquidSpread();
    caller.sendMessage("Liquid Spread: " + ((Server)this.Plugin).GetLiquidSpread());
  }
}
