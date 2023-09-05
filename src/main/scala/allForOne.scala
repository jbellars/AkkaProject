import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import akka.actor.{Actor, ActorSystem, AllForOneStrategy, Props}
import scala.concurrent.duration._ 

// demos how all actors are acted upon if one of them fails (all for one strategy)

// duration - how long you are willing to wait to try to restart a process
// demo arithmetic error
// demo unhandled exception
// demos how all actors are acted upon when one fails (all for one strategy)

class Printer extends Actor {
    // usually a step to clean up before a restart
    override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
        println("Printer: I am restarting!")
    }

    def receive={
        case msg: String => println(msg)
        // Force an error when sent an integer
        case msg: Int => 1/0
    }
}

class IntAdder extends Actor {
    var amount = 0
    def receive = {
        case msg: Int => amount = amount + msg
            println(s"sum is $amount")
        case msg: String => throw new IllegalArgumentException
    }

    // triggered after stop happens
    override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
        println("IntAdder: I am stopping!")
    }
}

class SupervisorStrategy extends Actor {
    override val supervisorStrategy = AllForOneStrategy(maxNrOfRetries=10,withinTimeRange= 1 minute){
        case _: ArithmeticException => Restart // the actor
        case _: IllegalArgumentException => Stop
        case _: NullPointerException => Resume
        // any exception not already handled:
        case _: Exception => Escalate // it will fail
    }
    val printer = context.actorOf(Props[Printer])
    val intAdder = context.actorOf(Props[IntAdder])

    def receive = {
        case "Start" =>
            printer!"Hello!"
            printer!10 // will cause arithmetic exception from div by zero

    }
}

object allForOne extends App {
    val actorSys = ActorSystem("allForOne")
    val sup = actorSys.actorOf(Props[SupervisorStrategy])
    sup!"Start"
}
