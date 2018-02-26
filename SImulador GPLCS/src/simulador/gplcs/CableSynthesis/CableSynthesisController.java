package simulador.gplcs.CableSynthesis;

import KHM.KHMController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.awt.Toolkit;
import java.util.Vector;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * @author moyses
 */
public class CableSynthesisController {

    public static Scene getCableSynthesisScene(Stage primaryStage){
    
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
        
        /*CREATE THE GRID*/
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25,25,25,25));
        grid.setPrefSize(screenWidth*50, screenHeight*50);
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
        JFXButton button1 = new JFXButton("KHM");
        JFXButton button2 = new JFXButton("TNO/EAB");
        JFXButton button3 = new JFXButton("BT0");
        button1.setFocusTraversable(false);
        button2.setFocusTraversable(false);
        button3.setFocusTraversable(false);
        button1.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                primaryStage.setScene(CableSynthesisController.getKHMScreen(primaryStage));
                primaryStage.setX(screenWidth*10);
                primaryStage.setY(screenHeight*10);
            }
        });

        
        /*ADDING ALL ELEMENTS TO GRID*/
        grid.add(label, 0, 0, 3, 1);
        grid.setHalignment(label, HPos.CENTER);
        grid.add(helpLabel, 0, 1, 3, 1);
        grid.setHalignment(helpLabel, HPos.CENTER);
        grid.add(button1, 0, 2, 1, 1);
        grid.add(button2, 1, 2, 1, 1);
        grid.add(button3, 2, 2, 1, 1);
        grid.setAlignment(Pos.CENTER);
        
        /*CREATE SCENE*/
        Scene scene = new Scene(grid);
        String css = CableSynthesisController.class.getResource("CableSynthesisScreen.css").toExternalForm(); 
        scene.getStylesheets().add(css);
        
        return scene;
        
    }
    
    public static Scene getKHMScreen(Stage primaryStage){
    
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
        
        /*CREATE THE GRID*/
        GridPane grid = new GridPane();
        grid.setVgap(20);
        grid.setHgap(10);
        grid.setPadding(new Insets(25,25,25,25));
        grid.setPrefSize(screenWidth*80, screenHeight*80);
        for (int i = 0; i < 4; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(25);
            grid.getColumnConstraints().add(column);
        }
        
        /*CREATE THE LABEL OF SCREEN*/
        String ApplicationName = "KH Model Cable Synthesis";
        Label label = new Label(ApplicationName);
        label.setId("LabelScreen");
        label.setAlignment(Pos.CENTER);

        /*CREATE HELP LABEL*/
        Label helpLabel = new Label("Insert the parameters to calculate:");
        helpLabel.setId("HelpLabel");
        helpLabel.setAlignment(Pos.CENTER);
        Label helpLabel2 = new Label("Parameters:");
        helpLabel2.setId("HelpLabel");
        helpLabel2.setAlignment(Pos.CENTER);

        /*CREATE 6 INPUTS*/
        JFXTextField k1 = new JFXTextField();
        k1.setLabelFloat(true);
        k1.setPromptText("K1");
        JFXTextField k2 = new JFXTextField();
        k2.setLabelFloat(true);
        k2.setPromptText("K2");
        JFXTextField k3 = new JFXTextField();
        k3.setLabelFloat(true);
        k3.setPromptText("K3");
        JFXTextField h1 = new JFXTextField();
        h1.setLabelFloat(true);
        h1.setPromptText("H1");
        JFXTextField h2 = new JFXTextField();
        h2.setLabelFloat(true);
        h2.setPromptText("H2");
        JFXTextField cableLength = new JFXTextField();
        cableLength.setLabelFloat(true);
        cableLength.setPromptText("Cable Length");

        /*CREATE 1 SELECT BOX*/
        JFXComboBox<Label> frequency = new JFXComboBox<Label>();
        frequency.getItems().add(new Label("Min1 - Max1"));
        frequency.getItems().add(new Label("Min2 - Max2"));
        frequency.getItems().add(new Label("Min3 - Max3"));
        frequency.setPromptText("Frequency Band");
        JFXComboBox<Label> scale = new JFXComboBox<Label>();
        scale.getItems().add(new Label("Logarithmic"));
        scale.getItems().add(new Label("Linear"));
        scale.setPromptText("Scale");
        JFXComboBox<Label> parameterCalc = new JFXComboBox<Label>();
        parameterCalc.getItems().add(new Label("Propagation Constant"));
        parameterCalc.getItems().add(new Label("Chatacteristic Impedance"));
        parameterCalc.setPromptText("Parameter to be Calculated");

        /*CREATE 1 BUTTON*/
        JFXButton calculate = new JFXButton("Calculate");
        calculate.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                Stage chart = new Stage();
           
                Scene graph = new Scene(KHMController.generatePropagationConstant(1.97311e-003, 1.24206e-008, 3.03005e-005, 98.5944, 6.0876e+003, 200, 2.2e6, 106e6, 51.75e3), screenWidth*50, screenHeight*50);
                
                chart.setScene(graph);
                
                chart.show();
           }
        });
        
        /*ADDING ALL ELEMENTS TO GRID*/
        grid.add(label, 0, 0, 4, 1);
        grid.setHalignment(label, HPos.CENTER);
        grid.add(helpLabel, 0, 1, 4, 1);
        grid.setHalignment(helpLabel, HPos.CENTER);
        grid.add(helpLabel2, 0, 2, 1, 1);
        grid.setHalignment(helpLabel2, HPos.CENTER);
        grid.add(k1, 0, 3, 1, 1);
        grid.add(k2, 0, 4, 1, 1);
        grid.add(k3, 0, 5, 1, 1);
        grid.add(h1, 0, 6, 1, 1);
        grid.add(h2, 0, 7, 1, 1);
        grid.add(cableLength, 1, 3, 1, 1);
        grid.setHalignment(cableLength, HPos.CENTER);
        frequency.setMinWidth(screenWidth*20);
        grid.add(frequency, 1, 4, 1, 1);
        grid.setHalignment(frequency, HPos.CENTER);
        scale.setMinWidth(screenWidth*20);
        grid.add(scale, 1, 5, 1, 1);
        grid.setHalignment(scale, HPos.CENTER);
        parameterCalc.setMinWidth(screenWidth*20);
        grid.add(parameterCalc, 1, 6, 1, 1);
        grid.setHalignment(parameterCalc, HPos.CENTER);
        grid.add(calculate, 2, 4, 1, 1);
        grid.setAlignment(Pos.CENTER);
        
        /*CREATE SCENE*/
        Scene scene = new Scene(grid);
        String css = CableSynthesisController.class.getResource("KHMScreen.css").toExternalForm(); 
        scene.getStylesheets().add(css);
        
        return scene;
    
    }
    
}
