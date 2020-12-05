package models.DAO

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

object DbConfiguration {
  lazy val config = DatabaseConfig.forConfig[JdbcProfile]("MySQL")
  val db: JdbcProfile#Backend#Database = config.db
}
