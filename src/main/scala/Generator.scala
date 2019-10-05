package org.mackler.cvičenie

import Exercise.Item
import Speaker.textToSpeech

class Generator(exerciseName: String, items: Seq[Item]) {
  import Generator._

  def generate() {
    val countWidth = (items.length - 1).toString.length // needed for zero-padded item numbers
    println(s"Processing ${items.length} items in exercise ${exerciseName}")

    (0 until items.length) foreach { itemNumber =>
      val item = items(itemNumber)

      val questionText = "<speak><prosody rate='slow'>" +
                          addEmphases(item.question.mkString(" <break time='1s'/> ")) +
                         " </prosody></speak>"
      val answerText1 = "<speak><prosody rate='x-slow'> " + addEmphases(item.answer) + " </prosody></speak>"

      val questionSpeech: Array[Byte] = textToSpeech(questionText)
      val answerSpeech1: Array[Byte] = textToSpeech(answerText1)

      val (joined, nameWithNumber) = {
        item.question.last.last match {
          case '?' => // if the question ends with a question mark, play the answer twice
            val answerText2 = "<speak><prosody rate='slow'> " + removeEmphases(item.answer) + " </speak>"
            val answerSpeech2: Array[Byte] = textToSpeech(answerText2)
            (Joiner.join(questionSpeech, answerSpeech1, answerSpeech2), true)
          case _ =>
            (Joiner.join(questionSpeech, answerSpeech1), false)
        }
      }

      val itemNumberString: String =
        if (nameWithNumber)
          s"%0${countWidth}d".format(itemNumber)
        else ""
      Exercise.write(exerciseName, itemNumberString, removePunctuation(item.answer), joined)
      joined.close()
    }
  }
}

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
    removeEmphases(s).replaceAll("""\.""", "").replaceAll("""<[^>]*>""", "")

}
