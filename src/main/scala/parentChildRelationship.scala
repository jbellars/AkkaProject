import akka.actor.{ActorSystem, Props, Actor}

// parent-children scenario
// one actor is able to manage other actors and help them recover if they have trouble

case object CreateChild
case class Greet(msg: String)

class ChildActor extends Actor {
    def receive = {
        // displays that a relationship exists between parent and child
        case Greet(msg: String) => println(s"My parent [${self.path.parent}] greeted me [${self.path}] with a message: $msg")
    }
}

class ParentActor extends Actor {
    def receive = {
        case CreateChild =>
        // creates an actor of type ChildActor
        val child = context.actorOf(Props[ChildActor], "child")
        // sends the child a greeting message
        child!Greet("Hello!")
    }
}

// create app that demonstrates the relationship
object parentChildRelationship extends App {
    // sets up parent child actor system
    val actorSystem = ActorSystem("ParentChild")
    // creates an actor of type ParentActor
    val parent = actorSystem.actorOf(Props[ParentActor], "Parent")
    // sends a create child message
    parent!CreateChild
}