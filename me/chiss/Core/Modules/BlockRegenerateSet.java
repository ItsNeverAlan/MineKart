package me.chiss.Core.Modules;

import java.util.ArrayList;
import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R3.CraftChunk;



public class BlockRegenerateSet
{
  private boolean _restore = false;
  private int _blocksPerTick = 10;
  private int _index = 0;
  
  private ArrayList<BlockRegenerateData> _blocks = new ArrayList();
  private HashSet<org.bukkit.Chunk> _chunks = new HashSet();
  
  public BlockRegenerateSet(int blocksPerTick)
  {
    this._blocksPerTick = blocksPerTick;
  }
  
  public void AddBlock(Location loc, int id, byte data)
  {
    if (!this._restore) {
      this._blocks.add(new BlockRegenerateData(loc, id, data));
    }
  }
  
  public void Start() {
    this._restore = true;
    this._index = (this._blocks.size() - 1);
  }
  
  public int GetRate()
  {
    return this._blocksPerTick;
  }
  
  public boolean IsRestoring()
  {
    return this._restore;
  }
  
  public boolean RestoreNext()
  {
    if (this._index < 0)
    {
      LightChunks();
      return false;
    }
    
    ((BlockRegenerateData)this._blocks.get(this._index)).Restore(this._chunks);
    this._index -= 1;
    
    return true;
  }
  
  private void LightChunks()
  {
    for (org.bukkit.Chunk chunk : this._chunks)
    {
      net.minecraft.server.v1_6_R3.Chunk c = ((CraftChunk)chunk).getHandle();
      c.initLighting();
    }
  }
  
  public ArrayList<BlockRegenerateData> GetBlocks()
  {
    return this._blocks;
  }
}
