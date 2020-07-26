package me.chiss.Core.Modules;

import java.util.Iterator;
import java.util.LinkedList;
import me.chiss.Core.Module.AModule;
import me.chiss.Core.Utility.UtilItem;
import mineplex.core.common.Rank;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.itemstack.ItemStackFactory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Give extends AModule
{
  public Give(JavaPlugin plugin)
  {
    super("Give", plugin);
  }
  




  public void enable() {}
  




  public void disable() {}
  



  public void config() {}
  



  public void commands()
  {
    AddCommand("g");
  }
  




  public void command(Player paramPlayer, String paramString, String[] paramArrayOfString)
  {
    throw new Error("Unresolved compilation problem: \n\tThe method Clients() is undefined for the type Give\n");
  }
  











  public void help(Player caller)
  {
    UtilPlayer.message(caller, F.main("Give", "Commands List;"));
    UtilPlayer.message(caller, F.help("/g <item> (amount)", "Give Item to Self", Rank.ADMIN));
    UtilPlayer.message(caller, F.help("/g <player> <item> (amount)", "Give Item to Player", Rank.ADMIN));
  }
  

  public void give(Player caller, String target, String name, String amount)
  {
    LinkedList<Material> itemList = new LinkedList();
    itemList = Util().Items().matchItem(caller, name, true);
    if (itemList.isEmpty()) { return;
    }
    
    LinkedList<Player> giveList = new LinkedList();
    
    if (target.equalsIgnoreCase("all"))
    {
      for (Player cur : UtilServer.getPlayers()) {
        giveList.add(cur);
      }
    }
    else {
      giveList = UtilPlayer.matchOnline(caller, target, true);
      if (giveList.isEmpty()) { return;
      }
    }
    

    int count = 1;
    try
    {
      count = Integer.parseInt(amount);
      
      if (count < 1)
      {
        UtilPlayer.message(caller, F.main("Give", "Invalid Amount [" + amount + "]. Defaulting to [1]."));
        count = 1;
      }
    }
    catch (Exception e)
    {
      UtilPlayer.message(caller, F.main("Give", "Invalid Amount [" + amount + "]. Defaulting to [1]."));
    }
    

    String givenList = "";
    for (??? = giveList.iterator(); ((Iterator)???).hasNext();) { Player cur = (Player)((Iterator)???).next();
      givenList = givenList + cur.getName() + " "; }
    if (givenList.length() > 0) {
      givenList = givenList.substring(0, givenList.length() - 1);
    }
    for (??? = itemList.iterator(); ((Iterator)???).hasNext();) { Material curItem = (Material)((Iterator)???).next();
      
      for (Player cur : giveList)
      {
        ItemStack stack = ItemStackFactory.Instance.CreateStack(curItem, count);
        

        if (UtilInv.insert(cur, stack))
        {

          if (!cur.equals(caller)) {
            UtilPlayer.message(cur, F.main("Give", "You received " + F.item(new StringBuilder(String.valueOf(count)).append(" ").append(ItemStackFactory.Instance.GetName(curItem, (byte)0, false)).toString()) + " from " + F.elem(caller.getName()) + "."));
          }
        }
      }
      if (target.equalsIgnoreCase("all")) {
        UtilPlayer.message(caller, F.main("Give", new StringBuilder("You gave ").append(F.item(new StringBuilder(String.valueOf(count)).append(" ").append(ItemStackFactory.Instance.GetName(curItem, (byte)0, false)).toString())).append(" to ").append(F.elem("ALL")).toString()) + ".");
      }
      else if (giveList.size() > 1) {
        UtilPlayer.message(caller, F.main("Give", "You gave " + F.item(new StringBuilder(String.valueOf(count)).append(" ").append(ItemStackFactory.Instance.GetName(curItem, (byte)0, false)).toString()) + " to " + F.elem(givenList) + "."));
      }
      else {
        UtilPlayer.message(caller, F.main("Give", "You gave " + F.item(new StringBuilder(String.valueOf(count)).append(" ").append(ItemStackFactory.Instance.GetName(curItem, (byte)0, false)).toString()) + " to " + F.elem(((Player)giveList.getFirst()).getName()) + "."));
      }
      

      Log("Gave [" + count + " " + ItemStackFactory.Instance.GetName(curItem, (byte)0, false) + "] to [" + givenList + "] from [" + caller.getName() + "].");
    }
  }
}
