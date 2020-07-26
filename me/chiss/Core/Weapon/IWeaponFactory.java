package me.chiss.Core.Weapon;

import java.util.Collection;

public abstract interface IWeaponFactory
{
  public abstract Collection<IWeapon> GetWeapons();
}
