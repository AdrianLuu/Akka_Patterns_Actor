package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class TellToAndForget {
    public static void main(String[] args) {
		// Instantiate an actor system
        final ActorSystem system = ActorSystem.create("System");

		// Inst first and second actors, and transmitter
	    final ActorRef a = system.actorOf(FirstActor.createActor(), "a");	
		final ActorRef b = system.actorOf(SecondActor.createActor(), "b");
        final ActorRef transmitter = system.actorOf(Transmitter.createActor(), "transmitter");
        
		RefAndRef m1 = new RefAndRef(transmitter, b);
		a.tell(m1, ActorRef.noSender());

		MessageString m2 = new MessageString("start");
		a.tell(m2, ActorRef.noSender());	

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
