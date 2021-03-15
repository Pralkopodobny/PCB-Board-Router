package GeneticAlg.Comparators;

import GeneticAlg.Point;

import java.util.Comparator;

public class VerticalPointComparator implements Comparator<Point> {

    @Override
    public int compare(Point point, Point t1) {
        return Integer.compare(point.y, t1.y);
    }
}
