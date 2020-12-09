package MLPackage
import MLPackage.Arima.pridictPice
import MLPackage.SparkSessionStarter.spark
import MLPackage.importCsv.getStockDataframe
import org.scalatest.{FlatSpec, Matchers}

class ArimaSpec extends FlatSpec with Matchers{
  behavior of "Arima"

  it should "Arima predict results" in {
    val df =  getStockDataframe(spark,"IBM")
    pridictPice(df).length shouldBe 30
  }

}

