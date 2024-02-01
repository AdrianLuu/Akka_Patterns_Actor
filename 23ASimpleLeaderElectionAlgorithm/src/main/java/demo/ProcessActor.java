package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ProcessActor extends UntypedAbstractActor {

    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private final int id;
    private boolean participating = false; // Initially each process in the ring is marked as non-participant.

    // Actor reference
    private ActorRef next;
    private int electedId;
    

    public ProcessActor(int id) {
        this.id = id;
    }

    static public Props createActor(int id) {
        return Props.create(ProcessActor.class, () -> new ProcessActor(id));
    }

    @Override
	public void onReceive(Object message) throws Throwable {
        if (message instanceof NextRef) {
            this.next = ((NextRef) message).getNext(); // References to the next process
        }
        if (message instanceof ElectionMessage) {
            ElectionMessage electionMessage = (ElectionMessage) message;
            handleElectionMessage(electionMessage);
        } 
        if (message instanceof ElectedMessage) {
            ElectedMessage electedMessage = (ElectedMessage) message;
            handleElectedMessage(electedMessage);
        }
	}

    private void handleElectionMessage(ElectionMessage message) {
        int incomingId = ((ElectionMessage) message).getId();
        if (incomingId > this.id) {
            participating = true; 
            sendElectionMessage(incomingId); // Forward the election message with the same incoming ID
        }
        if (incomingId < this.id) {
            if(!participating) {
                participating = true; // Mark self as participant
                sendElectionMessage(this.id); // Send an new election message with own ID
            } else {
                log.info("Process {} discards the election message", this.id);
            }
        }
        if (incomingId == this.id) {
            log.info("Process {} starts acting as the leader", this.id);
            participating = false; // The leader process marks itself as non-participant 
            this.electedId = this.id; // Records the elected UID
            sendElectedMessage(this.id); 
        } 
    }

    private void handleElectedMessage(ElectedMessage message) {
        int electedId = ((ElectedMessage) message).getId();
        if (electedId != this.id) {
            participating = false; 
            this.electedId = electedId; // Records the elected UID
            log.info("Process {} records {} as the leader", this.id, electedId);
            next.tell(message, getSelf()); // Forwards the elected message unchanged
        } else {
            log.info("Process {} received its own elected message. The election is complete.", this.id);
        }
    }

    private void sendElectionMessage(int id) {
        log.info("Process {} is sending an election message with ID {}", this.id, id);
        next.tell(new ElectionMessage(id), getSelf());
    }

    private void sendElectedMessage(int id) {
        log.info("Process {} is sending an elected message with ID {}", this.id, id);
        next.tell(new ElectedMessage(id), getSelf());
    }

}
