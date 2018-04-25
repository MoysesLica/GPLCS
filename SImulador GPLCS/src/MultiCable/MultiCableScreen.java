package MultiCable;

import CableSynthesis.CableSynthesisScreen;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import BT0.BT0;
import BT0.BT0Controller;
import BT0.BT0Screen;
import GPLCS.SimuladorGPLCS;
import KHM1.KHM1;
import KHM1.KHM1Controller;
import TNO_EAB.TNO_EAB;
import TNO_EAB.TNO_EABController;
import TNO_EAB.TNO_EABScreen;
import TransmissionLine.GenericCableModel;
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
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MultiCableScreen {

	public static ScrollPane getMultiCableScreen(Stage primaryStage) {
	
		/*CREATE THE GRID*/
        GridPane grid = new GridPane();
        grid.setVgap(25);
        grid.setHgap(10);
        grid.setPadding(new Insets(30,30,30,30));
        for (int i = 0; i < 3; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(33.3);
            grid.getColumnConstraints().add(column);
        }
        
        Vector<Integer> linha = new Vector<Integer>();
        
        /*CREATE THE LABEL OF SCREEN*/
        String ApplicationName = "Multi Cable Transfer Function";
        Label label = new Label(ApplicationName);
        label.setId("title");
        label.setAlignment(Pos.CENTER);

        /*CREATE HELP LABEL*/
        Label helpLabel = new Label("Select one of models above to start the calculation of transfer function to multiples cables:");
        helpLabel.setId("HelpLabel");
        helpLabel.setAlignment(Pos.CENTER);

        /*CREATE 3 BUTTONS*/
        JFXButton buttonKHM1 = new JFXButton("KHM 1");
        buttonKHM1.setId("KHM1");
        buttonKHM1.setFocusTraversable(false);
        buttonKHM1.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	grid.getChildren().remove(grid.getChildren().size() - 1);
            	grid.add(MultiCableScreen.getMultiCableScreen(primaryStage, "KHM1"), 0, linha.get(0), 3, 1);
            	String css1 = MultiCableScreen.class.getResource("MultiCableScreen.css").toExternalForm(); 
            	primaryStage.getScene().getStylesheets().clear();
            	primaryStage.getScene().getStylesheets().addAll(css1);
            }
        });
        
        JFXButton buttonTNOEAB = new JFXButton("TNO/EAB");
        buttonTNOEAB.setId("TNOEAB");
        buttonTNOEAB.setFocusTraversable(false);
        buttonTNOEAB.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {

            	grid.getChildren().remove(grid.getChildren().size() - 1);
            	grid.add(MultiCableScreen.getMultiCableScreen(primaryStage, "TNO/EAB"), 0, linha.get(0), 3, 1);
            	String css1 = MultiCableScreen.class.getResource("MultiCableScreen.css").toExternalForm(); 
            	primaryStage.getScene().getStylesheets().clear();
            	primaryStage.getScene().getStylesheets().addAll(css1);

            }
        });
        
        JFXButton buttonBT0    = new JFXButton("BT0");
        buttonBT0.setId("BT0");
        buttonBT0.setFocusTraversable(false);
        buttonBT0.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {

            	grid.getChildren().remove(grid.getChildren().size() - 1);
            	grid.add(MultiCableScreen.getMultiCableScreen(primaryStage, "BT0"), 0, linha.get(0), 3, 1);
            	String css1 = MultiCableScreen.class.getResource("MultiCableScreen.css").toExternalForm(); 
            	primaryStage.getScene().getStylesheets().clear();
            	primaryStage.getScene().getStylesheets().addAll(css1);

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
            	primaryStage.getScene().setRoot(CableSynthesisScreen.getCableSynthesisScene(primaryStage));
            	String css = CableSynthesisScreen.class.getResource("CableSynthesisScreen.css").toExternalForm(); 
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
        line++;
        
        grid.add(new GridPane(), 0, line, 3, 1);

        linha.add(line);
        grid.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
                
        return scrollPane;

	}
	
	public static GridPane getMultiCableScreen(Stage primaryStage, String transmissionLineModel) {
	
		/*SCREEN WIDTH AND HEIGHT*/
	    int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
	    int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;

        /*CREATE THE GRID*/
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25,25,25,25));
        for (int i = 0; i < 5; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(20);
            grid.getColumnConstraints().add(column);
        }

        /*CREATE THE LABEL OF SCREEN*/
        String ApplicationName = "";
        String ModelDescription = "";
        
        if(transmissionLineModel.contains("KHM1")) {
        	ApplicationName = "Generate Transfer Function to Multiples Cables";
            ModelDescription = "Transmission Line Model: KH Model 1";
        }else if(transmissionLineModel.contains("BT0")) {
        	ApplicationName = "Generate Transfer Function to Multiples Cables";
            ModelDescription = "Transmission Line Model: British Telecom Model 0";
        }else if(transmissionLineModel.contains("TNO/EAB")) {
        	ApplicationName = "Generate Transfer Function to Multiples Cables";
            ModelDescription = "Transmission Line Model: TNO/EAB Model";
        }
        
        Label label = new Label(ApplicationName);
        label.setId("LabelScreen");
        label.setAlignment(Pos.CENTER);

        Label labelModel = new Label(ModelDescription);
        labelModel.setId("LabelScreen");
        labelModel.setAlignment(Pos.CENTER);

        
        /*CREATE HELP LABELS PREDEFINED*/
        Label labelTopology = new Label("Select the topology to generate the curves: ");
        labelTopology.setId("HelpLabel");
        labelTopology.setAlignment(Pos.CENTER);
        labelTopology.setWrapText(true);
        
        Group networkRegion = new Group();

        /*CREATE NETWORK TOPOLOGY REGION*/
        Rectangle networkRegionBorder = new Rectangle( 0, 0, screenWidth*90, screenHeight*55);
        networkRegionBorder.setStroke(Color.BLACK);
        networkRegionBorder.setFill(Color.TRANSPARENT);
        networkRegionBorder.setStrokeWidth(1);
        
        networkRegion.getChildren().add(networkRegionBorder);
        networkRegionBorder.toFront();
        
        Vector<GenericCableModel> config = new Vector<GenericCableModel>();
		Vector<Text> cableText = new Vector<Text>();
		Vector<Line> cableSegment = new Vector<Line>();
        
        /*CREATE THE INPUTS CONFIG*/
        JFXComboBox<Label> frequency = new JFXComboBox<Label>();
        frequency.getItems().add(new Label("2.2MHz - 106MHz"));
        frequency.getItems().add(new Label("2.2MHz - 212MHz"));
        frequency.getItems().add(new Label("2.2MHz - 424MHz"));
        frequency.getItems().add(new Label("2.2MHz - 848MHz"));
        frequency.getItems().add(new Label("Custom"));
        frequency.setPromptText("Frequency Band");
        
        JFXComboBox<Label> topology = new JFXComboBox<Label>();
        
        MultiCableScreen.configComboBoxConfigurations(topology, networkRegion, config, cableText, cableSegment, networkRegionBorder, primaryStage, transmissionLineModel);
        
        JFXTextField loadImpedance = new JFXTextField();
        loadImpedance.setLabelFloat(true);
        loadImpedance.setPromptText("Zl (Load Impedance)");

        JFXTextField sourceImpedance = new JFXTextField();
        sourceImpedance.setLabelFloat(true);
        sourceImpedance.setPromptText("Zs (Source Impedance)");
        
        JFXTextField minF = new JFXTextField();
        minF.setLabelFloat(true);
        minF.setPromptText("Minimum Frequency (in MHz)");

        JFXTextField maxF = new JFXTextField();
        maxF.setLabelFloat(true);
        maxF.setPromptText("Maximum Frequency (in MHz)");

        JFXTextField step = new JFXTextField();
        step.setLabelFloat(true);
        step.setPromptText("Step (in MHz)");
        
        JFXComboBox<Label> scale = new JFXComboBox<Label>();
        scale.getItems().add(new Label("Logarithmic"));
        scale.getItems().add(new Label("Linear"));
        scale.setPromptText("Scale");
        scale.setMaxWidth(Double.MAX_VALUE);
        
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
                
            	/**********************************************************************/
            	/*FUNCTIONS TO CALCULATE THE TRANSFER FUNCTION*/
            	
            	boolean haveNull = false;
            	
            	for(int i = 0; i < config.size(); i++)
            		if(config.get(i) == null)
            			haveNull = true;
            	
            	if(haveNull) {
            		
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Please configure all cable segments before calculate the transfer function!");
					alert.showAndWait();
            	
					return;
					
            	}
            		
        		/*SEND ALL DATA TO GENERATE THE GRAPH*/
        		Vector<GenericCableModel> data = new Vector<GenericCableModel>();
        		Vector<Double> bridgeTap = new Vector<Double>();
        		
        		if(topology.getValue() == null) {
        			
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Select at least a type of topology!");
					alert.showAndWait();
					return;
        			
        		}
        		
        		switch(topology.getValue().getText().split(",")[0]) {
            	
            		case "Topology 1":
            			data.add(config.get(0));
            			data.add(config.get(1));
            			break;

            		case "Topology 2":
            			data.add(config.get(0));
        				data.add(config.get(2));
        				data.add(config.get(1));
        				bridgeTap.add(new Double(1));
        				break;

            		case "Topology 3":
        				data.add(config.get(0));
        				data.add(config.get(1));
        				data.add(config.get(2));
            			break;

            		case "Topology 4":
        				data.add(config.get(0));
        				data.add(config.get(3));
        				bridgeTap.add(new Double(3));
        				data.add(config.get(1));
        				data.add(config.get(2));
            			break;

            		case "Topology 5":
        				data.add(config.get(0));
        				data.add(config.get(3));
        				bridgeTap.add(new Double(3));
        				data.add(config.get(1));
        				data.add(config.get(4));
        				bridgeTap.add(new Double(4));
        				data.add(config.get(2));
            			break;
            			
            	}
        		
                double minF_value;
                double maxF_value;
                double step_value;
                String axisScale;
                double Zl_value;
                double Zs_value;
        		
                try{
                    
                    if(frequency.getValue().getText().contains("Custom")) {

                    	minF_value = Double.parseDouble(minF.getText()) * 1e6;
                        maxF_value = Double.parseDouble(maxF.getText()) * 1e6;
                    	step_value = Double.parseDouble(step.getText()) * 1e6;

                    }else {                        	

                    	minF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
                        maxF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
                    	step_value = 51.75e3;
                        
                    }
                    
                	Zl_value = Double.parseDouble(loadImpedance.getText());
                	Zs_value = Double.parseDouble(sourceImpedance.getText());
                    axisScale = scale.getValue().getText();

                }catch(Exception e){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error, please fill correctly the inputs before continue!");
                    alert.setContentText(e.toString());
                    alert.showAndWait();
                    return;
                }

        		MultiCableController.generateTransferFunction(data, bridgeTap, minF_value, maxF_value, step_value, axisScale, Zl_value, Zs_value);
        		            	
            	/**********************************************************************/
                
            }
        });        
        
        /*****************************************************************************************************************/
        /*INSERT THE ELEMENTS IN GRID*/
        
        int line = 0;
        
        label.setMaxWidth(Double.MAX_VALUE);
        grid.add(label, 0, line, 5, 1);
        GridPane.setHalignment(label, HPos.CENTER);
		GridPane.setValignment(label, VPos.CENTER);
		line++;
		
		labelModel.setMaxWidth(Double.MAX_VALUE);
        grid.add(labelModel, 0, line, 5, 1);
        GridPane.setHalignment(labelModel, HPos.CENTER);
		GridPane.setValignment(labelModel, VPos.CENTER);
		line++;
		
		labelTopology.setMaxWidth(Double.MAX_VALUE);
        grid.add(labelTopology, 0, line, 5, 1);
        GridPane.setHalignment(labelTopology, HPos.CENTER);
		GridPane.setValignment(labelTopology, VPos.CENTER);
		line++;
		
		topology.setMaxWidth(Double.MAX_VALUE);
        grid.add(topology, 0, line, 5, 1);
        GridPane.setHalignment(topology, HPos.CENTER);
		GridPane.setValignment(topology, VPos.CENTER);
		line++;
		
		frequency.setMaxWidth(Double.MAX_VALUE);
        grid.add(frequency, 0, line, 1, 1);
        GridPane.setHalignment(frequency, HPos.CENTER);
		GridPane.setValignment(frequency, VPos.CENTER);
		loadImpedance.setMaxWidth(Double.MAX_VALUE);
        grid.add(loadImpedance, 1, line, 1, 1);
        GridPane.setHalignment(loadImpedance, HPos.CENTER);
		GridPane.setValignment(loadImpedance, VPos.CENTER);
		sourceImpedance.setMaxWidth(Double.MAX_VALUE);
        grid.add(sourceImpedance, 2, line, 1, 1);
        GridPane.setHalignment(sourceImpedance, HPos.CENTER);
		GridPane.setValignment(sourceImpedance, VPos.CENTER);
				
        final int lineFrequencyCustom = line + 1;
        
		frequency.valueProperty().addListener(new ChangeListener<Label>() {
            @Override
            public void changed(ObservableValue<? extends Label> observable, Label oldValue, Label newValue) {

            	if(newValue.getText().contains("Custom")) {
            		
            		/*ADDING LINE*/
                    minF.setMaxWidth(Double.MAX_VALUE);
                    grid.add(minF, 1, lineFrequencyCustom, 1, 1);
                    GridPane.setHalignment(minF, HPos.CENTER);
                    GridPane.setValignment(minF, VPos.CENTER);
                    
                    maxF.setMaxWidth(Double.MAX_VALUE);
                    grid.add(maxF, 2, lineFrequencyCustom, 1, 1);
                    GridPane.setHalignment(maxF, HPos.CENTER);
                    GridPane.setValignment(maxF, VPos.CENTER);
                    
                    step.setMaxWidth(Double.MAX_VALUE);
                    grid.add(step, 3, lineFrequencyCustom, 1, 1);
                    GridPane.setHalignment(step, HPos.CENTER);
                    GridPane.setValignment(step, VPos.CENTER);
            		
            	}else {
            		
            		grid.getChildren().remove(minF);
            		grid.getChildren().remove(maxF);
            		grid.getChildren().remove(step);
            		
            	}
            	
            }
        });
		        		
		scale.setMaxWidth(Double.MAX_VALUE);
        grid.add(scale, 3, line, 1, 1);
        GridPane.setHalignment(scale, HPos.CENTER);
		GridPane.setValignment(scale, VPos.CENTER);
		calculate.setMaxWidth(Double.MAX_VALUE);
        grid.add(calculate, 4, line, 1, 1);
        GridPane.setHalignment(calculate, HPos.CENTER);
		GridPane.setValignment(calculate, VPos.CENTER);
		line++;
		
		grid.add(networkRegion, 0, line, 5, 1);
        GridPane.setHalignment(networkRegion, HPos.CENTER);
		GridPane.setValignment(networkRegion, VPos.CENTER);
		line++;
		
		grid.setAlignment(Pos.CENTER);
		
        /*****************************************************************************************************************/
		                
        return grid;
	
	}
	
	public static void generateConfiguration1(
			Group networkRegion, Rectangle networkRegionBorder, Stage primaryStage,
	        Vector<GenericCableModel> config, Vector<Text> cableText, Vector<Line> cableSegment, String transmissionLineModel
			) {
			
		double Xs = networkRegion.getBoundsInParent().getMinX();
		double Ys = networkRegion.getBoundsInParent().getMinY();
		double Xe = networkRegion.getBoundsInParent().getMaxX();
		double Ye = networkRegion.getBoundsInParent().getMaxY();
		
		int tamLine = 150;
		int startX = 100;
		
		/*GENERATE THE SEGMENTS*/
		double[] posLine1 = {startX, (Ye - Ys)/2, startX + tamLine, (Ye - Ys)/2};
		double[] posLine2 = {posLine1[2], (Ye - Ys)/2, posLine1[2] + tamLine, (Ye - Ys)/2};

		MultiCableScreen.generateStandardLine(
				posLine1, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, 0, transmissionLineModel);
		MultiCableScreen.generateStandardLine(
				posLine2, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, 0, transmissionLineModel);
		
		/*GENERATE THE POINTS*/
		double[] posPoint1 = { posLine1[0] , posLine1[1] };
		double[] posPoint2 = { posLine2[0] , posLine2[1] };
		double[] posPoint3 = { posLine2[2] , posLine2[3] };

		MultiCableScreen.generateStandardPoint(posPoint1, Color.RED, networkRegion, "Start Point");
		MultiCableScreen.generateStandardPoint(posPoint2, Color.BLACK, networkRegion, "");
		MultiCableScreen.generateStandardPoint(posPoint3, Color.PURPLE, networkRegion, "End Point");
		
	}

	public static void generateConfiguration2(
			Group networkRegion, Rectangle networkRegionBorder, Stage primaryStage,
	        Vector<GenericCableModel> config, Vector<Text> cableText, Vector<Line> cableSegment, String transmissionLineModel
			) {
			
		double Xs = networkRegion.getBoundsInParent().getMinX();
		double Ys = networkRegion.getBoundsInParent().getMinY();
		double Xe = networkRegion.getBoundsInParent().getMaxX();
		double Ye = networkRegion.getBoundsInParent().getMaxY();
		
		int tamLine = 150;
		int startX = 100;
		
		/*GENERATE THE SEGMENTS*/
		double[] posLine1 = {startX, (Ye - Ys)/2, startX + tamLine, (Ye - Ys)/2};
		double[] posLine2 = {posLine1[2], (Ye - Ys)/2, posLine1[2] + tamLine, (Ye - Ys)/2};
		double[] posLine3 = {posLine1[2], (Ye - Ys)/2, posLine1[2] , (Ye - Ys)/2 + tamLine};

		MultiCableScreen.generateStandardLine(
				posLine1, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, 0, transmissionLineModel);
		MultiCableScreen.generateStandardLine(
				posLine2, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, 0, transmissionLineModel);
		MultiCableScreen.generateStandardLineWithOffsetOnText(
				posLine3, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, -90, transmissionLineModel);
		
		/*GENERATE THE POINTS*/
		double[] posPoint1 = { posLine1[0] , posLine1[1] };
		double[] posPoint2 = { posLine2[0] , posLine2[1] };
		double[] posPoint3 = { posLine2[2] , posLine2[3] };
		double[] posPoint4 = { posLine3[2] , posLine3[3] };

		MultiCableScreen.generateStandardPoint(posPoint1, Color.RED, networkRegion, "Start Point");
		MultiCableScreen.generateStandardPoint(posPoint2, Color.BLACK, networkRegion, "");
		MultiCableScreen.generateStandardPoint(posPoint3, Color.PURPLE, networkRegion, "End Point");
		MultiCableScreen.generateStandardPoint(posPoint4, Color.GOLD, networkRegion, "Bridge Tap");
		
	}

	public static void generateConfiguration3(
			Group networkRegion, Rectangle networkRegionBorder, Stage primaryStage,
	        Vector<GenericCableModel> config, Vector<Text> cableText, Vector<Line> cableSegment, String transmissionLineModel
			) {
			
		double Xs = networkRegion.getBoundsInParent().getMinX();
		double Ys = networkRegion.getBoundsInParent().getMinY();
		double Xe = networkRegion.getBoundsInParent().getMaxX();
		double Ye = networkRegion.getBoundsInParent().getMaxY();
		
		int tamLine = 150;
		int startX = 100;
		
		/*GENERATE THE SEGMENTS*/
		double[] posLine1 = {startX, (Ye - Ys)/2, startX + tamLine, (Ye - Ys)/2};
		double[] posLine2 = {posLine1[2], (Ye - Ys)/2, posLine1[2] + tamLine, (Ye - Ys)/2};
		double[] posLine3 = {posLine2[2], (Ye - Ys)/2, posLine2[2] + tamLine, (Ye - Ys)/2};

		MultiCableScreen.generateStandardLine(
				posLine1, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, 0, transmissionLineModel);
		MultiCableScreen.generateStandardLine(
				posLine2, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, 0, transmissionLineModel);
		MultiCableScreen.generateStandardLine(
				posLine3, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, 0, transmissionLineModel);
		
		/*GENERATE THE POINTS*/
		double[] posPoint1 = { posLine1[0] , posLine1[1] };
		double[] posPoint2 = { posLine2[0] , posLine2[1] };
		double[] posPoint3 = { posLine3[0] , posLine3[1] };
		double[] posPoint4 = { posLine3[2] , posLine3[3] };

		MultiCableScreen.generateStandardPoint(posPoint1, Color.RED, networkRegion, "Start Point");
		MultiCableScreen.generateStandardPoint(posPoint2, Color.BLACK, networkRegion, "");
		MultiCableScreen.generateStandardPoint(posPoint3, Color.BLACK, networkRegion, "");		
		MultiCableScreen.generateStandardPoint(posPoint4, Color.PURPLE, networkRegion, "End Point");
		
	}
	
	public static void generateConfiguration4(
			Group networkRegion, Rectangle networkRegionBorder, Stage primaryStage,
	        Vector<GenericCableModel> config, Vector<Text> cableText, Vector<Line> cableSegment, String transmissionLineModel
			) {
			
		double Xs = networkRegion.getBoundsInParent().getMinX();
		double Ys = networkRegion.getBoundsInParent().getMinY();
		double Xe = networkRegion.getBoundsInParent().getMaxX();
		double Ye = networkRegion.getBoundsInParent().getMaxY();
		
		int tamLine = 150;
		int startX = 100;
		
		/*GENERATE THE SEGMENTS*/
		double[] posLine1 = {startX, (Ye - Ys)/2, startX + tamLine, (Ye - Ys)/2};
		double[] posLine2 = {posLine1[2], (Ye - Ys)/2, posLine1[2] + tamLine, (Ye - Ys)/2};
		double[] posLine3 = {posLine2[2], (Ye - Ys)/2, posLine2[2] + tamLine, (Ye - Ys)/2};

		double[] posBridge1 = {posLine1[2], (Ye - Ys)/2, posLine1[2] , (Ye - Ys)/2 + tamLine};
		
		MultiCableScreen.generateStandardLine(
				posLine1, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, 0, transmissionLineModel);
		MultiCableScreen.generateStandardLine(
				posLine2, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, 0, transmissionLineModel);
		MultiCableScreen.generateStandardLine(
				posLine3, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, 0, transmissionLineModel);
		
		MultiCableScreen.generateStandardLineWithOffsetOnText(
				posBridge1, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, -90, transmissionLineModel);
		
		
		/*GENERATE THE POINTS*/
		double[] posPoint1 = { posLine1[0] , posLine1[1] };
		double[] posPoint2 = { posLine2[0] , posLine2[1] };
		double[] posPoint3 = { posLine3[0] , posLine3[1] };
		double[] posPoint4 = { posLine3[2] , posLine3[3] };
		double[] posPoint5 = { posBridge1[2] , posBridge1[3] };

		MultiCableScreen.generateStandardPoint(posPoint1, Color.RED, networkRegion, "Start Point");
		MultiCableScreen.generateStandardPoint(posPoint2, Color.BLACK, networkRegion, "");
		MultiCableScreen.generateStandardPoint(posPoint3, Color.BLACK, networkRegion, "");		
		MultiCableScreen.generateStandardPoint(posPoint4, Color.PURPLE, networkRegion, "End Point");
		MultiCableScreen.generateStandardPoint(posPoint5, Color.GOLD, networkRegion, "Bridge Tap");
		
	}
	
	public static void generateConfiguration5(
			Group networkRegion, Rectangle networkRegionBorder, Stage primaryStage,
	        Vector<GenericCableModel> config, Vector<Text> cableText, Vector<Line> cableSegment, String transmissionLineModel
			) {
			
		double Xs = networkRegion.getBoundsInParent().getMinX();
		double Ys = networkRegion.getBoundsInParent().getMinY();
		double Xe = networkRegion.getBoundsInParent().getMaxX();
		double Ye = networkRegion.getBoundsInParent().getMaxY();
		
		int tamLine = 150;
		int startX = 100;
		
		/*GENERATE THE SEGMENTS*/
		double[] posLine1 = {startX, (Ye - Ys)/2, startX + tamLine, (Ye - Ys)/2};
		double[] posLine2 = {posLine1[2], (Ye - Ys)/2, posLine1[2] + tamLine, (Ye - Ys)/2};
		double[] posLine3 = {posLine2[2], (Ye - Ys)/2, posLine2[2] + tamLine, (Ye - Ys)/2};

		double[] posBridge1 = {posLine1[2], (Ye - Ys)/2, posLine1[2] , (Ye - Ys)/2 + tamLine};
		double[] posBridge2 = {posLine2[2], (Ye - Ys)/2, posLine2[2] , (Ye - Ys)/2 + tamLine};
		
		MultiCableScreen.generateStandardLine(
				posLine1, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, 0, transmissionLineModel);
		MultiCableScreen.generateStandardLine(
				posLine2, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, 0, transmissionLineModel);
		MultiCableScreen.generateStandardLine(
				posLine3, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, 0, transmissionLineModel);
		
		MultiCableScreen.generateStandardLineWithOffsetOnText(
				posBridge1, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, -90, transmissionLineModel);
		MultiCableScreen.generateStandardLineWithOffsetOnText(
				posBridge2, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, -90, transmissionLineModel);
		
		
		/*GENERATE THE POINTS*/
		double[] posPoint1 = { posLine1[0] , posLine1[1] };
		double[] posPoint2 = { posLine2[0] , posLine2[1] };
		double[] posPoint3 = { posLine3[0] , posLine3[1] };
		double[] posPoint4 = { posLine3[2] , posLine3[3] };
		double[] posPoint5 = { posBridge1[2] , posBridge1[3] };
		double[] posPoint6 = { posBridge2[2] , posBridge2[3] };

		MultiCableScreen.generateStandardPoint(posPoint1, Color.RED, networkRegion, "Start Point");
		MultiCableScreen.generateStandardPoint(posPoint2, Color.BLACK, networkRegion, "");
		MultiCableScreen.generateStandardPoint(posPoint3, Color.BLACK, networkRegion, "");		
		MultiCableScreen.generateStandardPoint(posPoint4, Color.PURPLE, networkRegion, "End Point");
		MultiCableScreen.generateStandardPoint(posPoint5, Color.GOLD, networkRegion, "Bridge Tap");
		MultiCableScreen.generateStandardPoint(posPoint6, Color.GOLD, networkRegion, "Bridge Tap");
		
	}
	
	public static void configComboBoxConfigurations(JFXComboBox<Label> topology, Group networkRegion, Vector<GenericCableModel> config, Vector<Text> cableText, Vector<Line> cableSegment, Rectangle networkRegionBorder, Stage primaryStage, String transmissionLineModel) {
		
        topology.getItems().add(new Label("Topology 1, two segments"));
        topology.getItems().add(new Label("Topology 2, two segments with 1 bridge tap"));
        topology.getItems().add(new Label("Topology 3, three segments"));
        topology.getItems().add(new Label("Topology 4, three segments with 1 bridge tap"));
        topology.getItems().add(new Label("Topology 5, three segments with 2 bridge tap"));
        topology.setPromptText("Select the topology");
        topology.setId("comboBox");
        
        /*DRAW DIFERENT TOPOLOGIES*/
        topology.valueProperty().addListener(new ChangeListener<Label>() {
            @Override
            public void changed(ObservableValue<? extends Label> observable, Label oldValue, Label newValue) {

            	/*CLEAN THE VARS*/
            	networkRegion.getChildren().clear();
            	config.clear();
            	cableText.clear();
            	cableSegment.clear();
            	
            	networkRegion.getChildren().add(networkRegionBorder);
            	
            	switch(newValue.getText().split(",")[0]) {
            	
            		case "Topology 1":
            			MultiCableScreen.generateConfiguration1(
            					networkRegion, networkRegionBorder, primaryStage, config, cableText, cableSegment, transmissionLineModel);
            			break;

            		case "Topology 2":
            			MultiCableScreen.generateConfiguration2(
            					networkRegion, networkRegionBorder, primaryStage, config, cableText, cableSegment, transmissionLineModel);
            			break;

            		case "Topology 3":
            			MultiCableScreen.generateConfiguration3(
            					networkRegion, networkRegionBorder, primaryStage, config, cableText, cableSegment, transmissionLineModel);
            			break;

            		case "Topology 4":
            			MultiCableScreen.generateConfiguration4(
            					networkRegion, networkRegionBorder, primaryStage, config, cableText, cableSegment, transmissionLineModel);
            			break;

            		case "Topology 5":
            			MultiCableScreen.generateConfiguration5(
            					networkRegion, networkRegionBorder, primaryStage, config, cableText, cableSegment, transmissionLineModel);
            			break;

            	}
            	
            }
        });
		
	}
	
	public static void generateStandardLine(
			double[] pos, Color color, Stage primaryStage, Group networkRegion,
			Vector<GenericCableModel> config, Vector<Text> cableText, Vector<Line> cableSegment, double angle, String transmissionLineModel
			) {

		int standardLineStroke = 5;

		config.add(null);
		
		Line newSegment = new Line(pos[0],pos[1],pos[2],pos[3]);
		newSegment.setStroke(color);
		newSegment.setStrokeWidth(standardLineStroke);
		cableSegment.add(newSegment);
        newSegment.toBack();
        networkRegion.getChildren().add(newSegment);
		        
        /*ADD TEXT OF CABLE*/
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setRadius(2.0f);
        ds.setColor(Color.BLUE);
        
        /*CREATE LABEL TO FIRST CABLE(START POINT)*/
        Text textCable = new Text("Id Segment = "+(cableText.size())+"\nUnconfigured");        	        	
        
        textCable.setEffect(ds);
        textCable.setCache(true);
        textCable.setStyle(
        		"    -fx-font: 12px Monospaced;\r\n" + 
        		"    -fx-stroke-width: 1;"
        		);
        
        textCable.setFill(Color.RED);

        textCable.setRotate(angle);
        
        textCable.applyCss(); 
                
        textCable.setX((pos[0] + pos[2])/2 - textCable.getLayoutBounds().getWidth()/2);
        textCable.setY((pos[1] + pos[3])/2 - 35);        
        
        cableText.add(textCable);
        networkRegion.getChildren().add(textCable);
        
        /*****************************************************************/
        
        newSegment.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            		primaryStage.getScene().setCursor(Cursor.HAND);
            }
        });

        newSegment.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            		primaryStage.getScene().setCursor(Cursor.DEFAULT);
            }
        });
        
        /*ON DOUBLE CLICK GENERATE MODAL TO CONFIG SEGMENT*/
        newSegment.setOnMouseClicked(new EventHandler<MouseEvent>() {

        	public void handle(MouseEvent event) {
        		
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    if(event.getClickCount() == 2){

                    	MultiCableScreen.generateModal(primaryStage, cableText, config, transmissionLineModel, cableSegment.indexOf(newSegment));
                    
                    }

                }
                
            }
        	
        });

        /*****************************************************************/
        
	}

	public static void generateStandardLineWithOffsetOnText(
			double[] pos, Color color, Stage primaryStage, Group networkRegion,
			Vector<GenericCableModel> config, Vector<Text> cableText, Vector<Line> cableSegment, double angle, String transmissionLineModel
			) {

		int standardLineStroke = 5;
		
		config.add(null);

		Line newSegment = new Line(pos[0],pos[1],pos[2],pos[3]);
		newSegment.setStroke(color);
		newSegment.setStrokeWidth(standardLineStroke);
		cableSegment.add(newSegment);
        newSegment.toBack();
        networkRegion.getChildren().add(newSegment);
		        
        /*ADD TEXT OF CABLE*/
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setRadius(2.0f);
        ds.setColor(Color.BLUE);
        
        /*CREATE LABEL TO FIRST CABLE(START POINT)*/
        Text textCable = new Text("Id Segment = "+(cableText.size())+"\nUnconfigured");        	        	
        
        textCable.setEffect(ds);
        textCable.setCache(true);
        textCable.setStyle(
        		"    -fx-font: 12px Monospaced;\r\n" + 
        		"    -fx-stroke-width: 1;"
        		);
        
        textCable.setFill(Color.RED);

        textCable.setRotate(angle);
        
        textCable.applyCss(); 
                
        textCable.setX((pos[0] + pos[2])/2 - 80);
        textCable.setY((pos[1] + pos[3])/2);        
        
        cableText.add(textCable);
        networkRegion.getChildren().add(textCable);
        
        /*****************************************************************/
        
        newSegment.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            		primaryStage.getScene().setCursor(Cursor.HAND);
            }
        });

        newSegment.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            		primaryStage.getScene().setCursor(Cursor.DEFAULT);
            }
        });
        
        /*ON DOUBLE CLICK GENERATE MODAL TO CONFIG SEGMENT*/
        newSegment.setOnMouseClicked(new EventHandler<MouseEvent>() {

        	public void handle(MouseEvent event) {
        		
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    if(event.getClickCount() == 2){

                    	MultiCableScreen.generateModal(primaryStage, cableText, config, transmissionLineModel, cableSegment.indexOf(newSegment));
                    
                    }

                }
                
            }
        	
        });

        /*****************************************************************/
        
	}

	public static void generateModal(Stage primaryStage, Vector<Text> text, Vector<GenericCableModel> config, String cableModel, int position) {
		
    	Stage dialog = new Stage();
		    	
        GridPane grid = new GridPane();
        grid.setVgap(25);
        grid.setHgap(10);
        grid.setPadding(new Insets(25,25,25,25));

        for (int i = 0; i < 3; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(33.3);
            grid.getColumnConstraints().add(column);
        }
        
		/*CREATE THE CONTROLS*/
			/*TO BT0*/
			if(cableModel.contains("KHM1")) {
		        /*CREATE THE GRID*/
		        
		        /*CREATE THE LABEL OF SCREEN*/
		        String ApplicationName = "KH Model 1 Multi Cable Transfer Function Cable Configuration";
		        Label label = new Label(ApplicationName);
		        label.setId("LabelScreen");
		        label.setAlignment(Pos.CENTER);
		        
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
		        			break;
		            	}
		            	
		            }
		        });

		        JFXTextField cableLength = new JFXTextField();
		        cableLength.setLabelFloat(true);
		        cableLength.setPromptText("Cable Length");
		        
		        /*GENERATE CALC BUTTON*/
		        Region calcIcon = GlyphsStack.create().add(
		        		GlyphsBuilder.create(FontAwesomeIcon.class)
		        			.icon(FontAwesomeIconName.SAVE)
		        			.style("-fx-fill: white;")
		        			.size("1em")
		        			.build()
		        		);        

		        Button calculate = new Button("Save Cable Configuration", calcIcon);
		        calculate.setId("calculate");
		        /*SET BUTTON ONCLICK FUNCTION*/
		        calculate.setOnMousePressed(new EventHandler<MouseEvent>() {
		            public void handle(MouseEvent me) {
		                
		                double k1_value ;
		                double k2_value  ;
		                double k3_value  ;
		                double h1_value;
		                double h2_value  ;

		                double cableLength_value;
		                
		                /*VALIDATE INFO'S*/
		                try{
		                	
		                    k1_value = Double.parseDouble(k1.getText());
		                    k2_value = Double.parseDouble(k2.getText());
		                    k3_value = Double.parseDouble(k3.getText());
		                    h1_value = Double.parseDouble(h1.getText());
		                    h2_value = Double.parseDouble(h2.getText());

		                    cableLength_value = Double.parseDouble(cableLength.getText());
		                    
		                }catch(Exception e){
		                    Alert alert = new Alert(AlertType.ERROR);
		                    alert.setTitle("Error");
		                    alert.setHeaderText("Error, please fill correctly the inputs before continue!");
		                    alert.setContentText(e.toString());
		                    alert.showAndWait();
		                    return;
		                }
		                                
		                /*ADD TO CONFIG ARRAY*/
		                
		                config.set(position, (GenericCableModel) new KHM1(k1_value, k2_value, k3_value, 
		                		h1_value, h2_value, cableLength_value)         	
		    			);
		                
		                /*ALTER TEXT OF CABLE*/		                
		                text.get(position).setText(text.get(position).getText().replace("Unconfigured", "Configured"));		                	
		                
		                dialog.close();
		                
		           }
		            
		        });
		        
		        /*ADDING ALL ELEMENTS TO GRID*/
		        int line = 0;
		        
		        /*ADDING LINE*/
		        grid.add(label, 0, line, 3, 1);
		        GridPane.setHalignment(label, HPos.CENTER);
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
				
				/*ADDING LINE*/
				h1.setMaxWidth(Double.MAX_VALUE);
				grid.add(h1, 0, line, 1, 1);
				GridPane.setHalignment(h1, HPos.CENTER);
				GridPane.setValignment(h1, VPos.CENTER);
				h2.setMaxWidth(Double.MAX_VALUE);
				grid.add(h2, 1, line, 1, 1);
				GridPane.setHalignment(h2, HPos.CENTER);
				GridPane.setValignment(h2, VPos.CENTER);
				cableLength.setMaxWidth(Double.MAX_VALUE);
				grid.add(cableLength, 2, line, 1, 1);
				GridPane.setHalignment(cableLength, HPos.CENTER);
				GridPane.setValignment(cableLength, VPos.CENTER);
				line++;
		        		        				
		        /*ADDING LINE*/
		        calculate.setMaxWidth(Double.MAX_VALUE/2);
		        grid.add(calculate, 0, line, 3, 1);
		        GridPane.setHalignment(calculate, HPos.CENTER);
		        GridPane.setValignment(calculate, VPos.CENTER);
		        line++;
		        
		        grid.setAlignment(Pos.CENTER);
		        			
			}else if(cableModel.contains("BT0")) {
				/*TO BT0*/

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
		        
		        /*GENERATE CALC BUTTON*/
		        Region calcIcon = GlyphsStack.create().add(
		        		GlyphsBuilder.create(FontAwesomeIcon.class)
		        			.icon(FontAwesomeIconName.CALCULATOR)
		        			.style("-fx-fill: white;")
		        			.size("1em")
		        			.build()
		        		);        
		        Button calculate = new Button("Save Cable Configuration", calcIcon);
		        calculate.setId("calculate");
		        /*SET BUTTON ONCLICK FUNCTION*/
		        calculate.setOnMousePressed(new EventHandler<MouseEvent>() {
		            public void handle(MouseEvent me) {
		                
		                Vector<String> headings = new Vector<String>();
		                
		                double Roc_value ;
		                double ac_value  ;
		                double L0_value  ;
		                double Linf_value;
		                double fm_value  ;
		                double Nb_value  ;
		                double g0_value  ;
		                double Nge_value ;
		                double C0_value  ;
		                double Cinf_value;
		                double Nce_value ;

		                double cableLength_value;
		                
		                /*VALIDATE INFO'S*/
		                try{
		                	
		                	if(cableTypes.getValue() == null) {
		                		
		                		headings.add("Custom");
		                		
		                	}else {
		                		
		                		headings.add(cableTypes.getValue().getText());
		                		
		                	}

		                    Roc_value = Double.parseDouble(Roc.getText());
		                    ac_value = Double.parseDouble(ac.getText());
		                    L0_value = Double.parseDouble(L0.getText());
		                    Linf_value = Double.parseDouble(Linf.getText());
		                    fm_value = Double.parseDouble(fm.getText());
		                    Nb_value = Double.parseDouble(Nb.getText());
		                    g0_value = Double.parseDouble(g0.getText());
		                    Nge_value = Double.parseDouble(Nge.getText());
		                    C0_value = Double.parseDouble(C0.getText());
		                    Cinf_value = Double.parseDouble(Cinf.getText());
		                    Nce_value = Double.parseDouble(Nce.getText());

		                    cableLength_value = Double.parseDouble(cableLength.getText());
		                    
		                }catch(Exception e){
		                    Alert alert = new Alert(AlertType.ERROR);
		                    alert.setTitle("Error");
		                    alert.setHeaderText("Error, please fill correctly the inputs before continue!");
		                    alert.setContentText(e.toString());
		                    alert.showAndWait();
		                    return;
		                }
		                                
		                /*ADD TO CONFIG ARRAY*/
		                		                
		                config.set(position, (GenericCableModel) new BT0(Roc_value, ac_value, L0_value, Linf_value, fm_value, Nb_value, 
		                		g0_value, Nge_value, C0_value, Cinf_value, Nce_value, cableLength_value)         	
		    			);
		                
		                /*ALTER TEXT OF CABLE*/		                
		                text.get(position).setText(text.get(position).getText().replace("Unconfigured", "Configured"));		                	
		                
		                dialog.close();		                
		           
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
					grid.add(Roc, 0, line, 1, 1);
					GridPane.setHalignment(Roc, HPos.CENTER);
					GridPane.setValignment(Roc, VPos.CENTER);
					ac.setMaxWidth(Double.MAX_VALUE);
					grid.add(ac, 1, line, 1, 1);
					GridPane.setHalignment(ac, HPos.CENTER);
					GridPane.setValignment(ac, VPos.CENTER);
					L0.setMaxWidth(Double.MAX_VALUE);
					grid.add(L0, 2, line, 1, 1);
					GridPane.setHalignment(L0, HPos.CENTER);
					GridPane.setValignment(L0, VPos.CENTER);
					line++;
					
					/*ADDING LINE*/
					Linf.setMaxWidth(Double.MAX_VALUE);
					grid.add(Linf, 0, line, 1, 1);
					GridPane.setHalignment(Linf, HPos.CENTER);
					GridPane.setValignment(Linf, VPos.CENTER);
					fm.setMaxWidth(Double.MAX_VALUE);
					grid.add(fm, 1, line, 1, 1);
					GridPane.setHalignment(fm, HPos.CENTER);
					GridPane.setValignment(fm, VPos.CENTER);
					Nb.setMaxWidth(Double.MAX_VALUE);
					grid.add(Nb, 2, line, 1, 1);
					GridPane.setHalignment(Nb, HPos.CENTER);
					GridPane.setValignment(Nb, VPos.CENTER);
					line++;
	
					/*ADDING LINE*/
					g0.setMaxWidth(Double.MAX_VALUE);
					grid.add(g0, 0, line, 1, 1);
					GridPane.setHalignment(g0, HPos.CENTER);
					GridPane.setValignment(g0, VPos.CENTER);
					Nge.setMaxWidth(Double.MAX_VALUE);
					grid.add(Nge, 1, line, 1, 1);
					GridPane.setHalignment(Nge, HPos.CENTER);
					GridPane.setValignment(Nge, VPos.CENTER);
					Cinf.setMaxWidth(Double.MAX_VALUE);
					grid.add(Cinf, 2, line, 1, 1);
					GridPane.setHalignment(Cinf, HPos.CENTER);
					GridPane.setValignment(Cinf, VPos.CENTER);
					line++;
	
					/*ADDING LINE*/
					C0.setMaxWidth(Double.MAX_VALUE);
					grid.add(C0, 0, line, 1, 1);
					GridPane.setHalignment(C0, HPos.CENTER);
					GridPane.setValignment(C0, VPos.CENTER);
					Nce.setMaxWidth(Double.MAX_VALUE);
					grid.add(Nce, 1, line, 1, 1);
					GridPane.setHalignment(Nce, HPos.CENTER);
					GridPane.setValignment(Nce, VPos.CENTER);
					cableLength.setMaxWidth(Double.MAX_VALUE);
					grid.add(cableLength, 2, line, 1, 1);
					GridPane.setHalignment(cableLength, HPos.CENTER);
					GridPane.setValignment(cableLength, VPos.CENTER);
					line++;
					
			        /*ADDING LINE*/
			        calculate.setMaxWidth(Double.MAX_VALUE);
			        grid.add(calculate, 1, line, 1, 1);
			        GridPane.setHalignment(calculate, HPos.CENTER);
			        GridPane.setValignment(calculate, VPos.CENTER);
			        line++;
			        
			        grid.setAlignment(Pos.CENTER);
			        			        
				}else if(cableModel.contains("TNO/EAB")) {
					/*TO TNO/EAB*/

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
			                
			        JFXTextField cableLength = new JFXTextField();
			        cableLength.setLabelFloat(true);
			        cableLength.setPromptText("Cable Length");

			        /*GENERATE CALC BUTTON*/
			        Region calcIcon = GlyphsStack.create().add(
			        		GlyphsBuilder.create(FontAwesomeIcon.class)
			        			.icon(FontAwesomeIconName.CALCULATOR)
			        			.style("-fx-fill: white;")
			        			.size("1em")
			        			.build()
			        		);        
			        Button calculate = new Button("Save Cable Configuration", calcIcon);
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
			                double cableLength_value;
			                
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
			                    
			                }catch(Exception e){
			                    Alert alert = new Alert(AlertType.ERROR);
			                    alert.setTitle("Error");
			                    alert.setHeaderText("Error, please fill correctly the inputs before continue!");
			                    alert.setContentText(e.toString());
			                    alert.showAndWait();
			                    return;
			                }
			                
			                /*ADD TO CONFIG ARRAY*/
    		                
			                config.set(position, (GenericCableModel) new TNO_EAB(Z0inf_value, nVF_value, Rs0_value, qL_value, qH_value, qx_value, 
			                		qy_value, qc_value, phi_value, fd_value, cableLength_value)         	
			    			);
			                
			                /*ALTER TEXT OF CABLE*/		                
			                text.get(position).setText(text.get(position).getText().replace("Unconfigured", "Configured"));		                	
			                
			                dialog.close();		       
			                
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
			        
			        /*ADDING LINE*/
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

			        /*ADDING LINE*/
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
			                
			        /*ADDING LINE*/
			        fd.setMaxWidth(Double.MAX_VALUE);
			        grid.add(fd, 0, line, 1, 1);
			        GridPane.setHalignment(fd, HPos.CENTER);
			        GridPane.setValignment(fd, VPos.CENTER);
			        cableLength.setMaxWidth(Double.MAX_VALUE);
			        grid.add(cableLength, 1, line, 1, 1);
			        GridPane.setHalignment(cableLength, HPos.CENTER);
			        GridPane.setValignment(cableLength, VPos.CENTER);
			        line++;
			        
			        /*ADDING LINE*/
			        calculate.setMaxWidth(Double.MAX_VALUE);
			        grid.add(calculate, 1, line, 1, 1);
			        GridPane.setHalignment(calculate, HPos.CENTER);
			        GridPane.setValignment(calculate, VPos.CENTER);
			        line++;
			        			        
			        grid.setAlignment(Pos.CENTER);
				        			        
					}
		
        /*CREATE WINDOW ICON AND TITLE*/
        try {
	        Image image = new Image(SimuladorGPLCS.class.getResourceAsStream("logo_ufpa.png"));
	        dialog.getIcons().add(image);
	        dialog.setTitle("Cable Segment Configuration");
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	dialog.setScene(new Scene(grid));
    	dialog.getScene().getStylesheets().add(MultiCableScreen.class.getResource("MultiCableScreen.css").toExternalForm());
    	
    	dialog.initOwner(primaryStage);
    	dialog.initModality(Modality.APPLICATION_MODAL); 
    	dialog.showAndWait();
    	
	}

	public static void generateStandardPoint(
			double[] pos,  Color color, Group networkRegion, String text) {
		
		double sizeCircle = 7; 
		
		/*GENERATE POINT*/
		Circle circle = new Circle(pos[0], pos[1], sizeCircle);
		
		/*GENERATE FIRST POINT*/
		circle.setFill(color);
		
        /*CREATE LABEL TO FIRST CABLE(START POINT)*/
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setRadius(2.0f);
        ds.setColor(Color.BLUE);

		Text textCable = new Text(text);        	
        
        textCable.setEffect(ds);
        textCable.setCache(true);
        textCable.setStyle(
        		"    -fx-font: 12px Monospaced;\r\n" + 
        		"    -fx-stroke-width: 1;"
        		);
        textCable.setFill(Color.RED);
        textCable.applyCss(); 
        
        textCable.setX(pos[0] - textCable.getLayoutBounds().getWidth()*3/4);
        textCable.setY(pos[1] + 25);        
        
        networkRegion.getChildren().add(textCable);
		
		circle.toFront();

        networkRegion.getChildren().add(circle);

	}

	
}