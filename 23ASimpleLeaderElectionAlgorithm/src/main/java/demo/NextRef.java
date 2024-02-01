package demo;
import akka.actor.ActorRef;

public class NextRef {
    private final ActorRef next;

    public NextRef(ActorRef next) {
        this.next = next;
    }

    public ActorRef getNext() {
        return next;
    }
}
