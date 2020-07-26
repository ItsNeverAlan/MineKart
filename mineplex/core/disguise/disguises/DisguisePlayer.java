package mineplex.core.disguise.disguises;

import java.lang.reflect.Field;
import net.minecraft.server.v1_6_R3.MathHelper;
import net.minecraft.server.v1_6_R3.Packet;
import net.minecraft.server.v1_6_R3.Packet20NamedEntitySpawn;


public class DisguisePlayer
  extends DisguiseHuman
{
  private static Field _spawnDataWatcherField;
  private String _name;
  
  public DisguisePlayer(org.bukkit.entity.Entity entity, String name)
  {
    super(entity);
    
    if (name.length() > 16)
    {
      name = name.substring(0, 16);
    }
    
    this._name = name;
    
    SetSpawnDataWatcherField();
  }
  

  public Packet GetSpawnPacket()
  {
    Packet20NamedEntitySpawn packet = new Packet20NamedEntitySpawn();
    packet.a = this.Entity.id;
    packet.b = this._name;
    packet.c = MathHelper.floor(this.Entity.locX * 32.0D);
    packet.d = MathHelper.floor(this.Entity.locY * 32.0D);
    packet.e = MathHelper.floor(this.Entity.locZ * 32.0D);
    packet.f = ((byte)(int)(this.Entity.yaw * 256.0F / 360.0F));
    packet.g = ((byte)(int)(this.Entity.pitch * 256.0F / 360.0F));
    
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
    
    return packet;
  }
  
  private void SetSpawnDataWatcherField()
  {
    if (_spawnDataWatcherField == null)
    {
      try
      {
        _spawnDataWatcherField = Packet20NamedEntitySpawn.class.getDeclaredField("i");
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
}
