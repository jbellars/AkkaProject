import akka.actor.{Props, ActorSystem, Actor, PoisonPill}

case object Stop

class ShutDownActor extends Actor {
    override def receive:Receive = {
        case msg:String => println(s"$msg")
        case Stop => context.stop(self)
    }
}

object shutdown extends App {
    val actorSystem = ActorSystem("shutdownTest")
    val shutdownActor1 = actorSystem.actorOf(Props[ShutDownActor], "shutdownActor1")
    shutdownActor1!"Test"
    //shutdownActor1!PoisonPill
    shutdownActor1!Stop
    shutdownActor1!"Test2"
}