package demo;

import akka.actor.ActorRef;

public class MessageRef {
    ActorRef ref;

    public MessageRef (ActorRef ref) {
        this.ref = ref;
    }
}