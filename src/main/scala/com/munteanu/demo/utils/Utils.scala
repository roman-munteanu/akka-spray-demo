package com.munteanu.demo.utils

import java.text.{ParseException, SimpleDateFormat}

import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, format}

/**
 * Created by romunteanu on 8/24/2015.
 */
object Utils {

  val mysqlDateTimeFormat: String = "yyyy-MM-dd HH:mm:ss"
  val formatter: format.DateTimeFormatter = DateTimeFormat.forPattern(mysqlDateTimeFormat)
  val df = new SimpleDateFormat(mysqlDateTimeFormat)

  val dateTimePattern = """\d{4}-\d{2}-\d{2}\s\d{2}:\d{2}:\d{2}""".r
  val datePattern = """\d{4}-\d{2}-\d{2}""".r
  val timePattern = """\d{2}:\d{2}:\d{2}""".r

  def string2Date(value: String): java.util.Date = {
    try {
      value match {
        case dateTimePattern() => df.parse(value)
        case datePattern() => df.parse(value + " 00:00:00")
        case timePattern() => df.parse("1970-01-01 " + value)
        case _ => throw new IllegalArgumentException
      }
    } catch {
      case e: Exception => null
    }
  }

  def string2DateTime(value: String): DateTime = {
    val mValue = value match {
      case dateTimePattern() => value
      case datePattern() => value + " 00:00:00"
      case timePattern() => "1970-01-01 " + value
      case _ => throw new IllegalArgumentException
    }

    try {
      formatter.parseDateTime(mValue)
    } catch {
      case e: ParseException => null
    }
  }
}
