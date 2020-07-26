package me.chiss.Core.PvpShop;

import ItemToken;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;



























public class ShopItem
  implements IShopItem, Listener
{
  protected PvpShopFactory Factory;
  private int _salesPackageId;
  private Material _type;
  private byte _data;
  private String _name;
  private String _deliveryName;
  private String[] _desc;
  private int _amount;
  private boolean _free;
  private int _tokenCost;
  private int _creditCost;
  private int _pointCost;
  private int _economyCost;
  private int _slot;
  private boolean _canDamage;
  private float _returnPercent;
  
  public ShopItem(PvpShopFactory paramPvpShopFactory, String paramString1, String paramString2, String[] paramArrayOfString, Material paramMaterial, byte paramByte, int paramInt1, int paramInt2, float paramFloat, int paramInt3) {}
  
  public Material GetType()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  

  public byte GetData()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  

  public int GetAmount()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  

  public int GetTokenCost()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  

  public int GetCreditCost()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  

  public int GetPointCost()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  

  public int GetEconomyCost()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  

  public int GetSalesPackageId()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  

  public String GetName()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  

  public String GetDeliveryName()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  





  @EventHandler
  public void Damage(CustomDamageEvent paramCustomDamageEvent)
  {
    throw new Error("Unresolved compilation problem: \n\tUtilGear cannot be resolved\n");
  }
  



  public boolean IsFree()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public void Update(ItemToken paramItemToken)
  {
    throw new Error("Unresolved compilation problem: \n\tItemToken cannot be resolved to a type\n");
  }
  




  public String[] GetDesc()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  

  public int GetSlot()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public float GetReturnPercent()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
}
