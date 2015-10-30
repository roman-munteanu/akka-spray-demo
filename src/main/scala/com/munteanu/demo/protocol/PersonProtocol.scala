package com.munteanu.demo.protocol

import java.util.Date

import com.munteanu.demo.utils.Utils
import spray.json.{JsString, JsValue, RootJsonFormat, DefaultJsonProtocol}

/**
 * Created by romunteanu on 10/28/2015.
 */
object PersonProtocol extends DefaultJsonProtocol {
  import com.munteanu.demo.domain.Person

  implicit val DateFormat = new RootJsonFormat[Date] {
    lazy val dateFormat = new java.text.SimpleDateFormat(Utils.mysqlDateTimeFormat)
    def read(json: JsValue): Date = dateFormat.parse(json.convertTo[String])
    def write(date: Date) = JsString(dateFormat.format(date))
  }

  implicit val jsonFormatPerson = jsonFormat6(Person.apply)
}
