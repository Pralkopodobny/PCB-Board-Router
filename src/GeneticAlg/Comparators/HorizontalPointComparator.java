package GeneticAlg.Comparators;

import GeneticAlg.Point;

import java.util.Comparator;

public class HorizontalPointComparator implements Comparator<Point> {
    @Override
    public int compare(Point point, Point t1) {
        return Integer.compare(point.x, t1.x);
    }
}
