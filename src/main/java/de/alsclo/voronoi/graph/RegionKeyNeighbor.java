package de.alsclo.voronoi.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RegionKeyNeighbor {

	Map<Set<Point>, Set<Set<Point>>> neighbors;

	public RegionKeyNeighbor() {
		neighbors = new HashMap<Set<Point>, Set<Set<Point>>>();
	}

	public void addAdjacentSites(Point a, Point b) {
		Set<Point> sites1 = new HashSet<Point>();
		sites1.add(a);
		Set<Point> sites2 = new HashSet<Point>();
		sites2.add(b);

		Set<Set<Point>> sites1neighbors = this.neighbors.getOrDefault(sites1,
				new HashSet<Set<Point>>());
		sites1neighbors.add(sites2);
		this.neighbors.put(sites1, sites1neighbors);

		Set<Set<Point>> sites2neighbors = this.neighbors.getOrDefault(sites2,
				new HashSet<Set<Point>>());
		sites2neighbors.add(sites1);
		this.neighbors.put(sites2, sites2neighbors);

	}

	public void addAdjacentSites(Collection<Point> points1,
			Collection<Point> points2) {
		Set<Point> sites1 = new HashSet<Point>(points1);
		Set<Point> sites2 = new HashSet<Point>(points2);

		Set<Set<Point>> sites1neighbors = this.neighbors.getOrDefault(sites1,
				new HashSet<Set<Point>>());
		sites1neighbors.add(sites2);
		this.neighbors.put(sites1, sites1neighbors);

		Set<Set<Point>> sites2neighbors = this.neighbors.getOrDefault(sites2,
				new HashSet<Set<Point>>());
		sites2neighbors.add(sites1);
		this.neighbors.put(sites2, sites2neighbors);

	}
	
	/**
	 * Return neighbors of a given site
	 * @param p
	 * @return
	 */
	public Set<Set<Point>> getNeighbors(Point p) {
		Set<Point> sites = new HashSet<Point>();
		sites.add(p);
		return this.neighbors.get(sites); 
	}
	
	/**
	 * Return neighbors of a given site(s)
	 * @param p
	 * @return
	 */
	public Set<Set<Point>> getNeighbors(Set<Point> sites) {
		return this.neighbors.get(sites); 
	}
	
	
	/**
	 * Get all region keys
	 * @return
	 */
	public Set<Set<Point>> getKeys() {
		return neighbors.keySet();
	}
}
