// specify the package
package userinterface;

// system imports
import javafx.scene.paint.Color;

// project imports
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import controller.Transaction;

/** The class containing the Update Worker View for the KSSPE
* Inventory Management project
 */
//==============================================================
public class UpdateWorkerView extends AddWorkerView
{

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public UpdateWorkerView(Transaction t)
	{
		super(t);
	}

	//-------------------------------------------------------------
	protected String getActionText()
	{
		return "** UPDATING WORKER **";
	}

	//-------------------------------------------------------------
	public void populateFields()
	{
		//DEBUG: I need about a gallon of coffee right now
		System.out.println("Populating UpdateWorkerView:37");
		String banId = (String)myController.getState("BannerId");
		System.out.println("Grabbed ID");
		if (banId != null)
		{
		 	bannerId.setText(banId);
		 	bannerId.setDisable(true);
		 	removeDisables();
		}
		String fName = (String)myController.getState("FirstName");
		if (fName != null)
		{
		 	firstName.setText(fName);
		}
		String lName = (String)myController.getState("LastName");
		if (lName != null)
		{
		 	lastName.setText(lName);
		}
		String mail = (String)myController.getState("Email");
		if (mail != null)
		{
		 	email.setText(mail);
		}
		String phoneNum = (String)myController.getState("PhoneNumber");
		if (phoneNum != null)
		{
		 	phoneNumber.setText(phoneNum);
		}


		submitButton.setText("Update"); 
		ImageView icon = new ImageView(new Image("/images/savecolor.png"));
		icon.setFitHeight(15);
		icon.setFitWidth(15);
		submitButton.setGraphic(icon);
	}

	public void clearValues(){

	}
}

//---------------------------------------------------------------
//	Revision History:
//


