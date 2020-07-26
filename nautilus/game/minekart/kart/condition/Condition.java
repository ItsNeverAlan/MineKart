package nautilus.game.minekart.kart.condition;

import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilPlayer;
import nautilus.game.minekart.gp.GP;
import nautilus.game.minekart.item.KartItemType;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.KartManager;
import nautilus.game.minekart.kart.KartState;
import nautilus.game.minekart.kart.KartUtil;
import nautilus.game.minekart.kart.crash.Crash_Knockback;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;

public class Condition
{
  public static Note.Tone[] starSong = {
    Note.Tone.C, 
    
    0, Note.Tone.C, 
    


    0, 0, 0, Note.Tone.C, 
    




    0, 0, 0, 0, 0, Note.Tone.C, 
    


    0, 0, 0, Note.Tone.C, 
    


    0, 0, 0, Note.Tone.C, 
    
    0, Note.Tone.C, 
    


    0, 0, 0, Note.Tone.C, 
    




    0, 0, 0, 0, 0, Note.Tone.B, 
    
    0, Note.Tone.B, 
    


    0, 0, 0, Note.Tone.B, 
    




    0, 0, 0, 0, 0, Note.Tone.B, 
    


    0, 0, 0, Note.Tone.B, 
    


    0, 0, 0, Note.Tone.B, 
    
    0, Note.Tone.B, 
    


    000Note.Tone.B };
  






  public static void Boost(Kart kart)
  {
    if (!kart.HasCondition(ConditionType.Boost)) {
      return;
    }
    if (!kart.CanControl()) {
      return;
    }
    
    if (kart.GetVelocity().length() <= 0.0D) {
      kart.SetVelocity(kart.GetDriver().getLocation().getDirection().setY(0));
    }
    UtilAlg.Normalize(kart.GetVelocity());
    kart.GetVelocity().multiply(1.2D);
    kart.GetDriver().getWorld().playSound(kart.GetDriver().getLocation(), Sound.FIZZ, 0.2F, 0.5F);
  }
  
  public static void LightningSlow(Kart kart)
  {
    if (!kart.HasCondition(ConditionType.Lightning)) {
      return;
    }
    
    Entity ent = kart.GetDriver();
    
    ent.getWorld().playEffect(ent.getLocation(), Effect.SMOKE, 1);
    ent.getWorld().playEffect(ent.getLocation(), Effect.SMOKE, 3);
    ent.getWorld().playEffect(ent.getLocation(), Effect.SMOKE, 5);
    ent.getWorld().playEffect(ent.getLocation(), Effect.SMOKE, 7);
    


    Vector vel = kart.GetVelocity();
    if (kart.GetCrash() != null) {
      vel = kart.GetCrash().GetVelocity();
    }
    if (vel.length() <= 0.0D) {
      return;
    }
    
    if (KartUtil.IsGrounded(kart))
    {
      double drag = 0.008D;
      

      vel.multiply(1.0D - drag);
      

      Vector dragVec = new Vector(vel.getX(), 0.0D, vel.getZ());
      UtilAlg.Normalize(dragVec);
      dragVec.multiply(0.0016D);
      vel.subtract(dragVec);
    }
  }
  
  public static void StarEffect(Kart kart)
  {
    if (!kart.HasCondition(ConditionType.Star)) {
      return;
    }
    if (!kart.GetDriver().isOnline()) {
      return;
    }
    
    if (starSong[(kart.GetStarSongTick() % starSong.length)] != null)
    {
      kart.GetDriver().getWorld().playSound(kart.GetDriver().getLocation(), Sound.NOTE_PIANO, 1.0F, 
        mineplex.core.common.util.UtilSound.GetPitch(new Note(1, starSong[(kart.GetStarSongTick() % starSong.length)], false)));
    }
    

    if (kart.GetStarSongTick() % 3 == 0)
    {
      Color color = Color.RED;
      
      if (kart.GetStarSongTick() % 12 == 0) { color = Color.BLUE;
      } else if (kart.GetStarSongTick() % 9 == 0) { color = Color.GREEN;
      } else if (kart.GetStarSongTick() % 6 == 0) { color = Color.YELLOW;
      }
      

      ItemStack head = new ItemStack(Material.LEATHER_HELMET);
      LeatherArmorMeta meta = (LeatherArmorMeta)head.getItemMeta();
      meta.setColor(color);
      head.setItemMeta(meta);
      
      ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
      meta = (LeatherArmorMeta)chest.getItemMeta();
      meta.setColor(color);
      chest.setItemMeta(meta);
      
      ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
      meta = (LeatherArmorMeta)legs.getItemMeta();
      meta.setColor(color);
      legs.setItemMeta(meta);
      
      ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
      meta = (LeatherArmorMeta)boots.getItemMeta();
      meta.setColor(color);
      boots.setItemMeta(meta);
      
      kart.GetDriver().getInventory().setHelmet(head);
      kart.GetDriver().getInventory().setChestplate(chest);
      kart.GetDriver().getInventory().setLeggings(legs);
      kart.GetDriver().getInventory().setBoots(boots);
    }
    

    kart.SetStarSongTick(kart.GetStarSongTick() + 1);
  }
  
  public static void StarCollide(Kart kart)
  {
    if (!kart.HasCondition(ConditionType.Star)) {
      return;
    }
    for (Kart other : kart.GetGP().GetKarts())
    {
      if (!other.equals(kart))
      {

        if ((!other.HasCondition(ConditionType.Ghost)) && (!other.HasCondition(ConditionType.Star)))
        {

          if (other.GetKartState() == KartState.Drive)
          {

            if (kart.GetDriver().getWorld().equals(other.GetDriver().getWorld()))
            {

              if (mineplex.core.common.util.UtilMath.offset(kart.GetDriver(), other.GetDriver()) <= 1.0D)
              {


                kart.GetDriver().getWorld().playSound(kart.GetDriver().getLocation(), Sound.EXPLODE, 0.5F, 0.5F);
                

                UtilPlayer.message(other.GetDriver(), F.main("MK", F.elem(UtilEnt.getName(kart.GetDriver())) + " rammed you with " + F.item("Star") + "."));
                UtilPlayer.message(kart.GetDriver(), F.main("MK", "You rammed " + F.elem(UtilEnt.getName(other.GetDriver())) + " with " + F.item("Star") + "."));
                

                new Crash_Knockback(other, kart.GetDriver().getLocation(), 1.5D);
              } } } } }
    }
  }
  
  public static void SuperMushroom(Kart kart) {
    if (!kart.HasCondition(ConditionType.SuperMushroom)) {
      return;
    }
    if (kart.GetItemStored() != KartItemType.SuperMushroom) {
      return;
    }
    ItemStack item = kart.GetDriver().getInventory().getItem(3);
    
    if (item == null)
    {
      kart.SetItemStored(KartItemType.SuperMushroom);
    }
    else
    {
      kart.SetItemStored(null);
    }
  }
  
  public static void WolfHeart(Kart kart)
  {
    if (!kart.HasCondition(ConditionType.WolfHeart)) {
      return;
    }
    kart.GetDriver().playEffect(org.bukkit.EntityEffect.WOLF_HEARTS);
  }
  
  public static void BlazeFire(Kart kart)
  {
    if (!kart.HasCondition(ConditionType.BlazeFire)) {
      return;
    }
    
    Vector vel = kart.GetDriver().getLocation().getDirection();
    vel.setY(0);
    vel.normalize();
    kart.GetVelocity().setX(vel.getX());
    kart.GetVelocity().setZ(vel.getZ());
    
    if (kart.GetVelocity().length() <= 0.0D) {
      kart.SetVelocity(kart.GetDriver().getLocation().getDirection().setY(0));
    }
    UtilAlg.Normalize(kart.GetVelocity());
    kart.GetVelocity().multiply(1.1D);
    

    new nautilus.game.minekart.item.world_items_custom.Flame(kart.GetGP().Manager.KartManager.ItemManager, kart, KartUtil.GetBehind(kart));
    kart.GetDriver().getWorld().playSound(kart.GetDriver().getLocation(), Sound.FIRE, 2.0F, 2.0F);
  }
}
