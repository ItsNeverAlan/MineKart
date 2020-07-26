package me.chiss.Core.Scheduler;

import java.util.Calendar;
import java.util.HashMap;
import mineplex.core.MiniPlugin;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;




public class Scheduler
  extends MiniPlugin
{
  public static Scheduler Instance;
  private HashMap<IScheduleListener, Long> _listenerMap;
  private long _startOfDay = 0L;
  
  public static void Initialize(JavaPlugin plugin)
  {
    if (Instance == null) {
      Instance = new Scheduler(plugin);
    }
  }
  
  private Scheduler(JavaPlugin plugin) {
    super("Scheduler", plugin);
    
    this._listenerMap = new HashMap();
    
    Calendar calender = Calendar.getInstance();
    
    this._startOfDay = (System.currentTimeMillis() - calender.get(11) * 3600000L - calender.get(12) * 60000L - calender.get(13) * 1000L - calender.get(14));
  }
  

  public void AddCommands()
  {
    AddCommand(new ForceDailyCommand(this));
  }
  
  @EventHandler
  public void UpdateDaily(UpdateEvent event)
  {
    if (event.getType() != UpdateType.SEC) {
      return;
    }
    for (IScheduleListener listener : this._listenerMap.keySet())
    {
      if ((((Long)this._listenerMap.get(listener)).longValue() < 86400000L) && (((Long)this._listenerMap.get(listener)).longValue() + this._startOfDay <= System.currentTimeMillis()))
      {
        listener.AppointmentFire();
        this._listenerMap.put(listener, Long.valueOf(((Long)this._listenerMap.get(listener)).longValue() + 86400000L));
      }
    }
    
    if (System.currentTimeMillis() - this._startOfDay >= 86400000L) {
      ResetStartOfDay();
    }
  }
  
  public void ScheduleDailyRecurring(IScheduleListener listener, long offsetFromDay) {
    long time = (86400000L + offsetFromDay) % 86400000L;
    
    this._listenerMap.put(listener, Long.valueOf(time));
    
    if (((Long)this._listenerMap.get(listener)).longValue() + this._startOfDay <= System.currentTimeMillis()) {
      this._listenerMap.put(listener, Long.valueOf(((Long)this._listenerMap.get(listener)).longValue() + 86400000L));
    }
  }
  
  public void ResetStartOfDay() {
    for (IScheduleListener listener : this._listenerMap.keySet())
    {
      if (((Long)this._listenerMap.get(listener)).longValue() >= 86400000L)
      {
        this._listenerMap.put(listener, Long.valueOf(((Long)this._listenerMap.get(listener)).longValue() - 86400000L));
      }
    }
    
    this._startOfDay = System.currentTimeMillis();
  }
  
  public long GetTimeTilNextAppt(IScheduleListener listener)
  {
    return ((Long)this._listenerMap.get(listener)).longValue() + this._startOfDay - System.currentTimeMillis();
  }
}
