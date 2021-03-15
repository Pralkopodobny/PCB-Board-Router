package GeneticAlg;

import GeneticAlg.Comparators.HorizontalPointComparator;
import GeneticAlg.Comparators.VerticalPointComparator;
import javafx.scene.shape.Line;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.Arrays;

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
        return start + "->" + end;
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

}
