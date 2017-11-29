import org.uacalc.alg.Malcev.isCongruenceDistIdempotent
import org.uacalc.ui.tm.ProgressReport
//import org.uacalc.alg.Malcev.cpIdempotent
//import org.uacalc.alg.Malcev.congruenceModularForIdempotent
import org.uacalc.alg.Malcev.isCongruenceModularIdempotent
import org.uacalc.alg.Malcev.cubeTermBlockerIdempotent

import org.uacalc.io.AlgebraIO
import java.io.File
import java.io.PrintWriter

object CMexample {
  def main(args : Array[String]) {
    val home_dir = "/home/williamdemeo/git/TEAMS/UACalc"
    val algebra_dir = home_dir + "/api-examples/algebras"
    val output_dir = home_dir + "/api-examples/output"

    //writer.write("Hello Scala")
    //writer.close()
    val out_file = new PrintWriter(new File(output_dir + "/isCM.txt" ))
    out_file.write("Alg isCM  CTB\n")
    out_file.write("--- ----  ---------\n")
    (1 to 10).map{
      x => {
        val A = AlgebraIO.readAlgebraFile(algebra_dir + "/CIB5-" + x.toString + ".ua")
        val ctb = cubeTermBlockerIdempotent(A, new ProgressReport())
        out_file.write(" " + x.toString + "  " + isCongruenceModularIdempotent(A, new ProgressReport()).toString)
        try {
          out_file.write("  " + ctb.toString + "\n")
        } catch {
          case e : Exception => out_file.write("  " + "NONE\n")
        }
      }
    }
    out_file.close()
    //isCongruenceModularIdempotent(A, new ProgressReport())
  }

}

