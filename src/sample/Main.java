package sample;

import GeneticAlg.BoardConfig;
import GeneticAlg.Route;
import GeneticAlg.Segment;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        BoardConfig config;
        try{
            config = new BoardConfig("Assets/Zad0.txt");
            System.out.println(config);
            System.out.println(Segment.Direction.LEFT.getMaxDistance(1,2, 10, 4));
            Route route = Route.createRandomRoute(3,3, 4,4, config);
            System.out.println(route);
            Group group = new Group();
            for(int i = 0; i < route.segments.size(); i++){
                Line line = new Line(route.segments.get(i).getX()*30, route.segments.get(i).getY()*30, route.segments.get(i).getX2()*30, route.segments.get(i).getY2()*30);
                group.getChildren().add(line);
            }

            Scene scene = new Scene(group, 600, 300);
            primaryStage.setScene(scene);

        }
        catch (FileNotFoundException e){
            System.out.println("No config file");
            System.exit(404);
        }

        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
