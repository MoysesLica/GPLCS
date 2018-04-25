package CableSynthesis;

import TNO_EAB.TNO_EABScreen;
import DiffBetweenModels.DiffBetweenModelsScreen;
import KHM1.KHM1Screen;
import MultiCable.MultiCableScreen;
import SingleCable.SingleCableScreen;
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
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author moyses
 */
public class CableSynthesisScreen {

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
        Label helpLabel = new Label("Select one of options above to start:");
        helpLabel.setId("HelpLabel");
        helpLabel.setAlignment(Pos.CENTER);

        Tooltip buttonSingleCable= new Tooltip("In this submodule you can generate "
										      +"\nthe transmission line characteristics "
										      +"\nfor single cable segments, like the "
										      +"\npropagation constant, characteristic "
										      +"\nimpedance, the primary parameters, "
										      +"\namong others.");        
        buttonSingleCable.setAutoHide(true);
        buttonSingleCable.setShowDelay(Duration.seconds(0));
        
        /*CREATE SINGLE CABLE SCREEN*/
        JFXButton singleCable   = new JFXButton("Single Cable");
        singleCable.setTooltip(buttonSingleCable);
        singleCable.setId("Single Screen");
        singleCable.setFocusTraversable(false);
        singleCable.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                primaryStage.getScene().setRoot(SingleCableScreen.getSingleCableScreen(primaryStage));
            	String css = SingleCableScreen.class.getResource("SingleCableScreen.css").toExternalForm(); 
            	primaryStage.getScene().getStylesheets().clear();
            	primaryStage.getScene().getStylesheets().add(css);
            }
        });

        Tooltip buttonMultiToolTip= new Tooltip("In this submodule you can generate "
										   +"\nthe transfer function for multiples "
										   +"\ncable segments.");
        buttonMultiToolTip.setAutoHide(true);
        buttonMultiToolTip.setShowDelay(Duration.seconds(0));

        JFXButton buttonMulti  = new JFXButton("Multiples Cables");
        buttonMulti.setTooltip(buttonMultiToolTip);
        buttonMulti.setId("Multi");
        buttonMulti.setFocusTraversable(false);
        buttonMulti.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                primaryStage.getScene().setRoot(MultiCableScreen.getMultiCableScreen(primaryStage));
            	String css = MultiCableScreen.class.getResource("MultiCableScreen.css").toExternalForm(); 
            	primaryStage.getScene().getStylesheets().clear();
            	primaryStage.getScene().getStylesheets().add(css);
            }
        });
        
        Tooltip buttonDiffTooltip = new Tooltip("In this submodule you can compare the "
											 +"\ndifferent transmission line models that "
											 +"\nare used for G. Fast.");
        buttonDiffTooltip.setAutoHide(true);
        buttonDiffTooltip.setShowDelay(Duration.seconds(0));

        
        JFXButton buttonDiff   = new JFXButton("Difference Between Models");
        buttonDiff.setTooltip(buttonDiffTooltip);
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
        grid.add(singleCable, 0, line, 1, 1);
        GridPane.setHalignment(singleCable, HPos.RIGHT);
        grid.add(buttonMulti, 1, line, 1, 1);
        GridPane.setHalignment(buttonMulti, HPos.CENTER);
        grid.add(buttonDiff, 2, line, 1, 1);
        GridPane.setHalignment(buttonDiff, HPos.LEFT);        
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
