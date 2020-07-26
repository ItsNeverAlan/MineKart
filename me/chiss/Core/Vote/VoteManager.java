package me.chiss.Core.Vote;

import mineplex.core.MiniPlugin;
import mineplex.core.server.event.PlayerVoteEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;









public class VoteManager
  extends MiniPlugin
{
  public VoteManager(JavaPlugin plugin)
  {
    super("Vote", plugin);
  }
  








  @EventHandler(priority=EventPriority.LOWEST)
  public void onPlayerVote(PlayerVoteEvent paramPlayerVoteEvent)
  {
    throw new Error("Unresolved compilation problem: \n\tThe method Donor() is undefined for the type CoreClient\n");
  }
}
