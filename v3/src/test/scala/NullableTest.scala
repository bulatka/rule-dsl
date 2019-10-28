import org.bulatnig.v3.dsl.RuleEvaluator
import org.bulatnig.v3.model.{Field, Format, Transaction}
import org.scalatest.FunSuite

import scala.tools.reflect.ToolBoxError

class NullableTest extends FunSuite {

  test("check nullable exists") {
    val format = Format(List(Field("amount1", Field.Type.int, nullable = true)))

    val txModel = new Transaction
    txModel.data("amount1") = 1

    assert(new RuleEvaluator(format, "tx.amount1_exists").evaluate(txModel))
  }

  test("check nullable doesn't exist") {
    val format = Format(List(
      Field("amount1", Field.Type.int, nullable = true),
    ))

    assert(!new RuleEvaluator(format, "tx.amount1_exists").evaluate(new Transaction))
  }

  test("check nullable field evaluation") {
    val format = Format(List(
      Field("amount1", Field.Type.int, nullable = false),
      Field("amount2", Field.Type.int, nullable = true),
    ))

    val txModel = new Transaction
    txModel.data("amount1") = 1
    txModel.data("amount2") = 2

    assert(new RuleEvaluator(format, "tx.amount1 < tx.amount2(0)").evaluate(txModel))
  }

  test("check nullable field default") {
    val format = Format(List(
      Field("amount1", Field.Type.int, nullable = false),
      Field("amount2", Field.Type.int, nullable = true),
    ))

    val txModel = new Transaction
    txModel.data("amount1") = 1

    assert(new RuleEvaluator(format, "tx.amount1 > tx.amount2(0)").evaluate(txModel))
  }

  test("can't use nullable field without default") {
    val format = Format(List(Field("amount1", Field.Type.int, nullable = true)))

    val txModel = new Transaction
    txModel.data("amount1") = 1

    val caught = intercept[ToolBoxError] {
      new RuleEvaluator(format, "tx.amount1 > 0")
    }
    assert(caught.getMessage.contains("missing argument list for method amount1 in class DslTransactionImpl"))
  }

}
