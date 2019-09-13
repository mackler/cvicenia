package org.mackler.cvičenie

import Speaker.textToSpeech

object Generator {

  /** Convert markdown emphases to HTML */
  private def addEmphasis(s: String): String =
    s.replaceAll("""\*\*([^*]*)\*\*""", """<emphasis level="string">>$1</emphasis>""").
      replaceAll("""\*([^*]*)\*""", """<emphasis>$1</emphasis>""")

  /** Remove markdown emphases */
  private def removeEmphasis(s: String): String =
    s.replaceAll("""\*""", "")

  def apply(exerciseName: String) {
    val items: Seq[Exercise.Item] = Exercise read exerciseName
    val countWidth = (items.length - 1).toString.length // needed for zero-padded item numbers
    println(s"Processing ${items.length} items in exercise ${exerciseName}")

    (0 until items.length) foreach { itemNumber =>
      val item = items(itemNumber)

      val questionText = "<speak><prosody rate='slow'>" +
                         addEmphasis(item.question.mkString(" <break time='1s'/> ")) +
                         " </prosody></speak>"
      val answerText1 = "<speak><prosody rate='x-slow'> " + addEmphasis(item.answer) + " </prosody></speak>"
      val answerText2 = "<speak><prosody rate='slow'> " + removeEmphasis(item.answer) + " </speak>"

      val q: Array[Byte] = textToSpeech(questionText)
      val a1: Array[Byte] = textToSpeech(answerText1)
      val a2: Array[Byte] = textToSpeech(answerText2)
      val joined = Joiner.join(q, a1, a2)

      val itemName: String = exerciseName + "-" + s"%0${countWidth}d".format(itemNumber)
      Exercise.write(exerciseName, itemName, joined)
      joined.close()
    }
  }
}
