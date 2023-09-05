import akka.actor.{Props, ActorSystem, Actor, PoisonPill}

case object MyStop

class ShutDownActor extends Actor {
    override def receive:Receive = {
        case msg:String => println(s"$msg")
        case MyStop => context.stop(self)
    }
}

object shutdown extends App {
    val actorSystem = ActorSystem("shutdownTest")
    val shutdownActor1 = actorSystem.actorOf(Props[ShutDownActor], "shutdownActor1")
    shutdownActor1!"Test"
    //shutdownActor1!PoisonPill // achieves same result as Stop
    shutdownActor1!MyStop
    shutdownActor1!"Test2"
}