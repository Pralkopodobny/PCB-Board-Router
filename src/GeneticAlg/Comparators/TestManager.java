package GeneticAlg.Comparators;

import junit.framework.TestResult;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TestManager {
    private int[][] results;
    private BufferedWriter writer;
    private final static String FILENAME="Wyniki.csv";
    private int iter;

    public TestManager(int testNumber, int iter){
        results = new int[testNumber][iter];
        this.iter = iter;
        try {
            writer = new BufferedWriter(new FileWriter(FILENAME));
            for (int i = 0; i < testNumber - 1; i++){
                writer.write("test" + i+1 + ",");
            }
            writer.write("test" + testNumber);
            writer.newLine();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(404);
        }
    }

    public void setResult(int testNumber, int iter, int miss){
        results[testNumber][iter] = miss;
    }
    public void printResults(){
        try{
            for(int i = 0; i < iter; i++){
                for(int j = 0; j < results.length - 1; j++){
                    writer.write(results[j][i] + ",");
                }
                writer.write(Integer.toString(results[results.length - 1][i]));
                writer.newLine();
            }
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(404);
        }

    }

}
