package simulador.gplcs.CableSynthesis;

import KHM.KHMController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import java.util.*;
import java.util.regex.Pattern;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.stream.Collectors;
import java.io.PrintStream;
import javafx.stage.DirectoryChooser;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

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
						final String finalTextOfFile = textOfFile;
						/******************************************/

						/*CREATE WINDOW AND GRID*/
						Stage fileWindow = new Stage();
						
						GridPane grid = new GridPane();
						
						ColumnConstraints col1,col2,col3;
						col1 = col2 = col3 = new ColumnConstraints();
			            col1.setPercentWidth(33);
			            col2.setPercentWidth(33);
			            col3.setPercentWidth(33);
			            grid.getColumnConstraints().add(col1);
			            grid.getColumnConstraints().add(col2);
			            grid.getColumnConstraints().add(col3);
			            
			            /*CREATE INPUTS*/
									            			            
			            Text help1 = new Text("File content: ");
						help1.setFont(Font.font(17));
			            help1.setStyle("-fx-font-weight: 700");

			            Text help2 = new Text("Content Formated: ");
						help2.setFont(Font.font(17));
			            help2.setStyle("-fx-font-weight: 700");
						
						Text labelContentFile = new Text(textOfFile);
						labelContentFile.setFont(Font.font("Monospaced",14));
						labelContentFile.maxWidth(screenWidth*50);
						labelContentFile.prefWidth(screenWidth*50);
						labelContentFile.autosize();

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
				        fileFrequency.setMinWidth(screenWidth*18);

				        JFXComboBox<Label> fileScale = new JFXComboBox<Label>();
				        fileScale.getItems().add(new Label("Logarithmic"));
				        fileScale.getItems().add(new Label("Linear"));
				        fileScale.setPromptText("Scale");
				        fileScale.setMinWidth(screenWidth*18);
				        
				        JFXComboBox<Label> fileParameterCalc = new JFXComboBox<Label>();
				        fileParameterCalc.getItems().add(new Label("Propagation Constant"));
				        fileParameterCalc.getItems().add(new Label("Characteristic Impedance"));
				        fileParameterCalc.getItems().add(new Label("Transfer Function"));
				        fileParameterCalc.setPromptText("Parameter to be Calculated");
				        fileParameterCalc.setMinWidth(screenWidth*18);				        
				        
				        JFXButton separate = new JFXButton("Check File");
				        separate.setStyle("-fx-padding: 0.7em 0.57em;-fx-font-size: 14px;-jfx-button-type: RAISED;-fx-background-color: #666;-fx-pref-width: 200;-fx-text-fill: WHITE;-fx-cursor: hand;");

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
				                  
  				                  
				                  /*VERIFY IF HAVE 6 LINES*/
				                  if(linesAndColumns.size() == 6) {
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
				                	  TableView<String[]> table = new TableView();
				                      table.setEditable(false);
				                      
				                      /*CREATING THE COLUMNS OF TABLE*/
				                      
				                      for(int i = 0; i < linesAndColumns.get(0).size(); i++) {
				                    	  TableColumn<String[],String> col = new TableColumn();
				                    	  col.setText(linesAndColumns.get(0).get(i));
				                    	  col.setCellValueFactory((Callback<CellDataFeatures<String[],String>,ObservableValue<String>>)new Callback<TableColumn.CellDataFeatures<String[],String>,ObservableValue<String>>(){public ObservableValue<String>call(TableColumn.CellDataFeatures<String[],String>p){String[]x=p.getValue();if(x!=null&&x.length>0){return new SimpleStringProperty(x[0]);}else{return new SimpleStringProperty("<no name>");}}});
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
				                      formatedTableScroll.setMaxHeight(screenHeight*20);
				                      
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
				        
				        JFXButton calc = new JFXButton("Calculate");
				        calc.setStyle("-fx-padding: 0.7em 0.57em;-fx-font-size: 14px;-jfx-button-type: RAISED;-fx-background-color: #666;-fx-pref-width: 200;-fx-text-fill: WHITE;-fx-cursor: hand;");
				        calc.setOnMousePressed(new EventHandler<MouseEvent>() {
				            public void handle(MouseEvent me) {
				        
				            	
				                
				            }
				        });
						ScrollPane scrollFileContent = new ScrollPane();
						scrollFileContent.setContent(labelContentFile);

						/*LINE 1*/
						grid.add(help1, 0, 0, 3, 1);
						grid.setHalignment(help1, HPos.CENTER);
						grid.setValignment(help1, VPos.CENTER);
						
						/*LINE 2*/
						grid.add(scrollFileContent, 0, 1, 3, 1);
						grid.setHalignment(scrollFileContent, HPos.CENTER);
						grid.setValignment(scrollFileContent, VPos.CENTER);
						
						/*LINE 3*/
						grid.add(fileColumnSeparator, 0, 2, 1, 1);
						grid.setHalignment(fileColumnSeparator, HPos.CENTER);
						grid.setValignment(fileColumnSeparator, VPos.CENTER);
						
						grid.add(fileCableLength, 1, 2, 1, 1);
						grid.setHalignment(fileCableLength, HPos.CENTER);
						grid.setValignment(fileCableLength, VPos.CENTER);

						grid.add(fileFrequency, 2, 2, 1, 1);
						grid.setHalignment(fileFrequency, HPos.CENTER);
						grid.setValignment(fileFrequency, VPos.CENTER);

						/*LINE 4*/
						grid.add(fileScale, 0, 3, 1, 1);
						grid.setHalignment(fileScale, HPos.CENTER);
						grid.setValignment(fileScale, VPos.CENTER);						

						grid.add(fileParameterCalc, 1, 3, 1, 1);
						grid.setHalignment(fileParameterCalc, HPos.CENTER);
						grid.setValignment(fileParameterCalc, VPos.CENTER);						

						grid.add(separate, 2, 3, 1, 1);
						grid.setHalignment(separate, HPos.CENTER);
						grid.setValignment(separate, VPos.CENTER);

						/*LINE 5*/
						grid.add(calc, 0, 4, 3, 1);
						grid.setHalignment(calc, HPos.CENTER);
						grid.setValignment(calc, VPos.CENTER);

						/*LINE 6*/
						grid.add(help2, 0, 5, 3, 1);
						grid.setHalignment(help2, HPos.CENTER);
						grid.setValignment(help2, VPos.CENTER);
						
						grid.setPadding(new Insets(10, 10, 10, 10));
						grid.setVgap(20);
						grid.setHgap(20);
						
						Scene sceneFile = new Scene(grid, screenWidth*60, screenHeight*80);
						
						fileWindow.setResizable(false);
						fileWindow.setScene(sceneFile);
						fileWindow.show();
						
						/******************************************/
        	            
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
