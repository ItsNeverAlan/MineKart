package nautilus.game.minekart.track;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import mineplex.core.common.util.UtilMath;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class TrackProcessor
{
  private Location progressStart = null;
  private ArrayList<Location> progress = new ArrayList();
  private ArrayList<Location> spawns = new ArrayList();
  private ArrayList<Location> returns = new ArrayList();
  private ArrayList<Location> items = new ArrayList();
  private HashMap<Location, String> mobs = new HashMap();
  private HashMap<Location, String> jumps = new HashMap();
  
  private ArrayList<Location> spawnsOrdered = new ArrayList();
  private ArrayList<Location> progressOrdered = new ArrayList();
  
  private int spawnsDisplayIndex = 0;
  private int progressDisplayIndex = 0;
  
  public void ProcessTrack(Player caller)
  {
    this.progressStart = null;
    this.progress.clear();
    this.spawns.clear();
    this.returns.clear();
    this.items.clear();
    
    this.mobs.clear();
    this.jumps.clear();
    
    this.progressOrdered.clear();
    this.spawnsOrdered.clear();
    
    int processed = 0;
    
    caller.sendMessage("Scanning for Blocks...");
    BlockState state; for (int x = -500; x < 500; x++)
      for (int z = -500; z < 500; z++)
        for (int y = 0; y < 256; y++)
        {
          processed++;
          if (processed % 10000000 == 0) {
            caller.sendMessage("Processed: " + processed);
          }
          Block block = caller.getWorld().getBlockAt(caller.getLocation().getBlockX() + x, caller.getLocation().getBlockY() + y, caller.getLocation().getBlockZ() + z);
          

          if (block.getType() == Material.SIGN_POST)
          {
            Block type = block.getRelative(BlockFace.DOWN);
            if (type == null) {
              continue;
            }
            state = block.getState();
            
            Sign sign = (Sign)state;
            
            String lineA = sign.getLine(0);
            String lineB = sign.getLine(1);
            
            if ((type.getType() == Material.WOOL) && (lineA.equals("MOB")))
            {
              if (type.getData() == 14)
              {
                this.mobs.put(type.getLocation().add(0.5D, 1.5D, 0.5D), lineB);
                

                block.setTypeId(0);
                type.setTypeId(0);
              }
              
            }
            else if ((type.getType() == Material.EMERALD_BLOCK) && (lineA.equals("JUMP")))
            {
              this.jumps.put(type.getLocation(), lineB);
              

              block.setTypeId(0);
            }
          }
          
          if (block.getTypeId() == 148)
          {

            Block type = block.getRelative(BlockFace.DOWN);
            if (type != null)
            {

              if (type.getType() == Material.WOOL)
              {

                if (type.getData() == 1)
                {
                  if (this.progressStart != null) {
                    caller.sendMessage("Error: Duplicate Progress Start");
                  }
                  this.progressStart = type.getLocation().add(0.5D, 0.0D, 0.5D);
                }
                else if (type.getData() == 4)
                {
                  this.progress.add(type.getLocation().add(0.5D, 0.0D, 0.5D));
                }
                else if (type.getData() == 5)
                {
                  this.items.add(type.getLocation().add(0.5D, 0.0D, 0.5D));
                }
                else if (type.getData() == 11)
                {
                  this.spawns.add(type.getLocation().add(0.5D, 0.0D, 0.5D));
                } else {
                  if (type.getData() != 10)
                    continue;
                  this.returns.add(type.getLocation().add(0.5D, 0.0D, 0.5D));
                }
                





                block.setTypeId(0);
                type.setTypeId(0);
              } }
          } }
    caller.sendMessage("Ordering Progress Blocks...");
    
    if (this.progressStart == null)
    {
      caller.sendMessage("Error: No Progress Start Found (Orange Wool)");
      return;
    }
    

    this.progressOrdered.add(this.progressStart);
    Location loc;
    while (!this.progress.isEmpty())
    {
      Location cur = (Location)this.progressOrdered.get(this.progressOrdered.size() - 1);
      Location next = null;
      double dist = 9999.0D;
      
      for (state = this.progress.iterator(); state.hasNext();) { loc = (Location)state.next();
        
        double newDist = UtilMath.offset(cur, loc);
        
        if (next == null)
        {
          next = loc;
          dist = newDist;

        }
        else if (newDist < dist)
        {
          next = loc;
          dist = newDist;
        }
      }
      
      this.progress.remove(next);
      this.progressOrdered.add(next);
    }
    

    while (!this.spawns.isEmpty())
    {
      Location spawn = null;
      double dist = 9999.0D;
      
      for (Location loc : this.spawns)
      {
        double newDist = UtilMath.offset(this.progressStart, loc);
        
        if (spawn == null)
        {
          spawn = loc;
          dist = newDist;

        }
        else if (newDist < dist)
        {
          spawn = loc;
          dist = newDist;
        }
      }
      
      this.spawns.remove(spawn);
      this.spawnsOrdered.add(spawn);
    }
    

    if (this.returns.isEmpty()) {
      this.returns = this.progressOrdered;
    }
    
    try
    {
      FileWriter fstream = new FileWriter(caller.getWorld().getName() + File.separatorChar + "TrackInfo.dat");
      BufferedWriter out = new BufferedWriter(fstream);
      
      out.write("TRACK_NAME:");
      out.write("\n");
      out.write("\n");
      out.write("MIN_X:");
      out.write("\n");
      out.write("MAX_X:");
      out.write("\n");
      out.write("MIN_Z:");
      out.write("\n");
      out.write("MAX_Z:");
      out.write("\n");
      out.write("\n");
      out.write("ROAD_TYPES:");
      out.write("\n");
      out.write("RETURN_TYPES:");
      out.write("\n");
      out.write("\n");
      out.write("SPAWN_DIRECTION:0");
      out.write("\n");
      out.write("SPAWNS:" + LocationsToString(this.spawnsOrdered));
      out.write("\n");
      out.write("\n");
      out.write("PROGRESS:" + LocationsToString(this.progressOrdered));
      out.write("\n");
      out.write("\n");
      out.write("RETURNS:" + LocationsToString(this.returns));
      out.write("\n");
      out.write("\n");
      out.write("ITEMS:" + LocationsToString(this.items));
      out.write("\n");
      out.write("\n");
      out.write("CREATURES:" + LocationSignsToString(this.mobs));
      out.write("\n");
      out.write("\n");
      out.write("JUMPS:" + LocationSignsToString(this.jumps));
      
      out.close();
    }
    catch (Exception e)
    {
      caller.sendMessage("Error: File Write Error");
    }
    

    caller.sendMessage("Track Data Saved.");
  }
  
  public String LocationsToString(ArrayList<Location> locs)
  {
    String out = "";
    
    for (Location loc : locs) {
      out = out + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + ":";
    }
    return out;
  }
  
  public String LocationSignsToString(HashMap<Location, String> locs)
  {
    String out = "";
    
    for (Location loc : locs.keySet()) {
      out = out + (String)locs.get(loc) + "@" + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + ":";
    }
    return out;
  }
  
  @EventHandler
  public void Update(UpdateEvent event)
  {
    if (event.getType() != UpdateType.FAST) {
      return;
    }
    if (!this.progressOrdered.isEmpty())
    {
      if (this.progressDisplayIndex >= this.progressOrdered.size()) {
        this.progressDisplayIndex = 0;
      }
      Location loc = (Location)this.progressOrdered.get(this.progressDisplayIndex);
      loc.getBlock().getWorld().playEffect(loc.getBlock().getLocation(), Effect.STEP_SOUND, 41);
      
      this.progressDisplayIndex += 1;
    }
    
    if (!this.spawnsOrdered.isEmpty())
    {
      if (this.spawnsDisplayIndex >= this.spawnsOrdered.size()) {
        this.spawnsDisplayIndex = 0;
      }
      Location loc = (Location)this.spawnsOrdered.get(this.spawnsDisplayIndex);
      loc.getBlock().getWorld().playEffect(loc.getBlock().getLocation(), Effect.STEP_SOUND, 22);
      
      this.spawnsDisplayIndex += 1;
    }
    
    for (Location loc : this.returns) {
      loc.getBlock().getWorld().playEffect(loc.getBlock().getLocation(), Effect.STEP_SOUND, 90);
    }
    for (Location loc : this.items) {
      loc.getBlock().getWorld().playEffect(loc.getBlock().getLocation(), Effect.STEP_SOUND, 18);
    }
  }
}
