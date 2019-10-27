import java.util.regex.Pattern

import org.bulatnig.v2.dsl.RuleEvaluator
import org.bulatnig.v2.model.{Field, Format, Transaction}
import org.scalatest.FunSuite
import org.scalatest.Matchers._

import scala.tools.reflect.ToolBoxError

class RuleEvaluatorTest extends FunSuite {

  test("evaluate valid rule") {
    val format = Format(List(
      Field("amount1", Field.Type.int),
      Field("amount2", Field.Type.int)
    ))
    val rule = "tx.amount1 > tx.amount2"
    val evaluator = new RuleEvaluator(format, rule)

    val txModel = new Transaction
    txModel.data("amount1") = 2
    txModel.data("amount2") = 1

    assert(evaluator.evaluate(txModel))
  }

  test("fail to compile rule using non-existing transaction field") {
    val format = Format(List(Field("amount1", Field.Type.int)))
    val rule = "tx.amount1 > tx.amount2"
    val caught = intercept[ToolBoxError] {
      new RuleEvaluator(format, rule)
    }
    caught.getMessage should include regex "value amount2 is not a member of .*DslTransactionImpl"
  }

  test("fail to compile invalid type operations") {
    val format = Format(List(
      Field("amount1", Field.Type.int),
      Field("amount2", Field.Type.string)
    ))
    val rule = "(tx.amount1 / tx.amount2) > 0"
    val caught = intercept[ToolBoxError] {
      new RuleEvaluator(format, rule)
    }
    assert(Pattern.compile(".*method value / .* cannot be applied to \\(String\\)", Pattern.DOTALL).matcher(caught.getMessage).find())
  }

}
