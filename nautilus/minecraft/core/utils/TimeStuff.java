package nautilus.minecraft.core.utils;

public class TimeStuff
{
  public static String GetTimespanString(long millis)
  {
    int secs = (int)(millis / 1000L);
    
    if (secs < 60) {
      return secs + " seconds";
    }
    return secs / 60 + " minutes";
  }
}
