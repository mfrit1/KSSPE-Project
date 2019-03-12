
// specify the package

// system imports
import java.util.Locale;
import java.util.ResourceBundle;
import java.io.FileOutputStream;
import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Hyperlink;

// project imports
import event.Event;
import event.EventLog;
import common.PropertyFile;
import javafx.scene.image.Image;

import controller.Receptionist;
import userinterface.MainStageContainer;
import userinterface.WindowPosition;


/** The class containing the main program  for the Professional Clothes Closet application */
//==============================================================
public class KSSPE extends Application
{

	private Receptionist myReceptionist;		// the main behavior for the application

	/** Main stage of the application */
	private Stage mainStage;

	// start method for this class, the main application object
	//----------------------------------------------------------
	public void start(Stage primaryStage)
	{
	   System.out.println("Brockport KSSPE Reservation System Version 1.00");
	   System.out.println("Copyright 2019 Brockport Dream Team: Lucas Wing, Nick Barnard, Liam Allport, Matt Fritschi");

	   MainStageContainer.setStage(primaryStage, "KSSPE Version 1.00  |  Copyright: Dream Team");
	   mainStage = MainStageContainer.getInstance();
       mainStage.getIcons().add(new Image("/images/BPT_LOGO_All-In-One_Color.png")); // set small icon in top left to bport icon
	   
	   // Finish setting up the stage (ENABLE THE GUI TO BE CLOSED USING THE TOP RIGHT X
        mainStage.setOnCloseRequest(new EventHandler <javafx.stage.WindowEvent>() {
            @Override
            public void handle(javafx.stage.WindowEvent event) {
                System.exit(0);
            }
           });

       try
	   {
			myReceptionist = new Receptionist();
	   }
	   catch(Exception exc)
	   {
			System.err.println("KSSPE.KSSPE - could not create Receptionist!");
			new Event(Event.getLeafLevelClassName(this), "KSSPE.<init>", "Unable to create Receptionist object", Event.ERROR);
			exc.printStackTrace();
	   }

  	   WindowPosition.placeCenter(mainStage);

       mainStage.show();
	}


	/** 
	 * The "main" entry point for the application. Carries out actions to
	 * set up the application
	 */
	//----------------------------------------------------------
    public static void main(String[] args)
	{

		launch(args);
	}

}
