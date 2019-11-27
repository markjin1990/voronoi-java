package de.alsclo.voronoi.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import lombok.ToString;

@ToString
public class Bisector {
	private final Set<Point> points1 = new HashSet<Point>(),
			points2 = new HashSet<Point>();

	/**
	 * Bisector with only one point in each side
	 * @param a
	 * @param b
	 */
	public Bisector(Point a, Point b) {
		points1.add(a);
		points2.add(b);
	}
	
	/**
	 * Bisector with multiple points in each side
	 * @param newPoints1
	 * @param newPoints2
	 */
	public Bisector(Collection<Point> newPoints1, Collection<Point> newPoints2) {
		points1.addAll(newPoints1);
		points2.addAll(newPoints2);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Bisector bisector = (Bisector) o;
		return (points1.equals(bisector.points1)
				&& points2.equals(bisector.points2))
				|| (points1.equals(bisector.points2)
						&& points2.equals(bisector.points1));
	}

	@Override
	public int hashCode() {
		return points1.hashCode() + points2.hashCode();
	}
}
