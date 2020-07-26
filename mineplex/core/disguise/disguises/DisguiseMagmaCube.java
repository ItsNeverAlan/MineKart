package mineplex.core.disguise.disguises;

import java.lang.reflect.Field;
import net.minecraft.server.v1_6_R3.DataWatcher;
import net.minecraft.server.v1_6_R3.EnumEntitySize;
import net.minecraft.server.v1_6_R3.MathHelper;
import net.minecraft.server.v1_6_R3.Packet;
import net.minecraft.server.v1_6_R3.Packet24MobSpawn;

public class DisguiseMagmaCube extends DisguiseInsentient
{
  private static Field _spawnDataWatcherField;
  private static Field _spawnListField;
  
  public DisguiseMagmaCube(org.bukkit.entity.Entity entity)
  {
    super(entity);
    
    this.DataWatcher.a(16, new Byte((byte)1));
    
    SetSpawnDataWatcherField();
    SetSpawnListField();
  }
  
  public void SetSize(int i)
  {
    this.DataWatcher.watch(16, new Byte((byte)i));
  }
  
  public int GetSize()
  {
    return this.DataWatcher.getByte(16);
  }
  
  public Packet GetSpawnPacket()
  {
    Packet24MobSpawn packet = new Packet24MobSpawn();
    packet.a = this.Entity.id;
    packet.b = 62;
    packet.c = EnumEntitySize.SIZE_2.a(this.Entity.locX);
    packet.d = MathHelper.floor(this.Entity.locY * 32.0D);
    packet.e = EnumEntitySize.SIZE_2.a(this.Entity.locZ);
    packet.i = ((byte)(int)(this.Entity.yaw * 256.0F / 360.0F));
    packet.j = ((byte)(int)(this.Entity.pitch * 256.0F / 360.0F));
    packet.k = ((byte)(int)(this.Entity.yaw * 256.0F / 360.0F));
    
    double var2 = 3.9D;
    double var4 = 0.0D;
    double var6 = 0.0D;
    double var8 = 0.0D;
    
    if (var4 < -var2)
    {
      var4 = -var2;
    }
    
    if (var6 < -var2)
    {
      var6 = -var2;
    }
    
    if (var8 < -var2)
    {
      var8 = -var2;
    }
    
    if (var4 > var2)
    {
      var4 = var2;
    }
    
    if (var6 > var2)
    {
      var6 = var2;
    }
    
    if (var8 > var2)
    {
      var8 = var2;
    }
    
    packet.f = ((int)(var4 * 8000.0D));
    packet.g = ((int)(var6 * 8000.0D));
    packet.h = ((int)(var8 * 8000.0D));
    
    try
    {
      _spawnDataWatcherField.set(packet, this.DataWatcher);
    }
    catch (IllegalArgumentException e)
    {
      e.printStackTrace();
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
    }
    
    try
    {
      _spawnListField.set(packet, this.DataWatcher.b());
    }
    catch (IllegalArgumentException e)
    {
      e.printStackTrace();
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
    }
    
    return packet;
  }
  
  private void SetSpawnListField()
  {
    if (_spawnListField == null)
    {
      try
      {
        _spawnListField = Packet24MobSpawn.class.getDeclaredField("u");
        _spawnListField.setAccessible(true);
      }
      catch (NoSuchFieldException e)
      {
        e.printStackTrace();
      }
      catch (SecurityException e)
      {
        e.printStackTrace();
      }
    }
  }
  
  private void SetSpawnDataWatcherField()
  {
    if (_spawnDataWatcherField == null)
    {
      try
      {
        _spawnDataWatcherField = Packet24MobSpawn.class.getDeclaredField("t");
        _spawnDataWatcherField.setAccessible(true);
      }
      catch (NoSuchFieldException e)
      {
        e.printStackTrace();
      }
      catch (SecurityException e)
      {
        e.printStackTrace();
      }
    }
  }
  
  protected String getHurtSound()
  {
    return "mob.slime." + (GetSize() > 1 ? "big" : "small");
  }
  
  protected float getVolume()
  {
    return 0.4F * GetSize();
  }
}
