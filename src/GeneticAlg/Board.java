package GeneticAlg;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;



import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private static final Color[] colors = {Color.BLACK, Color.BLUE, Color.RED, Color.GREEN, Color.GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK};
    private BoardConfig info;
    private ArrayList<Route> routes = new ArrayList<>();
    private boolean[][] collided;
    public Board(BoardConfig info){
        this.info = info;
        var intRoutes = info.getRoutes();

        for(int i = 0; i < intRoutes.size(); i++){
            int[] temp = intRoutes.get(i);
            routes.add(Route.createRandomRoute(temp[0], temp[1], temp[2], temp[3], info));
        }
        collided = new boolean[intRoutes.size()][intRoutes.size()];
        for (var collisions: collided) {
            Arrays.fill(collisions, false);
        }
    }

    public Board(Board other){
        this.info = other.info;
        this.routes = new ArrayList<>();
        for(var route : other.routes){
            routes.add(new Route(route));
        }
    }

    @Override
    public String toString() {
        return "Board{" +
                "info=" + info +
                ", routes=" + routes +
                '}';
    }

    public Group drawLines(){
        Group group = new Group();
        ArrayList<Line[]> drawnRoutes = new ArrayList<>();
        for(int i = 0; i < routes.size(); i++){
            Line[] lines = routes.get(i).drawLines();
            for(int j = 0; j < lines.length; j++) {
                lines[j].setStroke(colors[i%colors.length]);
            }
            group.getChildren().addAll(lines);
            Point[] temp1 = new Point[]{routes.get(i).getStart(), routes.get(i).getEnd()};
            for(int j = 0; j < temp1.length; j++){
                Circle c = new Circle(temp1[j].x * BoardConfig.PIXEL_SIZE+ BoardConfig.PIXEL_SIZE, temp1[j].y  * BoardConfig.PIXEL_SIZE + BoardConfig.PIXEL_SIZE, BoardConfig.RADIUS);
                c.setStroke(colors[i%colors.length]);
                group.getChildren().add(c);
            }

        }
        return group;
    }

    public int bad(){
        int sum = 0;
        for(int i = 0; i < routes.size(); i++){
            sum+=routes.get(i).length() * BoardConfig.LENGTH_WEIGHT;
            sum+=routes.get(i).numberOfSegments() * BoardConfig.SEGMENT_WEIGHT;
            sum += routes.get(i).lengthNotOnBoard() * BoardConfig.OUT_OF_BOUNDS_WEIGHT;
            int selfCols = routes.get(i).selfCollisions();
            sum += selfCols * BoardConfig.SELF_COLLISION_WEIGHT;
            for(int j = i + 1; j <routes.size(); j++){
                int col = routes.get(i).collisions(routes.get(j));
                sum+= col * BoardConfig.COLLISION_WEIGHT;
            }
        }
        return sum;
    }

    public int bad(int[][] scale){
        int sum = 0;
        for(int i = 0; i < routes.size(); i++){
            sum+=routes.get(i).length() * BoardConfig.LENGTH_WEIGHT;
            sum+=routes.get(i).numberOfSegments() * BoardConfig.SEGMENT_WEIGHT;
            sum += routes.get(i).lengthNotOnBoard() * BoardConfig.OUT_OF_BOUNDS_WEIGHT;
            int selfCols = routes.get(i).selfCollisions();
            collided[i][i] = selfCols > 0;
            sum += selfCols * scale[i][i];
            for(int j = i + 1; j <routes.size(); j++){
                int col = routes.get(i).collisions(routes.get(j));
                collided[i][j] = col > 0;
                sum+= col * scale[i][j];
            }
        }
        return sum;
    }

    public void cross(int index, Board other){
        Route r = routes.get(index);
        routes.set(index, other.routes.get(index));
        other.routes.set(index, r);
    }

    public void mutate(int index){
        routes.get(index).mutate();
    }
}
