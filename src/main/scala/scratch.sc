import org.uacalc.alg.Malcev.isCongruenceDistIdempotent
import org.uacalc.ui.tm.ProgressReport
//import org.uacalc.alg.Malcev.cpIdempotent
//import org.uacalc.alg.Malcev.congruenceModularForIdempotent
import org.uacalc.alg.Malcev.isCongruenceModularIdempotent

import org.uacalc.io.AlgebraIO
import java.io._

object scratch {

  val home_dir = "git/TEAMS/UACalc"
  val algebra_dir = home_dir + "/api-examples/algebras"
  val output_dir = home_dir + "/api-examples/output"

  //writer.write("Hello Scala")
  //writer.close()
  val alg_file = algebra_dir+ "/CIB5-207.ua"
  val A = AlgebraIO.readAlgebraFile(alg_file)
  //val out_file = new PrintWriter(new File(output_dir + "A1-isCM.txt" ))
  if(isCongruenceDistIdempotent(A, new ProgressReport())) {
    println("--------------------------")
    println("YES!!!!!!!!!!!!!!!!!!!!!!!")
    println("--------------------------")
  }else {

    println("--------------------------")
    println("NO!!!!!!!!!!!!!!!!!!!!!!!!")
    println("--------------------------")

  }
  //isCongruenceModularIdempotent(A, new ProgressReport())

}
