package MLPackage

import java.time.{ZoneId, ZonedDateTime}

import com.cloudera.sparkts.models.ARIMA._
import com.cloudera.sparkts.{DateTimeIndex, DayFrequency, TimeSeriesRDD}
import org.apache.spark.sql.{DataFrame, Dataset, Row}
import org.apache.spark.sql.functions.to_timestamp
import org.apache.spark.sql.types.DoubleType

//reference: http://www.technippet.com/2017/01/stock-price-prediction-time-series.html
object Arima {
  def pridictPice(Data:DataFrame): List[Double]= {
    val featureData = Data.select("timestamp", "Name", "adjusted_close")

    val formattedDF = featureData
      .withColumn("timestamp", to_timestamp(featureData("timestamp")))
      .withColumn("price", featureData("adjusted_close").cast(DoubleType))
      .drop( "adjusted_close").sort("timestamp")
    //formattedDF.registerTempTable("formattedDF")
    val minDate = formattedDF.selectExpr("min(timestamp)").collect()(0).getTimestamp(0)
    val maxDate = formattedDF.selectExpr("max(timestamp)").collect()(0).getTimestamp(0)

    val zone = ZoneId.systemDefault()
    val dtIndex = DateTimeIndex.uniformFromInterval(
      ZonedDateTime.of(minDate.toLocalDateTime, zone),
      ZonedDateTime.of(maxDate.toLocalDateTime, zone),
      new DayFrequency(1)
    )

    val tsRdd = TimeSeriesRDD.timeSeriesRDDFromObservations(dtIndex, formattedDF, "timestamp", "Name", "price")
    val resultDF = tsRdd.mapSeries { vector => {
      val noOfDays = 30
      val newVec = new org.apache.spark.mllib.linalg.DenseVector(vector.toArray.map(x => if (x.equals(Double.NaN)) 0 else x))
      //      val newVecSize = newVec.size
      val arimaModel = fitModel(1, 0, 0, newVec)
      val forecasted = arimaModel.forecast(newVec, noOfDays)
      //      val forecastedVecSize = forecasted.size
      new org.apache.spark.mllib.linalg.DenseVector(forecasted.toArray.slice(forecasted.size - (noOfDays + 1), forecasted.size - 1))
    }
    }
    val pridictedPriceList = resultDF.collectAsTimeSeries().data.values.toList
    pridictedPriceList
  }
}
