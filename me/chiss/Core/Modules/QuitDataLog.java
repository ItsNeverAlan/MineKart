package me.chiss.Core.Modules;

import org.bukkit.Location;


public class QuitDataLog
{
  private int _logTime = 0;
  private long _logLast = 0L;
  private Location _logLoc = null;
  
  public QuitDataLog(int time, Location loc)
  {
    this._logTime = time;
    this._logLast = System.currentTimeMillis();
    this._logLoc = loc;
  }
  
  public int GetLogTime() {
    return this._logTime;
  }
  
  public void SetLogTime(int _logTime) {
    this._logTime = _logTime;
  }
  
  public long GetLogLast() {
    return this._logLast;
  }
  
  public void SetLogLast(long _logLast) {
    this._logLast = _logLast;
  }
  
  public Location GetLogLoc() {
    return this._logLoc;
  }
  
  public void SetLogLoc(Location _logLoc) {
    this._logLoc = _logLoc;
  }
}
