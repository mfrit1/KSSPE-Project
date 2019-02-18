// specify the package
package controller;

// system imports
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Properties;
import java.util.Vector;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;
import exception.MultiplePrimaryKeysException;

import userinterface.View;
import userinterface.ViewFactory;
import model.Worker;

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

	public void processTransaction(Properties props)
	{
		String bannerId = props.getProperty("BannerId");
		try
		{

			Worker oldWorker = new Worker(bannerId);
			errorMessage = "ERROR: Banner ID " + bannerId + " already exists!";
			new Event(Event.getLeafLevelClassName(this), "processTransaction",
					"Worker with Banner Id : " + bannerId + " already exists!",
					Event.ERROR);
		}
		catch (InvalidPrimaryKeyException ex) //we want to end up here since the Worker shouldn't already exist. 
		{

			//props.setProperty("Status", "Active");
			
			/*
				break up person/worker information. 
				Check if person exists or not. If not, create it. If so, modify it.
				Create a new worker with the information. 
			*/
			Properties personInfo = getPersonInfo();
			Properties workerInfo = getWorkerInfo();
			
			
			Worker newWorker = new Worker(workerInfo);
			newWorker.createNewRecord();
			errorMessage = (String)newWorker.getState("UpdateStatusMessage");

		}
		catch (MultiplePrimaryKeysException ex2) //how the heck did you get here?
		{
			errorMessage = "ERROR: Multiple Workers with Banner ID!";
			new Event(Event.getLeafLevelClassName(this), "processTransaction",
					"Found multiple banner Ids with id : " + bannerId + ". Reason: " + ex2.toString(),
					Event.ERROR);

		}
		catch (NullPointerException e)
		{
			errorMessage = "ERROR: No Database connection!";
		}
	}
	
	
	private Properties getPersonInfo()
	{
		
		return null;
	}
	private Properties getWorkerInfo()
	{
		
		return null;
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

