package me.chiss.Core.Modules;

import me.chiss.Core.Module.AModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Logger
  extends AModule
{
  public Logger(JavaPlugin plugin)
  {
    super("Logger", plugin);
  }
  
  public void enable() {}
  
  public void disable() {}
  
  public void config() {}
  
  public void commands() {}
  
  public void command(Player caller, String cmd, String[] args) {}
  
  @EventHandler
  public void handleCommand(PlayerCommandPreprocessEvent event) {}
  
  public void logChat(String type, Player from, String to, String message) {}
}
