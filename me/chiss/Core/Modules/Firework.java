package me.chiss.Core.Modules;

import java.util.ArrayList;
import java.util.HashSet;
import me.chiss.Core.Module.AModule;
import mineplex.core.common.util.F;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;





public class Firework
  extends AModule
{
  private HashSet<Color> _colors;
  
  public Firework(JavaPlugin plugin)
  {
    super("Firework", plugin);
  }
  


  public void enable()
  {
    this._colors = new HashSet();
    
    this._colors.add(Color.AQUA);
    this._colors.add(Color.BLACK);
    this._colors.add(Color.BLUE);
    this._colors.add(Color.FUCHSIA);
    this._colors.add(Color.GRAY);
    this._colors.add(Color.GREEN);
    this._colors.add(Color.LIME);
    this._colors.add(Color.MAROON);
    this._colors.add(Color.NAVY);
    this._colors.add(Color.OLIVE);
    this._colors.add(Color.ORANGE);
    this._colors.add(Color.PURPLE);
    this._colors.add(Color.RED);
    this._colors.add(Color.SILVER);
    this._colors.add(Color.TEAL);
    this._colors.add(Color.WHITE);
    this._colors.add(Color.YELLOW);
  }
  



  public void disable() {}
  



  public void config() {}
  



  public void commands()
  {
    AddCommand("fw");
  }
  

  public void command(Player paramPlayer, String paramString, String[] paramArrayOfString)
  {
    throw new Error("Unresolved compilation problem: \n\tThe method Clients() is undefined for the type Firework\n");
  }
  














































  public FireworkEffect BuildFirework(Player caller, String typeString, String colorString, String specialString)
  {
    FireworkEffect.Type type = FireworkEffect.Type.BALL;
    for (FireworkEffect.Type cur : FireworkEffect.Type.values()) {
      if (typeString.equalsIgnoreCase(cur.toString()))
      {
        caller.sendMessage(F.value("Type", type.toString()));
        type = cur;
        break;
      }
    }
    
    ArrayList<Color> colors = new ArrayList();
    colorString = colorString.toLowerCase();
    for (String colorToken : colorString.split(","))
    {
      ArrayList<Color> matchList = new ArrayList();
      Color match = null;
      
      for (Color cur : this._colors)
      {
        if (cur.toString().toLowerCase().equals(colorToken))
        {
          match = cur;
          break;
        }
        
        if (cur.toString().toLowerCase().contains(colorToken))
        {
          matchList.add(cur);
        }
      }
      
      if (match != null)
      {
        caller.sendMessage(F.value("Added Color", match.toString()));
        colors.add(match);
      }
      else if (!matchList.isEmpty())
      {
        if (matchList.size() == 1)
        {
          caller.sendMessage(F.value("Added Color", ((Color)matchList.get(0)).toString()));
          colors.add((Color)matchList.get(0));
        }
        else
        {
          caller.sendMessage(F.value("Multiple Color Matches", colorToken));
        }
      }
      else
      {
        caller.sendMessage(F.value("No Color Matches", colorToken));
      }
    }
    if (colors.isEmpty()) {
      colors.add(Color.WHITE);
    }
    boolean flicker = false;
    boolean trail = false;
    
    if ((specialString.toLowerCase().contains("flicker")) || 
      (specialString.toLowerCase().contains("flick")) || 
      (specialString.toLowerCase().contains("f"))) {
      flicker = true;
    }
    if ((specialString.toLowerCase().contains("trail")) || 
      (specialString.toLowerCase().contains("t"))) {
      flicker = true;
    }
    return 
      FireworkEffect.builder().flicker(flicker).trail(trail).withColor(colors).withFade(colors).with(type).build();
  }
}
