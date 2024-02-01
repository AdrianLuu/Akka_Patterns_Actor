package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.concurrent.ExecutionContext;
import java.time.Duration;

public class SenderActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ActorRef broadcaster;  

	public SenderActor() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(SenderActor.class, () -> {
			return new SenderActor();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof StringAndRef){
			this.broadcaster = ((StringAndRef) message).ref;
			log.info("Message received from " + getSender().path().name() + ": " + ((StringAndRef) message).message);
			getContext().system().scheduler().scheduleOnce(Duration.ofMillis(1000), getSelf(), "go", getContext().system().dispatcher(), ActorRef.noSender());	
		}
		if(message instanceof String){
			log.info("Message received from " + getSender() + ": " + message.toString());
			if(message.equals("go")){
				broadcaster.tell(new MessageString("message"), getSelf());
			}
            
        }  
	}
}