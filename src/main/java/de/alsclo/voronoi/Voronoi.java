package de.alsclo.voronoi;

import de.alsclo.voronoi.beachline.Beachline;
import de.alsclo.voronoi.event.Event;
import de.alsclo.voronoi.event.SiteEvent;
import de.alsclo.voronoi.graph.Edge;
import de.alsclo.voronoi.graph.Graph;
import de.alsclo.voronoi.graph.Point;
import de.alsclo.voronoi.graph.Polygon;
import de.alsclo.voronoi.graph.Vertex;
import lombok.Getter;
import lombok.val;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.lang.Math;

/**
 * The main class and entry point of voronoi-java. Constructing a new instance
 * of this class will generate the diagram in the form of a
 * {@link de.alsclo.voronoi.graph.Graph}.
 *
 */
public class Voronoi {

	@Getter
	private final Graph graph;
	@Getter
	private Queue<Graph> voronoiQ = new LinkedList<Graph>();

	@Getter
	private final int order;

	/**
	 * Builds an unbounded voronoi diagram based on the given site points.
	 *
	 * @param points
	 *            the site points
	 */
	public Voronoi(Collection<Point> points, int k) {
		this.order = k;
		Graph curGraph = findOriginalVoronoi(points);
		while (k > 1) {
			// Save this graph to queue
			voronoiQ.add(curGraph);
			curGraph = findHigherOrderVoronoi(curGraph);
			k--;
		}
		this.graph = curGraph;
	}

	public Voronoi(Collection<Point> points) {
		this.order = 1;
		this.graph = findOriginalVoronoi(points);
	}

	/**
	 * Find Voronoi diagram using Fortune's algorithm
	 * 
	 * @param points
	 */
	public Graph findOriginalVoronoi(Collection<Point> points) {
		Graph tempGraph = new Graph();
		val queue = new PriorityQueue<Event>();
		points.stream().map(SiteEvent::new).forEach(queue::offer);
		points.forEach(tempGraph::addSite);

		val beachline = new Beachline();
		double sweep = Double.MAX_VALUE;
		while (!queue.isEmpty()) {
			val e = queue.peek();
			assert e.getPoint().y <= sweep;
			e.handle(queue, beachline, tempGraph);
			queue.remove(e);
			sweep = e.getPoint().y;
		}
		return tempGraph;
	}

	public Graph findHigherOrderVoronoi(Graph graph) {
		Graph newGraph = graph.clone();

		// Partition each polygon
		for (Set<Point> regionKey : newGraph.getRegionKeys()) {
			Polygon targetPolygon = this.graph.getPolygon(regionKey);
			Set<Set<Point>> neighborRegionKeys = graph.getNeighbors(regionKey);
			
			// Sort neighbor region key clockwise
			List<Set<Point>> sortedNeighborRegionKeys = new ArrayList<Set<Point>>(
					neighborRegionKeys);
			Collections.sort(sortedNeighborRegionKeys, new Comparator() {
				@Override
				public int compare(Object o1, Object o2) {
					Set<Point> k1 = ((Set<Point>) o1);
					Set<Point> k2 = ((Set<Point>) o2);

					Polygon p1 = graph.getPolygon(k1);
					Polygon p2 = graph.getPolygon(k2);

					Point centroidPolygon1 = p1.getCentroid();
					Point centroidPolygon2 = p2.getCentroid();
					Point centroidTarPolygon = targetPolygon.getCentroid();

					double angle1 = Math.toDegrees(Math.atan2(
							centroidPolygon1.x - centroidTarPolygon.x,
							centroidPolygon1.y - centroidTarPolygon.y));
					// Keep angle between 0 and 360
					angle1 = angle1 + Math.ceil(-angle1 / 360) * 360;
					
					double angle2 = Math.toDegrees(Math.atan2(
							centroidPolygon2.x - centroidTarPolygon.x,
							centroidPolygon2.y - centroidTarPolygon.y));
					// Keep angle between 0 and 360
					angle2 = angle2 + Math.ceil(-angle2 / 360) * 360;

					if (angle1 == angle2) {
						return 0;
					} else
						return angle1 > angle2 ? 1 : -1;
				}
			});			
		}

		// Merge partitioned polygons

		return newGraph;
	}

	/**
	 * Applies the bounding box with the specified coordinates to a copy of this
	 * voronoi diagram. If this voronoi diagram is already unbounded an
	 * identical copy is returned.
	 *
	 * @param x
	 *            the x coordinate of the bounding box
	 * @param y
	 *            the y coordinate of the bounding box
	 * @param width
	 *            the width of the bounding box
	 * @param height
	 *            the height of the bounding box
	 *
	 * @return a copy of this voronoi digram bounded by the given bounding box
	 * @throws IllegalArgumentException
	 *             if any site point of this diagram lies outside the given
	 *             bounding box
	 */
	public Voronoi applyBoundingBox(double x, double y, double width,
			double height) {
		getGraph().getSitePoints().stream().filter(
				p -> p.x < x || p.x > x + width || p.y < y || p.y > y + height)
				.findAny().ifPresent(p -> {
					throw new IllegalArgumentException(
							"Site " + p + " lies outside the bounding box.");
				});
		throw new UnsupportedOperationException("Not implemented.");// TODO
	}

	/**
	 * Computes a lloyd relaxation of this voronoi diagram. The original diagram
	 * remains unchanged.
	 * 
	 * @see <a href="https://en.wikipedia.org/wiki/Lloyd's_algorithm">Lloyd's
	 *      algorithm on wikipedia</a>
	 *
	 * @return a new voronoi diagram representing the lloyd relaxation of this
	 *         one
	 */
	public Voronoi relax() {
		Map<Set<Point>, Set<Edge>> edges = new HashMap<>();

		graph.getSitePoints().forEach(p -> {
			Set<Point> key = new HashSet<Point>();
			key.add(p);
			edges.put(key, new HashSet<>());
		});
		graph.edgeStream().forEach(e -> {
			edges.get(e.getSites1()).add(e);
			edges.get(e.getSites2()).add(e);
		});
		List<Point> newPoints = graph.getSitePoints().stream().map(site -> {
			Set<Point> key = new HashSet<Point>();
			key.add(site);

			Set<Vertex> vertices = Stream
					.concat(edges.get(key).stream().map(Edge::getA),
							edges.get(key).stream().map(Edge::getB))
					.collect(Collectors.toSet());
			if (vertices.isEmpty() || vertices.contains(null)) {
				return site;
			} else {
				double avgX = vertices.stream()
						.mapToDouble(v -> v.getLocation().x).average()
						.getAsDouble();
				double avgY = vertices.stream()
						.mapToDouble(v -> v.getLocation().y).average()
						.getAsDouble();
				return new Point(avgX, avgY);
			}
		}).collect(Collectors.toList());
		return new Voronoi(newPoints);
	}

}
