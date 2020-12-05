package getData
import org.apache.spark.sql.types.{DateType, DoubleType, IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object importCsv extends App{
  System.setProperty("hadoop.home.dir", "c:\\winutil\\")
  val spark = SparkSession.builder
    .master("local[*]")
    .appName("Spark import")
    .getOrCreate()
  import spark.implicits._
  var stockID: String = "IBM"
  val API_KEY = "3V0DC5QKBMDYUPNX"
  val uri: String = s"https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=${stockID}&apikey=${API_KEY}&datatype=csv"
  val url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=IBM&apikey=demo&datatype=csv"
  val demo = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&apikey=demo&datatype=csv"
  var res = scala.io.Source.fromURL(uri).mkString.stripMargin.lines.toList
  val csvData: Dataset[String] = spark.sparkContext.parallelize(res).toDS()
  val trainingData = spark.read.option("header", true).option("inferSchema",true).csv(csvData)
  trainingData.show()


}