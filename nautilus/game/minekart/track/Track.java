package nautilus.game.minekart.track;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import mineplex.core.common.util.F;
import mineplex.core.common.util.MapUtil;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.recharge.Recharge;
import mineplex.core.teleport.Teleport;
import nautilus.game.minekart.gp.GP;
import nautilus.game.minekart.gp.GPBattle;
import nautilus.game.minekart.gp.GPManager;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.track.ents.Bomb;
import nautilus.game.minekart.track.ents.Silverfish;
import net.minecraft.server.v1_6_R3.ChunkPreLoadEvent;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public class Track
{
  public static enum TrackState
  {
    Loading, 
    Countdown, 
    Live, 
    Ending, 
    Ended;
  }
  
  private World World = null;
  
  private int MinX = 0;
  private int MinZ = 0;
  private int MaxX = 0;
  private int MaxZ = 0;
  private int CurX = 0;
  private int CurZ = 0;
  
  private GP GP;
  
  private Teleport _teleport;
  
  private Recharge _recharge;
  
  private int _trackId;
  private String _name;
  private String _file;
  private TrackState _state = TrackState.Loading;
  private long _stateTime = System.currentTimeMillis();
  private int _countdown = 6;
  private boolean _nextTrack = false;
  
  private HashMap<Kart, Double> _scores = new HashMap();
  private ArrayList<Kart> _positions = new ArrayList();
  

  private float _yaw = 0.0F;
  
  private ArrayList<Location> _kartStart;
  
  private ArrayList<Location> _returnPoints;
  
  private ArrayList<Location> _trackProgress;
  private ArrayList<TrackItem> _itemBlocks;
  private HashMap<Location, Double> _jumps;
  private ArrayList<TrackEntity> _creatures;
  private ArrayList<Integer> _trackBlocks;
  private ArrayList<Integer> _returnBlocks;
  
  public Track(GP gp, Teleport teleport, Recharge recharge, String file, int id)
  {
    this.GP = gp;
    this._teleport = teleport;
    this._recharge = recharge;
    
    this._trackId = id;
    
    this._file = file;
    
    this._kartStart = new ArrayList();
    this._returnPoints = new ArrayList();
    this._trackProgress = new ArrayList();
    this._itemBlocks = new ArrayList();
    
    this._jumps = new HashMap();
    this._creatures = new ArrayList();
    
    this._trackBlocks = new ArrayList();
    this._returnBlocks = new ArrayList();
    

    this.GP.Manager.TrackManager.RegisterTrack(this);
  }
  
  public GP GetGP()
  {
    return this.GP;
  }
  
  public String GetName()
  {
    return this._name;
  }
  
  public String GetFile()
  {
    return this._file;
  }
  
  public World GetWorld()
  {
    return this.World;
  }
  
  public float GetYaw()
  {
    return this._yaw;
  }
  
  public ArrayList<Location> GetSpawns()
  {
    return this._kartStart;
  }
  
  public HashMap<Kart, Double> GetScores()
  {
    return this._scores;
  }
  
  public ArrayList<Kart> GetPositions()
  {
    return this._positions;
  }
  
  public ArrayList<Location> GetProgress()
  {
    return this._trackProgress;
  }
  
  public ArrayList<Location> GetReturn()
  {
    return this._returnPoints;
  }
  
  public ArrayList<TrackItem> GetItems()
  {
    return this._itemBlocks;
  }
  
  public HashMap<Location, Double> GetJumps()
  {
    return this._jumps;
  }
  
  public ArrayList<TrackEntity> GetCreatures()
  {
    return this._creatures;
  }
  
  public ArrayList<Integer> GetTrackBlocks()
  {
    return this._trackBlocks;
  }
  
  public ArrayList<Integer> GetReturnBlocks()
  {
    return this._returnBlocks;
  }
  
  public void SetState(TrackState state)
  {
    if (this._state != state)
    {
      if ((state == TrackState.Ended) || (this._state == TrackState.Ended)) {
        this.GP.SwitchScoreboards();
      } else if ((state == TrackState.Countdown) || (this._state == TrackState.Countdown)) {
        this.GP.SwitchScoreboards();
      }
    }
    this._state = state;
    this._stateTime = System.currentTimeMillis();
    this._countdown = 31;
    
    if (state == TrackState.Countdown) {
      this._countdown = 8;
    }
  }
  
  public TrackState GetState() {
    return this._state;
  }
  
  public long GetStateTime()
  {
    return this._stateTime;
  }
  
  public void Update()
  {
    String type = "Race";
    if ((this.GP instanceof GPBattle)) {
      type = "Battle";
    }
    if (this._state != TrackState.Loading)
    {


      if (this._state == TrackState.Countdown)
      {
        if (UtilTime.elapsed(this._stateTime, 1000L))
        {
          this._stateTime = System.currentTimeMillis();
          this._countdown -= 1;
          

          for (Player cur : this.GP.GetPlayers())
          {
            if (this._countdown <= 5)
            {


              if (this._countdown > 0)
              {
                UtilPlayer.message(cur, F.main("MK", type + " begins in " + F.time(new StringBuilder(String.valueOf(this._countdown)).append(" Seconds").toString()) + "..."));
                cur.playSound(cur.getLocation(), Sound.NOTE_PIANO, 1.0F, 1.0F);
              }
              else
              {
                UtilPlayer.message(cur, F.main("MK", type + " has started!"));
                cur.playSound(cur.getLocation(), Sound.NOTE_PIANO, 2.0F, 2.0F);
              }
            }
          }
          if (this._countdown <= 0) {
            SetState(TrackState.Live);
          }
        }
      } else if (this._state != TrackState.Live)
      {


        if (this._state == TrackState.Ending)
        {
          boolean allFinished = true;
          
          for (Kart kart : GetGP().GetKarts()) {
            if (!kart.HasFinishedTrack())
              allFinished = false;
          }
          if (allFinished)
          {
            this.GP.Announce(F.main("MK", type + " has ended."));
            SetState(TrackState.Ended);

          }
          else if (UtilTime.elapsed(this._stateTime, 1000L))
          {
            this._stateTime = System.currentTimeMillis();
            this._countdown -= 1;
            

            if (this._countdown % 5 == 0)
            {
              this.GP.Announce(F.main("MK", type + " ends in " + F.time(new StringBuilder(String.valueOf(this._countdown)).append(" Seconds").toString()) + "..."));
              
              for (Player cur : this.GP.GetPlayers())
              {
                if (this._countdown > 0) cur.playSound(cur.getLocation(), Sound.NOTE_PIANO, 1.0F, 0.5F); else {
                  cur.playSound(cur.getLocation(), Sound.NOTE_PIANO, 2.0F, 0.0F);
                }
              }
            }
            if (this._countdown <= 0)
            {
              this.GP.Announce(F.main("MK", type + " has ended."));
              SetState(TrackState.Ended);
            }
          }
        }
        else
        {
          if (this._nextTrack) {
            return;
          }
          if (UtilTime.elapsed(this._stateTime, 10000L))
          {
            GetGP().NextTrack();
            this._nextTrack = true;
          }
        } }
    }
  }
  
  protected String GetFolder() {
    return GetGP().GetId() + "-" + GetGP().GetSet().GetName() + "-" + GetFile();
  }
  
  public void Initialize()
  {
    final Track track = this;
    
    System.out.println("Initializing....");
    
    UtilServer.getServer().getScheduler().runTaskAsynchronously(this.GP.Manager.GetPlugin(), new Runnable()
    {

      public void run()
      {
        track.UnzipWorld();
        

        UtilServer.getServer().getScheduler().runTask(Track.this.GP.Manager.GetPlugin(), new Runnable()
        {

          public void run()
          {
            Track.this.World = mineplex.core.common.util.WorldUtil.LoadWorld(new WorldCreator(Track.this.GetFolder()));
            

            this.val$track.LoadTrackData();
          }
        });
      }
    });
  }
  
  protected void UnzipWorld()
  {
    String folder = GetFolder();
    new File(folder).mkdir();
    new File(folder + File.separatorChar + "region").mkdir();
    new File(folder + File.separatorChar + "data").mkdir();
    nautilus.minecraft.core.utils.ZipUtil.UnzipToDirectory(GetFile() + ".zip", folder);
  }
  

  public void LoadTrackData()
  {
    try
    {
      FileInputStream fstream = new FileInputStream(GetFolder() + File.separatorChar + "TrackInfo.dat");
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new java.io.InputStreamReader(in));
      
      String line;
      while ((line = br.readLine()) != null) {
        String line;
        String[] tokens = line.split(":");
        
        if (tokens.length >= 2)
        {

          if (tokens[0].length() != 0)
          {


            if (tokens[0].equalsIgnoreCase("TRACK_NAME"))
            {
              this._name = tokens[1];

            }
            else if (tokens[0].equalsIgnoreCase("ROAD_TYPES"))
            {
              try
              {
                this._trackBlocks.add(Integer.valueOf(Integer.parseInt(tokens[1])));
              }
              catch (Exception e)
              {
                System.out.println("Track Data Read Error: Invalid Road Type [" + tokens[1] + "]");
              }
              
            }
            else if (tokens[0].equalsIgnoreCase("RETURN_TYPES"))
            {
              try
              {
                this._returnBlocks.add(Integer.valueOf(Integer.parseInt(tokens[1])));
              }
              catch (Exception e)
              {
                System.out.println("Track Data Read Error: Invalid Return Type [" + tokens[1] + "]");
              }
              
            }
            else if (tokens[0].equalsIgnoreCase("SPAWN_DIRECTION"))
            {
              try
              {
                this._yaw = Float.valueOf(tokens[1]).floatValue();
              }
              catch (Exception e)
              {
                System.out.println("Track Data Read Error: Invalid Yaw [" + tokens[1] + "]");
              }
              
            }
            else if (tokens[0].equalsIgnoreCase("SPAWNS"))
            {
              for (int i = 1; i < tokens.length; i++)
              {
                Location loc = StrToLoc(tokens[i]);
                if (loc != null)
                {
                  this._kartStart.add(loc);
                }
                
              }
            } else if (tokens[0].equalsIgnoreCase("PROGRESS"))
            {
              for (int i = 1; i < tokens.length; i++)
              {
                Location loc = StrToLoc(tokens[i]);
                if (loc != null)
                {
                  this._trackProgress.add(loc);
                }
                
              }
            } else if (tokens[0].equalsIgnoreCase("RETURNS"))
            {
              for (int i = 1; i < tokens.length; i++)
              {
                Location loc = StrToLoc(tokens[i]);
                if (loc != null)
                {
                  this._returnPoints.add(loc);
                }
                
              }
            } else if (tokens[0].equalsIgnoreCase("ITEMS"))
            {
              for (int i = 1; i < tokens.length; i++)
              {
                Location loc = StrToLoc(tokens[i]);
                if (loc != null)
                {
                  this._itemBlocks.add(new TrackItem(loc));
                }
                
              }
            } else if (tokens[0].equalsIgnoreCase("CREATURES"))
            {
              for (int i = 1; i < tokens.length; i++)
              {
                String[] ents = tokens[i].split("@");
                
                Location loc = StrToLoc(ents[1]);
                if (loc != null)
                {
                  loc = loc.getBlock().getLocation();
                  loc.subtract(0.0D, 1.0D, 0.0D);
                  
                  TrackEntity ent = null;
                  
                  if (ents[0].equalsIgnoreCase("BOMB")) ent = new Bomb(this, loc);
                  if (ents[0].equalsIgnoreCase("MOLE")) ent = new nautilus.game.minekart.track.ents.Mole(this, loc);
                  if (ents[0].equalsIgnoreCase("FISH")) ent = new Silverfish(this, loc);
                  if (ents[0].equalsIgnoreCase("TRAIN")) { ent = new nautilus.game.minekart.track.ents.Train(this, loc);
                  }
                  if (ent != null)
                  {
                    this._creatures.add(ent);
                  }
                  else
                  {
                    System.out.println("Track Data Read Error: Invalid Track Entity [" + ents[0] + "]");
                  }
                  
                }
              }
            } else if (tokens[0].equalsIgnoreCase("JUMPS"))
            {
              for (int i = 1; i < tokens.length; i++)
              {
                String[] jumps = tokens[i].split("@");
                
                Location loc = StrToLoc(jumps[1]);
                if (loc != null)
                {
                  loc = loc.getBlock().getLocation();
                  
                  this._jumps.put(loc, Double.valueOf(Double.parseDouble(jumps[0])));
                }
              }
            } else if (tokens[0].equalsIgnoreCase("MIN_X"))
            {
              try
              {
                this.MinX = Integer.parseInt(tokens[1]);
                this.CurX = this.MinX;
              }
              catch (Exception e)
              {
                System.out.println("Track Data Read Error: Invalid MinX [" + tokens[1] + "]");
              }
              
            }
            else if (tokens[0].equalsIgnoreCase("MAX_X"))
            {
              try
              {
                this.MaxX = Integer.parseInt(tokens[1]);
              }
              catch (Exception e)
              {
                System.out.println("Track Data Read Error: Invalid MaxX [" + tokens[1] + "]");
              }
              
            } else if (tokens[0].equalsIgnoreCase("MIN_Z"))
            {
              try
              {
                this.MinZ = Integer.parseInt(tokens[1]);
                this.CurZ = this.MinZ;
              }
              catch (Exception e)
              {
                System.out.println("Track Data Read Error: Invalid MinZ [" + tokens[1] + "]");
              }
              
            } else if (tokens[0].equalsIgnoreCase("MAX_Z"))
            {
              try
              {
                this.MaxZ = Integer.parseInt(tokens[1]);
              }
              catch (Exception e)
              {
                System.out.println("Track Data Read Error: Invalid MaxZ [" + tokens[1] + "]");
              } }
          }
        }
      }
      in.close();
      
      this.GP.Manager.TrackManager.LoadTrack(this);
    }
    catch (Exception e)
    {
      System.err.println("Error: " + e.getMessage());
    }
  }
  
  public boolean LoadChunks(long maxMilliseconds)
  {
    long startTime = System.currentTimeMillis();
    for (; 
        this.CurX <= this.MaxX; this.CurX += 16)
    {
      for (; this.CurZ <= this.MaxZ; this.CurZ += 16)
      {
        if (System.currentTimeMillis() - startTime >= maxMilliseconds) {
          return false;
        }
        this.World.getChunkAt(new Location(this.World, this.CurX, 0.0D, this.CurZ));
      }
      
      this.CurZ = this.MinZ;
    }
    
    return true;
  }
  

  public void Uninitialize()
  {
    this._kartStart.clear();
    this._returnPoints.clear();
    this._trackProgress.clear();
    this._itemBlocks.clear();
    this._jumps.clear();
    this._creatures.clear();
    this._trackBlocks.clear();
    this._returnBlocks.clear();
    

    MapUtil.UnloadWorld(GetGP().Manager.GetPlugin(), this.World);
    MapUtil.ClearWorldReferences(this.World.getName());
    mineplex.core.common.util.FileUtil.DeleteFolder(new File(this.World.getName()));
    
    this.World = null;
  }
  
  private Location StrToLoc(String loc)
  {
    String[] coords = loc.split(",");
    
    try
    {
      return new Location(this.World, Integer.valueOf(coords[0]).intValue() + 0.5D, Integer.valueOf(coords[1]).intValue(), Integer.valueOf(coords[2]).intValue() + 0.5D);
    }
    catch (Exception e)
    {
      System.out.println("Track Data Read Error: Invalid Location String [" + loc + "]");
    }
    
    return null;
  }
  
  public void SpawnTeleport()
  {
    Track prevTrack = this.GP.GetTrack(this._trackId - 1);
    

    if (prevTrack != null)
    {
      int i = 0;
      for (Kart kart : prevTrack.GetPositions())
      {
        Location loc = (Location)GetSpawns().get(i);
        loc.setYaw(GetYaw());
        loc.setPitch(30.0F);
        

        if ((GetGP() instanceof GPBattle))
        {
          Vector dir = UtilAlg.getTrajectory(kart.GetDriver().getLocation(), (Location)GetProgress().get(0));
          loc.setYaw(UtilAlg.GetYaw(dir));
          loc.setPitch(UtilAlg.GetPitch(dir));
        }
        
        this._teleport.TP(kart.GetDriver(), loc, false);
        i++;
      }
      
    }
    else
    {
      int i = 0;
      for (Player player : GetGP().GetPlayers())
      {
        Location loc = (Location)GetSpawns().get(i);
        loc.setYaw(GetYaw());
        loc.setPitch(30.0F);
        

        if ((GetGP() instanceof GPBattle))
        {
          Vector dir = UtilAlg.getTrajectory(player.getLocation(), (Location)GetProgress().get(0));
          loc.setYaw(UtilAlg.GetYaw(dir));
          loc.setPitch(UtilAlg.GetPitch(dir));
        }
        
        this._teleport.TP(player, loc, false);
        i++;
      }
    }
  }
  
  public void RemoveKart(Kart kart)
  {
    this._scores.remove(kart);
    this._positions.remove(kart);
  }
  
  public void ChunkUnload(ChunkUnloadEvent event)
  {
    if (this.World == null) {
      return;
    }
    if (!event.getWorld().equals(this.World)) {
      return;
    }
    event.setCancelled(true);
  }
  
  public void ChunkLoad(ChunkPreLoadEvent event)
  {
    if (this.World == null) {
      return;
    }
    if (!event.GetWorld().equals(this.World)) {
      return;
    }
    int x = event.GetX();
    int z = event.GetZ();
    
    if ((x >= this.MinX >> 4) && (x <= this.MaxX >> 4) && (z >= this.MinZ >> 4) && (z <= this.MaxZ >> 4))
    {
      return;
    }
    
    event.setCancelled(true);
  }
  
  public Recharge GetRecharge()
  {
    return this._recharge;
  }
}
