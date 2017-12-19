/**
 * Created by chris on 2/5/17.
 */
class Timer {

//    private int comparisons;
//    private int moves;
    private long time;

    Timer(){
//        comparisons = 0;
//        moves = 0;
//        time = System.nanoTime();
    }

    void reset(){
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

    long getElapsedTime(){
        return System.nanoTime() - time;
    }
}
