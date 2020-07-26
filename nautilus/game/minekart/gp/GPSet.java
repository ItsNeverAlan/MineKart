package nautilus.game.minekart.gp;

import org.bukkit.Material;

public enum GPSet
{
  MushroomCup("Mushroom Cup", new String[] { "MushroomA", "MushroomB", "MushroomC", "MushroomD" }, Material.RAW_CHICKEN, false), 
  FlowerCup("Flower Cup", new String[] { "MushroomA", "MushroomB", "MushroomC", "MushroomD" }, Material.COOKED_CHICKEN, false), 
  StarCup("Star Cup", new String[] { "MushroomA", "MushroomB", "MushroomC", "MushroomD" }, Material.CARROT_ITEM, false), 
  SpecialCup("Special Cup", new String[] { "MushroomA", "MushroomB", "MushroomC", "MushroomD" }, Material.GOLDEN_CARROT, false), 
  
  Battle("Battle", new String[] { "BattleA", "BattleB", "BattleC" }, Material.RAW_BEEF, true);
  

  private String _name;
  private String[] _mapNames;
  private Material _displayMat;
  private boolean _battle;
  
  private GPSet(String name, String[] mapNames, Material mat, boolean battle)
  {
    this._name = name;
    this._mapNames = mapNames;
    this._displayMat = mat;
    this._battle = battle;
  }
  
  public String GetName()
  {
    return this._name;
  }
  
  public String[] GetMapNames()
  {
    return this._mapNames;
  }
  
  public Material GetDisplayMaterial()
  {
    return this._displayMat;
  }
  
  public boolean IsBattle()
  {
    return this._battle;
  }
}
