package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;

public class RefList {
    ArrayList<ActorRef> refList;

    public RefList (ArrayList<ActorRef> refList) {
        this.refList = refList;
    }
}