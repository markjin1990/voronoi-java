package de.alsclo.voronoi.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Polygon {
	private Set<Point> key;
	private List<Edge> edeges;

	public Polygon(Set<Point> regionKey) {
		edeges = new LinkedList<Edge>();
		key = regionKey;
	}

	// return size
	public int size() {
		return edeges.size();
	}

	// edge
	public void add(Edge e) {
		edeges.add(e);
	}

	public String toString() {
		if (size() == 0)
			return "[ ]";
		String s = "";
		List<Edge> l = new ArrayList<Edge>(edeges);
		s = s + "[ ";
		for (int i = 0; i <= size(); i++)
			s = s + l.get(i) + " ";
		s = s + "]";
		return s;
	}

}
