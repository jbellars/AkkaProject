package routingExamples

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.RoundRobinPool

// Distributed computing example

// whichever actor has the least amount of activity is used (or randomly selected if the same)
class mailboxActor3 extends Actor {
  override def receive = {
    case msg: String => println(s"I am ${self.path.name}, message is: $msg")
    case _ => println("Invalid message!")
  }
}

object roundRobinActor extends App {
  val actorSystem = ActorSystem("Route")
  val router = actorSystem.actorOf(RoundRobinPool(5).props(Props[mailboxActor3]))

  for (i <- 1 to 10) {
    router!s"Hello $i"
    // Thread.sleep(200) // <-- visibly demonstrates round robin in orderly fashion in more pronounced fashion
  }
}
