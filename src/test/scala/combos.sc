import java.io.{File, PrintWriter}
import java.util

import org.uacalc.alg.SmallAlgebra
import org.uacalc.io.AlgebraIO
import org.uacalc.alg.Malcev.isCongruenceDistIdempotent
import org.uacalc.alg.Malcev.isCongruenceModularIdempotent
import org.uacalc.alg.Malcev.cubeTermBlockerIdempotent
import org.uacalc.ui.tm.ProgressReport
//import java.util.List

import scalaz.Alpha.A

object combos {

  //val myList = List("A", "C", "G")
  val vectorOfLists =
    for {
      i <- 0 to 2
      j <- 0 to 2
      k <- 0 to 2
    } yield List(i,j,k)

  val idempotentTables =
    for {
      i <- vectorOfLists
      j <- vectorOfLists
      k <- vectorOfLists
      if (i(0)==0 && j(1)==1 && k(2)==2)
    } yield List(i,j,k)

  // idempotentTables.length   // 729 = 3*3*3*3, as expected

  def writeListOfAlgebrasToFile(path: String, tables: IndexedSeq[List[List[Int]]]) {
    // get new filehandle for writing
    val fh = new PrintWriter(new File(path + "/IdempBinarsOn3.ua"))
    fh.write("<?xml version='1.0'?>\n")
    fh.write("<algebraList>\n")
    tables.zipWithIndex.foreach(p => writeAlgebraToFile(p._1, p._2, fh))
    fh.write("</algebraList>\n")
    fh.close()
  }
  def writeListOfAlgebrasToSeparateFiles(path: String, tables: IndexedSeq[List[List[Int]]]) {
    // get new filehandle for writing
    tables.zipWithIndex.foreach(p => {
      val fh = new PrintWriter(new File(path + "/IdempBin3-" + p._2 + ".ua"))
      fh.write("<?xml version='1.0'?>\n")
      fh.write("<algebraList>\n")
      writeAlgebraToFile(p._1, p._2, fh)
      fh.write("</algebraList>\n")
      fh.close()
    })
  }

  def writeAlgebraToFile(opTable: List[List[Int]], i: Int, fh: PrintWriter) = {
    val size = opTable(0).length
    fh.write("<algebra>\n")
    fh.write("  <basicAlgebra>\n")
    fh.write("    <algName>B"+i+"</algName>\n")
    fh.write("    <desc>idempotent binar of size 3</desc>\n")
    fh.write("    <cardinality>"+ size +"</cardinality>\n")
    fh.write("    <operations>\n")
    fh.write("      <op>\n")
    fh.write("        <opSymbol>\n")
    fh.write("          <opName>b</opName>\n")
    fh.write("          <arity>2</arity>\n")
    fh.write("        </opSymbol>\n")
    fh.write("        <opTable>\n")
    fh.write("          <intArray>\n")
    for (i <- 0 until size) {
      fh.write("            <row r=\"[" + i + "]\">")
      for (j <- 0 until size) {
        fh.write(opTable(i)(j) + ", ")
      }
      fh.write("</row>\n")
    }
    fh.write("          </intArray>\n")
    fh.write("        </opTable>\n")
    fh.write("      </op>\n")
    fh.write("  </operations>\n")
    fh.write("</basicAlgebra>\n")
    fh.write("</algebra>\n")
  }

  val path = "/home/williamdemeo/tmp"
  //writeListOfAlgebrasToSeparateFiles(path, idempotentTables)
  //  myList.flatMap(i => myList.map(j => myList.map(k => List(i,j,k))))
  //  val results_fh = new PrintWriter(new File(path + "/IdempBin3-Properties.ua"))
  //  for {
  //    i <- 0 until idempotentTables.length
  //  } yield {
  //    val A: SmallAlgebra = AlgebraIO.readAlgebraFile(path + "/IdempBin3-" + i + ".ua")
  //    results_fh.write(A.getName + "   " + isCongruenceDistIdempotent(A, new ProgressReport()).toString() + "\n")
  //  }
  //  results_fh.close()
  val isCD: IndexedSeq[Boolean] =
  for {
    i <- 0 until idempotentTables.length
  } yield isCongruenceDistIdempotent(AlgebraIO.readAlgebraFile(path + "/IdempBin3-" + i + ".ua"),new ProgressReport())
  println("CD: " + isCD.count(p => p))

  val isCM: IndexedSeq[Boolean] =
    for {
      i <- 0 until idempotentTables.length
    } yield isCongruenceModularIdempotent(AlgebraIO.readAlgebraFile(path + "/IdempBin3-" + i + ".ua"), new ProgressReport())
  println("CM: " + isCM.count(p => p))
  val CDCM = isCD.zip(isCM)

  println("CD or CM: " + CDCM.count(p => p._1 || p._2))

  println("CD and CM: " + CDCM.count(p => p._1 && p._2))

  println("CD - CM: " + CDCM.count(p => p._1 && !p._2))

  println("CM - CD: " + CDCM.count(p => !p._1 && p._2))



  val hasCTB: IndexedSeq[Boolean] =
    for {
      i <- 0 until idempotentTables.length
    } yield (cubeTermBlockerIdempotent(AlgebraIO.readAlgebraFile(path + "/IdempBin3-" + i + ".ua"), new ProgressReport())!=null)
  hasCTB.count(p => p)

  val CDCTB = isCD.zip(hasCTB)

  println("CD or CTB: " + CDCTB.count(p => p._1 || p._2))

  println("CD and CTB: " + CDCTB.count(p => p._1 && p._2))

  println("CD - CTB: " + CDCTB.count(p => p._1 && !p._2))

  println("CTB - CD: " + CDCTB.count(p => !p._1 && p._2))


  val CMCTB = isCM.zip(hasCTB)

  println("CM or CTB: " + CMCTB.count(p => p._1 || p._2))

  println("CM and CTB: " + CMCTB.count(p => p._1 && p._2))

  println("CM - CTB: " + CMCTB.count(p => p._1 && !p._2))

  println("CTB - CM: " + CMCTB.count(p => !p._1 && p._2))




  //  val results = idempotentTables.zipWithIndex.filter(p => (!isCD(p._2) && isCM(p._2) && hasCTB(p._2))).map(p => p._2)
//  results.length
}