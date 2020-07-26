package me.chiss.Core.Modules;

import me.chiss.Core.Module.AModule;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilWorld;
import mineplex.core.teleport.Teleport;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Fix extends AModule
{
  public Fix(JavaPlugin plugin)
  {
    super("Glitch Fix", plugin);
  }
  




  public void enable() {}
  




  public void disable() {}
  



  public void config() {}
  



  public void commands()
  {
    AddCommand("speed1");
    AddCommand("speed2");
    AddCommand("speed3");
    AddCommand("speed4");
    AddCommand("speed5");
  }
  


  public void command(Player paramPlayer, String paramString, String[] paramArrayOfString)
  {
    throw new Error("Unresolved compilation problems: \n\tThe method Speed(String, LivingEntity, LivingEntity, double, int, boolean, boolean, boolean) in the type ConditionFactory is not applicable for the arguments (String, Player, Player, int, int, boolean, boolean)\n\tThe method Speed(String, LivingEntity, LivingEntity, double, int, boolean, boolean, boolean) in the type ConditionFactory is not applicable for the arguments (String, Player, Player, int, int, boolean, boolean)\n\tThe method Speed(String, LivingEntity, LivingEntity, double, int, boolean, boolean, boolean) in the type ConditionFactory is not applicable for the arguments (String, Player, Player, int, int, boolean, boolean)\n\tThe method Speed(String, LivingEntity, LivingEntity, double, int, boolean, boolean, boolean) in the type ConditionFactory is not applicable for the arguments (String, Player, Player, int, int, boolean, boolean)\n\tThe method Speed(String, LivingEntity, LivingEntity, double, int, boolean, boolean, boolean) in the type ConditionFactory is not applicable for the arguments (String, Player, Player, int, int, boolean, boolean)\n");
  }
  









  @EventHandler
  public void fixDoorGlitch(BlockPlaceEvent event)
  {
    if (event.getBlock().getTypeId() != 64) {
      return;
    }
    
    event.getBlockPlaced().setType(org.bukkit.Material.IRON_DOOR_BLOCK);
    

    UtilPlayer.message(event.getPlayer(), F.main(this._moduleName, "Please use Iron Doors."));
  }
  
  @EventHandler
  public void fixWallClimb(BlockBreakEvent event)
  {
    if (!event.isCancelled()) {
      return;
    }
    Block player = event.getPlayer().getLocation().getBlock();
    Block block = event.getBlock();
    
    if (player.getRelative(BlockFace.DOWN).getTypeId() != 0) {
      return;
    }
    if (block.getY() != player.getY() + 2) {
      return;
    }
    
    if ((block.getX() != player.getX()) && (block.getZ() != player.getZ())) {
      return;
    }
    
    if ((Math.abs(block.getX() - player.getX()) != 1) && (Math.abs(block.getZ() - player.getZ()) != 1)) {
      return;
    }
    
    Teleport().TP(event.getPlayer(), UtilWorld.locMerge(event.getPlayer().getLocation(), player.getLocation().add(0.5D, 0.0D, 0.5D)));
    

    UtilPlayer.message(event.getPlayer(), F.main(this._moduleName, "Wall Climb Prevented."));
  }
  
  @EventHandler
  public void fixBlockClimb(PlayerInteractEvent event)
  {
    if (!event.isCancelled()) {
      return;
    }
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }
    if (!UtilBlock.isBlock(event.getPlayer().getItemInHand())) {
      return;
    }
    Block player = event.getPlayer().getLocation().getBlock();
    Block block = event.getClickedBlock().getRelative(event.getBlockFace());
    

    if ((Math.abs(event.getPlayer().getLocation().getX() - (block.getX() + 0.5D)) > 0.8D) || 
      (Math.abs(event.getPlayer().getLocation().getZ() - (block.getZ() + 0.5D)) > 0.8D) || 
      (player.getY() < block.getY()) || (player.getY() > block.getY() + 1)) {
      return;
    }
    if (!UtilBlock.solid(block.getRelative(BlockFace.DOWN))) {
      return;
    }
    
    Teleport().TP(event.getPlayer(), UtilWorld.locMerge(event.getPlayer().getLocation(), block.getLocation().add(0.5D, 0.0D, 0.5D)));
    

    UtilPlayer.message(event.getPlayer(), F.main(this._moduleName, "Block Climb Prevented."));
  }
}
