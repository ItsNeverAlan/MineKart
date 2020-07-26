package nautilus.game.minekart.kart.condition;

public class ConditionData
{
  private ConditionType _type;
  private long _start;
  private long _duration;
  
  public ConditionData(ConditionType type, long duration)
  {
    this._type = type;
    this._start = System.currentTimeMillis();
    this._duration = duration;
  }
  
  public boolean IsExpired()
  {
    return System.currentTimeMillis() > this._start + this._duration;
  }
  
  public boolean IsCondition(ConditionType type)
  {
    return this._type == type;
  }
  
  public void Expire()
  {
    this._start = 0L;
  }
}
