package mineplex.core.disguise.disguises;

import org.bukkit.entity.Skeleton.SkeletonType;

public class DisguiseSkeleton extends DisguiseMonster
{
  public DisguiseSkeleton(org.bukkit.entity.Entity entity)
  {
    super(entity);
    
    this.DataWatcher.a(13, Byte.valueOf((byte)0));
  }
  

  protected int GetEntityTypeId()
  {
    return 51;
  }
  
  public void SetSkeletonType(Skeleton.SkeletonType skeletonType)
  {
    this.DataWatcher.watch(13, Byte.valueOf((byte)skeletonType.getId()));
  }
  
  public int GetSkeletonType()
  {
    return this.DataWatcher.getByte(13);
  }
  
  protected String getHurtSound()
  {
    return "mob.skeleton.hurt";
  }
}
