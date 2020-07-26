package me.chiss.Core.Modules;

import java.util.LinkedList;
import me.chiss.Core.Module.AModule;
import mineplex.core.account.CoreClient;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;







public class Information
  extends AModule
{
  public Information(JavaPlugin plugin)
  {
    super("Client Information", plugin);
    
    AddCommand("x");
    AddCommand("info");
  }
  

  public void command(Player caller, String cmd, String[] args)
  {
    if ((args.length == 0) || (args[0].equals("help")))
    {
      help(caller);
      return;
    }
    
    Handle(caller, args);
  }
  







  private void help(Player caller) {}
  







  public void Handle(Player paramPlayer, String[] paramArrayOfString)
  {
    throw new Error("Unresolved compilation problems: \n\tThe method Clients() is undefined for the type Information\n\tThe method Clients() is undefined for the type Information\n");
  }
  

































  public void Account(Player paramPlayer, CoreClient paramCoreClient)
  {
    throw new Error("Unresolved compilation problems: \n\tThe method Clients() is undefined for the type Information\n\tThe method Acc() is undefined for the type CoreClient\n\tThe method Acc() is undefined for the type CoreClient\n\tThe method Acc() is undefined for the type CoreClient\n\tThe method Rank() is undefined for the type CoreClient\n\tThe method Acc() is undefined for the type CoreClient\n\tThe method Acc() is undefined for the type CoreClient\n\tThe method Rank() is undefined for the type CoreClient\n\tThe method Acc() is undefined for the type CoreClient\n\tThe method Acc() is undefined for the type CoreClient\n");
  }
  












































  public void NAC(Player paramPlayer, CoreClient paramCoreClient)
  {
    throw new Error("Unresolved compilation problems: \n\tThe method Clients() is undefined for the type Information\n\tThe method NAC() is undefined for the type CoreClient\n\tThe method NAC() is undefined for the type CoreClient\n\tThe method NAC() is undefined for the type CoreClient\n\tThe method NAC() is undefined for the type CoreClient\n\tThe method NAC() is undefined for the type CoreClient\n\tThe method Acc() is undefined for the type CoreClient\n\tThe method Acc() is undefined for the type CoreClient\n\tThe method NAC() is undefined for the type CoreClient\n\tThe method Acc() is undefined for the type CoreClient\n\tThe method NAC() is undefined for the type CoreClient\n\tThe method NAC() is undefined for the type CoreClient\n\tThe method Rank() is undefined for the type CoreClient\n\tThe method NAC() is undefined for the type CoreClient\n\tThe method Rank() is undefined for the type CoreClient\n\tThe method NAC() is undefined for the type CoreClient\n\tThe method NAC() is undefined for the type CoreClient\n");
  }
  

























































  public void Ban(Player paramPlayer, CoreClient paramCoreClient)
  {
    throw new Error("Unresolved compilation problems: \n\tThe method Ban() is undefined for the type CoreClient\n\tThe method Ban() is undefined for the type CoreClient\n\tThe method Ban() is undefined for the type CoreClient\n\tThe method Ban() is undefined for the type CoreClient\n\tThe method Ban() is undefined for the type CoreClient\n\tThe method Ban() is undefined for the type CoreClient\n\tThe method Ban() is undefined for the type CoreClient\n\tThe method Ban() is undefined for the type CoreClient\n\tThe method Ban() is undefined for the type CoreClient\n\tThe method Ban() is undefined for the type CoreClient\n");
  }
  


























  public void Mute(Player caller, CoreClient target)
  {
    LinkedList<String> out = new LinkedList();
    

    out.add(F.main(GetName(), "Mute Information - " + target.GetPlayerName()));
    

    UtilPlayer.message(caller, out);
  }
  
  public void Alias(Player caller, CoreClient target)
  {
    LinkedList<String> out = new LinkedList();
    

    out.add(F.main(GetName(), "Alias Information - " + target.GetPlayerName()));
    

    UtilPlayer.message(caller, out);
  }
  





  public void Clan(Player paramPlayer, CoreClient paramCoreClient)
  {
    throw new Error("Unresolved compilation problems: \n\tThe method Clan() is undefined for the type CoreClient\n\tThe method Clan() is undefined for the type CoreClient\n\tThe method Clan() is undefined for the type CoreClient\n\tThe method Clan() is undefined for the type CoreClient\n\tThe method Clan() is undefined for the type CoreClient\n");
  }
  























  public void Ignore(Player paramPlayer, CoreClient paramCoreClient)
  {
    throw new Error("Unresolved compilation problem: \n\tThe method Ignore() is undefined for the type CoreClient\n");
  }
}
