package mineplex.core.command;

import mineplex.core.account.CoreClient;
import mineplex.core.account.CoreClientManager;
import mineplex.core.common.Rank;
import mineplex.core.common.util.NautHashMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandCenter implements org.bukkit.event.Listener
{
  public static CommandCenter Instance;
  protected JavaPlugin Plugin;
  protected CoreClientManager ClientManager;
  protected NautHashMap<String, ICommand> Commands;
  
  public static void Initialize(JavaPlugin plugin, CoreClientManager clientManager)
  {
    if (Instance == null) {
      Instance = new CommandCenter(plugin, clientManager);
    }
  }
  
  public CoreClientManager GetClientManager() {
    return this.ClientManager;
  }
  
  private CommandCenter(JavaPlugin instance, CoreClientManager manager)
  {
    this.Plugin = instance;
    this.ClientManager = manager;
    this.Commands = new NautHashMap();
    this.Plugin.getServer().getPluginManager().registerEvents(this, this.Plugin);
  }
  
  @EventHandler
  public void OnPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
  {
    String commandName = event.getMessage().substring(1);
    String[] args = null;
    
    if (commandName.contains(" "))
    {
      commandName = commandName.split(" ")[0];
      args = event.getMessage().substring(event.getMessage().indexOf(' ') + 1).split(" ");
    }
    
    ICommand command = (ICommand)this.Commands.get(commandName);
    
    if ((command != null) && (this.ClientManager.Get(event.getPlayer()).GetRank().Has(event.getPlayer(), command.GetRequiredRank(), true)))
    {
      command.SetAliasUsed(commandName);
      command.Execute(event.getPlayer(), args);
      
      event.setCancelled(true);
    }
  }
  
  public void AddCommand(ICommand command)
  {
    for (String commandRoot : command.Aliases())
    {
      this.Commands.put(commandRoot, command);
      command.SetCommandCenter(this);
    }
  }
  
  public void RemoveCommand(ICommand command)
  {
    for (String commandRoot : command.Aliases())
    {
      this.Commands.remove(commandRoot);
      command.SetCommandCenter(null);
    }
  }
}
