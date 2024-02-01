package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Broadcaster extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	// Create an ArrayList to store actor references (b and c)
    private ArrayList<ActorRef> actorLst = new ArrayList<ActorRef>();

	public Broadcaster() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Broadcaster.class, () -> {
			return new Broadcaster();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageString & ((MessageString) message).data.equals("join")){
			actorLst.add(getSender());
			log.info("Message received from " + getSender() + ": " + ((MessageString) message).data);
		}
		if(message instanceof MessageString & ((MessageString) message).data.equals("message")){
			//log.info("Message received from " + getSender() + ": " + ((MessageString) message).data);
            for(ActorRef i : actorLst){
				i.tell(message, getSelf());
			}
		}
	}
}