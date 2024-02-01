package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SecondActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ActorRef actorRefc; 

	public SecondActor() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(SecondActor.class, () -> {
			return new SecondActor();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof StringAndRef){
			log.info(getSelf() + " receives from " + getSender() + ": " + ((StringAndRef) message).message);
			actorRefc = ((StringAndRef) message).ref;
			MessageString m = new MessageString("res1");
			actorRefc.tell(m, getSelf()); 
		}
	}
}