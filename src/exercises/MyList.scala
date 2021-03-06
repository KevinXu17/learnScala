package exercises

import sun.invoke.empty.Empty

abstract class MyList[+A] {
  /*
     head = first element of the list
     tail = remainder of the list
     isEmpty = is this list empty
     add(int) => new list with this element added
     toString => a string representation of the list
   */
  def head: A
  def tail: MyList[A]
  def isEmpty: Boolean
  def add[B >: A](element: B): MyList[B]  // lower bound to make sure all add is A or super type of A turn it to be B
  def printElements: String
  // polymorphic call
  override def toString: String = "[" + printElements + "]"

  def map[B](transfromer: A => B): MyList[B]
  def flatMap[B](transformer: A => MyList[B]) : MyList[B]
  def filter(predicate: A => Boolean): MyList[A]
  def ++[B >: A](list: MyList[B]): MyList[B]
}

case object Empty extends MyList[Nothing] {
  def head: Nothing = throw new NoSuchElementException

  def tail: MyList[Nothing] = throw new NoSuchElementException

  def isEmpty: Boolean = true

  def add[B >: Nothing](element: B): MyList[B] = new Cons(element, Empty)

  override def printElements: String = ""

  override def map[B](transfromer: Nothing => B): MyList[B] = Empty

  override def flatMap[B](transformer: Nothing => MyList[B]): MyList[B] = Empty

  override def filter(predicate: Nothing => Boolean): MyList[Nothing] = Empty

  override def ++[B >: Nothing](list: MyList[B]): MyList[B] = Empty
}

case class Cons[+A](h: A, t: MyList[A]) extends MyList[A] {
  def head: A = h
  def tail: MyList[A] = t
  def isEmpty: Boolean = false
  def add[B >: A](element: B): MyList[B] = new Cons(element, this)

  override def printElements: String = if (t.isEmpty) "" + h else h + " " + t.printElements

  override def map[B](transfromer: A =>  B): MyList[B] = {
    new Cons(transfromer(h), t.map(transfromer))
  }

  override def flatMap[B](transformer: A =>  MyList[B]): MyList[B] = {
    transformer(h) ++ t.flatMap(transformer)
  }

  override def filter(predicate: A => Boolean): MyList[A] = {
    if (predicate(h)) new Cons(h, t.filter(predicate)) else t.filter(predicate)
  }

  override def ++[B >: A](list: MyList[B]): MyList[B] = new Cons(h, t ++ list)
}

//trait MyPredicate[-T] {
//  def test(a: T): Boolean
//}


//trait Mytransformer[-A, B] {
//  def transform(a: A): B
//}

object ListTest extends App {
//  val list = new Cons(1, Empty)
//  println(list.head)
  val empty: MyList[Nothing] = Empty
  val emptyOfLOI: MyList[Int] = Empty
  val listOfIntegers: MyList[Int] = new Cons(1, new Cons(2, emptyOfLOI))      // new Cons(1, empty)
  val emptyOfLOS: MyList[String] = Empty
  val listOfStrings: MyList[String] = new Cons("a", new Cons("b", emptyOfLOS))  // new Cons("a", empty)

  println(listOfIntegers.map(new Function1[Int, Int] {
    override def apply(a: Int): Int = a * 2
  }))
  println(listOfIntegers.filter(new Function1[Int, Boolean] {
    override def apply(a: Int): Boolean = a % 2 == 0
  }))

}
