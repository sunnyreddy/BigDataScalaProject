package models.login
import models.DAO.{User, UserTable}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class LoginHandler {
  this: UserTable =>

  def addUser(username: String, password: String, name: String, email: String, portfolioID: String, availableFund:BigDecimal): Future[Boolean] = {
    filter_username(username).map(s => s.length == 1).map(b => b match {
      case true => false
      case false => {
        val user = User(username, password, name, email, username+"1", 10000)
        insert_user(user)
        true
      }
    })
  }

  def validateUser(username: String, password: String): Future[Boolean] = {
    filter_username(username).map(s => s.headOption match {
      case Some(u) => u.password == password
      case None => false
    })
    }

  def getUserEmail(username:String): Future[String] = {
    // to do
    filter_username(username).map(s => s.headOption match {
      case Some(u) => u.email
      case None => "medasaikanth@gmail.com"
    })
  }

  def getPortfolioId(username:String): Future[String] = {
    filter_username(username).map(s => s.headOption match {
      case Some(u) => u.portfolioID
      case None => "Not Exists"
    })
  }
  def getAvailableFund(username:String): Future[Any] = {
    filter_username(username).map(s => s.headOption match {
      case Some(u) => u.availableFund
      case None => "Not Exists"
    })
  }

}
