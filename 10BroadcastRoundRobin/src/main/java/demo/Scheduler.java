package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class Scheduler {
    public static void main(String[] args) {
		// Instantiate an actor system
        final ActorSystem system = ActorSystem.create("System");

		// Inst first and second actor
	    final ActorRef a = system.actorOf(SenderActor.createActor(), "a");	
		final ActorRef b = system.actorOf(ReceiverActor.createActor(), "b");
		final ActorRef c = system.actorOf(ReceiverActor.createActor(), "c");
        final ActorRef broadcaster = system.actorOf(Broadcaster.createActor(), "broadcaster");
        
		// Create a broadcaster reference message, to activate abc's first action
        StringAndRef broadcasterRefMsg = new StringAndRef(broadcaster, "references broadcaster");
		
		a.tell(broadcasterRefMsg, ActorRef.noSender());
		b.tell(broadcasterRefMsg, ActorRef.noSender());
		c.tell(broadcasterRefMsg, ActorRef.noSender());	

	    try {
			waitBeforeTerminate();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			system.terminate();
		}
	}

	public static void waitBeforeTerminate() throws InterruptedException {
		Thread.sleep(6000);
	}
		
}
