package me.chiss.Core.Modules;

import me.chiss.Core.Utility.UtilInput;

public class Utility extends me.chiss.Core.Module.AModule
{
  private me.chiss.Core.Utility.UtilAccount _utilAccount;
  private mineplex.core.common.util.UtilEvent _utilEvent;
  private mineplex.core.common.util.UtilGear _utilGear;
  private UtilInput _utilInput;
  private me.chiss.Core.Utility.UtilItem _utilItem;
  
  public Utility(org.bukkit.plugin.java.JavaPlugin plugin) {
    super("Utility", plugin);
  }
  





  public void enable() {}
  





  public void disable() {}
  




  public void config() {}
  




  public void commands() {}
  




  public void command(org.bukkit.entity.Player caller, String cmd, String[] args) {}
  




  public me.chiss.Core.Utility.UtilAccount Acc()
  {
    if (this._utilAccount == null) {
      this._utilAccount = new me.chiss.Core.Utility.UtilAccount(this);
    }
    return this._utilAccount;
  }
  

  public mineplex.core.common.util.UtilEvent Event()
  {
    throw new Error("Unresolved compilation problem: \n\tThe constructor UtilEvent(Utility) is undefined\n");
  }
  



  public mineplex.core.common.util.UtilGear Gear()
  {
    throw new Error("Unresolved compilation problem: \n\tThe constructor UtilGear(Utility) is undefined\n");
  }
  


  public UtilInput Input()
  {
    if (this._utilInput == null) {
      this._utilInput = new UtilInput(this);
    }
    return this._utilInput;
  }
  
  public me.chiss.Core.Utility.UtilItem Items()
  {
    if (this._utilItem == null) {
      this._utilItem = new me.chiss.Core.Utility.UtilItem(this);
    }
    return this._utilItem;
  }
}
