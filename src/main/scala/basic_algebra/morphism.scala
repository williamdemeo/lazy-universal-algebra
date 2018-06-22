package basic_algebra

//import Array._
//import scala.collection.JavaConverters._
//import basic_algebra.UACalcAlgebraFactory.UACalcOperation
//import basic_algebra.UACalcAlgebraFactory.makeUACalcOperationFromScalaTable
//import org.uacalc.alg.BasicAlgebra
//import scala.collection.immutable

object morphism {

  // type alias to point out that util.List is the Java representation of a list
  type JavaList[T] = java.util.List[T]
  val algSize = 4

  val unaryops: List[List[Int]]=
    (for {
      i <- 0 until algSize
      j <- 0 until algSize
      k <- 0 until algSize
      l <- 0 until algSize
    } yield List(i,j,k,l)).toList

  val bijections : List[List[Int]] = unaryops.filter(t => distinct(t))

  def distinct (t : List[Int]) : Boolean= t match {
    case List() => true
    case x :: xs => !xs.contains(x) && distinct(xs)
  }

  val opTable: Array[Array[Int]] = Array(Array(0,2,1,1), Array(0,1,3,2), Array(0,3,2,1), Array(1,2,1,3))

  // Check whether any of the bijections are endomorphisms of myAlg
  def isEndomorphism(f: List[Int], opTable: Array[Array[Int]]): Boolean ={
    // check f(a*b) = f(a)*f(b)
    val fTable: IndexedSeq[Int] = (0 until algSize).flatMap(i => (0 until algSize).map(j => f(opTable(i)(j))))
    val Tablef: IndexedSeq[Int] = (0 until algSize).flatMap(i => (0 until algSize).map(j => opTable(f(i))(f(j))))
    fTable.indices.forall(x => fTable(x) == Tablef(x))
  }

  //  val op: UACalcOperation = makeUACalcOperationFromScalaTable(opTable, op_name="bin", arity=2, algSize=4)
  //  val myAlg: BasicAlgebra = new BasicAlgebra("My Alg", 4, seqAsJavaList(List(op)))


  def main(args: Array[String]): Unit = {
    for {
      b <- bijections
      if isEndomorphism(b,opTable)
    } yield println(b)
  }

}
