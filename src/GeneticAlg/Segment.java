package GeneticAlg;

import java.awt.geom.Line2D;

public class Segment {

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
    private int x, y, x2, y2;
    private Direction direction;

    public Segment(int x, int y, int length, Direction direction){
        this.x = x;
        this.y = y;
        this.length = length;
        this.direction = direction;
        updateEnds();
    }
    public void updateEnds(){
        switch(direction){
            case DOWN:{
                x2 = x;
                y2 = y+length;
                break;
            }
            case UP:{
                x2 = x;
                y2 = y-length;
                break;
            }
            case RIGHT:{
                x2 = x + length;
                y2 = y;
                break;
            }
            case LEFT:{
                x2 = x-length;
                y2 = y;
                break;
            }
        }
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean collides(Segment other){
        return Line2D.linesIntersect(x, y, x2, y2, other.x, other.y, other.x2, other.y2);
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    @Override
    public String toString() {
        return "(" +x+","+y+")->("+x2+","+y2+")";
    }
}
