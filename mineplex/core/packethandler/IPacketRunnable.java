package mineplex.core.packethandler;

import net.minecraft.server.v1_6_R3.Packet;
import org.bukkit.entity.Player;

public abstract interface IPacketRunnable
{
  public abstract boolean run(Packet paramPacket, Player paramPlayer, PacketArrayList paramPacketArrayList);
}
