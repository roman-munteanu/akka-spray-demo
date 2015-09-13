package com.munteanu.demo.domain

/**
 * Created by munteanu on 13.09.15.
 */
trait IdentifiableTable[I] {
  def id: slick.lifted.Rep[I]
}
