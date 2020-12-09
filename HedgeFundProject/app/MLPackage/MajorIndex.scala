package MLPackage

import MLPackage.Arima.pridictPice
import MLPackage.SparkSessionStarter.spark
import MLPackage.importCsv.{ getStockDataframe}


object MajorIndexETF {
  import spark.implicits._
  def getSP500ETF() = {
    val SP500ETF: String = "SPY"
    recommendPercentDirect(SP500ETF)
  }
  def NasdaqTech()= {
    val NasdaqTech: String = "QTEC"
    recommendPercentDirect(NasdaqTech)
  }
  def getRule(stockID:String) ={
    if((getSP500ETF> 2)  && (recommendPercentDirect(stockID)> 0)) {
      getSP500ETF * 5 / recommendPercentDirect(stockID)
    }
    else "Not Recommend"
  }

  def countIncrease(inputs:List[Double],price: Double):Boolean ={
    var a = 0
    for (i <- 0 until inputs.length - 1) {
      if(inputs.lift(i).get > price) {
        a += 1
      } else {a -= 1}
    }
    if (a > 0) {
      true
    }
    false
  }
  def highest(inputs:List[Double],price: Double):Double = {
    var globalHigh = price
    var i  = 0
    while (i < inputs.length) {
      if(inputs.lift(i).get > globalHigh) {
        globalHigh = inputs.lift(i).get
      }
      i += 1
    }
    globalHigh
  }
  def recommendPercent(highPrice:Double, price: Double): Double ={
    100* (highPrice - price) /price
  }

  def recommendPercentDirect(stockID:String): Double ={
    val df = getStockDataframe(spark,stockID)
    val predictList = pridictPice(df)
    val price:Double = df.select("adjusted_close").head().get(0).asInstanceOf[Double]
    val highestPrice = highest(predictList, price)
    recommendPercent(highestPrice, price)
  }
}
