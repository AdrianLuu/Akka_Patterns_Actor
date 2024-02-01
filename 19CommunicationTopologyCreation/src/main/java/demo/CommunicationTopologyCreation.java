package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class CommunicationTopologyCreation {
    public static void main(String[] args) {
		// Instantiate an actor system
        final ActorSystem system = ActorSystem.create("System");

		// Define the adjacency matrix
		boolean[][] adjacencyMatrix = {
			{false, true, true, false},
			{false, false, false, true},
			{true, false, false, true},
			{true, false, false, true}
		};

		// Instantiate all 4 actors
		ActorRef[] nodes = new ActorRef[adjacencyMatrix.length];
		for (int i = 0; i < adjacencyMatrix.length; i ++){
			nodes[i] = system.actorOf(Node.createActor(), "node" + (i+1));
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
