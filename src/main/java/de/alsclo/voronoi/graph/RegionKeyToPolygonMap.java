package de.alsclo.voronoi.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RegionKeyToPolygonMap {
	private final Map<Set<Point>, Polygon> data = new HashMap<>();

	public void put(Set<Point> regionKey, Vertex v) {
		Polygon p = data.getOrDefault(regionKey, new Polygon(regionKey));
		p.add(v);
		data.put(regionKey, p);
	}
	
	public Polygon get(Set<Point> regionKey) {
		return this.data.get(regionKey);
	}

	/**
	 * Get all region keys
	 * 
	 * @return
	 */
	public Set<Set<Point>> getKeys() {
		return data.keySet();
	}

}
