package models.DAO

import scala.concurrent.Future

case class Portfolio(protfolioID: String, stockCode: String, quantity: Double)

trait PortfolioTable {

  import DbConfiguration.config.profile.api._

    class Portfolios(tag: Tag) extends Table[Portfolio](tag, "Portfolio") {
      def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
      def portfolioID = column[String]("PortfolioID")
      def stockCode = column[String]("stockCode")
      def quantity = column[Double]("quantity")
      def * = (portfolioID, stockCode, quantity) <> (Portfolio.tupled, Portfolio.unapply)
    }

    val portfolios = TableQuery[Portfolios]

    /* Function to check if the portfolioID persists in the database or not*/
    def filter_portfolioID(portfolioID: String): Future[Seq[Portfolio]] = {
      DbConfiguration.config.db.run(portfolios.filter(_.portfolioID === portfolioID).result)
    }

    /*Function to insert Portfolio record in Portfolio table*/
    def insert_row(row: Portfolio): Future[Int] = {
      DbConfiguration.config.db.run(portfolios += row)
    }

    /*Function to receive all rows with specific PortfolioID and stock code in Portfolio table*/
    def retriveByID(portfolioID: String, stockCode: String): Future[Seq[Portfolio]] = {
      DbConfiguration.config.db.run(portfolios.filter(x =>(x.portfolioID === portfolioID &&x.stockCode === stockCode)).result)
    }
}

