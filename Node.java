/**
 * Created by chris on 1/21/17.
 */

public class Node {

    private Node parent;
    private int position;
    private Board.Action action;
    private int pathcost;
    private String state;

    public Node(Board problem, int pathcost) {
        //root node
        this.state = problem.getState();
        this.position = problem.findPosition(problem.getState());
        this.pathcost = pathcost;
    }

    public Node(Board problem, Node parent, Board.Action action) {
        //child node
        this.parent = parent; //parent node of this node
        this.action = action; //action taken from parent to reach this node
        this.position = problem.getDest(parent.getPosition(), action);
        this.state = problem.swapPosition(position, parent.getState());
        this.pathcost = parent.getPathCost() + problem.getStepCost(parent.getState(), action); //cost to reach this state
    }

    public int getPathCost(){ return pathcost; }
    public int getPosition(){ return position; }
    public String getState(){ return state; }
    public Board.Action getAction(){ return action; }
    public Node getParent(){ return parent; }
}
