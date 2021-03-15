package GeneticAlg;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;



import java.util.ArrayList;

public class Board {
    private BoardConfig info;
    private ArrayList<Route> routes = new ArrayList<>();
    private static final Color[] colors = {Color.BLACK, Color.BLUE, Color.RED, Color.GREEN, Color.GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK};
    public Board(BoardConfig info){
        this.info = info;
        var intRoutes = info.getRoutes();
        for(int i = 0; i < intRoutes.size(); i++){
            int[] temp = intRoutes.get(i);
            routes.add(Route.createRandomRoute(temp[0], temp[1], temp[2], temp[3], info));
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
            for(int j = 0; j <routes.size(); j++){
                if(i != j) sum+= routes.get(i).collisions(routes.get(j)) * BoardConfig.COLLISION_WEIGHT;
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
            for(int j = 0; j <routes.size(); j++){
                if(i != j) sum+= routes.get(i).collisions(routes.get(j)) * scale[i][j];
            }
        }
        return sum;
    }
}
