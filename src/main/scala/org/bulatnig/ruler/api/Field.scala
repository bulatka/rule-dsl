package org.bulatnig.ruler.api

import org.bulatnig.ruler.api.Category.Category

case class Field(name: String, category: Category, pattern: String, example: String) {

}

object Category extends Enumeration {
  type Category = Value
  val int, string, date = Value
}
