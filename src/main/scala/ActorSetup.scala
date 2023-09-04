import akka.actor.{ActorSystem,Actor,Props}

object ActorSetup extends App {
    val actorSys = ActorSystem("HelloWorld")
    val actor = actorSys.actorOf(Props[AddActor], "addActor")
    
    actor!5
    actor!4
    actor!3
}

class AddActor extends Actor{
    var sum = 0

    override def receive:Receive = {
        case x: Int => sum = sum + x
        // print the value of the sum
        println(s"Current sum: $sum")

        case _ => println("Error! Bad data!")
    }
}