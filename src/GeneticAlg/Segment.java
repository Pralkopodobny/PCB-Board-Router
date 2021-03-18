package GeneticAlg;

import GeneticAlg.Comparators.HorizontalPointComparator;
import GeneticAlg.Comparators.VerticalPointComparator;
import javafx.scene.shape.Line;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.Arrays;
import java.util.Objects;

public class Segment {
    private static VerticalPointComparator vCompare = new VerticalPointComparator();
    private static HorizontalPointComparator hCompare = new HorizontalPointComparator();

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;
        public static Direction getDirection(int i){
            switch (i){
                case 0: return UP;
                case 1: return DOWN;
                case 2: return LEFT;
                default: return RIGHT;
            }
        }
        public int getMaxDistance(int x, int y, int maxX, int maxY){
            switch (this){
                case LEFT: return x;
                case RIGHT: return maxX - x;
                case UP: return y;
                default: return maxY - y;
            }
        }
        public boolean isVertical(){
            switch (this){
                case DOWN:
                case UP:
                    return true;
                default:
                    return false;
            }
        }
        public Direction reverse(){
            switch (this){
                case UP: return DOWN;
                case DOWN: return UP;
                case RIGHT: return LEFT;
                default: return RIGHT;
            }
        }
    }

    private int length;
    private Point start, end;
    private Direction direction;
    private Segment(){};

    public Segment(int x, int y, int length, Direction direction){
        start = new Point(x, y);
        this.length = length;
        this.direction = direction;
        end = createEnd();
    }
    public Segment(Point start, int length, Direction direction){
        this.start = new Point(start);
        this.length = length;
        this.direction = direction;
        end = createEnd();
    }
    public Segment(Segment other){
        this.length = other.length;
        this.direction = other.direction;
        this.start = new Point(other.start);
        this.end = new Point(other.end);
    }

    public static Segment getVerticalSegment(Point start, Point end){
        assert (start.x == end.x);
        if(start.equals(end)) return null;
        Segment segment = new Segment();
        segment.start = new Point(start);
        segment.end = new Point(end);
        segment.length = end.y - start.y;
        if(segment.length < 0){
            segment.length *=-1;
            segment.direction = Direction.UP;
        }
        else{
            segment.direction = Direction.DOWN;
        }
        return segment;
    }
    public static Segment getHorizontalSegment(Point start, Point end){
        assert (start.y == end.y);
        if(start.equals(end)) return null;
        Segment segment = new Segment();
        segment.start = new Point(start);
        segment.end = new Point(end);
        segment.length = end.x - start.x;
        if(segment.length < 0){
            segment.length *=-1;
            segment.direction = Direction.LEFT;
        }
        else{
            segment.direction = Direction.RIGHT;
        }
        return segment;
    }

    private Point createEnd(){
        switch(direction){
            case DOWN:{
                return new Point(start.x, start.y+length);
            }
            case UP:{
                return new Point(start.x, start.y-length);
            }
            case RIGHT:{
                return new Point(start.x + length, start.y);
            }
            default:{
                return new Point(start.x - length, start.y);
            }
        }
    }

    public int getLength() {
        return length;
    }

    public int getX() {
        return start.x;
    }

    public int getY() {
        return start.y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean contains(int x, int y){
        return contains(new Point(x, y));
    }
    public boolean contains(Point p){
        if(direction.isVertical()){
            if(p.x != start.x) return false;
            if(direction == Direction.UP){
                return start.y >= p.y && p.y >= end.y;
            }
            else{
                return start.y <= p.y && p.y <= end.y;
            }
        }
        else {
            if(p.y != start.y) return false;
            if(direction == Direction.RIGHT){
                return start.x <= p.x && p.x <= end.x;
            }
            else{
                return start.x >= p.x && p.x >= end.x;
            }
        }
    }


    public boolean collides(Segment other){
        return Line2D.linesIntersect(start.x, start.y, end.x, end.y, other.getX(), other.getY(), other.getX2(), other.getY2());
    }

    public int getX2() {
        return end.x;
    }

    public int getY2() {
        return end.y;
    }

    public void shortenToPoint(Point p){
        while (!p.equals(end)){
            if(end.equals(start)) throw new IllegalArgumentException();
            switch(direction){
                case UP:
                    end.y ++;
                    length--;
                    break;
                case DOWN:
                    end.y --;
                    length--;
                    break;
                case RIGHT:
                    end.x --;
                    length--;
                    break;
                case LEFT:
                    end.x ++;
                    length--;
                    break;
            }
        }
    }

    public void expandToPoint(Point p){
        while (!p.equals(end)){
            if(end.equals(start)) throw new IllegalArgumentException();
            switch(direction){
                case UP:
                    end.y --;
                    length++;
                    break;
                case DOWN:
                    end.y ++;
                    length++;
                    break;
                case RIGHT:
                    end.x ++;
                    length++;
                    break;
                case LEFT:
                    end.x --;
                    length++;
                    break;
            }
        }
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return start + "->" + end +"("+length+")";
    }

    public Line draw(){
        int scale = BoardConfig.PIXEL_SIZE;
        return new Line(start.x*scale + scale, start.y * scale + scale, end.x * scale + scale, end.y * scale + scale);
    }

    public int notOnBoard(int maxX, int maxY){
        if(start.onBoard(maxX, maxY) && end.onBoard(maxX, maxY)) return 0;
        Point[] sortedStartAndEnd = new Point[2];
        Point[] points = new Point[4];
        points[0] = start;
        points[1] = end;
        int result = 0;
        if(direction.isVertical()){
            if(start.x < 0 || start.x >= maxX) return length;
            points[2] = new Point(start.x, 0);
            sortedStartAndEnd[0] = start;
            points[3] = new Point(start.x, maxY);
            sortedStartAndEnd[1] = end;
            Arrays.sort(points, vCompare);
            Arrays.sort(sortedStartAndEnd, vCompare);
        }
        else{
            if(start.y < 0 || start.y >= maxX) return length;
            points[2] = new Point(0, start.y);
            sortedStartAndEnd[0] = start;
            points[3] = new Point(maxX, start.y);
            sortedStartAndEnd[1] = end;
            Arrays.sort(points, hCompare);
            Arrays.sort(sortedStartAndEnd, hCompare);
        }
        if(direction.isVertical()){
            if(!sortedStartAndEnd[0].onBoard(maxX, maxY)){
                if(!sortedStartAndEnd[1].onBoard(maxX, maxY)){
                    if(sortedStartAndEnd[0] == points[0] && sortedStartAndEnd[1] == points[3]){
                        return points[1].y - points[0].y + points[3].y - points[2].y;
                    }
                    else{
                        return length;
                    }
                }
                else {
                    return length - (points[2].y - points[1].y);
                }
            }
            else{
                return length - (points[2].y - points[1].y);
            }

        }
        else{
            if(!sortedStartAndEnd[0].onBoard(maxX, maxY)){
                if(!sortedStartAndEnd[1].onBoard(maxX, maxY)){
                    if(sortedStartAndEnd[0] == points[0] && sortedStartAndEnd[1] == points[3]){
                        return points[1].x - points[0].x + points[3].x - points[2].x;
                    }
                    else{
                        return length;
                    }
                }
                else {
                    return length - (points[2].x - points[1].x);
                }
            }
            else{
                return length - (points[2].x - points[1].x);
            }
        }
    }

    public Point[] collisionPoints(Segment other){
        if(!collides(other)) return null;

        if(direction.isVertical() != other.direction.isVertical()){
            if(direction.isVertical()){
                return new Point[]{new Point(start.x, other.start.y)};
            }
            else {
                return new Point[]{new Point(other.start.x, start.y)};
            }
        }
        else {
            Segment other2;
            other2 = direction==other.direction?other:other.getReverse();
            if(contains(other2.start)){
                if(contains(other2.end)){
                    return direction.isVertical()?pointsBetweenVertical(other2.getY(), other2.getY2(), other2.getX()):
                            pointsBetweenHorizontal(other2.getX(), other2.getX2(), other2.getY());
                }
                else{
                    return direction.isVertical()?pointsBetweenVertical(other2.getY(), getY2(), getX()):
                            pointsBetweenHorizontal(other2.getX(), getX2(), getY());
                }
            }
            else{
                return direction.isVertical()?pointsBetweenVertical(getY(), other2.getY2(), getX()):
                        pointsBetweenHorizontal(getX(), other2.getX2(), getY());
            }
        }
    }

    public int collisionPointsCount(Segment other){
        if(!collides(other)) return 0;

        if(direction.isVertical() != other.direction.isVertical()){
            return 1;
        }
        else {
            Segment other2;
            other2 = direction==other.direction?other:other.getReverse();
            if(contains(other2.start)){
                if(contains(other2.end)){
                    return direction.isVertical()?pointsBetweenCount(other2.getY(), other2.getY2()):
                            pointsBetweenCount(other2.getX(), other2.getX2());
                }
                else{
                    return direction.isVertical()?pointsBetweenCount(other2.getY(), getY2()):
                            pointsBetweenCount(other2.getX(), getX2());
                }
            }
            else{
                return direction.isVertical()?pointsBetweenCount(getY(), other2.getY2()):
                        pointsBetweenCount(getX(), other2.getX2());
            }
        }
    }

    public Segment[] getSplit(int cut){
        if(cut == 0 || cut >= length) throw new IllegalArgumentException();
        Segment[] newSegments = new Segment[2];
        switch(direction){
            case LEFT:
                newSegments[0] = new Segment(start.x, start.y, cut, Direction.LEFT);
                newSegments[1] = new Segment(start.x - cut, start.y, length - cut, Direction.LEFT);
                break;
            case RIGHT:
                newSegments[0] = new Segment(start.x, start.y, cut, Direction.RIGHT);
                newSegments[1] = new Segment(start.x + cut, start.y, length - cut, Direction.RIGHT);
                break;
            case DOWN:
                newSegments[0] = new Segment(start.x, start.y, cut, Direction.DOWN);
                newSegments[1] = new Segment(start.x, start.y + cut, length - cut, Direction.DOWN);
                break;
            default:
                newSegments[0] = new Segment(start.x, start.y, cut, Direction.UP);
                newSegments[1] = new Segment(start.x, start.y - cut, length - cut, Direction.UP);
                break;
        }
        return newSegments;
    }

    public Segment getReverse(){
        return new Segment(end, length, direction.reverse());
    }

    private Point[] pointsBetweenHorizontal(int x1, int x2, int y){
        int min, max, size;
        if(x1<x2){
            min = x1;
            max = x2;
        }
        else{
            min = x2;
            max = x1;
        }
        size = max-min + 1;
        Point[] points = new Point[size];
        for(int i = 0; i < size; i++){
            points[i] = new Point(min+i, y);
        }
        return points;
    }

    private Point[] pointsBetweenVertical(int y1, int y2, int x){
        int min, max, size;
        if(y1<y2){
            min = y1;
            max = y2;
        }
        else{
            min = y2;
            max = y1;
        }
        size = max-min + 1;
        Point[] points = new Point[size];
        for(int i = 0; i < size; i++){
            points[i] = new Point(x, min+i);
        }
        return points;
    }

    private int pointsBetweenCount(int a, int b){
        return a>b ? a-b + 1 : b-a + 1;
    }

    public void moveByVector(int x, int y){
        start.x += x;
        end.x += x;
        start.y += y;
        end.y += y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Segment segment = (Segment) o;
        return length == segment.length &&
                start.equals(segment.start) &&
                direction == segment.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(length, start, direction);
    }

    public boolean validate() {
        return !end.equals(createEnd());
    }
}
