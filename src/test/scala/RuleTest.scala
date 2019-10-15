import org.bulatnig.ruler.api.Transaction
import org.bulatnig.ruler.dsl.{TransactionAdapter, TransactionAdapterFactory}
import org.scalatest.FunSuite

import scala.reflect.runtime.currentMirror
import scala.reflect.runtime.universe._
import scala.tools.reflect.ToolBox


class RuleTest extends FunSuite {
  private val toolbox = currentMirror.mkToolBox()

  test("compile and run code at runtime") {
    val tree = toolbox.parse("println(\"compiled and run at runtime!\")")
    val compiled = toolbox.compile(tree)
    compiled()
  }

  test("wrap and compile a callable function") {
    val function = "def apply(x: Int): Int = x + 2"
    val functionWrapper = "object FunctionWrapper { " + function + "}"
    val functionSymbol = toolbox.define(toolbox.parse(functionWrapper).asInstanceOf[toolbox.u.ImplDef])
    val func = toolbox.eval(q"$functionSymbol.apply _").asInstanceOf[Int => Int]
    val result = func(1)
    println(result)
  }

  test("decifer class definition") {
    val tx = new Transaction
    tx.data("ref_text") = "cup"
    tx.data("amount") = 55

//    def apply(tx: Transaction) = new TransactionAdapter(tx) {
//      val amount1: Int = tx.data("amount").asInstanceOf[Int]
//
//      def amount2: Boolean = tx.data.contains("amount2")
//
//      def amount2(default: Int): Int = tx.data.getOrElse("amount2", default).asInstanceOf[Int]
//    }

    val function = """def apply(tx: org.bulatnig.ruler.api.Transaction) = new org.bulatnig.ruler.dsl.TransactionAdapter(tx) {
                     |  val ref_text: String = tx.data("ref_text").asInstanceOf[String]
                     |  val amount: Int = tx.data("amount").asInstanceOf[Int]
                     |}""".stripMargin
    val functionWrapper = "object FunctionWrapper { " + function + "}"
    val tree = toolbox.parse(functionWrapper).asInstanceOf[toolbox.u.ImplDef]
    val functionSymbol = toolbox.define(tree)
    val func = toolbox.eval(q"$functionSymbol.apply _").asInstanceOf[Transaction => TransactionAdapter]
    val result = func(tx)
    print(showRaw(tree))
  }

  test("runtime class definition") {
    val tx = new Transaction
    tx.data("ref_text") = "cup"
    tx.data("amount") = 55

    val properties = List(
      ValDef(Modifiers(), TermName("ref_text"), Select(Ident(TermName("Predef")), TypeName("String")),
        TypeApply(Select(Apply(Select(Select(Select(This(TypeName("$anon")), TermName("tx")), TermName("data")),
          TermName("apply")), List(Literal(Constant("ref_text")))), TermName("asInstanceOf")),
          List(Select(Ident(TermName("Predef")), TypeName("String"))))),
      ValDef(Modifiers(), TermName("amount"), Select(Ident(TermName("scala")), TypeName("Int")),
        TypeApply(Select(Apply(Select(Select(Select(This(TypeName("$anon")), TermName("tx")), TermName("data")),
          TermName("apply")), List(Literal(Constant("amount")))), TermName("asInstanceOf")),
          List(Select(Ident(TermName("scala")), TypeName("Int")))))
    )

    val functionWrapper = q"object FunctionWrapper { def apply(tx: org.bulatnig.ruler.api.Transaction) = new org.bulatnig.ruler.dsl.TransactionAdapter(tx) { ..$properties } }"
    val functionSymbol = toolbox.define(functionWrapper.asInstanceOf[toolbox.u.ImplDef])
    val func = toolbox.eval(q"$functionSymbol.apply _").asInstanceOf[Transaction => TransactionAdapter]
    val result = func(tx)
    println(result)
  }

  test("quasiquotes") {
    val txModel = new Transaction
    txModel.data("amount1") = 1
    txModel.data("amount2") = 2

    val properties = List(
      ValDef(Modifiers(), TermName("amount1"), Select(Ident(TermName("scala")), TypeName("Int")),
        TypeApply(Select(Apply(Select(Ident(TermName("tx")), TermName("data")), List(Literal(Constant("amount1")))), TermName("asInstanceOf")),
          List(Select(Ident(TermName("scala")), TypeName("Int"))))),
      q"val ${TermName("amount2")}: Int = tx.data(${Constant("amount2")}).asInstanceOf[Int]"
    )

    val adapterClass =
      q"""class TransactionAdapterImpl(tx: org.bulatnig.ruler.api.Transaction)
         extends org.bulatnig.ruler.dsl.TransactionAdapter(tx) { ..$properties }"""
    val adapterSymbol = toolbox.define(adapterClass.asInstanceOf[toolbox.u.ImplDef])
    val txAdapterFactory = toolbox.eval(
      q"""new org.bulatnig.ruler.dsl.TransactionAdapterFactory {
            def wrap(tx: org.bulatnig.ruler.api.Transaction): org.bulatnig.ruler.dsl.TransactionAdapter =
                return new $adapterSymbol(tx)
          }""").asInstanceOf[TransactionAdapterFactory]

    val rule = toolbox.parse("tx.amount1 + tx.amount2")
    val ruleEvaluator = toolbox.eval(q"(tx: $adapterSymbol) => $rule").asInstanceOf[TransactionAdapter => Int]

    val tx = txAdapterFactory.wrap(txModel)
    val result = ruleEvaluator(tx)
    assert(result == 3)
  }
}
