package org.mackler.cvičenie

import collection.JavaConverters._

import java.io.{ ByteArrayInputStream, SequenceInputStream }
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem.getAudioInputStream

object Joiner {

  /** Duration of silence relative to speech it follows. */
  val silenceRatio = 1.2


  def join(first: Array[Byte], second: Array[Byte], third: Array[Byte] = Array.empty[Byte]): AudioInputStream = {

    def silentWavBytes(ais: AudioInputStream): Array[Byte] = {
      val silenceByteLength = ais.getFrameLength * ais.getFormat.getFrameSize * silenceRatio
      first.take(44) ++ Array.fill[Byte](silenceByteLength.toInt)(0)
    }

    val firstBais = new ByteArrayInputStream(first)
    val firstAis = getAudioInputStream(firstBais)
    val secondBais = new ByteArrayInputStream(second)
    val secondAis = getAudioInputStream(secondBais)

    // this is the duraction of the answer spoken slowly
    val silenceBytes1 = silentWavBytes(secondAis)

    val silenceOne = getAudioInputStream(new ByteArrayInputStream(silenceBytes1))
    val silenceTwo = getAudioInputStream(new ByteArrayInputStream(silenceBytes1))

    val answeredOnceSpeech = Iterator(firstAis, silenceOne, secondAis, silenceTwo)
    val answeredOnceLength: Long = firstAis.getFrameLength +
        (secondAis.getFrameLength * (1 + 2 * silenceRatio)).toInt

    val joined: AudioInputStream = if (third.length == 0)
      new AudioInputStream(
        new SequenceInputStream(answeredOnceSpeech.asJavaEnumeration),
        firstAis.getFormat,
        answeredOnceLength
      )
    else { // we repeat the answer a second time at normal speed
      val thirdBais = new ByteArrayInputStream(third)
      val thirdAis = getAudioInputStream(thirdBais)
      val thirdFrameLength = thirdAis.getFrameLength
      val silenceBytes2 = silentWavBytes(thirdAis)
      val silenceThree = getAudioInputStream(new ByteArrayInputStream(silenceBytes2))

      val j = new AudioInputStream(
        new SequenceInputStream((answeredOnceSpeech ++ Iterator(thirdAis, silenceThree)).asJavaEnumeration),
        firstAis.getFormat,
        answeredOnceLength + (thirdFrameLength * (1 + silenceRatio)).toInt
      )

      silenceThree.close()
      thirdAis.close()
      thirdBais.close()

      j
    }

    silenceTwo.close()
    silenceOne.close()
    secondAis.close()
    firstAis.close()
    secondBais.close()
    firstBais.close()

    joined
  }

}
