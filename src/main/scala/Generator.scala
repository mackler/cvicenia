package org.mackler.cvičenie

import Speaker.textToSpeech

object Generator {

  val bell = """<audio src="https://storage.googleapis.com/org_mackler_sounds/rollout.mp3">
                  <break time="3s" />
                </audio>"""

  def apply(exerciseName: String) {
    println(s"processing exercise ${exerciseName}")
    val items: Seq[Exercise.Item] = Exercise.read(exerciseName)
    val countWidth = (items.length - 1).toString.length
    println(s"there are ${items.length} items")
    (0 until items.length) foreach { itemNumber =>
      val item = items(itemNumber)
      val q: Array[Byte] =
        textToSpeech(s"<speak> ${bell} ${item.prompt} <break time='2s'/> ${item.question} </speak>")
      val a: Array[Byte] = textToSpeech(s"<speak> ${item.answer} </speak>")
      val joined = Joiner.join(q, a)
      val itemName: String = exerciseName + "-" + s"%0${countWidth}d".format(itemNumber)
      Exercise.write(exerciseName, itemName, joined)
      joined.close()
    }
  }
}
