package me.chiss.Core.Utility;

import java.util.LinkedList;
import me.chiss.Core.Modules.Utility;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class UtilItem
  extends AUtility
{
  public UtilItem(Utility util)
  {
    super(util);
  }
  
  public LinkedList<Material> matchItem(Player caller, String items, boolean inform)
  {
    LinkedList<Material> matchList = new LinkedList();
    
    String failList = "";
    

    for (String cur : items.split(","))
    {
      Material match = searchItem(caller, cur, inform);
      
      if (match != null) {
        matchList.add(match);
      }
      else {
        failList = failList + cur + " ";
      }
    }
    if ((inform) && (failList.length() > 0))
    {
      failList = failList.substring(0, failList.length() - 1);
      UtilPlayer.message(caller, F.main("Item(s) Search", 
        C.mBody + " Invalid [" + 
        C.mElem + failList + 
        C.mBody + "]."));
    }
    
    return matchList;
  }
  
  public Material searchItem(Player caller, String args, boolean inform)
  {
    LinkedList<Material> matchList = new LinkedList();
    
    for (Material cur : Material.values())
    {

      if (cur.toString().equalsIgnoreCase(args)) {
        return cur;
      }
      if (cur.toString().toLowerCase().contains(args.toLowerCase())) {
        matchList.add(cur);
      }
      
      String[] arg = args.split(":");
      
      int id = 0;
      
      try
      {
        if (arg.length > 0) { id = Integer.parseInt(arg[0]);
        }
      }
      catch (Exception e)
      {
        continue;
      }
      if (id == cur.getId()) {
        return cur;
      }
    }
    
    if (matchList.size() != 1)
    {
      if (!inform) {
        return null;
      }
      
      UtilPlayer.message(caller, F.main("Item Search", 
        C.mCount + matchList.size() + 
        C.mBody + " matches for [" + 
        C.mElem + args + 
        C.mBody + "]."));
      
      if (matchList.size() > 0)
      {
        String matchString = "";
        for (Material cur : matchList)
          matchString = matchString + F.elem(cur.toString()) + ", ";
        if (matchString.length() > 1) {
          matchString = matchString.substring(0, matchString.length() - 2);
        }
        UtilPlayer.message(caller, F.main("Item Search", 
          C.mBody + "Matches [" + 
          C.mElem + matchString + 
          C.mBody + "]."));
      }
      
      return null;
    }
    
    return (Material)matchList.get(0);
  }
  
  public String itemToStr(ItemStack item)
  {
    String data = "0";
    if (item.getData() != null) {
      data = item.getData().getData();
    }
    return item.getType() + ":" + item.getAmount() + ":" + item.getDurability() + ":" + data;
  }
}
