package MultiCable;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class MultiCableController {

	public static Vector<Map<Integer, Integer>> organizeInMap(Circle start, Map<Integer, Line> mapCableSegments, Vector<Circle> endPoints) {
				
		Vector<Map<Integer, Integer>> map = new Vector<Map<Integer, Integer>>();
		Map<Integer, Integer> way = new LinkedHashMap<Integer, Integer>();

		map.add(way);
		
		/*VAR TO CONTROL THE ACTUAL SEGMENT ANALISED*/
		int actualSegment = 0;
		
		/*GET THE FIRST SEGMENT(OBS.: START = -1)*/
		for(int i = 0; i < mapCableSegments.size(); i++) {
			
			if(start.getBoundsInParent().intersects(mapCableSegments.get(i).getBoundsInParent())) {
			
				way.put(new Integer(-1), i);
				actualSegment = i;
				
			}
			
		}
			
		/*GET ALL SEGMENTS CONNECTED TO OTHER SEGMENTS*/
		MultiCableController.putOnGraph(map, new LinkedHashMap<Integer, Line>(mapCableSegments), actualSegment, 0);
		
		/*GET ALL SEGMENTS CONNECTED TO ONE END POINT*/
		for(int i = 0; i < endPoints.size(); i++) {
			for(int j = 0; j < mapCableSegments.size(); j++) {				
				if(endPoints.get(i).getBoundsInParent().intersects(mapCableSegments.get(j).getBoundsInParent())) {
					for(int k = 0; k < map.size(); k++) {
						if(map.get(k).containsValue(j)) {
							map.get(k).put(j,-2);
						}
					}
				}
			}
		}
		
		return map;
		
	}
		
	public static void putOnGraph(Vector<Map<Integer, Integer>> map, Map<Integer, Line> mapCableSegments, int actualSegment, int comeFromBifurcationOfNPaths) {
		
		/*GET THE NUMBER OF BIFURCATION*/
		int bifurcation = MultiCableController.getNumberBifurcation(mapCableSegments, actualSegment);
		bifurcation = bifurcation - comeFromBifurcationOfNPaths;		
		
		if(bifurcation > 1) {

			/*GENERATE THE BIFURCATION*/
			Vector<Map<Integer, Integer>> deleted = new Vector<Map<Integer, Integer>>();

			for(int i = 0; i < map.size(); i++) {
				if(map.get(i).containsValue(actualSegment)) {
					deleted.add(map.get(i));
					map.remove(map.get(i));
				}
			}
			
			for(int i = 0; i < deleted.size(); i++) {	
				for(int j = 0; j < bifurcation; j++) {
					map.add(new LinkedHashMap<Integer, Integer>(deleted.get(i)));
				}
			}

		}else if(bifurcation == 0) return;
		
		/*ADD THE NEXT SEGMENT TO EACH BIFURCATION*/
		for(int i = 0; i < map.size(); i++) {
			
			int position = -1;
			
			for(Integer key : mapCableSegments.keySet()) {
				
				if( mapCableSegments.get(actualSegment).getBoundsInParent().intersects( mapCableSegments.get(key).getBoundsInParent() ) ) {

					position = key;
			
					boolean alreadyPutted = false;
					
					for(int k = 0; k < i; k++) {
						
						if(map.get(k).containsValue(position)) {

							alreadyPutted = true;
							
						}
							
					}

					if(!alreadyPutted && actualSegment != position) {
						map.get(i).put(actualSegment, position);	
						break;
					}
					
				}	
				
			}
			
		}
				
		/*ELIMINATE THE SEGMENT PERCORRED*/
		mapCableSegments.remove(actualSegment);
		
		/*REMAKE THE PROCESS TO EACH NEW MAP*/
		for(int i = 0; i < map.size(); i++) {

			if(mapCableSegments.size() != 0) {

				MultiCableController.putOnGraph(map, mapCableSegments, map.get(i).get(actualSegment), bifurcation - 1);

			}			

		}
		
	}

	public static int getNumberBifurcation(Map<Integer, Line> mapCableSegments, int segment) {
		
		int num = 0;
		
		for(Integer key : mapCableSegments.keySet())
			if ( mapCableSegments.containsKey(segment) )
				if(mapCableSegments.get(segment).getBoundsInParent().intersects(mapCableSegments.get(key).getBoundsInParent()) && segment != key)
					num++;
		
		
		return num;
		
	}
		
}