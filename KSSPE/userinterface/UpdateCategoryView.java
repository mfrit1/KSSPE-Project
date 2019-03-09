// specify the package
package userinterface;

// system imports
import javafx.scene.paint.Color;

// project imports
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import controller.Transaction;

/** The class containing the Modify Article Type View  for the Professional Clothes
 *  Closet application 
 */
//==============================================================
public class UpdateCategoryView extends AddCategoryView
{

	//

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public UpdateCategoryView(Transaction t)
	{
		super(t);
	}

	//-------------------------------------------------------------
	protected String getActionText()
	{
		return "** UPDATING CATEGORY **";
	}

	//-------------------------------------------------------------
	public void populateFields()
	{
		String bcPrefix = (String)myController.getState("BarcodePrefix");
		if (bcPrefix != null)
		{
			barcodePrefix.setText(bcPrefix);
			barcodePrefix.setDisable(true);
		}
		String nameText = (String)myController.getState("Name");
		if (nameText != null)
		{
			name.setText(nameText);
		}
		
		submitButton.setText("Update"); //fix submitbutton
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


