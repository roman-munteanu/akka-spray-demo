package com.munteanu.demo.protocol

import spray.json._

/**
 * Created by romunteanu on 8/17/2015.
 */
object MyTaskProtocol extends DefaultJsonProtocol {
  import com.munteanu.demo.domain.MyTask

  case class TaskNotFound(msg: String = "Task not found.")

  implicit val jsonFormatMyTask = jsonFormat3(MyTask.apply)
}