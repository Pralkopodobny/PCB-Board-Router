package GeneticAlg;

import GeneticAlg.Comparators.TestManager;
import javafx.util.Pair;
import junit.framework.Test;
import junit.framework.TestResult;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Population {
    private final static int NUMBER_OF_BOARDS = 60;
    private BoardConfig config;
    private ArrayList<Board> boards = new ArrayList<>(NUMBER_OF_BOARDS);
    private int[][] scale;
    private int[] misfits;
    private int[] realMisfits;
    private double[] fitness;
    private double[] sumOfFitness;
    private int bestMisfit, bestIndex;
    private int lastBestMisfit = Integer.MAX_VALUE;
    private Random rng = new Random();
    private TestManager testManager;
    private int testNumb;

    public Population(BoardConfig config, String filename, int testNumber, TestManager testManager){
        this.config = config;
        this.testNumb = testNumber;
        this.testManager = testManager;
        for(int i = 0; i < NUMBER_OF_BOARDS; i++){
            boards.add(new Board(config));
        }
        scale = new int[config.getRoutes().size()][config.getRoutes().size()];
        for(var x : scale){
            Arrays.fill(x, 30);
        }
        misfits = new int[NUMBER_OF_BOARDS];
        realMisfits = new int[NUMBER_OF_BOARDS];
        fitness = new double[NUMBER_OF_BOARDS];
        sumOfFitness = new double[NUMBER_OF_BOARDS];
    }

    public void eval(){
        misfits[0] = boards.get(0).bad(scale);
        bestMisfit = misfits[0];
        bestIndex = 0;
        for(int i = 1; i < NUMBER_OF_BOARDS; i++){
            misfits[i] = boards.get(i).bad(scale);
            if(misfits[i] < bestMisfit) {
                bestMisfit = misfits[i];
                bestIndex = i;
            }
        }

        for(int i = 0; i < misfits.length; i++){
            fitness[i] = (double) bestMisfit / misfits[i];
        }
    }
    public void realEval(){
        for(int i = 0; i < NUMBER_OF_BOARDS; i++){
            realMisfits[i] = boards.get(i).bad();
        }
    }
    public void evalNoScale(){
        misfits[0] = boards.get(0).bad();
        bestMisfit = misfits[0];
        bestIndex = 0;
        for(int i = 1; i < NUMBER_OF_BOARDS; i++){
            misfits[i] = boards.get(i).bad();
            if(misfits[i] < bestMisfit) {
                bestMisfit = misfits[i];
                bestIndex = i;
            }
        }

        for(int i = 0; i < misfits.length; i++){
            fitness[i] = (double) bestMisfit / misfits[i];
        }
    }
    public void evalRoulette(){
        misfits[0] = boards.get(0).bad(scale);
        bestMisfit = misfits[0];
        bestIndex = 0;
        for(int i = 1; i < NUMBER_OF_BOARDS; i++){
            misfits[i] = boards.get(i).bad(scale);
            if(misfits[i] < bestMisfit) {
                bestMisfit = misfits[i];
                bestIndex = i;
            }
        }

        fitness[0] = (double) bestMisfit / misfits[0];
        sumOfFitness[0] = fitness[0];
        for(int i = 1; i < misfits.length; i++){
            fitness[i] = (double) bestMisfit / misfits[i];
            sumOfFitness[i] = sumOfFitness[i - 1] + fitness[i];
        }
    }
    private int roulette(){
        double roll = ThreadLocalRandom.current().nextDouble(sumOfFitness[NUMBER_OF_BOARDS - 1]);
        for(int i = 0; i < NUMBER_OF_BOARDS - 1; i++){
            if(roll >= sumOfFitness[i + 1]) return i;
        }
        return NUMBER_OF_BOARDS - 1;
    }
    private int[] rouletteSelection(){
        int mother = roulette(), father = roulette();
        while(mother == father){
            father = roulette();
        }
        return new int[]{mother, father};
    }
    private int tournament(){
        Set<Integer> indexes = new HashSet<Integer>();
        while (indexes.size() < BoardConfig.TOURNAMENT_SIZE){
            indexes.add(rng.nextInt(NUMBER_OF_BOARDS));
        }
        LinkedList<Pair<Double, Integer>> pairs = new LinkedList<>();
        for (var i: indexes) {
            pairs.add(new Pair<>(fitness[i], i));
        }
        return Collections.max(pairs, Comparator.comparing(Pair::getKey)).getValue();

    }

    private int[] tournamentSelection(){
        int mother = tournament(), father = tournament();
        while(mother == father){
            father = tournament();
        }
        return new int[]{mother, father};
    }

    public Board runRoulette(int iterations){
        for(int i = 0; i < iterations; i++){
            evalRoulette();
            realEval();
            uploadBest(i);
            int actualBestMisfit = boards.get(bestIndex).bad();
            if(lastBestMisfit > actualBestMisfit){
                increaseScale();
            }
            else{
                lastBestMisfit = actualBestMisfit;
            }
            ArrayList<Board> newBoards = new ArrayList<>(NUMBER_OF_BOARDS);
            newBoards.add(boards.get(bestIndex));
            while(newBoards.size() < NUMBER_OF_BOARDS){
                int[] parentsIndexes = rouletteSelection();
                Board a = new Board(boards.get(parentsIndexes[0]));
                Board b = new Board(boards.get(parentsIndexes[1]));
                int roll = rng.nextInt(100);
                if(roll < BoardConfig.CROSSOVER_PROBABILITY){
                    roll = rng.nextInt(config.getRoutes().size());
                    a.cross(roll, b);
                }
                for(int k = 0; k < config.getRoutes().size(); k++){
                    roll = rng.nextInt(100);
                    if(roll < BoardConfig.MUTATION_PROBABILITY){
                        a.mutate(k);
                    }
                    roll = rng.nextInt(100);
                    if(roll < BoardConfig.MUTATION_PROBABILITY){
                        b.mutate(k);
                    }
                }
                newBoards.add(a);
                if(newBoards.size() < NUMBER_OF_BOARDS)
                    newBoards.add(b);
            }
            boards = newBoards;
        }
        return boards.get(bestIndex);
    }
    public Board losulosu(int iterations){
        for(int j = 0; j <iterations; j++) {
            evalNoScale();
            realEval();
            uploadBest(j);
            boards.set(0, boards.get(bestIndex));
            for (int i = 1; i < boards.size(); i++) {
                boards.set(i, new Board(config));
            }
        }
        return boards.get(bestIndex);
    }

    public Board runTournament(int iterations){
        for(int i = 0; i < iterations; i++){
            eval();
            realEval();
            uploadBest(i);
            int actualBestMisfit = boards.get(bestIndex).bad();
            if(lastBestMisfit > actualBestMisfit){
                increaseScale();
            }
            else{
                lastBestMisfit = actualBestMisfit;
            }
            ArrayList<Board> newBoards = new ArrayList<>(NUMBER_OF_BOARDS);
            newBoards.add(boards.get(bestIndex));
            while(newBoards.size() < NUMBER_OF_BOARDS){
                int[] parentsIndexes = tournamentSelection();
                Board a = new Board(boards.get(parentsIndexes[0]));
                Board b = new Board(boards.get(parentsIndexes[1]));
                int roll = rng.nextInt(100);
                if(roll < BoardConfig.CROSSOVER_PROBABILITY){
                    roll = rng.nextInt(config.getRoutes().size());
                    a.cross(roll, b);
                }
                for(int k = 0; k < config.getRoutes().size(); k++){
                    roll = rng.nextInt(100);
                    if(roll < BoardConfig.MUTATION_PROBABILITY){
                        a.mutate(k);
                    }
                    roll = rng.nextInt(100);
                    if(roll < BoardConfig.MUTATION_PROBABILITY){
                        b.mutate(k);
                    }
                }
                newBoards.add(a);
                if(newBoards.size() < NUMBER_OF_BOARDS)
                    newBoards.add(b);
            }
            boards = newBoards;

        }
        return boards.get(bestIndex);
    }


    private void increaseScale(){
        var b = boards.get(bestIndex);
        for(int i = 0; i < scale.length; i++){
            for(int j = 0; j <scale[i].length; j++){
                if(b.collided[i][j]) scale[i][j]++;
            }
        }
    }

    private void uploadBest(int iter){
        testManager.setResult(testNumb, iter, realMisfits[bestIndex]);
    }


}
