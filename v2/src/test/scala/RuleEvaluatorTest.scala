import org.bulatnig.v2.dsl.RuleEvaluator
import org.bulatnig.v2.model.{Field, Format, Transaction}
import org.scalatest.FunSuite

class RuleEvaluatorTest extends FunSuite {

  test("create transaction adapter according to format") {
    val format = Format(List(Field("amount1", Field.Type.int), Field("amount2", Field.Type.int)))
    val rule = "tx.amount1 > tx.amount2"
    val evaluator = new RuleEvaluator(format, rule)

    val txModel = new Transaction
    txModel.data("amount1") = 2
    txModel.data("amount2") = 1

    assert(evaluator.evaluate(txModel))
  }

}
