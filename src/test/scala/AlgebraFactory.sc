import org.uacalc.alg.BasicAlgebra

import scala.collection.JavaConverters._
import basic_algebra.UACalcAlgebraFactory._
import org.uacalc.io.AlgebraIO
//import scala.collection.JavaConverters.seqAsJavaList
//import org.uacalc.io.AlgebraIO
//import org.uacalc.alg.Malcev.isCongruenceDistIdempotent
//import org.uacalc.alg.Malcev.isCongruenceModularIdempotent
//import org.uacalc.alg.Malcev.cubeTermBlockerIdempotent
//import org.uacalc.ui.tm.ProgressReport

object AlgebraFactory {
  /* Examples/Tests of the UACalcAlgebraFactory class/object. */
  println("EXAMPLE 1: a unary operation defined from a (Scala) function.")

  println("---- (1) Define a function, e.g., by specifying what it does at each point. ----")
  def plus_mod5(args: List[Int]): Int = {
    def plus_mod5_aux(xs: List[Int], acc: Int): Int = xs match {
      case Nil => acc % 5
      case y :: ys => plus_mod5_aux(ys, acc + y)
    }
    plus_mod5_aux(args, 0)
  }

  println("---- (2) Turn the function from (1) into various UACalcOperations. ----")
  val op1: UACalcOperation =
    makeUACalcOperationFromScalaFunction(plus_mod5, "binaryPlusMod5", 2, 5)
  val op2: UACalcOperation =
    makeUACalcOperationFromScalaFunction(plus_mod5, "ternaryPlusMod5", 3, 5)

  println("---- (3) Check that op1([4,10]) and op2([4,10,1]) give expected results ----")
  val ans1: Int = op1.intValueAt(Array(4,10))
  println("4 + 10 mod 5 = 4 =?= " + ans1.toString)
  val ans2: Int = op2.intValueAt(Array(4, 10, 1))
  println("4 + 10 + 1 mod 5 = 0 =?= " + ans2.toString)

  println("---- (4) Make a JavaList of the operations you want as the basic operations of your algebra. ----")
  val my_ops = seqAsJavaList(List(op1, op2))

  println("---- (5) Construct the algebra. ----")
  val myAlg: BasicAlgebra = new BasicAlgebra("My Alg", 5, my_ops)

  println("---- (6)(optional) Sanity check: we actually constructed something. ----")
  println("myAlg.getName() = " + myAlg.getName())
  println("myAlg.universe() = " + myAlg.universe())
  println("myAlg.operations() = " + myAlg.makeOperationTables())

  println("---- (7)(optional) write the algebra to a UACalc file ----")
  AlgebraIO.writeAlgebraFile(myAlg, "Example1_UACalcAlgebraFromScalaFunctions.ua")


  // EXAMPLE 2: a binary operation defined from a table.

}