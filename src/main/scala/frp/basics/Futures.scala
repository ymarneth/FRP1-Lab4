package frp.basics

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Random, Success}

given ExecutionContext = ExecutionContext.global

// noinspection Duplicates
object Futures:

  private def println(x: Any): Unit = Console.println(s"$x (thread id=${Thread.currentThread.threadId})")

  private def doWork(task: String, steps: Int): Unit =
    for (i <- 1 to steps)
      println(s"$task: $i")
      if (i == 6) throw new IllegalArgumentException()
      Thread.sleep(200)
  end doWork

  private def compute(task: String, n: Int): Int =
    for (i <- 1 to n)
      println(s"$task: $i")
      Thread.sleep(200)
    Random.nextInt(1000)
  end compute

  private def combine(value1: Int, value2: Int): Int =
    for (i <- 1 to 5)
      println(s"combine: $i")
      Thread.sleep(200)
    value1 + value2
  end combine

  private def combineAsync(value1: Int, value2: Int): Future[Int] =
    Future(combine(value1, value2))
  end combineAsync

  private def sequentialInvocation(): Unit = {
    doWork("task1", 5)
    doWork("task2", 5)
  }

  private def simpleFutures(): Unit = {
    val f1 = Future {
      doWork("task1", 5)
    }
    val f2 = Future {
      doWork("task2", 5)
    }

    Await.ready(f1, Duration.Inf) // 0 Punkte in der Klausur :)
    Await.ready(f2, Duration.Inf)
  }

  private def futuresWithCallback(): Unit = {
    val f1 = Future {
      compute("task1", 8)
    }
    val f2 = Future {
      compute("task2", 5)
    }

    f1.foreach(_ => println("task1 completed"))
    f2.foreach(_ => println("task2 completed"))
    f1.failed.foreach(e => println(s"task1 failed: $e"))

    f1.onComplete {
      case Success(value) => println(s"task1 completed with value $value")
      case Failure(exception) => println(s"task1 failed with exception $exception")
    }

    Await.ready(f1, Duration.Inf)
    Await.ready(f2, Duration.Inf)
    Thread.sleep(100) // 0 Punkte in der Klausur :)
  }

  private def futureComposition1(): Unit = {
    val f1 = Future {
      compute("task1", 5)
    }
    val f2 = Future {
      compute("task2", 5)
    }

    //val f3 = f1.flatMap(v1 => f2.map(v2 => combine(v1, v2)))

    val f3 =
      for {
        v1 <- f1
        v2 <- f2
      } yield combine(v1, v2)

    val done = f3 andThen {
      case Success(r) => println(s"result = $r")
    }

    Await.ready(done, Duration.Inf)
  }

  private def futureComposition2(): Unit = {
    val f1 = Future {
      compute("task1", 5)
    }
    val f2 = Future {
      compute("task2", 5)
    }

    val f3 = for {
      v1 <- f1
      v2 <- f2
      r <- combineAsync(v1, v2)
    } yield r
  }

  def main(args: Array[String]): Unit =
    println(s"availableProcessors=${Runtime.getRuntime.availableProcessors}")

    println("==== sequentialInvocation ====")
    sequentialInvocation()

    println("\n==== simpleFutures ====")
    simpleFutures()

    println("\n==== futuresWithCallback ====")
    futuresWithCallback()

    println("\n==== futureComposition1 ====")
    futureComposition1()

    println("\n==== futureComposition2 ====")
    futureComposition2()
  end main

end Futures

