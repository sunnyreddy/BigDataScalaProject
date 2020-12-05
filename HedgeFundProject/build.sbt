val SparkVersion = "2.4.0"
lazy val root = (project in file(".")).enablePlugins(PlayScala)
  .settings(
    name := """Hedge Fund""",
    organization := "com.example",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.11.8",
    libraryDependencies++=Seq(
  //spark
      "org.apache.spark" %% "spark-core" % SparkVersion,
      "org.apache.spark" %% "spark-sql" % SparkVersion,
  //dl4j
      "org.deeplearning4j" % "deeplearning4j-core" % "1.0.0-beta7",
      "org.nd4j" % "nd4j-native-platform" % "1.0.0-beta7",
  //visualization
      "org.vegas-viz" %% "vegas-spark" % "0.3.11", // Vegas Viz Library
      "org.scalatest" %% "scalatest" % "3.0.5" % "test", // Scala test library
  //sparkmllib
      "org.apache.spark" %% "spark-mllib" % "2.4.0",
  //bigDL
      "com.intel.analytics.bigdl" % "bigdl-SPARK_2.4" % "0.11.0",
  //
     // "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
      "com.typesafe.play" %% "play-slick" % "4.0.2",
      "com.typesafe.play" %% "play-slick-evolutions" % "4.0.2",
      //slick
      "com.typesafe.slick" %% "slick" % "3.3.2",
      "org.slf4j" % "slf4j-nop" % "1.6.4",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
      "mysql" % "mysql-connector-java" % "8.0.15"
      )
    )

//libraryDependencies += guice

//libraryDependencies += jdbc

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
