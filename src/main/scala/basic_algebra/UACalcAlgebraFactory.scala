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
    */
  def makeUACalcOperation(fn: List[Int] => Int,
                          op_name: String,
                          arity: Int,
                          algSize: Int
                         ): UACalcOperation = {
    val op: UACalcOperation = new AbstractOperation(op_name, arity, algSize) {
      override def valueAt(list: JavaList[_]): AnyRef = new NoSuchMethodException("!!!!!!!!sorry, no valueAt method!!!!!!")

      def intValueAt(list: JavaList[Int]): Int = fn(list.asScala.toList)
    }
    op
  }

}
