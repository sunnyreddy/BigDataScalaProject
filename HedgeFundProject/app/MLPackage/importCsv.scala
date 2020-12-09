package MLPackage

//import MLPackage.SparkSessionStarter.spark
import org.apache.spark.sql.functions.lit
import org.apache.spark.sql.types.{DateType, DoubleType, IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.sql._
object importCsv extends App{
  System.setProperty("hadoop.home.dir", "c:\\winutil\\")
  //  var stockID: String = "IBM"
  val API_KEY = "3V0DC5QKBMDYUPNX"

  val urlcsv = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=IBM&apikey=demo&datatype=csv"
  val demo = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&apikey=demo&datatype=csv"
  //training
  lazy val spark = SparkSession.builder().appName("Stock-prediction").master("local[*]").getOrCreate();
  import spark.implicits._
  def getStockDataframe(sparkSession: SparkSession, stockID: String): DataFrame = {
    val uri: String = s"https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=${stockID}&apikey=${API_KEY}&datatype=csv"
    val res = scala.io.Source.fromURL(uri).mkString.stripMargin.lines.toList
    val csvData: Dataset[String] = spark.sparkContext.parallelize(res).toDS()
    val Data = spark.read.option("header", true).option("inferSchema",true).csv(csvData)
    val DataWithName = Data.withColumn("Name", lit(s"${stockID}"))
    DataWithName
  }
//
//  getStockDataframe(spark,"IBM").show()
}