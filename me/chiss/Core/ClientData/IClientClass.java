package me.chiss.Core.ClientData;

import org.bukkit.inventory.ItemStack;

public abstract interface IClientClass {
  static {
    throw new Error("Unresolved compilation problems: \n\tThe import me.chiss.Core.Class cannot be resolved\n\tThe import me.chiss.Core.Skill cannot be resolved\n\tThe import me.chiss.Core.Skill cannot be resolved\n\tIPvpClass cannot be resolved to a type\n\tIPvpClass cannot be resolved to a type\n\tIPvpClass cannot be resolved to a type\n\tISkill cannot be resolved to a type\n\tISkill cannot be resolved to a type\n\tISkill cannot be resolved to a type\n\tISkill cannot be resolved to a type\n\tISkill cannot be resolved to a type\n\tSkillType cannot be resolved to a type\n\tISkill cannot be resolved to a type\n");
  }
  
  public abstract String GetName();
  
  public abstract void SetGameClass(IPvpClass paramIPvpClass);
  
  public abstract void SetGameClass(IPvpClass paramIPvpClass, boolean paramBoolean);
  
  public abstract IPvpClass GetGameClass();
  
  public abstract void AddSkill(ISkill paramISkill, int paramInt);
  
  public abstract void RemoveSkill(ISkill paramISkill);
  
  public abstract java.util.Collection GetSkills();
  
  public abstract java.util.Collection GetDefaultSkills();
  
  public abstract ISkill GetSkillByType(SkillType paramSkillType);
  
  public abstract void ResetSkills();
  
  public abstract void ClearSkills();
  
  public abstract void ClearDefaultSkills();
  
  public abstract int GetSkillLevel(ISkill paramISkill);
  
  public abstract org.bukkit.inventory.PlayerInventory GetInventory();
  
  public abstract void OpenInventory(org.bukkit.inventory.Inventory paramInventory);
  
  public abstract void CloseInventory();
  
  public abstract void UpdateInventory();
  
  public abstract void ClearDefaults();
  
  public abstract void AddDefaultArmor(ItemStack[] paramArrayOfItemStack);
  
  public abstract void PutDefaultItem(ItemStack paramItemStack, int paramInt);
  
  public abstract java.util.HashMap GetDefaultItems();
  
  public abstract ItemStack[] GetDefaultArmor();
  
  public abstract void SetDefaultHead(ItemStack paramItemStack);
  
  public abstract void SetDefaultChest(ItemStack paramItemStack);
  
  public abstract void SetDefaultLegs(ItemStack paramItemStack);
  
  public abstract void SetDefaultFeet(ItemStack paramItemStack);
  
  public abstract void ResetToDefaults(boolean paramBoolean1, boolean paramBoolean2);
}
