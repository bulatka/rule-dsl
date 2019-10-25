package org.bulatnig.ruler.dsl

import org.bulatnig.ruler.api.Transaction

abstract class TransactionAdapter(protected val tx: Transaction) {

}

trait TransactionAdapterFactory {
  def wrap(tx: Transaction): TransactionAdapter
}

class Rule {
  def run()(implicit s: String): Unit = {
    println(s)
  }
}