package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Merger extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	// Create an ArrayList to store senderActor references (abc)
    private ArrayList<ActorRef> senderRefLst = new ArrayList<ActorRef>();
	private ActorRef receiverRef;
	private ArrayList<String> msgHistory = new ArrayList<String>();

	public Merger() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Merger.class, () -> {
			return new Merger();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {

        if(message instanceof StringAndRef){
			log.info(getSelf() + " receives from " + getSender() + ": " + ((StringAndRef) message).message);
            receiverRef = ((StringAndRef) message).ref;
        }

		if(message instanceof MessageString){
            if(senderRefLst.indexOf(getSender()) != -1){
                msgHistory.set(senderRefLst.indexOf(getSender()), ((MessageString)message).data);
                if(check()){
                    log.info("Message merged");
                    receiverRef.tell(new MessageString(msgHistory.get(0)), this.getSelf() );
                }
            }
        }

        if(message instanceof MessageJoin){
			log.info(getSelf() + " receives from " + getSender() + ": " + ((MessageJoin) message).data);
            senderRefLst.add(getSender());
            msgHistory.add("");
        }

        if(message instanceof MessageUnjoin){
            log.info(getSelf() + " receives from " + getSender() + ": " + ((MessageUnjoin) message).data);
            msgHistory.remove(senderRefLst.indexOf(getSender()));
            senderRefLst.remove(getSender());
        }
	}
    
	private boolean check(){
        String firstMsg = msgHistory.get(0);
        for(String m : msgHistory){
            if(! m.equals(firstMsg)){
                return false;
            }
        }
        return true;
	}
}