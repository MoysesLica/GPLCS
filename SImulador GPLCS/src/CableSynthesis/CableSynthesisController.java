package CableSynthesis;

import KHM.KHMController;
import KHM.KHMScreen;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import GPLCS.SimuladorGPLCS;
import de.jensd.fx.glyphs.*;
import de.jensd.fx.glyphs.fontawesome.*;
import java.util.*;
import java.util.regex.Pattern;
import java.awt.Toolkit;
import java.io.File;
import javafx.scene.layout.Region;
import java.io.FileNotFoundException;
import java.util.stream.Collectors;
import java.io.PrintStream;
import javafx.stage.DirectoryChooser;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

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
        JFXButton buttonKHM2   = new JFXButton("KHM 2");
        JFXButton buttonKHM3   = new JFXButton("KHM 3");
        JFXButton buttonTNOEAB = new JFXButton("TNO/EAB");
        JFXButton buttonBT0    = new JFXButton("BT0");
        buttonKHM1.setFocusTraversable(false);
        buttonKHM2.setFocusTraversable(false);
        buttonKHM3.setFocusTraversable(false);
        buttonTNOEAB.setFocusTraversable(false);
        buttonBT0.setFocusTraversable(false);
        buttonKHM1.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                primaryStage.getScene().setRoot(KHMScreen.getKHMScreen(primaryStage));
            	String css = KHMScreen.class.getResource("KHMScreen.css").toExternalForm(); 
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

        /*FIRST LINE*/
        grid.add(label, 0, 0, 3, 1);
        grid.setHalignment(label, HPos.CENTER);
        
        /*SECOND LINE*/
        grid.add(helpLabel, 0, 1, 3, 1);
        grid.setHalignment(helpLabel, HPos.CENTER);
        
        /*THIRD LINE*/
        grid.add(buttonKHM1, 0, 2, 1, 1);
        grid.setHalignment(buttonKHM1, HPos.RIGHT);
        grid.add(buttonKHM2, 1, 2, 1, 1);
        grid.setHalignment(buttonKHM2, HPos.CENTER);
        grid.add(buttonKHM3, 2, 2, 1, 1);
        grid.setHalignment(buttonKHM3, HPos.LEFT);

        /*FOURTH LINE*/
        grid.add(buttonTNOEAB, 0, 3, 1, 1);
        grid.setHalignment(buttonTNOEAB, HPos.RIGHT);
        grid.add(buttonBT0, 1, 3, 1, 1);
        grid.setHalignment(buttonBT0, HPos.CENTER);
        
        /*FIFTH LINE*/
        grid.add(back, 1, 4, 1, 1);
        grid.setHalignment(back, HPos.CENTER);
        grid.setAlignment(Pos.CENTER);
        
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
