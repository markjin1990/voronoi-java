package de.alsclo.voronoi.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

//@RequiredArgsConstructor
public class Edge {
	@Getter
	private final Set<Point> sites1 = new HashSet<Point>(),
			sites2 = new HashSet<Point>();

	public Edge(Point a, Point b) {
		this.sites1.add(a);
		this.sites2.add(b);
	}

	public Edge(Collection<Point> points1, Collection<Point> points2) {
		sites1.addAll(points1);
		sites2.addAll(points2);
	}

	@Getter
	private Vertex a, b;

	public void addVertex(Vertex v) {
		if (a == null) {
			a = v;
		} else if (b == null) {
			b = v;
		} else {
			throw new IllegalStateException(
					"Trying to set a third vertex on an edge");
		}
	}

	@Override
	public String toString() {
		return "Edge(" + a + ", " + b + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Edge edge = (Edge) o;

		if (!sites1.equals(edge.sites1))
			return false;
		if (!sites2.equals(edge.sites2))
			return false;
		if (a != null ? !a.equals(edge.a) : edge.a != null)
			return false;
		return b != null ? b.equals(edge.b) : edge.b == null;
	}

	@Override
	public int hashCode() {
		int result = sites1.hashCode();
		result = 31 * result + sites2.hashCode();
		result = 31 * result + (a != null ? a.hashCode() : 0);
		result = 31 * result + (b != null ? b.hashCode() : 0);
		return result;
	}
}
