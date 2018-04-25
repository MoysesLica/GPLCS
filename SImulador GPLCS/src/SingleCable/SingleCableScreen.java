package SingleCable;

import java.util.Vector;

import com.jfoenix.controls.JFXButton;

import BT0.BT0Screen;
import CableSynthesis.CableSynthesisScreen;
import KHM1.KHM1Screen;
import TNO_EAB.TNO_EABScreen;
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
import javafx.scene.layout.Border;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.shape.Box;
import javafx.stage.Stage;


public class SingleCableScreen {

    /*CREATE WINDOW FOR CABLE SYNSTHESIS*/
    public static ScrollPane getSingleCableScreen(Stage primaryStage){
    
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

        /*CREATE THE HELP LABEL*/
        Label helpLabel = new Label("Select the model to start calculate the transmission line characteristics: ");
        helpLabel.setId("HelpLabel");
        helpLabel.setAlignment(Pos.CENTER);
        
        Vector<Integer> linha = new Vector<Integer>();
                
        /*CREATE 3 BUTTONS*/
        JFXButton buttonKHM1   = new JFXButton("KHM 1");
        buttonKHM1.setId("KHM1");
        buttonKHM1.setFocusTraversable(false);
        buttonKHM1.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	grid.getChildren().remove(grid.getChildren().size() - 1);
                grid.add(KHM1Screen.getKHMScreen(primaryStage), 0, linha.get(0), 3, 1);
                String css1 = KHM1Screen.class.getResource("KHM1Screen.css").toExternalForm(); 
            	String css2 = SingleCableScreen.class.getResource("SingleCableScreen.css").toExternalForm(); 
            	primaryStage.getScene().getStylesheets().clear();
            	primaryStage.getScene().getStylesheets().addAll(css1, css2);
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
            	grid.getChildren().remove(grid.getChildren().size() - 1);
                grid.add(TNO_EABScreen.getTNO_EABScreen(primaryStage), 0, linha.get(0), 3, 1);
            	String css1 = TNO_EABScreen.class.getResource("TNO_EABScreen.css").toExternalForm(); 
            	String css2 = SingleCableScreen.class.getResource("SingleCableScreen.css").toExternalForm(); 
            	primaryStage.getScene().getStylesheets().clear();
            	primaryStage.getScene().getStylesheets().addAll(css1, css2);
            }
        });
        
        JFXButton buttonBT0    = new JFXButton("BT0");
        buttonBT0.setId("BT0");
        buttonBT0.setFocusTraversable(false);
        buttonBT0.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	grid.getChildren().remove(grid.getChildren().size() - 1);
                grid.add(BT0Screen.getBT0Screen(primaryStage), 0, linha.get(0), 3, 1);
            	String css1 = BT0Screen.class.getResource("BT0Screen.css").toExternalForm(); 
            	String css2 = SingleCableScreen.class.getResource("SingleCableScreen.css").toExternalForm(); 
            	primaryStage.getScene().getStylesheets().clear();
            	primaryStage.getScene().getStylesheets().addAll(css1, css2);
            }
        });

        JFXButton buttonBT1    = new JFXButton("BT1");
        buttonBT1.setId("BT1");
        buttonBT1.setFocusTraversable(false);
        
        HBox box = new HBox();
        box.setPadding(new Insets(10,10,10,10));
        box.setAlignment(Pos.CENTER);
        box.setSpacing(15);
        box.getChildren().addAll(buttonKHM1, buttonTNOEAB ,buttonBT0);
        
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
        grid.add(box, 0, line, 3, 1);
        GridPane.setHalignment(box, HPos.CENTER);
        line++;
        
        /*ADDING BACK BUTTON*/
        Region iconBack = GlyphsStack.create().add(
        		GlyphsBuilder.create(FontAwesomeIcon.class)
        			.icon(FontAwesomeIconName.REPLY)
        			.style("-fx-fill: white;")
        			.size("1em")
        			.build()
        		);
        
        Button back = new Button("Back", iconBack);
        back.setId("back-button");
        back.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	
            	/*COME BACK TO CABLE SYNTHESIS SCREEN*/
            	primaryStage.getScene().setRoot(CableSynthesisScreen.getCableSynthesisScene(primaryStage));
            	String css = CableSynthesisScreen.class.getResource("CableSynthesisScreen.css").toExternalForm(); 
            	primaryStage.getScene().getStylesheets().clear();
            	primaryStage.getScene().getStylesheets().add(css);

            }
        });
        
        /*ADDING LINE*/
        grid.add(back, 0, line, 3, 1);
        GridPane.setHalignment(back, HPos.CENTER);
        line++;
        
        /*ADDING LINE*/
        linha.add(line);
        
        grid.add(new GridPane(), 0, line, 3, 1);
        GridPane.setHalignment(new GridPane(), HPos.CENTER);
        line++;
        
        /*CREATE SCROLL PANE*/
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        
        return scrollPane;
        
    }

	
}