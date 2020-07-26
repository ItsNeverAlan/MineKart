package me.chiss.Core.Plugin;

import me.chiss.Core.Config.Config;
import me.chiss.Core.Loot.LootFactory;
import me.chiss.Core.Modules.Blood;
import me.chiss.Core.Modules.Logger;
import mineplex.core.energy.Energy;
import mineplex.core.pet.PetManager;

public abstract interface IPlugin
{
  static
  {
    throw new Error("Unresolved compilation problem: \n\tThe import mineplex.core.packethandler.INameColorer cannot be resolved\n");
  }
  
  public abstract void Log(String paramString1, String paramString2);
  
  public abstract org.bukkit.plugin.java.JavaPlugin GetPlugin();
  
  public abstract me.chiss.Core.Module.ModuleManager GetModules();
  
  public abstract Config GetConfig();
  
  public abstract me.chiss.Core.Modules.Utility GetUtility();
  
  public abstract Blood GetBlood();
  
  public abstract me.chiss.Core.Modules.BlockRegenerate GetBlockRegenerate();
  
  public abstract mineplex.core.blockrestore.BlockRestore GetBlockRestore();
  
  public abstract mineplex.core.creature.Creature GetCreature();
  
  public abstract Energy GetEnergy();
  
  public abstract mineplex.minecraft.game.core.fire.Fire GetFire();
  
  public abstract Logger GetLogger();
  
  public abstract LootFactory GetLoot();
  
  public abstract me.chiss.Core.Modules.Observer GetObserver();
  
  public abstract me.chiss.Core.Server.Server GetServer();
  
  public abstract mineplex.core.spawn.Spawn GetSpawn();
  
  public abstract mineplex.core.teleport.Teleport GetTeleport();
  
  public abstract mineplex.core.projectile.ProjectileManager GetThrow();
  
  public abstract org.bukkit.Location GetSpawnLocation();
  
  public abstract String GetWebServerAddress();
  
  public abstract PetManager GetPetManager();
}
