import java.util.Comparator;

/***
 * Created by chris on 1/30/17.
 *
 * @author Chris Santos
 * @version 2.0
 * @since 2017-01-21
 */
public class NodeComparator implements Comparator<Node> {

    public int compare(Node a, Node b) {
        return Integer.compare(a.getScore(), b.getScore());
//        if(a.getScore() > b.getScore()) return 1;
//        if(a.getScore() < b.getScore()) return -1;
//        return 0;
    }
}