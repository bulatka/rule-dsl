package org.bulatnig.v2.model

import java.time.Instant

import scala.collection.mutable

case class Field(name: String, `type`: Field.Type.Type)

object Field {

  object Type extends Enumeration {
    type Type = Value
    val int = Value("Int")
    val string = Value("String")
  }

}

case class Format(fields: List[Field])

class Transaction() {

  var id = 0
  var dateCreated = Instant.now()
  var data = mutable.Map[String, Any]()

}
