package mineplex.minecraft.game.core.combat;

public class CombatDamage
{
  private String _name;
  private double _dmg;
  
  public CombatDamage(String name, double dmg)
  {
    this._name = name;
    this._dmg = dmg;
  }
  
  public String GetName()
  {
    return this._name;
  }
  
  public double GetDamage()
  {
    return this._dmg;
  }
}
