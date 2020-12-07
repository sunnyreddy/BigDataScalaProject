package actors

import akka.actor.{Actor, ActorSystem, Props}


/**
 * This is the supervisor actor that will spawn the worker actors at each client connection.
 * Anytime that the worker actors send their updates, this guy communicates the change to the
 * messenger actor.
 */
class BossActor extends Actor {


  override def receive: Receive = {

    // A new client has connected. Let's spawn a new WorkerActor and tell him to start working.
    case LoginSuccessMessage(_) =>
      // creates a new WorkerActor and tell him to start doing some work
      val newUserActor = ActorSystem().actorOf(Props[UserActor])
      sender ! "Start working"

      // send a reply to the responding actor and update the messenger actor
//      sender ! ClientConnectionSuccessful(clientIdCounter, activeClients)
//      Application.messengerActor ! ActiveClientsChanged(activeClients)

  }
}
