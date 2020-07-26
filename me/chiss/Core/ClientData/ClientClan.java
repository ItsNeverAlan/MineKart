package me.chiss.Core.ClientData;

import ClanRelation;
import IRepository;
import java.util.HashMap;
import mineplex.core.account.CoreClient;
import nautilus.minecraft.core.webserver.token.Account.ClientClanToken;












public class ClientClan
  extends ClientDataBase<ClientClanToken>
{
  private String _clanName;
  private String _inviter;
  private long _delay;
  private boolean _safe;
  private boolean _mapOn;
  private String _territory;
  private String _owner;
  private boolean _clanChat;
  private boolean _allyChat;
  private HashMap<String, ClanRelation> _relations;
  private String mimic;
  private boolean autoClaim;
  
  public ClientClan(CoreClient paramCoreClient, IRepository paramIRepository) {}
  
  public void Load()
  {
    throw new Error("Unresolved compilation problem: \n\tThe method Load() of type ClientClan must override or implement a supertype method\n");
  }
  

  public void LoadToken(ClientClanToken paramClientClanToken)
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  



  public void Update(String paramString1, String paramString2, long paramLong)
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  


  public boolean CanJoin()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  



  public boolean InClan()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public void Reset()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  

  public String GetClanName()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public void SetClan(String paramString) {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public String GetInviter() {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public void SetInviter(String paramString) {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public long GetDelay() {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public void SetDelay(long paramLong) {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public boolean IsSafe() {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public void SetSafe(boolean paramBoolean) {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public boolean IsMapOn() {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public void SetMapOn(boolean paramBoolean) {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public String GetTerritory() {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public void SetTerritory(String paramString) {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public String GetOwner() {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public void SetOwner(String paramString) {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public boolean IsClanChat() {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public void SetClanChat(boolean paramBoolean) {
    throw new Error("Unresolved compilation problem: \n");
  }
  



  public boolean IsAllyChat()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public void SetAllyChat(boolean paramBoolean) {
    throw new Error("Unresolved compilation problem: \n");
  }
  



  public String GetMimic()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public void SetMimic(String paramString) {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public boolean IsAutoClaim() {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public void SetAutoClaim(boolean paramBoolean)
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  
  public void SetRelationship(String paramString, ClanRelation paramClanRelation) {
    throw new Error("Unresolved compilation problems: \n\tClanRelation cannot be resolved to a type\n\tClanRelation cannot be resolved to a type\n");
  }
  
  public ClanRelation GetRelation(String paramString)
  {
    throw new Error("Unresolved compilation problems: \n\tClanRelation cannot be resolved to a type\n\tClanRelation cannot be resolved to a type\n");
  }
}
