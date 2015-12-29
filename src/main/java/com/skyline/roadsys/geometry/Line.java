package com.skyline.roadsys.geometry;

import javax.vecmath.*;

import com.skyline.roadsys.streetgraph.*;
import com.skyline.roadsys.util.*;

/*
 * Shamelessly stolen from https://github.com/pazdera/libcity
 */
public class Line {

	public Point first;
	public Point second;

	public Line() {
		first = new Point();
		second = new Point();
	}

	public Line(Point first, Point second) {
		this.first = new Point(first);
		this.second = new Point(second);
	}

	public Line(Point point, Vector3d vector) {
		this.first = new Point(point);
		this.second = new Point(vector);
	}

	/** < Own copyructor is neccessary */
	public Line(Line source) {
		this.first = new Point(source.begining());
		this.second = new Point(source.end());
	}

	public void set(Point begining, Point end) {
		setBegining(begining);
		setEnd(end);
	}

	public void setBegining(Point begining) {
		this.first = begining;
	}

	public void setEnd(Point end) {
		this.second = end;
	}

	public Point begining() {
		return this.first;
	}

	public Point end() {
		return this.second;
	}

	public boolean hasPoint2D(Point point) {

		double lineTest = (point.x - first.x) * (second.y - first.y) -
				(point.y - first.y) * (second.x - first.x);
		return (Math.abs(lineTest) < Units.EPSILON);
	}

	public IntersectionType intersection2D(Line another, Point intersection)
	// SOURCE: http://paulbourke.net/geometry/lineline2d/
	{
		double denominator = ((another.end().y - another.begining().y) * (end().x - begining().x)) -
				((another.end().x - another.begining().x) * (end().y - begining().y)), firstNumerator = ((another.end().x - another.begining().x) * (begining().y - another.begining().y)) -
				((another.end().y - another.begining().y) * (begining().x - another.begining().x));

		if (Math.abs(denominator) <= 0.001) // WARNING: should be
											// libcity::EPSILON
		/* If the denominator is 0, both lines have same direction vector */
		{
			// Lines are parallel thus nonintersecting
			return IntersectionType.PARALLEL;
		}

		double ua = firstNumerator / denominator;

		intersection.x = (begining().x + ua * (end().x - begining().x));
		intersection.y = (begining().y + ua * (end().y - begining().y));

		return IntersectionType.INTERSECTING;
	}

	public double distance(Point point)
	{
		Point closestPoint = nearestPoint(point);
		return closestPoint.distance(point);
	}

	public Point nearestPoint(Point point)
	{
		double parameter;
		Point orthogonalProjection = new Point();

		parameter = (second.x - first.x) * (point.x - first.x) +
				(second.y - first.y) * (point.y - first.y) +
				(second.z - first.z) * (point.z - first.z);

		double d = first.distance(second);
		parameter /= (d * d);

		orthogonalProjection.x = ((1 - parameter) * first.x + second.x * parameter);
		orthogonalProjection.y = ((1 - parameter) * first.y + second.y * parameter);
		orthogonalProjection.z = ((1 - parameter) * first.z + second.z * parameter);

		return orthogonalProjection;
	}

	public double pointPositionTest(Point point)
	{
		// return (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);
		return (second.x - first.x) * (point.y - first.y) - (second.y - first.y) * (point.x - first.x);
	}

	public String toString() {
		return "Line(" + first.toString() + ", " + second.toString() + ")";
	}

	public boolean equals(Line another) {
		return (begining().equals(another.begining()) && end().equals(another.end())) ||
				(begining().equals(another.end()) && end().equals(another.begining()));
	}

}
