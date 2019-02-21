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

/** The class containing the AddArticleTypeTransaction for the Professional Clothes Closet application */
//==============================================================
public class AddWorkerTransaction extends Transaction
{
	private String errorMessage = "";
	private Receptionist myReceptionist;
	private Worker myWorker;

	public AddWorkerTransaction() throws Exception
	{
		super();
	}

	public void processTransaction(Properties props)
	{
		try
		{
			Worker newWorker = new Worker(props);
			
			if(myWorker.getState("Credential") != null) //if I can get the credential, the worker must exist already.
			{
				errorMessage = "ERROR: Worker already exists!";											
			}
			else
			{
				
				try
				{
					props.setProperty("Status", "Active");
					props.setProperty("DateAdded", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
					props.setProperty("DateLastUpdated", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
					
					newWorker = new Worker(props, true);
					newWorker.save();
					
					errorMessage = "Worker Added Successfully";
				}
				catch (InvalidPrimaryKeyException ex2) 
				{
					errorMessage = ex2.getMessage();
				}
			}
		}
		catch (InvalidPrimaryKeyException ex) 
		{
			errorMessage = ex.getMessage();
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
			if(myWorker.getState("Credential") != null)
			{
				return myWorker.getState("Credential");
			}
			return null;
		}
		else if (key.equals("FirstName") == true)
		{
			if(myWorker != null)
				return myWorker.getState("FirstName");
			return null;
		}
		else if (key.equals("LastName") == true)
		{
			if(myWorker != null)
				return myWorker.getState("LastName");
			return null;
		}
		else if (key.equals("Email") == true)
		{
			if(myWorker != null)
				return myWorker.getState("Email");
			return null;
		}
		else if (key.equals("PhoneNumber") == true)
		{
			if(myWorker != null)
				return myWorker.getState("PhoneNumber");
			return null;
		}
		else
			return null;
	}

	public void stateChangeRequest(String key, Object value)
	{
		// DEBUG System.out.println("AddArticleTypeTransaction.sCR: key: " + key);
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
		
				if(myWorker.getState("Credential") != null)
				{
					errorMessage = "ERROR: Worker already exists!";											
				}
				else if(myWorker.getState("FirstName") != null)
					errorMessage = "Person found!";
				else
					errorMessage = "";
			}
			catch(Exception ex)
			{
				errorMessage = (String)myWorker.getState("UpdateStatusMessage");
			}
		}
		if(key.equals("removePersonData") == true)
		{
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

