package charts;

import java.util.Vector;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class HoverNode extends StackPane {
	  HoverNode(double x, double y) {
		  setPrefSize(15, 15);		      
		  Label label = labelHoverNode(x, y);
		  Vector<Integer> times = new Vector<Integer>();
		  times.add(0);
		  setOnMouseMoved(new EventHandler<MouseEvent>() {
		    @Override public void handle(MouseEvent mouseEvent) {
		      if(label.getParent() != null) {
			      if(times.get(0) == 10) {
			    	  label.getParent().setId("");
				      getChildren().clear();		    	  			    	  
				      times.set(0, 0);
			      }
			      times.set(0, times.get(0) + 1);
		      }else {
			      getChildren().setAll(label);
			      label.getParent().setId("visible");
		      }
		  }
		  });
		  setOnMouseExited(new EventHandler<MouseEvent>() {
		    @Override public void handle(MouseEvent mouseEvent) {
			      if(label.getParent() != null) {
				      label.getParent().setId("");
				      getChildren().clear();		    	  			    	  
			      }
	      }
		  });

	}
	
	private Label labelHoverNode(double x, double y) {
	  final Label label = new Label(x + " = " + y);
	  label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
	  label.setStyle("-fx-padding: 20;-fx-font-size: 14; -fx-font-weight: bold; -fx-opacity: 1.0 ;");
	  label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
	  return label;
	}
}
