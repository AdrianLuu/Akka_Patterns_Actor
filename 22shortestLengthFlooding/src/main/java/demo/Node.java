package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Node extends UntypedAbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private ArrayList<ActorRef> neighbors;
    private Map<Integer, Integer> seqNumToLengthMap = new HashMap<>();
    private MessageStats stats = new MessageStats();

    public Node() {}

    public static Props createActor() {
        return Props.create(Node.class, Node::new);
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof RefList) {
            this.neighbors = ((RefList) message).refList;
        } 
		if (message instanceof FloodMessage) {
            FloodMessage floodMessage = (FloodMessage) message;
            boolean shouldFlood = true;
            int currentLength = floodMessage.length;
            int seqNum = floodMessage.seqNum;

            if (seqNumToLengthMap.containsKey(seqNum)) {
                int storedLength = seqNumToLengthMap.get(seqNum); 
                if (storedLength <= currentLength) {
                    // If the stored length is shorter or equal, drop the message.
                    shouldFlood = false;
                    log.info(getSelf().path().name() + " dropped message with sequence " + seqNum +
                            " and length " + currentLength + " as a shorter or equal length has been seen");
                }
            }

            if (shouldFlood) {
                // Store the smallest length for this sequence number.
                seqNumToLengthMap.put(seqNum, Math.min(seqNumToLengthMap.getOrDefault(seqNum, Integer.MAX_VALUE), currentLength));
                // Update stats
                stats.updateStats(currentLength);
                
                log.info(getSelf().path().name() + " is flooding message with sequence " +
                         seqNum + " and length " + currentLength + " to neighbors");
                floodMessage.length++; // Increment the length before flooding
                for (ActorRef ref : neighbors) {
                    ref.tell(floodMessage, getSelf());
                }
            }
        }
    }
}
