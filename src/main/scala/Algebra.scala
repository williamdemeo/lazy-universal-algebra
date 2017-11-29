
trait Algebra[T] {
  val universe : Set[T]
  val signature : List[Int]
  val operations : List[Operation[T]]
}


trait Operation[T] {
  val arity : Int
  def apply(arg : List[T]) : Option[T]
}

//case class Group(size : Int) extends Algebra[Int] {
//  val universe = 0 to size
//  val signature = List(2, 1, 0)
//
//
//}