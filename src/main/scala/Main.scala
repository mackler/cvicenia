package org.mackler.cvičenie

import java.io.{ FileOutputStream, OutputStream }

object Main extends App {

  if (args.length < 1)
    System.err.println("need at least one exercise")
  else {
    args foreach Generator.apply
//    val exerciseName: String = args(0)
  }

  Speaker.closeClient()

}
