package simulador.gplcs;


import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.InputStream;

import com.jfoenix.controls.JFXButton;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import simulador.gplcs.CableSynthesis.CableSynthesisController;

/**
 * @author moyses
 */

public class SImuladorGPLCS extends Application {
    
    @Override
    public void start(Stage primaryStage) {

    	/*GET THE WIDTH AND HEIGHT OF SCREEN TO CREATE WINDOW*/
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
        
        /*CREATE THE APPLICATION LABEL*/
        String ApplicationName = "G. Fast Physical Layer and Cable Simulator";
        Label label = new Label(ApplicationName);
        label.setId("ApplicationName");
        label.setAlignment(Pos.CENTER);

        /*CREATE BUTTONS FOR ALL FUNCTIONS OF SIMULATOR*/
        JFXButton button1 = new JFXButton("Cable Synthesis");
        JFXButton button2 = new JFXButton("Cable Analysis");
        JFXButton button3 = new JFXButton("Network Simulation");
        button1.setFocusTraversable(false);
        button2.setFocusTraversable(false);
        button3.setFocusTraversable(false);
        button1.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                primaryStage.setScene(CableSynthesisController.getCableSynthesisScene(primaryStage));
            }
        });
        
        /*CREATE FOOTER*/
        String ApplicationFooter = "UFPA 2018 - Todos os Direitos Reservados";
        Label labelFooter = new Label(ApplicationFooter);
        labelFooter.setId("ApplicationFooter");
        labelFooter.setAlignment(Pos.CENTER);
        
        /*CREATE WINDOW ICON AND TITLE*/
        try {
	        Image image = new Image(getClass().getResourceAsStream("logo_ufpa.png"));
	        primaryStage.getIcons().add(image);
	        primaryStage.setTitle("G. Fast Physical Layer and Cable Simulator");
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        /*ADDING ALL ELEMENTS TO GRID*/
        grid.add(label, 0, 0, 3, 1);
        grid.setHalignment(label, HPos.CENTER);
        grid.add(button1, 0, 1, 1, 1);
        grid.add(button2, 1, 1, 1, 1);
        grid.add(button3, 2, 1, 1, 1);
        grid.add(labelFooter, 0, 2, 3, 1);
        grid.setHalignment(labelFooter, HPos.CENTER);
        grid.setAlignment(Pos.CENTER);
        
        /*CREATE SCROLL PANE*/
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        /*CREATE SCENE*/
        Scene scene = new Scene(scrollPane);
        String css = this.getClass().getResource("MainScreen.css").toExternalForm(); 
        scene.getStylesheets().add(css);
        
        /*SHOW WINDOW*/
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
