package GeneticAlg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Route {
    static Random rng = new Random();
    private int x, y, x2, y2;
    private static final int MAX_NUMBER_OF_MOVES = 500;
    public ArrayList<Segment> segments = new ArrayList<>();
    public static Route createRandomRoute(int x, int y, int x2, int y2, BoardConfig info){
        Route route = new Route();
        route.x = x;
        route.y = y;
        route.x2 = x2;
        route.y2 = y2;
        ArrayList<Segment> segments = new ArrayList<>();
        int moves = rng.nextInt(MAX_NUMBER_OF_MOVES - 1) + 1;
        Segment.Direction direction;
        do{
            direction = Segment.Direction.getDirection(rng.nextInt(4));
        }
        while (direction.getMaxDistance(x, y, info.getxSize(), info.getySize()) <= 0);
        int distance = rng.nextInt(direction.getMaxDistance(x, y, info.getxSize(), info.getySize() - 1)) + 1;
        segments.add(new Segment(x, y, distance, direction));
        System.out.println(moves);
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
                System.out.println(max);

                distance = max>1?rng.nextInt(max - 1) + 1:1;
                Segment temp = new Segment(last.getX2(), last.getY2(), distance, direction);
                if(Route.canAdd(segments, temp))
                segments.add(temp);
            }

        }
        route.segments = segments;
        return route;
    }
    private static boolean canAdd(ArrayList<Segment> segments, Segment newSegment){
        for(int i = 0; i <segments.size() - 1; i++){
            if (segments.get(i).collides(newSegment)) return false;
        }
        return true;
    }
//    public static Route createRandomRoute(int x, int y, int x2, int y2, BoardConfig info){
//        Route route = new Route();
//        route.x = x;
//        route.y = y;
//        route.x2 = x2;
//        route.y2 = y2;
//        int[] end = new int[] {x2, y2};
//        ArrayList<int[]> bonusPoints = new ArrayList<>();
//        int count = rng.nextInt(MAX_BONUS_POINTS) + 1;
//        for (int i = 0; i < count; i++) {
//            int[] point = generatePoint(x, y, x2, y2, info.getxSize(), info.getySize());
//            bonusPoints.add(point);
//        }
//        Segment[] temp = rng.nextBoolean()?connectPointsDown(x, y, bonusPoints.get(0)[0], bonusPoints.get(0)[0]):connectPointsUp(x, y, bonusPoints.get(0)[0], bonusPoints.get(0)[0]);
//        ArrayList<Segment> segments = new ArrayList<>(Arrays.asList(temp));
//        for(int i = 1; i < bonusPoints.size(); i++){
//            tryAddPoint(segments, bonusPoints.get(i));
//        }
//        Segment last = segments.get(segments.size() - 1);
//        while (last.getX() != x2 && last.getY() != y2){
//            if(!tryAddPoint(segments, end)){
//                segments.remove(segments.size()-1);
//            }
//        }
//        route.segments = segments;
//
//
//        return route;
//    }
//    public static boolean tryAddPoint(ArrayList<Segment> segments, int[] point){
//        Segment last = segments.get(segments.size()-1);
//        Segment[] newSegments;
//        boolean addedSomething = false;
//        boolean down = rng.nextBoolean();
//        if(down){
//            newSegments = connectPointsDown(last.getX(), last.getY(), point[0], point[1]);
//        }
//        else {
//            newSegments = connectPointsUp(last.getX(), last.getY(), point[0], point[1]);
//        }
//        for(int i=0; i < newSegments.length; i++){
//            if(canJoin(newSegments[i], segments)){
//                segments.add(newSegments[i]);
//                addedSomething = true;
//            }
//            else break;
//        }
//
//        if(!addedSomething){
//            if(!down){
//                newSegments = connectPointsDown(last.getX(), last.getY(), point[0], point[1]);
//            }
//            else {
//                newSegments = connectPointsUp(last.getX(), last.getY(), point[0], point[1]);
//            }
//            for(int i=0; i < newSegments.length; i++){
//                if(canJoin(newSegments[i], segments)){
//                    segments.add(newSegments[i]);
//                    addedSomething = true;
//                }
//                else break;
//            }
//        }
//        return addedSomething;
//    }
//    public static boolean canJoin(Segment newSegment, ArrayList<Segment> segments){
//        for (int i = 0; i < segments.size(); i++){
//            if(Segment.selfCollides(segments.get(i), newSegment)) return false;
//        }
//        return true;
//    }
//    public static int[] generatePoint(int x, int y, int x2, int y2, int maxX, int maxY){
//        int[] point = new int[2];
//        do{
//            point[0] = rng.nextInt(maxX);
//            point[1] = rng.nextInt(maxY);
//        }
//        while ((point[0] == x && point[1] == y) || (point[0] == x2 && point[1] == y2));
//        return point;
//    }
//
//    public static Segment[] connectPointsUp(int x, int y, int x2, int y2){
//        if(x < x2){
//            if (y < y2){
//                Segment s1 = new Segment(x, y, x2-x, Segment.Direction.RIGHT);
//                Segment s2 = new Segment(x2, y, y2-y, Segment.Direction.DOWN);
//                return new Segment[]{s1, s2};
//            }
//            else if(y == y2){
//                Segment s = new Segment(x, y, x2-x, Segment.Direction.RIGHT);
//                return new Segment[]{s};
//            }
//            else {
//                Segment s1 = new Segment(x, y, y-y2, Segment.Direction.UP);
//                Segment s2 = new Segment(x, y2, x2-x, Segment.Direction.RIGHT);
//                return new Segment[]{s1, s2};
//            }
//        }
//        else if(x == x2){
//            if (y < y2){
//                Segment s = new Segment(x, y, y2-y, Segment.Direction.DOWN);
//                return new Segment[]{s};
//            }
//            else if(y == y2){
//                return new Segment[]{};
//            }
//            else {
//                Segment s = new Segment(x, y, y-y2, Segment.Direction.UP);
//                return new Segment[]{s};
//            }
//        }
//        else {
//            if (y < y2){
//                Segment s1 = new Segment(x, y, x-x2, Segment.Direction.LEFT);
//                Segment s2 = new Segment(x2, y, y2-y, Segment.Direction.DOWN);
//                return new Segment[]{s1, s2};
//            }
//            else if(y == y2){
//                Segment s = new Segment(x, y, x-x2, Segment.Direction.LEFT);
//                return new Segment[]{s};
//            }
//            else {
//                Segment s1 = new Segment(x, y, y-y2, Segment.Direction.UP);
//                Segment s2 = new Segment(x, y2, x-x2, Segment.Direction.LEFT);
//                return new Segment[]{s1, s2};
//            }
//        }
//    }
//    public static Segment[] connectPointsDown(int x, int y, int x2, int y2 ){
//        if(x < x2){
//            if (y < y2){
//                Segment s1 = new Segment(x, y, y2-y, Segment.Direction.DOWN);
//                Segment s2 = new Segment(x, y2, x2-x, Segment.Direction.RIGHT);
//                return new Segment[]{s1, s2};
//            }
//            else if(y == y2){
//                Segment s = new Segment(x, y, x2-x, Segment.Direction.RIGHT);
//                return new Segment[]{s};
//            }
//            else {
//                Segment s1 = new Segment(x, y, x2-x, Segment.Direction.RIGHT);
//                Segment s2 = new Segment(x2, y, y-y2, Segment.Direction.UP);
//                return new Segment[]{s1, s2};
//            }
//        }
//        else if(x == x2){
//            if (y < y2){
//                Segment s = new Segment(x, y, y2-y, Segment.Direction.DOWN);
//                return new Segment[]{s};
//            }
//            else if(y == y2){
//                return new Segment[]{};
//            }
//            else {
//                Segment s = new Segment(x, y, y-y2, Segment.Direction.UP);
//                return new Segment[]{s};
//            }
//        }
//        else {
//            if (y < y2){
//                Segment s1 = new Segment(x, y, y2-y, Segment.Direction.DOWN);
//                Segment s2 = new Segment(x, y2, x-x2, Segment.Direction.LEFT);
//                return new Segment[]{s1, s2};
//            }
//            else if(y == y2){
//                Segment s = new Segment(x, y, x-x2, Segment.Direction.LEFT);
//                return new Segment[]{s};
//            }
//            else {
//                Segment s1 = new Segment(x, y, x-x2, Segment.Direction.LEFT);
//                Segment s2 = new Segment(x2, y, y-y2, Segment.Direction.UP);
//                return new Segment[]{s1, s2};
//            }
//        }
//    }

    @Override
    public String toString() {
        return "Route{" +
                "x=" + x +
                ", y=" + y +
                ", x2=" + x2 +
                ", y2=" + y2 +
                ", segments=" + segments +
                '}';
    }
}
