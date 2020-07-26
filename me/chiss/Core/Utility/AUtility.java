package me.chiss.Core.Utility;

import me.chiss.Core.Modules.Utility;

public class AUtility
{
  private Utility _util;
  
  public AUtility(Utility util) {
    this._util = util;
  }
  
  public Utility Util()
  {
    return this._util;
  }
}
