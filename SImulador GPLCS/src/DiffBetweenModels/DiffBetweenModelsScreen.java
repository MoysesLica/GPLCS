package DiffBetweenModels;

import java.io.File;
import java.util.Vector;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import CableSynthesis.CableSynthesisController;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.GlyphsStack;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class DiffBetweenModelsScreen {
	

    /*GET WINDOW FOR DIFFERENCE BETWEEM MODELS ANALISYS*/
    public static ScrollPane getDiffScreen(Stage primaryStage){
    
        /*CREATE THE GRID*/
        GridPane grid = new GridPane();
        grid.setVgap(25);
        grid.setHgap(10);
        grid.setPadding(new Insets(25,25,25,25));
        for (int i = 0; i < 3; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(25);
            grid.getColumnConstraints().add(column);
        }
        
        /*CREATE THE LABEL OF SCREEN*/
        String ApplicationName = "Difference between cable models";
        Label label = new Label(ApplicationName);
        label.setId("LabelScreen");
        label.setAlignment(Pos.CENTER);

        /*CREATE HELP LABELS TYPES OF MODELS*/
        Label labelTypesOfModels = new Label("Select the type of models to compare: ");
        labelTypesOfModels.setId("HelpLabelTypes");
        labelTypesOfModels.setAlignment(Pos.CENTER);

        Label labelTNO_EAB = new Label("TNO/EAB"); 
        Label labelKHM1 = new Label("KHM 1"); 
        
        JFXComboBox<Label> model1 = new JFXComboBox<Label>();
        model1.getItems().addAll(new Label("TNO/EAB"), new Label("KHM 1"));
        model1.setPromptText("Select the Cable Model");
        
        JFXComboBox<Label> model2 = new JFXComboBox<Label>();
        model2.getItems().addAll(labelTNO_EAB, labelKHM1);
        model2.setPromptText("Select the Cable Model");

        Button selectButton = new Button("Select");
        selectButton.setId("select");
        
        /*ON CHANGE CABLE TYPE*/
        model1.valueProperty().addListener(new ChangeListener<Label>() {
            @Override
            public void changed(ObservableValue<? extends Label> observable, Label oldValue, Label newValue) {
            	if(oldValue != null) {

                	switch(oldValue.getText()) {

	            		case "TNO/EAB":
	                    		model2.getItems().add(labelTNO_EAB);            			
	            			break;
	
	            		case "KHM 1":
	            				model2.getItems().add(labelKHM1);            			            			
	            			break;
	
	            	}
            		
            	}
            	
            	switch(newValue.getText()) {

            		case "TNO/EAB":
                    		model2.getItems().remove(labelTNO_EAB);            			
            			break;

            		case "KHM 1":
            				model2.getItems().remove(labelKHM1);            			            			
            			break;

            	}
            	

            }
        });

        Label andLabel = new Label("and");
        andLabel.setId("HelpLabel");
        
        /*CREATE HELP LABELS PREDEFINED*/
        Label labelPred = new Label("Select the type of cable to generate the curves: ");
        labelPred.setId("HelpLabel");
        labelPred.setAlignment(Pos.CENTER);
                        
        /*CREATE COMBOBOX AND LABEL OF PREDEFINED CABLES*/
        JFXComboBox<Label> cableTypes = new JFXComboBox<Label>();
        cableTypes.getItems().add(new Label("Custom"));
        cableTypes.getItems().add(new Label("CAT5"));
        cableTypes.getItems().add(new Label("B05a"));
        cableTypes.getItems().add(new Label("T05b"));
        cableTypes.getItems().add(new Label("T05h"));
        cableTypes.getItems().add(new Label("T05u"));
        cableTypes.setPromptText("Custom");
        
        final Label descriptionCable = new Label("Enter manually the parameters or choose at side a predefined cable type");
        descriptionCable.setId("descriptionCable");
        descriptionCable.setWrapText(true);
        
        /*ON CHANGE CABLE TYPE*/
        cableTypes.valueProperty().addListener(new ChangeListener<Label>() {
            @Override
            public void changed(ObservableValue<? extends Label> observable, Label oldValue, Label newValue) {

            	/******** STANDARD CABLES TYPE *********/
            	switch(newValue.getText()) {
            	
            		case "CAT5":
        				descriptionCable.setText("Typical Category 5 cable commonly used in structured cabling for computer networks, such as Ethernet");

    				break;
            		case "B05a":
            			descriptionCable.setText("Cable Aerial Drop-wire No 55 (CAD55), a typical copper line used in the UK");

        			break;
            		case "T05b":
    					descriptionCable.setText("Medium quality multi-quad cable used in buildings");            			

        			break;
            		case "T05h":
    					descriptionCable.setText("Low quality cable used for in-house telephony wiring");

        			break;
            		case "T05u":
        				descriptionCable.setText("KPN cable, a typical access line used in the Netherlands");

    				break;
            		default:
        				descriptionCable.setText("Enter manually the parameters or choose at side a predefined cable type");

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
        
        /*GENERATE SECOND COLUMN LINE*/
        labelTypesOfModels.setMaxWidth(Double.MAX_VALUE);
        grid.add(labelTypesOfModels, 0, 1, 3, 1);
        GridPane.setHalignment(labelTypesOfModels, HPos.CENTER);
        
        /*GENERATE THIRD LINE*/
        model1.setMaxWidth(Double.MAX_VALUE);
        grid.add(model1, 0, 2, 1, 1);
        GridPane.setHalignment(model1, HPos.CENTER);
        
        grid.add(andLabel, 1, 2, 1, 1);
        GridPane.setFillWidth(andLabel, true);
        andLabel.setMaxWidth(Double.MAX_VALUE);
        andLabel.setAlignment(Pos.CENTER);
        GridPane.setHalignment(andLabel, HPos.CENTER);
        
        model2.setMaxWidth(Double.MAX_VALUE);
        grid.add(model2, 2, 2, 1, 1);
        GridPane.setHalignment(model2, HPos.CENTER);
        
        /*GENERATE FOURTH LINE*/        
        selectButton.setMaxWidth(Double.MAX_VALUE);
        grid.add(selectButton, 1, 3, 1, 1);
        GridPane.setHalignment(selectButton, HPos.CENTER);
        
        /*SET SELECT BUTTON ONCLICK FUNCTION*/
        selectButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {

            	if(model1.getValue()==null || model2.getValue()==null) {
            		
              	  Alert alert = new Alert(AlertType.INFORMATION);
                  alert.setTitle("Select the models to compare");
                  alert.setHeaderText("Select the models to compare!");
                  alert.showAndWait();
            		
            	}else {

                	int line = 0;
                	
              		grid.getChildren().clear();

              		grid.add(label, 0, line, 3, 1);
                    GridPane.setHalignment(label, HPos.CENTER);
                    line++;
                    
                    /*GENERATE SECOND COLUMN LINE*/
                    labelTypesOfModels.setMaxWidth(Double.MAX_VALUE);
                    grid.add(labelTypesOfModels, 0, line, 3, 1);
                    GridPane.setHalignment(labelTypesOfModels, HPos.CENTER);
                    line++;
                    
                    /*GENERATE THIRD LINE*/
                    model1.setMaxWidth(Double.MAX_VALUE);
                    grid.add(model1, 0, line, 1, 1);
                    GridPane.setHalignment(model1, HPos.CENTER);
                    
                    grid.add(andLabel, 1, line, 1, 1);
                    GridPane.setFillWidth(andLabel, true);
                    andLabel.setMaxWidth(Double.MAX_VALUE);
                    andLabel.setAlignment(Pos.CENTER);
                    GridPane.setHalignment(andLabel, HPos.CENTER);
                    
                    model2.setMaxWidth(Double.MAX_VALUE);
                    grid.add(model2, 2, line, 1, 1);
                    GridPane.setHalignment(model2, HPos.CENTER);
                    line++;
                    
                    /*GENERATE FOURTH LINE*/        
                    selectButton.setMaxWidth(Double.MAX_VALUE);
                    grid.add(selectButton, 1, line, 1, 1);
                    GridPane.setHalignment(selectButton, HPos.CENTER);
              		line++;                    

              		/*GENERATE ALL POSSIBLE INPUTS*/
              		
              			/*TNO*/
              		
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
	        	        
            	        /*KHM 1*/
	        	        
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
              		
                    /*GENERATE PREDETERMINED VALUES*/
        	        Label labelPred = new Label("Select the type of cable to generate the curves: ");
        	        labelPred.setId("HelpLabel");
        	        labelPred.setAlignment(Pos.CENTER);

        	        /*CREATE COMBOBOX AND LABEL OF PREDEFINED CABLES*/
        	        JFXComboBox<Label> cableTypes = new JFXComboBox<Label>();
        	        cableTypes.getItems().add(new Label("Custom"));
        	        cableTypes.getItems().add(new Label("CAT5"));
        	        cableTypes.getItems().add(new Label("B05a"));
        	        cableTypes.getItems().add(new Label("T05b"));
        	        cableTypes.getItems().add(new Label("T05h"));
        	        cableTypes.getItems().add(new Label("T05u"));
        	        cableTypes.setPromptText("Custom");
        	        
        	        final Label descriptionCable = new Label("Enter manually the parameters or choose at side a predefined cable type");
        	        descriptionCable.setId("descriptionCable");
        	        descriptionCable.setWrapText(true);
        	        
        	        /*ON CHANGE CABLE TYPE*/
        	        cableTypes.valueProperty().addListener(new ChangeListener<Label>() {
        	            @Override
        	            public void changed(ObservableValue<? extends Label> observable, Label oldValue, Label newValue) {

        	            	/******** STANDARD CABLES TYPE *********/
        	            	switch(newValue.getText()) {
        	            	
        	            		case "CAT5":
        	        				descriptionCable.setText("Typical Category 5 cable commonly used in structured cabling for computer networks, such as Ethernet");
        	        				k1.setText("1.97311e-003");
        	        				k2.setText("1.24206e-008");
        	        				k3.setText("3.03005e-005");
        	        				h1.setText("98.5944");
        	        				h2.setText("6.0876e+003");
        	        				k1.setDisable(true);
        	        				k2.setDisable(true);
        	        				k3.setDisable(true);
        	        				h1.setDisable(true);
        	        				h2.setDisable(true);
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
        	        				k1.setText("1.67334e-003");
        	        				k2.setText("1.35369e-007");
        	        				k3.setText("3.13189e-005");
        	        				h1.setText("106.6383");
        	        				h2.setText("5.5601e+003");
        	        				k1.setDisable(true);
        	        				k2.setDisable(true);
        	        				k3.setDisable(true);
        	        				h1.setDisable(true);
        	        				h2.setDisable(true);
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
        	        				k1.setText("1.70454e-003");
        	        				k2.setText("4.98183e-011");
        	        				k3.setText("3.10070e-005");
        	        				h1.setText("132.3825");
        	        				h2.setText("6.9128e+003");
        	        				k1.setDisable(true);
        	        				k2.setDisable(true);
        	        				k3.setDisable(true);
        	        				h1.setDisable(true);
        	        				h2.setDisable(true);
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
        	        				k1.setText("2.48426e-003");
        	        				k2.setText("4.65719e-008");
        	        				k3.setText("3.07543e-005");
        	        				h1.setText("100.3102");
        	        				h2.setText("6.9374e+003");
        	        				k1.setDisable(true);
        	        				k2.setDisable(true);
        	        				k3.setDisable(true);
        	        				h1.setDisable(true);
        	        				h2.setDisable(true);
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
        	        				k1.setText("1.78466e-003");
        	        				k2.setText("2.51367e-008");
        	        				k3.setText("2.87051e-005");
        	        				h1.setText("127.0785");
        	        				h2.setText("6.9114e+003");
        	        				k1.setDisable(true);
        	        				k2.setDisable(true);
        	        				k3.setDisable(true);
        	        				h1.setDisable(true);
        	        				h2.setDisable(true);
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
        	        				k1.setText("");
        	        				k2.setText("");
        	        				k3.setText("");
        	        				h1.setText("");
        	        				h2.setText("");
        	        				k1.setDisable(false);
        	        				k2.setDisable(false);
        	        				k3.setDisable(false);
        	        				h1.setDisable(false);
        	        				h2.setDisable(false);
        	        				Z0inf.setText("");
        	        				nVF.setText  ("");
        	        				Rs0.setText  ("");
        	        				qL.setText   ("");
        	        				qH.setText   ("");
        	        				qx.setText   ("");
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
            	        
        			/*ADDING TO GRID*/
        			cableTypes.setMaxWidth(Double.MAX_VALUE);
        			grid.add(cableTypes, 0, line, 1, 1);
        			GridPane.setHalignment(cableTypes, HPos.CENTER);
        			GridPane.setValignment(cableTypes, VPos.CENTER);		
        			descriptionCable.setMaxWidth(Double.MAX_VALUE);
        			grid.add(descriptionCable, 1, line, 2, 1);
        			GridPane.setHalignment(descriptionCable, HPos.CENTER);
        			GridPane.setValignment(descriptionCable, VPos.CENTER);		
        			line++;
            	        
            	        
                	switch(model1.getValue().getText()) {
                	
                		case "TNO/EAB":
                	        
                	        /*ADDING HELP LABEL*/
                	        Label helpLabel = new Label("TNO/EAB Parameters");
                	        helpLabel.setId("HelpLabel");
                	        grid.add(helpLabel, 1, line, 1, 1);
                	        GridPane.setFillWidth(helpLabel, true);
                	        helpLabel.setMaxWidth(Double.MAX_VALUE);
                	        helpLabel.setAlignment(Pos.CENTER);
                	        GridPane.setHalignment(helpLabel, HPos.CENTER);
                	        line++;
                	        
                	        /*GENERATE FOURTH LINE*/
                	        Z0inf.setMaxWidth(Double.MAX_VALUE);
                	        grid.add(Z0inf, 0, line, 1, 1);
                	        GridPane.setHalignment(Z0inf, HPos.CENTER);
                	        GridPane.setValignment(Z0inf, VPos.CENTER);
                	        nVF.setMaxWidth(Double.MAX_VALUE);
                	        grid.add(nVF, 1, line, 1, 1);
                	        GridPane.setHalignment(nVF, HPos.CENTER);
                	        GridPane.setValignment(nVF, VPos.CENTER);
                	        Rs0.setMaxWidth(Double.MAX_VALUE);
                	        grid.add(Rs0, 2, line, 1, 1);
                	        GridPane.setHalignment(Rs0, HPos.CENTER);
                	        GridPane.setValignment(Rs0, VPos.CENTER);
                	        line++;
                	        
                	        /*GENERATE FIFTH LINE*/
                			qL.setMaxWidth(Double.MAX_VALUE);
                	        grid.add(qL, 0, line, 1, 1);
                	        GridPane.setHalignment(qL, HPos.CENTER);
                	        GridPane.setValignment(qL, VPos.CENTER);
                	        qH.setMaxWidth(Double.MAX_VALUE);
                	        grid.add(qH, 1, line, 1, 1);
                	        GridPane.setHalignment(qH, HPos.CENTER);
                	        GridPane.setValignment(qH, VPos.CENTER);
                	        qx.setMaxWidth(Double.MAX_VALUE);
                	        grid.add(qx, 2, line, 1, 1);
                	        GridPane.setHalignment(qx, HPos.CENTER);
                	        GridPane.setValignment(qx, VPos.CENTER);
                	        line++;
                	        
                	        /*GENERATE SIXTH LINE*/
                	        qy.setMaxWidth(Double.MAX_VALUE);
                	        grid.add(qy, 0, line, 1, 1);
                	        GridPane.setHalignment(qy, HPos.CENTER);
                	        GridPane.setValignment(qy, VPos.CENTER);
                	        qc.setMaxWidth(Double.MAX_VALUE);
                	        grid.add(qc, 1, line, 1, 1);
                	        GridPane.setHalignment(qc, HPos.CENTER);
                	        GridPane.setValignment(qc, VPos.CENTER);
                	        phi.setMaxWidth(Double.MAX_VALUE);
                	        grid.add(phi, 2, line, 1, 1);
                	        GridPane.setHalignment(phi, HPos.CENTER);
                	        GridPane.setValignment(phi, VPos.CENTER);
                	        line++;        
                	        
                	        /*GENERATE SEVENTH LINE*/
                	        fd.setMaxWidth(Double.MAX_VALUE);
                	        grid.add(fd, 0, line, 1, 1);
                	        GridPane.setHalignment(fd, HPos.CENTER);
                	        GridPane.setValignment(fd, VPos.CENTER);
                			line++;
                	        
            			break;
                	
                		case "KHM 1":
                    
                	        Label helpLabel2 = new Label("KHM 1 Parameters");
                	        helpLabel2.setId("HelpLabel");
                	        grid.add(helpLabel2, 1, line, 1, 1);
                	        GridPane.setFillWidth(helpLabel2, true);
                	        helpLabel2.setMaxWidth(Double.MAX_VALUE);
                	        helpLabel2.setAlignment(Pos.CENTER);
                	        GridPane.setHalignment(helpLabel2, HPos.CENTER);
                	        line++;
                	        
                	        /*GENERATE FOURTH LINE*/
                			k1.setMaxWidth(Double.MAX_VALUE);
                	        grid.add(k1, 0, line, 1, 1);
                	        GridPane.setHalignment(k1, HPos.CENTER);
                	        GridPane.setValignment(k1, VPos.CENTER);
                			k2.setMaxWidth(Double.MAX_VALUE);
                	        grid.add(k2, 1, line, 1, 1);
                	        GridPane.setHalignment(k2, HPos.CENTER);
                	        GridPane.setValignment(k2, VPos.CENTER);
                			k3.setMaxWidth(Double.MAX_VALUE);
                	        grid.add(k3, 2, line, 1, 1);
                	        GridPane.setHalignment(k3, HPos.CENTER);
                	        GridPane.setValignment(k3, VPos.CENTER);
                	        line++;
                	        
                	        /*GENERATE FIFTH LINE*/
                			h1.setMaxWidth(Double.MAX_VALUE);
                	        grid.add(h1, 0, line, 1, 1);
                	        GridPane.setHalignment(h1, HPos.CENTER);
                	        GridPane.setValignment(h1, VPos.CENTER);
                			h2.setMaxWidth(Double.MAX_VALUE);
                	        grid.add(h2, 1, line, 1, 1);
                	        GridPane.setHalignment(h2, HPos.CENTER);
                	        GridPane.setValignment(h2, VPos.CENTER);
                			line++;
                	        
            			break;
            			
                	}
                	
                	switch(model2.getValue().getText()) {
                	
            		case "TNO/EAB":
            		
            	        /*ADDING HELP LABEL*/
            	        Label helpLabel = new Label("TNO/EAB Parameters");
            	        helpLabel.setId("HelpLabel");
            	        grid.add(helpLabel, 1, line, 1, 1);
            	        GridPane.setFillWidth(helpLabel, true);
            	        helpLabel.setMaxWidth(Double.MAX_VALUE);
            	        helpLabel.setAlignment(Pos.CENTER);
            	        GridPane.setHalignment(helpLabel, HPos.CENTER);
            	        line++;
            	        
            	        /*GENERATE FOURTH LINE*/
            	        Z0inf.setMaxWidth(Double.MAX_VALUE);
            	        grid.add(Z0inf, 0, line, 1, 1);
            	        GridPane.setHalignment(Z0inf, HPos.CENTER);
            	        GridPane.setValignment(Z0inf, VPos.CENTER);
            	        nVF.setMaxWidth(Double.MAX_VALUE);
            	        grid.add(nVF, 1, line, 1, 1);
            	        GridPane.setHalignment(nVF, HPos.CENTER);
            	        GridPane.setValignment(nVF, VPos.CENTER);
            	        Rs0.setMaxWidth(Double.MAX_VALUE);
            	        grid.add(Rs0, 2, line, 1, 1);
            	        GridPane.setHalignment(Rs0, HPos.CENTER);
            	        GridPane.setValignment(Rs0, VPos.CENTER);
            	        line++;
            	        
            	        /*GENERATE FIFTH LINE*/
            			qL.setMaxWidth(Double.MAX_VALUE);
            	        grid.add(qL, 0, line, 1, 1);
            	        GridPane.setHalignment(qL, HPos.CENTER);
            	        GridPane.setValignment(qL, VPos.CENTER);
            	        qH.setMaxWidth(Double.MAX_VALUE);
            	        grid.add(qH, 1, line, 1, 1);
            	        GridPane.setHalignment(qH, HPos.CENTER);
            	        GridPane.setValignment(qH, VPos.CENTER);
            	        qx.setMaxWidth(Double.MAX_VALUE);
            	        grid.add(qx, 2, line, 1, 1);
            	        GridPane.setHalignment(qx, HPos.CENTER);
            	        GridPane.setValignment(qx, VPos.CENTER);
            	        line++;
            	        
            	        /*GENERATE SIXTH LINE*/
            	        qy.setMaxWidth(Double.MAX_VALUE);
            	        grid.add(qy, 0, line, 1, 1);
            	        GridPane.setHalignment(qy, HPos.CENTER);
            	        GridPane.setValignment(qy, VPos.CENTER);
            	        qc.setMaxWidth(Double.MAX_VALUE);
            	        grid.add(qc, 1, line, 1, 1);
            	        GridPane.setHalignment(qc, HPos.CENTER);
            	        GridPane.setValignment(qc, VPos.CENTER);
            	        phi.setMaxWidth(Double.MAX_VALUE);
            	        grid.add(phi, 2, line, 1, 1);
            	        GridPane.setHalignment(phi, HPos.CENTER);
            	        GridPane.setValignment(phi, VPos.CENTER);
            	        line++;        
            	        
            	        /*GENERATE SEVENTH LINE*/
            	        fd.setMaxWidth(Double.MAX_VALUE);
            	        grid.add(fd, 0, line, 1, 1);
            	        GridPane.setHalignment(fd, HPos.CENTER);
            	        GridPane.setValignment(fd, VPos.CENTER);
            			line++;
            			
        			break;
            	
            		case "KHM 1":
                
            	        Label helpLabel2 = new Label("KHM 1 Parameters");
            	        helpLabel2.setId("HelpLabel");
            	        grid.add(helpLabel2, 1, line, 1, 1);
            	        GridPane.setFillWidth(helpLabel2, true);
            	        helpLabel2.setMaxWidth(Double.MAX_VALUE);
            	        helpLabel2.setAlignment(Pos.CENTER);
            	        GridPane.setHalignment(helpLabel2, HPos.CENTER);
            	        line++;
            	        
            	        /*ADDING TO GRID*/
            			k1.setMaxWidth(Double.MAX_VALUE);
            	        grid.add(k1, 0, line, 1, 1);
            	        GridPane.setHalignment(k1, HPos.CENTER);
            	        GridPane.setValignment(k1, VPos.CENTER);
            			k2.setMaxWidth(Double.MAX_VALUE);
            	        grid.add(k2, 1, line, 1, 1);
            	        GridPane.setHalignment(k2, HPos.CENTER);
            	        GridPane.setValignment(k2, VPos.CENTER);
            			k3.setMaxWidth(Double.MAX_VALUE);
            	        grid.add(k3, 2, line, 1, 1);
            	        GridPane.setHalignment(k3, HPos.CENTER);
            	        GridPane.setValignment(k3, VPos.CENTER);
            	        line++;
            	        
            	        /*ADDING TO GRID*/
            			h1.setMaxWidth(Double.MAX_VALUE);
            	        grid.add(h1, 0, line, 1, 1);
            	        GridPane.setHalignment(h1, HPos.CENTER);
            	        GridPane.setValignment(h1, VPos.CENTER);
            			h2.setMaxWidth(Double.MAX_VALUE);
            	        grid.add(h2, 1, line, 1, 1);
            	        GridPane.setHalignment(h2, HPos.CENTER);
            	        GridPane.setValignment(h2, VPos.CENTER);
            			line++;
            	        
        			break;
        			
            	}
                	
        	        Label helpLabel = new Label("Input the parameters of both models");
        	        helpLabel.setId("HelpLabel");
        	        grid.add(helpLabel, 1, line, 1, 1);
        	        GridPane.setFillWidth(helpLabel, true);
        	        helpLabel.setMaxWidth(Double.MAX_VALUE);
        	        helpLabel.setAlignment(Pos.CENTER);
        	        GridPane.setHalignment(helpLabel, HPos.CENTER);
        	        line++;
                	
        	        /*GENERATE CONFIGS INPUTS*/
                    JFXTextField cableLength = new JFXTextField();
                    cableLength.setLabelFloat(true);
                    cableLength.setPromptText("Cable Length");

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
                
        	        /*ADDING TO GRID*/
            		cableLength.setMaxWidth(Double.MAX_VALUE);
                    grid.add(cableLength, 0, line, 1, 1);
                    GridPane.setHalignment(cableLength, HPos.CENTER);
                    GridPane.setValignment(cableLength, VPos.CENTER);
                    frequency.setMaxWidth(Double.MAX_VALUE);
                    grid.add(frequency, 1, line, 1, 1);
                    GridPane.setHalignment(frequency, HPos.CENTER);
                    GridPane.setValignment(frequency, VPos.CENTER);
                    scale.setMaxWidth(Double.MAX_VALUE);
                    grid.add(scale, 2, line, 1, 1);
                    GridPane.setHalignment(scale, HPos.CENTER);
                    GridPane.setValignment(scale, VPos.CENTER);
                    line++;
                    
        	        /*ADDING TO GRID*/
                    parameterCalc.setMaxWidth(Double.MAX_VALUE);
                    grid.add(parameterCalc, 0, line, 1, 1);
                    GridPane.setHalignment(parameterCalc, HPos.CENTER);
                    GridPane.setValignment(parameterCalc, VPos.CENTER);
                	line++;

                	/*GENERATE CALCULATE BUTTON*/
			        Region iconCalc = GlyphsStack.create().add(
			        		GlyphsBuilder.create(FontAwesomeIcon.class)
			        			.icon(FontAwesomeIconName.CALCULATOR)
			        			.style("-fx-fill: white;")
			        			.size("1em")
			        			.build()
			        		);

			        Button calc = new Button("Calculate",iconCalc);
			        calc.setId("calculate");
			        calc.setMaxWidth(Double.MAX_VALUE);
			        /*SEND DATA TO CALCULATE*/
			        calc.setOnMousePressed(new EventHandler<MouseEvent>() {
			            public void handle(MouseEvent me) {
			                
		            	/*CALL FUNCTION TO PLOT DATA*/

			            	/*FOR COMPARISON BETWEEN TNO AND KHM1*/
			            	if(model1.getValue().getText().contains("TNO/EAB") && model2.getValue().getText().contains("KHM 1")) {
			            					            		
			            		Vector<String> headings = new Vector<String>();

			            		Vector<Double> k1_value = new Vector<Double>();
			                    Vector<Double> k2_value = new Vector<Double>();
			                    Vector<Double> k3_value = new Vector<Double>();
			                    Vector<Double> h1_value = new Vector<Double>();
			                    Vector<Double> h2_value = new Vector<Double>();

			                    Vector<Double> Z0inf_value = new Vector<Double>();
			                    Vector<Double> nVF_value = new Vector<Double>();
			                    Vector<Double> Rs0_value = new Vector<Double>();
			                    Vector<Double> qL_value = new Vector<Double>();
			                    Vector<Double> qH_value = new Vector<Double>();
			                    Vector<Double> qx_value = new Vector<Double>();
			                    Vector<Double> qy_value = new Vector<Double>();
			                    Vector<Double> qc_value = new Vector<Double>();
			                    Vector<Double> phi_value = new Vector<Double>();
			                    Vector<Double> fd_value = new Vector<Double>();
			                    
			                    double minF;
			                    double maxF;
			                    double cableLength_value;
			                    String axisScale;
			                    String parameter;
			                    
			                    /*VALIDATE INFO'S*/
			                    try{
			                    	
			                    	if(cableTypes.getValue() == null) {
			                    		
			                    		headings.add("Custom");
			                    		
			                    	}else {

			                    		headings.add(cableTypes.getValue().getText());

			                    	}
			                    	
			                    	Z0inf_value.add(Double.parseDouble(Z0inf.getText()));
			                    	nVF_value.add(Double.parseDouble(nVF.getText()));
			                    	Rs0_value.add(Double.parseDouble(Rs0.getText()));
			                    	qL_value.add(Double.parseDouble(qL.getText()));
			                    	qH_value.add(Double.parseDouble(qH.getText()));
			                    	qx_value.add(Double.parseDouble(qx.getText()));
			                    	qy_value.add(Double.parseDouble(qy.getText()));
			                    	qc_value.add(Double.parseDouble(qc.getText()));
			                    	phi_value.add(Double.parseDouble(phi.getText()));
			                    	fd_value.add(Double.parseDouble(fd.getText()));
			                    	
			                        k1_value.add(Double.parseDouble(k1.getText()));
			                        k2_value.add(Double.parseDouble(k2.getText()));
			                        k3_value.add(Double.parseDouble(k3.getText()));
			                        h1_value.add(Double.parseDouble(h1.getText()));
			                        h2_value.add(Double.parseDouble(h2.getText()));
			                    	
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
			                    DiffBetweenModelsController.generateDiffKHM1TNO(headings, Z0inf_value,nVF_value,Rs0_value,qL_value,
			                    		qH_value,qx_value,qy_value,qc_value,phi_value,fd_value,k1_value,k2_value,k3_value,
			                    		h1_value,h2_value,cableLength_value,minF,maxF,51.75e3,axisScale,parameter, false);
			            		
			            	}if(model2.getValue().getText().contains("TNO/EAB") && model1.getValue().getText().contains("KHM 1")) {
											            		
			            		Vector<String> headings = new Vector<String>();

			            		Vector<Double> k1_value = new Vector<Double>();
			            		Vector<Double> k2_value = new Vector<Double>();
			            		Vector<Double> k3_value = new Vector<Double>();
			            		Vector<Double> h1_value = new Vector<Double>();
			            		Vector<Double> h2_value = new Vector<Double>();
								
			            		Vector<Double> Z0inf_value = new Vector<Double>();
			            		Vector<Double> nVF_value = new Vector<Double>();
			            		Vector<Double> Rs0_value = new Vector<Double>();
			            		Vector<Double> qL_value = new Vector<Double>();
			            		Vector<Double> qH_value = new Vector<Double>();
			            		Vector<Double> qx_value = new Vector<Double>();
			            		Vector<Double> qy_value = new Vector<Double>();
			            		Vector<Double> qc_value = new Vector<Double>();
								Vector<Double> phi_value = new Vector<Double>();
								Vector<Double> fd_value = new Vector<Double>();
								
								double minF;
								double maxF;
								double cableLength_value;
								String axisScale;
								String parameter;
								
								/*VALIDATE INFO'S*/
								try{
									
			                    	if(cableTypes.getValue() == null) {
			                    		
			                    		headings.add("Custom");
			                    		
			                    	}else {

			                    		headings.add(cableTypes.getValue().getText());

			                    	}
									
									Z0inf_value.add(Double.parseDouble(Z0inf.getText()));
									nVF_value.add(Double.parseDouble(nVF.getText()));
									Rs0_value.add(Double.parseDouble(Rs0.getText()));
									qL_value.add(Double.parseDouble(qL.getText()));
									qH_value.add(Double.parseDouble(qH.getText()));
									qx_value.add(Double.parseDouble(qx.getText()));
									qy_value.add(Double.parseDouble(qy.getText()));
									qc_value.add(Double.parseDouble(qc.getText()));
									phi_value.add(Double.parseDouble(phi.getText()));
									fd_value.add(Double.parseDouble(fd.getText()));
									
								    k1_value.add(Double.parseDouble(k1.getText()));
								    k2_value.add(Double.parseDouble(k2.getText()));
								    k3_value.add(Double.parseDouble(k3.getText()));
								    h1_value.add(Double.parseDouble(h1.getText()));
								    h2_value.add(Double.parseDouble(h2.getText()));
									
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
								DiffBetweenModelsController.generateDiffKHM1TNO(headings, Z0inf_value,nVF_value,Rs0_value,qL_value,
										qH_value,qx_value,qy_value,qc_value,phi_value,fd_value,k1_value,k2_value,k3_value,
										h1_value,h2_value,cableLength_value,minF,maxF,51.75e3,axisScale,parameter, true);
								
								}
			            	
			            	
			            }
			        });
        	        calc.setMaxWidth(Double.MAX_VALUE);
        	        grid.add(calc, 1, line, 1, 1);
        	        GridPane.setHalignment(calc, HPos.CENTER);
        	        line++;
			        
                	/*GENERATE BACK BUTTON*/
        	        back.setMaxWidth(Double.MAX_VALUE);
        	        grid.add(back, 1, line, 1, 1);
        	        GridPane.setHalignment(back, HPos.CENTER);
            	        	        
            	}
            	
            }
        });

        
        
        /*GENERATE FIFTH LINE*/
        back.setMaxWidth(Double.MAX_VALUE);
        grid.add(back, 1, 4, 1, 1);
        GridPane.setHalignment(back, HPos.CENTER);
        
        grid.setAlignment(Pos.CENTER);
        
        /*CREATE SCENE*/
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
                
        return scrollPane;
    
    }

	
}
