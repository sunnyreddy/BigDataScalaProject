package MLPackage

import MLPackage.Arima.pridictPice
import MLPackage.SparkSessionStarter.spark
import getData.importCsv.{API_KEY, getStockDataframe}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.sql.functions.lit

object MajorIndexETF {
  import spark.implicits._
  //  var stockID: String = "IBM"
  val API_KEY = "3V0DC5QKBMDYUPNX"
  def getSP500ETF() = {
    val SP500ETF: String = "SPY"
    val sp500 = getStockDataframe(spark,SP500ETF)
    pridictPice(sp500)
  }
  def NasdaqTech()= {
    val NasdaqTech: String = "QTEC"
    val Nasdaq = getStockDataframe(spark,NasdaqTech)
    pridictPice(Nasdaq)
  }

}
