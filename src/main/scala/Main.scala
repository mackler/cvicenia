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

  // TODO: If there are exceptions in both the try-block and the
  // finally-block, then the latter will “overthrow” the former and
  // that exception will be lost.
  // Search StackOverflow for "scala try-with-resources" for
  // workarounds.

  try {
    val outputFilename = "output.mp3"
    val out: OutputStream = new FileOutputStream(outputFilename)
    // TODO: see comment above regarding try-with-resources in Scala.
    try {
      out.write(Speaker.textToSpeech(testText))
      println(s"Audio content written to file $outputFilename")
    } finally {
      out.close()
    }

  } catch {
    case t: Throwable => println(s"Fail: ${t.getMessage}")
  } finally {
    Speaker.closeClient()
  }

  println()
}
