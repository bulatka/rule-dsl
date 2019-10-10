package org.bulatnig.ruler.dsl

class Rule {
  def run()(implicit s: String): Unit = {
    println(s)
  }
}
