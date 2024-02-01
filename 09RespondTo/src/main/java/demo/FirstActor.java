package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class FirstActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ActorRef actorRefb, actorRefc;  

	public FirstActor() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(FirstActor.class, () -> {
			return new FirstActor();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof RefAndRef){
			actorRefb = ((RefAndRef) message).ref1;
			actorRefc = ((RefAndRef) message).ref2;
			log.info(getSelf() + " received message from ({}): references to b and c", getSender());
			StringAndRef m = new StringAndRef(actorRefc, "req1");
			actorRefb.tell(m, getSelf());
		} 
	}
}