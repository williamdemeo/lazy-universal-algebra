import org.uacalc.alg.op.AbstractOperation
import org.uacalc.alg.BasicAlgebra
//import scala.collection.JavaConverters.seqAsJavaList
import scala.collection.JavaConverters._
import org.uacalc.io.AlgebraIO
import org.uacalc.alg.Malcev.isCongruenceDistIdempotent
import org.uacalc.alg.Malcev.isCongruenceModularIdempotent
import org.uacalc.alg.Malcev.cubeTermBlockerIdempotent
import org.uacalc.ui.tm.ProgressReport
import basic_algebra.UACalcAlgebraFactory._

object AlgebraFactory {
//
//  // type alias since Scala has its own Operation class
//  type UACalcOperation = org.uacalc.alg.op.Operation
//
//  // type alias to point out that util.List is the Java representation of a list
//  type JavaList[T] = java.util.List[T]
//
//  /** makeUACalcOperation
//    * Return a subclass of AbstractOperation with intValueAt() defined using the function fn.
//    *
//    * @param fn      : the function used to define the intValueAt() method of the class.
//    * @param op_name : a string name for the class.
//    * @param arity   : the arity of the operation (must be 1 if fn is a list or tuple)
//    * @param algSize : the size of the universe of the algebra on which this operates.
//    */
//  def makeUACalcOperation(fn: List[Int] => Int,
//                          op_name: String,
//                          arity: Int,
//                          algSize: Int
//                         ): UACalcOperation = {
//   val op: UACalcOperation = new AbstractOperation(op_name, arity, algSize) {
//      override def valueAt(list: JavaList[_]): AnyRef = new NoSuchMethodException("!!!!!!!!sorry, no valueAt method!!!!!!")
//
//      def intValueAt(list: JavaList[Int]): Int = fn(list.asScala.toList)
//    }
//    op
//  }

  println("To use makeUACalcOperation, imitate the steps in the examples below.")

  println("EXAMPLE 1: a unary operation defined from a (Scala) function.")

  println("(1) Define a function, e.g., by specifying what it does at each point.")
  def plus_mod5(args: List[Int]): Int = {
    def plus_mod5_aux(xs: List[Int], acc: Int): Int = xs match {
      case Nil => acc % 5
      case y :: ys => plus_mod5_aux(ys, acc + y)
    }

    plus_mod5_aux(args, 0)
  }


  println("(2) Turn the function from (1) into various UACalcOperations")
  val op1: UACalcOperation = makeUACalcOperation(plus_mod5, "binaryPlusMod5", 2, 5)
  val op2: UACalcOperation = makeUACalcOperation(plus_mod5, "ternaryPlusMod5", 3, 5)

  //
  //println("Check that the op1([4,10]) works as expected")
  //val ans1: Int = op1.intValueAt(Array(4,10))
//  println("4 + 10 mod 5 = " + ans1.toString)

  //println("Check that the op2([4,10,1]) works as expected")
  //val ans2: Int = op2.intValueAt(Array(4, 10, 1))
//  println("4 + 10 + 1 mod 5 = ", ans2.toString)

  println("(3) Make a JavaList of the operations from (2) that you will want in the set of basic operations of your algebra.")
  val my_ops = seqAsJavaList(List(op1, op2))

  // (4) Use the JavaList of UACalcOperations from (2) to define a new BasicAlgebra.
  val myAlg: BasicAlgebra = new BasicAlgebra("My Alg", 5, my_ops)

  // EXAMPLE 2: a binary operation defined from a table.

}