package com.munteanu.demo.protocol

import java.util.Date
import org.joda.time.format.DateTimeFormat
import org.joda.time.{format, DateTime}
import spray.json._

import com.munteanu.demo.utils.Utils

/**
 * Created by romunteanu on 8/24/2015.
 */
object WorkingDayProtocol extends DefaultJsonProtocol {
  import com.munteanu.demo.domain.WorkingDay

  implicit val DateFormat = new RootJsonFormat[Date] {
    lazy val dateFormat = new java.text.SimpleDateFormat(Utils.mysqlDateTimeFormat)
    def read(json: JsValue): Date = dateFormat.parse(json.convertTo[String])
    def write(date: Date) = JsString(dateFormat.format(date))
  }

  implicit val dtFormat = new RootJsonFormat[DateTime] {
    lazy val formatter: format.DateTimeFormatter = DateTimeFormat.forPattern(Utils.mysqlDateTimeFormat)
    def read(json: JsValue): DateTime = formatter.parseDateTime(json.convertTo[String])
    def write(dateTime: DateTime) = JsString(dateTime.toString(Utils.mysqlDateTimeFormat))
  }

  implicit val jsonFormatWorkingDay = jsonFormat7(WorkingDay.apply)
}
