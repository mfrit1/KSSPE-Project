// specify the package
package userinterface;

// system imports
import utilities.GlobalVariables;
import utilities.Utilities;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import javafx.scene.control.PasswordField;

import java.util.Properties;
import java.util.Observer;
import java.util.Observable;

// project imports
import java.util.Enumeration;
import java.util.Vector;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import controller.Transaction;

/** The class containing the SearchWorkerView for the KSSPE Inventory Mgmt application
 */
//==============================================================
public class SearchWorkerView extends View implements Observer
{

	// GUI components
	protected TextField bannerId;
	protected Text actionText;
	protected Text prompt;

	protected HBox bannerBox;
	protected HBox doneCont;
	protected Button submitButton;
	protected Button cancelButton;

	protected MessageView statusLog;

	// constructor for this class -- takes a controller object
	//----------------------------------------------------------
	public SearchWorkerView(Transaction t)
	{
		super(t);

		VBox container = new VBox(10);
		container.setStyle("-fx-background-color: slategrey");
		container.setPadding(new Insets(15, 5, 5, 5));

		container.getChildren().add(createTitle());

		container.getChildren().add(createFormContent());

		container.getChildren().add(createStatusLog("             "));

		getChildren().add(container);

		myController.addObserver(this);
	}

	//-------------------------------------------------------------
	protected String getActionText()
	{
		return "** ENTER BANNER ID **";
	}

	// Create the title container
	//-------------------------------------------------------------
	private Node createTitle()
	{
		VBox container = new VBox(10);
		container.setPadding(new Insets(1, 10, 1, 10));
		
        Text clientText = new Text("KSSPE DEPARTMENT");
			clientText.setFont(Font.font("Copperplate", FontWeight.EXTRA_BOLD, 36));
			clientText.setEffect(new DropShadow());
			clientText.setTextAlignment(TextAlignment.CENTER);
			clientText.setFill(Color.WHITESMOKE);
		container.getChildren().add(clientText);

		Text titleText = new Text(" Reservation Management System ");
			titleText.setFont(Font.font("Copperplate", FontWeight.THIN, 28));
			titleText.setTextAlignment(TextAlignment.CENTER);
			titleText.setFill(Color.GOLD);
		container.getChildren().add(titleText);

		Text blankText = new Text("  ");
			blankText.setFont(Font.font("Arial", FontWeight.BOLD, 15));
			blankText.setWrappingWidth(350);
			blankText.setTextAlignment(TextAlignment.CENTER);
			blankText.setFill(Color.WHITE);
		container.getChildren().add(blankText);

		actionText = new Text("     " + getActionText() + "       ");
			actionText.setFont(Font.font("Copperplate", FontWeight.BOLD, 22));
			actionText.setWrappingWidth(450);
			actionText.setTextAlignment(TextAlignment.CENTER);
			actionText.setFill(Color.DARKGREEN);
		container.getChildren().add(actionText);
		
		container.setAlignment(Pos.CENTER);

		return container;
	}

	// Create the main form content
	//-------------------------------------------------------------
	private VBox createFormContent()
	{
		VBox vbox = new VBox(10);
		vbox.setAlignment(Pos.CENTER);
		
		Font myFont = Font.font("Copperplate", FontWeight.THIN, 16);
		Font bannerFont = Font.font("copperplate", FontWeight.BOLD, 18);   

		Text blankText = new Text("  ");
			blankText.setFont(Font.font("Arial", FontWeight.BOLD, 17));
			blankText.setWrappingWidth(350);
			blankText.setTextAlignment(TextAlignment.CENTER);
			blankText.setFill(Color.WHITE);
		vbox.getChildren().add(blankText);

		
		bannerBox = new HBox(10);
		bannerBox.setAlignment(Pos.CENTER);
		bannerBox.setPadding(new Insets(0, 20, 10, 0));
		
		Text bannerIdLabel = new Text("Banner ID :");
			bannerIdLabel.setFill(Color.GOLD);
			bannerIdLabel.setFont(bannerFont);
			bannerIdLabel.setUnderline(true);
			bannerIdLabel.setTextAlignment(TextAlignment.RIGHT);
		bannerBox.getChildren().add(bannerIdLabel);

		bannerId = new TextField();
			bannerId.setMinWidth(150);
			bannerId.addEventFilter(KeyEvent.KEY_RELEASED, event->{
				clearErrorMessage();
				
				if(Utilities.checkBannerId(bannerId.getText()))
				{
					System.out.println("Yup");
				}
				else
				{

				}
			});
		bannerBox.getChildren().add(bannerId);
		
		doneCont = new HBox(10);
		doneCont.setAlignment(Pos.CENTER);
            doneCont.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            doneCont.setStyle("-fx-background-color: GOLD");
		});
        doneCont.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            doneCont.setStyle("-fx-background-color: SLATEGREY");
		});
		
		ImageView icon = new ImageView(new Image("/images/search.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		submitButton = new Button("Search", icon);
			submitButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			submitButton.setOnAction((ActionEvent e) -> {
				sendToController();
			});
			submitButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				submitButton.setEffect(new DropShadow());
			});
			submitButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				submitButton.setEffect(null);
			});
		doneCont.getChildren().add(submitButton);
		
		icon = new ImageView(new Image("/images/return.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		cancelButton = new Button("Return", icon);
			cancelButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			cancelButton.setOnAction((ActionEvent e) -> {
				clearErrorMessage();
				myController.stateChangeRequest("CancelTransaction", null);
			});
			cancelButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				cancelButton.setEffect(new DropShadow());
			});
			cancelButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				cancelButton.setEffect(null);
			});
		doneCont.getChildren().add(cancelButton);
		
		vbox.getChildren().add(bannerBox);
		vbox.getChildren().add(doneCont);
	    
		return vbox;
	}

	private void sendToController()
	{
		clearErrorMessage();
		
		String BannerID = bannerId.getText();
		
		if(Utilities.checkBannerId(BannerID)) 
		{
			
			Properties props = new Properties();
			props.setProperty("BannerId", BannerID);		
			myController.stateChangeRequest("WorkerData", props);
									
		}
		else
		{
			displayErrorMessage("Please enter a valid Banner Id.");
			bannerId.requestFocus();
		}
		
	}
	
	
	//-------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);
		return statusLog;
	}

	public void clearValues()
	{
		bannerId.clear();
	}

	private void setOutlines()
	{
		bannerId.setStyle("-fx-border-color: transparent; -fx-focus-color: green;");
	}

	/**
	 * Update method
	 */
	//---------------------------------------------------------
	
	public void update(Observable o, Object value)
	{
		clearErrorMessage();

		String val = (String)value;
		if (val.startsWith("ERR") == true)
		{
			displayErrorMessage(val);
		}
		else
		{
			clearValues();
			displayMessage(val);
		}
		
	}

	/**
	 * Display error message
	 */
	//----------------------------------------------------------
	public void displayErrorMessage(String message)
	{
		statusLog.displayErrorMessage(message);
	}

	/**
	 * Display info message
	 */
	//----------------------------------------------------------
	public void displayMessage(String message)
	{
		statusLog.displayMessage(message);
	}

	/**
	 * Clear error message
	 */
	//----------------------------------------------------------
	public void clearErrorMessage()
	{
		statusLog.clearErrorMessage();
	}

}

//---------------------------------------------------------------
//	Revision History:
//
