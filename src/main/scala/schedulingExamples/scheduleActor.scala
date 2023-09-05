package schedulingExamples

import akka.actor.{Actor, ActorSystem, Props}
import scala.concurrent.duration._

// extend scheduling to trigger actors

class MessageActor extends Actor {
  def receive = {
    case "message1" => println("First message!")
    case "message2" => println("Second message!")
  }
}

object scheduleActor extends App {
  val system = ActorSystem("Schedule")
  import system.dispatcher
  val actor = system.actorOf(Props[MessageActor])

  system.scheduler.scheduleOnce(10 seconds, actor, "message1")
  system.scheduler.schedule(11 seconds, 2 seconds, actor, "message2")
}
