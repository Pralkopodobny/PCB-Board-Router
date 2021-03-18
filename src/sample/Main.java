package sample;

import GeneticAlg.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.awt.geom.Line2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        BoardConfig config;
        try{
            config = new BoardConfig("Assets/Zad3.txt");
            Population p = new Population(config);
            //Board b = p.runRoulette(10000);
            Board b = p.runTournament(8000);
            System.out.println(b.toString());
            Group group = b.drawLines();

            Scene scene = new Scene(group, 1000, 1000);
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
