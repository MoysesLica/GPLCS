package MultiCable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import TransmissionLine.GenericCableModel;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class MultiCableController {

	public static Vector<Map<Integer, Integer>> organizeInMap(Circle start, Map<Integer, Line> mapCableSegments, Vector<Circle> endPoints) {
				
		Vector<Map<Integer, Integer>> map = new Vector<Map<Integer, Integer>>();
		Map<Integer, Integer> way = new HashMap<Integer, Integer>();

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
		MultiCableController.putOnGraph(map, new HashMap<Integer, Line>(mapCableSegments), actualSegment);

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
	
	/*PAREI AQUI ONDE ESTÁ DANDO ERRO AO PEGAR NÓS CONSECUTIVOS LONGE DO COMEÇO(A PARTIR DO SEGUNDO)*/
	
	public static void putOnGraph(Vector<Map<Integer, Integer>> map, Map<Integer, Line> mapCableSegments, int actualSegment) {
		
		/*JUST TEST IF IN FINAL*/
		try { 
			mapCableSegments.get(actualSegment).getBoundsInParent();
		}
		catch (NullPointerException e) {
		  return;
		}

		
		/*GET THE NUMBER OF BIFURCATION*/
		int bifurcation = MultiCableController.getNumberBifurcation(mapCableSegments, actualSegment);
		
		if(bifurcation != 1) {

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
					map.add(new HashMap<Integer, Integer>(deleted.get(i)));
				}
			}

		}
		
		/*ADD THE NEXT SEGMENT TO EACH BIFURCATION*/
		for(int i = 0; i < map.size(); i++) {
			
			int position = -1;
			
			for(int j = 0; j < mapCableSegments.size(); j++) {
				
				if( mapCableSegments.get(actualSegment).getBoundsInParent().intersects( mapCableSegments.get(j).getBoundsInParent() ) ) {

					position = j;
			
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
			
			MultiCableController.putOnGraph(map, mapCableSegments, map.get(i).get(actualSegment));

		}
		
	}

	public static int getNumberBifurcation(Map<Integer, Line> mapCableSegments, int segment) {
		
		int num = 0;
		
		for(int i = 0; i < mapCableSegments.size(); i++)
			if(mapCableSegments.get(segment).getBoundsInParent().intersects(mapCableSegments.get(i).getBoundsInParent()) && segment != i)
				num++;
		
		
		return num;
		
	}
		
}
