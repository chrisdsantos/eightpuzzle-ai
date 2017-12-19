import java.util.*;

/***
 * Board class.
 *
 * @author Chris Santos
 * @version 2.0
 * @since 2017-02-03
 */
public class Board{

    private static boolean DEBUG = false;
    private static Random random = new Random();
    private static final int MAX_BOARD_SIZE = 9;
    public enum Action { LEFT, RIGHT, UP, DOWN }
    private String state;

    // *************
    // INITIAL STATE
    // *************


    // *** SETTERS ***

    /**
     * set
     *
     * Set a user-defined board.
     */
    void set(String state){
        this.state = state;
    }


    /**
     * setRandom
     *
     * Generates a random board.
     */
    void setRandom(){
        Map<Integer, Integer> tilemap = new HashMap<>(); // position and tile value map
        Iterator positerator; // position iterator
        Set positions; // set of positions
        int randompos; // a random tile position
        int key; // tile position
        int value; // value at tile position

        // initialize stack to hold tiles
        Stack<Integer> tileStack = new Stack<>();

        if(DEBUG) System.out.println("Generating puzzle.");

        // create tiles 0-8, add to stack
        // ensures no duplicate numbers
        for(int i = 0; i < MAX_BOARD_SIZE; i++){
            //add tiles to stack
            tileStack.push(i);

            if(DEBUG) System.out.println("Pushing value '" + i + "' to stack.");
        }

        // empty the tile stack to the board
        while(!tileStack.isEmpty()) {
            randompos = random.nextInt(MAX_BOARD_SIZE);
            if (!tilemap.containsKey(randompos)) {
                int tilevalue = tileStack.pop();
                tilemap.put(randompos, tilevalue);

                if(DEBUG) System.out.println("Assigning value '" + tilevalue + "' to position " + randompos + "");
            }
        }

        //initialize set of positions (keys)
        positions = tilemap.keySet();

        //enable iteration of positions
        positerator = positions.iterator();

        // create a board (string)
        StringBuilder statebuilder = new StringBuilder();

        // get tile value at each position and place into 'statebuilder'
        while (positerator.hasNext()) {
            key = (Integer)positerator.next();
            value = tilemap.get(key);
            statebuilder.append(value);
        }

        // convert board state to string
        state = statebuilder.toString();

        if(DEBUG) System.out.println("Finished generating.");
    }


    // *** GETTERS ***


    /***
     * Get a string representation of the board's state.
     *
     * @return Board's state in string format
     */
    String getState(){ return state; }


    /***
     * Return all possible moves of the board's state.
     *
     * @return List of the board's possible moves
     */
    List<Action> getMoves(){

        List<Action> actions = new ArrayList<>();

        if(DEBUG) System.out.print("Current state: " + getPosition() + "\n" +
                "Moves: ");

        // add available horizontal moves based on location
        switch(getPosition()){
            case 0:
            case 3:
            case 6:
                actions.add(Action.RIGHT);
                if(DEBUG) System.out.print("RIGHT ");
                break;
            case 1:
            case 4:
            case 7:
                actions.add(Action.RIGHT);
                if(DEBUG) System.out.print("RIGHT ");
                actions.add(Action.LEFT);
                if(DEBUG) System.out.print("LEFT ");
                break;
            case 2:
            case 5:
            case 8:
                actions.add(Action.LEFT);
                if(DEBUG) System.out.print("LEFT ");
                break;
        }

        // add available vertical moves based on location
        switch(getPosition()){
            case 0:
            case 1:
            case 2:
                actions.add(Action.DOWN);
                if(DEBUG) System.out.print("DOWN ");
                break;
            case 3:
            case 4:
            case 5:
                actions.add(Action.DOWN);
                if(DEBUG) System.out.print("DOWN ");
                actions.add(Action.UP);
                if(DEBUG) System.out.print("UP ");
                break;
            case 6:
            case 7:
            case 8:
                actions.add(Action.UP);
                if(DEBUG) System.out.print("UP ");
                break;
        }

        if(DEBUG) System.out.println();

        return actions;
    }


    /***
     * Get the position of the blank tile.
     *
     * @return The blank tile's position.
     */
    private int getPosition(){
        return state.indexOf('0');
    }


    /***
     * Get the blank tile's possible destination.
     *
     * @param action Direction of possible blank tile destination.
     * @return destination
     */
    int getDestination(Action action){
        int position = getPosition();
        int destination = 0;

        switch(action){
            case LEFT:
                destination = position - 1;
                break;
            case RIGHT:
                destination = position + 1;
                break;
            case UP:
                destination = position - 3;
                break;
            case DOWN:
                destination = position + 3;
                break;
        }
        return destination;
    }


    /***
     * Displays the current state of the board.
     */
    public void print(){
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                System.out.print(state.charAt((3 * i) + j) + " ");
            }
            System.out.println();
        }
    }

}