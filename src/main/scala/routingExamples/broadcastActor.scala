package routingExamples

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.BroadcastPool

// Distributed computing example

// whichever actor has the least amount of activity is used (or randomly selected if the same)
class mailboxActor4 extends Actor {
  override def receive = {
    case msg: String => println(s"I am ${self.path.name}, message is: $msg")
    case _ => println("Invalid message!")
  }
}

// sends the same message to multiple actors through a broadcast pool
object broadcastActor extends App {
  val actorSystem = ActorSystem("Route")
  val router = actorSystem.actorOf(BroadcastPool(5).props(Props[mailboxActor4]))

  router!s"Hello"
}
