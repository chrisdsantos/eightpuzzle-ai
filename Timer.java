/**
 * Created by chris on 2/5/17.
 */
public class Timer {

//    private int comparisons;
//    private int moves;
    private long time;

    public Timer(){
//        comparisons = 0;
//        moves = 0;
        time = System.nanoTime();
    }

    public void reset(){
//        comparisons = 0;
//        moves = 0;
        time = System.nanoTime();
    }

    // *************************
    // NOT USED FOR THIS PROJECT
    // *************************

//    public void addComparison(){
//        comparisons = comparisons + 1;
//    }
//
//    public void addComparisons(int n){
//        comparisons = comparisons + n;
//    }
//
//    public void addMove(){
//        moves = moves + 1;
//    }
//
//    public void addMoves(int n){
//        moves = moves + n;
//    }
//
//    public long getComparisons(){
//        return comparisons;
//    }
//
//    public long getMoves(){
//        return moves;
//    }

    public long getElapsedTime(){
        return System.nanoTime()-time;
    }
}
