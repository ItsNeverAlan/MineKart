package mineplex.core.chat;

import mineplex.core.MiniClientPlugin;
import mineplex.core.account.CoreClient;
import mineplex.core.account.CoreClientManager;
import mineplex.core.chat.command.BroadcastCommand;
import mineplex.core.chat.command.SilenceCommand;
import mineplex.core.common.Rank;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;




public class Chat
  extends MiniClientPlugin<ChatClient>
{
  private CoreClientManager _clientManager;
  private long _silenced = 0L;
  
  public Chat(JavaPlugin plugin, CoreClientManager clientManager)
  {
    super("Chat", plugin);
    
    this._clientManager = clientManager;
  }
  


  public void AddCommands()
  {
    AddCommand(new SilenceCommand(this));
    AddCommand(new BroadcastCommand(this));
  }
  








  public void Silence(long duration, boolean inform)
  {
    if (duration > 0L) {
      this._silenced = (System.currentTimeMillis() + duration);
    } else {
      this._silenced = duration;
    }
    if (!inform) {
      return;
    }
    
    if (duration == -1L) {
      UtilServer.broadcast(F.main("Chat", "Chat has been silenced for " + F.time("Permanent") + "."));
    } else if (duration == 0L) {
      UtilServer.broadcast(F.main("Chat", "Chat is no longer silenced."));
    } else {
      UtilServer.broadcast(F.main("Chat", "Chat has been silenced for " + F.time(UtilTime.MakeStr(duration, 1)) + "."));
    }
  }
  
  @EventHandler
  public void preventMe(PlayerCommandPreprocessEvent event) {
    if (event.getMessage().toLowerCase().startsWith("/me"))
    {
      event.getPlayer().sendMessage(F.main(GetName(), "Quite full of yourself aren't you?  Nobody cares."));
      event.setCancelled(true);
    }
    else if (event.getMessage().toLowerCase().startsWith("/tell"))
    {
      event.getPlayer().sendMessage(F.main(GetName(), "Step back bro, thats not a command on this server!"));
      event.setCancelled(true);
    }
  }
  
  @EventHandler
  public void lagTest(PlayerCommandPreprocessEvent event)
  {
    if ((event.getMessage().equals("lag")) || (event.getMessage().equals("ping")))
    {
      event.getPlayer().sendMessage(F.main(GetName(), "PONG!"));
      event.setCancelled(true);
    }
  }
  
  @EventHandler
  public void SilenceUpdate(UpdateEvent event)
  {
    if (event.getType() != UpdateType.FAST) {
      return;
    }
    SilenceEnd();
  }
  
  public void SilenceEnd()
  {
    if (this._silenced <= 0L) {
      return;
    }
    if (System.currentTimeMillis() > this._silenced) {
      Silence(0L, true);
    }
  }
  
  public boolean SilenceCheck(Player player) {
    SilenceEnd();
    
    if (this._silenced == 0L) {
      return false;
    }
    if (this._clientManager.Get(player).GetRank().Has(player, Rank.MODERATOR, false)) {
      return false;
    }
    if (this._silenced == -1L) {
      UtilPlayer.message(player, F.main(GetName(), "Chat is silenced permanently."));
    } else {
      UtilPlayer.message(player, F.main(GetName(), "Chat is silenced for " + F.time(UtilTime.MakeStr(this._silenced - System.currentTimeMillis(), 1)) + "."));
    }
    return true;
  }
  
  @EventHandler(priority=EventPriority.LOWEST)
  public void HandleChat(AsyncPlayerChatEvent event)
  {
    if (event.isCancelled()) {
      return;
    }
    Player sender = event.getPlayer();
    
    if (SilenceCheck(sender))
    {
      event.setCancelled(true);
      return;
    }
    if (!Recharge.Instance.use(sender, "Chat Message", 500L, false))
    {
      UtilPlayer.message(sender, F.main("Chat", "You are sending messages too fast."));
      event.setCancelled(true);
    }
  }
  
  public long Silenced()
  {
    return this._silenced;
  }
  

  protected ChatClient AddPlayer(String player)
  {
    return new ChatClient();
  }
}
