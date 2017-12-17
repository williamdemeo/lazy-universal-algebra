package generator

import scala.util._

trait Tree

case class Inner(left: Tree, right: Tree) extends Tree

case class Leaf(x: Int) extends Tree


object generators {

  trait Generator[+T] {
    self =>

    def generate: T

    def map[S](f: T => S): Generator[S] =
      new Generator[S] {
        def generate = f(self.generate)
      }

    def flatMap[S](f: T => Generator[S]): Generator[S] =
      new Generator[S] {
        def generate = f(self.generate).generate
      }
  }

  val integers = new Generator[Int] {
    def generate = Random.nextInt()
  }

  //  val boolean_first_try = new Generator[Boolean] {
  //    def generate = (integers.generate > 0)
  //  }
  val boolean = integers.map(_ >= 0)

  def pairs[T, U](t: Generator[T], u: Generator[U]) = new Generator[(T, U)] {
    def generate = (t.generate, u.generate)
  } // this requires generators come with map and flatMap

  def single[T](x: T): Generator[T] = new Generator[T] {
    def generate = x
  }

  def choose(lo: Int, hi: Int): Generator[Int] =
    for (x <- integers) yield lo + x % (hi - lo)

  def oneOf[T](xs: T*): Generator[T] =
    for (i <- choose(0,xs.length)) yield xs(i)

  def lists: Generator[List[Int]] = for {
    isEmpty <- boolean
    list <- if(isEmpty) emptyLists else nonEmptyLists
  } yield list

  def emptyLists = single(Nil)
  def nonEmptyLists = for {
    head <- integers
    tail <- lists
  } yield head :: tail

  def trees: Generator[Tree] = for {
    isLeaf <- boolean
    tree <- if (isLeaf) leaves else inners
  } yield tree

  def leaves: Generator[Leaf] = for {
    i <- integers
  } yield Leaf(i)

  def inners: Generator[Inner] = for {
    l <- trees
    r <- trees
  } yield new Inner(l, l)

}


  // To generate random booleans, we want to be able to write
  // val booleans = for (i <- integers) yield i > 0

