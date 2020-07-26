package me.chiss.Core.Utility;

import java.net.InetSocketAddress;
import me.chiss.Core.Modules.Utility;
import org.bukkit.entity.Player;

public class UtilAccount extends AUtility
{
  public UtilAccount(Utility util)
  {
    super(util);
  }
  

  public String IPtoStr(Player player)
  {
    if (player == null) {
      return "Unknown";
    }
    if (player.getAddress() == null) {
      return "Unknown";
    }
    String IP = player.getAddress().toString();
    String trimIP = "";
    
    for (int i = 0; i < IP.length(); i++)
    {
      if (IP.charAt(i) == ':')
      {
        trimIP = IP.substring(1, i);
        break;
      }
    }
    
    return trimIP;
  }
}
