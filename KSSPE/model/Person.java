// specify the package
package model;

// system imports
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;
import event.Event;

// project imports
import exception.InvalidPrimaryKeyException;
import exception.PasswordMismatchException;
import database.*;

/** The class containing the Person for the KSSPE application */
//==============================================================
public class Person extends EntityBase
{
	private static final String myTableName = "Person";
	private boolean oldFlag = true;
	
	// GUI Components
	private String updateStatusMessage = "";

	// constructor for this class
	//----------------------------------------------------------
	public Person(Properties props) throws InvalidPrimaryKeyException
	{
		super(myTableName);

		String idToQuery = props.getProperty("BannerId");

		String query = "SELECT * FROM " + myTableName + " WHERE (BannerId = " + idToQuery + " )";

		Vector allDataRetrieved =  getSelectQueryResult(query);

		// You must get one account at least
		if (allDataRetrieved != null && allDataRetrieved.size() != 0)
		{
			int size = allDataRetrieved.size();

			// There should be EXACTLY one account. More than that is an error
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple persons matching banner id : "
					+ idToQuery + " found.");
			}
			else
			{
				// copy all the retrieved data into persistent state
				Properties retrievedPersonData = (Properties)allDataRetrieved.elementAt(0);
				persistentState = new Properties();
				
				Enumeration allKeys = retrievedPersonData.propertyNames();
				while (allKeys.hasMoreElements() == true)
				{
					String nextKey = (String)allKeys.nextElement();
					String nextValue = retrievedPersonData.getProperty(nextKey);
					
					if (nextValue != null)
					{
						persistentState.setProperty(nextKey, nextValue);
					}
				}
				
				oldFlag = true;
			}
		}
		// If no Person found for this user name, throw an exception
		else
		{
			throw new InvalidPrimaryKeyException("No Person found for Banner Id: " + idToQuery);
		}

		

	}

	//----------------------------------------------------------
	public Person(Properties props, boolean check) throws InvalidPrimaryKeyException
	{
		super(myTableName);

		oldFlag = false;
		persistentState = new Properties();
		
		if (props.getProperty("BannerId") == null) throw new InvalidPrimaryKeyException("ERROR: Idiot - provide a banner Id at least");
		
		Enumeration allKeys = props.propertyNames();
		while (allKeys.hasMoreElements() == true)
		{
			String nextKey = (String)allKeys.nextElement();
			String nextValue = props.getProperty(nextKey);
			
			if (nextValue != null)
			{
				persistentState.setProperty(nextKey, nextValue);
			}
		}
		
		
	}

	//----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("UpdateStatusMessage") == true)
			return updateStatusMessage;
		
		return persistentState.getProperty(key);
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		persistentState.setProperty(key, (String)value);

	}
	
	//------------------------------------------------------------------
	public void save()
	{
		updateStateInDatabase();
	}
	
	//------------------------------------------------------------------
	private void updateStateInDatabase()
	{
		try
		{
			if (oldFlag == true)
			{
				Properties whereClause = new Properties();
				whereClause.setProperty("BannerId", persistentState.getProperty("BannerId"));
				updatePersistentState(mySchema, persistentState, whereClause);
				updateStatusMessage = "Person: " + persistentState.getProperty("BannerId") + " updated successfully!";
			}
			else
			{
				insertPersistentState(mySchema, persistentState);
				oldFlag = true;
				updateStatusMessage = "Person: " + persistentState.getProperty("BannerId") + " inserted successfully!";
			}
		}
		catch (SQLException ex)
		{
			updateStatusMessage = "ERROR: Person could not be installed in database!";
			new Event(Event.getLeafLevelClassName(this), "updateStateInDatabase", "Person with BannerID : " + 
				persistentState.getProperty("BannerId") + " could not be saved in database: " + ex.toString(), Event.ERROR);
		}
			
	}
	
	//------------------------------------------------------------------
	protected void initializeSchema(String tableName)
	{
		if (mySchema == null)
		{
			mySchema = getSchemaInfo(tableName);
		}
	}
}


