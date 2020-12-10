package models.DAO

import scala.concurrent.Future

case class Portfolio(portfolioID: String, stockCode: String, quantity: Double, rule: Double)

trait PortfolioTable {

  import DbConfiguration.config.profile.api._

    class Portfolios(tag: Tag) extends Table[Portfolio](tag, "portfolio") {
      //def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
      def portfolioID = column[String]("PortfolioID", O.PrimaryKey)
      def stockCode = column[String]("stockCode")
      def quantity = column[Double]("quantity")
      def rule = column[Double]("ruleLimit")

      def * = (portfolioID, stockCode, quantity, rule) <> (Portfolio.tupled, Portfolio.unapply)
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

    def delete_row(portfolio: Portfolio): Future[Int] = {
      val query = portfolios.filter(_.portfolioID === portfolio.portfolioID)
      val action = query.delete
      DbConfiguration.config.db.run(action)
    }

    /*Function to receive all rows with specific PortfolioID and stock code in Portfolio table*/
    def retriveByID(portfolioID: String, stockCode: String): Future[Seq[Portfolio]] = {
      DbConfiguration.config.db.run(portfolios.filter(x =>(x.portfolioID === portfolioID &&x.stockCode === stockCode)).result)
    }
}

