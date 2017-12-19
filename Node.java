import java.util.*;


/***
 * Node class.
 *
 * @author Chris Santos
 * @version 2.0
 * @since 2017-01-21
 */
public class Node{

    private static final int STEPCOST = 1;
    private Board.Action action;
    private Node parent;
    private int pathcost;
    private int score;
    private String state;
    private Board problem;

    // root node
    Node(Board problem) {
        this.problem = problem;
        this.state = problem.getState();
        this.parent = null;
        this.action = null;
        this.pathcost = 0;
        this.score = 0;
    }

    // child nodes
    Node(Board problem, Node parent, Board.Action action) {
        this.problem = problem;
        this.state = null;
        this.parent = parent; // preceding board state
        this.action = action; // action taken to achieve this state
        this.pathcost = parent.getPathCost() + STEPCOST; // <--- g(n)
        this.score = 0;
    }

    String getState(){ return state; }
    Node getParent(){ return parent; }
    public Board.Action getAction(){ return action; }
    int getPathCost(){ return pathcost; }
    int getScore(){ return score; }
    int getDestination(Board.Action action){ return problem.getDestination(action); }
    List<Board.Action> getMoves(){ return problem.getMoves(); }

    void setScore(int score){ this.score = score; }
    void setState(String state){ this.state = state; }
}
