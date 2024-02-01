package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.actor.Props;

public class Node extends UntypedAbstractActor {
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	// Actor reference
    ArrayList<ActorRef> neighbors;
	ArrayList<Integer> sequence = new ArrayList<>();

	public Node() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Node.class, Node::new);
	}

    @Override
    public void onReceive(Object message) {
        if (message instanceof RefList) {
            this.neighbors = ((RefList) message).refList;
			for(ActorRef ref : neighbors) {
				log.info(getSelf().path().name() + " has reference to " + ref.path().name());
			}
        }
		if (message instanceof FloodMessage) {
			if(sequence.contains(((FloodMessage) message).seqNum)) {
				log.info(getSelf().path().name() + " dropped message with sequence " + 
				((FloodMessage) message).seqNum + " as it's already seen");
				return;
			} else {
				sequence.add(((FloodMessage) message).seqNum);
				log.info(getSelf().path().name() + " is flooding message with sequence " +
                ((FloodMessage) message).seqNum + " to neighbors");
				for(ActorRef ref : neighbors) {
					ref.tell(message, getSelf());
				}
			}
		}
    }
}