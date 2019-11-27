package de.alsclo.voronoi.graph;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

class BisectorMap {

	private final Map<Bisector, Edge> data = new HashMap<>();

	void put(Point a, Point b, Edge e) {
		assert !data.containsKey(new Bisector(a, b));
		data.put(new Bisector(a, b), e);
	}
	
	void put(Collection<Point> sites1, Collection<Point> sites2, Edge e) {
		assert !data.containsKey(new Bisector(sites1, sites2));
		data.put(new Bisector(sites1, sites2), e);
	}

	Edge get(Point a, Point b) {
		return data.get(new Bisector(a, b));
	}
	
	Edge get(Collection<Point> pointsA, Collection<Point> pointsB) {
		return data.get(new Bisector(pointsA, pointsB));
	}

	Collection<Edge> values() {
		return data.values();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		BisectorMap that = (BisectorMap) o;

		return data.size() == that.data.size() && data.keySet().stream()
				.allMatch(that.data.keySet()::contains);
	}

	@Override
	public int hashCode() {
		return data.hashCode();
	}

	Stream<Edge> stream() {
		return data.values().stream();
	}

	
//	@ToString
//	private static class Bisector {
//		private final Set<Point> points1 = new HashSet<Point>(),
//				points2 = new HashSet<Point>();
//
//		/**
//		 * Bisector with only one point in each side
//		 * @param a
//		 * @param b
//		 */
//		public Bisector(Point a, Point b) {
//			points1.add(a);
//			points2.add(b);
//		}
//		
//		/**
//		 * Bisector with multiple points in each side
//		 * @param newPoints1
//		 * @param newPoints2
//		 */
//		public Bisector(Collection<Point> newPoints1, Collection<Point> newPoints2) {
//			points1.addAll(newPoints1);
//			points2.addAll(newPoints2);
//		}
//
//		@Override
//		public boolean equals(Object o) {
//			if (this == o)
//				return true;
//			if (o == null || getClass() != o.getClass())
//				return false;
//			Bisector bisector = (Bisector) o;
//			return (points1.equals(bisector.points1)
//					&& points2.equals(bisector.points2))
//					|| (points1.equals(bisector.points2)
//							&& points2.equals(bisector.points1));
//		}
//
//		@Override
//		public int hashCode() {
//			return points1.hashCode() + points2.hashCode();
//		}
//	}
}
