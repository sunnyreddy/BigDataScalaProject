package DAO
package models
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import slick.driver.MySQLDriver.api._

object MysqlTests extends App {

  val usersRepository = new UserProfileRepository_MySQL

  println("Inserting new user: Test Example")
  val eventualInsertResult = usersRepository.insert(UserProfile(1, "Test", "Example"))
  val insertResult = Await.result(eventualInsertResult, Duration.Inf)
  println(s"Insert result: $insertResult\n")

  println("Reading user by ID: 1")
  val eventualMaybeUserProfile = usersRepository.get(1)
  val maybeUserProfile = Await.result(eventualMaybeUserProfile, Duration.Inf)
  println(s"User profile by ID 1: $maybeUserProfile\n")

  println("Updating user by ID: 1")
  val eventualUpdateResult = usersRepository.update(1, "Test1")
  val updateResult = Await.result(eventualUpdateResult, Duration.Inf)
  println(s"Update result: $updateResult\n")

  println("Deleting user by ID: 1")
  val eventualDeleteResult = usersRepository.delete(1)
  val deleteResult = Await.result(eventualDeleteResult, Duration.Inf)
  println(s"Delete result: $deleteResult")

}

case class UserProfile(id: Int,
                       firstName: String,
                       lastName: String)

class UserProfileRepository_MySQL {
  lazy val config = DatabaseConfig.forConfig[JdbcProfile]("MySQL")
  val db: JdbcProfile#Backend#Database = config.db
  val userProfileQuery: TableQuery[UserProfiles] = TableQuery[UserProfiles]

  def insert(user: UserProfile): Future[Int] =
    db.run(userProfileQuery += user)

  def get(id: Int): Future[Option[UserProfile]] =
    db.run(
      userProfileQuery
        .filter(_.id === id)
        .take(1)
        .result
        .headOption)

  def update(id: Int, firstName: String): Future[Int] =
    db.run(
      userProfileQuery
        .filter(_.id === id)
        .map(_.firstName)
        .update(firstName))

  def delete(id: Int): Future[Int] =
    db.run(userProfileQuery.filter(_.id === id).delete)
}

private[models] class UserProfiles(tag: Tag) extends Table[UserProfile](tag, "user_profiles") {

  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def firstName: Rep[String] = column[String]("first_name")

  def lastName: Rep[String] = column[String]("last_name")

  def * : ProvenShape[UserProfile] = (id, firstName, lastName) <>(UserProfile.tupled, UserProfile.unapply) // scalastyle:ignore

}