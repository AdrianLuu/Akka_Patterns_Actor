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
	private ActorRef actorRef1, actorRef2;  // Transmitter and actor b 

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
			this.actorRef2 = ((RefAndRef) message).ref2;
			this.actorRef1 = ((RefAndRef) message).ref1;
			log.info("received message from ({})", getSender().path().name());
		}
		if(message instanceof MessageString){
			if(((MessageString) message).data == "start"){
				StringAndRef m = new StringAndRef(actorRef2, "hello");
				actorRef1.tell(m, getSelf()); // Sender: a - itself
				log.info("Message received from " + getSender() + ": " + ((MessageString) message).data);
			}
		}
		if(message instanceof String){
            log.info("Message received: ({})", message);
        }  
	}
}