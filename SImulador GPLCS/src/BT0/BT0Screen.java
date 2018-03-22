package BT0;

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
import javafx.scene.control.TextField;

public class BT0Screen {

    /*GET WINDOW FOR BT0 CABLE SYNTHESIS*/
    public static ScrollPane getBT0Screen(Stage primaryStage){
    
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
        String ApplicationName = "British Telecom Model 0 Cable Synthesis";
        Label label = new Label(ApplicationName);
        label.setId("LabelScreen");
        label.setAlignment(Pos.CENTER);

        /*CREATE HELP LABELS PREDEFINED*/
        Label labelPred = new Label("Select the type of cable to generate the curves: ");
        labelPred.setId("HelpLabel");
        labelPred.setAlignment(Pos.CENTER);
                
        /*CREATE THE INPUTS PARAMETERS*/        
        JFXTextField Roc = new JFXTextField();
        Roc.setLabelFloat(true);
        Roc.setPromptText("R0c");

        JFXTextField ac = new JFXTextField();
        ac.setLabelFloat(true);
        ac.setPromptText("ac");

        JFXTextField L0 = new JFXTextField();
        L0.setLabelFloat(true);
        L0.setPromptText("L0");
        
        JFXTextField Linf = new JFXTextField();
        Linf.setLabelFloat(true);
        Linf.setPromptText("L∞");

        JFXTextField fm = new JFXTextField();
        fm.setLabelFloat(true);
        fm.setPromptText("fm");

        JFXTextField Nb = new JFXTextField();
        Nb.setLabelFloat(true);
        Nb.setPromptText("Nb");

        JFXTextField g0 = new JFXTextField();
        g0.setLabelFloat(true);
        g0.setPromptText("g0");
        
        JFXTextField Nge = new JFXTextField();
        Nge.setLabelFloat(true);
        Nge.setPromptText("Nge");

        JFXTextField Cinf = new JFXTextField();
        Cinf.setLabelFloat(true);
        Cinf.setPromptText("C∞");

        JFXTextField C0 = new JFXTextField();
        C0.setLabelFloat(true);
        C0.setPromptText("C0");

        JFXTextField Nce = new JFXTextField();
        Nce.setLabelFloat(true);
        Nce.setPromptText("Nce");        
        
        /*CREATE COMBOBOX AND LABEL OF PREDEFINED CABLES*/
        JFXComboBox<Label> cableTypes = new JFXComboBox<Label>();
        cableTypes.getItems().add(new Label("Custom"));
        cableTypes.getItems().add(new Label("B05a"));
        cableTypes.getItems().add(new Label("BT_dw1"));
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
            	
	        		case "BT_dw1":
	        			descriptionCable.setText("Cable Aerial Drop-wire No 55 (CAD55), a typical copper line used in the UK");
	        			Roc.setText("65.32");
	    				ac.setText("2.7152831e-3");
	    				L0.setText("0.884242e-3");
	    				Linf.setText("800.587e-6");
	    				fm.setText("263371");
	    				Nb.setText("1.30698");
	    				g0.setText("855e-9");
	    				Nge.setText("0.746");
	    				C0.setText("46.5668e-9");
	    				Cinf.setText("28.0166e-9");
	    				Nce.setText("0.117439");
	    				Roc.setDisable(true);
	    				ac.setDisable(true);
	    				L0.setDisable(true);
	    				Linf.setDisable(true);
	    				fm.setDisable(true);
	    				Nb.setDisable(true);
	    				g0.setDisable(true);
	    				Nge.setDisable(true);
	    				C0.setDisable(true);
	    				Cinf.setDisable(true);
	    				Nce.setDisable(true);
					break;

            		case "B05a":
            			descriptionCable.setText("Cable Aerial Drop-wire No 55 (CAD55), a typical copper line used in the UK");
            			Roc.setText("187.0831");
        				ac.setText("0.0457");
        				L0.setText("6.5553e-004");
        				Linf.setText("5.0973e-004");
        				fm.setText("8.1241e+005");
        				Nb.setText("1.0142");
        				g0.setText("1.0486e-010");
        				Nge.setText("1.1500");
        				C0.setText("-6.9514e-011");
        				Cinf.setText("4.5578e-008");
        				Nce.setText("-0.1500");
        				Roc.setDisable(true);
        				ac.setDisable(true);
        				L0.setDisable(true);
        				Linf.setDisable(true);
        				fm.setDisable(true);
        				Nb.setDisable(true);
        				g0.setDisable(true);
        				Nge.setDisable(true);
        				C0.setDisable(true);
        				Cinf.setDisable(true);
        				Nce.setDisable(true);
    				break;
            		default:
        				descriptionCable.setText("Enter manually the parameters or choose at side a predefined cable type");
        				Roc.setText("");
        				ac.setText("");
        				L0.setText("");
        				Linf.setText("");
        				fm.setText("");
        				Nb.setText("");
        				g0.setText("");
        				Nge.setText("");
        				C0.setText("");
        				Cinf.setText("");
        				Nce.setText("");
        				Roc.setDisable(false);
        				ac.setDisable(false);
        				L0.setDisable(false);
        				Linf.setDisable(false);
        				fm.setDisable(false);
        				Nb.setDisable(false);
        				g0.setDisable(false);
        				Nge.setDisable(false);
        				C0.setDisable(false);
        				Cinf.setDisable(false);
        				Nce.setDisable(false);
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
                
                Vector<String> headings = new Vector<String>();
                
                Vector<Double> Roc_value = new Vector<Double>();
                Vector<Double> ac_value = new Vector<Double>();
                Vector<Double> L0_value  = new Vector<Double>();
                Vector<Double> Linf_value= new Vector<Double>();
                Vector<Double> fm_value  = new Vector<Double>();
                Vector<Double> Nb_value  = new Vector<Double>();
                Vector<Double> g0_value  = new Vector<Double>();
                Vector<Double> Nge_value = new Vector<Double>();
                Vector<Double> C0_value  = new Vector<Double>();
                Vector<Double> Cinf_value= new Vector<Double>();
                Vector<Double> Nce_value = new Vector<Double>();

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

                    Roc_value.add(Double.parseDouble(Roc.getText()));
                    ac_value.add(Double.parseDouble(ac.getText()));
                    L0_value.add(Double.parseDouble(L0.getText()));
                    Linf_value.add(Double.parseDouble(Linf.getText()));
                    fm_value.add(Double.parseDouble(fm.getText()));
                    Nb_value.add(Double.parseDouble(Nb.getText()));
                    g0_value.add(Double.parseDouble(g0.getText()));
                    Nge_value.add(Double.parseDouble(Nge.getText()));
                    C0_value.add(Double.parseDouble(C0.getText()));
                    Cinf_value.add(Double.parseDouble(Cinf.getText()));
                    Nce_value.add(Double.parseDouble(Nce.getText()));

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
                BT0Controller.generateGraphs(headings, Roc_value, ac_value,L0_value,Linf_value,fm_value,Nb_value,g0_value,Nge_value,C0_value,Cinf_value,Nce_value, cableLength_value, minF, maxF, 51.75e3, axisScale, parameter);
                
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
        
        int line = 0;
        
        /*ADDING LINE*/
        grid.add(label, 0, line, 3, 1);
        GridPane.setHalignment(label, HPos.CENTER);
        line++;
        
		/*ADDING LINE*/
		grid.add(labelPred, 0, line, 3, 1);
		GridPane.setHalignment(labelPred, HPos.CENTER);
		GridPane.setValignment(labelPred, VPos.CENTER);
		line++;
		
		/*ADDING LINE*/
		cableTypes.setMaxWidth(Double.MAX_VALUE);
		grid.add(cableTypes, 0, line, 1, 1);
		GridPane.setHalignment(cableTypes, HPos.CENTER);
		GridPane.setValignment(cableTypes, VPos.CENTER);		
		descriptionCable.setMaxWidth(Double.MAX_VALUE);
		grid.add(descriptionCable, 1, line, 2, 1);
		GridPane.setHalignment(descriptionCable, HPos.CENTER);
		GridPane.setValignment(descriptionCable, VPos.CENTER);		
		line++;
		
		/*ADDING LINE*/		
		Roc.setMaxWidth(Double.MAX_VALUE);
		grid.add(Roc, 0, line, 3, 1);
		GridPane.setHalignment(Roc, HPos.CENTER);
		GridPane.setValignment(Roc, VPos.CENTER);
		ac.setMaxWidth(Double.MAX_VALUE);
		grid.add(ac, 1, line, 3, 1);
		GridPane.setHalignment(ac, HPos.CENTER);
		GridPane.setValignment(ac, VPos.CENTER);
		L0.setMaxWidth(Double.MAX_VALUE);
		grid.add(L0, 2, line, 3, 1);
		GridPane.setHalignment(L0, HPos.CENTER);
		GridPane.setValignment(L0, VPos.CENTER);
		line++;
		
		/*ADDING LINE*/
		Linf.setMaxWidth(Double.MAX_VALUE);
		grid.add(Linf, 0, line, 3, 1);
		GridPane.setHalignment(Linf, HPos.CENTER);
		GridPane.setValignment(Linf, VPos.CENTER);
		fm.setMaxWidth(Double.MAX_VALUE);
		grid.add(fm, 1, line, 3, 1);
		GridPane.setHalignment(fm, HPos.CENTER);
		GridPane.setValignment(fm, VPos.CENTER);
		Nb.setMaxWidth(Double.MAX_VALUE);
		grid.add(Nb, 2, line, 3, 1);
		GridPane.setHalignment(Nb, HPos.CENTER);
		GridPane.setValignment(Nb, VPos.CENTER);
		line++;

		/*ADDING LINE*/
		g0.setMaxWidth(Double.MAX_VALUE);
		grid.add(g0, 0, line, 3, 1);
		GridPane.setHalignment(g0, HPos.CENTER);
		GridPane.setValignment(g0, VPos.CENTER);
		Nge.setMaxWidth(Double.MAX_VALUE);
		grid.add(Nge, 1, line, 3, 1);
		GridPane.setHalignment(Nge, HPos.CENTER);
		GridPane.setValignment(Nge, VPos.CENTER);
		Cinf.setMaxWidth(Double.MAX_VALUE);
		grid.add(Cinf, 2, line, 3, 1);
		GridPane.setHalignment(Cinf, HPos.CENTER);
		GridPane.setValignment(Cinf, VPos.CENTER);
		line++;

		/*ADDING LINE*/
		C0.setMaxWidth(Double.MAX_VALUE);
		grid.add(C0, 0, line, 3, 1);
		GridPane.setHalignment(C0, HPos.CENTER);
		GridPane.setValignment(C0, VPos.CENTER);
		Nce.setMaxWidth(Double.MAX_VALUE);
		grid.add(Nce, 1, line, 3, 1);
		GridPane.setHalignment(Nce, HPos.CENTER);
		GridPane.setValignment(Nce, VPos.CENTER);
		cableLength.setMaxWidth(Double.MAX_VALUE);
		grid.add(cableLength, 2, line, 3, 1);
		GridPane.setHalignment(cableLength, HPos.CENTER);
		GridPane.setValignment(cableLength, VPos.CENTER);
		line++;
        
        /*ADDING LINE*/
        frequency.setMaxWidth(Double.MAX_VALUE);
        grid.add(frequency, 0, line, 1, 1);
        GridPane.setHalignment(frequency, HPos.CENTER);
        GridPane.setValignment(frequency, VPos.CENTER);
        scale.setMaxWidth(Double.MAX_VALUE);
        grid.add(scale, 1, line, 1, 1);
        GridPane.setHalignment(scale, HPos.CENTER);
        GridPane.setValignment(scale, VPos.CENTER);
        parameterCalc.setMaxWidth(Double.MAX_VALUE);
        grid.add(parameterCalc, 2, line, 1, 1);
        GridPane.setHalignment(parameterCalc, HPos.CENTER);
        GridPane.setValignment(parameterCalc, VPos.CENTER);
		line++;
        
        /*ADDING LINE*/
        calculate.setMaxWidth(Double.MAX_VALUE);
        grid.add(calculate, 1, line, 1, 1);
        GridPane.setHalignment(calculate, HPos.CENTER);
        GridPane.setValignment(calculate, VPos.CENTER);
        line++;
        
        /*ADDING LINE*/
        back.setMaxWidth(Double.MAX_VALUE);
        grid.add(back, 1, line, 1, 1);
        GridPane.setHalignment(back, HPos.CENTER);
        GridPane.setValignment(back, VPos.CENTER);
        grid.setAlignment(Pos.CENTER);
        line++;
        
        /*CREATE SCENE*/
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
                
        return scrollPane;
    
    }

	
}
