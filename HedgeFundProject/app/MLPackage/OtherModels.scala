package MLPackage

import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.feature.{Normalizer, VectorAssembler}
import org.apache.spark.ml.regression.{DecisionTreeRegressionModel, DecisionTreeRegressor, LinearRegression, LinearRegressionModel, RandomForestRegressionModel, RandomForestRegressor}
import org.apache.spark.sql.{DataFrame, SparkSession}

object OtherModels {
  //for 5 minute interval data
  def linearRegression(input: DataFrame): (LinearRegressionModel, Double) = {
    val featureData = input.select("hign","low","adjusted_close","volumn")
    val assembler1 = new VectorAssembler().
      setInputCols(Array( "hign","low", "adjusted_close", "volumn")).
      setOutputCol("features").transform(featureData)

    val normalizer = new Normalizer()
      .setInputCol("features")
      .setOutputCol("normFeatures")
      .setP(2.0)
      .transform(assembler1)
    val lr = new LinearRegression()
      .setLabelCol("adjusted_close")
      .setFeaturesCol("normFeatures")
      .setMaxIter(10)
      .setRegParam(1.0)
      .setElasticNetParam(1.0)

    val Array(trainingData,testData) = normalizer.randomSplit(Array(0.7,0.3),20)
    val lrModel = lr.fit(trainingData)
    val trainingSummary = lrModel.summary

    val Lr_rootMeanSquaredError = trainingSummary.rootMeanSquaredError
    (lrModel,Lr_rootMeanSquaredError)
  }

  def RandomForestRegression(input: DataFrame): (RandomForestRegressionModel, Double) = {
    val assembler1 = new VectorAssembler().
      setInputCols(Array( "high","low", "close", "volume")).
      setOutputCol("features").transform(input)

    val normalizer = new Normalizer()
      .setInputCol("features")
      .setOutputCol("normFeatures")
      .setP(2.0)
      .transform(assembler1)
    val RandomForestRegressor = new RandomForestRegressor()
      .setLabelCol("adjusted_close")
      .setFeaturesCol("normFeatures")

    val Array(trainingData,testData) = normalizer.randomSplit(Array(0.7,0.3),20)

    val RandomForestRegressorModel = RandomForestRegressor.fit(trainingData)
    //lrModel.transform(testData).select("features","normFeatures", "adjusted_close", "prediction").show()

    val predictData = RandomForestRegressorModel.transform(testData)
    val evaluator = new RegressionEvaluator()
      .setLabelCol("adjusted_close")
      .setPredictionCol("prediction")
      .setMetricName("rmse")
    val rf_rootMeanSquaredError = evaluator.evaluate(predictData)
    (RandomForestRegressorModel, rf_rootMeanSquaredError)
  }

}
