package org.mackler.cvičenie

import util.{ Failure, Success }

import java.io.{ FileOutputStream, OutputStream }

object Main extends App {

  if (args.length < 1)
    System.err.println("need at least one exercise")
  else {
    (args map Exercise.read).
      foldLeft[Either[Seq[Throwable], Seq[Generator]]](Right(Seq.empty[Generator])) { case (acc, next) =>
        next match {
          case Success(gen) => acc match {
            case Right(gens) => Right(gens :+ gen)
            case e => e
          }
          case Failure(e) => acc match {
            case Left(es) => Left(es :+ e)
            case _ => Left(Seq(e))
          }
        }
      } match {
        case Left(exceptions) => exceptions foreach { e => System.err.println(e.getMessage) }
        case Right(exercises) => exercises foreach { _.generate() }
      }
  }

  Speaker.closeClient()

}
