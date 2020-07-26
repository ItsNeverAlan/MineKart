package mineplex.minecraft.game.core.condition;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;
import mineplex.core.MiniPlugin;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTime.TimeUnit;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.condition.events.ConditionApplyEvent;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class ConditionManager extends MiniPlugin
{
  private ConditionFactory _factory;
  private ConditionApplicator _applicator;
  protected ConditionEffect Effect;
  private WeakHashMap<LivingEntity, LinkedList<Condition>> _conditions = new WeakHashMap();
  private WeakHashMap<LivingEntity, LinkedList<ConditionIndicator>> _indicators = new WeakHashMap();
  private WeakHashMap<LivingEntity, Entity> _buffer = new WeakHashMap();
  private HashSet<LivingEntity> _hideIndicator = new HashSet();
  private HashSet<Entity> _items = new HashSet();
  
  public ConditionManager(JavaPlugin plugin)
  {
    super("Condition Manager", plugin);
    
    Factory();
    Applicator();
    Effect();
  }
  
  public ConditionFactory Factory()
  {
    if (this._factory == null) {
      this._factory = new ConditionFactory(this);
    }
    return this._factory;
  }
  
  public ConditionApplicator Applicator()
  {
    if (this._applicator == null) {
      this._applicator = new ConditionApplicator();
    }
    return this._applicator;
  }
  
  public ConditionEffect Effect()
  {
    if (this.Effect == null) {
      this.Effect = new ConditionEffect(this);
    }
    return this.Effect;
  }
  
  public void Disable()
  {
    Iterator localIterator2;
    for (Iterator localIterator1 = this._indicators.keySet().iterator(); localIterator1.hasNext(); 
        localIterator2.hasNext())
    {
      LivingEntity ent = (LivingEntity)localIterator1.next();
      localIterator2 = ((LinkedList)this._indicators.get(ent)).iterator(); continue;ConditionIndicator ind = (ConditionIndicator)localIterator2.next();
      
      if (ind.GetIndicator() != null) {
        ind.GetIndicator().remove();
      }
      HandlerList.unregisterAll(ind);
    }
  }
  

  public Condition AddCondition(Condition newCon)
  {
    ConditionApplyEvent condEvent = new ConditionApplyEvent(newCon);
    GetPlugin().getServer().getPluginManager().callEvent(condEvent);
    
    if (condEvent.isCancelled()) {
      return null;
    }
    
    if (!this._conditions.containsKey(newCon.GetEnt())) {
      this._conditions.put(newCon.GetEnt(), new LinkedList());
    }
    ((LinkedList)this._conditions.get(newCon.GetEnt())).add(newCon);
    

    newCon.OnConditionAdd();
    

    HandleIndicator(newCon);
    
    return newCon;
  }
  
  public void HandleIndicator(Condition newCon)
  {
    ConditionIndicator ind = GetIndicatorType(newCon);
    

    if (ind == null)
    {
      AddIndicator(newCon);

    }
    else
    {
      UpdateIndicator(ind, newCon);
    }
  }
  
  public ConditionIndicator GetIndicatorType(Condition newCon)
  {
    if (!this._indicators.containsKey(newCon.GetEnt())) {
      this._indicators.put(newCon.GetEnt(), new LinkedList());
    }
    for (ConditionIndicator ind : (LinkedList)this._indicators.get(newCon.GetEnt())) {
      if (ind.GetCondition().GetType() == newCon.GetType())
        return ind;
    }
    return null;
  }
  

  public void AddIndicator(Condition newCon)
  {
    ConditionIndicator newInd = new ConditionIndicator(newCon);
    

    if (!this._indicators.containsKey(newCon.GetEnt())) {
      this._indicators.put(newCon.GetEnt(), new LinkedList());
    }
    LinkedList<ConditionIndicator> entInds = (LinkedList)this._indicators.get(newCon.GetEnt());
    























    UtilServer.getServer().getPluginManager().registerEvents(newInd, this._plugin);
    

    entInds.addFirst(newInd);
    

    if (newCon.GetInformOn() != null) {
      UtilPlayer.message(newCon.GetEnt(), F.main("Condition", newCon.GetInformOn()));
    }
  }
  

  public void UpdateIndicator(ConditionIndicator ind, Condition newCon)
  {
    if ((!ind.GetCondition().IsExpired()) && 
      (ind.GetCondition().IsBetterOrEqual(newCon, newCon.IsAdd()))) {
      return;
    }
    ind.SetCondition(newCon);
  }
  
  @EventHandler(priority=EventPriority.HIGHEST)
  public void ExpireConditions(UpdateEvent event)
  {
    if (event.getType() != UpdateType.TICK) {
      return;
    }
    Iterator<Condition> conditionIterator;
    for (Iterator localIterator = this._conditions.keySet().iterator(); localIterator.hasNext(); 
        


        conditionIterator.hasNext())
    {
      LivingEntity ent = (LivingEntity)localIterator.next();
      
      conditionIterator = ((LinkedList)this._conditions.get(ent)).iterator();
      
      continue;
      
      Condition cond = (Condition)conditionIterator.next();
      
      if (cond.Tick()) {
        conditionIterator.remove();
      }
    }
    
    Iterator<ConditionIndicator> conditionIndicatorIterator;
    for (localIterator = this._indicators.keySet().iterator(); localIterator.hasNext(); 
        


        conditionIndicatorIterator.hasNext())
    {
      LivingEntity ent = (LivingEntity)localIterator.next();
      
      conditionIndicatorIterator = ((LinkedList)this._indicators.get(ent)).iterator();
      
      continue;
      
      ConditionIndicator conditionIndicator = (ConditionIndicator)conditionIndicatorIterator.next();
      
      if (conditionIndicator.GetCondition().IsExpired())
      {
        Condition replacement = GetBestCondition(ent, conditionIndicator.GetCondition().GetType());
        
        if (replacement == null)
        {

          if ((!this._hideIndicator.contains(ent)) && (conditionIndicator.GetIndicator() != null))
          {
            Entity below = conditionIndicator.GetIndicator().getVehicle();
            Entity above = conditionIndicator.GetIndicator().getPassenger();
            
            conditionIndicator.GetIndicator().eject();
            conditionIndicator.GetIndicator().leaveVehicle();
            
            if ((above != null) && (below != null)) {
              below.setPassenger(above);
            }
            Vector vec = new Vector(Math.random() - 0.5D, 0.0D, Math.random() - 0.5D);
            vec.normalize().multiply(0.1D).setY(0.2D);
            conditionIndicator.GetIndicator().setVelocity(vec);
            

            this._items.add(conditionIndicator.GetIndicator());
          }
          
          HandlerList.unregisterAll(conditionIndicator);
          conditionIndicatorIterator.remove();
          

          if (((LinkedList)this._indicators.get(ent)).isEmpty())
          {
            RemoveBuffer((Entity)this._buffer.remove(ent));
          }
          


          if (conditionIndicator.GetCondition().GetInformOff() != null) {
            UtilPlayer.message(conditionIndicator.GetCondition().GetEnt(), F.main("Condition", conditionIndicator.GetCondition().GetInformOff()));
          }
        } else {
          UpdateIndicator(conditionIndicator, replacement);
        }
      }
    }
  }
  
  public Condition GetBestCondition(LivingEntity ent, Condition.ConditionType type)
  {
    if (!this._conditions.containsKey(ent)) {
      return null;
    }
    Condition best = null;
    
    for (Condition con : (LinkedList)this._conditions.get(ent))
    {
      if (con.GetType() == type)
      {

        if (!con.IsExpired())
        {

          if (best == null)
          {
            best = con;


          }
          else if (con.IsBetterOrEqual(best, false))
            best = con; }
      }
    }
    return best;
  }
  
  public Condition GetActiveCondition(LivingEntity ent, Condition.ConditionType type)
  {
    if (!this._indicators.containsKey(ent)) {
      return null;
    }
    for (ConditionIndicator ind : (LinkedList)this._indicators.get(ent))
    {
      if (ind.GetCondition().GetType() == type)
      {

        if (!ind.GetCondition().IsExpired())
        {

          return ind.GetCondition(); }
      }
    }
    return null;
  }
  
  @EventHandler
  public void Remove(UpdateEvent event)
  {
    if (event.getType() != UpdateType.TICK) {
      return;
    }
    HashSet<Entity> expired = new HashSet();
    
    for (Entity cur : this._items) {
      if ((UtilEnt.isGrounded(cur)) || (cur.isDead()) || (!cur.isValid()))
        expired.add(cur);
    }
    for (Entity cur : expired)
    {
      this._items.remove(cur);
      cur.remove();
    }
  }
  
  @EventHandler
  public void Respawn(PlayerRespawnEvent event)
  {
    Clean(event.getPlayer());
  }
  
  @EventHandler
  public void Quit(PlayerQuitEvent event)
  {
    Clean(event.getPlayer());
  }
  

  @EventHandler(priority=EventPriority.MONITOR)
  public void Death(EntityDeathEvent event)
  {
    if (((event.getEntity() instanceof Player)) && 
      (event.getEntity().getHealth() > 0.0D)) {
      return;
    }
    Clean(event.getEntity());
  }
  

  public void Clean(LivingEntity ent)
  {
    this._conditions.remove(ent);
    

    RemoveBuffer((Entity)this._buffer.remove(ent));
    
    this._hideIndicator.remove(ent);
    

    LinkedList<ConditionIndicator> inds = (LinkedList)this._indicators.remove(ent);
    if (inds == null) {
      return;
    }
    for (ConditionIndicator ind : inds)
    {
      HandlerList.unregisterAll(ind);
      
      if (ind.GetCondition().IsVisible()) {
        ind.GetIndicator().remove();
      }
    }
  }
  
  public void DebugInfo(Player player) {
    int count = 0;
    for (LivingEntity ent : this._indicators.keySet())
    {
      if ((ent.isDead()) || (!ent.isValid()) || (((ent instanceof Player)) && (!((Player)ent).isOnline())))
      {
        count++;
      }
    }
    
    player.sendMessage(F.main(GetName(), count + " Invalid Indicators."));
    
    count = 0;
    for (LivingEntity ent : this._conditions.keySet())
    {
      if ((ent.isDead()) || (!ent.isValid()) || (((ent instanceof Player)) && (!((Player)ent).isOnline())))
      {
        count++;
      }
    }
    
    player.sendMessage(F.main(GetName(), count + " Invalid Conditions."));
    
    count = 0;
    for (LivingEntity ent : this._buffer.keySet())
    {
      if ((ent.isDead()) || (!ent.isValid()) || (((ent instanceof Player)) && (!((Player)ent).isOnline())))
      {
        count++;
      }
    }
    
    player.sendMessage(F.main(GetName(), count + " Invalid Buffers."));
    
    count = 0;
    for (Entity ent : this._items)
    {
      if ((ent.isDead()) || (!ent.isValid()) || (((ent instanceof Player)) && (!((Player)ent).isOnline())))
      {
        count++;
      }
    }
    
    player.sendMessage(F.main(GetName(), count + " Invalid Items."));
    
    count = 0;
    for (LivingEntity ent : this._hideIndicator)
    {
      if ((ent.isDead()) || (!ent.isValid()) || (((ent instanceof Player)) && (!((Player)ent).isOnline())))
      {
        count++;
      }
    }
    
    player.sendMessage(F.main(GetName(), count + " Invalid Hidden Indicators."));
  }
  
  @EventHandler
  public void Debug(UpdateEvent event)
  {
    if (event.getType() != UpdateType.SEC) {
      return;
    }
    for (LivingEntity ent : this._indicators.keySet())
    {
      if ((ent instanceof Player))
      {

        Player player = (Player)ent;
        if (player.getItemInHand() != null)
        {

          if (player.getItemInHand().getType() == Material.PAPER)
          {

            if (player.isOp())
            {

              UtilPlayer.message(player, C.cGray + ((LinkedList)this._indicators.get(ent)).size() + " Indicators ----------- " + ((LinkedList)this._conditions.get(ent)).size() + " Conditions");
              for (ConditionIndicator ind : (LinkedList)this._indicators.get(ent))
                UtilPlayer.message(player, 
                  F.elem(new StringBuilder().append(ind.GetCondition().GetType()).append(" ").append(ind.GetCondition().GetMult() + 1).toString()) + " for " + 
                  F.time(mineplex.core.common.util.UtilTime.convertString(ind.GetCondition().GetTicks() * 50L, 1, UtilTime.TimeUnit.FIT)) + " via " + 
                  F.skill(ind.GetCondition().GetReason()) + " from " + 
                  F.name(UtilEnt.getName(ind.GetCondition().GetSource())) + ".");
            } } }
      } }
  }
  
  @EventHandler
  public void Pickup(PlayerPickupItemEvent event) {
    if (event.isCancelled()) {
      return;
    }
    if (this._items.contains(event.getItem())) {
      event.setCancelled(true);
    }
    else if (this._buffer.containsValue(event.getItem())) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler
  public void HopperPickup(InventoryPickupItemEvent event) {
    if (event.isCancelled()) {
      return;
    }
    if (this._items.contains(event.getItem())) {
      event.setCancelled(true);
    }
    else if (this._buffer.containsValue(event.getItem())) {
      event.setCancelled(true);
    }
  }
  
  public void EndCondition(LivingEntity target, Condition.ConditionType type, String reason) {
    if (!this._conditions.containsKey(target)) {
      return;
    }
    for (Condition cond : (LinkedList)this._conditions.get(target)) {
      if (((reason == null) || (cond.GetReason().equals(reason))) && (
        (type == null) || (cond.GetType() == type)))
      {
        cond.Expire();
        
        Condition best = GetBestCondition(target, cond.GetType());
        if (best != null) best.Apply();
      }
    }
  }
  
  public boolean HasCondition(LivingEntity target, Condition.ConditionType type, String reason) {
    if (!this._conditions.containsKey(target)) {
      return false;
    }
    for (Condition cond : (LinkedList)this._conditions.get(target)) {
      if (((reason == null) || (cond.GetReason().equals(reason))) && (
        (type == null) || (cond.GetType() == type)))
        return true;
    }
    return false;
  }
  
  public WeakHashMap<LivingEntity, LinkedList<ConditionIndicator>> GetIndicators()
  {
    return this._indicators;
  }
  
  public void SetIndicatorVisibility(LivingEntity ent, boolean showIndicator)
  {
    if (!showIndicator)
    {
      if (this._hideIndicator.contains(ent)) {
        return;
      }
      this._hideIndicator.add(ent);
      
      LinkedList<ConditionIndicator> inds = (LinkedList)this._indicators.remove(ent);
      if (inds == null) { return;
      }
      
      for (ConditionIndicator ind : inds)
      {
        HandlerList.unregisterAll(ind);
        
        if (ind.GetCondition().IsVisible()) {
          ind.GetIndicator().remove();
        }
      }
      
      RemoveBuffer((Entity)this._buffer.remove(ent));


    }
    else if (this._hideIndicator.remove(ent)) {
      LoadIndicators(ent);
    }
  }
  
  public void RemoveBuffer(Entity buffer)
  {
    if (buffer == null) {
      return;
    }
    buffer.eject();
    buffer.leaveVehicle();
    buffer.remove();
  }
  
  public void LoadIndicators(LivingEntity ent)
  {
    LinkedList<ConditionIndicator> inds = (LinkedList)this._indicators.get(ent);
    if (inds == null) { return;
    }
    Entity previous = null;
    for (ConditionIndicator ind : inds)
    {

      if ((ind.IsVisible()) && (!this._hideIndicator.contains(ent)))
      {

        if (!this._buffer.containsKey(ent))
        {
          Entity buffer = ent.getWorld().dropItem(ent.getLocation(), ItemStackFactory.Instance.CreateStack(Material.GHAST_TEAR));
          ent.setPassenger(buffer);
          this._buffer.put(ent, buffer);
          previous = buffer;
        }
        
        previous.setPassenger(ind.GetIndicator());
        previous = ind.GetIndicator();
      }
    }
  }
  
  public boolean IsSilenced(LivingEntity ent, String ability)
  {
    if (!this._indicators.containsKey(ent)) {
      return false;
    }
    for (ConditionIndicator ind : (LinkedList)this._indicators.get(ent)) {
      if (ind.GetCondition().GetType() == Condition.ConditionType.SILENCE)
      {
        if (ability != null)
        {
          if ((ent instanceof Player))
          {
            if (Recharge.Instance.use((Player)ent, "Silence Feedback", 200L, false))
            {

              UtilPlayer.message(ent, F.main("Condition", "Cannot use " + F.skill(ability) + " while silenced."));
              

              ((Player)ent).playSound(ent.getLocation(), Sound.BAT_HURT, 0.8F, 0.8F);
            }
          }
        }
        return true;
      }
    }
    return false;
  }
  
  public boolean IsInvulnerable(LivingEntity ent)
  {
    if (!this._indicators.containsKey(ent)) {
      return false;
    }
    for (ConditionIndicator ind : (LinkedList)this._indicators.get(ent)) {
      if (ind.GetCondition().GetType() == Condition.ConditionType.INVULNERABLE)
        return true;
    }
    return false;
  }
  
  public boolean IsCloaked(LivingEntity ent)
  {
    if (!this._indicators.containsKey(ent)) {
      return false;
    }
    for (ConditionIndicator ind : (LinkedList)this._indicators.get(ent)) {
      if (ind.GetCondition().GetType() == Condition.ConditionType.CLOAK)
        return true;
    }
    return false;
  }
  
  @EventHandler
  public void DisableIndicators(UpdateEvent event)
  {
    if (event.getType() != UpdateType.FAST) {
      return;
    }
    Iterator<Map.Entry<LivingEntity, LinkedList<ConditionIndicator>>> conditionIndIterator = this._indicators.entrySet().iterator();
    
    while (conditionIndIterator.hasNext())
    {
      Map.Entry<LivingEntity, LinkedList<ConditionIndicator>> entry = (Map.Entry)conditionIndIterator.next();
      LivingEntity ent = (LivingEntity)entry.getKey();
      
      if ((ent.isDead()) || (!ent.isValid()) || (((ent instanceof Player)) && (!((Player)ent).isOnline())))
      {
        ent.remove();
        
        for (ConditionIndicator ind : (LinkedList)entry.getValue())
        {
          HandlerList.unregisterAll(ind);
          
          if (ind.GetIndicator() != null) {
            ind.GetIndicator().remove();
          }
        }
        conditionIndIterator.remove();
      }
    }
    
    Iterator<Map.Entry<LivingEntity, LinkedList<Condition>>> conditionIterator = this._conditions.entrySet().iterator();
    
    while (conditionIterator.hasNext())
    {
      Map.Entry<LivingEntity, LinkedList<Condition>> entry = (Map.Entry)conditionIterator.next();
      LivingEntity ent = (LivingEntity)entry.getKey();
      
      if ((ent.isDead()) || (!ent.isValid()) || (((ent instanceof Player)) && (!((Player)ent).isOnline())))
      {
        ent.remove();
        
        conditionIterator.remove();
      }
    }
    
    Iterator<Map.Entry<LivingEntity, Entity>> bufferIterator = this._buffer.entrySet().iterator();
    
    while (bufferIterator.hasNext())
    {
      Map.Entry<LivingEntity, Entity> entry = (Map.Entry)bufferIterator.next();
      LivingEntity ent = (LivingEntity)entry.getKey();
      
      if ((ent.isDead()) || (!ent.isValid()) || (((ent instanceof Player)) && (!((Player)ent).isOnline())))
      {
        ent.remove();
        
        bufferIterator.remove();
      }
    }
  }
}
