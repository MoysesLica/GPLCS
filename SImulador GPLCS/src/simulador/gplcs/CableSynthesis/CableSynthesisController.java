package simulador.gplcs.CableSynthesis;

import KHM.KHMController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import java.util.*;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import javafx.stage.DirectoryChooser;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @author moyses
 */
public class CableSynthesisController {

    /*CREATE WINDOW FOR CABLE SYNSTHESIS*/
    public static Scene getCableSynthesisScene(Stage primaryStage){
    
    	/*GET SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
        
        /*CREATE THE GRID*/
        GridPane grid = new GridPane();
        grid.setVgap(25);
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
        
        /*CREATE SCROLL PANE*/
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        
        /*CREATE SCENE*/
        Scene scene = new Scene(scrollPane);
        String css = CableSynthesisController.class.getResource("CableSynthesisScreen.css").toExternalForm(); 
        scene.getStylesheets().add(css);
        
        return scene;
        
    }
    
    /*GET WINDOW FOR KHM CABLE SYNTHESIS*/
    public static Scene getKHMScreen(Stage primaryStage){
    
    	/*GET SCREEN HEIGHT AND WIDTH*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
        
        /*CREATE THE GRID*/
        GridPane grid = new GridPane();
        grid.setVgap(25);
        grid.setHgap(10);
        grid.setPadding(new Insets(0,0,0,0));
        grid.setPrefSize(screenWidth*80, screenHeight*80);
        for (int i = 0; i < 3; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(25);
            grid.getColumnConstraints().add(column);
        }
        
        /*CREATE THE LABEL OF SCREEN*/
        String ApplicationName = "KH Model Cable Synthesis";
        Label label = new Label(ApplicationName);
        label.setId("LabelScreen");
        label.setAlignment(Pos.CENTER);

        /*CREATE HELP LABELS*/
        Label helpLabel = new Label("Insert the parameters to calculate:");
        helpLabel.setId("HelpLabel");
        helpLabel.setAlignment(Pos.CENTER);
        
        /*CREATE THE INPUTS PARAMETERS*/        
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

        /*CREATE THE INPUTS CONFIG*/
        JFXComboBox<Label> frequency = new JFXComboBox<Label>();
        frequency.getItems().add(new Label("2.2MHz - 106MHz"));
        frequency.getItems().add(new Label("2.2MHz - 212MHz"));
        frequency.getItems().add(new Label("2.2MHz - 424MHz"));
        frequency.getItems().add(new Label("2.2MHz - 848MHz"));
        frequency.setPromptText("Frequency Band");
        JFXComboBox<Label> scale = new JFXComboBox<Label>();
        scale.getItems().add(new Label("Logarithmic"));
        scale.getItems().add(new Label("Linear"));
        scale.setPromptText("Scale");
        JFXComboBox<Label> parameterCalc = new JFXComboBox<Label>();
        parameterCalc.getItems().add(new Label("Propagation Constant"));
        parameterCalc.getItems().add(new Label("Characteristic Impedance"));
        parameterCalc.getItems().add(new Label("Transfer Function"));
        parameterCalc.setPromptText("Parameter to be Calculated");

        /*GENERATE FILE INPUT BUTTON*/
        JFXButton fileInput = new JFXButton("Select File With Parameters to Upload");
        fileInput.setId("fileInput");
        /*SET BUTTON ONCLICK FUNCTION*/
        fileInput.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	/*GET THE FILE*/
            	FileChooser fileChooser = new FileChooser();
            	fileChooser.setTitle("Open a Parameter File");

            	File file = fileChooser.showOpenDialog(primaryStage);

            	if(file != null) {
            		Scanner scanner;
					try {
						scanner = new Scanner(file);
	        	        String textOfFile = "";
						while (scanner.hasNextLine()) {
							textOfFile += scanner.nextLine() + "\n";
	        	        }
        	        	Stage fileView = new Stage();
        	        	GridPane grid = new GridPane();
        	        	grid.setHgap(10);
        	            grid.setVgap(10);
        	            grid.setPadding(new Insets(10, 10, 10, 10));
        	            final Text contentOfFile = new Text(textOfFile);
        	            grid.add(contentOfFile, 0, 0, 2, 1);        	            
        	            
        	            ScrollPane scroll = new ScrollPane();
        	            scroll.setContent(grid);
        	            
        	            Scene fileViewer = new Scene(scroll, screenWidth*75, screenHeight*75);
        	            
        	            fileView.setScene(fileViewer);
        	            fileView.show();
        	            
        	            
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

            	} 		
                
           }
        });

        
        /*GENERATE CALC BUTTON*/
        JFXButton calculate = new JFXButton("Generate Graphs");
        calculate.setId("calculate");
        /*SET BUTTON ONCLICK FUNCTION*/
        calculate.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                
                double k1_value;
                double k2_value;
                double k3_value;
                double h1_value;
                double h2_value;
                double minF;
                double maxF;
                double cableLength_value;
                String axisScale;
                String parameter;
                
                /*VALIDATE INFO'S*/
                try{
                    k1_value = Double.parseDouble(k1.getText());
                    k2_value = Double.parseDouble(k2.getText());
                    k3_value = Double.parseDouble(k3.getText());
                    h1_value = Double.parseDouble(h1.getText());
                    h2_value = Double.parseDouble(h2.getText());
                    cableLength_value = Double.parseDouble(cableLength.getText());
                    minF = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
                    maxF = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
                    axisScale = scale.getValue().getText();
                    parameter = parameterCalc.getValue().getText();
                }catch(NumberFormatException e){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error, please fill correctly the inputs before continue!");
                    alert.setContentText(e.toString());
                    alert.showAndWait();
                    return;
                }
                
                /*GENERATE GRAPHS*/
                KHMController.generateGraphs(k1_value, k2_value, k3_value, h1_value, h2_value, cableLength_value, minF, maxF, 51.75e3, axisScale, parameter);
                
           }
        });
        
        /*CREATE OUTPUT FILE BUTTON*/
        JFXButton outputFile = new JFXButton("Generate Result Output File");
        outputFile.setId("fileOutput");
        /*CREATE ONCLICK FUNCTION*/
        outputFile.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
            	
            	/*SAVE FILE WINDOW*/
            	FileChooser fileChooser = new FileChooser();
            	fileChooser.setTitle("Save Result Output File");
                File selectedDirectory = fileChooser.showSaveDialog(primaryStage);
            	
            	if(selectedDirectory != null){
            		
                	double k1_value;
                    double k2_value;
                    double k3_value;
                    double h1_value;
                    double h2_value;
                    double minF;
                    double maxF;
                    double cableLength_value;
                    String axisScale;
                    String parameter;
                	
                	try{
                        k1_value = Double.parseDouble(k1.getText());
                        k2_value = Double.parseDouble(k2.getText());
                        k3_value = Double.parseDouble(k3.getText());
                        h1_value = Double.parseDouble(h1.getText());
                        h2_value = Double.parseDouble(h2.getText());
                        cableLength_value = Double.parseDouble(cableLength.getText());
                        minF = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
                        maxF = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
                        axisScale = scale.getValue().getText();
                        parameter = parameterCalc.getValue().getText();
                    }catch(NumberFormatException ee){
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error, please fill correctly the inputs before continue!");
                        alert.setContentText(ee.toString());
                        alert.showAndWait();
                        return;
                    }
                    
                    try {
						KHMController.generateOutputFile(k1_value, k2_value, k3_value, h1_value, h2_value, cableLength_value, minF, maxF, 51.75e3, axisScale, parameter, selectedDirectory);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}                    
                	
                }
            }
        });
        
        /*ADDING ALL ELEMENTS TO GRID*/
        grid.add(label, 0, 0, 3, 1);
        grid.setHalignment(label, HPos.CENTER);
        grid.add(helpLabel, 0, 1, 3, 1);
        grid.setHalignment(helpLabel, HPos.CENTER);
        
        /*GENERATE FIRST LINE*/
        grid.add(k1, 0, 2, 1, 1);
        grid.add(k2, 1, 2, 1, 1);
        grid.add(k3, 2, 2, 1, 1);
        
        /*GENERATE SECOND LINE*/
        grid.add(h1, 0, 3, 1, 1);
        grid.add(h2, 1, 3, 1, 1);
        grid.add(cableLength, 2, 3, 1, 1);
        grid.setHalignment(cableLength, HPos.CENTER);
        
        /*GENERATE THIRD LINE*/
        frequency.setMinWidth(screenWidth*20);
        grid.add(frequency, 0, 4, 1, 1);
        grid.setHalignment(frequency, HPos.CENTER);
        scale.setMinWidth(screenWidth*20);
        grid.add(scale, 1, 4, 1, 1);
        grid.setHalignment(scale, HPos.CENTER);
        parameterCalc.setMinWidth(screenWidth*20);
        grid.add(parameterCalc, 2, 4, 1, 1);
        grid.setHalignment(parameterCalc, HPos.CENTER);

        /*GENERATE FOURTH LINE*/
        fileInput.setMinWidth(screenWidth*20);
        grid.add(fileInput, 0, 5, 1, 1);
        calculate.setMinWidth(screenWidth*20);
        grid.add(calculate, 1, 5, 1, 1);
        outputFile.setMinWidth(screenWidth*20);
        grid.add(outputFile, 2, 5, 1, 1);
        grid.setAlignment(Pos.CENTER);
        
        /*CREATE SCENE*/
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        
        Scene scene = new Scene(scrollPane);
        String css = CableSynthesisController.class.getResource("KHMScreen.css").toExternalForm(); 
        scene.getStylesheets().add(css);
        
        return scene;
    
    }
 
    /*CREATE WINDOW SHOWING KHM HELP*/
    public static void generateHelpKHM() {
    	
    	Stage helpScreen = new Stage();
    	
    	/*GET SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;

        /*GENERATE GRID PANE*/
        GridPane grid = new GridPane();
        grid.setVgap(30);
        grid.setHgap(10);
        grid.setPadding(new Insets(25,25,25,25));
        grid.setPrefSize(screenWidth*80, screenHeight*80);
        
        Scene scene = new Scene(grid);
        
        
        /*ADDING HELP HERE*/
        
        helpScreen.setScene(scene);
        helpScreen.show();
    	
    }
    
}
