package me.chiss.Core.Module;

import Clans;
import ClassFactory;
import DamageManager;
import Field;
import IRepository;
import SkillFactory;
import java.util.HashSet;
import me.chiss.Core.Config.Config;
import me.chiss.Core.Loot.LootFactory;
import me.chiss.Core.Modules.BlockRegenerate;
import me.chiss.Core.Modules.Blood;
import me.chiss.Core.Modules.Ignore;
import me.chiss.Core.Modules.Logger;
import me.chiss.Core.Modules.Observer;
import me.chiss.Core.Modules.Utility;
import me.chiss.Core.Modules.Wiki;
import me.chiss.Core.Plugin.IRelation;
import me.chiss.Core.Server.Server;
import mineplex.core.account.CoreClientManager;
import mineplex.core.blockrestore.BlockRestore;
import mineplex.core.creature.Creature;
import mineplex.core.energy.Energy;
import mineplex.core.explosion.Explosion;
import mineplex.core.projectile.ProjectileManager;
import mineplex.core.recharge.Recharge;
import mineplex.core.spawn.Spawn;
import mineplex.core.teleport.Teleport;
import mineplex.minecraft.game.core.condition.ConditionManager;
import mineplex.minecraft.game.core.fire.Fire;
import mineplex.minecraft.game.core.mechanics.Weapon;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


















public abstract class AModule
  implements Listener
{
  private IRepository _repository;
  protected String _moduleName;
  protected CoreClientManager _clients;
  protected JavaPlugin _plugin;
  protected Config _config;
  protected HashSet<String> _commands;
  
  public AModule(String paramString, JavaPlugin paramJavaPlugin) {}
  
  public AModule(String paramString, JavaPlugin paramJavaPlugin, IRepository paramIRepository) {}
  
  public IRepository GetRepository()
  {
    throw new Error("Unresolved compilation problems: \n\tIRepository cannot be resolved to a type\n\tIRepository cannot be resolved to a type\n");
  }
  
  public final AModule onEnable()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  






  public final void onDisable()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  




  public void enable() { throw new Error("Unresolved compilation problem: \n"); }
  public void disable() { throw new Error("Unresolved compilation problem: \n"); }
  public void config() { throw new Error("Unresolved compilation problem: \n"); }
  public void commands() { throw new Error("Unresolved compilation problem: \n"); }
  public void command(Player paramPlayer, String paramString, String[] paramArrayOfString) { throw new Error("Unresolved compilation problem: \n"); }
  
  public final String GetName() { throw new Error("Unresolved compilation problem: \n"); }
  

  public final void AddCommand(String paramString)
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public final boolean HasCommand(String paramString)
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public void Log(String paramString)
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  






  public JavaPlugin Plugin()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public ModuleManager Modules()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  





  public Config Config()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  





  public Utility Util()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  





  public BlockRegenerate BlockRegenerate()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  





  public BlockRestore BlockRestore()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  





  public Blood Blood()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  





  public Clans Clans()
  {
    throw new Error("Unresolved compilation problems: \n\tClans cannot be resolved to a type\n\tThe method GetClans() is undefined for the type IPlugin\n");
  }
  





  public ClassFactory Classes()
  {
    throw new Error("Unresolved compilation problems: \n\tClassFactory cannot be resolved to a type\n\tThe method GetClasses() is undefined for the type IPlugin\n");
  }
  












  public ConditionManager Condition()
  {
    throw new Error("Unresolved compilation problem: \n\tThe method GetCondition() is undefined for the type IPlugin\n");
  }
  
  public Creature Creature() { throw new Error("Unresolved compilation problem: \n"); }
  






  public DamageManager Damage()
  {
    throw new Error("Unresolved compilation problems: \n\tDamageManager cannot be resolved to a type\n\tThe method GetDamage() is undefined for the type IPlugin\n");
  }
  





  public Energy Energy()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  












  public Explosion Explosion()
  {
    throw new Error("Unresolved compilation problem: \n\tThe method GetExplosion() is undefined for the type IPlugin\n");
  }
  
  public Field Field() { throw new Error("Unresolved compilation problems: \n\tField cannot be resolved to a type\n\tThe method GetField() is undefined for the type IPlugin\n"); }
  






  public Fire Fire()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  












  public Ignore Ignore()
  {
    throw new Error("Unresolved compilation problem: \n\tThe method GetIgnore() is undefined for the type IPlugin\n");
  }
  
  public Logger Logger() { throw new Error("Unresolved compilation problem: \n"); }
  






  public LootFactory Loot()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  





  public Observer Observer()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  












  public IRelation Relation()
  {
    throw new Error("Unresolved compilation problem: \n\tThe method GetRelation() is undefined for the type IPlugin\n");
  }
  





  public Recharge Recharge()
  {
    throw new Error("Unresolved compilation problem: \n\tThe method GetRecharge() is undefined for the type IPlugin\n");
  }
  
  public Server ServerM() { throw new Error("Unresolved compilation problem: \n"); }
  






  public SkillFactory Skills()
  {
    throw new Error("Unresolved compilation problems: \n\tSkillFactory cannot be resolved to a type\n\tThe method GetSkills() is undefined for the type IPlugin\n");
  }
  





  public Spawn Spawn()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  





  public Teleport Teleport()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  





  public ProjectileManager Throw()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  












  public Weapon Weapon()
  {
    throw new Error("Unresolved compilation problem: \n\tThe method GetWeapon() is undefined for the type IPlugin\n");
  }
  
  public SkillFactory SkillFactory() { throw new Error("Unresolved compilation problems: \n\tSkillFactory cannot be resolved to a type\n\tThe method GetSkills() is undefined for the type IPlugin\n"); }
  













  public Wiki Wiki()
  {
    throw new Error("Unresolved compilation problem: \n\tThe method GetWiki() is undefined for the type IPlugin\n");
  }
}
