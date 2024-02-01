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
			log.info(getSelf().path().name() + " received flooding message '" + ((FloodMessage) message).message + "' from " + getSender().path().name());
            for(ActorRef ref : neighbors) {
				ref.tell(message, getSelf());
			}
		}
    }
}