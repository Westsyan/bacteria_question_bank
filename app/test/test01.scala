package test

import java.util.Arrays._
import java.util._


object test01 {


  def main(args: Array[String]): Unit = {



    val ar = Array("1","2","3","4","5","6","7","8","9","10")
    Collections.shuffle(asList(ar:_*))

    ar.foreach(println)

  }



}
