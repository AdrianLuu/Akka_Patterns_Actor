package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class SimpleLeaderElectionAlgorithm {
    public static void main(String[] args) {

        // Instantiate an actor system
        final ActorSystem system = ActorSystem.create("System");

        // Assuming there are 18 processes in the ring
        ActorRef[] processes = new ActorRef[18];
        for (int i = 0; i < processes.length; i++) {
            processes[i] = system.actorOf(ProcessActor.createActor(i), "process" + i);
        }

        // Set up the ring topology - reference to the next process
        for (int i = 0; i < processes.length; i++) {
            ActorRef nextProcess = processes[(i + 1) % processes.length];
            processes[i].tell(new NextRef(nextProcess), ActorRef.noSender());
        }
        
        // We can choose any process to start the election
        // Here process 4 starts the election by sending election message to its neighbor process 5 
        processes[5].tell(new ElectionMessage(4), ActorRef.noSender());
    
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
