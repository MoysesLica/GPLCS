package CableAnalysis;

import com.jfoenix.controls.JFXButton;

import GPLCS.SimuladorGPLCS;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.GlyphsStack;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class CableAnalysisScreen {

    /*CREATE WINDOW FOR CABLE SYNSTHESIS*/
    public static ScrollPane getCableAnalysisScene(Stage primaryStage){
    
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
        String ApplicationName = "Cable Analysis Module";
        Label label = new Label(ApplicationName);
        label.setId("LabelScreen");
        label.setAlignment(Pos.CENTER);

        /*CREATE HELP LABEL*/
        Label helpLabel = new Label("Select one of models above to start cable analysis:");
        helpLabel.setId("HelpLabel");
        helpLabel.setAlignment(Pos.CENTER);

        /*CREATE 3 BUTTONS*/
        JFXButton buttonKHM1   = new JFXButton("KHM 1");
        buttonKHM1.setId("KHM1");
        buttonKHM1.setFocusTraversable(false);
        buttonKHM1.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	/*ADD*/
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
            	/*ADD*/
            }
        });
        
        JFXButton buttonBT0    = new JFXButton("BT0");
        buttonBT0.setId("BT0");
        buttonBT0.setFocusTraversable(false);
        buttonBT0.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	/*ADD*/
            }
        });

        JFXButton buttonBT1    = new JFXButton("BT1");
        buttonBT1.setId("BT1");
        buttonBT1.setFocusTraversable(false);
                
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
        grid.add(buttonTNOEAB, 1, line, 1, 1);
        GridPane.setHalignment(buttonTNOEAB, HPos.CENTER);
        grid.add(buttonBT0, 2, line, 1, 1);
        GridPane.setHalignment(buttonBT0, HPos.LEFT);
        
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
