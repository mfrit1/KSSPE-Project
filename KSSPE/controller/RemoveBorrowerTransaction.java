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
import model.Borrower;
import model.BorrowerCollection;

/** The class containing the ModifyBorrowerTransaction for the KSSPE application */
//==============================================================
public class RemoveBorrowerTransaction extends Transaction
{
	private String errorMessage = "";
	private Receptionist myReceptionist;
	private Borrower myBorrower;
	private BorrowerCollection myBorrowerList;

	//----------------------------------------------------------------
	public RemoveBorrowerTransaction() throws Exception
	{
		super();
	}

	//----------------------------------------------------------------
	public void processTransaction(Properties props)
	{
		myBorrowerList = new BorrowerCollection();
		
		if (props.getProperty("BannerId") != null)
		{
			String bannerId = props.getProperty("BannerId");
			myBorrowerList.findByBannerId(bannerId);
		}
		else if(props.getProperty("FirstName") != null || props.getProperty("LastName") != null)
		{
			if(props.getProperty("FirstName") != null && props.getProperty("LastName") != null)
			{
				myBorrowerList.findByFirstAndLast(props);
			}
			else if(props.getProperty("FirstName") != null)
			{
				String name = props.getProperty("FirstName");
				myBorrowerList.findByFirstName(name);
			}
			else
			{
				String name = props.getProperty("LastName");
				myBorrowerList.findByLastName(name);
			}
		}
		else
		{
			myBorrowerList.findAll();
		}
		
		try
		{	
			Scene newScene = createBorrowerCollectionView();	
			swapToView(newScene);
		}
		catch (Exception ex)
		{
			new Event(Event.getLeafLevelClassName(this), "processTransaction",
					"Error in creating BorrowerCollectionView", Event.ERROR);
		}
	}
	
	//-----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("Error") == true)
		{
			return errorMessage;
		}
		else if (key.equals("Alert") == true)
		{
			return true;
		}
		else if (key.equals("BorrowerList") == true)
		{
			return myBorrowerList;
		}
		else if (key.equals("BannerId") == true)
		{
			return myBorrower.getState("BannerId");
		}
		else if (key.equals("FirstName") == true)
		{
			return myBorrower.getState("FirstName");
		}
		else if (key.equals("LastName") == true)
		{
			return myBorrower.getState("LastName");
		}
		else if (key.equals("Email") == true)
		{
			return myBorrower.getState("Email");
		}
		else if (key.equals("PhoneNumber") == true)
		{
			return myBorrower.getState("PhoneNumber");
		}
		else if (key.equals("Penalty") == true)
		{
			return myBorrower.getState("Penalty");
		}
		else if (key.equals("BlockStatus") == true)
		{
			return myBorrower.getState("BlockStatus");
		}
		else if (key.equals("Notes") == true)
		{
			return myBorrower.getState("Notes");
		}
		else
			return null;
	}

	//-------------------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		errorMessage = "";
		
		if (key.equals("DoYourJob") == true)
		{
			myReceptionist = (Receptionist)value;
			doYourJob();
		}
		if (key.equals("SearchBorrower") == true)
		{
			processTransaction((Properties)value);
		}
		if (key.equals("BorrowerSelected") == true)
		{
			myBorrower = myBorrowerList.retrieve((String)value);
			
			removeBorrowerHelper();
		}
		if (key.equals("CancelBorrowerList") == true)
		{
			Scene oldScene = createView();	
			swapToView(oldScene);
		}
		if (key.equals("CancelTransaction") == true)
		{
			myReceptionist.stateChangeRequest("CancelTransaction", null);
		}
		
		setChanged();
        notifyObservers(errorMessage);
	}
	
	//------------------------------------------------------------------------
	private void removeBorrowerHelper()
	{
		myBorrower.stateChangeRequest("Status", "Inactive");
		myBorrower.stateChangeRequest("DateLastUpdated", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		myBorrower.save();
		
		errorMessage = (String)myBorrower.getState("UpdateStatusMessage");
		
		if(errorMessage.startsWith("ERR") == false)
		{
			if (myBorrowerList != null)
			{
				myBorrowerList.remove((String)myBorrower.getState("BannerId"));
			}
			errorMessage = errorMessage.replace("updated", "removed");
		}
	}
	
	//--------------------------------------------------------------------------
	protected Scene createBorrowerCollectionView()
	{
		Scene currentScene;

		View newView = ViewFactory.createView("BorrowerCollectionView", this);
		currentScene = new Scene(newView);

		return currentScene;
	}

	//------------------------------------------------------
	protected Scene createView()
	{
		Scene currentScene = myViews.get("SearchBorrowerView");

		if (currentScene == null)
		{
			View newView = ViewFactory.createView("SearchBorrowerView", this);
			currentScene = new Scene(newView);
			myViews.put("SearchBorrowerView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}
	}
}

