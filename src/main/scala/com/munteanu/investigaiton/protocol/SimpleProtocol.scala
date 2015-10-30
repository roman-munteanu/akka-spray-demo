package com.munteanu.investigaiton.protocol

/**
 * Created by romunteanu on 9/2/2015.
 */
object SimpleProtocol {
  case class RequestMessage(msg: String)
  case class ResponseMessage(msg: String)
  case class Alpha(s: String)
  case class Beta(a: Alpha)
  case class Gamma(s: String)
}
