//package DAO
////import slick.jdbc.SQLServerProfile.api._
//import slick.basic.DatabaseConfig
//import slick.lifted.ProvenShape
//package models
//import scala.concurrent.Future
//import slick.driver.MySQLDriver.api._
//import slick.jdbc.JdbcProfile
//
//case class UserProfile(id: Int,
//                       firstName: String,
//                       lastName: String)
//
//class UserProfileRepository_MySQL {
//  lazy val config = DatabaseConfig.forConfig[JdbcProfile]("slick.dbs.default")
//  val db: JdbcProfile#Backend#Database = config.db
//  val userProfileQuery: TableQuery[UserProfiles] = TableQuery[UserProfiles]
//
//  def insert(user: UserProfile): Future[Int] =
//    db.run(userProfileQuery += user)
//
//  def get(id: Int): Future[Option[UserProfile]] =
//    db.run(
//      userProfileQuery
//        .filter(_.id === id)
//        .take(1)
//        .result
//        .headOption)
//
//  def update(id: Int, firstName: String): Future[Int] =
//    db.run(
//      userProfileQuery
//        .filter(_.id === id)
//        .map(_.firstName)
//        .update(firstName))
//
//  def delete(id: Int): Future[Int] =
//    db.run(userProfileQuery.filter(_.id === id).delete)
//}
//
//private[models] class UserProfiles(tag: Tag) extends Table[UserProfile](tag, "user_profiles") {
//
//  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
//
//  def firstName: Rep[String] = column[String]("first_name")
//
//  def lastName: Rep[String] = column[String]("last_name")
//
//  def * : ProvenShape[UserProfile] = (id, firstName, lastName) <>(UserProfile.tupled, UserProfile.unapply) // scalastyle:ignore
//
//}