package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.time.Duration;

public class Publisher extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	// Topic reference
	private ActorRef topicRef;
	
    private int duration;
    private String msg1;
    private String msg2;

	public Publisher(int duration, String msg1, String msg2) {
        this.duration = duration;
        this.msg1 = msg1;
        this.msg2 = msg2;
    }

	// Static function creating actor
	public static Props createActor(int duration, String msg1, String msg2) {
		return Props.create(Publisher.class, () -> {
			return new Publisher(duration, msg1, msg2);
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
        if(message instanceof StringAndRef){
			log.info(getSelf().path().name() + " receives from " + getSender().path().name() + ": " + ((StringAndRef) message).message);
            topicRef = ((StringAndRef) message).ref;
            getContext().system().scheduler().scheduleOnce(Duration.ofMillis(this.duration), getSelf(), "go", getContext().system().dispatcher(), ActorRef.noSender());
        }

        if(message instanceof String) {
            if(((String)message).equals("go")) {
                topicRef.tell(msg1, getSelf());
                getContext().system().scheduler().scheduleOnce(Duration.ofMillis(6000), getSelf(), "go again", getContext().system().dispatcher(), ActorRef.noSender());    
            }
            if(((String)message).equals("go again") && getSelf().path().name().equals("publisher1")) {
                topicRef.tell(msg2, getSelf());
            }
        }
	}
}