// specify the package
package controller;

// system imports
import java.util.Hashtable;
import java.util.Properties;

import javafx.stage.Stage;
import javafx.scene.Scene;

// project imports
import impresario.IModel;
import impresario.ISlideShow;
import impresario.IView;
import impresario.ModelRegistry;

import exception.InvalidPrimaryKeyException;
import exception.PasswordMismatchException;
import event.Event;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

import model.AccountHolder;
/** The class containing the Receptionist  for the ATM application */
//==============================================================
public class Receptionist implements IView, IModel
// This class implements all these interfaces (and does NOT extend 'EntityBase')
// because it does NOT play the role of accessing the back-end database tables.
// It only plays a front-end role. 'EntityBase' objects play both roles.
{
	// For Impresario
	private Properties dependencies;
	private ModelRegistry myRegistry;
	
	//account
	private AccountHolder myAccountHolder;
	
	// GUI Components
	private Hashtable<String, Scene> myViews;
	private Stage myStage;

	private String loginErrorMessage = "";
	private String transactionErrorMessage = "";

	// constructor for this class
	//----------------------------------------------------------
	public Receptionist()
	{
		myStage = MainStageContainer.getInstance();
		myViews = new Hashtable<String, Scene>();

		// STEP 3.1: Create the Registry object - if you inherit from
		// EntityBase, this is done for you. Otherwise, you do it yourself
		myRegistry = new ModelRegistry("Receptionist");
		if(myRegistry == null)
		{
			new Event(Event.getLeafLevelClassName(this), "Receptionist",
					"Could not instantiate Registry", Event.ERROR);
		}

		// STEP 3.2: Be sure to set the dependencies correctly
		setDependencies();

		// Set up the initial view
		createAndShowLoginView();
	}

	//-----------------------------------------------------------------------------------
	private void setDependencies()
	{
		dependencies = new Properties();
		dependencies.setProperty("Login", "LoginError");
		//dependencies.setProperty("Deposit", "TransactionError");
		//dependencies.setProperty("Withdraw", "TransactionError");
		//dependencies.setProperty("Transfer", "TransactionError");
		//dependencies.setProperty("BalanceInquiry", "TransactionError");
		//dependencies.setProperty("ImposeServiceCharge", "TransactionError");


		myRegistry.setDependencies(dependencies);
	}

	/**
	 * Method called from client to get the value of a particular field
	 * held by the objects encapsulated by this object.
	 *
	 * @param	key	Name of database column (field) for which the client wants the value
	 *
	 * @return	Value associated with the field
	 */
	//----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("LoginError") == true)
		{
			return loginErrorMessage;
		}
		else
		if (key.equals("TransactionError") == true)
		{
			return transactionErrorMessage;
		}
		else
		if (key.equals("Name") == true)
		{
			if (myAccountHolder != null)
			{
				return myAccountHolder.getState("Name");
			}
			else
				return "Undefined";
		}
		else
			return "";
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		// STEP 4: Write the sCR method component for the key you
		// just set up dependencies for
		// DEBUG System.out.println("Receptionist.sCR: key = " + key);

		if (key.equals("Login") == true)
		{
			if (value != null)
			{
				loginErrorMessage = "";

				boolean flag = loginAccountHolder((Properties)value);
				if (flag == true)
				{
					createAndShowReceptionistView();
				}
			}
		}
		else
		if (key.equals("CancelTransaction") == true)
		{
			createAndShowReceptionistView();
		}
		else
		if (key.equals("ExitSystem") == true)
		{
			System.exit(0);
		}	
		else
		if ((key.equals("AddArticleType") == true) || (key.equals("UpdateArticleType") == true) ||
			(key.equals("RemoveArticleType") == true) || (key.equals("AddColor") == true) ||
			(key.equals("UpdateColor") == true) || (key.equals("RemoveColor") == true) ||
			(key.equals("AddClothingItem") == true) || (key.equals("UpdateClothingItem") == true) ||
			(key.equals("RemoveClothingItem") == true) || (key.equals("CheckoutClothingItem") == true) ||
			(key.equals("AddRequest") == true) || (key.equals("FulfillRequest") == true) ||
			(key.equals("RemoveRequest") == true) || (key.equals("ListAvailableInventory") == true) ||
            (key.equals("UntillDateReport") == true) || (key.equals("TopDonatorReport") == true))
			{
				String transType = key;

				transType = transType.trim();
					
				if (myAccountHolder != null)
				{
					doTransaction(transType);
				}
				else
				{
					transactionErrorMessage = "Transaction impossible: Customer not identified";
				}
			}
		else
		if (key.equals("Logout") == true)
		{
			myAccountHolder = null;
			myViews.remove("ReceptionistView");

			createAndShowLoginView();
		}

		myRegistry.updateSubscribers(key, this);
	}

	/** Called via the IView relationship */
	//----------------------------------------------------------
	public void updateState(String key, Object value)
	{
		// DEBUG System.out.println("Receptionist.updateState: key: " + key);

		stateChangeRequest(key, value);
	}
	

	
	// Login AccountHolder corresponding to user name and password.
	//----------------------------------------------------------
	public boolean loginAccountHolder(Properties props)
	{

		try
		{
			myAccountHolder = new AccountHolder(props);
			// DEBUG System.out.println("Account Holder: " + myAccountHolder.getState("Name") + " successfully logged in");
			return true;
		}
		catch (InvalidPrimaryKeyException ex)
		{
				loginErrorMessage = "ERROR: " + ex.getMessage();
				return false;
		}
		catch (PasswordMismatchException exec)
		{

				loginErrorMessage = "ERROR: " + exec.getMessage();
				return false;
		}
		catch (NullPointerException ex)
		{
				loginErrorMessage = "ERROR: " + "Not Connected To Database";
				return false;
			
		}

	}
        
      
	/**
	 * Create a Transaction depending on the Transaction type (deposit,
	 * withdraw, transfer, etc.). Use the AccountHolder holder data to do the
	 * create.
	 */
	//----------------------------------------------------------
	public void doTransaction(String transactionType)
	{
		try
		{
			Transaction trans = TransactionFactory.createTransaction(
					transactionType);

			trans.subscribe("CancelTransaction", this);
			trans.stateChangeRequest("DoYourJob", "");
		}
		catch (Exception ex)
		{
			transactionErrorMessage = "FATAL ERROR: TRANSACTION FAILURE: Unrecognized transaction!!";
			new Event(Event.getLeafLevelClassName(this), "createTransaction",
					"Transaction Creation Failure: Unrecognized transaction " + ex.toString(),
					Event.ERROR);
		} 
	}


	//------------------------------------------------------------
	private void createAndShowReceptionistView()
	{
		Scene currentScene = (Scene)myViews.get("ReceptionistView");
		
		// create our initial view
		if (currentScene == null){
			
			View newView = ViewFactory.createView("ReceptionistView", this); // USE VIEW FACTORY
			currentScene = new Scene(newView);
			myViews.put("ReceptionistView", currentScene);
			
		}

        swapToView(currentScene);

	}
	
	private void createAndShowLoginView()
	{
		Scene currentScene = (Scene)myViews.get("LoginView");

		if (currentScene == null)
		{
			// create our initial view
			View newView = ViewFactory.createView("LoginView", this); // USE VIEW FACTORY
			currentScene = new Scene(newView);
			myViews.put("LoginView", currentScene);
		}
				
		swapToView(currentScene);
		
	}


	/** Register objects to receive state updates. */
	//----------------------------------------------------------
	public void subscribe(String key, IView subscriber)
	{
		myRegistry.subscribe(key, subscriber);
	}

	/** Unregister previously registered objects. */
	//----------------------------------------------------------
	public void unSubscribe(String key, IView subscriber)
	{
		myRegistry.unSubscribe(key, subscriber);
	}

	//-----------------------------------------------------------------------------
	public void swapToView(Scene newScene)
	{		
		if (newScene == null)
		{
			System.out.println("Receptionist.swapToView(): Missing view for display");
			new Event(Event.getLeafLevelClassName(this), "swapToView",
					"Missing view for display ", Event.ERROR);
			return;
		}

		myStage.setScene(newScene);
		myStage.sizeToScene();


		//Place in center
		WindowPosition.placeCenter(myStage);
	}

}

