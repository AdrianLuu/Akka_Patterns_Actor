package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ReceiverActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	// Actor reference
	private ActorRef broadcaster;  

	public ReceiverActor() {}

	// Static function that creates actor
	public static Props createActor() {
		return Props.create(ReceiverActor.class, () -> {
			return new ReceiverActor();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof StringAndRef){
			this.broadcaster = ((StringAndRef) message).ref;
			log.info("Message received from " + getSender().path().name() + ": " + ((StringAndRef) message).message);
			broadcaster.tell(new MessageString("join"), getSelf());	
		}
		if(message instanceof MessageString){
			log.info("Message received from " + getSender().path().name() + ": " + ((MessageString) message).data);
		}
	}
}



