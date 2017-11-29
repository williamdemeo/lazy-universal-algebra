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
  println("Example 2: Constructing an algebra with unary operations defined 'by hand' as lists.")
  println("The algebra will have universe {0, 1, ..., 7}, and three operations.")

  println("---- (1) Construc some unary functions on {0, ..., 7} using Scala List[Int] type.")
  val f0 = List(7, 6, 6, 7, 3, 2, 2, 3)
  val f1 = List(0, 1, 1, 0, 4, 5, 5, 4)
  val f2 = List(0, 2, 3, 1, 0, 2, 3, 1)

  println("---- (2) Use the functions defined in (1) to construct UACalc operations.")
  // makeUACalcOperationFromScalaList(fn: List[Int], name: String, arity: Int, algSize: Int)
  val opf0 = makeUACalcOperationFromScalaList(f0, "f0", 1, 8)
  val opf1 = makeUACalcOperationFromScalaList(f1, "f1", 1, 8)
  val opf2 = makeUACalcOperationFromScalaList(f2, "f2", 1, 8)

  println("---- (3) Check that opf0(i) gives expected results for various i ----")
  println("opf0.intValueAt(0) = 7 =?= " + opf0.intValueAt(0))
  println("opf0.intValueAt(2) = 6 =?= " + opf0.intValueAt(2))
  println("opf0.intValueAt(5) = 2 =?= " + opf0.intValueAt(5))
  println("opf0.intValueAt(7) = 3 =?= " + opf0.intValueAt(7))

  println("---- (4) Make a JavaList of the operations you want as the basic operations of your algebra. ----")
  val my_opsf = seqAsJavaList(List(opf0, opf1, opf2))

  println("---- (5) Construct the algebra. ----")
  val myAlg2: BasicAlgebra = new BasicAlgebra("My 2nd Alg", 8, my_opsf)

  println("---- (6)(optional) Sanity check: we actually constructed something. ----")
  println("myAlg2.getName() = " + myAlg2.getName())
  println("myAlg2.universe() = " + myAlg2.universe())

  // For some reason writing to file doesn't work for this example.
  // It works for Example 1 though.)
  // println("---- (7)(optional) write the algebra to a UACalc file ----")
  // AlgebraIO.writeAlgebraFile(myAlg2, "Example2_UACalcAlgebraFromScalaLists.ua")

}

//  # The above is nice and easy, but the following is more general
//  # and useful if you want to define many operations:
//  # Suppose all the operations are stored as a list of lists, as in:
//    fns = f0, f1, f2
//  # Build the list of operations by looping through fns:
//    ops = []
//  for i in range(len(fns)):
//    ops.append(Operation(fns[i], "f"+str(i), 1, 8))
//
//
//  # Next we check that the operations are as expected:
//    print "The unary operations are:"
//  for i in range(len(ops)):
//    print "   " + ops[i].symbol().name() + ":",
//  for j in range(8):
//    print ops[i].intValueAt([j]),
//  print " "
//
//
//  # Finally, construct the algebra:
//    alg = BasicAlgebra("MyUnaryAlgebra", 8, ops)
//
//  print "\nQuick check that we constructed an algebra:"
//  print "   alg.getName() = ", alg.getName()
//  print "   alg.universe() = ", alg.universe()
//
//  if os.path.exists("../Algebras"):
//    fqname2 = "../Algebras/Example2_MutliunaryAlgebra.ua"
//  # Optionally, write the algebra to a UACalc file that can be loaded into the gui.
//  AlgebraIO.writeAlgebraFile(alg, fqname2)
//  print "UACalc algebra file created:", fqname2
//
//
//
//  print "\n\n---- Example 3 ----"
//  print "Constructing an algebra with unary operations defined 'by hand' as vectors."
//  print "Quick and dirty method: write to a .ua file, then read with AlgebraIO"
//
//  def write_unaryalgebra(ops, fqname):
//  '''Write a .ua file representing a multiunary algebra with operations given
//    by the vectors in the argument ops.'''
//  outfile = open(fqname, 'w')  # open file for appending (might change this to 'w')
//  outfile.write("<?xml version=\"1.0\"?>\n")
//  outfile.write("<algebra>\n<basicAlgebra>\n<algName>"+name+"</algName>\n")
//  outfile.write("<cardinality>"+ str(len(ops[0]))+"</cardinality>\n")
//  outfile.write("<operations>\n")
//  opcount=0
//
//  for op in ops:
//    outfile.write("<op>\n<opSymbol>\n<opName>op"+str(opcount)+"</opName>\n")
//  outfile.write("<arity>1</arity>\n</opSymbol>\n<opTable>\n<intArray>\n<row>")
//  for x in range(len(op)-1):
//    outfile.write(str(op[x])+",")
//  outfile.write(str(op[-1]))
//  outfile.write("</row>\n</intArray>\n</opTable>\n</op>\n")
//  opcount=opcount+1;
//  outfile.write("</operations>\n</basicAlgebra>\n</algebra>\n")
//  outfile.close()
//
//  # The algebra will have universe {0, 1, ..., 7}, and the following unary operations:
//    f0 = 7,6,6,7,3,2,2,3
//  f1 = 0,1,1,0,4,5,5,4
//  f2 = 0,2,3,1,0,2,3,1
//  ff = f0, f1, f2
//
//  name = "Example3"
//  if os.path.exists("../Algebras"):
//    fqname3 = '../Algebras/'+name+'.ua'
//  else:
//  fqname3 = name+'.ua'
//
//  print "Writing algebra to file ", fqname3
//  write_unaryalgebra(ff, fqname3)
//
//
//  alg = AlgebraIO.readAlgebraFile(fqname3)
//
//  print "\nQuick check that we constructed an algebra:"
//  print "   alg.getName() = ", alg.getName()
//  print "   alg.universe() = ", alg.universe()
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


