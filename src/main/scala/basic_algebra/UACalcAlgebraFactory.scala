package basic_algebra



import org.uacalc.alg.op.AbstractOperation

import scala.collection.JavaConverters._

//class UACalcAlgebraFactory {}

object UACalcAlgebraFactory {

  // type alias since Scala has its own Operation class
  type UACalcOperation = org.uacalc.alg.op.Operation

  // type alias to point out that util.List is the Java representation of a list
  type JavaList[T] = java.util.List[T]


  /** makeUACalcOperation
    * Return a subclass of AbstractOperation with intValueAt() defined using the function fn.
    *
    * @param fn      : the function used to define the intValueAt() method of the class.
    * @param op_name : a string name for the class.
    * @param arity   : the arity of the operation (must be 1 if fn is a list or tuple)
    * @param algSize : the size of the universe of the algebra on which this operates.
    *
    * @example       :
    *
    *  (1) Define a scala function, e.g., addition mod 5.
    *  ```scala
    *    def plus_mod5(args: List[Int]): Int = {
    *      def plus_mod5_aux(xs: List[Int], acc: Int): Int = xs match {
    *        case Nil => acc % 5
    *        case y :: ys => plus_mod5_aux(ys, acc + y)
    *      }
    *      plus_mod5_aux(args, 0)
    *    }
    *  ```
    *
    *  (2) Use the operation defined in (1) to make some UACalcOperations.
    *
    *  ```scala
    *  val op1: UACalcOperation = makeUACalcOperation(plus_mod5, "binaryPlusMod5", 2, 5)
    *  val op2: UACalcOperation = makeUACalcOperation(plus_mod5, "ternaryPlusMod5", 3, 5)
    *  ```
    *
    *  (3) Check that the op1([4,10]) and op2([4,10, 1]) work as expected.
    *
    *  ```scala
    *  val ans1: Int = op1.intValueAt(Array(4,10))
    *  println("4 + 10 mod 5 = " + ans1.toString)
    *  val ans2: Int = op2.intValueAt(Array(4,10,1))
    *  println("4 + 10 + 1 mod 5 = " + ans2.toString)
    *  ```
    *
    *  (4) Make a JavaList of the operations from (2) that you want for the basic operations of your algebra.
    *
    *  ```scala
    *  val my_ops = seqAsJavaList(List(op1, op2))
    *  ```
    *
    *  (5) Build a UACalcAlgebra.
    *
    *  ```scala
    *  val myAlg: BasicAlgebra = new BasicAlgebra("My Alg", 5, my_ops)
    *  ```
    */
  def makeUACalcOperationFromScalaFunction(fn      : List[Int] => Int,
                                           op_name : String,
                                           arity   : Int,
                                           algSize : Int ): UACalcOperation =
    new AbstractOperation(op_name, arity, algSize) {
      override def intValueAt(args: Array[Int]): Int = fn(args.toList)
      override def valueAt(list: JavaList[_]): AnyRef = ???
    }

}