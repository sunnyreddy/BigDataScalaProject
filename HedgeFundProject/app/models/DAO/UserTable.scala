package models.DAO

import scala.concurrent.Future
import scala.collection.mutable

case class User(username: String, password: String, name: String, email: String, portfolioID: String, availableFund: BigDecimal)



trait UserTable{
//  this: DbConfiguration =>
  import DbConfiguration.config.profile.api._

  class Users(tag: Tag) extends Table[User](tag, "USERS") {
    def username = column[String]("USERNAME", O.Length(24), O.PrimaryKey)
    def password = column[String]("PASSWORD", O.Length(24))
    def name = column[String]("NAME")
    def email = column[String]("EMAIL", O.Length(50))
    def portfolioID = column[String]("portfolioID")
    def availableFund = column[BigDecimal]("availableFund")
    def * = (username, password, name, email, portfolioID, availableFund) <> (User.tupled, User.unapply)
  }

  var allUsers = TableQuery[Users]

  /* Function to check if the username persists in the database or not*/
  def filter_username(username: String): Future[Seq[User]] = {
    DbConfiguration.config.db.run(allUsers.filter(_.username === username).result)
  }

  /* Function to check if the username persists in the database or not*/
  def checkExist(username: String): Future[Boolean] = {
    DbConfiguration.config.db.run(allUsers.filter(_.username === username).exists.result)
  }

  /*Function to insert record in User table*/
  def insert_user(user: User): Future[Int] = {
    DbConfiguration.config.db.run(allUsers += user)
  }

  /*Function to list all users*/
  def show(): Future[Seq[User]] = DbConfiguration.config.db.run {
    allUsers.result
  }
}

