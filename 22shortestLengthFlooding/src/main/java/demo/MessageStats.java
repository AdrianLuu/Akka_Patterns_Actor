package demo;

public class MessageStats {
    private int count; 
    private int minLength; 

    public MessageStats() {
        this.count = 0;
        this.minLength = Integer.MAX_VALUE; 
    }

    public void updateStats(int length) {
        count++; 
        if (length < minLength) {
            minLength = length; 
        }
    }

    public int getCount() {
        return count;
    }

    public int getMinLength() {
        return minLength;
    }
}

