package me.chiss.Core.PvpShop;

import org.bukkit.Material;

public abstract interface IShopItem
{
  public abstract Material GetType();
  
  public abstract byte GetData();
  
  public abstract int GetAmount();
  
  public abstract int GetTokenCost();
  
  public abstract int GetPointCost();
  
  public abstract int GetCreditCost();
  
  public abstract int GetEconomyCost();
  
  public abstract boolean IsFree();
  
  public abstract int GetSalesPackageId();
  
  public abstract String GetName();
  
  public abstract String[] GetDesc();
  
  public abstract int GetSlot();
  
  public abstract String GetDeliveryName();
  
  public abstract float GetReturnPercent();
}
