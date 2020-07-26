package me.chiss.Core.Weapon;

import nautilus.minecraft.core.webserver.token.Server.WeaponToken;
import org.bukkit.Material;

public abstract interface IWeapon
{
  public abstract Material GetType();
  
  public abstract int GetAmount();
  
  public abstract int GetGemCost();
  
  public abstract int GetEconomyCost();
  
  public abstract boolean IsFree();
  
  public abstract int GetSalesPackageId();
  
  public abstract String GetName();
  
  public abstract void Update(WeaponToken paramWeaponToken);
  
  public abstract String[] GetDesc();
}
