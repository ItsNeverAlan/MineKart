package mineplex.core.packethandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import net.minecraft.server.v1_6_R3.Packet;
import net.minecraft.server.v1_6_R3.Packet28EntityVelocity;
import net.minecraft.server.v1_6_R3.Packet31RelEntityMove;
import net.minecraft.server.v1_6_R3.Packet33RelEntityMoveLook;
import net.minecraft.server.v1_6_R3.Packet34EntityTeleport;
import net.minecraft.server.v1_6_R3.Packet40EntityMetadata;
import org.bukkit.entity.Player;


public class PacketArrayList
  extends ArrayList<Packet>
{
  private static final long serialVersionUID = 1L;
  private Player _owner;
  private PacketHandler _handler;
  private Field _packet40Metadata;
  
  public PacketArrayList(Player owner, PacketHandler handler)
  {
    this._owner = owner;
    this._handler = handler;
    
    try
    {
      this._packet40Metadata = Packet40EntityMetadata.class.getDeclaredField("b");
    }
    catch (NoSuchFieldException e)
    {
      e.printStackTrace();
    }
    catch (SecurityException e)
    {
      e.printStackTrace();
    }
    
    this._packet40Metadata.setAccessible(true);
  }
  






































  public boolean add(Packet o)
  {
    if ((o instanceof Packet34EntityTeleport))
    {
      Packet34EntityTeleport packet = (Packet34EntityTeleport)o;
      


      if ((this._handler.IsForwarding(this._owner)) && (this._handler.IsForwarded(this._owner, packet.a)))
      {
        return super.add(new Packet34EntityTeleport(this._handler.GetForwardId(this._owner, packet.a), packet.b, packet.c, packet.d, packet.e, packet.f));
      }
      if (this._handler.IsBlocked(this._owner, packet.a)) {
        return false;
      }
    } else if ((o instanceof Packet28EntityVelocity))
    {
      Packet28EntityVelocity packet = (Packet28EntityVelocity)o;
      


      if ((this._handler.IsForwarding(this._owner)) && (this._handler.IsForwarded(this._owner, packet.a)))
      {

        return false;
      }
      if (this._handler.IsBlocked(this._owner, packet.a)) {
        return false;
      }
    } else if ((o instanceof Packet31RelEntityMove))
    {
      Packet31RelEntityMove packet = (Packet31RelEntityMove)o;
      


      if ((this._handler.IsForwarding(this._owner)) && (this._handler.IsForwarded(this._owner, packet.a)))
      {
        return super.add(new Packet31RelEntityMove(this._handler.GetForwardId(this._owner, packet.a), packet.b, packet.c, packet.d));
      }
      if (this._handler.IsBlocked(this._owner, packet.a)) {
        return false;
      }
    } else if ((o instanceof Packet33RelEntityMoveLook))
    {
      Packet33RelEntityMoveLook packet = (Packet33RelEntityMoveLook)o;
      


      if ((this._handler.IsForwarding(this._owner)) && (this._handler.IsForwarded(this._owner, packet.a)))
      {
        return super.add(new Packet33RelEntityMoveLook(this._handler.GetForwardId(this._owner, packet.a), packet.b, packet.c, packet.d, packet.e, packet.f));
      }
      if (this._handler.IsBlocked(this._owner, packet.a)) {
        return false;
      }
    }
    





    if (this._handler.FireRunnables(o, this._owner, this))
    {
      return forceAdd(o);
    }
    
    return true;
  }
  
  public boolean forceAdd(Packet packet)
  {
    return super.add(packet);
  }
  
  public void Deactivate()
  {
    this._owner = null;
    this._packet40Metadata = null;
  }
}
