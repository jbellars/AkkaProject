package schedulingExamples

import akka.actor.{Actor, ActorSystem, Cancellable, Props}
import scala.concurrent.duration._

class MessageActor2 extends Actor {
  var i =3

  def receive = {
    case "message" => println("Scheduled Message!")
    i = i - 1

    if (i==0){
      CancelSchedule.cancellable.cancel()
      println("Stopped.")
    }
  }

}

object CancelSchedule extends App {
  val system = ActorSystem("schedule")
  import system.dispatcher
  val actor = system.actorOf(Props[MessageActor2])
  // schedule stored in cancellable value
  val cancellable: Cancellable = system.scheduler.schedule(0 seconds, 2 seconds, actor, "message")
}
