package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.concurrent.ExecutionContext;
import java.time.Duration;
import java.lang.String;

public class Actor extends UntypedAbstractActor {

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	// Actor reference
	private ActorRef topic1;
	private ActorRef topic2;

	public Actor() {
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Actor.class, () -> {
			return new Actor();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof RefAndRef) {
			log.info(getSelf() + " references to topics");
			this.topic1 = ((RefAndRef) message).ref1;
			this.topic2 = ((RefAndRef) message).ref2;

			switch (getSelf().path().name()) {
				case "a":
					this.topic1.tell(new MessageString("subscribe"), getSelf());
					// Actor a waits for 3 seconds before unsubscribing from topic1
					// getContext().system().scheduler().scheduleOnce(Duration.ofMillis(3000), getSelf(), "unsubscribe", getContext().system().dispatcher(), ActorRef.noSender());
					break;
				case "b":
					this.topic1.tell(new MessageString("subscribe"), getSelf());
					this.topic2.tell(new MessageString("subscribe"), getSelf());
					break;
				case "c":
					this.topic2.tell(new MessageString("subscribe"), getSelf());
					break;
			}
		}
		
		if (message instanceof MessageString) {
			log.info(getSelf().path().name() + " receives from " + getSender().path().name() + ": " + ((MessageString) message).data);
			
			if (getSender().path().name().equals("topic2") && ((MessageString) message).data.equals("world")) {
				// Actor a should unsubscribe after receiving "world" from topic2
				this.topic1.tell(new MessageString("unsubscribe"), getSelf());
			}	 
		}
		/*
		if(message instanceof String) {
			if(((String) message).equals("unsubscribe")) {
				if (getSelf().path().name().equals("a")) {
					this.topic1.tell(new MessageString("unsubscribe"), getSelf());
				}
				
			}
		}
		*/
	}
}