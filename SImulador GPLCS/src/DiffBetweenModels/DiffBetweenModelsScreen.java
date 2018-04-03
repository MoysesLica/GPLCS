package DiffBetweenModels;

import java.io.File;
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

public class DiffBetweenModelsScreen {

    /*CREATE WINDOW FOR INPUT FILE*/
    public static ScrollPane getInputFileWindow(Stage primaryStage, String model1, String model2) {
    	
        	GridPane grid = new GridPane();
			grid.setPadding(new Insets(50, 50, 50, 50));
			grid.setVgap(20);
			grid.setHgap(20);

			ColumnConstraints col1,col2,col3;
			col1 = col2 = col3 = new ColumnConstraints();
            col1.setPercentWidth(33);
            col2.setPercentWidth(33);
            col3.setPercentWidth(33);
            grid.getColumnConstraints().add(col1);
            grid.getColumnConstraints().add(col2);
            grid.getColumnConstraints().add(col3);

			RowConstraints row1, row2, row3, row4, row5, row6;
			row1 = new RowConstraints();
			row2 = new RowConstraints();
			row3 = new RowConstraints();
			row4 = new RowConstraints();
			row5 = new RowConstraints();
			row6 = new RowConstraints();
            row1.setPercentHeight(7);
            row2.setPercentHeight(25);
            row3.setPercentHeight(7);
            row4.setPercentHeight(7);
            row5.setPercentHeight(25);
            row6.setPercentHeight(7);
            grid.getRowConstraints().addAll(row1,row2,row3,row4,row5,row6);
			

            /*CREATE INPUTS*/
						            			            
            Text help1 = new Text("File content("+model1+"): ");
			help1.setFont(Font.font("System",FontWeight.BOLD,17));

            Text help2 = new Text("File content("+model2+"): ");
			help2.setFont(Font.font("System",FontWeight.BOLD,17));

			Text labelContentFile1 = new Text();
			labelContentFile1.setFont(Font.font("Monospaced",14));
			labelContentFile1.maxWidth(Double.MAX_VALUE);

			Text labelContentFile2 = new Text();
			labelContentFile2.setFont(Font.font("Monospaced",14));
			labelContentFile2.maxWidth(Double.MAX_VALUE);

	        JFXTextField fileColumnSeparator1 = new JFXTextField();
	        fileColumnSeparator1.setLabelFloat(true);
	        fileColumnSeparator1.setPromptText("Input the column separator character");

	        JFXTextField fileColumnSeparator2 = new JFXTextField();
	        fileColumnSeparator2.setLabelFloat(true);
	        fileColumnSeparator2.setPromptText("Input the column separator character");

	        JFXTextField fileCableLength = new JFXTextField();
	        fileCableLength.setLabelFloat(true);
	        fileCableLength.setPromptText("Input the cable length");

	        JFXComboBox<Label> fileFrequency = new JFXComboBox<Label>();
	        fileFrequency.getItems().add(new Label("2.2MHz - 106MHz"));
	        fileFrequency.getItems().add(new Label("2.2MHz - 212MHz"));
	        fileFrequency.getItems().add(new Label("2.2MHz - 424MHz"));
	        fileFrequency.getItems().add(new Label("2.2MHz - 848MHz"));
	        fileFrequency.getItems().add(new Label("Custom"));
	        fileFrequency.setPromptText("Frequency Band");
	        fileFrequency.setMaxWidth(Double.MAX_VALUE);

	        JFXComboBox<Label> fileScale = new JFXComboBox<Label>();
	        fileScale.getItems().add(new Label("Logarithmic"));
	        fileScale.getItems().add(new Label("Linear"));
	        fileScale.setPromptText("Scale");
	        fileScale.setMaxWidth(Double.MAX_VALUE);
	        
	        JFXComboBox<Label> fileParameterCalc = new JFXComboBox<Label>();
	        fileParameterCalc.getItems().add(new Label("Propagation Constant"));
	        fileParameterCalc.getItems().add(new Label("Characteristic Impedance"));
	        fileParameterCalc.getItems().add(new Label("Transfer Function"));
	        fileParameterCalc.getItems().add(new Label("Primary Parameters"));
	        fileParameterCalc.setPromptText("Parameter to be Calculated");
	        fileParameterCalc.setMaxWidth(Double.MAX_VALUE);				
	        
	        /********************************************************************/
	        Region iconSeparate = GlyphsStack.create().add(
	        		GlyphsBuilder.create(FontAwesomeIcon.class)
	        			.icon(FontAwesomeIconName.CHECK)
	        			.style("-fx-fill: white;")
	        			.size("1em")
	        			.build()
	        		);	        
	        
	        JFXButton separateModel1 = new JFXButton("Check File", iconSeparate);
	        separateModel1.setId("separate");
	        separateModel1.setMaxWidth(Double.MAX_VALUE);

	        JFXButton separateModel2 = new JFXButton("Check File", iconSeparate);
	        separateModel2.setId("separate");
	        separateModel2.setMaxWidth(Double.MAX_VALUE);


	        /********************************************************************/
	        Region iconCalc = GlyphsStack.create().add(
	        		GlyphsBuilder.create(FontAwesomeIcon.class)
	        			.icon(FontAwesomeIconName.CALCULATOR)
	        			.style("-fx-fill: white;")
	        			.size("1em")
	        			.build()
	        		);

	        Button calc = new Button("Calculate",iconCalc);
	        calc.setId("calc");
	        calc.setMaxWidth(Double.MAX_VALUE);
	        

	        /********************************************************************/
	        Region iconSelect1 = GlyphsStack.create().add(
	        		GlyphsBuilder.create(FontAwesomeIcon.class)
	        			.icon(FontAwesomeIconName.UPLOAD)
	        			.style("-fx-fill: white;")
	        			.size("1em")
	        			.build()
	        		);

	        Region iconSelect2 = GlyphsStack.create().add(
	        		GlyphsBuilder.create(FontAwesomeIcon.class)
	        			.icon(FontAwesomeIconName.UPLOAD)
	        			.style("-fx-fill: white;")
	        			.size("1em")
	        			.build()
	        		);

	        Button selectFileModel1 = new Button("Select File of "+model1, iconSelect1);
	        selectFileModel1.setId("fileModel");
	        selectFileModel1.setMaxWidth(Double.MAX_VALUE);

	        Button selectFileModel2 = new Button("Select File of "+model2, iconSelect2);
	        selectFileModel2.setId("fileModel");
	        selectFileModel2.setMaxWidth(Double.MAX_VALUE);
	        
	        JFXTextField fileMinF = new JFXTextField();
	        fileMinF.setLabelFloat(true);
	        fileMinF.setPromptText("Minimum Frequency (in MHz)");

	        JFXTextField fileMaxF = new JFXTextField();
	        fileMaxF.setLabelFloat(true);
	        fileMaxF.setPromptText("Maximum Frequency (in MHz)");

	        JFXTextField fileStep = new JFXTextField();
	        fileStep.setLabelFloat(true);
	        fileStep.setPromptText("Step (in MHz)");


			ScrollPane scrollFileContent1 = new ScrollPane();
			scrollFileContent1.setContent(labelContentFile1);

			ScrollPane scrollFileContent2 = new ScrollPane();
			scrollFileContent1.setContent(labelContentFile2);

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
	            	
	            	/*COME BACK TO DIFF SCREEN*/
	            	primaryStage.getScene().setRoot(DiffBetweenModelsScreen.getDiffScreen(primaryStage));
	            	String css = DiffBetweenModelsScreen.class.getResource("DiffBetweenModelsScreen.css").toExternalForm(); 
	            	primaryStage.getScene().getStylesheets().clear();
	            	primaryStage.getScene().getStylesheets().add(css);
	            	
	            }
	        });

	        int line = 0;
			        
			/*ADDING LINE*/
			grid.add(help1, 0, line, 3, 1);
			GridPane.setHalignment(help1, HPos.CENTER);
			GridPane.setValignment(help1, VPos.CENTER);
			line++;
			
			/*ADDING LINE*/
			grid.add(scrollFileContent1, 0, line, 3, 1);
			GridPane.setHalignment(scrollFileContent1, HPos.CENTER);
			GridPane.setValignment(scrollFileContent1, VPos.CENTER);
			line++;
			
			/*ADDING LINE*/
			grid.add(selectFileModel1, 0, line, 1, 1);
			GridPane.setHalignment(selectFileModel1, HPos.CENTER);
			GridPane.setValignment(selectFileModel1, VPos.CENTER);

			grid.add(fileColumnSeparator1, 1, line, 1, 1);
			GridPane.setHalignment(fileColumnSeparator1, HPos.CENTER);
			GridPane.setValignment(fileColumnSeparator1, VPos.CENTER);

			grid.add(separateModel1, 2, line, 1, 1);
			GridPane.setHalignment(separateModel1, HPos.CENTER);
			GridPane.setValignment(separateModel1, VPos.CENTER);

			line++;

			/*ADDING LINE*/
			grid.add(help2, 0, line, 3, 1);
			GridPane.setHalignment(help2, HPos.CENTER);
			GridPane.setValignment(help2, VPos.CENTER);
			line++;
			
			/*ADDING LINE*/
			grid.add(scrollFileContent2, 0, line, 3, 1);
			GridPane.setHalignment(scrollFileContent2, HPos.CENTER);
			GridPane.setValignment(scrollFileContent2, VPos.CENTER);
			line++;
			
			/*ADDING LINE*/
			grid.add(selectFileModel2, 0, line, 1, 1);
			GridPane.setHalignment(selectFileModel2, HPos.CENTER);
			GridPane.setValignment(selectFileModel2, VPos.CENTER);

			grid.add(fileColumnSeparator2, 1, line, 1, 1);
			GridPane.setHalignment(fileColumnSeparator2, HPos.CENTER);
			GridPane.setValignment(fileColumnSeparator2, VPos.CENTER);

			grid.add(separateModel2, 2, line, 1, 1);
			GridPane.setHalignment(separateModel2, HPos.CENTER);
			GridPane.setValignment(separateModel2, VPos.CENTER);

			line++;

			/*ADDING LINE*/
			grid.add(fileCableLength, 0, line, 1, 1);
			GridPane.setHalignment(fileCableLength, HPos.CENTER);
			GridPane.setValignment(fileCableLength, VPos.CENTER);

			grid.add(fileFrequency, 1, line, 1, 1);
			GridPane.setHalignment(fileFrequency, HPos.CENTER);
			GridPane.setValignment(fileFrequency, VPos.CENTER);
						
			grid.add(fileScale, 2, line, 1, 1);
			GridPane.setHalignment(fileScale, HPos.CENTER);
			GridPane.setValignment(fileScale, VPos.CENTER);						

			line++;

			/*ADDING LINE*/
			grid.add(fileParameterCalc, 0, line, 1, 1);
			GridPane.setHalignment(fileParameterCalc, HPos.CENTER);
			GridPane.setValignment(fileParameterCalc, VPos.CENTER);						
			line++;
			
			final int lineFrequencyCustomFile = line;
			
			fileFrequency.valueProperty().addListener(new ChangeListener<Label>() {
	            @Override
	            public void changed(ObservableValue<? extends Label> observable, Label oldValue, Label newValue) {

	            	if(newValue.getText().contains("Custom")) {
	            		
	            		/*ADDING LINE*/
	                    fileMinF.setMaxWidth(Double.MAX_VALUE);
	                    grid.add(fileMinF, 0, lineFrequencyCustomFile, 1, 1);
	                    GridPane.setHalignment(fileMinF, HPos.CENTER);
	                    GridPane.setValignment(fileMinF, VPos.CENTER);
	                    
	                    fileMaxF.setMaxWidth(Double.MAX_VALUE);
	                    grid.add(fileMaxF, 1, lineFrequencyCustomFile, 1, 1);
	                    GridPane.setHalignment(fileMaxF, HPos.CENTER);
	                    GridPane.setValignment(fileMaxF, VPos.CENTER);
	                    
	                    fileStep.setMaxWidth(Double.MAX_VALUE);
	                    grid.add(fileStep, 2, lineFrequencyCustomFile, 1, 1);
	                    GridPane.setHalignment(fileStep, HPos.CENTER);
	                    GridPane.setValignment(fileStep, VPos.CENTER);
	            		
	            	}
	            	
	            }
	        });
			
			line++;
			
			/*ADDING LINE*/
			grid.add(calc, 1, line, 1, 1);
			GridPane.setHalignment(calc, HPos.CENTER);
			GridPane.setValignment(calc, VPos.CENTER);
			back.setMaxWidth(Double.MAX_VALUE);

			grid.add(back, 2, line, 1, 1);
			GridPane.setHalignment(back, HPos.CENTER);
			GridPane.setValignment(back, VPos.CENTER);

			line++;
			
			
			
			calc.setOnMousePressed(new EventHandler<MouseEvent>() {
	            public void handle(MouseEvent me) {

	            	
	            	ArrayList<ArrayList<String>> linesAndColumns1 = new ArrayList<ArrayList<String>>();
	                ArrayList<ArrayList<String>> linesAndColumns2 = new ArrayList<ArrayList<String>>();

	            	boolean error = false;

	            	/*VERIFY IF COLUMN SEPARATOR IS GIVED*/
	            	if(!fileColumnSeparator1.getText().isEmpty()) {
	            		
	                  ArrayList<String> lines = new ArrayList(Arrays.asList(labelContentFile1.getText().split("\n")));
	                  /*REMOVE EMPTY LINES*/
	                  for(int i = 0; i < lines.size(); i++)
	                	  if(lines.get(i).isEmpty())
	                		  lines.remove(i);
	                  	                  
		                  for(int i = 0; i < lines.size(); i++){
		                	  
			                  ArrayList<String> columnData = new ArrayList<String>(Arrays.asList(lines.get(i).split(Pattern.quote(fileColumnSeparator1.getText().trim()))));
			                  linesAndColumns1.add(columnData);
	                  }
	                  
		                  
	                  /*VERIFY THE NUMBER OF LINES BASED ON EACH MODEL*/
	                  if(
	                		  (linesAndColumns1.size() == 12 && model1.contains("BT0"))
	                		  ||
	                		  (linesAndColumns1.size() == 11 && model1.contains("TNO/EAB"))
	                		  ||
	                		  (linesAndColumns1.size() == 6 && model1.contains("KHM 1"))
	                		  
	                		  ) {
		                  int numberColumns = linesAndColumns1.get(0).size();
		                  for(int i = 1; i < linesAndColumns1.size(); i++) {
		                	  /*VERIFY IF HAVE THE SAME LENGTH OF COLUMNS*/
		                	  if(linesAndColumns1.get(i).size() != numberColumns) {
		                		  error = true;
			                	  System.out.println("cols");
		                	  }
		                	  /*VERIFY IF ALL CELLS AFTER 1 IS NUMBERS*/
		                	  for(int j = 0; j < linesAndColumns1.get(i).size(); j++) {
		                		  try {
	                				  Double.parseDouble(linesAndColumns1.get(i).get(j));
	                			  }catch(NumberFormatException e) {
	                				  error = true;
				                	  System.out.println("NAN");
	                			  }
			                  }  
		                  }

	                  }else {
	                	  System.out.println("lines");
	                	  error = true;  
	                  }
	                  
	            	}
	                  
            		/*VERIFY IF COLUMN SEPARATOR IS GIVED*/
	            	if(!fileColumnSeparator2.getText().isEmpty()) {
	            	
	                  ArrayList<String> lines = new ArrayList(Arrays.asList(labelContentFile2.getText().split("\n")));
	                  /*REMOVE EMPTY LINES*/
	                  for(int i = 0; i < lines.size(); i++)
	                	  if(lines.get(i).isEmpty())
	                		  lines.remove(i);
	                  
	                  
		                  for(int i = 0; i < lines.size(); i++){
		                	  
			                  ArrayList<String> columnData = new ArrayList<String>(Arrays.asList(lines.get(i).split(Pattern.quote(fileColumnSeparator2.getText().trim()))));
			                  linesAndColumns2.add(columnData);
	                  }
	                  
		                  
	                  /*VERIFY THE NUMBER OF LINES BASED ON EACH MODEL*/
	                  if(
	                		  (linesAndColumns2.size() == 12 && model2.contains("BT0"))
	                		  ||
	                		  (linesAndColumns2.size() == 11 && model2.contains("TNO/EAB"))
	                		  ||
	                		  (linesAndColumns2.size() == 6 && model2.contains("KHM 1"))
	                		  
	                		  ) {
		                  int numberColumns = linesAndColumns2.get(0).size();
		                  for(int i = 1; i < linesAndColumns2.size(); i++) {
		                	  /*VERIFY IF HAVE THE SAME LENGTH OF COLUMNS*/
		                	  if(linesAndColumns2.get(i).size() != numberColumns) {
		                		  error = true;
			                	  System.out.println("cols");
		                	  }
		                	  /*VERIFY IF ALL CELLS AFTER 1 IS NUMBERS*/
		                	  for(int j = 0; j < linesAndColumns2.get(i).size(); j++) {
		                		  try {
	                				  Double.parseDouble(linesAndColumns2.get(i).get(j));
	                			  }catch(NumberFormatException e) {
	                				  error = true;
				                	  System.out.println("NAN");
	                			  }
			                  }  
		                  }

	                  }else {
	                	  System.out.println("lines");
	                	  error = true;  
	                  }
	                  
	            	}
	            	
	            	if(error) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("File error, format of files incorrect!");
						alert.showAndWait();
	            	}else {
	            		
	            		/*VERIFY IF HAVE THE SAME NUMBER OF COLUMNS AND MATCH HEADINGS*/
	            		for(int i = 0; i < linesAndColumns1.get(0).size(); i++) {
	            			
	            			if(!linesAndColumns1.get(0).get(i).trim().contains(linesAndColumns2.get(0).get(i).trim())) {

	            				error = true;
	            				
	            			}
	            			
	            		}
	            		
	            		if(!error && linesAndColumns1.get(0).size() == linesAndColumns2.get(0).size()) {
	            				            			
	                       Vector<Double> Roc = new Vector<Double>();
	                       Vector<Double> ac = new Vector<Double>();
	                       Vector<Double> L0  = new Vector<Double>();
	                       Vector<Double> Linf= new Vector<Double>();
	                       Vector<Double> fm  = new Vector<Double>();
	                       Vector<Double> Nb  = new Vector<Double>();
	                       Vector<Double> g0  = new Vector<Double>();
	                       Vector<Double> Nge = new Vector<Double>();
	                       Vector<Double> C0  = new Vector<Double>();
	                       Vector<Double> Cinf= new Vector<Double>();
	                       Vector<Double> Nce = new Vector<Double>();
	                       
	                	   Vector<Double> k1 = new Vector<Double>();
	                	   Vector<Double> k2 = new Vector<Double>();
	                	   Vector<Double> k3 = new Vector<Double>();
	                	   Vector<Double> h1 = new Vector<Double>();
	                	   Vector<Double> h2 = new Vector<Double>();
	                	   
	                	   Vector<Double> Z0Inf = new Vector<Double>();
	                	   Vector<Double> nVF = new Vector<Double>();
	                	   Vector<Double> Rs0 = new Vector<Double>();
	                	   Vector<Double> qL = new Vector<Double>();
	                	   Vector<Double> qH = new Vector<Double>();
	                	   Vector<Double> qx = new Vector<Double>();
	                	   Vector<Double> qy = new Vector<Double>();
	                	   Vector<Double> qc = new Vector<Double>();
	                	   Vector<Double> phi = new Vector<Double>();
	                	   Vector<Double> fd = new Vector<Double>();
	                       
	                       Vector<String> headings = new Vector<String>();

	                       /**************************************************************************************************************/
	                       
	            			if(model1.contains("BT0")) {	            				
		                	   
			                   for(int i = 0; i < linesAndColumns1.get(0).size(); i++)
			                	   headings.add(linesAndColumns1.get(0).get(i));

			                   for(int i = 0; i < linesAndColumns1.get(1).size(); i++)
			                	   Roc.add(Double.parseDouble(linesAndColumns1.get(1).get(i)));

			                   for(int i = 0; i < linesAndColumns1.get(2).size(); i++)
			                	   ac.add(Double.parseDouble(linesAndColumns1.get(2).get(i)));

			                   for(int i = 0; i < linesAndColumns1.get(3).size(); i++)
			                	   L0.add(Double.parseDouble(linesAndColumns1.get(3).get(i)));

			                   for(int i = 0; i < linesAndColumns1.get(4).size(); i++)
			                	   Linf.add(Double.parseDouble(linesAndColumns1.get(4).get(i)));

			                   for(int i = 0; i < linesAndColumns1.get(5).size(); i++)
			                	   fm.add(Double.parseDouble(linesAndColumns1.get(5).get(i)));

			                   for(int i = 0; i < linesAndColumns1.get(6).size(); i++)
			                	   Nb.add(Double.parseDouble(linesAndColumns1.get(6).get(i)));

			                   for(int i = 0; i < linesAndColumns1.get(7).size(); i++)
			                	   g0.add(Double.parseDouble(linesAndColumns1.get(7).get(i)));

			                   for(int i = 0; i < linesAndColumns1.get(8).size(); i++)
			                	   Nge.add(Double.parseDouble(linesAndColumns1.get(8).get(i)));

			                   for(int i = 0; i < linesAndColumns1.get(9).size(); i++)
			                	   C0.add(Double.parseDouble(linesAndColumns1.get(9).get(i)));

			                   for(int i = 0; i < linesAndColumns1.get(10).size(); i++)
			                	   Cinf.add(Double.parseDouble(linesAndColumns1.get(10).get(i)));

			                   for(int i = 0; i < linesAndColumns1.get(11).size(); i++)
			                	   Nce.add(Double.parseDouble(linesAndColumns1.get(11).get(i)));
	            				
	            			}else if(model1.contains("KHM 1")) {	            				
			                	   
				                   for(int i = 0; i < linesAndColumns1.get(0).size(); i++)
				                	   headings.add(linesAndColumns1.get(0).get(i));

				                   for(int i = 0; i < linesAndColumns1.get(1).size(); i++)
				                	   k1.add(Double.parseDouble(linesAndColumns1.get(1).get(i)));

				                   for(int i = 0; i < linesAndColumns1.get(2).size(); i++)
				                	   k2.add(Double.parseDouble(linesAndColumns1.get(2).get(i)));

				                   for(int i = 0; i < linesAndColumns1.get(3).size(); i++)
				                	   k3.add(Double.parseDouble(linesAndColumns1.get(3).get(i)));

				                   for(int i = 0; i < linesAndColumns1.get(4).size(); i++)
				                	   h1.add(Double.parseDouble(linesAndColumns1.get(4).get(i)));

				                   for(int i = 0; i < linesAndColumns1.get(5).size(); i++)
				                	   h2.add(Double.parseDouble(linesAndColumns1.get(5).get(i)));
		            				
	            			}else if(model1.contains("TNO/EAB")) {	            				
			                	   
				                   for(int i = 0; i < linesAndColumns1.get(0).size(); i++)
				                	   headings.add(linesAndColumns1.get(0).get(i));

				                   for(int i = 0; i < linesAndColumns1.get(1).size(); i++)
				                	   Z0Inf.add(Double.parseDouble(linesAndColumns1.get(1).get(i)));
				                   for(int i = 0; i < linesAndColumns1.get(2).size(); i++)
				                	   nVF.add(Double.parseDouble(linesAndColumns1.get(2).get(i)));
				                   for(int i = 0; i < linesAndColumns1.get(3).size(); i++)
				                	   Rs0.add(Double.parseDouble(linesAndColumns1.get(3).get(i)));
				                   for(int i = 0; i < linesAndColumns1.get(4).size(); i++)
				                	   qL.add(Double.parseDouble(linesAndColumns1.get(4).get(i)));
				                   for(int i = 0; i < linesAndColumns1.get(5).size(); i++)
				                	   qH.add(Double.parseDouble(linesAndColumns1.get(5).get(i)));
				                   for(int i = 0; i < linesAndColumns1.get(6).size(); i++)
				                	   qx.add(Double.parseDouble(linesAndColumns1.get(6).get(i)));
				                   for(int i = 0; i < linesAndColumns1.get(7).size(); i++)
				                	   qy.add(Double.parseDouble(linesAndColumns1.get(7).get(i)));
				                   for(int i = 0; i < linesAndColumns1.get(8).size(); i++)
				                	   qc.add(Double.parseDouble(linesAndColumns1.get(8).get(i)));
				                   for(int i = 0; i < linesAndColumns1.get(9).size(); i++)
				                	   phi.add(Double.parseDouble(linesAndColumns1.get(9).get(i)));
				                   for(int i = 0; i < linesAndColumns1.get(10).size(); i++)
				                	   fd.add(Double.parseDouble(linesAndColumns1.get(10).get(i)));
	            				
	            			}
	            			
	            			/*********************************************************************************************************/
	            			
	            			if(model2.contains("BT0")) {	            				
			                	   
				                   for(int i = 0; i < linesAndColumns2.get(1).size(); i++)
				                	   Roc.add(Double.parseDouble(linesAndColumns2.get(1).get(i)));

				                   for(int i = 0; i < linesAndColumns2.get(2).size(); i++)
				                	   ac.add(Double.parseDouble(linesAndColumns2.get(2).get(i)));

				                   for(int i = 0; i < linesAndColumns2.get(3).size(); i++)
				                	   L0.add(Double.parseDouble(linesAndColumns2.get(3).get(i)));

				                   for(int i = 0; i < linesAndColumns2.get(4).size(); i++)
				                	   Linf.add(Double.parseDouble(linesAndColumns2.get(4).get(i)));

				                   for(int i = 0; i < linesAndColumns2.get(5).size(); i++)
				                	   fm.add(Double.parseDouble(linesAndColumns2.get(5).get(i)));

				                   for(int i = 0; i < linesAndColumns2.get(6).size(); i++)
				                	   Nb.add(Double.parseDouble(linesAndColumns2.get(6).get(i)));

				                   for(int i = 0; i < linesAndColumns2.get(7).size(); i++)
				                	   g0.add(Double.parseDouble(linesAndColumns2.get(7).get(i)));

				                   for(int i = 0; i < linesAndColumns2.get(8).size(); i++)
				                	   Nge.add(Double.parseDouble(linesAndColumns2.get(8).get(i)));

				                   for(int i = 0; i < linesAndColumns2.get(9).size(); i++)
				                	   C0.add(Double.parseDouble(linesAndColumns2.get(9).get(i)));

				                   for(int i = 0; i < linesAndColumns2.get(10).size(); i++)
				                	   Cinf.add(Double.parseDouble(linesAndColumns2.get(10).get(i)));

				                   for(int i = 0; i < linesAndColumns2.get(11).size(); i++)
				                	   Nce.add(Double.parseDouble(linesAndColumns2.get(11).get(i)));
		            				
		            			}else if(model2.contains("KHM 1")) {	            				
				                	   
					                   for(int i = 0; i < linesAndColumns2.get(1).size(); i++)
					                	   k1.add(Double.parseDouble(linesAndColumns2.get(1).get(i)));

					                   for(int i = 0; i < linesAndColumns2.get(2).size(); i++)
					                	   k2.add(Double.parseDouble(linesAndColumns2.get(2).get(i)));

					                   for(int i = 0; i < linesAndColumns2.get(3).size(); i++)
					                	   k3.add(Double.parseDouble(linesAndColumns2.get(3).get(i)));

					                   for(int i = 0; i < linesAndColumns2.get(4).size(); i++)
					                	   h1.add(Double.parseDouble(linesAndColumns2.get(4).get(i)));

					                   for(int i = 0; i < linesAndColumns2.get(5).size(); i++)
					                	   h2.add(Double.parseDouble(linesAndColumns2.get(5).get(i)));
			            				
		            			}else if(model2.contains("TNO/EAB")) {	            				
				                	   
					                   for(int i = 0; i < linesAndColumns2.get(1).size(); i++)
					                	   Z0Inf.add(Double.parseDouble(linesAndColumns2.get(1).get(i)));
					                   for(int i = 0; i < linesAndColumns2.get(2).size(); i++)
					                	   nVF.add(Double.parseDouble(linesAndColumns2.get(2).get(i)));
					                   for(int i = 0; i < linesAndColumns2.get(3).size(); i++)
					                	   Rs0.add(Double.parseDouble(linesAndColumns2.get(3).get(i)));
					                   for(int i = 0; i < linesAndColumns2.get(4).size(); i++)
					                	   qL.add(Double.parseDouble(linesAndColumns2.get(4).get(i)));
					                   for(int i = 0; i < linesAndColumns2.get(5).size(); i++)
					                	   qH.add(Double.parseDouble(linesAndColumns2.get(5).get(i)));
					                   for(int i = 0; i < linesAndColumns2.get(6).size(); i++)
					                	   qx.add(Double.parseDouble(linesAndColumns2.get(6).get(i)));
					                   for(int i = 0; i < linesAndColumns2.get(7).size(); i++)
					                	   qy.add(Double.parseDouble(linesAndColumns2.get(7).get(i)));
					                   for(int i = 0; i < linesAndColumns2.get(8).size(); i++)
					                	   qc.add(Double.parseDouble(linesAndColumns2.get(8).get(i)));
					                   for(int i = 0; i < linesAndColumns2.get(9).size(); i++)
					                	   phi.add(Double.parseDouble(linesAndColumns2.get(9).get(i)));
					                   for(int i = 0; i < linesAndColumns2.get(10).size(); i++)
					                	   fd.add(Double.parseDouble(linesAndColumns2.get(10).get(i)));
		            				
		            			}
	            			
	            				/***********************************************SEND DATA TO PLOT****************************************************************/
	            			
	 	                   double minF_value = 0;
		                   double maxF_value = 0;
		                   double step_value = 0;
		                   double cableLength;
		                    String axisScale;
		                    String parameter;
	            			
			            	/*FOR COMPARISON BETWEEN TNO AND KHM1*/
			            	if(model1.contains("TNO/EAB") && model2.contains("KHM 1")) {
			            					            					                    
			                    
			                    /*VALIDATE INFO'S*/
			                    try{
			                        cableLength = Double.parseDouble(fileCableLength.getText());
			                        if(fileFrequency.getValue().getText().contains("Custom")) {
				                    	   
				                    	   minF_value = Double.parseDouble(fileMinF.getText()) * 1e6;
					                       maxF_value = Double.parseDouble(fileMaxF.getText()) * 1e6;
				                    	   step_value = Double.parseDouble(fileStep.getText()) * 1e6;

				                       }else {
					                       
				                    	   minF_value = Double.parseDouble(fileFrequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
					                       maxF_value = Double.parseDouble(fileFrequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
				                    	   step_value = 51.75e3;
					                       
				                       }
			                        axisScale = fileScale.getValue().getText();
			                        parameter = fileParameterCalc.getValue().getText();
			                    }catch(Exception ee){
			                        Alert alert = new Alert(AlertType.ERROR);
			                        alert.setTitle("Error");
			                        alert.setHeaderText("Error, please fill correctly the inputs before continue!");
			                        alert.setContentText(ee.toString());
			                        alert.showAndWait();
			                        return;
			                    }
			                    		                    				                    
			                    /*GENERATE GRAPHS*/
			                    DiffBetweenModelsController.generateDiffKHM1TNO(headings, Z0Inf,nVF,Rs0,qL,
			                    		qH,qx,qy,qc,phi,fd,k1,k2,k3,
			                    		h1,h2,cableLength,minF_value,maxF_value,step_value,axisScale,parameter, false);
			            	
			            	}else if(model2.contains("TNO/EAB") && model1.contains("KHM 1")) {
											            					                    
			                    /*VALIDATE INFO'S*/
			            		try{
			                        cableLength = Double.parseDouble(fileCableLength.getText());
			                        if(fileFrequency.getValue().getText().contains("Custom")) {
				                    	   
				                    	   minF_value = Double.parseDouble(fileMinF.getText()) * 1e6;
					                       maxF_value = Double.parseDouble(fileMaxF.getText()) * 1e6;
				                    	   step_value = Double.parseDouble(fileStep.getText()) * 1e6;

				                       }else {
					                       
				                    	   minF_value = Double.parseDouble(fileFrequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
					                       maxF_value = Double.parseDouble(fileFrequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
				                    	   step_value = 51.75e3;
					                       
				                       }
axisScale = fileScale.getValue().getText();
			                        parameter = fileParameterCalc.getValue().getText();
			                    }catch(Exception ee){
			                        Alert alert = new Alert(AlertType.ERROR);
			                        alert.setTitle("Error");
			                        alert.setHeaderText("Error, please fill correctly the inputs before continue!");
			                        alert.setContentText(ee.toString());
			                        alert.showAndWait();
			                        return;
			                    }
										                    				                    
								/*GENERATE GRAPHS*/
								DiffBetweenModelsController.generateDiffKHM1TNO(headings, Z0Inf,nVF,Rs0,qL,
										qH,qx,qy,qc,phi,fd,k1,k2,k3,
										h1,h2,cableLength,minF_value,maxF_value,step_value,axisScale,parameter, true);
							
							/*FOR COMPARISON BETWEEN TNO AND BT0*/	
							}else if(model1.contains("TNO/EAB") && model2.contains("BT0")) {
											                    
			                    /*VALIDATE INFO'S*/
			            		try{
			                        cableLength = Double.parseDouble(fileCableLength.getText());
			                        if(fileFrequency.getValue().getText().contains("Custom")) {
				                    	   
				                    	   minF_value = Double.parseDouble(fileMinF.getText()) * 1e6;
					                       maxF_value = Double.parseDouble(fileMaxF.getText()) * 1e6;
				                    	   step_value = Double.parseDouble(fileStep.getText()) * 1e6;

				                       }else {
					                       
				                    	   minF_value = Double.parseDouble(fileFrequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
					                       maxF_value = Double.parseDouble(fileFrequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
				                    	   step_value = 51.75e3;
					                       
				                       }
axisScale = fileScale.getValue().getText();
			                        parameter = fileParameterCalc.getValue().getText();
			                    }catch(Exception ee){
			                        Alert alert = new Alert(AlertType.ERROR);
			                        alert.setTitle("Error");
			                        alert.setHeaderText("Error, please fill correctly the inputs before continue!");
			                        alert.setContentText(ee.toString());
			                        alert.showAndWait();
			                        return;
			                    }
			                    		                    				                    
			                    /*GENERATE GRAPHS*/
			                    DiffBetweenModelsController.generateDiffBT0TNO(headings, Z0Inf,nVF,Rs0,qL,
			                    		qH,qx,qy,qc,phi,fd,
			                    		Roc, ac,L0,Linf,fm,Nb,
			                    		g0,Nge,C0,Cinf,Nce
			                    		,cableLength,minF_value,maxF_value,step_value,axisScale,parameter, false);
			            	
			            	}else if(model2.contains("TNO/EAB") && model1.contains("BT0")) {
			            					                    
			                    /*VALIDATE INFO'S*/
			            		try{
			                        cableLength = Double.parseDouble(fileCableLength.getText());
			                        if(fileFrequency.getValue().getText().contains("Custom")) {
				                    	   
				                    	   minF_value = Double.parseDouble(fileMinF.getText()) * 1e6;
					                       maxF_value = Double.parseDouble(fileMaxF.getText()) * 1e6;
				                    	   step_value = Double.parseDouble(fileStep.getText()) * 1e6;

				                       }else {
					                       
				                    	   minF_value = Double.parseDouble(fileFrequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
					                       maxF_value = Double.parseDouble(fileFrequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
				                    	   step_value = 51.75e3;
					                       
				                       }
axisScale = fileScale.getValue().getText();
			                        parameter = fileParameterCalc.getValue().getText();
			                    }catch(Exception ee){
			                        Alert alert = new Alert(AlertType.ERROR);
			                        alert.setTitle("Error");
			                        alert.setHeaderText("Error, please fill correctly the inputs before continue!");
			                        alert.setContentText(ee.toString());
			                        alert.showAndWait();
			                        return;
			                    }
										                    				                    
								/*GENERATE GRAPHS*/
								DiffBetweenModelsController.generateDiffBT0TNO(headings, Z0Inf,nVF,Rs0,qL,
										qH,qx,qy,qc,phi,fd,
										Roc, ac,L0,Linf,fm,Nb,
										g0,Nge,C0,Cinf,Nce
										,cableLength,minF_value,maxF_value,step_value,axisScale,parameter, true);
								
								/*FOR COMPARISON BETWEEN KHM1 AND BT0*/
								}else if(model1.contains("KHM 1") && model2.contains("BT0")) {
													                    
				                    /*VALIDATE INFO'S*/
				            		try{
				                        cableLength = Double.parseDouble(fileCableLength.getText());
				                        if(fileFrequency.getValue().getText().contains("Custom")) {
 				                    	   
 				                    	   minF_value = Double.parseDouble(fileMinF.getText()) * 1e6;
 					                       maxF_value = Double.parseDouble(fileMaxF.getText()) * 1e6;
 				                    	   step_value = Double.parseDouble(fileStep.getText()) * 1e6;

 				                       }else {
 					                       
 				                    	   minF_value = Double.parseDouble(fileFrequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
 					                       maxF_value = Double.parseDouble(fileFrequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
 				                    	   step_value = 51.75e3;
 					                       
 				                       }
axisScale = fileScale.getValue().getText();
				                        parameter = fileParameterCalc.getValue().getText();
				                    }catch(Exception ee){
				                        Alert alert = new Alert(AlertType.ERROR);
				                        alert.setTitle("Error");
				                        alert.setHeaderText("Error, please fill correctly the inputs before continue!");
				                        alert.setContentText(ee.toString());
				                        alert.showAndWait();
				                        return;
				                    }
				                    		                    				                    
				                    /*GENERATE GRAPHS*/
				                    DiffBetweenModelsController.generateDiffBT0KHM1(headings, 
				                    		k1, k2, k3, h1, h2,
				                    		Roc, ac,L0,Linf,fm,Nb,
				                    		g0,Nge,C0,Cinf,Nce
				                    		,cableLength,minF_value,maxF_value,step_value,axisScale,parameter, false);
				            	
				            	}else if(model2.contains("KHM 1") && model1.contains("BT0")) {
				            		
				                    /*VALIDATE INFO'S*/
				            		try{
				                        cableLength = Double.parseDouble(fileCableLength.getText());
				                        if(fileFrequency.getValue().getText().contains("Custom")) {
 				                    	   
 				                    	   minF_value = Double.parseDouble(fileMinF.getText()) * 1e6;
 					                       maxF_value = Double.parseDouble(fileMaxF.getText()) * 1e6;
 				                    	   step_value = Double.parseDouble(fileStep.getText()) * 1e6;

 				                       }else {
 					                       
 				                    	   minF_value = Double.parseDouble(fileFrequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
 					                       maxF_value = Double.parseDouble(fileFrequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
 				                    	   step_value = 51.75e3;
 					                       
 				                       }
axisScale = fileScale.getValue().getText();
				                        parameter = fileParameterCalc.getValue().getText();
				                    }catch(Exception ee){
				                        Alert alert = new Alert(AlertType.ERROR);
				                        alert.setTitle("Error");
				                        alert.setHeaderText("Error, please fill correctly the inputs before continue!");
				                        alert.setContentText(ee.toString());
				                        alert.showAndWait();
				                        return;
				                    }
				                    		                    				                    
				                    /*GENERATE GRAPHS*/
				                    DiffBetweenModelsController.generateDiffBT0KHM1(headings, 
				                    		k1, k2, k3, h1, h2,
				                    		Roc, ac,L0,Linf,fm,Nb,
				                    		g0,Nge,C0,Cinf,Nce
				                    		,cableLength,minF_value,maxF_value,step_value,axisScale,parameter, true);
				            	
				            	}
	            			
	            		}else {
	            			
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Error");
							alert.setHeaderText("The files must have the same numbers of columns, and the headings must match each other!");
							alert.showAndWait();
	            			
	            		}
	            		
	            	}
		            		            	
	            }
	        });
			
			
			
			
	        selectFileModel1.setOnMousePressed(new EventHandler<MouseEvent>() {
	            public void handle(MouseEvent me) {

	            	/*GET THE FILE*/
	            	FileChooser fileChooser = new FileChooser();
	            	fileChooser.setTitle("Open a "+model1+" Parameter File");

	            	File file1 = fileChooser.showOpenDialog(primaryStage);	            	
	            	
	            	if(file1 != null) {
	            		
	            		Scanner scanner;
	    				try {
	    					scanner = new Scanner(file1);
	            	        String textOfFile = "";
	    					while (scanner.hasNextLine()) {
	    						textOfFile += scanner.nextLine() + "\n";
	            	        }
	    					
	    					grid.getChildren().remove(scrollFileContent1);
	    					labelContentFile1.setText(textOfFile);
	    					scrollFileContent1.setContent(labelContentFile1);
	    					grid.add(scrollFileContent1, 0, 1, 3, 1);

	    					/******************************************/		
	    					
	    				} catch (Exception e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}

	            	}
	            	
	            }
	            
	        });
	        
	        selectFileModel2.setOnMousePressed(new EventHandler<MouseEvent>() {
	            public void handle(MouseEvent me) {

	            	/*GET THE FILE*/
	            	FileChooser fileChooser = new FileChooser();
	            	fileChooser.setTitle("Open a "+model2+" Parameter File");

	            	File file1 = fileChooser.showOpenDialog(primaryStage);	            	
	            	
	            	if(file1 != null) {
	            		
	            		Scanner scanner;
	    				try {
	    					scanner = new Scanner(file1);
	            	        String textOfFile = "";
	    					while (scanner.hasNextLine()) {
	    						textOfFile += scanner.nextLine() + "\n";
	            	        }
	    					
	    					grid.getChildren().remove(scrollFileContent2);
	    					labelContentFile2.setText(textOfFile);
	    					scrollFileContent2.setContent(labelContentFile2);
	    					grid.add(scrollFileContent2, 0, 4, 3, 1);

	    					/******************************************/		
	    					
	    				} catch (Exception e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}	

	            	}
	            	
	            }
	        });
	        
	        separateModel1.setOnMousePressed(new EventHandler<MouseEvent>() {
	            public void handle(MouseEvent me) {
	            	
	            	/*VERIFY IF COLUMN SEPARATOR IS GIVED*/
	            	if(!fileColumnSeparator1.getText().isEmpty()) {
	            	
	            	  boolean error = false;
	            		
	                  ArrayList<String> lines = new ArrayList(Arrays.asList(labelContentFile1.getText().split("\n")));
	                  /*REMOVE EMPTY LINES*/
	                  for(int i = 0; i < lines.size(); i++)
	                	  if(lines.get(i).isEmpty())
	                		  lines.remove(i);
	                  
	                  ArrayList<ArrayList<String>> linesAndColumns = new ArrayList<ArrayList<String>>();
	                  
		                  for(int i = 0; i < lines.size(); i++){
		                	  
			                  ArrayList<String> columnData = new ArrayList<String>(Arrays.asList(lines.get(i).split(Pattern.quote(fileColumnSeparator1.getText().trim()))));
			                  linesAndColumns.add(columnData);
	                  }
	                  
		                  
	                  /*VERIFY THE NUMBER OF LINES BASED ON EACH MODEL*/
	                  if(
	                		  (linesAndColumns.size() == 12 && model1.contains("BT0"))
	                		  ||
	                		  (linesAndColumns.size() == 11 && model1.contains("TNO/EAB"))
	                		  ||
	                		  (linesAndColumns.size() == 6 && model1.contains("KHM 1"))
	                		  
	                		  ) {
		                  int numberColumns = linesAndColumns.get(0).size();
		                  for(int i = 1; i < linesAndColumns.size(); i++) {
		                	  /*VERIFY IF HAVE THE SAME LENGTH OF COLUMNS*/
		                	  if(linesAndColumns.get(i).size() != numberColumns) {
		                		  error = true;
			                	  System.out.println("cols");
		                	  }
		                	  /*VERIFY IF ALL CELLS AFTER 1 IS NUMBERS*/
		                	  for(int j = 0; j < linesAndColumns.get(i).size(); j++) {
		                		  try {
	                				  Double.parseDouble(linesAndColumns.get(i).get(j));
	                			  }catch(NumberFormatException e) {
	                				  error = true;
				                	  System.out.println("NAN");
	                			  }
			                  }  
		                  }

	                  }else {
	                	  System.out.println("lines");
	                	  error = true;  
	                  }
	                  
	                  if(error) {
	                	  Alert alert = new Alert(AlertType.ERROR);
	                      alert.setTitle("Error");
	                      alert.setHeaderText("File error, format of file incorrect!");
	                      alert.showAndWait();
	                  }else {

	                	  /*IF EVERYTHING OK GENERATE TABLE OF VALUES TO CONFIRM THAT FILE IS CORRECTLY*/
	                	  TableView<String[]> table = new TableView<String[]>();
	                      table.setEditable(false);
	                      
	                      /*CREATING THE COLUMNS OF TABLE*/
	                      
	                      for(int i = 0; i < linesAndColumns.get(0).size(); i++) {
	                    	  Vector<Integer> actualI = new Vector<Integer>();
	                    	  actualI.add(i);
	                    	  TableColumn<String[],String> col = new TableColumn<String[],String>();
	                    	  col.setText(linesAndColumns.get(0).get(i));
	                    	  col.setCellValueFactory((Callback < CellDataFeatures < String[], String > , ObservableValue < String >> ) new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
		                    	   public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
		                    	    String[] x = p.getValue();
		                    	    if (x != null && x.length > Integer.parseInt(actualI.get(0).toString())) {
		                    	     return new SimpleStringProperty(x[Integer.parseInt(actualI.get(0).toString())]);
		                    	    } else {
		                    	     return new SimpleStringProperty("<no name>");
		                    	    }
		                    	   }
		                    	  });
	                    	  table.getColumns().addAll(col);					                                
	                      }				                      

	                      /*ADDING INFORMATION TO COLUMNS*/
	                      String[][] data = new String[linesAndColumns.size() - 1][linesAndColumns.get(0).size()];
	                      for(int i = 1; i < linesAndColumns.size(); i++) {
	                    	  for(int j = 0; j < linesAndColumns.get(0).size(); j++) {				                    		  
	                    		  data[i - 1][j] = linesAndColumns.get(i).get(j);
	                    	  }
	                      }
	                      table.getItems().addAll(Arrays.asList(data));
	                      
	                      ScrollPane formatedTableScroll = new ScrollPane();
	                      formatedTableScroll.setContent(table);
	                      formatedTableScroll.setFitToWidth(true);
	                      formatedTableScroll.setFitToHeight(false);
	                      formatedTableScroll.setMaxHeight(Double.MAX_VALUE);
	                      
	                      /*REMOVE LABEL FILE 1 AND ADD TABLE*/
	                      grid.getChildren().remove(scrollFileContent1);
	                      grid.add(formatedTableScroll, 0, 1, 3, 1);
	                      				                      
	                  }

	            		
	            	}else {
	            		
	            		Alert alert = new Alert(AlertType.ERROR);
	                    alert.setTitle("Error");
	                    alert.setHeaderText("Please give column separator for file!");
	                    alert.showAndWait();
	                    return;
	            		
	            	}
	                
	           }	        
	            
            });
	        
	        
	        separateModel2.setOnMousePressed(new EventHandler<MouseEvent>() {
	            public void handle(MouseEvent me) {
	            	
	            	/*VERIFY IF COLUMN SEPARATOR IS GIVED*/
	            	if(!fileColumnSeparator2.getText().isEmpty()) {
	            	
	            	  boolean error = false;
	            		
	                  ArrayList<String> lines = new ArrayList(Arrays.asList(labelContentFile2.getText().split("\n")));
	                  /*REMOVE EMPTY LINES*/
	                  for(int i = 0; i < lines.size(); i++)
	                	  if(lines.get(i).isEmpty())
	                		  lines.remove(i);
	                  
	                  ArrayList<ArrayList<String>> linesAndColumns = new ArrayList<ArrayList<String>>();
	                  
		                  for(int i = 0; i < lines.size(); i++){
		                	  
			                  ArrayList<String> columnData = new ArrayList<String>(Arrays.asList(lines.get(i).split(Pattern.quote(fileColumnSeparator2.getText().trim()))));
			                  linesAndColumns.add(columnData);
	                  }
	                  
		                  
	                  /*VERIFY THE NUMBER OF LINES BASED ON EACH MODEL*/
	                  if(
	                		  (linesAndColumns.size() == 12 && model2.contains("BT0"))
	                		  ||
	                		  (linesAndColumns.size() == 11 && model2.contains("TNO/EAB"))
	                		  ||
	                		  (linesAndColumns.size() == 6 && model2.contains("KHM 1"))
	                		  
	                		  ) {
		                  int numberColumns = linesAndColumns.get(0).size();
		                  for(int i = 1; i < linesAndColumns.size(); i++) {
		                	  /*VERIFY IF HAVE THE SAME LENGTH OF COLUMNS*/
		                	  if(linesAndColumns.get(i).size() != numberColumns) {
		                		  error = true;
			                	  System.out.println("cols");
		                	  }
		                	  /*VERIFY IF ALL CELLS AFTER 1 IS NUMBERS*/
		                	  for(int j = 0; j < linesAndColumns.get(i).size(); j++) {
		                		  try {
	                				  Double.parseDouble(linesAndColumns.get(i).get(j));
	                			  }catch(NumberFormatException e) {
	                				  error = true;
				                	  System.out.println("NAN");
	                			  }
			                  }  
		                  }

	                  }else {
	                	  System.out.println("lines");
	                	  error = true;  
	                  }
	                  
	                  if(error) {
	                	  Alert alert = new Alert(AlertType.ERROR);
	                      alert.setTitle("Error");
	                      alert.setHeaderText("File error, format of file incorrect!");
	                      alert.showAndWait();
	                  }else {

	                	  /*IF EVERYTHING OK GENERATE TABLE OF VALUES TO CONFIRM THAT FILE IS CORRECTLY*/
	                	  TableView<String[]> table = new TableView<String[]>();
	                      table.setEditable(false);
	                      
	                      /*CREATING THE COLUMNS OF TABLE*/
	                      
	                      for(int i = 0; i < linesAndColumns.get(0).size(); i++) {
	                    	  Vector<Integer> actualI = new Vector<Integer>();
	                    	  actualI.add(i);
	                    	  TableColumn<String[],String> col = new TableColumn<String[],String>();
	                    	  col.setText(linesAndColumns.get(0).get(i));
	                    	  col.setCellValueFactory((Callback < CellDataFeatures < String[], String > , ObservableValue < String >> ) new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
		                    	   public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
		                    	    String[] x = p.getValue();
		                    	    if (x != null && x.length > Integer.parseInt(actualI.get(0).toString())) {
		                    	     return new SimpleStringProperty(x[Integer.parseInt(actualI.get(0).toString())]);
		                    	    } else {
		                    	     return new SimpleStringProperty("<no name>");
		                    	    }
		                    	   }
		                    	  });
	                    	  table.getColumns().addAll(col);					                                
	                      }				                      

	                      /*ADDING INFORMATION TO COLUMNS*/
	                      String[][] data = new String[linesAndColumns.size() - 1][linesAndColumns.get(0).size()];
	                      for(int i = 1; i < linesAndColumns.size(); i++) {
	                    	  for(int j = 0; j < linesAndColumns.get(0).size(); j++) {				                    		  
	                    		  data[i - 1][j] = linesAndColumns.get(i).get(j);
	                    	  }
	                      }
	                      table.getItems().addAll(Arrays.asList(data));
	                      
	                      ScrollPane formatedTableScroll = new ScrollPane();
	                      formatedTableScroll.setContent(table);
	                      formatedTableScroll.setFitToWidth(true);
	                      formatedTableScroll.setFitToHeight(false);
	                      formatedTableScroll.setMaxHeight(Double.MAX_VALUE);
	                      
	                      /*REMOVE LABEL FILE 1 AND ADD TABLE*/
	                      grid.getChildren().remove(scrollFileContent2);
	                      grid.add(formatedTableScroll, 0, 4, 3, 1);
	                      				                      
	                  }

	            		
	            	}else {
	            		
	            		Alert alert = new Alert(AlertType.ERROR);
	                    alert.setTitle("Error");
	                    alert.setHeaderText("Please give column separator for file!");
	                    alert.showAndWait();
	                    return;
	            		
	            	}
	                
	           }	        
	            
            });
	        
	        
			
			ScrollPane pane = new ScrollPane();
			pane.setContent(grid);
			pane.setFitToHeight(true);
			pane.setFitToWidth(true);

			return pane;
            
       }

	
	
	

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
        Label labelBT0 = new Label("BT0"); 
        
        JFXComboBox<Label> model1 = new JFXComboBox<Label>();
        model1.getItems().addAll(new Label("TNO/EAB"), new Label("KHM 1"), new Label("BT0"));
        model1.setPromptText("Select the Cable Model");
        
        JFXComboBox<Label> model2 = new JFXComboBox<Label>();
        model2.getItems().addAll(labelTNO_EAB, labelKHM1, labelBT0);
        model2.setPromptText("Select the Cable Model");

        Button selectButton = new Button("Select");
        selectButton.setId("select");
        
        Label andLabel = new Label("and");
        andLabel.setId("HelpLabel");
        
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

	            		case "BT0":
            				model2.getItems().add(labelBT0);            			            			
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

            		case "BT0":
	        				model2.getItems().remove(labelBT0);            			            			
	        			break;

            	}

            	model2.getSelectionModel().clearSelection();
            	
            	grid.getChildren().clear();
            	
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
                
                /*ADDING LINE*/
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
                
                /*ADDING LINE*/        
                selectButton.setMaxWidth(Double.MAX_VALUE);
                grid.add(selectButton, 1, line, 1, 1);
                GridPane.setHalignment(selectButton, HPos.CENTER);
          		line++;                    

                /*ADDING LINE*/        
                back.setMaxWidth(Double.MAX_VALUE);
                grid.add(back, 1, line, 1, 1);
                GridPane.setHalignment(back, HPos.CENTER);
          		line++;                    


            }
        });
        
        
        
        model2.valueProperty().addListener(new ChangeListener<Label>() {
            @Override
            public void changed(ObservableValue<? extends Label> observable, Label oldValue, Label newValue) {

            	grid.getChildren().clear();
            	
            	int line = 0;
            	
          		grid.getChildren().clear();

          		grid.add(label, 0, line, 3, 1);
                GridPane.setHalignment(label, HPos.CENTER);
                line++;
                
                /*ADDING LINE*/
                labelTypesOfModels.setMaxWidth(Double.MAX_VALUE);
                grid.add(labelTypesOfModels, 0, line, 3, 1);
                GridPane.setHalignment(labelTypesOfModels, HPos.CENTER);
                line++;
                
                /*ADDING LINE*/
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
                
                /*ADDING LINE*/        
                selectButton.setMaxWidth(Double.MAX_VALUE);
                grid.add(selectButton, 1, line, 1, 1);
                GridPane.setHalignment(selectButton, HPos.CENTER);
          		line++;                    

                /*ADDING LINE*/        
                back.setMaxWidth(Double.MAX_VALUE);
                grid.add(back, 1, line, 1, 1);
                GridPane.setHalignment(back, HPos.CENTER);
          		line++;                    

            }
        });
        
        
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
        frequency.getItems().add(new Label("Custom"));
        frequency.setPromptText("Frequency Band");
        
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
              		
            	        /*BT0*/        
            	        
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
        	        				/*KHM1*/
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
        	        				/*TNO/EAB*/
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
        	        				/*BT0*/
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
        	            		case "B05a":
        	            			descriptionCable.setText("Cable Aerial Drop-wire No 55 (CAD55), a typical copper line used in the UK");
        	        				/*KHM 1*/
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
        	            			/*TNO/EAB*/
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
        	        				/*BT0*/
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
        	            		case "T05b":
        	    					descriptionCable.setText("Medium quality multi-quad cable used in buildings");            			
        	        				/*KHM 1*/
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
        	        				/*TNO/EAB*/
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
        	        				/*BT0*/
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
        	            		case "T05h":
        	    					descriptionCable.setText("Low quality cable used for in-house telephony wiring");
        	        				/*KHM 1*/
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
        	        				/*TNO/EAB*/
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
        	        				/*BT0*/
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
        	            		case "T05u":
        	        				descriptionCable.setText("KPN cable, a typical access line used in the Netherlands");
        	        				/*KHM 1*/
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
        	        				/*TNO/EAB*/
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
        	        				/*BT0*/
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
        	            		default:
        	        				descriptionCable.setText("Enter manually the parameters or choose at side a predefined cable type");
        	        				/*KHM 1*/
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
        	        				/*TNO/EAB*/
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
        	        				/*BT0*/
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
            			
                		case "BT0":
                            
                	        Label helpLabel3 = new Label("BT0 Parameters");
                	        helpLabel3.setId("HelpLabel");
                	        grid.add(helpLabel3, 1, line, 1, 1);
                	        GridPane.setFillWidth(helpLabel3, true);
                	        helpLabel3.setMaxWidth(Double.MAX_VALUE);
                	        helpLabel3.setAlignment(Pos.CENTER);
                	        GridPane.setHalignment(helpLabel3, HPos.CENTER);
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
        			
            		case "BT0":
                        
            	        Label helpLabel3 = new Label("BT0 Parameters");
            	        helpLabel3.setId("HelpLabel");
            	        grid.add(helpLabel3, 1, line, 1, 1);
            	        GridPane.setFillWidth(helpLabel3, true);
            	        helpLabel3.setMaxWidth(Double.MAX_VALUE);
            	        helpLabel3.setAlignment(Pos.CENTER);
            	        GridPane.setHalignment(helpLabel3, HPos.CENTER);
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
                    frequency.getItems().add(new Label("Custom"));
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

                        	primaryStage.getScene().setRoot(DiffBetweenModelsScreen.getInputFileWindow(primaryStage, model1.getValue().getText(), model2.getValue().getText()));
                    		String css = DiffBetweenModelsScreen.class.getResource("InputFileWindow.css").toExternalForm(); 
                        	primaryStage.getScene().getStylesheets().clear();
                        	primaryStage.getScene().getStylesheets().add(css);
                        
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
                        	
                        	/*SAVE FILE WINDOW*/
                        	FileChooser fileChooser = new FileChooser();
                        	fileChooser.setTitle("Save Result Output File");
                            File selectedDirectory = fileChooser.showSaveDialog(primaryStage);
                        	
                        	if(selectedDirectory != null){
                        		
        		            	/*CALL FUNCTION TO PLOT DATA*/

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

    			            	/*FOR COMPARISON BETWEEN TNO AND KHM1*/
    			            	if(model1.getValue().getText().contains("TNO/EAB") && model2.getValue().getText().contains("KHM 1")) {
    			            					            		
    			                    
				                   double minF_value = 0;
				                   double maxF_value = 0;
				                   double step_value = 0;

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
    			                        if(frequency.getValue().getText().contains("Custom")) {
    				                    	   
    				                    	   minF_value = Double.parseDouble(minF.getText()) * 1e6;
    					                       maxF_value = Double.parseDouble(maxF.getText()) * 1e6;
    				                    	   step_value = Double.parseDouble(step.getText()) * 1e6;

    				                       }else {
    					                       
    				                    	   minF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
    					                       maxF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
    				                    	   step_value = 51.75e3;
    					                       
    				                       }
    			                        axisScale = scale.getValue().getText();
    			                        parameter = parameterCalc.getValue().getText();
    			                    }catch(Exception ee){
    			                        Alert alert = new Alert(AlertType.ERROR);
    			                        alert.setTitle("Error");
    			                        alert.setHeaderText("Error, please fill correctly the inputs before continue!");
    			                        alert.setContentText(ee.toString());
    			                        alert.showAndWait();
    			                        return;
    			                    }
    			                    		                    				                    
    			                    /*GENERATE GRAPHS*/
    			                    DiffBetweenModelsController.generateOutputDiffKHM1TNO(headings, Z0inf_value,nVF_value,Rs0_value,qL_value,
    			                    		qH_value,qx_value,qy_value,qc_value,phi_value,fd_value,k1_value,k2_value,k3_value,
    			                    		h1_value,h2_value,cableLength_value,minF_value, maxF_value, step_value,axisScale,parameter, false, selectedDirectory);
    			            	
    			            	}else if(model2.getValue().getText().contains("TNO/EAB") && model1.getValue().getText().contains("KHM 1")) {
    											            		    								
    				                   double minF_value = 0;
    				                   double maxF_value = 0;
    				                   double step_value = 0;

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
    								    if(frequency.getValue().getText().contains("Custom")) {
 				                    	   
 				                    	   minF_value = Double.parseDouble(minF.getText()) * 1e6;
 					                       maxF_value = Double.parseDouble(maxF.getText()) * 1e6;
 				                    	   step_value = Double.parseDouble(step.getText()) * 1e6;

 				                       }else {
 					                       
 				                    	   minF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
 					                       maxF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
 				                    	   step_value = 51.75e3;
 					                       
 				                       }
    								    axisScale = scale.getValue().getText();
    								    parameter = parameterCalc.getValue().getText();
    								}catch(Exception ee){
    								    Alert alert = new Alert(AlertType.ERROR);
    								    alert.setTitle("Error");
    								    alert.setHeaderText("Error, please fill correctly the inputs before continue!");
    								    alert.setContentText(ee.toString());
    								    alert.showAndWait();
    								    return;
    								}
    										                    				                    
    								/*GENERATE GRAPHS*/
    								DiffBetweenModelsController.generateOutputDiffKHM1TNO(headings, Z0inf_value,nVF_value,Rs0_value,qL_value,
    										qH_value,qx_value,qy_value,qc_value,phi_value,fd_value,k1_value,k2_value,k3_value,
    										h1_value,h2_value,cableLength_value,minF_value, maxF_value, step_value,axisScale,parameter, true, selectedDirectory);
    							
    							/*FOR COMPARISON BETWEEN TNO AND BT0*/	
    							}else if(model1.getValue().getText().contains("TNO/EAB") && model2.getValue().getText().contains("BT0")) {
    			            					            		    			                    
    				                   double minF_value = 0;
    				                   double maxF_value = 0;
    				                   double step_value = 0;

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
    			                        if(frequency.getValue().getText().contains("Custom")) {
 				                    	   
 				                    	   minF_value = Double.parseDouble(minF.getText()) * 1e6;
 					                       maxF_value = Double.parseDouble(maxF.getText()) * 1e6;
 				                    	   step_value = Double.parseDouble(step.getText()) * 1e6;

 				                       }else {
 					                       
 				                    	   minF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
 					                       maxF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
 				                    	   step_value = 51.75e3;
 					                       
 				                       }
    			                        axisScale = scale.getValue().getText();
    			                        parameter = parameterCalc.getValue().getText();
    			                    }catch(Exception ee){
    			                        Alert alert = new Alert(AlertType.ERROR);
    			                        alert.setTitle("Error");
    			                        alert.setHeaderText("Error, please fill correctly the inputs before continue!");
    			                        alert.setContentText(ee.toString());
    			                        alert.showAndWait();
    			                        return;
    			                    }
    			                    		                    				                    
    			                    /*GENERATE GRAPHS*/
    			                    DiffBetweenModelsController.generateOutputDiffBT0TNO(headings, Z0inf_value,nVF_value,Rs0_value,qL_value,
    			                    		qH_value,qx_value,qy_value,qc_value,phi_value,fd_value,
    			                    		Roc_value, ac_value,L0_value,Linf_value,fm_value,Nb_value,
    			                    		g0_value,Nge_value,C0_value,Cinf_value,Nce_value
    			                    		,cableLength_value,minF_value, maxF_value, step_value,axisScale,parameter, false, selectedDirectory);
    			            	
    			            	}else if(model2.getValue().getText().contains("TNO/EAB") && model1.getValue().getText().contains("BT0")) {
    											            		

    				                   double minF_value = 0;
    				                   double maxF_value = 0;
    				                   double step_value = 0;
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
    								    if(frequency.getValue().getText().contains("Custom")) {
 				                    	   
 				                    	   minF_value = Double.parseDouble(minF.getText()) * 1e6;
 					                       maxF_value = Double.parseDouble(maxF.getText()) * 1e6;
 				                    	   step_value = Double.parseDouble(step.getText()) * 1e6;

 				                       }else {
 					                       
 				                    	   minF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
 					                       maxF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
 				                    	   step_value = 51.75e3;
 					                       
 				                       }
    								    axisScale = scale.getValue().getText();
    								    parameter = parameterCalc.getValue().getText();
    								}catch(Exception ee){
    								    Alert alert = new Alert(AlertType.ERROR);
    								    alert.setTitle("Error");
    								    alert.setHeaderText("Error, please fill correctly the inputs before continue!");
    								    alert.setContentText(ee.toString());
    								    alert.showAndWait();
    								    return;
    								}
    										                    				                    
    								/*GENERATE GRAPHS*/
    								DiffBetweenModelsController.generateOutputDiffBT0TNO(headings, Z0inf_value,nVF_value,Rs0_value,qL_value,
    										qH_value,qx_value,qy_value,qc_value,phi_value,fd_value,
    										Roc_value, ac_value,L0_value,Linf_value,fm_value,Nb_value,
    										g0_value,Nge_value,C0_value,Cinf_value,Nce_value
    										,cableLength_value,minF_value, maxF_value, step_value,axisScale,parameter, true, selectedDirectory);
    								
    								/*FOR COMPARISON BETWEEN KHM1 AND BT0*/
    								}else if(model1.getValue().getText().contains("KHM 1") && model2.getValue().getText().contains("BT0")) {
    		            		    				                    
    				                   double minF_value = 0;
    				                   double maxF_value = 0;
    				                   double step_value = 0;
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
    				                    	
    				                        k1_value.add(Double.parseDouble(k1.getText()));
    				                        k2_value.add(Double.parseDouble(k2.getText()));
    				                        k3_value.add(Double.parseDouble(k3.getText()));
    				                        h1_value.add(Double.parseDouble(h1.getText()));
    				                        h2_value.add(Double.parseDouble(h2.getText()));
    				                    	
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
    				                        if(frequency.getValue().getText().contains("Custom")) {
     				                    	   
     				                    	   minF_value = Double.parseDouble(minF.getText()) * 1e6;
     					                       maxF_value = Double.parseDouble(maxF.getText()) * 1e6;
     				                    	   step_value = Double.parseDouble(step.getText()) * 1e6;

     				                       }else {
     					                       
     				                    	   minF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
     					                       maxF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
     				                    	   step_value = 51.75e3;
     					                       
     				                       }
    				                        axisScale = scale.getValue().getText();
    				                        parameter = parameterCalc.getValue().getText();
    				                    }catch(Exception ee){
    				                        Alert alert = new Alert(AlertType.ERROR);
    				                        alert.setTitle("Error");
    				                        alert.setHeaderText("Error, please fill correctly the inputs before continue!");
    				                        alert.setContentText(ee.toString());
    				                        alert.showAndWait();
    				                        return;
    				                    }
    				                    		                    				                    
    				                    /*GENERATE GRAPHS*/
    				                    DiffBetweenModelsController.generateOutputDiffBT0KHM1(headings, 
    				                    		k1_value, k2_value, k3_value, h1_value, h2_value,
    				                    		Roc_value, ac_value,L0_value,Linf_value,fm_value,Nb_value,
    				                    		g0_value,Nge_value,C0_value,Cinf_value,Nce_value
    				                    		,cableLength_value,minF_value, maxF_value, step_value,axisScale,parameter, false, selectedDirectory);
    				            	
    				            	}else if(model2.getValue().getText().contains("KHM 1") && model1.getValue().getText().contains("BT0")) {
    		            		
    					                   double minF_value = 0;
    					                   double maxF_value = 0;
    					                   double step_value = 0;
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
    				                    	
    				                        k1_value.add(Double.parseDouble(k1.getText()));
    				                        k2_value.add(Double.parseDouble(k2.getText()));
    				                        k3_value.add(Double.parseDouble(k3.getText()));
    				                        h1_value.add(Double.parseDouble(h1.getText()));
    				                        h2_value.add(Double.parseDouble(h2.getText()));
    				                    	
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
    				                        if(frequency.getValue().getText().contains("Custom")) {
     				                    	   
     				                    	   minF_value = Double.parseDouble(minF.getText()) * 1e6;
     					                       maxF_value = Double.parseDouble(maxF.getText()) * 1e6;
     				                    	   step_value = Double.parseDouble(step.getText()) * 1e6;

     				                       }else {
     					                       
     				                    	   minF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
     					                       maxF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
     				                    	   step_value = 51.75e3;
     					                       
     				                       }
    				                        axisScale = scale.getValue().getText();
    				                        parameter = parameterCalc.getValue().getText();
    				                    }catch(Exception ee){
    				                        Alert alert = new Alert(AlertType.ERROR);
    				                        alert.setTitle("Error");
    				                        alert.setHeaderText("Error, please fill correctly the inputs before continue!");
    				                        alert.setContentText(ee.toString());
    				                        alert.showAndWait();
    				                        return;
    				                    }
    				                    		                    				                    
    				                    /*GENERATE GRAPHS*/
    				                    DiffBetweenModelsController.generateOutputDiffBT0KHM1(headings, 
    				                    		k1_value, k2_value, k3_value, h1_value, h2_value,
    				                    		Roc_value, ac_value,L0_value,Linf_value,fm_value,Nb_value,
    				                    		g0_value,Nge_value,C0_value,Cinf_value,Nce_value
    				                    		,cableLength_value,minF_value, maxF_value, step_value,axisScale,parameter, true, selectedDirectory);
    				            	
    				            	}
                        		
                            }
                        	
                        }
                        
                    });

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
			                    
				                   double minF_value = 0;
				                   double maxF_value = 0;
				                   double step_value = 0;
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
			                        if(frequency.getValue().getText().contains("Custom")) {
				                    	   
				                    	   minF_value = Double.parseDouble(minF.getText()) * 1e6;
					                       maxF_value = Double.parseDouble(maxF.getText()) * 1e6;
				                    	   step_value = Double.parseDouble(step.getText()) * 1e6;

				                       }else {
					                       
				                    	   minF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
					                       maxF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
				                    	   step_value = 51.75e3;
					                       
				                       }
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
			                    		h1_value,h2_value,cableLength_value,minF_value, maxF_value, step_value,axisScale,parameter, false);
			            	
			            	}else if(model2.getValue().getText().contains("TNO/EAB") && model1.getValue().getText().contains("KHM 1")) {
											            		
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
								
				                   double minF_value = 0;
				                   double maxF_value = 0;
				                   double step_value = 0;
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
								    if(frequency.getValue().getText().contains("Custom")) {
				                    	   
				                    	   minF_value = Double.parseDouble(minF.getText()) * 1e6;
					                       maxF_value = Double.parseDouble(maxF.getText()) * 1e6;
				                    	   step_value = Double.parseDouble(step.getText()) * 1e6;

				                       }else {
					                       
				                    	   minF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
					                       maxF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
				                    	   step_value = 51.75e3;
					                       
				                       }
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
										h1_value,h2_value,cableLength_value,minF_value, maxF_value, step_value,axisScale,parameter, true);
							
							/*FOR COMPARISON BETWEEN TNO AND BT0*/	
							}else if(model1.getValue().getText().contains("TNO/EAB") && model2.getValue().getText().contains("BT0")) {
			            					            		
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
			                    
				                   double minF_value = 0;
				                   double maxF_value = 0;
				                   double step_value = 0;
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
			                        if(frequency.getValue().getText().contains("Custom")) {
				                    	   
				                    	   minF_value = Double.parseDouble(minF.getText()) * 1e6;
					                       maxF_value = Double.parseDouble(maxF.getText()) * 1e6;
				                    	   step_value = Double.parseDouble(step.getText()) * 1e6;

				                       }else {
					                       
				                    	   minF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
					                       maxF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
				                    	   step_value = 51.75e3;
					                       
				                       }
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
			                    DiffBetweenModelsController.generateDiffBT0TNO(headings, Z0inf_value,nVF_value,Rs0_value,qL_value,
			                    		qH_value,qx_value,qy_value,qc_value,phi_value,fd_value,
			                    		Roc_value, ac_value,L0_value,Linf_value,fm_value,Nb_value,
			                    		g0_value,Nge_value,C0_value,Cinf_value,Nce_value
			                    		,cableLength_value,minF_value, maxF_value, step_value,axisScale,parameter, false);
			            	
			            	}else if(model2.getValue().getText().contains("TNO/EAB") && model1.getValue().getText().contains("BT0")) {
											            		
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
								
				                   double minF_value = 0;
				                   double maxF_value = 0;
				                   double step_value = 0;
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
								    if(frequency.getValue().getText().contains("Custom")) {
				                    	   
				                    	   minF_value = Double.parseDouble(minF.getText()) * 1e6;
					                       maxF_value = Double.parseDouble(maxF.getText()) * 1e6;
				                    	   step_value = Double.parseDouble(step.getText()) * 1e6;

				                       }else {
					                       
				                    	   minF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
					                       maxF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
				                    	   step_value = 51.75e3;
					                       
				                       }
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
								DiffBetweenModelsController.generateDiffBT0TNO(headings, Z0inf_value,nVF_value,Rs0_value,qL_value,
										qH_value,qx_value,qy_value,qc_value,phi_value,fd_value,
										Roc_value, ac_value,L0_value,Linf_value,fm_value,Nb_value,
										g0_value,Nge_value,C0_value,Cinf_value,Nce_value
										,cableLength_value,minF_value, maxF_value, step_value,axisScale,parameter, true);
								
								/*FOR COMPARISON BETWEEN KHM1 AND BT0*/
								}else if(model1.getValue().getText().contains("KHM 1") && model2.getValue().getText().contains("BT0")) {
		            		
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

			                	   Vector<Double> k1_value = new Vector<Double>();
			                	   Vector<Double> k2_value = new Vector<Double>();
			                	   Vector<Double> k3_value = new Vector<Double>();
			                	   Vector<Double> h1_value = new Vector<Double>();
			                	   Vector<Double> h2_value = new Vector<Double>();
				                    
				                   double minF_value = 0;
				                   double maxF_value = 0;
				                   double step_value = 0;
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
				                    	
				                        k1_value.add(Double.parseDouble(k1.getText()));
				                        k2_value.add(Double.parseDouble(k2.getText()));
				                        k3_value.add(Double.parseDouble(k3.getText()));
				                        h1_value.add(Double.parseDouble(h1.getText()));
				                        h2_value.add(Double.parseDouble(h2.getText()));
				                    	
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
				                        if(frequency.getValue().getText().contains("Custom")) {
 				                    	   
 				                    	   minF_value = Double.parseDouble(minF.getText()) * 1e6;
 					                       maxF_value = Double.parseDouble(maxF.getText()) * 1e6;
 				                    	   step_value = Double.parseDouble(step.getText()) * 1e6;

 				                       }else {
 					                       
 				                    	   minF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
 					                       maxF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
 				                    	   step_value = 51.75e3;
 					                       
 				                       }
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
				                    DiffBetweenModelsController.generateDiffBT0KHM1(headings, 
				                    		k1_value, k2_value, k3_value, h1_value, h2_value,
				                    		Roc_value, ac_value,L0_value,Linf_value,fm_value,Nb_value,
				                    		g0_value,Nge_value,C0_value,Cinf_value,Nce_value
				                    		,cableLength_value,minF_value, maxF_value, step_value,axisScale,parameter, false);
				            	
				            	}else if(model2.getValue().getText().contains("KHM 1") && model1.getValue().getText().contains("BT0")) {
		            		
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

									Vector<Double> k1_value = new Vector<Double>();
									Vector<Double> k2_value = new Vector<Double>();
									Vector<Double> k3_value = new Vector<Double>();
									Vector<Double> h1_value = new Vector<Double>();
									Vector<Double> h2_value = new Vector<Double>();
				                    
					                   double minF_value = 0;
					                   double maxF_value = 0;
					                   double step_value = 0;
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
				                    	
				                        k1_value.add(Double.parseDouble(k1.getText()));
				                        k2_value.add(Double.parseDouble(k2.getText()));
				                        k3_value.add(Double.parseDouble(k3.getText()));
				                        h1_value.add(Double.parseDouble(h1.getText()));
				                        h2_value.add(Double.parseDouble(h2.getText()));
				                    	
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
				                        if(frequency.getValue().getText().contains("Custom")) {
					                    	   
					                    	   minF_value = Double.parseDouble(minF.getText()) * 1e6;
						                       maxF_value = Double.parseDouble(maxF.getText()) * 1e6;
					                    	   step_value = Double.parseDouble(step.getText()) * 1e6;

					                       }else {
						                       
					                    	   minF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
						                       maxF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
					                    	   step_value = 51.75e3;
						                       
					                       }
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
				                    DiffBetweenModelsController.generateDiffBT0KHM1(headings, 
				                    		k1_value, k2_value, k3_value, h1_value, h2_value,
				                    		Roc_value, ac_value,L0_value,Linf_value,fm_value,Nb_value,
				                    		g0_value,Nge_value,C0_value,Cinf_value,Nce_value
				                    		,cableLength_value,minF_value, maxF_value, step_value,axisScale,parameter, true);
				            	
				            	}
			            	
			            	
			            }
			        });
        	        
			        final int lineFrequencyCustom = line;
			        
					frequency.valueProperty().addListener(new ChangeListener<Label>() {
			            @Override
			            public void changed(ObservableValue<? extends Label> observable, Label oldValue, Label newValue) {

			            	if(newValue.getText().contains("Custom")) {
			            		
			            		/*ADDING LINE*/
			                    minF.setMaxWidth(Double.MAX_VALUE);
			                    grid.add(minF, 0, lineFrequencyCustom, 1, 1);
			                    GridPane.setHalignment(minF, HPos.CENTER);
			                    GridPane.setValignment(minF, VPos.CENTER);
			                    
			                    maxF.setMaxWidth(Double.MAX_VALUE);
			                    grid.add(maxF, 1, lineFrequencyCustom, 1, 1);
			                    GridPane.setHalignment(maxF, HPos.CENTER);
			                    GridPane.setValignment(maxF, VPos.CENTER);
			                    
			                    step.setMaxWidth(Double.MAX_VALUE);
			                    grid.add(step, 2, lineFrequencyCustom, 1, 1);
			                    GridPane.setHalignment(step, HPos.CENTER);
			                    GridPane.setValignment(step, VPos.CENTER);
			            		
			            	}
			            	
			            }
			        });
					
					line++;
			        
        	        fileInput.setMaxWidth(Double.MAX_VALUE);
        	        grid.add(fileInput, 0, line, 1, 1);
        	        GridPane.setHalignment(fileInput, HPos.CENTER);

			        calc.setMaxWidth(Double.MAX_VALUE);
        	        grid.add(calc, 1, line, 1, 1);
        	        GridPane.setHalignment(calc, HPos.CENTER);

        	        outputFile.setMaxWidth(Double.MAX_VALUE);
        	        grid.add(outputFile, 2, line, 1, 1);
        	        GridPane.setHalignment(outputFile, HPos.CENTER);
        	        
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
