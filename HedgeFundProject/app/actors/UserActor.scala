package actors
import models.login.LoginHandler
import akka.actor.{ActorSystem, Props}
import akka.actor.Actor
import akka.io.Tcp.Register
import model.DBService


object UserActor {
  def props = Props[UserActor]


}
class UserActor extends Actor {
  import UserActor._
  override def receive: Receive = {
    case UserRegisterMessage(username, password, name , email , portfolioID) =>
      sender() ! "sss"
  }

}