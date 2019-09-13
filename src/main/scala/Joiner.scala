package org.mackler.cvičenie

import collection.JavaConverters._

import java.io.{ ByteArrayInputStream, SequenceInputStream }
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem.getAudioInputStream

object Joiner {

  def join(first: Array[Byte], second: Array[Byte], third: Array[Byte]): AudioInputStream = {
    val firstBais = new ByteArrayInputStream(first)
    val firstAis = getAudioInputStream(firstBais)
    val secondBais = new ByteArrayInputStream(second)
    val secondAis = getAudioInputStream(secondBais)
    val thirdBais = new ByteArrayInputStream(third)
    val thirdAis = getAudioInputStream(thirdBais)

    val secondFrameLength = secondAis.getFrameLength
    val thirdFrameLength = thirdAis.getFrameLength

    def silentWavBytes(ais: AudioInputStream): Array[Byte] = {
      val silenceByteLength = ais.getFrameLength * ais.getFormat.getFrameSize * 1.5
      first.take(44) ++ Array.fill[Byte](silenceByteLength.toInt)(0)
    }

    // this is the duraction of the answer spoken slowly
    val silenceBytes1 = silentWavBytes(secondAis)
    // this is the duraction of the answer repeated normally
    val silenceBytes2 = silentWavBytes(thirdAis)

    val silenceOne = getAudioInputStream(new ByteArrayInputStream(silenceBytes1))
    val silenceTwo = getAudioInputStream(new ByteArrayInputStream(silenceBytes1))
    val silenceThree = getAudioInputStream(new ByteArrayInputStream(silenceBytes2))
    val toBeJoined =
      Iterator(firstAis, silenceOne, secondAis, silenceTwo, thirdAis, silenceThree)

    val joined: AudioInputStream = new AudioInputStream(
      new SequenceInputStream(toBeJoined.asJavaEnumeration),
      firstAis.getFormat,
      firstAis.getFrameLength + (secondFrameLength * 4).toInt + (thirdFrameLength * 2.5).toInt
    )

    silenceThree.close()
    silenceTwo.close()
    silenceOne.close()
    thirdAis.close()
    secondAis.close()
    firstAis.close()
    thirdBais.close()
    secondBais.close()
    firstBais.close()

    joined
  }

}
