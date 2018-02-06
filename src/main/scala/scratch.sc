import org.uacalc.alg.Malcev.isCongruenceDistIdempotent
import org.uacalc.ui.tm.ProgressReport
//import org.uacalc.alg.Malcev.cpIdempotent
//import org.uacalc.alg.Malcev.congruenceModularForIdempotent
import org.uacalc.alg.Malcev.isCongruenceModularIdempotent

import org.uacalc.io.AlgebraIO
import java.io._

object scratch {

//  val home_dir = "git/TEAMS/UACalc"
//  val algebra_dir = home_dir + "/api-examples/algebras"
//  val output_dir = home_dir + "/api-examples/output"
//
//  //writer.write("Hello Scala")
//  //writer.close()
//  val alg_file = algebra_dir+ "/CIB5-207.ua"
//  val A = AlgebraIO.readAlgebraFile(alg_file)
//  //val out_file = new PrintWriter(new File(output_dir + "A1-isCM.txt" ))
//  if(isCongruenceDistIdempotent(A, new ProgressReport())) {
//    println("--------------------------")
//    println("YES!!!!!!!!!!!!!!!!!!!!!!!")
//    println("--------------------------")
//  }else {
//
//    println("--------------------------")
//    println("NO!!!!!!!!!!!!!!!!!!!!!!!!")
//    println("--------------------------")
//
//  }
//  //isCongruenceModularIdempotent(A, new ProgressReport())

  def crossProduct(la: Array[Int]): Array[List[Int]] =
    la.flatMap(a => la.map(b => List(a,b)))

  def crossProduct(la: Array[Int], lb: Array[Int]): Array[List[Int]] =
    la.flatMap(a => lb.map(b => List(a,b)))

  def crossProduct2(la: Array[List[Int]], lb: Array[Int]) =
    la.flatMap(a => lb.map(b => b::a))

  val algSize = 5
  val baseList = (0 until algSize).toArray
  val blf = baseList.flatMap(a => Array(a+1))
  val cp2 = crossProduct(baseList)
  val cp3 = crossProduct2(cp2, baseList)
  val cp4 = crossProduct2(cp3, baseList)
  val cp5 = crossProduct2(cp4, baseList)
  assert(cp5.length==  math.pow(5,5))


}
