package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SecondActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public SecondActor() {}

	// Static function that creates actor
	public static Props createActor() {
		return Props.create(SecondActor.class, () -> {
			return new SecondActor();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageString){
			log.info(getSelf().path().name() + " receives from " + getSender() + ": " + ((MessageString) message).data);
			// Use Substring to retrieve the sequence number of the request message
			MessageString msg = new MessageString("res" + ((MessageString) message).data.substring(3)); 
			getSender().tell(msg, getSelf()); 
		}
	}
}



