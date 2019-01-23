name := "bacteria_question_bank"
 
version := "1.0" 
      
lazy val `bacteria_question_bank` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
scalaVersion := "2.11.11"

libraryDependencies ++= Seq( jdbc , cache , ws , specs2 % Test ,filters )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

libraryDependencies += "commons-io" % "commons-io" % "2.5"

libraryDependencies += "com.typesafe.slick" % "slick-codegen_2.11" % "3.2.0"

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.43"

libraryDependencies += "com.typesafe.play" % "play-slick_2.11" % "2.1.0"

libraryDependencies += "com.github.tototoshi" % "slick-joda-mapper_2.11" % "2.3.0"