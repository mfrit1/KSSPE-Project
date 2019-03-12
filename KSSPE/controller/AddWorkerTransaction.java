// specify the package
package controller;

// system imports
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Properties;
import java.util.Vector;
import java.util.Enumeration;
import java.text.SimpleDateFormat;
import java.util.Date;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;
import exception.MultiplePrimaryKeysException;

import userinterface.View;
import userinterface.ViewFactory;
import model.Worker;
import model.Person;

/** The class containing the AddWorkerTransaction for the KSSPE application */
//==============================================================
public class AddWorkerTransaction extends Transaction
{
	private String errorMessage = "";
	private Receptionist myReceptionist;
	private Worker myWorker;
	private Person myPerson; 

	public AddWorkerTransaction() throws Exception
	{
		super();
	}

	public void processTransaction(Properties props)
	{
		try
		{
			new Worker(props);
			
			errorMessage = "ERROR: Worker already exists!";	
		}
		catch (InvalidPrimaryKeyException ex) 
		{
			
			try
			{
				props.setProperty("Status", "Active");
				props.setProperty("DateAdded", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				props.setProperty("DateLastUpdated", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				
				myWorker = new Worker(props, true);
				myWorker.save();
				
				errorMessage = "Worker Added Successfully";
			}
			catch (InvalidPrimaryKeyException ex2) 
			{
				errorMessage = ex2.getMessage();
			}
			
		}
	}
	
	//-----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("Error") == true)
		{
			return errorMessage;
		}
		else if (key.equals("TestWorker") == true)
		{
			if(myWorker != null)
			{
				return true;
			}
			return false;
		}
		else if (key.equals("FirstName") == true)
		{
			if(myPerson != null)
				return myPerson.getState("FirstName");
			return null;
		}
		else if (key.equals("LastName") == true)
		{
			if(myPerson != null)
				return myPerson.getState("LastName");
			return null;
		}
		else if (key.equals("Email") == true)
		{
			if(myPerson != null)
				return myPerson.getState("Email");
			return null;
		}
		else if (key.equals("PhoneNumber") == true)
		{
			if(myPerson != null)
				return myPerson.getState("PhoneNumber");
			return null;
		}
		else
			return null;
	}

	public void stateChangeRequest(String key, Object value)
	{
		errorMessage = "";
		
		if (key.equals("DoYourJob") == true)
		{
			myReceptionist = (Receptionist)value;
			doYourJob();
		}
		if (key.equals("WorkerData") == true)
		{
			processTransaction((Properties)value);
		}
		if(key.equals("getPersonData") == true)
		{
			try
			{
				myWorker = new Worker((Properties)value);
				
				errorMessage = "ERROR: Worker with Bannerid " + ((Properties)value).getProperty("BannerId") + " already exists!";
		
			}
			catch(Exception ex)
			{
				try
				{
					myPerson = new Person((Properties)value);
					
					errorMessage = "Person with Bannerid " + ((Properties)value).getProperty("BannerId") +  " Found!";
				}
				catch(Exception ex2)
				{
					//do nothing. Idc if nobody was found. 
				}
			}
		}
		if(key.equals("removePersonData") == true)
		{
			myPerson = null;
			myWorker = null;
		}
		if (key.equals("CancelTransaction") == true)
		{
			myReceptionist.stateChangeRequest("CancelTransaction", null);
		}
		
		setChanged();
        notifyObservers(errorMessage);
	}

	//------------------------------------------------------
	protected Scene createView()
	{
		Scene currentScene = myViews.get("AddWorkerView");

		if (currentScene == null)
		{
			View newView = ViewFactory.createView("AddWorkerView", this);
			currentScene = new Scene(newView);
			myViews.put("AddWorkerView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}
	}
}
