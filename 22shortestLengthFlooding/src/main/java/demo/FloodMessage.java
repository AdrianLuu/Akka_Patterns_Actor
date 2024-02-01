package demo;

public class FloodMessage {
    String message;
    int seqNum;
    int length;

    public FloodMessage(String message, int seqNum, int length) {
        this.message = message;
        this.seqNum = seqNum;
        this.length = length;
    }
}