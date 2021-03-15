package GeneticAlg;

import java.util.ArrayList;

public class Population {
    private final static int NUMBER_OF_BOARDS = 200;
    private BoardConfig config;
    private ArrayList<Board> boards = new ArrayList<>(NUMBER_OF_BOARDS);

    public Population(BoardConfig config){
        this.config = config;
        for(int i = 0; i < NUMBER_OF_BOARDS; i++){
            boards.add(new Board(config));
        }
    }
}
