package demo;

import akka.actor.ActorRef;

public class RefAndRef {
    ActorRef ref1;
    ActorRef ref2;

    public RefAndRef (ActorRef ref1, ActorRef ref2) {
        this.ref1 = ref1;
        this.ref2 = ref2;
    }
}