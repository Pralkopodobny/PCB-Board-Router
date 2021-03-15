package GeneticAlg;

import java.util.Objects;

public class Point {
    public int x;
    public int y;
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Point(Point other) {
        this.x = other.x;
        this.y = other.y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x &&
                y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public boolean equals(Point p){
        return x == p.x && y == p.y;
    }

    public boolean onBoard(int maxX, int maxY){
        return x<maxX && x >=0 && y<maxY && y >=0;
    }

    public static void sortVertical(){

    }
}
