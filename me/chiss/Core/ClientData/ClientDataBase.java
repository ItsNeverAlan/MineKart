package me.chiss.Core.ClientData;

import IClientDataLoad;
import IRepository;
import mineplex.core.account.CoreClient;
import mineplex.core.account.CoreClientManager;











public abstract class ClientDataBase<TokenType>
  implements IClientDataLoad
{
  protected CoreClient Client;
  protected String DataName;
  protected IRepository Repository;
  
  public ClientDataBase(CoreClient paramCoreClient, String paramString, IRepository paramIRepository) {}
  
  public ClientDataBase(CoreClient paramCoreClient, String paramString, IRepository paramIRepository, TokenType paramTokenType) {}
  
  public String GetDataName()
  {
    throw new Error("Unresolved compilation problem: \n");
  }
  

  protected abstract void LoadToken(TokenType paramTokenType);
  

  public CoreClientManager Manager()
  {
    throw new Error("Unresolved compilation problem: \n\tThe method Manager() is undefined for the type CoreClient\n");
  }
}
