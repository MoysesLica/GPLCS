package CableSynthesis;

import TNO_EAB.TNO_EABScreen;
import DiffBetweenModels.DiffBetweenModelsScreen;
import KHM1.KHM1Screen;
import BT0.BT0Screen;

import com.jfoenix.controls.JFXButton;

import GPLCS.SimuladorGPLCS;
import de.jensd.fx.glyphs.*;
import de.jensd.fx.glyphs.fontawesome.*;
import javafx.scene.layout.Region;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * @author moyses
 */
public class CableSynthesisController {

    /*CREATE WINDOW FOR CABLE SYNSTHESIS*/
    public static ScrollPane getCableSynthesisScene(Stage primaryStage){
    
        /*CREATE THE GRID*/
        GridPane grid = new GridPane();
        grid.setVgap(25);
        grid.setHgap(10);
        grid.setPadding(new Insets(25,25,25,25));
        for (int i = 0; i < 3; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(33.3);
            grid.getColumnConstraints().add(column);
        }
        
        /*CREATE THE LABEL OF SCREEN*/
        String ApplicationName = "Cable Synthesis Module";
        Label label = new Label(ApplicationName);
        label.setId("LabelScreen");
        label.setAlignment(Pos.CENTER);

        /*CREATE HELP LABEL*/
        Label helpLabel = new Label("Select one of models above to start cable synthesis:");
        helpLabel.setId("HelpLabel");
        helpLabel.setAlignment(Pos.CENTER);

        /*CREATE 3 BUTTONS*/
        JFXButton buttonKHM1   = new JFXButton("KHM 1");
        buttonKHM1.setId("KHM1");
        buttonKHM1.setFocusTraversable(false);
        buttonKHM1.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                primaryStage.getScene().setRoot(KHM1Screen.getKHMScreen(primaryStage));
            	String css = KHM1Screen.class.getResource("KHM1Screen.css").toExternalForm(); 
            	primaryStage.getScene().getStylesheets().clear();
            	primaryStage.getScene().getStylesheets().add(css);
            }
        });
        
        JFXButton buttonKHM2   = new JFXButton("KHM 2");
        buttonKHM2.setId("KHM2");
        buttonKHM2.setFocusTraversable(false);
        
        JFXButton buttonKHM3   = new JFXButton("KHM 3");
        buttonKHM3.setId("KHM3");
        buttonKHM3.setFocusTraversable(false);
        
        JFXButton buttonTNOEAB = new JFXButton("TNO/EAB");
        buttonTNOEAB.setId("TNOEAB");
        buttonTNOEAB.setFocusTraversable(false);
        buttonTNOEAB.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                primaryStage.getScene().setRoot(TNO_EABScreen.getTNO_EABScreen(primaryStage));
            	String css = TNO_EABScreen.class.getResource("TNO_EABScreen.css").toExternalForm(); 
            	primaryStage.getScene().getStylesheets().clear();
            	primaryStage.getScene().getStylesheets().add(css);
            }
        });
        
        JFXButton buttonBT0    = new JFXButton("BT0");
        buttonBT0.setId("BT0");
        buttonBT0.setFocusTraversable(false);
        buttonBT0.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                primaryStage.getScene().setRoot(BT0Screen.getBT0Screen(primaryStage));
            	String css = BT0Screen.class.getResource("BT0Screen.css").toExternalForm(); 
            	primaryStage.getScene().getStylesheets().clear();
            	primaryStage.getScene().getStylesheets().add(css);
            }
        });

        JFXButton buttonBT1    = new JFXButton("BT1");
        buttonBT1.setId("BT1");
        buttonBT1.setFocusTraversable(false);

        JFXButton buttonMulti  = new JFXButton("Multiples Cables");
        buttonMulti.setId("Multi");
        buttonMulti.setFocusTraversable(false);
        
        JFXButton buttonDiff   = new JFXButton("Difference Between Models");
        buttonDiff.setId("Diff");
        buttonDiff.setFocusTraversable(false);
        buttonDiff.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                primaryStage.getScene().setRoot(DiffBetweenModelsScreen.getDiffScreen(primaryStage));
            	String css = DiffBetweenModelsScreen.class.getResource("DiffBetweenModelsScreen.css").toExternalForm(); 
            	primaryStage.getScene().getStylesheets().clear();
            	primaryStage.getScene().getStylesheets().add(css);
            }
        });
        
        

        /*ADDING BACK BUTTON*/
        Region iconBack = GlyphsStack.create().add(
        		GlyphsBuilder.create(FontAwesomeIcon.class)
        			.icon(FontAwesomeIconName.REPLY)
        			.style("-fx-fill: white;")
        			.size("1em")
        			.build()
        		);
        
        Button back = new Button("Back", iconBack);
        back.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	/*COME BACK TO INITIAL SCREEN*/
            	primaryStage.getScene().setRoot(SimuladorGPLCS.createMainScene(primaryStage));
            	String css = SimuladorGPLCS.class.getResource("MainScreen.css").toExternalForm(); 
            	primaryStage.getScene().getStylesheets().clear();
            	primaryStage.getScene().getStylesheets().add(css);
            }
        });
        
        /*ADDING ALL ELEMENTS TO GRID*/
        int line = 0;
        
        /*ADDING LINE*/
        grid.add(label, 0, line, 3, 1);
        GridPane.setHalignment(label, HPos.CENTER);
        line++;
        
        /*ADDING LINE*/
        grid.add(helpLabel, 0, line, 3, 1);
        GridPane.setHalignment(helpLabel, HPos.CENTER);
        line++;
        
        /*ADDING LINE*/
        grid.add(buttonKHM1, 0, line, 1, 1);
        GridPane.setHalignment(buttonKHM1, HPos.RIGHT);
        grid.add(buttonKHM2, 1, line, 1, 1);
        GridPane.setHalignment(buttonKHM2, HPos.CENTER);
        grid.add(buttonKHM3, 2, line, 1, 1);
        GridPane.setHalignment(buttonKHM3, HPos.LEFT);
        line++;
        
        /*ADDING LINE*/
        grid.add(buttonTNOEAB, 0, line, 1, 1);
        GridPane.setHalignment(buttonTNOEAB, HPos.RIGHT);
        grid.add(buttonBT0, 1, line, 1, 1);
        GridPane.setHalignment(buttonBT0, HPos.CENTER);
        grid.add(buttonBT1, 2, line, 1, 1);
        GridPane.setHalignment(buttonBT1, HPos.LEFT);
        line++;
        
        /*ADDING LINE*/
        grid.add(buttonDiff, 0, line, 1, 1);
        GridPane.setHalignment(buttonDiff, HPos.RIGHT);        
        grid.add(buttonMulti, 1, line, 1, 1);
        GridPane.setHalignment(buttonMulti, HPos.CENTER);
        line++;
        
        /*ADDING LINE*/
        grid.add(back, 1, line, 1, 1);
        GridPane.setHalignment(back, HPos.CENTER);
        grid.setAlignment(Pos.CENTER);
        line++;
        
        /*CREATE SCROLL PANE*/
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        
        /*CREATE SCENE*/
        //Scene scene = new Scene(scrollPane);
        
        return scrollPane;
        
    }
    
}
