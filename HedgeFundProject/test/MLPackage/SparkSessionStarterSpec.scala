package MLPackage
import MLPackage.SparkSessionStarter.spark
import MLPackage.importCsv.getStockDataframe
import org.apache.spark.sql.SparkSession
import org.scalatest.{FlatSpec, Matchers}

class SparkSessionStarterSpec extends FlatSpec with Matchers {
  behavior of "Spark"
  //set property for winutil
  System.setProperty("hadoop.home.dir", "c:\\winutil\\")
  //start spark session
  val spark = SparkSession.builder
    .master("local[*]")
    .appName("Hedge")
    .getOrCreate()

  it should "spark name" in {
    spark.version shouldBe "2.4.0"
  }


}
