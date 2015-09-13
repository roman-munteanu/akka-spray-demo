package com.munteanu.demo.domain

/**
 * Created by munteanu on 13.09.15.
 */
trait MyEntity[PK] {
  def id: Option[PK]
}
