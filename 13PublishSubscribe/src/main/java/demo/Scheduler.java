package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class Scheduler {
    public static void main(String[] args) {
		// Instantiate an actor system
        final ActorSystem system = ActorSystem.create("System");

		// Instantiate actors
	    final ActorRef a = system.actorOf(Actor.createActor(), "a");	
		final ActorRef b = system.actorOf(Actor.createActor(), "b");
		final ActorRef c = system.actorOf(Actor.createActor(), "c");

		// Instantiate publishers
		final ActorRef publisher1 = system.actorOf(Publisher.createActor(1000, "hello", "hello2"), "publisher1");
		final ActorRef publisher2 = system.actorOf(Publisher.createActor(2000, "world", ""), "publisher2");

		// Instantiate topics
        final ActorRef topic1 = system.actorOf(Topic.createActor(), "topic1");
		final ActorRef topic2 = system.actorOf(Topic.createActor(), "topic2");
        
		// Create reference message
		RefAndRef topicRefMsg = new RefAndRef(topic1, topic2);
        StringAndRef topic1RefMsg = new StringAndRef(topic1, "references to topic 1");
		StringAndRef topic2RefMsg = new StringAndRef(topic2, "references to topic 2");
		
		a.tell(topicRefMsg, ActorRef.noSender());
		b.tell(topicRefMsg, ActorRef.noSender());
		c.tell(topicRefMsg, ActorRef.noSender());
		
		publisher1.tell(topic1RefMsg, ActorRef.noSender());
		publisher2.tell(topic2RefMsg, ActorRef.noSender());

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
