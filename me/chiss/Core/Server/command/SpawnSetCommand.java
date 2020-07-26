package me.chiss.Core.Server.command;

import me.chiss.Core.Server.Server;
import mineplex.core.command.CommandBase;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SpawnSetCommand extends CommandBase<Server>
{
  public SpawnSetCommand(Server plugin)
  {
    super(plugin, mineplex.core.common.Rank.ADMIN, new String[] { "spawnset" });
  }
  

  public void Execute(Player caller, String[] args)
  {
    caller.getWorld().setSpawnLocation(caller.getLocation().getBlockX(), caller.getLocation().getBlockY(), caller.getLocation().getBlockZ());
  }
}
