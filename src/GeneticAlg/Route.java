package GeneticAlg;

import javafx.scene.Group;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Route {
    static Random rng = new Random();
//    private int x, y, x2, y2;
    private Point start, end;
    private BoardConfig info;
    private static final int MAX_NUMBER_OF_MOVES = 500;
    public ArrayList<Segment> segments = new ArrayList<>();
    public static Route createRandomRoute(int x, int y, int x2, int y2, BoardConfig info){

        Route route = new Route();
        route.info = info;
//        route.x = x;
//        route.y = y;
        route.start = new Point(x,y);
//        route.x2 = x2;
//        route.y2 = y2;
        route.end = new Point(x2, y2);
        ArrayList<Segment> segments = new ArrayList<>();

        int moves = rng.nextInt(MAX_NUMBER_OF_MOVES - 1) + 1;

        Segment.Direction direction;
        do{
            direction = Segment.Direction.getDirection(rng.nextInt(4));
        }
        while (direction.getMaxDistance(x, y, info.getxSize(), info.getySize()) <= 0);

        int distance = rng.nextInt(direction.getMaxDistance(x, y, info.getxSize(), info.getySize() - 1)) + 1;
        Segment first = new Segment(x, y, distance, direction);
        if(first.contains(route.end))
        {
            first.shortenToPoint(route.end);
        }
        segments.add(first);
        for(int i = 1; i < moves; i++){
            Segment last = segments.get(segments.size() - 1);
            direction = Segment.Direction.getDirection(rng.nextInt(2) + (last.getDirection().isVertical()?2:0));
            int max = direction.getMaxDistance(last.getX2(), last.getY2(), info.getxSize(), info.getySize());
            if(max == 0) {
                direction = direction.reverse();
                max = direction.getMaxDistance(last.getX2(), last.getY2(), info.getxSize(), info.getySize());
            }
            if(max == 0){
                segments.remove(segments.size() - 1);
            }
            else {

                distance = max>1?rng.nextInt(max - 1) + 1:1;
                Segment temp = new Segment(last.getX2(), last.getY2(), distance, direction);
                if(Route.canAdd(segments, temp)){
                    if(temp.contains(route.end)){
                        temp.shortenToPoint(route.end);
                        segments.add(temp);
                        break;
                    }
                    segments.add(temp);
                }
            }
        }

        while(segments.size() == 0 || !segments.get(segments.size() - 1).getEnd().equals(route.end)){
            if(!solve(segments, route.end)) segments.remove(segments.size() - 1);
        }
        if(segments.size() == 0){
            Segment[] xd = connectPointsDown(x, y, x2, y2);
            segments.addAll(Arrays.asList(xd));
        }

        route.segments = segments;
        route.fixLast();
        return route;
    }
    private static boolean canAdd(ArrayList<Segment> segments, Segment newSegment){
        for(int i = 0; i <segments.size() - 1; i++){
            if (segments.get(i).collides(newSegment)) return false;
        }
        return segments.size() <= 0 || segments.get(segments.size() - 1).getDirection() != newSegment.getDirection();
    }

    private static boolean canAdd(ArrayList<Segment> segments, Segment[] newSegments){
        ArrayList<Segment> copy = new ArrayList<Segment>(segments);

        for (Segment newSegment : newSegments) {
            if (!canAdd(copy, newSegment)) {
                return false;
            } else copy.add(newSegment);
        }
        return true;
    }

    private static boolean solve(ArrayList<Segment> segments, Point end){
        Segment last = segments.get(segments.size() - 1);
        Point lastE = last.getEnd();
        Segment[] yeet = connectPointsUp(lastE.x, lastE.y, end.x, end.y);
        if(canAdd(segments, yeet)){
            segments.addAll(Arrays.asList(yeet));
            return true;
        }
        else {
            yeet = connectPointsDown(lastE.x, lastE.y, end.x, end.y);
            if(canAdd(segments, yeet)){
                segments.addAll(Arrays.asList(yeet));
                return true;
            }
        }
        return false;
    }
    public static Segment[] connectPointsUp(int x, int y, int x2, int y2){
        if(x < x2){
            if (y < y2){
                Segment s1 = new Segment(x, y, x2-x, Segment.Direction.RIGHT);
                Segment s2 = new Segment(x2, y, y2-y, Segment.Direction.DOWN);
                return new Segment[]{s1, s2};
            }
            else if(y == y2){
                Segment s = new Segment(x, y, x2-x, Segment.Direction.RIGHT);
                return new Segment[]{s};
            }
            else {
                Segment s1 = new Segment(x, y, y-y2, Segment.Direction.UP);
                Segment s2 = new Segment(x, y2, x2-x, Segment.Direction.RIGHT);
                return new Segment[]{s1, s2};
            }
        }
        else if(x == x2){
            if (y < y2){
                Segment s = new Segment(x, y, y2-y, Segment.Direction.DOWN);
                return new Segment[]{s};
            }
            else if(y == y2){
                return new Segment[]{};
            }
            else {
                Segment s = new Segment(x, y, y-y2, Segment.Direction.UP);
                return new Segment[]{s};
            }
        }
        else {
            if (y < y2){
                Segment s1 = new Segment(x, y, x-x2, Segment.Direction.LEFT);
                Segment s2 = new Segment(x2, y, y2-y, Segment.Direction.DOWN);
                return new Segment[]{s1, s2};
            }
            else if(y == y2){
                Segment s = new Segment(x, y, x-x2, Segment.Direction.LEFT);
                return new Segment[]{s};
            }
            else {
                Segment s1 = new Segment(x, y, y-y2, Segment.Direction.UP);
                Segment s2 = new Segment(x, y2, x-x2, Segment.Direction.LEFT);
                return new Segment[]{s1, s2};
            }
        }
    }
    public static Segment[] connectPointsDown(int x, int y, int x2, int y2 ) {
        if (x < x2) {
            if (y < y2) {
                Segment s1 = new Segment(x, y, y2 - y, Segment.Direction.DOWN);
                Segment s2 = new Segment(x, y2, x2 - x, Segment.Direction.RIGHT);
                return new Segment[]{s1, s2};
            } else if (y == y2) {
                Segment s = new Segment(x, y, x2 - x, Segment.Direction.RIGHT);
                return new Segment[]{s};
            } else {
                Segment s1 = new Segment(x, y, x2 - x, Segment.Direction.RIGHT);
                Segment s2 = new Segment(x2, y, y - y2, Segment.Direction.UP);
                return new Segment[]{s1, s2};
            }
        } else if (x == x2) {
            if (y < y2) {
                Segment s = new Segment(x, y, y2 - y, Segment.Direction.DOWN);
                return new Segment[]{s};
            } else if (y == y2) {
                return new Segment[]{};
            } else {
                Segment s = new Segment(x, y, y - y2, Segment.Direction.UP);
                return new Segment[]{s};
            }
        } else {
            if (y < y2) {
                Segment s1 = new Segment(x, y, y2 - y, Segment.Direction.DOWN);
                Segment s2 = new Segment(x, y2, x - x2, Segment.Direction.LEFT);
                return new Segment[]{s1, s2};
            } else if (y == y2) {
                Segment s = new Segment(x, y, x - x2, Segment.Direction.LEFT);
                return new Segment[]{s};
            } else {
                Segment s1 = new Segment(x, y, x - x2, Segment.Direction.LEFT);
                Segment s2 = new Segment(x2, y, y - y2, Segment.Direction.UP);
                return new Segment[]{s1, s2};
            }
        }
    }
    private void fixLast(){
        int size = segments.size();
        if (size > 1 && segments.get(size - 2).getDirection() == segments.get(size - 1).getDirection()) {
            segments.get(size - 2).expandToPoint(segments.get(size - 1).getEnd());
            segments.remove(size - 1);
        }
    }

    public Line[] drawLines(){
        Line[] lines = new Line[segments.size()];
        for(int i = 0; i < segments.size(); i++){
            lines[i] = segments.get(i).draw();
        }
        return lines;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "Route{" +
                "start=" + start +
                ", end=" + end +
                ", segments=" + segments +
                '}';
    }

    public int length(){
        int temp = 0;
        for(int i = 0; i < segments.size(); i++){
            temp+=segments.get(i).getLength();
        }
        return temp;
    }
    public int numberOfSegments(){
        return segments.size();
    }
    public int lengthNotOnBoard(){

        int temp = 0;
        for(int i = 0; i <segments.size(); i++){
            temp += segments.get(i).notOnBoard(info.getxSize(), info.getySize());
        }
        return temp;
    }

    public int collisions(Route r){
        var first = segments;
        var sec = r.segments;
        int count = 0;
        for(int i = 0; i < first.size(); i++){
            for(int j = 0; j <sec.size(); j++){
                if(first.get(i).collides(sec.get(j))){
                    count++;
                }
            }
        }
        return count;
    }
}
