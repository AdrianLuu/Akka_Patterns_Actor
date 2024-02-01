package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class ControlledFlooding {
    public static void main(String[] args) {
		// Instantiate an actor system
        final ActorSystem system = ActorSystem.create("System");

		// Define the adjacency matrix for an infinite loop
		boolean[][] adjacencyMatrix = {
            {false, true, false, false, false},
            {false, false, false, true, false},
            {false, false, false, true, false},
            {false, false, false, false, true},
            {false, true, false, false, false}   
        };

		// Instantiate all 4 actors
		ActorRef[] nodes = new ActorRef[adjacencyMatrix.length];
		for (int i = 0; i < adjacencyMatrix.length; i ++){
			nodes[i] = system.actorOf(Node.createActor(), "node" + (char) ('A' + i));
		}

		// Set up the neighbors for each actor
		for (int i = 0; i < adjacencyMatrix.length; i ++){
			RefList neighborMsg = new RefList(new ArrayList<ActorRef>());
			for (int j = 0; j < adjacencyMatrix.length; j ++){
				if (adjacencyMatrix[i][j]){
					neighborMsg.refList.add(nodes[j]);
				}
			}
			nodes[i].tell(neighborMsg, ActorRef.noSender());	
		}

		// Start the flooding algo from node A with sequence numbers 0, 1, and 2
		nodes[0].tell(new FloodMessage("Hey yo", 0), ActorRef.noSender());
		nodes[0].tell(new FloodMessage("Hey yo", 1), ActorRef.noSender());
		nodes[0].tell(new FloodMessage("Hey yo", 2), ActorRef.noSender());

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
