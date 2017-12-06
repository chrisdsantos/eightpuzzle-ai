import java.util.*;


public class Solver {

    private static boolean DEBUG = false;
    private static Scanner reader = new Scanner(System.in);
    private static Comparator<Node> comparator = new NodeComparator();
    private static Timer timer = new Timer();
    private static int heuristic;

    public int getHeuristic(){
        return heuristic;
    }

    /**
     * A * Search
     *
     * Search algorithm which implements a priority queue. Prioritizes minimum path cost nodes with minimum depth.
     *
     */
    private static void search(Board problem){

        PriorityQueue<Node> frontier = new PriorityQueue<>(11, comparator); // <--- to sort nodes by min. pathcost
        HashSet<String> explored = new HashSet<>(); // <--- tracks visited states
        HashMap<String, Integer> frontierset = new HashMap<>(); // <--- tracks nodes with cost
        Stack<String> stateorder = new Stack<>(); // <--- stack for path traceback
        Node root; // <--- node with initial state
        Node current; // <--- current node
        Node child; // <--- child node
        Node path; // <--- node for path traceback

        int cost;
        int numgennodes = 0;
        int numsoldepth = 0;

        root = new Node(problem, 0);
        frontier.add(root);
        if(!isSolvable(problem)) {
            System.out.println("Puzzle is unsolvable.");
            return;
        }
        // A * search alg f(n) = g(n) + h(n);
        while(true){
            if(frontier.isEmpty()){
                System.out.println("Failure.");
                return;
            }
            current = frontier.poll(); // <--- choose next least cost node
            if(DEBUG) System.out.println("Going " + current.getAction() + " with " + current.getPathCost() + " path cost.");
            //print(current.getState());
            //System.out.println();
            if(problem.isGoal(current.getState())){
                System.out.println("Solution found.\n");
                System.out.println("*** REPORT ***");
                System.out.println("Original state: " + root.getState());
                System.out.println("# Nodes generated: " + numgennodes);
                path = current;
                while(path.getParent() != null){
                    path = path.getParent();
                    numsoldepth++;
                    stateorder.push(path.getState());
                }
                System.out.println("Goal node depth: " + numsoldepth);
                while(!stateorder.isEmpty()){
                     System.out.println(stateorder.pop());
                }
                return;
                //NEED: return solution
            }
            explored.add(current.getState());
            if(DEBUG) System.out.println("State (String): " + current.getState());
            for(Board.Action action : problem.getMoves(current)){
                child = new Node(problem, current, action);
                if(DEBUG) System.out.println(
                        "Position: " + child.getPosition() + "\n"
                        + "Action: " + child.getAction() + "\n"
                        + "State: " + child.getState() + "\n"
                        + "Pathcost: " + child.getPathCost());
                if(!explored.contains(child.getState()) || !frontier.contains(child)){
                    frontier.add(child);
//                    frontierset.put(child.getState(), child.getPathCost());
                    numgennodes++;
                    if(DEBUG) System.out.println("Nodes generated: " + numgennodes);
                }
                else if(frontier.contains(child)){
                    frontier.remove(child);
                    frontier.add(child);
                }
                //check if child exists with higher cost
//                else if(frontierset.containsKey(child.getState())){
//                    cost = frontierset.get(child.getState());
//                    if(cost > child.getPathCost()){
//                        frontier.remove(child);
//                        frontier.add(child);
//                        frontierset.put(child.getState(), child.getPathCost());
//                    }
//                }
            }
        }
    }


    /**
     * isSolvable
     *
     * Determines whether puzzle is solvable.
     *
     * @return
     */
    public static boolean isSolvable(Board problem){
        int current;
        int compares;
        int total = 0;

        for(int i=0;i<problem.getState().length()-1;i++){
            current = Character.getNumericValue(problem.getState().charAt(i));
            if(current == 0) continue;
            for(int j=i+1;j<problem.getState().length();j++){
                compares = Character.getNumericValue(problem.getState().charAt(j));
                if(compares == 0) continue;
                if(current > compares){
                    total++;
                }
            }
        }
        if(DEBUG) System.out.println("Total inversions: " + total);
        return total%2 == 0;
    }


    //HEURISTIC FUNCTIONS


    /**
     * countMisplaced
     *
     * Counts number of tiles which are not at 'home' position.
     *
     * @return Number of misplaced tiles.
     */
    public int countMisplaced(String state){
        int value; //tile value
        int count = 0;

        for(int i=0;i<state.length();i++){
            value = Character.getNumericValue(state.charAt(i));
            if(i != value){
                count++;
            }
        }
        if(DEBUG) System.out.println("Total misplaced: " + count);
        return count;
    }


    /**
     * sumGoalDistance
     *
     * Determines how far each tile is from 'home' position, given a state.
     * Also known as Manhattan distance heuristic.
     *
     * @return Sum of the distances of each tile from their 'home' positions.
     */
    public int sumGoalDistance(String state){
        int value; //tile value
        int distance = 0;
        int actualdist = 0;
        int total = 0;

        if(DEBUG) System.out.print("Total distance = ");
        for(int i=0;i<state.length();i++){
            value = Character.getNumericValue(state.charAt(i));
            if(i != value){
                distance = Math.abs(i - value);
                actualdist = distance/3 + distance%3;
                if(DEBUG) System.out.print(actualdist + " + ");
                total += actualdist;
            }
        }
        if(DEBUG) System.out.println("= " + total);
        return total;
    }


    // **********
    // VIEW STATE
    // **********

    /**
     * print
     *
     * Prints current state.
     */
    public static void print(String state){
        for(int i=0;i<3;i++) {
            for(int j=0;j<3;j++) {
                System.out.print(state.charAt((3*i)+j) + " ");
            }
            System.out.println();
        }
    }


    // ****
    // MAIN
    // ****

    public static void main(String args[]){

        Board problem = new Board();

        System.out.println("8-Tile Puzzle Solver\n");
        while(true) {
            System.out.println("\n* MENU *\n" +
                    "1. Generate random puzzle and solve with h1(n)\n" +
                    "2. Generate random puzzle and solve with h2(n)\n" +
                    "3. Input puzzle and solve with h1(n)\n" +
                    "4. Input puzzle and solve with h2(n)\n" +
                    "5. Exit");
            System.out.print(">> ");
            int input = reader.nextInt();
            switch (input) {
                case 1:
                    problem.randomPuzzle();
                    heuristic = 1;
                    break;
                case 2:
                    problem.randomPuzzle();
                    heuristic = 2;
                    break;
                case 3:
                    problem.inputPuzzle();
                    heuristic = 1;
                    break;
                case 4:
                    problem.inputPuzzle();
                    heuristic = 2;
                    break;
                case 5:
                    System.exit(0);
                default:
                    break;
            }
            System.out.println("Solving...");
            timer.reset();
            search(problem);
            System.out.println("Elapsed time: " + timer.getElapsedTime() / Math.pow(10, 9) + " s");
        }
    }
}