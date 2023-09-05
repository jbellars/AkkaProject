package schedulingExamples

import akka.actor.ActorSystem

import scala.concurrent.duration._

object ScheduleOperation extends App {
  val system = ActorSystem("sched")
  import system.dispatcher

  // good for one off events that need to happen once during the course of a program
  system.scheduler.scheduleOnce(10 seconds){
    println("Hello!")
  }

  system.scheduler.schedule(11 seconds, 2 seconds){
    println("Repeating Hello!")
  }

}
