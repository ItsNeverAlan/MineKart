package mineplex.core.antihack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;





public class AntiHackRepository
{
  private AntiHack _plugin;
  private String _serverName;
  private Connection _connection;
  private String _connectionString = "jdbc:mysql://db.mineplex.com:3306/Mineplex";
  private String _userName = "root";
  private String _password = "tAbechAk3wR7tuTh";
  
  private static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS AntiHack (id INT NOT NULL AUTO_INCREMENT, serverName VARCHAR(256) NOT NULL, hackType VARCHAR(256) NOT NULL, playerName VARCHAR(256) NOT NULL, hackCount INT, updated LONG, PRIMARY KEY (id), UNIQUE KEY serverName_hackType_playerName (serverName, hackType, playerName));";
  private static String UPDATE_PLAYER_OFFENSES = "REPLACE INTO AntiHack (serverName, playerName, hackType, hackCount, updated) VALUES (?, ?, ?, ?, now());";
  
  public AntiHackRepository(AntiHack plugin, String serverName)
  {
    this._plugin = plugin;
    this._serverName = serverName;
  }
  
  public void initialize()
  {
    PreparedStatement preparedStatement = null;
    
    try
    {
      if ((this._connection == null) || (this._connection.isClosed())) {
        this._connection = DriverManager.getConnection(this._connectionString, this._userName, this._password);
      }
      
      preparedStatement = this._connection.prepareStatement(CREATE_TABLE);
      preparedStatement.execute();
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      


      if (preparedStatement != null)
      {
        try
        {
          preparedStatement.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
    }
    finally
    {
      if (preparedStatement != null)
      {
        try
        {
          preparedStatement.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
    }
  }
  




















































  public void saveOffenses()
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        PreparedStatement preparedStatement = null;
        
        try
        {
          AntiHackRepository.this._connection = DriverManager.getConnection(AntiHackRepository.this._connectionString, AntiHackRepository.this._userName, AntiHackRepository.this._password);
          
          preparedStatement = AntiHackRepository.this._connection.prepareStatement(AntiHackRepository.UPDATE_PLAYER_OFFENSES);
          



















          preparedStatement.executeBatch();
        }
        catch (Exception exception)
        {
          exception.printStackTrace();
          


          if (preparedStatement != null)
          {
            try
            {
              preparedStatement.close();
            }
            catch (SQLException e)
            {
              e.printStackTrace();
            }
          }
        }
        finally
        {
          if (preparedStatement != null)
          {
            try
            {
              preparedStatement.close();
            }
            catch (SQLException e)
            {
              e.printStackTrace();
            }
          }
        }
      }
    })
    


















































      .start();
  }
}
