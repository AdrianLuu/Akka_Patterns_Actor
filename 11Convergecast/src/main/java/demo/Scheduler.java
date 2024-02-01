package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class Scheduler {
    public static void main(String[] args) {
		// Instantiate an actor system
        final ActorSystem system = ActorSystem.create("System");

		// Instantiate actors
	    final ActorRef a = system.actorOf(SenderActor.createActor(), "a");	
		final ActorRef b = system.actorOf(SenderActor.createActor(), "b");
		final ActorRef c = system.actorOf(SenderActor.createActor(), "c");
		final ActorRef d = system.actorOf(ReceiverActor.createActor(), "d");
        final ActorRef merger = system.actorOf(Merger.createActor(), "merger");
        
		// Create a merger reference message
        StringAndRef mergerRefMsg = new StringAndRef(merger, "references to merger");
		
		a.tell(mergerRefMsg, ActorRef.noSender());
		b.tell(mergerRefMsg, ActorRef.noSender());
		c.tell(mergerRefMsg, ActorRef.noSender());	

		merger.tell(new StringAndRef(d, "referencess to d"), ActorRef.noSender());

	    try {
			waitBeforeTerminate();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			system.terminate();
		}
	}

	public static void waitBeforeTerminate() throws InterruptedException {
		Thread.sleep(5000);
	}
		
}
