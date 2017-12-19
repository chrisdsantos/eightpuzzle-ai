import java.util.*;

/***
 * An 8-Tile Puzzle Solver via A* search algorithm.
 *
 * @author Chris Santos
 * @version 2.0
 * @since 2017-01-21
 */
public class Solver {

    private static boolean DEBUG = true; // debugging
    private static Scanner reader = new Scanner(System.in); // reading input
    private static Comparator<Node> comparator = new NodeComparator(); // compare boards
    private static Timer timer = new Timer(); // count time to find solution
    private static int heuristictype; // keeps user-chosen heuristic function


    /***
     * Gives the user-chosen heuristic.
     *
     * @return The heuristic code.
     */
    private static int getHeuristicType(){
        return heuristictype;
    }


    /***
     * Search algorithm which implements a priority queue.
     * Prioritizes minimum path cost nodes with minimum depth.
     *
     * @param problem The initial board state to solve.
     */
    private static void aStarSearch(Board problem){

        Queue<Node> frontier = new PriorityQueue<>(comparator); // to prioritize state with minimum path cost
        Map<String,Integer> frontierlist = new HashMap<>(); // to check if state is in the frontier
        Set<String> explored = new HashSet<>(); // to record all 'explored' states
        Node root, current, nextmove; // define states
        int frontiercost, successorcost;

//        int generatedcount = 0; // count number of generated boards

        root = new Node(problem); // set initial board state
        frontierlist.put(root.getState(), 0);
        frontier.add(root); // add initial board to frontier priority queue (yet to be explored)

        // first, check if the given board is solvable
        if(!isSolvable(root.getState())) {
            System.out.println("Puzzle is unsolvable.");
            return;
        }

        while(!frontier.isEmpty()){
            current = frontier.poll(); // choose the next least cost board
            if(DEBUG) System.out.println("Polled " + current.getAction() + " with " + current.getScore() + " score.");
            frontierlist.remove(current.getState());
            problem.set(current.getState());

            // if the solution is found, print it
            if(isGoal(current.getState())) {
                reconstructPath(root, current);
                return;
            }

            // mark the current board state as 'explored'
            explored.add(current.getState());
            if(DEBUG) System.out.println("State " + current.getState() + " marked as explored.\n");

            // for each possible action, create new board states to explore (frontier)
            if(DEBUG) System.out.println("Creating new states to explore... ");

            for(Board.Action action : current.getMoves()){

                // create board states to explore for each action
                nextmove = new Node(problem, current, action);
                nextmove.setState(move(current.getState(), current.getDestination(action)));

                //if(DEBUG) System.out.println("New child state: " + nextmove.getState());

                // evaluate child node's cost
                nextmove.setScore(getScore(nextmove));

                if(DEBUG) System.out.println(
                        "Action: " + nextmove.getAction() + ", "
                                + "State: " + nextmove.getState() + ", "
                                + "Pathcost: " + nextmove.getPathCost() + ", "
                                + "Score: " + nextmove.getScore()
                );

                // if state has been explored, try next lowest cost state (loop)
                if(explored.contains(nextmove.getState())) {
                    if(DEBUG) System.out.println("State discarded.");
                    continue;
                }

                //if state has not been explored, but is in frontier set, compare costs
                if(frontierlist.containsKey(nextmove.getState())){
                    frontiercost = frontierlist.get(nextmove.getState()); // <--- the cost of frontier node to compare to successor
                    successorcost = getScore(current); // <--- the cost of the successor
                    if(successorcost < frontiercost){
                        frontierlist.replace(nextmove.getState(), successorcost);
                        nextmove.setScore(frontiercost);
                        frontier.remove(nextmove);
                        nextmove.setScore(successorcost);
                        frontier.add(nextmove);
                        if(DEBUG) System.out.println("State edited.");
                    }
                }else{
                    //state is not in frontier set, add to frontier set
                    frontierlist.put(nextmove.getState(), getScore(nextmove));
                    frontier.add(nextmove);
                    if(DEBUG) System.out.println("State added.");
                }
            }
        }
        // if the queue is empty, the program failed to find a solution, stops
        System.out.println("Failure.");
    }


    // *********
    // PATH COST
    // *********


    /***
     * Gives the cost h(n) of the action taken from a given position. ---> h(n)
     *
     * @param state The node's state.
     * @return Heuristic cost h(n) of the given action.
     */
    private static int getHeuristicCost(String state){
        if(getHeuristicType() == 1) {
            return countMisplaced(state);
        }else{
            return sumDistances(state);
        }
    }


    /**
     * Get the overall cost f(n) of taking an action.
     *
     * @param state Node to evaluate.
     * @return Overall cost f(n) of the given action.
     */
    private static int getScore(Node state){
        if(state.getParent() != null) {
            return state.getPathCost() + getHeuristicCost(state.getParent().getState()); //<--- f(n) = g(n) + h(n)
        }
        return state.getPathCost();
    }


    /***
     * Displays the path to the solution.
     *
     * @param root The initial problem.
     * @param current The goal state.
     */
    private static void reconstructPath(Node root, Node current){

        Stack<String> stateorder = new Stack<>(); // use stack to record path to solution
        Node path;
        int solutiondepth = 0; // keep count of generated boards, solution's depth

        System.out.println("Solution found.\n");
        System.out.println("*** REPORT ***");
        System.out.println("Original state: " + root.getState());
//        System.out.println("# Nodes generated: " + generatedcount);
        // set the current board as the final part of the solution (for tracing back to initial board state)
        path = current;

        // traverse solution's path in reverse order, count its depth, and record the path (push each board state to stack)
        while(path.getParent() != null){
            path = path.getParent();
            solutiondepth++;
            stateorder.push(path.getState());
        }
        System.out.println("Goal node depth: " + solutiondepth);

        // display the recorded path (pop each board state from stack)
        while(!stateorder.isEmpty()){
            System.out.println(stateorder.pop());
        }
    }


    /***
     * Determines if current state is the goal state.
     *
     * @param state A board state.
     * @return If the current state is goal state.
     */
    private static boolean isGoal(String state){
        System.out.println(countMisplaced(state) == 0);
        return countMisplaced(state) == 0;
    }


    /***
     * Determines whether puzzle is solvable by counting the number of inverted tiles.
     *
     * @param state A board state.
     * @return If the current state is solvable.
     */
    private static boolean isSolvable(String state){
        int current;
        int compares;
        int total = 0;

        // index i pointing starting from current position to 'i-1' position
        for(int i = 0; i < state.length() - 1; i++){
            current = Character.getNumericValue(state.charAt(i));

            if(current == 0) continue; // if current tile is empty, skip (all other tile values > 0)

            // compare the current tile to all the following tiles
            for(int j = i + 1; j < state.length(); j++){
                compares = Character.getNumericValue(state.charAt(j));

                if(compares == 0) continue; // if next tile is empty, skip (all other tile values > 0)
                if(current > compares) total++; // if 'current' tile's value > the compared tile's value, count it
            }
        }

//        if(DEBUG) System.out.println("Total inversions: " + total);

        return total % 2 == 0; // to be solvable, the total must be an even number
    }


    // HEURISTIC FUNCTIONS
    // functions which help determine how close the program is to reaching its goal state


    /***
     * Counts number of tiles which are not at 'home' position.
     *
     * @param state A board state.
     * @return Number of misplaced tiles.
     */
    private static int countMisplaced(String state){
        int tilenumber;
        int count = 0;

        for(int currentpos = 0; currentpos < state.length(); currentpos++){
            tilenumber = Character.getNumericValue(state.charAt(currentpos));
            if(currentpos != tilenumber){
                count++;
            }
        }

        if(DEBUG) System.out.println("Total misplaced: " + count);

        return count;
    }


    /***
     * Determines how far each tile is from 'home' position, given a state.
     * Also known as Manhattan distance heuristic.
     *
     * @param state A board state.
     * @return Sum of the distances of each tile from their 'home' positions.
     */
    private static int sumDistances(String state){
        int tilenumber;
        int distance;
        int moves;
        int total = 0;

        if(DEBUG) System.out.print("Total distance = ");

        for(int currentpos = 0; currentpos < state.length(); currentpos++){

            // get the value of the tile at current position
            tilenumber = Character.getNumericValue(state.charAt(currentpos));
            if(currentpos != tilenumber){
                // calculate the tile's distance from its 'origin' aka home position
                distance = Math.abs(currentpos - tilenumber);

                // calculate how many moves must be made to reach tile's home position
                // using division by 3 to count ith tile's distance by row and
                // modulus by 3 to count ith tile's distance by column
                moves = distance / 3 + distance % 3;

                if(DEBUG) System.out.print(moves + " + ");

                //add the value to the sum of distances
                total += moves;
            }
        }
        if(DEBUG) System.out.println("= " + total);
        return total;
    }


    // ****************
    // TRANSITION MODEL
    // ****************


    /**
     * Move blank tile to a given tile position.
     *
     * @param destination The blank tile's destination position.
     */
    private static String move(String state, int destination){
        char temp;
        String newstate;

        temp = state.charAt(destination);

        newstate = state;
        newstate = newstate.replace(temp, '_');
        newstate = newstate.replace('0', temp);
        newstate = newstate.replace('_', '0');
        return newstate;
    }


    // BASIC FUNCTIONS


    /***
     * Displays the program options
     *
     * @return The selection (as integer.)
     */
    private static int menu(){
        System.out.println("8-Tile Puzzle Solver\n");
        System.out.println("\n* MENU *\n" +
                "1. Generate random puzzle and solve with h1(n)\n" +
                "2. Generate random puzzle and solve with h2(n)\n" +
                "3. Input puzzle and solve with h1(n)\n" +
                "4. Input puzzle and solve with h2(n)\n" +
                "5. Exit");
        System.out.print(">> ");
        return reader.nextInt();
    }


    /***
     * Allows the user to a set board state.
     *
     * @param problem A board state.
     */
    private static void setBoard(Board problem){
        System.out.print("Input tile order: ");
        problem.set(reader.next());
    }


    /***
     * @param args Unused.
     */
    public static void main(String args[]){

        Board problem = new Board();

        while(true) {
            int choice = menu();
            switch (choice) {
                case 1:
                case 2:
                    problem.setRandom();
                    heuristictype = choice;
                    break;
                case 3:
                case 4:
                    setBoard(problem);
                    heuristictype = choice - 2;
                    break;
                case 5:
                    reader.close();
                    System.exit(0);
                default:
                    break;
            }


            timer.reset(); // start timer and solve

            System.out.println("Solving...");
            aStarSearch(problem);

            System.out.println("Elapsed time: " + timer.getElapsedTime() / Math.pow(10, -9) + " ns"); // output total time
        }
    }
}