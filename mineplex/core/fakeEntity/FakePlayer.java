package mineplex.core.fakeEntity;

import java.lang.reflect.Field;
import net.minecraft.server.v1_6_R3.DataWatcher;
import net.minecraft.server.v1_6_R3.MathHelper;
import net.minecraft.server.v1_6_R3.Packet;
import net.minecraft.server.v1_6_R3.Packet20NamedEntitySpawn;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;



public class FakePlayer
  extends FakeEntity
{
  private String _name;
  private static Field _spawnDataWatcherField;
  
  public FakePlayer(String name, Location location)
  {
    super(EntityType.PLAYER, location);
    
    this._name = name;
    
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
  
  public Packet Spawn(int id)
  {
    Packet20NamedEntitySpawn packet = new Packet20NamedEntitySpawn();
    packet.a = id;
    packet.b = this._name;
    packet.c = MathHelper.floor(GetLocation().getX() * 32.0D);
    packet.d = MathHelper.floor(GetLocation().getY() * 32.0D);
    packet.e = MathHelper.floor(GetLocation().getZ() * 32.0D);
    packet.f = ((byte)(int)(GetLocation().getYaw() * 256.0F / 360.0F));
    packet.g = ((byte)(int)(GetLocation().getPitch() * 256.0F / 360.0F));
    
    DataWatcher dataWatcher = new DataWatcher();
    
    UpdateDataWatcher(dataWatcher);
    
    try
    {
      _spawnDataWatcherField.set(packet, dataWatcher);
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
}
