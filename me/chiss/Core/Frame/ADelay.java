package me.chiss.Core.Frame;

import org.bukkit.entity.Player;

public abstract class ADelay implements Runnable
{
  public Player player;
  public String data;
  
  public ADelay(Player player, String data)
  {
    this.player = player;
    this.data = data;
  }
  

  public void run()
  {
    delayed();
  }
  
  public abstract void delayed();
}
