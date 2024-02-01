package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.concurrent.ExecutionContext;
import java.time.Duration;
import java.lang.String;

public class SenderActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ActorRef merger;  

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
			log.info(getSelf() + " receives from " + getSender() + ": " + ((StringAndRef) message).message);
			this.merger = ((StringAndRef) message).ref;
			this.merger.tell(new MessageJoin(), this.getSelf()); // Join merger
			log.info(getSelf() + " is joined with merger");
			getContext().system().scheduler().scheduleOnce(Duration.ofMillis(1000), getSelf(), "go", getContext().system().dispatcher(), ActorRef.noSender());
			}
	
		if(message instanceof String){

			log.info(getSelf() + " receives from " + getSender() + ": " + (String)message);

			if(((String)message).equals("go")){
				log.info(getSelf() + " sends hi to Merger");
				merger.tell( new MessageString("hi"), this.getSelf());
				if(this.getSelf().path().name().equals("c")){
					this.merger.tell(new MessageUnjoin(), this.getSelf()); // c is unjoined with merger
					log.info(getSelf() + " is unjoined with merger");
				} else {
					getContext().system().scheduler().scheduleOnce(Duration.ofMillis(1000), getSelf(), "go again", getContext().system().dispatcher(), ActorRef.noSender());
				}
			}

			if(((String)message).equals("go again") && ! this.getSelf().path().name().equals("c")){
				log.info(getSelf() + " sends hi2 to merger");
				merger.tell( new MessageString("hi2"), this.getSelf());
			}
		}
	}		
}