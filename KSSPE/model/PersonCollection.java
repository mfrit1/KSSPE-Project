// specify the package
package model;

// system imports
import utilities.GlobalVariables;
import java.util.Properties;
import java.util.Vector;
import javafx.scene.Scene;

// project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;

import userinterface.View;
import userinterface.ViewFactory;


/** The class containing the PersonCollection for the KSSPE
 *  application 
 */
//==============================================================
public class PersonCollection extends EntityBase
{
	private static final String myTableName = "Person";

	private Vector<Person> people;

	// constructor for this class
	//----------------------------------------------------------
	public PersonCollection() 
	{
		super(myTableName);
	}

	//-----------------------------------------------------------
	private void populateCollectionHelper(String query)
	{

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{
			people = new Vector<Person>();

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextPersonData = allDataRetrieved.elementAt(cnt);
				
				try
				{
					Person p = new Person(nextPersonData);
					
					addPerson(p);
				}
				catch(Exception e)
				{
					new Event(Event.getLeafLevelClassName(this), "populateCollectionHelper",
					"Error in creating a person from people collection", Event.ERROR);
				}
			}

		}
	}

	//-----------------------------------------------------------
	public void findByBannerId(String bannerId)
	{
		String query = "SELECT * FROM " + myTableName + " WHERE ((Status = 'Active') AND (BannerId = '" + bannerId + "'))";
		populateCollectionHelper(query);
	}

	//-----------------------------------------------------------
	public void findAll()
	{
		String query = "SELECT * FROM " + myTableName + " WHERE (Status = 'Active')";
		populateCollectionHelper(query);
	}

	//-----------------------------------------------------------
	public void findByName(String firstName, String lastName)
	{
		String query = "SELECT * FROM " + myTableName + " WHERE ((Status = 'Active') AND (FirstName LIKE '%" + firstName + "%')" 
		+ "AND (LastName LIKE '%" + lastName + "%'))";
		populateCollectionHelper(query);
	}


	//----------------------------------------------------------------------------------
	private void addPerson(Person p)
	{
		int index = findIndexToAdd(p);
		people.insertElementAt(p, index); // To build up a collection sorted on some key
	}

	//----------------------------------------------------------------------------------
	private int findIndexToAdd(Person p)
	{
		
		int low=0;
		int high = people.size()-1;
		int middle;

		while (low <=high)
		{
			middle = (low+high)/2;

			Person midSession = people.elementAt(middle);

			int result = Person.compare(p, midSession);

			if (result == 0)
			{
				return middle;
			}
			else if (result<0)
			{
				high=middle-1;
			}
			else
			{
				low=middle+1;
			}
		}
		return low;
	}


	/**
	 *
	 */
	//----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("People"))
			return people;
		else
			if (key.equals("PersonList"))
				return this;
		return null;
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		
	}

	//----------------------------------------------------------
	public Person retrieve(String bannerId)
	{
		Person retValue = null;
		for (int cnt = 0; cnt < people.size(); cnt++)
		{
			Person nextP = people.elementAt(cnt);
			String nextBannerId = (String)nextP.getState("BannerId");
			if (nextBannerId.equals(bannerId) == true)
			{
				retValue = nextP;
				return retValue;
			}
		}

		return retValue;
	}


	//----------------------------------------------------------
	public void updateState(String key, Object value)
	{
		stateChangeRequest(key, value);
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
