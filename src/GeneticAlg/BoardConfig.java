package GeneticAlg;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class BoardConfig {
    private int xSize, ySize;
    private ArrayList<int[]> routes = new ArrayList<>();

    public BoardConfig(String filename) throws FileNotFoundException {
        File myFile = new File(filename);
        Scanner myReader = new Scanner(myFile);
        String line = myReader.nextLine();
        String[] temp = line.split(";");
        xSize = Integer.parseInt(temp[0]);
        ySize = Integer.parseInt(temp[1]);
        while(myReader.hasNextLine()){
            line = myReader.nextLine();
            temp = line.split(";");
            int[] route = new int[4];
            for(int i = 0; i < 4; i++){
                route[i] = Integer.parseInt(temp[i]);
            }
            routes.add(route);
        }
        myReader.close();
    }

    public int getxSize() {
        return xSize;
    }

    public int getySize() {
        return ySize;
    }

    public ArrayList<int[]> getRoutes() {
        return routes;
    }

    @Override
    public String toString() {
        String temp = "BoardConfig{" +
                "xSize=" + xSize +
                ", ySize=" + ySize +
                ", routes=[";
        for (int i =0 ; i < routes.size(); i++){
            temp += Arrays.toString(routes.get(i));
        }
        return temp + "]";
    }
}