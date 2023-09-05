import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import akka.actor.{Actor, ActorSystem, OneForOneStrategy, Props, PoisonPill}
import scala.concurrent.duration._ 

// duration - how long you are willing to wait to try to restart a process
// demo arithmetic error
// demo unhandled exception

class Printer extends Actor {
    // usually a step to clean up before a restart
    override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
        println("I am restarting!")
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
            println(amount)
        case msg: String => throw new IllegalArgumentException
    }

    // triggered after stop happens
    override def postStop = {
        println("I am stopping!")
    }
}

class SupervisorStrategy extends Actor {
    override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries=10,withinTimeRange= 1 minute){
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
            intAdder!10
            intAdder!10
            intAdder!"Hello!" // will cause failure from illegal argument exception
    }
}

object oneForOne extends App {
    val actorSys = ActorSystem("oneForOne")
    val sup = actorSys.actorOf(Props[SupervisorStrategy])
    sup!"Start"
}
