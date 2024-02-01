package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class RespondTo {
    public static void main(String[] args) {
		// Instantiate an actor system
        final ActorSystem system = ActorSystem.create("System");

		// Inst first and second actors, and transmitter
	    final ActorRef a = system.actorOf(FirstActor.createActor(), "a");	
		final ActorRef b = system.actorOf(SecondActor.createActor(), "b");
        final ActorRef c = system.actorOf(ThirdActor.createActor(), "c");
        
		RefAndRef m = new RefAndRef(b, c);
		a.tell(m, ActorRef.noSender());

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
