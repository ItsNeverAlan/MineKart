package me.chiss.Core.Modules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import me.chiss.Core.Module.AModule;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockRegenerate extends AModule
{
  public HashSet<BlockRegenerateSet> _blockSets = new HashSet();
  
  public BlockRegenerate(JavaPlugin plugin)
  {
    super("Block Regenerate", plugin);
  }
  


  public void enable() {}
  

  public void disable()
  {
    int i;
    
    for (Iterator localIterator = this._blockSets.iterator(); localIterator.hasNext(); 
        i >= 0)
    {
      BlockRegenerateSet sets = (BlockRegenerateSet)localIterator.next();
      i = sets.GetBlocks().size() - 1; continue;
      ((BlockRegenerateData)sets.GetBlocks().get(i)).RestoreSlow();i--;
    }
  }
  



  public void config() {}
  



  public void commands() {}
  



  public void command(Player caller, String cmd, String[] args) {}
  



  public BlockRegenerateSet CreateSet(int blocksPerTick)
  {
    BlockRegenerateSet newSet = new BlockRegenerateSet(blocksPerTick);
    
    this._blockSets.add(newSet);
    
    return newSet;
  }
  
  @EventHandler
  public void Update(UpdateEvent event)
  {
    if (event.getType() != UpdateType.TICK) {
      return;
    }
    HashSet<BlockRegenerateSet> remove = new HashSet();
    
    for (BlockRegenerateSet set : this._blockSets) {
      if (set.IsRestoring()) {
        for (int i = 0; i < set.GetRate(); i++)
        {
          if (!set.RestoreNext())
          {
            remove.add(set);
            break;
          }
        }
      }
    }
  }
}
