package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class ShortestLengthFlooding {
    public static void main(String[] args) {
		// Instantiate an actor system
        final ActorSystem system = ActorSystem.create("System");

		// Define the adjacency matrix
		int [][] adjacencyMatrix = {
			{0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0}, 
            {1, 1, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0}, 
            {1, 1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
            {1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
            {1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1}, 
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, 
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0}, 
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0}
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
				if (adjacencyMatrix[i][j] == 1){
					neighborMsg.refList.add(nodes[j]);
				}
			}
			nodes[i].tell(neighborMsg, ActorRef.noSender());	
		}

		// Start the flooding algo from node A with sequence numbers 0
		nodes[0].tell(new FloodMessage("Message started with A", 0, 0), ActorRef.noSender());
		
		// Start the flooding algo from node L with sequence numbers 1
		nodes[11].tell(new FloodMessage("Message started with L", 1, 0), ActorRef.noSender());

		// Start the flooding algo from node I with sequence numbers 2
		nodes[8].tell(new FloodMessage("Message started with I", 2, 0), ActorRef.noSender());
        

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
