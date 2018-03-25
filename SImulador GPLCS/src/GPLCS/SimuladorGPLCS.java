package GPLCS;


import java.util.Locale;

import com.jfoenix.controls.JFXButton;

import CableSynthesis.CableSynthesisController;
import KHM1.KHM1Screen;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * @author moyses
 */

public class SimuladorGPLCS extends Application {
   
	public static ScrollPane createMainScene(Stage primaryStage) {
		Locale.setDefault(new Locale("en", "US"));
		        
        /*CREATE THE GRID*/
        GridPane grid = new GridPane();
        grid.setVgap(50);
        grid.setHgap(10);
        grid.setPadding(new Insets(50,50,50,50));
        for (int i = 0; i < 3; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(33.3);
            grid.getColumnConstraints().add(column);
        }
        
        /*CREATE THE APPLICATION LABEL*/
        String ApplicationName = "G. Fast Physical Layer and Cable Simulator";
        Label label = new Label(ApplicationName);
        label.setId("ApplicationName");
        label.setAlignment(Pos.CENTER);

        /*CREATE BUTTONS FOR ALL FUNCTIONS OF SIMULATOR*/
        JFXButton button1 = new JFXButton("Cable Synthesis");
        button1.setId("CS");
        JFXButton button2 = new JFXButton("Cable Analysis");
        button2.setId("CA");
        JFXButton button3 = new JFXButton("Network Simulation");
        button3.setId("NS");
        button1.setFocusTraversable(false);
        button2.setFocusTraversable(false);
        button3.setFocusTraversable(false);
        button1.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	String css = CableSynthesisController.class.getResource("CableSynthesisScreen.css").toExternalForm(); 
            	primaryStage.getScene().setRoot(CableSynthesisController.getCableSynthesisScene(primaryStage));
            	primaryStage.getScene().getStylesheets().add(css);
            }
        });
        
        /*CREATE FOOTER*/
        String ApplicationFooter = "UFPA 2018 - All Rights Reserved";
        Label labelFooter = new Label(ApplicationFooter);
        labelFooter.setId("ApplicationFooter");
        labelFooter.setAlignment(Pos.CENTER);
        
        /*CREATE WINDOW ICON AND TITLE*/
        try {
	        Image image = new Image(SimuladorGPLCS.class.getResourceAsStream("logo_ufpa.png"));
	        primaryStage.getIcons().add(image);
	        primaryStage.setTitle("G. Fast Physical Layer and Cable Simulator");
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        /*ADDING ALL ELEMENTS TO GRID*/
        grid.add(label, 0, 0, 3, 1);
        GridPane.setHalignment(label, HPos.CENTER);
        grid.add(button1, 0, 1, 1, 1);
        GridPane.setHalignment(button1, HPos.RIGHT);
        grid.add(button2, 1, 1, 1, 1);
        GridPane.setHalignment(button2, HPos.CENTER);
        grid.add(button3, 2, 1, 1, 1);
        GridPane.setHalignment(button3, HPos.LEFT);
        grid.add(labelFooter, 0, 2, 3, 1);
        GridPane.setHalignment(labelFooter, HPos.CENTER);
        grid.setAlignment(Pos.CENTER);
        
        
        /*CREATE SCROLL PANE*/
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        return scrollPane;
		
	}
	
    @Override
    public void start(Stage primaryStage) {
    	    	
        primaryStage.setScene(new Scene(SimuladorGPLCS.createMainScene(primaryStage)));
    	String css = SimuladorGPLCS.class.getResource("MainScreen.css").toExternalForm(); 
        primaryStage.getScene().getStylesheets().add(css);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
        primaryStage.show();
    
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
