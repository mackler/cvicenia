package org.mackler.cvičenie

import scala.io.Source

object Exercise {

  case class Item(prompt: String, question: String, answer: String)

  def read(name: String): Seq[Item] = {
    Source.fromResource(name + ".tsv").getLines.toSeq.filter(_(0) != '#').
      map { i =>
        val elems = i.split('\t')
        Item(elems(0), elems(1), elems(2))
      }
  }

}
