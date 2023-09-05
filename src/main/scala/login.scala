import akka.actor.{ActorSystem, Actor, Props}

// example of switching states in Akka
// initial unauthorized state
// change state to authorized state
// use "become"


class loginActor extends Actor {

    // default state of the Actor
    def receive:Receive = {
        case user: String => 
            if (user == "Bob"){
                context.become(isAuth)
            }
    }

    // authenticated state
    def isAuth:Receive = {
        case user: String => 
            if (user == "Username"){
                println("Bob")
            }

            if (user == "Logout"){
                println("Logout successful!")
                context.become(NotAuth)
            }
    }

    // de-authentcated state
    def NotAuth:Receive = {
        case user: String => 
            if (user == "Bob"){
                context.become(isAuth)
            }
    }
}

object login extends App {
    val actorSystem = ActorSystem("loginSys")
    val actor = actorSystem.actorOf(Props[loginActor])

    actor!"Bob"
    actor!"Username"
    actor!"Logout"
}