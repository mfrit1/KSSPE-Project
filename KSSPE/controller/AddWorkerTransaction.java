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

	public AddWorkerTransaction() throws Exception
	{
		super();
	}

	
	/*
		break up person/worker information. 
		Check if person exists or not. If not, create it. If so, modify it.
		Create a new worker with the information. 
	*/
	public void processTransaction(Properties props)
	{
		String bannerId = props.getProperty("BannerId");
		try
		{

			Worker oldWorker = new Worker(bannerId);
			errorMessage = "ERROR: Worker with ID: " + bannerId + " already exists!";
			new Event(Event.getLeafLevelClassName(this), "processTransaction",
					"Worker with Banner Id : " + bannerId + " already exists!",
					Event.ERROR);
		}
		catch (InvalidPrimaryKeyException ex) //we want to end up here since the Worker shouldn't already exist. 
		{
			props.setProperty("Status", "Active");
			
			Properties personInfo = getPersonInfo(props);
			
			Properties workerInfo = getWorkerInfo(props);
			workerInfo.setProperty("DateAdded", new SimpleDateFormat("MM-dd-yyyy").format(new Date()));
			workerInfo.setProperty("DateLastUpdated", new SimpleDateFormat("MM-dd-yyyy").format(new Date()));

			try
			{
				Person oldPerson = new Person(bannerId); //checks if person exists already. Continues if so. 
				oldPerson.insertNewInformation(personInfo);
				oldPerson.update();
				
				createNewWorker(workerInfo);
				errorMessage = "New Worker Created!";
			}
			catch (InvalidPrimaryKeyException ex2) //ends up here if person does not already exist.  
			{
				Person newPerson = new Person(personInfo);
				newPerson.createNewRecord();
			
				createNewWorker(workerInfo);
				errorMessage = "New Worker and Person Created!";
			}
			catch (MultiplePrimaryKeysException ex2) //how the heck did you get here?
			{
				errorMessage = "ERROR: Multiple People with Banner ID!";
				new Event(Event.getLeafLevelClassName(this), "processTransaction",
					"Found multiple People with id : " + bannerId + ". Reason: " + ex2.toString(),
					Event.ERROR);
			}
			catch (NullPointerException e)
			{
				errorMessage = "ERROR: No Database connection!";
			}
		}
		catch (MultiplePrimaryKeysException ex2) //how the heck did you get here?
		{
			errorMessage = "ERROR: Multiple Workers with Banner ID!";
			new Event(Event.getLeafLevelClassName(this), "processTransaction",
					"Found multiple Workers with id : " + bannerId + ". Reason: " + ex2.toString(),
					Event.ERROR);
		}
		catch (NullPointerException e)
		{
			errorMessage = "ERROR: No Database connection!";
		}
	}
	
	
	private void createNewWorker(Properties workerInfo)
	{
		Worker newWorker = new Worker(workerInfo);
		newWorker.createNewRecord();
		errorMessage = (String)newWorker.getState("UpdateStatusMessage");
	}
	
	// This separetes only the pertinate person information from a properties object. make this able to work by inputting key names and putting in utilities?.
	public Properties getPersonInfo(Properties props)
	{
		Properties personInfo = new Properties();
		
		Enumeration allKeys = props.propertyNames();
		while (allKeys.hasMoreElements() == true)
		{
			String nextKey = (String)allKeys.nextElement();
			String nextValue = props.getProperty(nextKey);
			
			if(nextKey.equals("BannerId") || nextKey.equals("FirstName") || nextKey.equals("LastName") 
				|| nextKey.equals("Email") || nextKey.equals("PhoneNumber"))
			{
				personInfo.setProperty(nextKey, nextValue);
			}
		}
		
		return personInfo;
	}
	// This separetes only the pertinate worker information from a properties object.
	public Properties getWorkerInfo(Properties props)
	{
		Properties workerInfo = new Properties();
		
		Enumeration allKeys = props.propertyNames();
		while (allKeys.hasMoreElements() == true)
		{
			String nextKey = (String)allKeys.nextElement();
			String nextValue = props.getProperty(nextKey);
			
			if(nextKey.equals("BannerId") || nextKey.equals("Credential") || nextKey.equals("Password") 
				|| nextKey.equals("Status") || nextKey.equals("DateAdded") || nextKey.equals("DateLastUpdated"))
			{
				workerInfo.setProperty(nextKey, nextValue);
			}
		}
		
		return workerInfo;
	}
	
	//-----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("Error") == true)
		{
			return errorMessage;
		}
		return null;
	}

	public void stateChangeRequest(String key, Object value)
	{
		// DEBUG System.out.println("AddArticleTypeTransaction.sCR: key: " + key);
		if (key.equals("DoYourJob") == true)
		{
			myReceptionist = (Receptionist)value;
			doYourJob();
		}
		if (key.equals("WorkerData") == true)
		{
			processTransaction((Properties)value);
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

