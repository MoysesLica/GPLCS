package TNO_EAB;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import CableSynthesis.CableSynthesisController;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.GlyphsStack;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TNO_EABScreen {

    public static ScrollPane getKHMScreen(Stage primaryStage){
        
        /*CREATE THE GRID*/
        GridPane grid = new GridPane();
        grid.setVgap(25);
        grid.setHgap(10);
        grid.setPadding(new Insets(0,0,0,0));
        for (int i = 0; i < 3; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(25);
            grid.getColumnConstraints().add(column);
        }
        
        /*CREATE THE LABEL OF SCREEN*/
        String ApplicationName = "TNO/EAB Model Cable Synthesis";
        Label label = new Label(ApplicationName);
        label.setId("LabelScreen");
        label.setAlignment(Pos.CENTER);

        /*CREATE HELP LABELS PREDEFINED*/
        Label labelPred = new Label("Select the type of cable to generate the curves: ");
        labelPred.setId("HelpLabel");
        labelPred.setAlignment(Pos.CENTER);
                
        /*CREATE THE INPUTS PARAMETERS*/        
        JFXTextField phi = new JFXTextField();
        phi.setLabelFloat(true);
        phi.setPromptText("φ");

        JFXTextField qH = new JFXTextField();
        qH.setLabelFloat(true);
        qH.setPromptText("qH");

        JFXTextField qL = new JFXTextField();
        qL.setLabelFloat(true);
        qL.setPromptText("qL");
        
        JFXTextField qx = new JFXTextField();
        qx.setLabelFloat(true);
        qx.setPromptText("qx");
        
        JFXTextField qy = new JFXTextField();
        qy.setLabelFloat(true);
        qy.setPromptText("qy");

        JFXTextField Rs0 = new JFXTextField();
        Rs0.setLabelFloat(true);
        Rs0.setPromptText("Rs0");

        JFXTextField Z0inf = new JFXTextField();
        Z0inf.setLabelFloat(true);
        Z0inf.setPromptText("Z0∞");

        JFXTextField nVF = new JFXTextField();
        nVF.setLabelFloat(true);
        nVF.setPromptText("ηVF");

        JFXTextField qc = new JFXTextField();
        qc.setLabelFloat(true);
        qc.setPromptText("qc");

        JFXTextField fd = new JFXTextField();
        fd.setLabelFloat(true);
        fd.setPromptText("fd");
        
        final Label descriptionCable = new Label("Enter manually the parameters or choose at side a predefined cable type");
        descriptionCable.setId("descriptionCable");
        descriptionCable.setWrapText(true);
        
        /*CREATE COMBOBOX AND LABEL OF PREDEFINED CABLES*/
        JFXComboBox<Label> cableTypes = new JFXComboBox<Label>();
        cableTypes.getItems().add(new Label("Custom"));
        cableTypes.getItems().add(new Label("CAT5"));
        cableTypes.getItems().add(new Label("B05a"));
        cableTypes.getItems().add(new Label("T05b"));
        cableTypes.getItems().add(new Label("T05h"));
        cableTypes.getItems().add(new Label("T05u"));
        cableTypes.setPromptText("Custom");
        /*ON CHANGE CABLE TYPE*/
        cableTypes.valueProperty().addListener(new ChangeListener<Label>() {
            @Override
            public void changed(ObservableValue<? extends Label> observable, Label oldValue, Label newValue) {

            	/******** STANDARD CABLES TYPE *********/
            	switch(newValue.getText()) {
            	
            		case "CAT5":
        				descriptionCable.setText("Typical Category 5 cable commonly used in structured cabling for computer networks, such as Ethernet");
        				Z0inf.setText("98.000000");
        				nVF.setText("0.690464");
        				Rs0.setText("165.900000e-3");
        				qL.setText("2.150000");
        				qH.setText("0.859450");
        				qx.setText("0.500000");
        				qy.setText("0.722636");
        				qc.setText("0");
        				phi.setText("0.973846e-3");
        				fd.setText("1.000000");
        				Z0inf.setDisable(true);
        				nVF.setDisable(true);
        				Rs0.setDisable(true);
        				qL.setDisable(true);
        				qH.setDisable(true);
        				qx.setDisable(true);
        				qy.setDisable(true);
        				qc.setDisable(true);
        				phi.setDisable(true);
        				fd.setDisable(true);
    				break;
            		case "B05a":
            			descriptionCable.setText("Cable Aerial Drop-wire No 55 (CAD55), a typical copper line used in the UK");
            			Z0inf.setText("105.0694");
        				nVF.setText  ("0.6976");
        				Rs0.setText  ("0.1871");
        				qL.setText   ("1.5315");
        				qH.setText   ("0.7415");
        				qx.setText   ("1");
        				qy.setText   ("0");
        				qc.setText   ("1.0016");
        				phi.setText  ("-0.2356");
        				fd.setText   ("1.000000");
        				Z0inf.setDisable(true);
        				nVF.setDisable(true);
        				Rs0.setDisable(true);
        				qL.setDisable(true);
        				qH.setDisable(true);
        				qx.setDisable(true);
        				qy.setDisable(true);
        				qc.setDisable(true);
        				phi.setDisable(true);
        				fd.setDisable(true);        				
        			break;
            		case "T05b":
    					descriptionCable.setText("Medium quality multi-quad cable used in buildings");            			
            			Z0inf.setText("132.348256");
        				nVF.setText  ("0.675449");
        				Rs0.setText  ("170.500000e-3");
        				qL.setText   ("1.789725");
        				qH.setText   ("0.725776");
        				qx.setText   ("0.799306");
        				qy.setText   ("1.030832");
        				qc.setText   ("0");
        				phi.setText  ("0.005222e-3");
        				fd.setText   ("1.000000");
        				Z0inf.setDisable(true);
        				nVF.setDisable(true);
        				Rs0.setDisable(true);
        				qL.setDisable(true);
        				qH.setDisable(true);
        				qx.setDisable(true);
        				qy.setDisable(true);
        				qc.setDisable(true);
        				phi.setDisable(true);
        				fd.setDisable(true);
        			break;
            		case "T05h":
    					descriptionCable.setText("Low quality cable used for in-house telephony wiring");        				
            			Z0inf.setText("98.369783");
        				nVF.setText  ("0.681182");
        				Rs0.setText  ("170.800000e-3");
        				qL.setText   ("1.700000");
        				qH.setText   ("0.650000");
        				qx.setText   ("0.777307");
        				qy.setText   ("1.500000");
        				qc.setText   ("0");
        				phi.setText  ("3.023930e-3");
        				fd.setText   ("1.000000");
        				Z0inf.setDisable(true);
        				nVF.setDisable(true);
        				Rs0.setDisable(true);
        				qL.setDisable(true);
        				qH.setDisable(true);
        				qx.setDisable(true);
        				qy.setDisable(true);
        				qc.setDisable(true);
        				phi.setDisable(true);
        				fd.setDisable(true);
        			break;
            		case "T05u":
        				descriptionCable.setText("KPN cable, a typical access line used in the Netherlands");        				
            			Z0inf.setText("125.636455");
        				nVF.setText  ("0.729623");
        				Rs0.setText  ("180.000000e-3");
        				qL.setText   ("1.666050");
        				qH.setText   ("0.740000");
        				qx.setText   ("0.848761");
        				qy.setText   ("1.207166");
        				qc.setText   ("0");
        				phi.setText  ("1.762056e-3");
        				fd.setText   ("1.000000");
        				Z0inf.setDisable(true);
        				nVF.setDisable(true);
        				Rs0.setDisable(true);
        				qL.setDisable(true);
        				qH.setDisable(true);
        				qx.setDisable(true);
        				qy.setDisable(true);
        				qc.setDisable(true);
        				phi.setDisable(true);
        				fd.setDisable(true);
    				break;
            		default:
        				descriptionCable.setText("Enter manually the parameters or choose at side a predefined cable type");        				
        				Z0inf.setText("");
        				nVF.setText  ("");
        				Rs0.setText  ("");
        				qL.setText   ("");
        				qH.setText   ("");
        				qy.setText   ("");
        				qy.setText   ("");
        				qc.setText   ("");
        				phi.setText  ("");
        				fd.setText   ("");
        				Z0inf.setDisable(false);
            			nVF.setDisable(false);
        				Rs0.setDisable(false);
        				qL.setDisable(false);
        				qH.setDisable(false);
        				qx.setDisable(false);
        				qy.setDisable(false);
        				qc.setDisable(false);
        				phi.setDisable(false);
        				fd.setDisable(false);
        			break;
            	}
            	
            }
        });
                
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
        parameterCalc.getItems().add(new Label("Primary Parameters"));
        parameterCalc.setPromptText("Parameter to be Calculated");

        /*GENERATE FILE INPUT BUTTON*/
        Region inputIcon = GlyphsStack.create().add(
        		GlyphsBuilder.create(FontAwesomeIcon.class)
        			.icon(FontAwesomeIconName.UPLOAD)
        			.style("-fx-fill: white;")
        			.size("1em")
        			.build()
        		);        
        Button fileInput = new Button("Select Parameter File", inputIcon);
        fileInput.setId("fileInput");
        /*SET BUTTON ONCLICK FUNCTION*/
        fileInput.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
         
            	/*GET THE FILE*/
            	FileChooser fileChooser = new FileChooser();
            	fileChooser.setTitle("Open a Parameter File");

            	File file = fileChooser.showOpenDialog(primaryStage);

            	if(file != null) {
            		/**********************AJEITAR AQUI***************************/
/*            		primaryStage.getScene().setRoot(TNO_EABScreen.getInputFileWindow(primaryStage, file));
            		String css = TNO_EABScreen.class.getResource("InputFileWindow.css").toExternalForm(); 
                	primaryStage.getScene().getStylesheets().clear();
                	primaryStage.getScene().getStylesheets().add(css);*/

            	}
            	
            }
        });
				        
        /*GENERATE CALC BUTTON*/
        Region calcIcon = GlyphsStack.create().add(
        		GlyphsBuilder.create(FontAwesomeIcon.class)
        			.icon(FontAwesomeIconName.CALCULATOR)
        			.style("-fx-fill: white;")
        			.size("1em")
        			.build()
        		);        
        Button calculate = new Button("Generate Graphs", calcIcon);
        calculate.setId("calculate");
        
        /*SET BUTTON ONCLICK FUNCTION*/
        calculate.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                
            	
            	
            	
            	
            	
            	
            	
                double Z0inf_value;
                double nVF_value;
                double Rs0_value;
                double qL_value;
                double qH_value;
                double qx_value;
                double qy_value;
                double qc_value;
                double phi_value;
                double fd_value;
                double minF;
                double maxF;
                double cableLength_value;
                String axisScale;
                String parameter;
                
                /*VALIDATE INFO'S*/
                try{
                	Z0inf_value = Double.parseDouble(Z0inf.getText());
                	nVF_value = Double.parseDouble(nVF.getText());
                	Rs0_value = Double.parseDouble(Rs0.getText());
                	qL_value = Double.parseDouble(qL.getText());
                	qH_value = Double.parseDouble(qH.getText());
                	qx_value = Double.parseDouble(qx.getText());
                	qy_value = Double.parseDouble(qy.getText());
                	qc_value = Double.parseDouble(qc.getText());
                	phi_value = Double.parseDouble(phi.getText());
                	fd_value = Double.parseDouble(fd.getText());
                    cableLength_value = Double.parseDouble(cableLength.getText());
                    minF = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
                    maxF = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
                    axisScale = scale.getValue().getText();
                    parameter = parameterCalc.getValue().getText();
                }catch(Exception e){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error, please fill correctly the inputs before continue!");
                    alert.setContentText(e.toString());
                    alert.showAndWait();
                    return;
                }
                
                /*GENERATE GRAPHS*/
                TNO_EABController.generateGraphs(Z0inf_value, nVF_value, Rs0_value, qL_value, qH_value, qx_value, qy_value, qc_value, phi_value, fd_value, cableLength_value, minF, maxF, 51.75e3, axisScale, parameter);
                
            	
            	
            	
            	
            	
            	
            	
            	
            	
            	
            	
            	
            	
            	

            }
        });
        
        /*CREATE OUTPUT FILE BUTTON*/
        Region outputIcon = GlyphsStack.create().add(
        		GlyphsBuilder.create(FontAwesomeIcon.class)
        			.icon(FontAwesomeIconName.DOWNLOAD)
        			.style("-fx-fill: white;")
        			.size("1em")
        			.build()
        		);        
        Button outputFile = new Button("Generate Result File", outputIcon);
        outputFile.setId("fileOutput");
        /*CREATE ONCLICK FUNCTION*/
        outputFile.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
            	
            
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
        back.setId("back-button");
        back.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {

            	/*COME BACK TO CABLE SYNTHESIS SCREEN*/
            	primaryStage.getScene().setRoot(CableSynthesisController.getCableSynthesisScene(primaryStage));
            	String css = CableSynthesisController.class.getResource("CableSynthesisScreen.css").toExternalForm(); 
            	primaryStage.getScene().getStylesheets().clear();
            	primaryStage.getScene().getStylesheets().add(css);

            }
        });
        
        /*ADDING ALL ELEMENTS TO GRID*/
        
        /*GENERATE FIRST LINE*/
        grid.add(label, 0, 0, 3, 1);
        GridPane.setHalignment(label, HPos.CENTER);
        
		/*LINE SECOND LINE*/
		grid.add(labelPred, 0, 1, 3, 1);
		GridPane.setHalignment(labelPred, HPos.CENTER);
		GridPane.setValignment(labelPred, VPos.CENTER);
		
		/*LINE THIRD LINE*/
		cableTypes.setMaxWidth(Double.MAX_VALUE);
		grid.add(cableTypes, 0, 2, 1, 1);
		GridPane.setHalignment(cableTypes, HPos.CENTER);
		GridPane.setValignment(cableTypes, VPos.CENTER);		
		descriptionCable.setMaxWidth(Double.MAX_VALUE);
		grid.add(descriptionCable, 1, 2, 2, 1);
		GridPane.setHalignment(descriptionCable, HPos.CENTER);
		GridPane.setValignment(descriptionCable, VPos.CENTER);		
		
        /*GENERATE FOURTH LINE*/
		phi.setMaxWidth(Double.MAX_VALUE);
        grid.add(phi, 0, 3, 1, 1);
        GridPane.setHalignment(phi, HPos.CENTER);
        GridPane.setValignment(phi, VPos.CENTER);
		qH.setMaxWidth(Double.MAX_VALUE);
        grid.add(qH, 1, 3, 1, 1);
        GridPane.setHalignment(qH, HPos.CENTER);
        GridPane.setValignment(qH, VPos.CENTER);
		qL.setMaxWidth(Double.MAX_VALUE);
        grid.add(qL, 2, 3, 1, 1);
        GridPane.setHalignment(qL, HPos.CENTER);
        GridPane.setValignment(qL, VPos.CENTER);
        
        /*GENERATE FIFTH LINE*/
		qx.setMaxWidth(Double.MAX_VALUE);
        grid.add(qx, 0, 4, 1, 1);
        GridPane.setHalignment(qx, HPos.CENTER);
        GridPane.setValignment(qx, VPos.CENTER);
        qy.setMaxWidth(Double.MAX_VALUE);
        grid.add(qy, 1, 4, 1, 1);
        GridPane.setHalignment(qy, HPos.CENTER);
        GridPane.setValignment(qy, VPos.CENTER);
        Rs0.setMaxWidth(Double.MAX_VALUE);
        grid.add(Rs0, 2, 4, 1, 1);
        GridPane.setHalignment(Rs0, HPos.CENTER);
        GridPane.setValignment(Rs0, VPos.CENTER);
        
        /*GENERATE SIXTH LINE*/
        Z0inf.setMaxWidth(Double.MAX_VALUE);
        grid.add(Z0inf, 0, 5, 1, 1);
        GridPane.setHalignment(Z0inf, HPos.CENTER);
        GridPane.setValignment(Z0inf, VPos.CENTER);
        nVF.setMaxWidth(Double.MAX_VALUE);
        grid.add(nVF, 1, 5, 1, 1);
        GridPane.setHalignment(nVF, HPos.CENTER);
        GridPane.setValignment(nVF, VPos.CENTER);
        qc.setMaxWidth(Double.MAX_VALUE);
        grid.add(qc, 2, 5, 1, 1);
        GridPane.setHalignment(qc, HPos.CENTER);
        GridPane.setValignment(qc, VPos.CENTER);
                
        /*GENERATE SEVENTH LINE*/
        fd.setMaxWidth(Double.MAX_VALUE);
        grid.add(fd, 0, 6, 1, 1);
        GridPane.setHalignment(fd, HPos.CENTER);
        GridPane.setValignment(fd, VPos.CENTER);
        cableLength.setMaxWidth(Double.MAX_VALUE);
        grid.add(cableLength, 1, 6, 1, 1);
        GridPane.setHalignment(cableLength, HPos.CENTER);
        GridPane.setValignment(cableLength, VPos.CENTER);
        frequency.setMaxWidth(Double.MAX_VALUE);
        grid.add(frequency, 2, 6, 1, 1);
        GridPane.setHalignment(frequency, HPos.CENTER);
        GridPane.setValignment(frequency, VPos.CENTER);

        /*GENERATE EIGHT LINE*/
        scale.setMaxWidth(Double.MAX_VALUE);
        grid.add(scale, 0, 7, 1, 1);
        GridPane.setHalignment(scale, HPos.CENTER);
        GridPane.setValignment(scale, VPos.CENTER);
        parameterCalc.setMaxWidth(Double.MAX_VALUE);
        grid.add(parameterCalc, 1, 7, 1, 1);
        GridPane.setHalignment(parameterCalc, HPos.CENTER);
        GridPane.setValignment(parameterCalc, VPos.CENTER);
        fileInput.setMaxWidth(Double.MAX_VALUE);
        grid.add(fileInput, 2, 7, 1, 1);
        GridPane.setHalignment(fileInput, HPos.CENTER);
        GridPane.setValignment(fileInput, VPos.CENTER);
		
        /*GENERATE NINETH LINE*/
        calculate.setMaxWidth(Double.MAX_VALUE);
        grid.add(calculate, 0, 8, 1, 1);
        GridPane.setHalignment(calculate, HPos.CENTER);
        GridPane.setValignment(calculate, VPos.CENTER);
        outputFile.setMaxWidth(Double.MAX_VALUE);
        grid.add(outputFile, 1, 8, 1, 1);
        GridPane.setHalignment(outputFile, HPos.CENTER);
        GridPane.setValignment(outputFile, VPos.CENTER);
        back.setMaxWidth(Double.MAX_VALUE);
        grid.add(back, 2, 8, 1, 1);
        GridPane.setHalignment(back, HPos.CENTER);
        GridPane.setValignment(back, VPos.CENTER);

        grid.setAlignment(Pos.CENTER);
        
        /*CREATE SCENE*/
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
                
        return scrollPane;
    
    }

	
}
