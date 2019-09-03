package org.mackler.cvičenie

import java.io.{ FileOutputStream, OutputStream }

object Main extends App {
  val testText = """<speak>
                      <audio src="https://storage.googleapis.com/org_mackler_sounds/rollout24k.mp3">
                        nefunguje
                        <break time="3s"/>
                      </audio>
                      Ahoj svet!
                    </speak>"""

  if (args.length < 1)
    System.err.println("need at least one exercise")
  else {
    println(s"processing exercise ${args(0)}")
    val items = Exercise.read(args(0))
    items foreach {
      println
    }
  }

  Speaker.closeClient()

}
