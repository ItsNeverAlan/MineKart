package nautilus.minecraft.core.utils;

public class GenericRunnable<T> implements Runnable
{
  protected T t;
  
  public GenericRunnable(T t)
  {
    this.t = t;
  }
  
  public void run() {}
}
