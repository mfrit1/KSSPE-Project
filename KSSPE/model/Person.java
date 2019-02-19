// specify the package
package model;

// system imports
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;

// project imports
import exception.InvalidPrimaryKeyException;
import exception.PasswordMismatchException;
import exception.MultiplePrimaryKeysException;
import database.*;

/** The class containing the Person for the KSSPE application */
//==============================================================
public class Person extends EntityBase
{
	private static final String myTableName = "person";
	
	private String updateStatusMessage = "";

	//--------------------------------------------------------- this is used to create a new person. Check if it exists or not first. 
	public Person(Properties props)
	{
		super(myTableName);

		insertNewInformation(props);

	}
	
	
	public void insertNewInformation(Properties props)
	{
		persistentState = new Properties();
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

	//---------------------------------------------------------- This is used to check if person exists or not. If so, get it. 
	public Person(String idToQuery) throws InvalidPrimaryKeyException, MultiplePrimaryKeysException
	{
		super(myTableName);

		String query = "SELECT * FROM " + myTableName + " WHERE (BannerId = " + idToQuery + ")";

		Vector allDataRetrieved =  getSelectQueryResult(query);

		// You must get one account at least
		if (allDataRetrieved != null)
		{
			int size = allDataRetrieved.size();

			// There should be EXACTLY one Person. More than that is an error
			if (size == 0)
			{
				throw new InvalidPrimaryKeyException("No Person matching : "
						+ idToQuery + " found.");
			}
			else
			{
				// There should be EXACTLY one Person. More than that is an error
				if (size != 1)
				{
					throw new MultiplePrimaryKeysException("Multiple Persons matching Banner Id : " + idToQuery + " found.");
				}
				else
				{
					// copy all the retrieved data into persistent state
					Properties retrievedCustomerData = (Properties)allDataRetrieved.elementAt(0);
					persistentState = new Properties();

					Enumeration allKeys = retrievedCustomerData.propertyNames();
					while (allKeys.hasMoreElements() == true)
					{
						String nextKey = (String)allKeys.nextElement();
						String nextValue = retrievedCustomerData.getProperty(nextKey);

						if (nextValue != null)
						{
							persistentState.setProperty(nextKey, nextValue);
						}
					}

				}
			}
		}
		// If no Worker found for this user name, throw an exception
		else
		{
			throw new InvalidPrimaryKeyException("No Person matching banner id : "
				+ idToQuery + " found.");
		}
	}
	
	//----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("UpdateStatusMessage") == true)
			return updateStatusMessage;
		else
			return persistentState.getProperty(key);
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		persistentState.setProperty(key, (String)value);

		//myRegistry.updateSubscribers(key, this);
	}
	
	public void update()
	{
		updateStateInDatabase();
	}
	
	public void createNewRecord()
	{
		try
		{
			insertPersistentState(mySchema, persistentState);
		}
		catch (SQLException ex)
		{
			updateStatusMessage = "Error in installing Person data in database!";
		}
		
	}
	
	//-----------------------------------------------------------------------------------
	private void updateStateInDatabase() 
	{
		try
		{
			if (persistentState.getProperty("BannerId") != null)
			{
				Properties whereClause = new Properties();
				whereClause.setProperty("BannerId", persistentState.getProperty("BannerId"));
				updatePersistentState(mySchema, persistentState, whereClause);
				updateStatusMessage = "Worker with banner Id : " + persistentState.getProperty("BannerId") + " updated successfully!";
			}
			else //This looks wrong for this setting. workers shouldn't auto-increment at all. Fix later?
			{
				Integer atID =
						insertAutoIncrementalPersistentState(mySchema, persistentState);
				persistentState.setProperty("ID", "" + atID.intValue());
				updateStatusMessage = "Worker with banner Id " +  persistentState.getProperty("BannerId")
				+ " installed successfully!";
			}
		}
		catch (SQLException ex)
		{
			updateStatusMessage = "Error in installing Person data in database!";
		}
		//DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
	}

	//-----------------------------------------------------------------------------------
	protected void initializeSchema(String tableName)
	{
		if (mySchema == null)
		{
			mySchema = getSchemaInfo(tableName);
		}
	}
}


