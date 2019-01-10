package utils

import java.io.File
import java.text.SimpleDateFormat
import java.util.Arrays.asList
import java.util.{Collections, Date}

import org.apache.commons.io.FileUtils
import org.joda.time.DateTime
import play.api.mvc.{AnyContent, Request}
import sun.misc.BASE64Encoder

/**
  * Created by yz on 2017/6/16.
  */
object Utils {

  def random(num:Int,length:Int) : Seq[Int]= {
    val ran = (0 to num).map(x=>(Math.random()*length).toInt)
    ran
  }


  def shuffle[T](array : Array[T]) : Array[T] = {
    Collections.shuffle(asList(array:_*))
    array
  }


  def dealWithTable(table:String) :String = {
    val tr = table.split("\n")
    val head ="<thead><tr><th>" + tr.head.split("\t").mkString("</th><th>") + "</th></tr></thead>"

    val body =  "<tbody><tr><td>" +
      tr.drop(1).map(_.split("\t").mkString("</td><td>")).mkString("</td></tr><tr><td>") +"</td></tr></tbody>"

    head + body
  }

  def getTime(startTime: Long) = {
    val endTime = System.currentTimeMillis()
    (endTime - startTime) / 1000.0
  }

  def deleteDirectory(direcotry: File) = {
    try {
      FileUtils.deleteDirectory(direcotry)
    } catch {
      case _ =>
    }
  }

  def deleteDirectory(tmpDir: String):Unit = {
    val direcotry = new File(tmpDir)
    deleteDirectory(direcotry)
  }

  def isDouble(value: String): Boolean = {
    try {
      value.toDouble
    } catch {
      case _: Exception =>
        return false
    }
    true
  }

  def refer(request:Request[AnyContent]):String = {
    val header = request.headers.toMap
    header.filter(_._1 == "Referer").map(_._2).head.head
  }

  def date : DateTime = {
    val now = new Date()
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val time = dateFormat.format(now)
    val date = new DateTime(dateFormat.parse(time).getTime)
    return date
  }

  def date2 : String = {
    val now = new Date()
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = dateFormat.format(now)
    date
  }

  def getImage(path:String,code:Int) : String = {
    val encoder = new BASE64Encoder
    val img = encoder.encode(FileUtils.readFileToByteArray(new File(path)))
    val photo = code match{
      case 1 => s"""<img src="data:image/png;base64,${img}"/>"""
      case 2 =>s"data:image/png;base64,${img}"
    }
    photo
  }


  val windowsPath = "F:\\database\\fern"
  val linuxPath = "/root/projects/fern"
  val path = {
    if (new File(windowsPath).exists()) windowsPath else linuxPath
  }

  val tmpPath = {
    if (new File(windowsPath).exists()) windowsPath + "/tmp" else linuxPath + "/tmp"
  }

}
