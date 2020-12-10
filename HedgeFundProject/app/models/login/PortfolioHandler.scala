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

  def deletePortfolio(portfolioID: String, stockCode: String, quantity: Double, rule: Double): Future[Boolean] = {
    filter_portfolioID(portfolioID).map(s => s.length == 1).map(b => b match {
      case false => false
      case true => {
        val portfolio = Portfolio(portfolioID, stockCode, quantity, rule)
        delete_row(portfolio)
        true
      }
    })
  }

  def getRule(portfolioID: String): Future[Any] = {
    filter_portfolioID(portfolioID).map(s => s.headOption match {
      case Some(p) => p.rule
      case None => "Not Exist"
    })
  }

  def getPortfolio(portfolioID: String): Future[Portfolio] = {
    filter_portfolioID(portfolioID).map(s => s.headOption match {
      case Some(p) => p
      case None => Portfolio("","",0,0)
    })
  }
}
