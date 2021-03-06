import org.uacalc.alg.BasicAlgebra

import scala.collection.JavaConverters._
import basic_algebra.UACalcAlgebraFactory._
import org.uacalc.io.AlgebraIO
import basic_algebra.UACalcAlgebraFactory._
//import scala.collection.JavaConverters.seqAsJavaList
import org.uacalc.io.AlgebraIO
import org.uacalc.alg.Malcev.isCongruenceDistIdempotent
import org.uacalc.alg.Malcev.isCongruenceModularIdempotent
import org.uacalc.alg.Malcev.cubeTermBlockerIdempotent
import org.uacalc.ui.tm.ProgressReport

object AlgebraFactory {

  // Examples/Tests of the UACalcAlgebraFactory class/object.

  println("=============================================================")
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
  val ans1: Int = op1.intValueAt(Array(4, 10))
  println("4 + 10 mod 5 = 4 =?= " + ans1.toString)
  val ans2: Int = op2.intValueAt(Array(4, 10, 1))
  println("4 + 10 + 1 mod 5 = 0 =?= " + ans2.toString)

  println("---- (4) Make a JavaList of the operations you want as the basic operations of your algebra. ----")
  val my_ops = seqAsJavaList(List(op1, op2))

  println("---- (5) Construct the algebra. ----")
  val myAlg: BasicAlgebra = new BasicAlgebra("My 1st Alg", 5, my_ops)

  println("---- (6)(optional) Sanity check: we actually constructed something. ----")
  println("myAlg.getName() = " + myAlg.getName())
  println("myAlg.universe() = " + myAlg.universe())

  println("---- (7)(optional) write the algebra to a UACalc file ----")
  AlgebraIO.writeAlgebraFile(myAlg, "Example1_UACalcAlgebraFromScalaFunctions.ua")




  println("====================================================================================")
  println("EXAMPLE 2: Constructing an algebra with unary operations defined 'by hand' as lists.")
  println("The algebra will have universe {0, 1, ..., 7}, and three operations.")

  println("---- (1) Construc some unary functions on {0, ..., 7} using Scala List[Int] type.")
  val f0 = List(7, 6, 6, 7, 3, 2, 2, 3)
  val f1 = List(0, 1, 1, 0, 4, 5, 5, 4)
  val f2 = List(0, 2, 3, 1, 0, 2, 3, 1)
  val funcs = List(f0, f1, f2)


  println("---- (2) Use the functions defined in (1) to construct UACalc operations.")
  val zfns = funcs.zipWithIndex
  val ops: List[UACalcOperation] =
    zfns.map{case (f,i) => makeUACalcOperationFromScalaList(f, "f" + i, 1, 8)}

  println("---- (3) Check that the opf have the expected operation tables ----")
  funcs.zip(ops).map{ case (f, op) => assert(verifyOp(f, op), "unexpected op values") }

  def verifyOp(f: List[Int], op: UACalcOperation) : Boolean =
    f.indices.forall(i => op.intValueAt(i) == f(i))

  println("---- (4) Make a JavaList of the operations you want as the basic operations of your algebra. ----")
  val java_ops = seqAsJavaList(ops)

  println("---- (5) Construct the algebra. ----")
  val myAlg2: BasicAlgebra = new BasicAlgebra("My 2nd Alg", 8, java_ops)

  println("---- (6)(optional) Sanity check: we actually constructed something. ----")
  println("myAlg2.getName() = " + myAlg2.getName())
  println("myAlg2.universe() = " + myAlg2.universe())

  // For some reason writing to file doesn't work for this example.
  // It works for Example 1 though.)
  //println("---- (7)(optional) write the algebra to a UACalc file ----")
  //AlgebraIO.writeAlgebraFile(myAlg2, "Example2_UACalcAlgebraFromScalaLists.ua")


  println("==================================================")
  println("EXAMPLE 3: Generating all 4-element idempotent groupoids.")
  val arity2 = 2
  val algSize4 = 4

  println("\n(1) Form the stream of all quadruples with values in {0,1,2,3}.")
  lazy val streamOfArrays =
    ( for {
      i <- 0 until algSize4
      j <- 0 until algSize4
      k <- 0 until algSize4
      l <- 0 until algSize4
    } yield Array(i,j,k,l) ).toStream

  println("\n(2) Form the sequence of binary op tables, filtering out non-idempotent ones.")
  lazy val idempotentTables: Stream[Array[Array[Int]]] =
    for {
      i <- streamOfArrays
      j <- streamOfArrays
      k <- streamOfArrays
      l <- streamOfArrays
      if i(0)==0 && j(1)==1 && k(2)==2 && l(3) == 3
    } yield Array(i,j,k,l)

  println("\n(3) Form sequence of algebras with tables from (2).")
  lazy val UACalcGroupoidStream: Stream[BasicAlgebra] =
    for (ti <- idempotentTables.zipWithIndex) yield {
      new BasicAlgebra(
        "Grpoid-"+ti._2,
        algSize4,
        seqAsJavaList(List(makeUACalcOperationFromScalaTable(ti._1, "*", arity2, algSize4)))
      )
    }

  println("---- (4) Sanity check: we actually constructed something.")
  println("   UACalcGroupoidStream(0).getName() = " + UACalcGroupoidStream.head.getName())
  println("   UACalcGroupoidStream(0).universe = " + UACalcGroupoidStream.head.universe())

  val algebrasWithProps = UACalcGroupoidStream.filter(A =>
    isCongruenceModularIdempotent(A, new ProgressReport()) &&
      !isCongruenceDistIdempotent(A, new ProgressReport()) &&
     cubeTermBlockerIdempotent(A, new ProgressReport()) != null
  ).take(2)
  //val fourAlgsWithProps = algebrasWithProps.take(4)

  println("---- (5) Check size to see if we got one or two ----")

  println("algebrasWithProps.length = ", algebrasWithProps.length)

  println("---- (6) writing algebras to a UACalc files ----")
  algebrasWithProps.map { alg =>
    println("   alg.getName() = " + alg.getName())
    AlgebraIO.writeAlgebraFile(alg, alg.getName() + ".ua")
  }



  def genPerms(algSize: Int): List[List[Int]] = {

    def genPerms_aux(counter: Int, acc: List[List[Int]]): List[List[Int]] = {
      if (counter == 0) acc
      else genPerms_aux(counter - 1, acc.flatMap(l => (0 until algSize).map(j => j :: l)))
    }

    genPerms_aux(algSize, List((0 until algSize).toList))

  }
  val testperms = genPerms(3)

}

//
//
//
//
//
//
//
//  print "\n\n---- Example 4 ----"
//  print "The congruence lattice of a congruence lattice."
//  print "\nThe congruence lattice Con(A) of the algebra A is itself an algebra (specifically, a lattice)."
//  print "We represent it in UACalc as an object of the class BasicLattice.  UACalc represents the"
//  print "universe of an algebra of cardinality n with integers {0, 1, ..., n-1}, and in some cases"
//  print "it is important to know to what elements these integers correspond."
//
//  print "\nFor example, suppose we read in Polin's algebra from the file polin.ua, and name this algebra P,"
//  P = AlgebraIO.readAlgebraFile("../Algebras/polin.ua")
//
//  print "and suppose we then construct an algebra that is the congruence lattice of P (using the convenient"
//  print "and fast UACalc con() method), and call this algebra conP."
//  conP = BasicLattice("conP", P.con(), 0)
//
//  print "\nWe can print the universe of conP with"
//  print "\n    conP.universe(): ", conP.universe()
//
//  print "\nNow suppose we want the congruence lattice of the algebra conP.  "
//  print "\n    conP.con().universe() gives ", conP.con().universe()
//
//  print "\nTo make use of this result, we must know to which elements of conP.universe() the"
//  print "integers appearing in the blocks of conP.con().universe() correspond."
//
//  n = conP.cardinality()
//  print "\nThe elements in the universe of conP happen to be labeled in UACalc as follows:"
//  print "\n    ( k, conP.getElement(k) ):\n   ",
//  for k in range(n):
//    print "( "+str(k)+",", conP.getElement(k), "), ",
//
//  print "\n\nPrinting the universe of conP..."
//  print "   ...with conP.universe():", conP.universe(), "   (lists elements in arbitrary order)"
//  print "   ...with conP.getUniverseList():", conP.getUniverseList(), "   (lists elements in correct order)"
//
//  print "\nSo, when you want to know how UACalc is labelling the elements,"
//  print "use either getUniverseList() or getElement(k), instead of universe()."
//


