package org.bulatnig.ruler.api

import java.time.Instant

import scala.collection.mutable

class Transaction() {

  var id = 0
  var dateCreated = Instant.now()
  var data = mutable.Map[String, Any]()

}
