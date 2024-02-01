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
		return Props.create(Node.class, () -> {
				return new Node();
		});
	}

    @Override
    public void onReceive(Object message) {
        if (message instanceof RefList) {
            this.neighbors = ((RefList) message).refList;
			for(ActorRef ref : neighbors) {
				log.info(getSelf().path().name() + " has reference to " + ref.path().name());
			}
        } 
    }
}