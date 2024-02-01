package demo;

import akka.actor.ActorRef;

public class StringAndRef {
    ActorRef ref;
    String message;

    public StringAndRef (ActorRef ref, String message) {
        this.ref = ref;
        this.message = message;
    }
}