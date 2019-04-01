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

/** The class containing the Borrower for the KSSPE application */
//==============================================================
public class Borrower extends EntityBase
{
	private static final String myTableName = "Borrower";
	private boolean oldFlag = true;
	private Person myPerson;

	// GUI Components
	private String updateStatusMessage = "";

	// constructor for this class
	//----------------------------------------------------------
	public Borrower(Properties props)    //creates and stores borrower object based off a current id. DOES NOT MAKE A NEW Borrower
		throws InvalidPrimaryKeyException
	{
		super(myTableName);
			
		String idToQuery = props.getProperty("BannerId");

		String query = "SELECT * FROM " + myTableName + " WHERE (BannerId = " + idToQuery + " AND Status = 'Active')";

		Vector allDataRetrieved =  getSelectQueryResult(query);

		// You must get one Borrower at least
		if (allDataRetrieved != null && allDataRetrieved.size() != 0)
		{
			int size = allDataRetrieved.size();

			// There should be EXACTLY one Borrower. More than that is an error
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple borrowers matching user id : "
					+ idToQuery + " found.");
			}
			else
			{
				// copy all the retrieved data into persistent state
				Properties retrievedBorrowerData = (Properties)allDataRetrieved.elementAt(0);
				persistentState = new Properties();
				myPerson = new Person(props);
				persistentState.setProperty("BannerId", retrievedBorrowerData.getProperty("BannerId"));
				persistentState.setProperty("BlockStatus", retrievedBorrowerData.getProperty("BlockStatus"));
				persistentState.setProperty("Penalty", retrievedBorrowerData.getProperty("Penalty"));
				persistentState.setProperty("Notes", retrievedBorrowerData.getProperty("Notes"));
				persistentState.setProperty("Status", retrievedBorrowerData.getProperty("Status"));
				persistentState.setProperty("DateLastUpdated", retrievedBorrowerData.getProperty("DateLastUpdated"));
				persistentState.setProperty("DateAdded", retrievedBorrowerData.getProperty("DateAdded"));
				
				updateStatusMessage = "Borrower created sucessfully!";
			}
		}
		// If no Borrower found for this banner Id, throw an exception
		else
		{
			throw new InvalidPrimaryKeyException("ERROR: No borrower found for Banner Id: " + idToQuery);
		}
		
		oldFlag = true; //The person and/or borrower are both old. 
	}

	//----------------------------------------------------------
	public Borrower(Properties props, boolean check) //create a completely new worker. 
		throws InvalidPrimaryKeyException
	{
		super(myTableName);

		oldFlag = false;
		persistentState = new Properties();
		
		if (props.getProperty("BannerId") == null) throw new InvalidPrimaryKeyException("ERROR: Idiot - provide a banner Id at least");
		
		Properties personProperties = new Properties();
		personProperties.setProperty("BannerId", props.getProperty("BannerId"));
		
		try
		{
			myPerson = new Person(personProperties);
			persistentState.setProperty("BannerId", props.getProperty("BannerId"));
			persistentState.setProperty("BlockStatus", props.getProperty("BlockStatus"));
			persistentState.setProperty("Penalty", props.getProperty("Penalty"));
			persistentState.setProperty("Notes", props.getProperty("Notes"));
			persistentState.setProperty("Status", props.getProperty("Status"));
			persistentState.setProperty("DateLastUpdated", props.getProperty("DateLastUpdated"));
			persistentState.setProperty("DateAdded", props.getProperty("DateAdded"));
			oldFlag = false;
		}
		catch (InvalidPrimaryKeyException invPKexcep)
		{
			personProperties = new Properties();
			personProperties.setProperty("BannerId", props.getProperty("BannerId"));
			personProperties.setProperty("FirstName", props.getProperty("FirstName"));
			personProperties.setProperty("LastName", props.getProperty("LastName"));
			personProperties.setProperty("Email", props.getProperty("Email"));
			personProperties.setProperty("PhoneNumber", props.getProperty("PhoneNumber"));
			myPerson = new Person(personProperties, true);
			
			persistentState.setProperty("BannerId", props.getProperty("BannerId"));
			persistentState.setProperty("BlockStatus", props.getProperty("BlockStatus"));
			persistentState.setProperty("Penalty", props.getProperty("Penalty"));
			persistentState.setProperty("Notes", props.getProperty("Notes"));
			persistentState.setProperty("Status", props.getProperty("Status"));
			persistentState.setProperty("DateLastUpdated", props.getProperty("DateLastUpdated"));
			persistentState.setProperty("DateAdded", props.getProperty("DateAdded"));
			oldFlag = false;
		}
		
		
	}
	
	
	public static int compare(Borrower a, Borrower b)
	{
		String aVal = (String)a.getState("BannerId");
		String bVal = (String)b.getState("BannerId");

		return aVal.compareTo(bVal);
	}

	//----------------------------------------------------------------
	 public Object getState(String key)
    {
        String value = persistentState.getProperty(key);
        
		if (key.equals("UpdateStatusMessage") == true)
		{
			return updateStatusMessage;
		}
        else if (value != null)
		{
            return value;
		}
        else
        {
            if (myPerson != null)
                return myPerson.getState(key);
            else
                return null;
        }
    }
	
	//------------------------------------------------------------------
	
	public void save()
	{
		updateStateInDatabase();
	}
	
	//------------------------------------------------------------------
	private void updateStateInDatabase()
	{
		if (myPerson != null)
		{
			myPerson.save();
			String personSaveMessage = (String)myPerson.getState("UpdateStatusMessage");
			if ((personSaveMessage.startsWith("ERR")) || (personSaveMessage.startsWith("Err")))
			{
				updateStatusMessage = "ERROR: Could not save contained Person!";
			}
			else
			{
				try
				{
					if (oldFlag == true)
					{
						Properties whereClause = new Properties();
						whereClause.setProperty("BannerId", persistentState.getProperty("BannerId"));
						updatePersistentState(mySchema, persistentState, whereClause);
						updateStatusMessage = "Borrower: " + persistentState.getProperty("BannerId") + " updated successfully!";
					}
					else
					{
						insertPersistentState(mySchema, persistentState);
						oldFlag = true;
						updateStatusMessage = "Borrower: " + persistentState.getProperty("BannerId") + " inserted successfully!";
					}
				}
				catch (SQLException ex)
				{
					updateStatusMessage = "ERROR: Borrower could not be installed in database!";
					new Event(Event.getLeafLevelClassName(this), "updateStateInDatabase", "Borrower with BannerID : " + 
						persistentState.getProperty("BannerId") + " could not be saved in database: " + ex.toString(), Event.ERROR);
				}
			}
		}
		else
		{
			updateStatusMessage = "ERROR: Borrower record corrupted!";
			new Event(Event.getLeafLevelClassName(this), "updateStateInDatabase", "Borrower with BannerID : " + 
				persistentState.getProperty("BannerId") + " does NOT have an associated Person record", Event.ERROR);
		}
	}
	
	
	public Vector<String> getEntryListView()
	{
		Vector<String> v = new Vector<String>();
		
		v.addElement((String)this.getState("BannerId"));
		v.addElement((String)this.getState("FirstName"));
		v.addElement((String)this.getState("LastName"));
		
		return v;
	}
	
	//-------------------------------------------------------------------
	
	public void stateChangeRequest(String key, Object value)
	{
		if(key.equals("BannerId") || key.equals("BlockStatus") || key.equals("Penalty") || key.equals("Notes") || key.equals("Status") || key.equals("DateAdded") || key.equals("DateLastUpdated"))
		{
			persistentState.setProperty(key, (String)value);
		}
		else if(key.equals("FirstName") || key.equals("LastName") || key.equals("Email") || key.equals("PhoneNumber") || key.equals("BannerId"))
		{
			myPerson.stateChangeRequest(key, value);
		}
	}
	

	protected void initializeSchema(String tableName)
	{
		if (mySchema == null)
		{
			mySchema = getSchemaInfo(tableName);
		}
	}
}


