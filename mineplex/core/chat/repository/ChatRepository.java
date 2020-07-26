package mineplex.core.chat.repository;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mineplex.core.mysql.AccountPreferenceRepository;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatRepository
  extends AccountPreferenceRepository
{
  private static String ALTER_ACCOUNT_PREFERENCE_TABLE = "SET @s = (SELECT IF((SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = 'accountPreferences' AND table_schema = DATABASE() AND column_name = 'filterChat') > 0, 'SELECT 1', 'ALTER TABLE accountPreferences ADD filterChat BOOL')); PREPARE stmt FROM @s; EXECUTE stmt;";
  
  private static String CREATE_FILTERED_TABLE = "CREATE TABLE IF NOT EXISTS filteredWords (id INT NOT NULL AUTO_INCREMENT, word VARCHAR(256), PRIMARY KEY (id));";
  private static String RETRIEVE_FILTERED_WORDS = "SELECT word FROM filteredWords;";
  private static String SAVE_FILTER_VALUE = "REPLACE INTO accountPreferences (filterChat) VALUES (?) WHERE playerName = ?;";
  
  public ChatRepository(JavaPlugin plugin)
  {
    super(plugin);
  }
  
  protected void initialize()
  {
    super.initialize();
    
    executeQuery(CREATE_FILTERED_TABLE);
  }
  
  protected void update()
  {
    super.update();
    executeQuery(ALTER_ACCOUNT_PREFERENCE_TABLE);
  }
  
  public List<String> retrieveFilteredWords()
  {
    List<String> filteredWords = new ArrayList();
    
    Connection connection = null;
    ResultSet resultSet = null;
    PreparedStatement preparedStatement = null;
    
    try
    {
      connection = getConnection();
      
      preparedStatement = connection.prepareStatement(RETRIEVE_FILTERED_WORDS);
      resultSet = preparedStatement.executeQuery();
      
      while (resultSet.next())
      {
        filteredWords.add(resultSet.getString(1));
      }
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
      
      if (resultSet != null)
      {
        try
        {
          resultSet.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
      
      if (connection != null)
      {
        try
        {
          connection.close();
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
      
      if (resultSet != null)
      {
        try
        {
          resultSet.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
      
      if (connection != null)
      {
        try
        {
          connection.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
    }
    
    return filteredWords;
  }
  
  public void saveFilterChat(String playerName, boolean filterChat)
  {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    
    int affectedRows = 0;
    
    try
    {
      connection = getConnection();
      preparedStatement = connection.prepareStatement(SAVE_FILTER_VALUE);
      
      preparedStatement.setString(1, playerName);
      
      affectedRows = preparedStatement.executeUpdate();
      
      if (affectedRows == 0)
      {
        System.out.println("Error saving FilterChat option.");
      }
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
      
      if (connection != null)
      {
        try
        {
          connection.close();
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
      
      if (connection != null)
      {
        try
        {
          connection.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
    }
  }
  
  public void loadClientInformation(Connection connection) {}
}
