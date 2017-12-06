import java.util.*;

/**
 * Created by chris on 2/3/17.
 */
public class Board {

    private static boolean DEBUG = false;
    private static final int MAX_BOARD_SIZE = 9;
    private static Scanner reader = new Scanner(System.in);
    private static Random random = new Random();
    private static String state;
    public enum Action { LEFT, RIGHT, UP, DOWN }
    private static Solver solver = new Solver();


    // *************
    // INITIAL STATE
    // *************

    /**
     * inputPuzzle
     *
     * Allows user to input string version of puzzle.
     */
    public void inputPuzzle(){

        System.out.print("Input tile order: ");
        state = reader.next();
    }


    /**
     * randomPuzzle
     *
     * Generates random 8-tile puzzle.
     */
    public void randomPuzzle(){
        Map<Integer, Integer> hmap = new HashMap<>();
        Iterator hmapiterator;
        Set hmapkeys;
        int randompos;
        int key;
        int value;
        //int position;

        Stack<Integer> tileNumbers = new Stack<>();
        if(DEBUG) System.out.println("Generating puzzle.");
        //create tiles 0-8, add to stack
        //ensures no duplicate numbers
        for(int i = 0; i < MAX_BOARD_SIZE; i++){
            tileNumbers.push(i);
            if(DEBUG) System.out.println("Pushing value '" + i + "' to stack.");
        }
        //while there are tiles to place, fill puzzle board
        while(!tileNumbers.isEmpty()) {
            randompos = random.nextInt(MAX_BOARD_SIZE);
            if (!hmap.containsKey(randompos)) {
                int tilevalue = tileNumbers.pop();
                hmap.put(randompos, tilevalue);
                if(DEBUG) System.out.println("Assigning value '" + tilevalue + "' to position " + randompos + "");
            }
        }
        hmapkeys = hmap.keySet();
        hmapiterator = hmapkeys.iterator();
        StringBuilder idbuilder = new StringBuilder();
        while (hmapiterator.hasNext()) {
            key = (Integer)hmapiterator.next();
            value = hmap.get(key);
            idbuilder.append(value);
        }
        state = idbuilder.toString();
        if(DEBUG) System.out.println("Finished generating.");
    }


    /**
     * getState
     *
     * @return state - string format
     */
    public String getState(){ return state; }


    // ****************
    // TRANSITION MODEL
    // ****************

    /**
     * getMoves
     *
     * Return all possible moves of the current state.
     *
     * @param current
     * @return
     */
    public List<Board.Action> getMoves(Node current){

        List<Board.Action> actions = new ArrayList<>();

        if(DEBUG) System.out.print("Current state: " + current.getPosition() + "\n" +
                "Moves: ");

        //add available horizontal moves based on location
        switch(current.getPosition()){
            case 0:
            case 3:
            case 6:
                actions.add(Board.Action.RIGHT);
                if(DEBUG) System.out.print("RIGHT ");
                break;
            case 1:
            case 4:
            case 7:
                actions.add(Board.Action.RIGHT);
                if(DEBUG) System.out.print("RIGHT ");
                actions.add(Board.Action.LEFT);
                if(DEBUG) System.out.print("LEFT ");
                break;
            case 2:
            case 5:
            case 8:
                actions.add(Board.Action.LEFT);
                if(DEBUG) System.out.print("LEFT ");
                break;
        }

        //add available vertical moves based on location
        switch(current.getPosition()){
            case 0:
            case 1:
            case 2:
                actions.add(Board.Action.DOWN);
                if(DEBUG) System.out.print("DOWN ");
                break;
            case 3:
            case 4:
            case 5:
                actions.add(Board.Action.DOWN);
                if(DEBUG) System.out.print("DOWN ");
                actions.add(Board.Action.UP);
                if(DEBUG) System.out.print("UP ");
                break;
            case 6:
            case 7:
            case 8:
                actions.add(Board.Action.UP);
                if(DEBUG) System.out.print("UP ");
                break;
        }
        if(DEBUG) System.out.println();
        return actions;
    }


    /**
     * getDest
     *
     * Returns position destination of blank tile.
     *
     * @param position
     * @param action
     * @return
     */
    public int getDest(int position, Action action){
        int dest = 0;

        switch(action){
            case LEFT:
                dest = position-1;
                break;
            case RIGHT:
                dest = position+1;
                break;
            case UP:
                dest = position-3;
                break;
            case DOWN:
                dest = position+3;
                break;
        }
        return dest;
    }


    /**
     * findPosition
     *
     * Returns position of blank tile.
     */
    public int findPosition(String state){
        return state.indexOf('0');
    }


    /**
     * swapPosition
     *
     * Trades blank tile position with destination position, gives new state (string).
     *
     * @param dest
     * @param state
     * @return New state - string
     */
    public String swapPosition(int dest, String state){
        char temp;
        String pstate;

        temp = state.charAt(dest);

        pstate = state;
        pstate = pstate.replace(temp, '_');
        pstate = pstate.replace('0', temp);
        pstate = pstate.replace('_', '0');

        return pstate;
    }


    // *********
    // GOAL TEST
    // *********

    /**
     * isGoal
     *
     * Determines if current state is the goal state.
     *
     * @return Whether or not current state is goal state.
     */
    public boolean isGoal(String state){
        return solver.countMisplaced(state) == 0;
    }


    // *********
    // PATH COST
    // *********

    /**
     * getStepCost
     *
     * Gives cost of action, given a position and action from that position.
     *
     * @param state
     * @param action
     * @return
     */
    public int getStepCost(String state, Action action){
        String newstate = "";

        int dest = getDest(findPosition(state), action);
        newstate = swapPosition(dest, state);

        if(solver.getHeuristic() == 1) {
            return solver.countMisplaced(newstate);
        }else{
            return solver.sumGoalDistance(newstate);
        }
    }
}