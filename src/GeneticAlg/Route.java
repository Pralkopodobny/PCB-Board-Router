package GeneticAlg;

import javafx.scene.Group;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Route {
    private static final int MAX_NUMBER_OF_MOVES = 500;
    static Random rng = new Random();
    private Point start, end;
    private BoardConfig info;
    public ArrayList<Segment> segments = new ArrayList<>();

    private Route(){};

    public Route(Route other){
        this.info = other.info;
        this.start = new Point(other.start);
        this.end = new Point(other.end);
        this.segments = new ArrayList<>();
        for (var segment: other.segments) {
            this.segments.add(new Segment(segment));
        }
    }

    public static Route createRandomRoute(int x, int y, int x2, int y2, BoardConfig info){

        Route route = new Route();
        route.info = info;
        route.start = new Point(x,y);
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
            if(!solve(segments, route.end, route.start)) segments.remove(segments.size() - 1);
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

    private static boolean solve(ArrayList<Segment> segments, Point end, Point start){
        Point lastE;
        if(segments.size() == 0){
            lastE = start;
        }
        else{
            Segment last = segments.get(segments.size() - 1);
            lastE = last.getEnd();
        }
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

    int selfCollisions(){
        int total = 0;
        for(int i = 0; i < segments.size() - 2; i++){
            total += segments.get(i).collisionPointsCount(segments.get(i+2));
        }
        return total;
    }

    public void simpleMutation(int index, int force){
        Segment mutated = segments.get(index), newPrev, newNext;
        if(mutated.getDirection().isVertical()){
            mutated.moveByVector(force, 0);
        }
        else{
            mutated.moveByVector(0, force);
        }
        Segment prev = index==0?null:segments.get(index - 1);
        newPrev = prev==null||prev.getStart().equals(mutated.getStart())?null:
                (prev.getDirection().isVertical()?Segment.getVerticalSegment(prev.getStart(), mutated.getStart()):
                        Segment.getHorizontalSegment(prev.getStart(), mutated.getStart()));
        if(prev == null){
            newPrev = mutated.getDirection().isVertical()?
                    Segment.getHorizontalSegment(start, mutated.getStart()):
                    Segment.getVerticalSegment(start, mutated.getStart());
        }
        else{
            newPrev = prev.getDirection().isVertical()?
                    Segment.getVerticalSegment(prev.getStart(), mutated.getStart()):
                    Segment.getHorizontalSegment(prev.getStart(), mutated.getStart());
        }
        Segment next = index==segments.size() - 1?null:segments.get(index + 1);
        if(next == null){
            newNext = mutated.getDirection().isVertical()?
                    Segment.getHorizontalSegment(mutated.getEnd(), end):
                    Segment.getVerticalSegment(mutated.getEnd(), end);
        }
        else{
            newNext = next.getDirection().isVertical()?
                    Segment.getVerticalSegment(mutated.getEnd(), next.getEnd()):
                    Segment.getHorizontalSegment(mutated.getEnd(), next.getEnd());
        }
        int offset = 0;
        if(prev != null){
            segments.remove(index - 1);
            if(newPrev != null)
                segments.add(index - 1, newPrev);
            else
                offset -=1;
        }
        if(next != null){
            segments.remove(index + 1 + offset);
            if(newNext != null)
                segments.add(index + 1 + offset, newNext);
        }
        if(prev == null && newPrev != null){
            segments.add(0, newPrev);
        }
        if(next == null && newNext != null){
            segments.add(newNext);
        }
        fix();
    }
    public void advancedMutation(int index, int force, int cut){
        Segment mutated = segments.get(index), connector, newNext, next;
        next = index<segments.size() - 1?segments.get(index + 1):null;
        Segment[] newMutated = mutated.getSplit(cut);
        if(mutated.getDirection().isVertical()){
            newMutated[1].moveByVector(force, 0);
            connector = Segment.getHorizontalSegment(newMutated[0].getEnd(), newMutated[1].getStart());
        }
        else {
            newMutated[1].moveByVector(0, force);
            connector = Segment.getVerticalSegment(newMutated[0].getEnd(), newMutated[1].getStart());
        }
        if(next == null){
            newNext = mutated.getDirection().isVertical()?
                    Segment.getHorizontalSegment(newMutated[1].getEnd(), end):
                    Segment.getVerticalSegment(newMutated[1].getEnd(), end);
        }
        else{
            newNext = next.getDirection().isVertical()?
                    Segment.getVerticalSegment(newMutated[1].getEnd(), next.getEnd()):
                    Segment.getHorizontalSegment(newMutated[1].getEnd(), next.getEnd());
        }
        segments.remove(index);
        segments.add(index,newMutated[0]);
        segments.add(index + 1, connector);
        segments.add(index + 2, newMutated[1]);
        if(next != null){
            segments.remove(index + 3);
        }
        if(newNext != null){
            segments.add(index + 3, newNext);
        }
        fix();

    }

    public void fix(){
        int i = fixFirstFrom(0);
        while (i != -1){
            i = fixFirstFrom(i);
        }
    }
    //Returns index of fixed segment. When nothing fixed returns -1
    private int fixFirstFrom(int index){
        for(int i = index; i + 1 < segments.size(); i++){
            if(segments.get(i).getDirection().isVertical() == segments.get(i + 1).getDirection().isVertical()){
                    Segment fixedSegment = segments.get(i).getDirection().isVertical()?
                            Segment.getVerticalSegment(segments.get(i).getStart(), segments.get(i+1).getEnd()):
                            Segment.getHorizontalSegment(segments.get(i).getStart(), segments.get(i+1).getEnd());

                    if(fixedSegment != null){
                        segments.add(i, fixedSegment);
                        segments.remove(i+1);
                        segments.remove(i+1);
                        return i;
                    }
                    else{
                        segments.remove(i);
                        segments.remove(i);
                        return i;
                    }
                }
            }

        return -1;
    }

    public static Route createTestRoute(ArrayList<Segment> segments){
        Route r = new Route();
        if(segments == null || segments.size() < 1) throw new IllegalArgumentException();
        r.segments = segments;
        r.start = new Point(segments.get(0).getStart());
        r.end = new Point(segments.get(segments.size() - 1).getEnd());
        return r;
    }

    public void mutate(){
        int index = rng.nextInt(segments.size());
        int roll = rng.nextInt(100);
        int force = (rng.nextInt(BoardConfig.MAX_MUTATION_FORCE) + 1) * (rng.nextBoolean()?1:-1);
        int length = segments.get(index).getLength();
        if(length>1 && roll >= BoardConfig.SIMPLE_MUTATION_CHANCE){
            int cut = length==2?1:rng.nextInt(length - 2) + 1;
            advancedMutation(index, force, cut);
        }
        else{
            simpleMutation(index, force);
        }
    }

    public int correct(){
        for(int i = 1; i < segments.size() - 1; i++){
            if(!segments.get(i).getEnd().equals(segments.get(i+1).getStart())) return i;
        }
        return -1;
    }
}

