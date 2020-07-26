package me.chiss.Core.Modules;

import java.util.Arrays;
import java.util.List;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.fakeEntity.FakeEntity;
import net.minecraft.server.v1_6_R3.EntityPlayer;
import net.minecraft.server.v1_6_R3.Packet28EntityVelocity;
import net.minecraft.server.v1_6_R3.PlayerConnection;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class UpdateThread
  extends Thread
{
  private EntityPlayer _player;
  private List<FakeEntity> _entities;
  private int counter = 0;
  
  public UpdateThread(EntityPlayer mcPlayer, FakeEntity... entities)
  {
    this._player = mcPlayer;
    this._entities = Arrays.asList(entities);
  }
  
  public void run()
  {
    int incrementer = 1;
    while (this.counter >= 0)
    {
      int i = 0;
      for (FakeEntity item : this._entities)
      {
        double radialLead = i * 2.094395102393195D;
        i++;
        
        Vector desiredA = GetTarget(this._player.locX, this._player.locY, this._player.locZ, this.counter, radialLead);
        











        Vector vel = UtilAlg.getTrajectory(item.GetLocation().toVector(), desiredA);
        
        vel = vel.normalize();
        





        vel = vel.multiply(0.08D);
        
        this._player.playerConnection.sendPacket(new Packet28EntityVelocity(item.GetEntityId(), vel.getX(), vel.getY(), vel.getZ()));
        item.SetLocation(item.GetLocation().add(vel));
        
        (this.counter % 20);
      }
      



      if (this.counter == 200) {
        incrementer = -1;
      }
      this.counter += incrementer;
      
      try
      {
        Thread.sleep(50L);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
  }
  

  public static Vector GetTarget(double originX, double originY, double originZ, int tick, double radialLead)
  {
    double speed = 10.0D;
    
    double oX = Math.sin(tick / speed + radialLead) * 1.5D;
    double oY = 0.5D;
    double oZ = Math.cos(tick / speed + radialLead) * 1.5D;
    
    return new Vector(originX + oX, originY + oY, originZ + oZ);
  }
}
