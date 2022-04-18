name := "twitterProject"

version := "0.1"

scalaVersion := "2.13.7"

lazy val twitter1 = (project in file("."))
  .settings(
    name := "twitter1",
    libraryDependencies += "com.eed3si9n" %% "gigahorse-okhttp" % "0.5.0",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.7" % Test,
    libraryDependencies += "org.apache.derby" % "derby" % "10.4.1.3",
    libraryDependencies += "org.apache.spark" %% "spark-core" % "3.2.0",
    libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.2.0",
    libraryDependencies += "org.apache.spark" %% "spark-streaming" % "3.2.0",
    libraryDependencies += "org.twitter4j" % "twitter4j-core" % "4.0.7",
    libraryDependencies += "org.twitter4j" % "twitter4j-stream" % "4.0.7",
    libraryDependencies += "org.apache.kafka" %% "kafka" % "3.0.0",

    libraryDependencies += "com.google.code.gson" % "gson" % "2.8.9",

    libraryDependencies += "org.apache.hadoop" % "hadoop-common" % "3.3.1",
    libraryDependencies += "org.apache.hadoop" % "hadoop-client" % "3.3.1",
    libraryDependencies += "org.apache.hadoop" % "hadoop-aws" % "3.3.1",
      libraryDependencies += "org.postgresql" % "postgresql" % "42.2.24"



  )