package me.chiss.Core.Plugin;

import org.bukkit.event.player.AsyncPlayerChatEvent;

public abstract interface IChat
{
  public abstract void HandleChat(AsyncPlayerChatEvent paramAsyncPlayerChatEvent, String paramString);
}
