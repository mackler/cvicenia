package org.mackler.cvičenie

import Speaker.textToSpeech

object Generator {

  /** Convert markdown emphases to HTML */
  private def addEmphases(s: String): String =
    s.replaceAll("""\*\*([^*]*)\*\*""", """<emphasis level="string">>$1</emphasis>""").
      replaceAll("""\*([^*]*)\*""", """<emphasis>$1</emphasis>""")

  /** Remove markdown emphases */
  private def removeEmphases(s: String): String =
    s.replaceAll("""\*""", "")

  /** Remove periods and asterisks for use in filenames */
  private def removePunctuation(s: String): String =
    removeEmphases(s).replaceAll("""\.""", "")

  def apply(exerciseName: String) {
    val items: Seq[Exercise.Item] = Exercise read exerciseName
    val countWidth = (items.length - 1).toString.length // needed for zero-padded item numbers
    println(s"Processing ${items.length} items in exercise ${exerciseName}")

    (0 until items.length) foreach { itemNumber =>
      val item = items(itemNumber)

      val questionText = "<speak><prosody rate='slow'>" +
                         addEmphases(item.question.mkString(" <break time='1s'/> ")) +
                         " </prosody></speak>"
      val answerText1 = "<speak><prosody rate='x-slow'> " + addEmphases(item.answer) + " </prosody></speak>"
      val answerText2 = "<speak><prosody rate='slow'> " + removeEmphases(item.answer) + " </speak>"

      val q: Array[Byte] = textToSpeech(questionText)
      val a1: Array[Byte] = textToSpeech(answerText1)
      val a2: Array[Byte] = textToSpeech(answerText2)
      val joined = Joiner.join(q, a1, a2)

      val itemName: String = exerciseName + "-" + s"%0${countWidth}d".format(itemNumber)
      Exercise.write(exerciseName, itemName, removePunctuation(item.answer), joined)
      joined.close()
    }
  }
}
