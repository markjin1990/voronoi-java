package de.alsclo.voronoi.graph;

import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@EqualsAndHashCode
public class Graph {

	private final BisectorMap edges = new BisectorMap();
	// TODO: update this
	private final RegionKeyToPolygonMap keys2Polygon = new RegionKeyToPolygonMap();
	private final RegionKeyNeighbor regionNeighbors = new RegionKeyNeighbor();
	private final Set<Point> sites = new HashSet<>();

	public void addEdge(Edge e) {
		edges.put(e.getSites1(), e.getSites2(), e);
		regionNeighbors.addAdjacentSites(e.getSites1(), e.getSites2());
		
		keys2Polygon.put(e.getSites1(), e.getA());
		keys2Polygon.put(e.getSites1(), e.getB());
		keys2Polygon.put(e.getSites2(), e.getA());
		keys2Polygon.put(e.getSites2(), e.getB());

	}

	/**
	 * Gets the edge that bisects the space between the two sites if there is
	 * one.
	 *
	 * @param a
	 *            point of the first site
	 * @param b
	 *            point of the second site
	 * @return an Optional containing the bisecting edge or an empty Optional if
	 *         none exist
	 */
	public Optional<Edge> getEdgeBetweenSites(Point a, Point b) {
		return Optional.ofNullable(edges.get(a, b));
	}

	public Set<Set<Point>> getNeighbors(Point p) {
		return this.regionNeighbors.getNeighbors(p);
	}
	
	public Set<Set<Point>> getNeighbors(Set<Point> regionKey) {
		return this.regionNeighbors.getNeighbors(regionKey);
	}
	
	public Polygon getPolygon(Set<Point> regionKey) {
		return this.keys2Polygon.get(regionKey);
	}

	public Graph clone() {
		Graph clonedGraph = new Graph();
		this.getSitePoints().forEach(p -> clonedGraph.addSite(p));
		this.edgeStream().forEach(e -> clonedGraph.addEdge(e));
		return clonedGraph;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Graph[");
		edges.values().stream()
				.map(e -> String.format("(%s,%s),\n", e.getA(), e.getB()))
				.sorted().forEachOrdered(sb::append);
		sb.append("]");
		return sb.toString();
	}

	public void addSite(Point newSite) {
		sites.add(newSite);
	}

	public Set<Point> getSitePoints() {
		return Collections.unmodifiableSet(sites);
	}
	
	/**
	 * Get all region keys
	 * @return
	 */
	public Set<Set<Point>> getRegionKeys() {
		return this.regionNeighbors.getKeys();
	}

	public Stream<Edge> edgeStream() {
		return edges.stream();
	}

}
