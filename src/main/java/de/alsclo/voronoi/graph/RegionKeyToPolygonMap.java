package de.alsclo.voronoi.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class RegionKeyToPolygonMap {
	private final Map<Set<Point>, Polygon> data = new HashMap<>();
	
	public void addVertexToSites(Set<Point> regionKey) {
		
	}
	
	/**
	 * Get all region keys
	 * @return
	 */
	public Set<Set<Point>> getKeys() {
		return data.keySet();
	}
	
}
