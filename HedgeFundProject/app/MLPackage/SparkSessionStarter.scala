package MLPackage
import org.apache.spark.sql.{SparkSession}

object SparkSessionStarter extends App{
  //set property for winutil
  System.setProperty("hadoop.home.dir", "c:\\winutil\\")
  //start spark session
  val spark = SparkSession.builder
    .master("local[*]")
    .appName("Spark import")
    .getOrCreate()

}
