package me.chiss.Core.Weapon;

import mineplex.core.donation.repository.GameSalesPackageToken;
import nautilus.minecraft.core.webserver.token.Server.WeaponToken;
import org.bukkit.Material;

public class Weapon implements IWeapon
{
  private int _salesPackageId;
  private String _customName;
  private Material _type;
  private int _amount;
  private int _gemCost;
  private int _economyCost;
  private boolean _free;
  private String[] _desc;
  
  public Weapon(String customName, String[] desc, Material type, int amount, int gemCost)
  {
    this._customName = customName;
    this._desc = desc;
    this._type = type;
    this._amount = amount;
    this._gemCost = gemCost;
    this._economyCost = 0;
  }
  

  public Material GetType()
  {
    return this._type;
  }
  

  public int GetAmount()
  {
    return this._amount;
  }
  

  public int GetGemCost()
  {
    return this._gemCost;
  }
  

  public int GetEconomyCost()
  {
    return this._economyCost;
  }
  

  public int GetSalesPackageId()
  {
    return this._salesPackageId;
  }
  

  public String GetName()
  {
    return this._customName != null ? this._customName : this._type.name();
  }
  

  public boolean IsFree()
  {
    return this._free;
  }
  

  public void Update(WeaponToken weaponToken)
  {
    this._salesPackageId = weaponToken.SalesPackage.GameSalesPackageId.intValue();
    this._gemCost = weaponToken.SalesPackage.Gems.intValue();
    this._free = weaponToken.SalesPackage.Free;
  }
  

  public String[] GetDesc()
  {
    return this._desc;
  }
}
