package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.time.Duration;
import scala.concurrent.Await;
import scala.concurrent.Future;
import akka.pattern.Patterns;
import akka.util.Timeout;

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
	}

	public void messageLoop(int num) {
		for(int i=0; i < num; i++){
			Timeout timeout = Timeout.create(Duration.ofSeconds(5));
			Future<Object> future = Patterns.ask(actorRef, new MessageString("req" + (i+1)), timeout);
			try{
                MessageString res = (MessageString) Await.result(future, timeout.duration());
				log.info(getSelf().path().name() + " receives from " + getSender() + ": "  + res.data);
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}