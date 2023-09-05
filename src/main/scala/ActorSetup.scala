import akka.actor.{ActorSystem,Actor,Props}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._

object ActorSetup extends App {
    implicit val timeout = Timeout(10 seconds)
    val actorSys = ActorSystem("HelloWorld")
    val actor = actorSys.actorOf(Props[AddActor], "addActor")
    
    // Ask for a response back from our request for an answer
    val future = (actor ? 5).mapTo[Int]
    val sum = Await.result(future, 10 seconds)
    println(sum)

    val future2 = (actor ? 10).mapTo[Int]
    val sum2 = Await.result(future2, 10 seconds)
    println(sum2)
}

class AddActor extends Actor{
    var sum = 0

    override def receive:Receive = {
        case x: Int => sum = sum + x
        // Send a message containing the sum to the sender
        sender!sum

        case _ => println("Error! Bad data!")
    }
}