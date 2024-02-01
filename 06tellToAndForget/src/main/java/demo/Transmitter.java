package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Transmitter extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ActorRef actorRef; // Actor b

	public Transmitter() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Transmitter.class, () -> {
			return new Transmitter();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof StringAndRef){
			log.info("Message received from " + getSender() + ": " + ((StringAndRef) message).message);
			this.actorRef = ((StringAndRef) message).ref;
			MessageString m = new MessageString(((StringAndRef) message).message);
			actorRef.tell(m, getSender()); // Sender: a
		}
	}
}