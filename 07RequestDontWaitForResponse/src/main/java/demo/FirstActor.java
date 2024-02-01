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
	private ActorRef actorRef;  // Actor b 

	public FirstActor() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(FirstActor.class, () -> {
			return new FirstActor();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageRef){
			this.actorRef = ((MessageRef) message).ref;
			log.info(getSelf().path().name() + " references to b ");
			messageLoop(30);
		}
		if(message instanceof MessageString){
			log.info(getSelf().path().name() + " receives from " + getSender() + ": " + ((MessageString) message).data);
		}
	}

	public void messageLoop(int num){
		for(int i=0; i < num; i++){
            MessageString msg = new MessageString("req" + (i+1));
		    this.actorRef.tell(msg, getSelf());
		}
	}
}