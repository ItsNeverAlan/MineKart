package nautilus.game.minekart.track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import mineplex.core.common.util.UtilMath;
import nautilus.game.minekart.gp.GP;
import nautilus.game.minekart.kart.Kart;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class TrackLogic
{
  public static void Positions(Track track)
  {
    if (track.GetWorld() == null) {
      return;
    }
    if ((track.GetState() == Track.TrackState.Loading) || (track.GetState() == Track.TrackState.Ended)) {
      return;
    }
    for (Kart kart : track.GetGP().GetKarts()) {
      SetKartProgress(track, kart);
    }
    
    track.GetScores().clear();
    for (Kart kart : track.GetGP().GetKarts()) {
      track.GetScores().put(kart, Double.valueOf(kart.GetLapScore()));
    }
    
    ArrayList<Kart> pos = track.GetPositions();
    

    pos.clear();
    for (Kart kart : track.GetScores().keySet())
    {
      boolean added = false;
      
      for (int i = 0; i < pos.size(); i++)
      {

        if ((kart.HasFinishedTrack()) && (!((Kart)pos.get(i)).HasFinishedTrack()))
        {
          pos.add(i, kart);
          added = true;
          break;
        }
        

        if ((kart.HasFinishedTrack()) || (!((Kart)pos.get(i)).HasFinishedTrack()))
        {




          if ((kart.HasFinishedTrack()) && (((Kart)pos.get(i)).HasFinishedTrack()))
          {
            if (kart.GetLapPlace() < ((Kart)pos.get(i)).GetLapPlace())
            {
              pos.add(i, kart);
              added = true;
              break;

            }
            


          }
          else if (((Double)track.GetScores().get(kart)).doubleValue() > ((Double)track.GetScores().get(pos.get(i))).doubleValue())
          {
            pos.add(i, kart);
            added = true;
            break;
          }
        }
      }
      if (!added) {
        pos.add(kart);
      }
    }
    
    for (int i = 0; i < pos.size(); i++) {
      ((Kart)pos.get(i)).SetLapPlace(i);
    }
    if (track.GetState() == Track.TrackState.Live)
    {
      for (Kart kart : track.GetScores().keySet())
      {
        if (kart.HasFinishedTrack()) {
          track.SetState(Track.TrackState.Ending);
        }
      }
    }
  }
  
  private static void SetKartProgress(Track track, Kart kart) {
    if (kart.HasFinishedTrack()) {
      return;
    }
    int node = -1;
    double bestDist = 9999.0D;
    
    for (int i = 0; i < track.GetProgress().size(); i++)
    {
      Location cur = (Location)track.GetProgress().get(i);
      double dist = UtilMath.offset(kart.GetDriver().getLocation(), cur);
      
      if (node == -1)
      {
        node = i;
        bestDist = dist;

      }
      else if (dist < bestDist)
      {
        node = i;
        bestDist = dist;
      }
    }
    
    if (node == -1) {
      return;
    }
    
    double score = 1000 * (node + 1);
    
    Location next = (Location)track.GetProgress().get((node + 1) % track.GetProgress().size());
    score -= UtilMath.offset(kart.GetDriver().getLocation(), next);
    

    kart.SetLapNode(node);
    kart.SetLapScore(score);
  }
  
  public static void UpdateItems(Track track)
  {
    if (track.GetWorld() == null) {
      return;
    }
    for (TrackItem item : track.GetItems())
    {
      if ((item.GetEntity() != null) && (!item.GetEntity().isValid())) {
        item.SetEntity(null);
      }
      if (item.GetEntity() != null)
      {
        if (item.GetEntity().getLocation().getY() <= item.GetLocation().getY()) {
          item.GetEntity().setVelocity(new Vector(0.0D, 0.2D, 0.0D));
        }
        item.GetEntity().setTicksLived(1);
      }
      
      if (mineplex.core.common.util.UtilTime.elapsed(item.GetDelay(), 6000L))
      {

        if (item.GetEntity() == null)
          item.SpawnEntity(track.GetWorld());
      }
    }
  }
  
  public static void PickupItem(Track track) {
    if (track.GetWorld() == null) {
      return;
    }
    for (Kart kart : track.GetGP().GetKarts())
    {
      if (!kart.HasFinishedTrack())
      {

        for (TrackItem item : track.GetItems())
        {
          if (item.GetEntity() != null)
          {

            if (UtilMath.offset(item.GetLocation(), kart.GetDriver().getLocation()) < 1.0D)
              item.Pickup(kart); }
        }
      }
    }
  }
  
  public static void Jump(Track track) {
    for (Kart kart : track.GetGP().GetKarts())
    {
      Block block = kart.GetDriver().getLocation().getBlock().getRelative(BlockFace.DOWN);
      
      if (block.getType() == Material.EMERALD_BLOCK)
      {

        if (track.GetJumps().containsKey(block.getLocation()))
        {

          if (track.GetRecharge().use(kart.GetDriver(), "Track Jump", 2000L, false))
          {


            Vector vel = kart.GetVelocity();
            





            vel.add(new Vector(0.0D, ((Double)track.GetJumps().get(block.getLocation())).doubleValue(), 0.0D));
          } } }
    }
  }
  
  public static void CollideMob(Track track) { Iterator localIterator2;
    for (Iterator localIterator1 = track.GetGP().GetKarts().iterator(); localIterator1.hasNext(); 
        
        localIterator2.hasNext())
    {
      Kart kart = (Kart)localIterator1.next();
      
      localIterator2 = track.GetCreatures().iterator(); continue;TrackEntity mob = (TrackEntity)localIterator2.next();
      
      mob.CheckCollision(kart);
    }
  }
  

  public static void UpdateEntities(Track track)
  {
    Iterator<TrackEntity> iterator = track.GetCreatures().iterator();
    
    while (iterator.hasNext())
    {
      TrackEntity ent = (TrackEntity)iterator.next();
      
      if (ent.Update())
      {
        if (ent.GetEntity() != null) {
          ent.GetEntity().remove();
        }
        iterator.remove();
      }
    }
  }
}
