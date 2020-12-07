package actors
import akka.actor.{ActorSystem, Props}
import akka.actor.Actor
import models.DAO.{User, UserTable}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


object UserActor extends UserTable{
  def props = Props[UserActor]
}
class UserActor extends Actor {
  import UserActor._
  override def receive: Receive = {
    case UserRegisterMessage(username, password, name , email) =>{
    }

  }
}