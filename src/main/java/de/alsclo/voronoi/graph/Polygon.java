package de.alsclo.voronoi.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Polygon {
	private Set<Point> key;
	private List<Vertex> vertices;
	private Point centroid;
	private List<Double> angles;

	public Polygon(Set<Point> regionKey) {
		vertices = new LinkedList<Vertex>();
		key = regionKey;
	}

	// return size
	public int size() {
		return vertices.size();
	}

	// edge
	public void add(Vertex e) {
		vertices.add(e);
	}
	
	/**
	 * Get the centroid of this polygon
	 * @return
	 */
	public Point getCentroid() {
		if (centroid == null) {
			double meanX = this.vertices.stream().mapToDouble(e -> e.getLocation().x).average().getAsDouble();
			double meanY = this.vertices.stream().mapToDouble(e -> e.getLocation().y).average().getAsDouble();
			centroid = new Point(meanX, meanY);
		}
		return centroid;
	}

	public String toString() {
		if (size() == 0)
			return "[ ]";
		String s = "";
		List<Vertex> l = new ArrayList<Vertex>(vertices);
		s = s + "[ ";
		for (int i = 0; i <= size(); i++)
			s = s + l.get(i) + " ";
		s = s + "]";
		return s;
	}

}
