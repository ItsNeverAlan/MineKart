package nautilus.game.minekart.item;

import java.util.ArrayList;
import mineplex.core.common.util.C;
import nautilus.game.minekart.item.use_default.ItemUse;
import nautilus.game.minekart.item.use_default.UseGreenShell;
import nautilus.game.minekart.item.use_default.UseMushroom;
import nautilus.game.minekart.item.use_default.UseRedShell;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum KartItemType
{
  Banana(
    "Banana", Material.GOLD_INGOT, 1, new nautilus.game.minekart.item.use_default.UseBanana()), 
  BananaBunch("Banana Bunch", Material.GOLD_SPADE, 6, new nautilus.game.minekart.item.use_default.UseBanana()), 
  FakeItem("Fake Item", Material.FLINT, 1, new nautilus.game.minekart.item.use_default.UseFakeItem()), 
  
  SingleGreenShell("Green Shell", Material.SLIME_BALL, 1, new UseGreenShell()), 
  DoubleGreenShell("2x Green Shell", Material.MELON_SEEDS, 1, new UseGreenShell()), 
  TripleGreenShell("3x Green Shell", Material.PUMPKIN_SEEDS, 1, new UseGreenShell()), 
  
  SingleRedShell("Red Shell", Material.MAGMA_CREAM, 1, new UseRedShell()), 
  DoubleRedShell("2x Red Shell", Material.RAW_FISH, 1, new UseRedShell()), 
  TripleRedShell("3x Red Shell", Material.COOKED_FISH, 1, new UseRedShell()), 
  
  Ghost("Ghost", Material.GHAST_TEAR, 1, new nautilus.game.minekart.item.use_default.UseGhost()), 
  Star("Star", Material.NETHER_STAR, 1, new nautilus.game.minekart.item.use_default.UseStar()), 
  Lightning("Lightning", Material.GLOWSTONE_DUST, 1, new nautilus.game.minekart.item.use_default.UseLightning()), 
  
  SingleMushroom("1x Mushroom", Material.BREAD, 1, new UseMushroom()), 
  DoubleMushroom("2x Mushroom", Material.BOWL, 1, new UseMushroom()), 
  TripleMushroom("3x Mushroom", Material.MUSHROOM_SOUP, 1, new UseMushroom()), 
  SuperMushroom("Super Mushroom", Material.GOLDEN_APPLE, 1, new UseMushroom()), 
  
  Chicken(
    "Egg Blaster", Material.EGG, 16, 0.16D, new nautilus.game.minekart.item.use_custom.UseChicken(), 
    new String[] {
    "", 
    ChatColor.RESET + C.cWhite + "16-Round Egg Blaster." }), 
  

  Pig("Pig Stink", Material.PORK, 1, 0.12D, new nautilus.game.minekart.item.use_custom.UsePig(), 
    new String[] {
    "", 
    ChatColor.RESET + C.cWhite + "Confuses all players.", 
    ChatColor.RESET + C.cWhite + "Lasts 20 seconds" }), 
  

  Wolf("Heart Barrier", Material.APPLE, 1, 0.16D, new nautilus.game.minekart.item.use_custom.UseWolf(), 
    new String[] {
    "", 
    ChatColor.RESET + C.cWhite + "Blocks 1 Shell/Banana/Fake Item", 
    ChatColor.RESET + C.cWhite + "Lasts 60 Seconds" }), 
  

  Spider("Spiderlings", Material.SEEDS, 1, 0.16D, new nautilus.game.minekart.item.use_custom.UseSpider(), 
    new String[] {
    "", 
    ChatColor.RESET + C.cWhite + "Release 1 Spiderling at each player.", 
    ChatColor.RESET + C.cWhite + "Spiderlings hunt players, causing a crash.", 
    ChatColor.RESET + C.cWhite + "Lasts 15 seconds." }), 
  

  Blaze("Infernal Kart", Material.BLAZE_POWDER, 1, 0.16D, new nautilus.game.minekart.item.use_custom.UseBlaze(), 
    new String[] {
    "", 
    ChatColor.RESET + C.cWhite + "Boost forwards with amazing handling.", 
    ChatColor.RESET + C.cWhite + "Leaves a trail of flames, slowing players." }), 
  

  Sheep("Super Sheep", Material.IRON_SPADE, 1, 0.08D, new nautilus.game.minekart.item.use_custom.UseSheep(), 
    new String[] {
    "", 
    ChatColor.RESET + C.cWhite + "Super Sheep flies around the track.", 
    ChatColor.RESET + C.cWhite + "Hunts down other nearby players.", 
    ChatColor.RESET + C.cWhite + "Lasts 15 seconds" }), 
  

  Enderman("Blink", Material.ENDER_PEARL, 1, 0.16D, new nautilus.game.minekart.item.use_custom.UseEnderman(), 
    new String[] {
    "", 
    ChatColor.RESET + C.cWhite + "Instantly teleport forward 20 blocks.", 
    ChatColor.RESET + C.cWhite + "Converts velocity into new direction.", 
    ChatColor.RESET + C.cWhite + "3 Uses." }), 
  

  Cow("Stampede", Material.DIAMOND_SPADE, 1, 0.16D, new nautilus.game.minekart.item.use_custom.UseCow(), 
    new String[] {
    "", 
    ChatColor.RESET + C.cWhite + "Angry cows charge foward at players." }), 
  

  Golem("Earthquake", Material.COAL, 1, 0.08D, new nautilus.game.minekart.item.use_custom.UseGolem(), 
    new String[] {
    "", 
    ChatColor.RESET + C.cWhite + "Halves all players velocity.", 
    ChatColor.RESET + C.cWhite + "Enemies are propelled upwards.", 
    ChatColor.RESET + C.cWhite + "More powerful at close range." });
  

  private String _name;
  
  private Material _mat;
  
  private int _amount;
  private ItemUse _action;
  private double _chance = 0.0D;
  private String[] _customDesc = { "Default" };
  
  private KartItemType(String name, Material mat, int amount, ItemUse action)
  {
    this._name = name;
    this._mat = mat;
    this._amount = amount;
    this._action = action;
  }
  
  private KartItemType(String name, Material mat, int amount, double customChance, ItemUse action, String[] customString)
  {
    this._name = name;
    this._mat = mat;
    this._amount = amount;
    
    this._action = action;
    
    this._chance = customChance;
    this._customDesc = customString;
  }
  
  public String GetName()
  {
    return this._name;
  }
  
  public ItemUse GetAction()
  {
    return this._action;
  }
  
  public Material GetMaterial()
  {
    return this._mat;
  }
  
  public int GetAmount()
  {
    return this._amount;
  }
  
  public double GetChance()
  {
    return this._chance;
  }
  
  public String[] GetDesc()
  {
    return this._customDesc;
  }
  
  public static ArrayList<KartItemType> GetItem(int pos)
  {
    ArrayList<KartItemType> itemBag = new ArrayList();
    
    if (pos == -1)
    {
      for (int i = 1; i > 0; i--) {
        itemBag.add(Star);
      }
      for (int i = 1; i > 0; i--) {
        itemBag.add(Ghost);
      }
      for (int i = 1; i > 0; i--) {
        itemBag.add(TripleRedShell);
      }
      for (int i = 2; i > 0; i--) {
        itemBag.add(TripleGreenShell);
      }
      for (int i = 2; i > 0; i--) {
        itemBag.add(BananaBunch);
      }
      for (int i = 2; i > 0; i--) {
        itemBag.add(FakeItem);
      }
      for (int i = 3; i > 0; i--) {
        itemBag.add(SingleRedShell);
      }
      for (int i = 3; i > 0; i--) {
        itemBag.add(Banana);
      }
      for (int i = 4; i > 0; i--) {
        itemBag.add(SingleGreenShell);
      }
    }
    else
    {
      for (int i = 20 - (27 - pos); i > 0; i--) {
        itemBag.add(Lightning);
      }
      for (int i = 20 - (18 - pos); i > 0; i--) {
        itemBag.add(Star);
      }
      for (int i = 20 - (18 - pos); i > 0; i--) {
        itemBag.add(SuperMushroom);
      }
      for (int i = 20 - (18 - pos); i > 0; i--) {
        itemBag.add(Ghost);
      }
      for (int i = 20 - (18 - pos); i > 0; i--) {
        itemBag.add(TripleRedShell);
      }
      for (int i = 20 - (9 - pos); i > 0; i--) {
        itemBag.add(TripleGreenShell);
      }
      for (int i = 20 - (9 - pos); i > 0; i--) {
        itemBag.add(TripleMushroom);
      }
      for (int i = 20 - (9 - pos); i > 0; i--) {
        itemBag.add(BananaBunch);
      }
      for (int i = 5 + (18 - pos); i > 0; i--) {
        itemBag.add(SingleRedShell);
      }
      for (int i = 10 + (18 - pos); i > 0; i--) {
        itemBag.add(SingleMushroom);
      }
      for (int i = 10 + (18 - pos); i > 0; i--) {
        itemBag.add(SingleGreenShell);
      }
      for (int i = 0 + (18 - pos); i > 0; i--) {
        itemBag.add(FakeItem);
      }
      for (int i = 0 + (18 - pos); i > 0; i--) {
        itemBag.add(Banana);
      }
    }
    return itemBag;
  }
}
