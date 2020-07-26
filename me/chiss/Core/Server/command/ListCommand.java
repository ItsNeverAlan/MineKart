package me.chiss.Core.Server.command;

import me.chiss.Core.Server.Server;
import mineplex.core.account.CoreClient;
import mineplex.core.account.CoreClientManager;
import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import org.bukkit.entity.Player;

public class ListCommand extends CommandBase<Server>
{
  public ListCommand(Server plugin)
  {
    super(plugin, Rank.ALL, new String[] { "list", "playerlist", "who" });
  }
  

  public void Execute(Player caller, String[] args)
  {
    UtilPlayer.message(caller, mineplex.core.common.util.F.main(((Server)this.Plugin).GetName(), "Listing Online Players:"));
    
    String staff = "";
    String other = "";
    for (Player cur : UtilServer.getPlayers())
    {
      if (((Server)this.Plugin).GetClientManager().Get(cur).GetRank().Has(cur, Rank.MODERATOR, false))
      {
        staff = staff + C.cWhite + cur.getName() + " ";
      }
      else
      {
        other = other + C.cWhite + cur.getName() + " ";
      }
    }
    

    if (staff.length() == 0) staff = "None"; else {
      staff = staff.substring(0, staff.length() - 1);
    }
    if (other.length() == 0) other = "None"; else {
      other = other.substring(0, other.length() - 1);
    }
    UtilPlayer.message(caller, "§c§lStaff");
    UtilPlayer.message(caller, staff);
    
    UtilPlayer.message(caller, "§a§lPlayers");
    UtilPlayer.message(caller, other);
  }
}
