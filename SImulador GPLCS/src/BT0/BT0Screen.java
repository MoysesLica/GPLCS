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

public class BT0Screen {

    /*CREATE WINDOW FOR INPUT FILE*/
    public static GridPane getInputFileWindow(Stage primaryStage, File file) {
    	
        	GridPane grid = new GridPane();
			grid.setPadding(new Insets(50, 50, 50, 50));
			grid.setVgap(20);
			grid.setHgap(20);

        	if(file != null) {
        		Scanner scanner;
				try {
					scanner = new Scanner(file);
        	        String textOfFile = "";
					while (scanner.hasNextLine()) {
						textOfFile += scanner.nextLine() + "\n";
        	        }
					final String finalTextOfFile = textOfFile;
					/******************************************/		
					
					ColumnConstraints col1,col2,col3;
					col1 = col2 = col3 = new ColumnConstraints();
		            col1.setPercentWidth(33);
		            col2.setPercentWidth(33);
		            col3.setPercentWidth(33);
		            grid.getColumnConstraints().add(col1);
		            grid.getColumnConstraints().add(col2);
		            grid.getColumnConstraints().add(col3);

					RowConstraints row1, row2, row3, row4, row5, row6, row7;
					row1 = new RowConstraints();
					row2 = new RowConstraints();
					row3 = new RowConstraints();
					row4 = new RowConstraints();
					row5 = new RowConstraints();
					row6 = new RowConstraints();
					row7 = new RowConstraints();
		            row1.setPercentHeight(10);
		            row2.setPercentHeight(25);
		            row3.setPercentHeight(10);
		            row4.setPercentHeight(10);
		            row5.setPercentHeight(10);
		            row6.setPercentHeight(10);
		            row7.setPercentHeight(25);
		            grid.getRowConstraints().addAll(row1,row2,row3,row4,row5,row6,row7);
		            
		            /*CREATE INPUTS*/
								            			            
		            Text help1 = new Text("File content: ");
					help1.setFont(Font.font("System",FontWeight.BOLD,17));

		            Text help2 = new Text("Content Formated: ");
					help2.setFont(Font.font("System",FontWeight.BOLD,17));
					
					Text labelContentFile = new Text(textOfFile);
					labelContentFile.setFont(Font.font("Monospaced",14));
					labelContentFile.maxWidth(Double.MAX_VALUE);
					
			        JFXTextField fileColumnSeparator = new JFXTextField();
			        fileColumnSeparator.setLabelFloat(true);
			        fileColumnSeparator.setPromptText("Input the column separator character");

			        JFXTextField fileCableLength = new JFXTextField();
			        fileCableLength.setLabelFloat(true);
			        fileCableLength.setPromptText("Input the cable length");

			        JFXComboBox<Label> fileFrequency = new JFXComboBox<Label>();
			        fileFrequency.getItems().add(new Label("2.2MHz - 106MHz"));
			        fileFrequency.getItems().add(new Label("2.2MHz - 212MHz"));
			        fileFrequency.getItems().add(new Label("2.2MHz - 424MHz"));
			        fileFrequency.getItems().add(new Label("2.2MHz - 848MHz"));
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
			        Region iconSeparate = GlyphsStack.create().add(
			        		GlyphsBuilder.create(FontAwesomeIcon.class)
			        			.icon(FontAwesomeIconName.CHECK)
			        			.style("-fx-fill: white;")
			        			.size("1em")
			        			.build()
			        		);
			        JFXButton separate = new JFXButton("Check File", iconSeparate);
			        separate.setId("separate");
			        separate.setMaxWidth(Double.MAX_VALUE);
			        
			        final Boolean checked = new Boolean(false);
			        
			        /*CHECK FILE*/
			        separate.setOnMousePressed(new EventHandler<MouseEvent>() {
			            public void handle(MouseEvent me) {
			            	
			            	/*VERIFY IF COLUMN SEPARATOR IS GIVED*/
			            	if(!fileColumnSeparator.getText().isEmpty()) {
			            	
			            	  boolean error = false;
			            		
			                  ArrayList<String> lines = new ArrayList(Arrays.asList(finalTextOfFile.split("\n")));
			                  /*REMOVE EMPTY LINES*/
			                  for(int i = 0; i < lines.size(); i++)
			                	  if(lines.get(i).isEmpty())
			                		  lines.remove(i);
			                  
			                  ArrayList<ArrayList<String>> linesAndColumns = new ArrayList<ArrayList<String>>();
			                  
				                  for(int i = 0; i < lines.size(); i++){
				                	  
					                  ArrayList<String> columnData = new ArrayList<String>(Arrays.asList(lines.get(i).split(Pattern.quote(fileColumnSeparator.getText().trim()))));
					                  linesAndColumns.add(columnData);
			                  }
			                  
				                  
			                  /*VERIFY IF HAVE 12 LINES*/
			                  if(linesAndColumns.size() == 12) {
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
			                      
			                      grid.add(formatedTableScroll, 0, 6, 3, 1);
			                      				                      
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
			        /*SEND DATA TO CALCULATE*/
			        calc.setOnMousePressed(new EventHandler<MouseEvent>() {
			            public void handle(MouseEvent me) {
			                boolean error = false;
			            	
			                /*              CHECK FILE           */
			                ArrayList<ArrayList<String>> linesAndColumns = new ArrayList<ArrayList<String>>();
			                
			            	/*VERIFY IF COLUMN SEPARATOR IS GIVED*/
			            	if(!fileColumnSeparator.getText().isEmpty()) {
			            					            		
			                  ArrayList<String> lines = new ArrayList(Arrays.asList(finalTextOfFile.split("\n")));
			                  /*REMOVE EMPTY LINES*/
			                  for(int i = 0; i < lines.size(); i++)
			                	  if(lines.get(i).isEmpty())
			                		  lines.remove(i);
			                  
			                  
				                  for(int i = 0; i < lines.size(); i++){
				                	  
					                  ArrayList<String> columnData = new ArrayList<String>(Arrays.asList(lines.get(i).split(Pattern.quote(fileColumnSeparator.getText().trim()))));
					                  linesAndColumns.add(columnData);
			                  }
			                  
				                  
			                  /*VERIFY IF HAVE 12 LINES*/
			                  if(linesAndColumns.size() == 12) {
				                  int numberColumns = linesAndColumns.get(0).size();
				                  for(int i = 1; i < linesAndColumns.size(); i++) {
				                	  /*VERIFY IF HAVE THE SAME LENGTH OF COLUMNS*/
				                	  if(linesAndColumns.get(i).size() != numberColumns) {
				                		  error = true;
				                	  }
				                	  /*VERIFY IF ALL CELLS AFTER 1 IS NUMBERS*/
				                	  for(int j = 0; j < linesAndColumns.get(i).size(); j++) {
				                		  try {
			                				  Double.parseDouble(linesAndColumns.get(i).get(j));
			                			  }catch(NumberFormatException e) {
			                				  error = true;
			                			  }
					                  }  
				                  }

			                  }else {
			                	  error = true;  
			                  }
			                  
			            	}else {
			            		error = true;
			            	}
			                /*              END CHECK FILE            */
			            	
			                   double minF = 0;
			                   double maxF = 0;
			                   double cableLength_value = 0;
			                   String axisScale = "";
			                   String parameter = "";
			                   
			                   /*VALIDATE INFO'S*/
			                   try{
			                       cableLength_value = Double.parseDouble(fileCableLength.getText());
			                       minF = Double.parseDouble(fileFrequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
			                       maxF = Double.parseDouble(fileFrequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
			                       axisScale = fileScale.getValue().getText();
			                       parameter = fileParameterCalc.getValue().getText();
			                   }catch(NumberFormatException e){
			                	   error = true;
			                   }
			                   
			                   if(error) {
			                	   Alert alert = new Alert(AlertType.ERROR);
			                       alert.setTitle("Error");
			                       alert.setHeaderText("Error, please fill correctly the inputs before continue, if inputs are correctly check file consistence!");
			                       alert.showAndWait();
			                       return;				                       
			                   }else {
			                	   
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
			                       
			                       Vector<String> headings = new Vector<String>();
			                	   
				                   for(int i = 0; i < linesAndColumns.get(0).size(); i++)
				                	   headings.add(linesAndColumns.get(0).get(i));

				                   for(int i = 0; i < linesAndColumns.get(1).size(); i++)
				                	   Roc_value.add(Double.parseDouble(linesAndColumns.get(1).get(i)));

				                   for(int i = 0; i < linesAndColumns.get(2).size(); i++)
				                	   ac_value.add(Double.parseDouble(linesAndColumns.get(2).get(i)));

				                   for(int i = 0; i < linesAndColumns.get(3).size(); i++)
				                	   L0_value.add(Double.parseDouble(linesAndColumns.get(3).get(i)));

				                   for(int i = 0; i < linesAndColumns.get(4).size(); i++)
				                	   Linf_value.add(Double.parseDouble(linesAndColumns.get(4).get(i)));

				                   for(int i = 0; i < linesAndColumns.get(5).size(); i++)
				                	   fm_value.add(Double.parseDouble(linesAndColumns.get(5).get(i)));

				                   for(int i = 0; i < linesAndColumns.get(6).size(); i++)
				                	   Nb_value.add(Double.parseDouble(linesAndColumns.get(6).get(i)));

				                   for(int i = 0; i < linesAndColumns.get(7).size(); i++)
				                	   g0_value.add(Double.parseDouble(linesAndColumns.get(7).get(i)));

				                   for(int i = 0; i < linesAndColumns.get(8).size(); i++)
				                	   Nge_value.add(Double.parseDouble(linesAndColumns.get(8).get(i)));

				                   for(int i = 0; i < linesAndColumns.get(9).size(); i++)
				                	   C0_value.add(Double.parseDouble(linesAndColumns.get(9).get(i)));

				                   for(int i = 0; i < linesAndColumns.get(10).size(); i++)
				                	   Cinf_value.add(Double.parseDouble(linesAndColumns.get(10).get(i)));

				                   for(int i = 0; i < linesAndColumns.get(11).size(); i++)
				                	   Nce_value.add(Double.parseDouble(linesAndColumns.get(11).get(i)));

				                   /*GENERATE GRAPHS*/
				                   BT0Controller.generateGraphs(headings, Roc_value, ac_value,L0_value,Linf_value,fm_value,Nb_value,g0_value,Nge_value,C0_value,Cinf_value,Nce_value, cableLength_value, minF, maxF, 51.75e3, axisScale, parameter);
			                       
			                   }
			                   				            	
			           }
			        });
			        				        
					ScrollPane scrollFileContent = new ScrollPane();
					scrollFileContent.setContent(labelContentFile);

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
			            	primaryStage.getScene().setRoot(BT0Screen.getBT0Screen(primaryStage));
			            	String css = BT0Screen.class.getResource("BT0Screen.css").toExternalForm(); 
			            	primaryStage.getScene().getStylesheets().clear();
			            	primaryStage.getScene().getStylesheets().add(css);

			            }
			        });
					
					/*LINE 1*/
					grid.add(help1, 0, 0, 3, 1);
					GridPane.setHalignment(help1, HPos.CENTER);
					GridPane.setValignment(help1, VPos.CENTER);
					
					/*LINE 2*/
					grid.add(scrollFileContent, 0, 1, 3, 1);
					GridPane.setHalignment(scrollFileContent, HPos.CENTER);
					GridPane.setValignment(scrollFileContent, VPos.CENTER);
					
					/*LINE 3*/
					grid.add(fileColumnSeparator, 0, 2, 1, 1);
					GridPane.setHalignment(fileColumnSeparator, HPos.CENTER);
					GridPane.setValignment(fileColumnSeparator, VPos.CENTER);
					
					grid.add(fileCableLength, 1, 2, 1, 1);
					GridPane.setHalignment(fileCableLength, HPos.CENTER);
					GridPane.setValignment(fileCableLength, VPos.CENTER);

					grid.add(fileFrequency, 2, 2, 1, 1);
					GridPane.setHalignment(fileFrequency, HPos.CENTER);
					GridPane.setValignment(fileFrequency, VPos.CENTER);

					/*LINE 4*/
					grid.add(fileScale, 0, 3, 1, 1);
					GridPane.setHalignment(fileScale, HPos.CENTER);
					GridPane.setValignment(fileScale, VPos.CENTER);						

					grid.add(fileParameterCalc, 1, 3, 1, 1);
					GridPane.setHalignment(fileParameterCalc, HPos.CENTER);
					GridPane.setValignment(fileParameterCalc, VPos.CENTER);						

					grid.add(separate, 2, 3, 1, 1);
					GridPane.setHalignment(separate, HPos.CENTER);
					GridPane.setValignment(separate, VPos.CENTER);

					/*LINE 5*/
					grid.add(calc, 1, 4, 1, 1);
					GridPane.setHalignment(calc, HPos.CENTER);
					GridPane.setValignment(calc, VPos.CENTER);
					back.setMaxWidth(Double.MAX_VALUE);
					grid.add(back, 2, 4, 1, 1);
					GridPane.setHalignment(back, HPos.CENTER);
					GridPane.setValignment(back, VPos.CENTER);

					/*LINE 6*/
					grid.add(help2, 0, 5, 3, 1);
					GridPane.setHalignment(help2, HPos.CENTER);
					GridPane.setValignment(help2, VPos.CENTER);
																
					/******************************************/
    	            
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

        	}
        	
			return grid;
            
       }

	
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
        parameterCalc.getItems().add(new Label("RLGC"));
        parameterCalc.setPromptText("Parameter to be Calculated");

        
        
        
        
        
        
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

                    double minF;
                    double maxF;
                    double cableLength_value;
                    String axisScale;
                    String parameter;
                    
                    /*VALIDATE INFO'S*/
                    try{
                    	
                        Roc_value=Double.parseDouble(Roc.getText());
                        ac_value=Double.parseDouble(ac.getText());
                        L0_value=Double.parseDouble(L0.getText());
                        Linf_value=Double.parseDouble(Linf.getText());
                        fm_value=Double.parseDouble(fm.getText());
                        Nb_value=Double.parseDouble(Nb.getText());
                        g0_value=Double.parseDouble(g0.getText());
                        Nge_value=Double.parseDouble(Nge.getText());
                        C0_value=Double.parseDouble(C0.getText());
                        Cinf_value=Double.parseDouble(Cinf.getText());
                        Nce_value=Double.parseDouble(Nce.getText());

                        cableLength_value = Double.parseDouble(cableLength.getText());
                        minF = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
                        maxF = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
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
                    BT0Controller.generateOutputFile(Roc_value, ac_value,L0_value,Linf_value,fm_value,Nb_value,g0_value,Nge_value,C0_value,Cinf_value,Nce_value, cableLength_value, minF, maxF, 51.75e3, axisScale, parameter, selectedDirectory);
                	
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

            		primaryStage.getScene().setRoot(BT0Screen.getInputFileWindow(primaryStage, file));
            		String css = BT0Screen.class.getResource("InputFileWindow.css").toExternalForm(); 
                	primaryStage.getScene().getStylesheets().clear();
                	primaryStage.getScene().getStylesheets().add(css);

            	}
            	
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
        fileInput.setMaxWidth(Double.MAX_VALUE);
        grid.add(fileInput, 0, line, 1, 1);
        GridPane.setHalignment(fileInput, HPos.CENTER);
        GridPane.setValignment(fileInput, VPos.CENTER);
        calculate.setMaxWidth(Double.MAX_VALUE);
        grid.add(calculate, 1, line, 1, 1);
        GridPane.setHalignment(calculate, HPos.CENTER);
        GridPane.setValignment(calculate, VPos.CENTER);
        outputFile.setMaxWidth(Double.MAX_VALUE);
        grid.add(outputFile, 2, line, 1, 1);
        GridPane.setHalignment(outputFile, HPos.CENTER);
        GridPane.setValignment(outputFile, VPos.CENTER);
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
