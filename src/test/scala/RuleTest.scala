import org.scalatest.FunSuite

import scala.reflect.runtime.currentMirror
import scala.reflect.runtime.universe._
import scala.tools.reflect.ToolBox


class RuleTest extends FunSuite {
  private val toolbox = currentMirror.mkToolBox()

  test("compile and run at runtime") {
    val tree = toolbox.parse("println(\"compiled and run at runtime!\")")
    val compiled = toolbox.compile(tree)
    compiled()
  }

  test("wrap and compile a function") {
    val function = "def apply(x: Int): Int = x + 2"
    val functionWrapper = "object FunctionWrapper { " + function + "}"
    val functionSymbol = toolbox.define(toolbox.parse(functionWrapper).asInstanceOf[toolbox.u.ImplDef])
    val func = toolbox.eval(q"$functionSymbol.apply _").asInstanceOf[Int => Int]
    val result = func(1)
    println(result)
  }

  test("method missing") {
    val tree = Apply(Ident(TermName("println")), List(Literal(Constant(2))))
    toolbox.eval(tree)
  }
}
