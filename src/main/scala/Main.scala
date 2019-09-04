package org.mackler.cvičenie

import Speaker.textToSpeech
import java.io.{ FileOutputStream, OutputStream }

object Main extends App {
  val bell = """<audio src="https://storage.googleapis.com/org_mackler_sounds/rollout24k.mp3">
                  <break time="3s" />
                </audio>"""

  if (args.length < 1)
    System.err.println("need at least one exercise")
  else {
    val exerciseName: String = args(0)
    println(s"processing exercise ${exerciseName}")
    val items: Seq[Exercise.Item] = Exercise.read(exerciseName)
    println(s"there are ${items.length} items")
    (0 until items.length) foreach { itemNumber =>
      val item = items(itemNumber)
      val q: Array[Byte] =
        textToSpeech(s"<speak> ${bell} ${item.prompt} <break time='2s'/> ${item.question} </speak>")
      val a: Array[Byte] = textToSpeech(s"<speak> ${item.answer} </speak>")
      val joined = Joiner.join(q, a)
      val itemName: String = exerciseName + "-" + itemNumber
      Exercise.write(exerciseName, itemName, joined)
      joined.close()
      println(s"wrote $itemName")
    }
  }

  Speaker.closeClient()

}
