package MLPackage

import MLPackage.SparkSessionStarter.spark
import MLPackage.importCsv.{getStockDataframe}
import org.apache.log4j.{Level, Logger}
import org.scalatest.{FlatSpec, Matchers}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

class importCsvSpec extends  FlatSpec with Matchers {

  Logger.getLogger("org").setLevel(Level.OFF)
  behavior of "API_Test"

  it should "return correct data" in {
    getStockDataframe(spark,"IBM").select("Name").head().get(0).asInstanceOf[String] shouldBe "IBM"
  }
}
//object testSpark extends App{
//  System.setProperty("hadoop.home.dir", "c:\\winutil\\")
//  val a = getStockDataframe(spark,"IBM").select("Name").head().get(0).asInstanceOf[String]
//  println(a == "IBM")
//}
