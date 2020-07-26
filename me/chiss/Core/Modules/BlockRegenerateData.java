package me.chiss.Core.Modules;

import java.util.HashSet;
import net.minecraft.server.v1_6_R3.WorldServer;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_6_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;

public class BlockRegenerateData
{
  private Location _loc;
  private int _id;
  private byte _data;
  
  public BlockRegenerateData(Location loc, int id, byte data)
  {
    this._loc = loc;
    this._id = id;
    this._data = data;
  }
  
  public Location GetBlock()
  {
    return this._loc;
  }
  
  public int GetId()
  {
    return this._id;
  }
  
  public byte GetData()
  {
    return this._data;
  }
  
  public void Restore(HashSet<org.bukkit.Chunk> _chunks)
  {
    _chunks.add(this._loc.getChunk());
    QuickRestoreBlock();
  }
  



  public void QuickRestoreBlock()
  {
    net.minecraft.server.v1_6_R3.Chunk c = ((CraftChunk)this._loc.getChunk()).getHandle();
    
    c.a(this._loc.getBlockX() & 0xF, this._loc.getBlockY(), this._loc.getBlockZ() & 0xF, this._id, this._data);
    ((CraftWorld)this._loc.getChunk().getWorld()).getHandle().notify(this._loc.getBlockX(), this._loc.getBlockY(), this._loc.getBlockZ());
  }
  
  public void RestoreSlow()
  {
    this._loc.getBlock().setTypeIdAndData(this._id, this._data, true);
  }
}
