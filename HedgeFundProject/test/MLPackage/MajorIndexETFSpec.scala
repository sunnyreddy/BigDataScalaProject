package MLPackage
import MLPackage.Arima.pridictPice
import org.scalatest.{FlatSpec, Matchers}
import MLPackage.MajorIndexETF.{getRule, highest, recommendPercent, recommendPercentDirect}
import MLPackage.SparkSessionStarter.spark
import MLPackage.importCsv.getStockDataframe
import org.apache.log4j.{Level, Logger}

class MajorIndexETFSpec extends FlatSpec with Matchers {

  behavior of "Helpers"

  it should "get higest 3" in {
    highest(List(1, 2, 3), 1) shouldBe 3
  }
}

  // other function return value keeps changing with time, so won't have a specific range

  // to find the some stck is recommend
  //!!  maximum 2 per time,
  object testReules extends App {
    System.setProperty("hadoop.home.dir", "c:\\winutil\\")
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)
    val stocks = List("AAPL", "IBM")
    for (stock <- stocks) {
//      println(stock, ": ")
//      val df = getStockDataframe(spark, stock)
//      val predictList = pridictPice(df)
//      println(predictList)
//      val price: Double = df.select("adjusted_close").head().get(0).asInstanceOf[Double]
//      println(price)
//      val highestPrice = highest(predictList, price)
//      println(highestPrice)
//      val percent = recommendPercent(highestPrice, price)
//      println(percent)
      println(s"Rules for ${stock}")
      println(getRule(stock))
    }
    //  val df = getStockDataframe(spark,"AAPL")
    //  val predictList = pridictPice(df)
    //  println(predictList)
    //  val price:Double = df.select("adjusted_close").head().get(0).asInstanceOf[Double]
    //  println(price)
    //  val highestPrice = highest(predictList, price)
    //  println(highestPrice)
    //  val AdjustedHighestPrice = highestPrice + price/3
    //  println(AdjustedHighestPrice)
    //  val percent = recommendPercent(AdjustedHighestPrice, price)
    //  println(percent)
  }


