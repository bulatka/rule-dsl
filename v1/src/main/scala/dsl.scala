package org.bulatnig.v1.dsl

import org.bulatnig.v1.api.Transaction

abstract class TransactionAdapter(protected val tx: Transaction) {

}

trait TransactionAdapterFactory {
  def wrap(tx: Transaction): TransactionAdapter
}