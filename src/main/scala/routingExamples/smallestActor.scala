package routingExamples

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.SmallestMailboxPool

// Distributed computing example

// whichever actor has the least amount of activity is used (or randomly selected if the same)
class mailboxActor extends Actor {
  override def receive = {
    case msg: String => println(s"I am ${self.path.name}, message is: $msg")
    case _ => println("Invalid message!")
  }
}

object smallestActor extends App {
  val actorSystem = ActorSystem("Route")
  val router = actorSystem.actorOf(SmallestMailboxPool(5).props(Props[mailboxActor]))

  for (i <- 1 to 10) {
    router!s"Hello $i"
  }
}
