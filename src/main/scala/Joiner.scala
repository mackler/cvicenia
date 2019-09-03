package org.mackler.cvičenie

import collection.JavaConverters._

import java.io.{ ByteArrayInputStream, SequenceInputStream }
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem.getAudioInputStream

object Joiner {

  def join(first: Array[Byte], second: Array[Byte]): AudioInputStream = {
    val firstBais = new ByteArrayInputStream(first)
    val secondBais = new ByteArrayInputStream(second)
    val firstAis = getAudioInputStream(firstBais)
    val secondAisOne = getAudioInputStream(secondBais)
    val secondAisTwo = getAudioInputStream(new ByteArrayInputStream(second))

    val secondFrameLength = secondAisOne.getFrameLength
    val silenceByteLength = secondFrameLength * secondAisOne.getFormat.getFrameSize * 1.5
    val silentWavBytes: Array[Byte] =
      first.take(44) ++ Array.fill[Byte](silenceByteLength.toInt)(0)
    val silenceOne = getAudioInputStream(new ByteArrayInputStream(silentWavBytes))
    val silenceTwo = getAudioInputStream(new ByteArrayInputStream(silentWavBytes))
    val silenceThree = getAudioInputStream(new ByteArrayInputStream(silentWavBytes))
    val toBeJoined =
      Iterator(firstAis, silenceOne, secondAisOne, silenceTwo, secondAisTwo, silenceThree)

    val joined: AudioInputStream = new AudioInputStream(
      new SequenceInputStream(toBeJoined.asJavaEnumeration),
      firstAis.getFormat,
      firstAis.getFrameLength + (secondFrameLength * 6.5).toInt
    )
    silenceThree.close()
    silenceTwo.close()
    silenceOne.close()
    secondAisOne.close()
    secondAisTwo.close()
    firstAis.close()
    secondBais.close()
    firstBais.close()
    joined
  }

}
