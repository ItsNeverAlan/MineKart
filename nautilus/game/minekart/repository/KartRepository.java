package nautilus.game.minekart.repository;

import java.util.List;
import mineplex.core.server.remotecall.JsonWebCall;
import org.bukkit.craftbukkit.libs.com.google.gson.reflect.TypeToken;



public class KartRepository
{
  private String _webAddress;
  
  public KartRepository(String webserverAddress)
  {
    this._webAddress = webserverAddress;
  }
  
  public List<KartItemToken> GetKartItems(List<KartItemToken> itemTokens)
  {
    (List)new JsonWebCall(this._webAddress + "MineKart/GetKartItems").Execute(new TypeToken() {}.getType(), itemTokens);
  }
}
