package me.chiss.Core.Modules;

import org.bukkit.entity.Player;


public class QuitDataQuit
{
  private boolean _offline = false;
  private long _quitTime = 0L;
  private int _quitCount = 0;
  
  private Player _player = null;
  
  public QuitDataQuit(Player player)
  {
    SetOffline(true);
    SetPlayer(player);
    this._quitTime = System.currentTimeMillis();
  }
  
  public long GetQuitTime() {
    return this._quitTime;
  }
  
  public void SetQuitTime(long _quitTime) {
    this._quitTime = _quitTime;
  }
  
  public boolean IsOffline() {
    return this._offline;
  }
  
  public boolean SetOffline(boolean offline)
  {
    this._offline = offline;
    
    this._quitTime = System.currentTimeMillis();
    
    if (offline)
    {
      this._quitCount += 1;
      
      if (this._quitCount >= 3) {
        return true;
      }
    }
    return false;
  }
  
  public int GetCount()
  {
    return this._quitCount;
  }
  
  public Player GetPlayer() {
    return this._player;
  }
  
  public void SetPlayer(Player _quitRef) {
    this._player = _quitRef;
  }
}
