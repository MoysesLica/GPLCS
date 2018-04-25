package GPLCS;


import java.util.Locale;

import com.jfoenix.controls.JFXButton;

import CableAnalysis.CableAnalysisScreen;
import CableSynthesis.CableSynthesisScreen;
import DiffBetweenModels.DiffBetweenModelsScreen;
import KHM1.KHM1;
import KHM1.KHM1Screen;
import MultiCable.MultiCableScreen;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.GlyphsStack;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;

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

        
        Tooltip button1Tooltip= new Tooltip("In this module you can generate the characteristics of single "
									      + "\ncables, calculate the transfer function for multiples cables "
									      + "\nand see the difference between the cable models.");
        button1Tooltip.setAutoHide(true);
        button1Tooltip.setShowDelay(Duration.seconds(0));
                
        /*CREATE BUTTONS FOR ALL FUNCTIONS OF SIMULATOR*/        
        JFXButton button1 = new JFXButton("Cable Synthesis");
        
        Tooltip.install(button1, button1Tooltip);
        
        button1.setId("CS");
        button1.setFocusTraversable(false);
        button1.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	String css = CableSynthesisScreen.class.getResource("CableSynthesisScreen.css").toExternalForm(); 
            	primaryStage.getScene().setRoot(CableSynthesisScreen.getCableSynthesisScene(primaryStage));
            	primaryStage.getScene().getStylesheets().add(css);
            }
        });
        JFXButton button2 = new JFXButton("Cable Analysis");
        button2.setId("CA");
        button2.setFocusTraversable(false);
        button2.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	String css = CableAnalysisScreen.class.getResource("CableAnalysisScreen.css").toExternalForm(); 
            	primaryStage.getScene().setRoot(CableAnalysisScreen.getCableAnalysisScene(primaryStage));
            	primaryStage.getScene().getStylesheets().add(css);
            }
        });
        JFXButton button3 = new JFXButton("Network Simulation");
        button3.setId("NS");
        button3.setFocusTraversable(false);
        
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
        grid.add(labelFooter, 0, 3, 3, 1);
        GridPane.setHalignment(labelFooter, HPos.CENTER);
        grid.setAlignment(Pos.CENTER);
        
        Region iconClose = GlyphsStack.create().add(
        		GlyphsBuilder.create(FontAwesomeIcon.class)
        			.icon(FontAwesomeIconName.CLOSE)
        			.style("-fx-fill: white;")
        			.size("1em")
        			.build()
        		);

        Button close = new Button("Close Aplication", iconClose);
		close.setId("close");
		
        Tooltip buttonCloseTooltip= new Tooltip("Close GPLCS. ");
        buttonCloseTooltip.setAutoHide(true);
        buttonCloseTooltip.setShowDelay(Duration.seconds(0));

        close.setTooltip(buttonCloseTooltip);

		grid.add(close, 1, 2, 1, 1);
		GridPane.setHalignment(close, HPos.CENTER);
		GridPane.setValignment(close, VPos.CENTER);		

        close.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	
            	/*COME BACK TO CABLE SYNTHESIS SCREEN*/
            	primaryStage.close();
            	
            }
        });        
        
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

    public static void main(String[] args) {
        launch(args);
    }
    
}
