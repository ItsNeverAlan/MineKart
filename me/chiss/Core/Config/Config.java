package me.chiss.Core.Config;

import java.util.HashMap;
import me.chiss.Core.Module.AModule;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class Config
  extends AModule
{
  public Config(JavaPlugin plugin)
  {
    super("Config", plugin);
  }
  

  private HashMap<String, HashMap<String, String>> _moduleConfig = new HashMap();
  

  public void enable()
  {
    this._moduleConfig.clear();
  }
  



























  public void disable()
  {
    long epoch = System.currentTimeMillis();
    Log("Saving Config...");
    


    Log("Config Saved. Took " + (System.currentTimeMillis() - epoch) + " milliseconds.");
  }
  






  public void config() {}
  






  public void commands() {}
  






  public void command(Player caller, String cmd, String[] args) {}
  






  public void writeVars() {}
  






  public void writeVar(String module, String variable, String value) {}
  





  public void addVar(String module, String variable, String value)
  {
    Log("Adding Variable [" + module + ": " + variable + " = " + value + "]");
    

    if (!this._moduleConfig.containsKey(module)) {
      this._moduleConfig.put(module, new HashMap());
    }
    
    ((HashMap)this._moduleConfig.get(module)).put(variable, value);
    
    writeVar(module, variable, value);
  }
  
  public String getString(String module, String variable, String def)
  {
    if (!this._moduleConfig.containsKey(module))
    {
      Log("Variable Not Found [" + module + ": " + variable + "]");
      addVar(module, variable, def);
      return def;
    }
    
    HashMap<String, String> varMap = (HashMap)this._moduleConfig.get(module);
    
    if (!varMap.containsKey(variable))
    {
      Log("Variable Not Found [" + module + ": " + variable + "]");
      addVar(module, variable, def);
      return def;
    }
    
    return (String)varMap.get(variable);
  }
  
  public int getInt(String module, String variable, int def)
  {
    if (!this._moduleConfig.containsKey(module))
    {
      Log("Variable Not Found [" + module + ": " + variable + "]");
      addVar(module, variable, def);
      return def;
    }
    
    HashMap<String, String> varMap = (HashMap)this._moduleConfig.get(module);
    
    if (!varMap.containsKey(variable))
    {
      Log("Variable Not Found [" + module + ": " + variable + "]");
      addVar(module, variable, def);
      return def;
    }
    
    try
    {
      return Integer.parseInt((String)varMap.get(variable));
    }
    catch (Exception e) {}
    
    return 0;
  }
  

  public long getLong(String module, String variable, long def)
  {
    if (!this._moduleConfig.containsKey(module))
    {
      Log("Variable Not Found [" + module + ": " + variable + "]");
      addVar(module, variable, def);
      return def;
    }
    
    HashMap<String, String> varMap = (HashMap)this._moduleConfig.get(module);
    
    if (!varMap.containsKey(variable))
    {
      Log("Variable Not Found [" + module + ": " + variable + "]");
      addVar(module, variable, def);
      return def;
    }
    
    try
    {
      return Long.parseLong((String)varMap.get(variable));
    }
    catch (Exception e) {}
    
    return 0L;
  }
  

  public boolean getBool(String module, String variable, boolean def)
  {
    if (!this._moduleConfig.containsKey(module))
    {
      Log("Variable Not Found [" + module + ": " + variable + "]");
      addVar(module, variable, def);
      return def;
    }
    
    HashMap<String, String> varMap = (HashMap)this._moduleConfig.get(module);
    
    if (!varMap.containsKey(variable))
    {
      Log("Variable Not Found [" + module + ": " + variable + "]");
      addVar(module, variable, def);
      return def;
    }
    
    try
    {
      return Boolean.parseBoolean((String)varMap.get(variable));
    }
    catch (Exception e)
    {
      addVar(module, variable, def); }
    return def;
  }
}
