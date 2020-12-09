package models.login

import models.DAO.{PortfolioTable, Portfolio}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PortfolioHandler {
  this: PortfolioTable =>

  def addPortfolio(portfolioID: String, stockCode: String, quantity: Double, rule: Double): Future[Boolean] = {
    filter_portfolioID(portfolioID).map(s => s.length == 1).map(b => b match {
      case true => false
      case false => {
        val portfolio = Portfolio(portfolioID, stockCode, quantity, rule)
        insert_row(portfolio)
        true
      }
    })
  }
}
