package me.chiss.Core.PvpShop;

import java.util.Collection;

public abstract interface IPvpShopFactory
{
  public abstract Collection<IShopItem> GetItems();
}
