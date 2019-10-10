package org.bulatnig.ruler.api

import java.time.Instant

class Transaction {

  var id = 0
  var dateCreated = Instant.now()
  val data = Map()

}
