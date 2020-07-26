package me.chiss.Core.Module;

import java.util.HashSet;

public class ModuleManager
{
  private HashSet<AModule> _modules;
  
  public ModuleManager()
  {
    this._modules = new HashSet();
  }
  
  public void Register(AModule module)
  {
    this._modules.add(module);
  }
  
  public HashSet<AModule> GetAll()
  {
    return this._modules;
  }
  
  public void onDisable()
  {
    for (AModule module : this._modules) {
      module.onDisable();
    }
  }
}
