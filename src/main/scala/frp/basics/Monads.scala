package frp.basics

import scala.util.{Failure, Success, Try}

object Monads:

  private def traditionalErrorHandling(): Unit = {
    try {
      for (s <- List("5", "2", "0", "x")) {
        val res = 10 / s.toInt
        println(s"'$s' => $res")
      }
    } catch {
      case e: Throwable => println(s"Exception: ${e.getMessage}")
    }
  }

  private def toInt(s: String): Try[Int] = Try {
    s.toInt
  }

  private def divide(a: Int, b: Int): Try[Int] = Try {
    a / b
  }

  private def monadCallbacks(): Unit = {
    println("-------------------------------------------------")
    for (s <- List("5", "2", "0", "x")) {
      val res = toInt(s)
      println(s"'$s' => $res")
    }

    println("-------------------------------------------------")
    for (s <- List("5", "2", "0", "x")) {
      val res: Unit = toInt(s).foreach(i => println(s"'$s' => $i"))
    }

    println("-------------------------------------------------")
    for (s <- List("5", "2", "0", "x")) {
      val res: Unit = toInt(s) match
        case Success(res) => println(s"'$s' => $res")
        case Failure(e) => println(s"'$s' => $e")
    }

    println("-------------------------------------------------")
    for (s <- List("5", "2", "0", "x")) {
      val res = toInt(s).flatMap(b => divide(10, b))
      println(s"'$s' => $res")
    }

    println("-------------------------------------------------")
    for (s <- List("5", "2", "0", "x")) {
      val res =
        for (
          b <- toInt(s);
          q <- divide(10, b)
        ) yield q

      println(s"'$s' => $res")
    }

    println("-------------------------------------------------")
    for ((s1, s2) <- Seq(("10", "5"), ("10", "0"), ("x", "5"), ("10", "2"))) {
      val res =
        toInt(s1) flatMap {
          a =>
            toInt(s2) flatMap {
              b => divide(a, b)
            }
        }
      println(s"($s1, $s2) => $res")
    }

    println("-------------------------------------------------")
    for ((s1, s2) <- Seq(("10", "5"), ("10", "0"), ("x", "5"), ("10", "2"))) {
      val res: Try[Int] = for {
        a <- toInt(s1)
        b <- toInt(s2)
        q <- divide(a, b)
      } yield q

      println(s"($s1, $s2) => $res")
    }
  }

  private def monadCombinators(): Unit = {}

  def main(args: Array[String]): Unit =
    println("======== traditionalErrorHandling ===========")
    traditionalErrorHandling()
    println()

    println("============= monadCallbacks ================")
    monadCallbacks()
    println()

    println("============ monadCombinators ===============")
    monadCombinators()
    println()
  end main

end Monads