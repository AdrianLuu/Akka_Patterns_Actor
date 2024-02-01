package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.ArrayList;

public class Topic extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this); 

	private ArrayList<ActorRef> subscribers = new ArrayList<>();

	public Topic() {}

	// Static function that creates actor
	public static Props createActor() {
		return Props.create(Topic.class, () -> {
			return new Topic();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageString){
			log.info(getSelf() + " receives from " + getSender() + ": " + ((MessageString) message).data);
			if(((MessageString) message).data.equals("subscribe")) {
				subscribers.add(getSender());
			} else if(((MessageString) message).data.equals("unsubscribe")) {
				subscribers.remove(getSender());
			} else {
				for(ActorRef subscriber : subscribers) {
					subscriber.tell(message, getSelf());
				}
			}
		}
	}
}



