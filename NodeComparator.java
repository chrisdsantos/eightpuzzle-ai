/**
 * Created by chris on 1/30/17.
 */

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {

    public int compare(Node a, Node b) {
        if(a.getPathCost() > b.getPathCost()) return 1;
        if(a.getPathCost() < b.getPathCost()) return -1;
        return 0;
    }
}